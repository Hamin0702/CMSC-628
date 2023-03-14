package com.example.sensorexample;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private Handler myhandler = new Handler();
    private Button sensorButton;
    private EditText editTextx,editTexty,editTextz,editTextgx,editTextgy,editTextgz;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private float acclx = 0, accly = 0, acclz = 0, gyrox = 0, gyroy = 0, gyroz = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = (Sensor) sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = (Sensor) sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        editTextx = (EditText) findViewById(R.id.editTextX);
        editTexty = (EditText) findViewById(R.id.editTextY);
        editTextz = (EditText) findViewById(R.id.editTextZ);

        editTextgx = (EditText) findViewById(R.id.editTextgx);
        editTextgy = (EditText) findViewById(R.id.editTextgy);
        editTextgz = (EditText) findViewById(R.id.editTextgz);

        sensorButton = (Button) findViewById(R.id.sensorbutton);

        sensorButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sensorbutton:
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
                sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, gyroscope);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            acclx = event.values[0];
            accly = event.values[1];
            acclz = event.values[2];
            Log.d("DEBUG",new Float(acclx).toString());
            AcclTask mytask = new AcclTask(acclx, accly, acclz);
            myhandler.post(mytask);
        }

        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            gyrox = event.values[0];
            gyroy = event.values[1];
            gyroz = event.values[2];
            GyroTask mytask = new GyroTask(gyrox, gyroy, gyroz);
            myhandler.post(mytask);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class AcclTask implements Runnable{
        float acclxt, acclyt, acclzt;
        public AcclTask(float accx, float accy, float accz){
            this.acclxt = accx;
            this.acclyt = accy;
            this.acclzt = accz;
        }

        @Override
        public void run() {
            editTextx.setText(new Float(this.acclxt).toString());
            editTexty.setText(new Float(this.acclyt).toString());
            editTextz.setText(new Float(this.acclzt).toString());
        }
    }

    private class GyroTask implements Runnable{
        float gyroxt, gyroyt, gyrozt;
        public GyroTask(float gyrx, float gyry, float gyrz){
            this.gyroxt = gyrx;
            this.gyroyt = gyry;
            this.gyrozt = gyrz;
        }
        @Override
        public void run() {
            editTextgx.setText(new Float(this.gyroxt).toString());
            editTextgy.setText(new Float(this.gyroyt).toString());
            editTextgz.setText(new Float(this.gyrozt).toString());
        }
    }
}