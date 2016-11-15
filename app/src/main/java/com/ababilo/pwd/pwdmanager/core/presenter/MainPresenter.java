package com.ababilo.pwd.pwdmanager.core.presenter;

import android.util.Log;

import com.ababilo.pwd.pwdmanager.App;
import com.ababilo.pwd.pwdmanager.core.view.MainView;
import com.ababilo.pwd.pwdmanager.model.Password;
import com.ababilo.pwd.pwdmanager.service.protocol.OnResponseReceived;
import com.ababilo.pwd.pwdmanager.service.protocol.ProtocolService;
import com.arellomobile.mvp.InjectViewState;

import java.util.Arrays;

import javax.inject.Inject;

/**
 * Created by ababilo on 12.11.16.
 */

@InjectViewState
public class MainPresenter extends BasePresenter<MainView> {

    @Inject
    ProtocolService protocolService;

    public MainPresenter() {
        App.getPresenterComponent().inject(this);
    }

    public void sendPassword(Password password) {
        if (null == password) {
            // error
        }

        getViewState().startSending();
        protocolService.sendPassword(password)
                .subscribe(
                        none -> {},
                        th -> {}
                );
    }

    public void connectDevice(String mac) {
        if (null == mac) {
            getViewState().onDeviceNotConnected();
            return;
        }
        protocolService.connect(mac, new OnResponseReceived() {
            @Override
            public void onPongReceived() {
                getViewState().onDeviceConnected();
            }

            @Override
            public void onResponseReceived(byte[] data) {
                // todo process
                Log.i("BT Response", Arrays.toString(data));
            }

            @Override
            public void onUnknownReceived() {
                Log.w("DEVICE", "Unknown response");
            }
        }).flatMap(none -> protocolService.sendPing()).subscribe();
    }
}
