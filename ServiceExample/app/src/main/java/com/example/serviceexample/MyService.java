package com.example.serviceexample;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyService extends Service {

    public MyBinder mybinder = new MyBinder();
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mybinder;
    }



    public class MyBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }
    }
}