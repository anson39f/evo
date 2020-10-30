package com.xds.project.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.xds.project.app.Constant;
import com.xds.project.ui.activity.LockActivity;

/**
 * @author XWG
 * @todo
 * @date 2020/10/30.
 */
public class ScreenListenerService extends Service {
    TelephonyManager telephonyManager;
    private boolean flag;

    public ScreenListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        if (!flag) {
            registerReceiver(receiver, intentFilter);
            flag = true;
        }
//        listenTelephonyState();
        return super.onStartCommand(intent, flags, startId);
    }

    private void listenTelephonyState() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:
                        //空闲
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:
                        //响铃
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        //挂机
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            Intent in = new Intent(context, LockActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            in.putExtra(Constant.JUMP_FROM, Constant.JUMP_FROM_SCREEN_LISTENER);
            startActivity(in);
//            }
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        if (flag && receiver != null) {
            unregisterReceiver(receiver);
            flag = false;

        }
        return super.onUnbind(intent);
    }

}
