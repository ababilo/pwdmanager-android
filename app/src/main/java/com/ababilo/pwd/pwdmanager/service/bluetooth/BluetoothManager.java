package com.ababilo.pwd.pwdmanager.service.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ababilo on 14.11.16.
 */

public class BluetoothManager {

    // SPP UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothAdapter adapter;
    private ExecutorService executor = Executors.newFixedThreadPool(1);
    private volatile boolean connected = false;
    private BluetoothSocket socket;
    private InputStream in;
    private OutputStream out;

    public BluetoothManager() {
        adapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void enable() {
        if (!adapter.isEnabled()) {
            adapter.enable();
        }
    }

    public void disable() {
        if (adapter.isEnabled()) {
            adapter.disable();
        }
    }

    public void connect(String mac, BluetoothObserver observer) {
        if (!adapter.isEnabled()) {
            throw new IllegalStateException("BT must be enabled");
        }

        BluetoothDevice device = adapter.getRemoteDevice(mac);
        try {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
            adapter.cancelDiscovery();
            socket.connect();

            in = socket.getInputStream();
            out = socket.getOutputStream();

            executor.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    boolean dataAvailable = false;
                    //check if data available
                    try {
                        if (in == null) {
                            break;
                        }
                        dataAvailable = in.available() > 0;

                        //start receiving if available
                        if (dataAvailable) {
                            observer.onDataReceive(IOUtils.toByteArray(in, in.available()));
                            if (Thread.currentThread().isInterrupted()) {
                                return;
                            }
                        }
                    } catch (Exception e) {
                        observer.onReceiveError(e);
                    }
                }
            });

            connected = true;
        } catch (IOException e) {
            connected = false;
            observer.onConnectError(e);
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    public void disconnect() {
        executor.shutdownNow();
        executor = Executors.newFixedThreadPool(1);
        if (!connected)
            return;
        try {
            //out.write(lowLevelProtocol.prepareDisconnectMessage());
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            Log.e("BT", "Error on stream flush", e);
        }
        try {
            socket.close();
        } catch (IOException e) {
            Log.e("BT", "Error on socket close", e);
        }
        connected = false;
    }

    public synchronized void sendData(byte[] data) throws IOException {
        try {
            out.write(data);
            out.flush();
        } catch (IOException e) {
            Log.e("BT", "Error on data send", e);
            throw e;
        }
    }

    public boolean isConnected() {
        return connected;
    }
}
