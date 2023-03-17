package com.example.homework1;

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
    private EditText pitchTextAcc, rollTextAcc, yawTextAcc;
    private EditText pitchTextGyr, rollTextGyr, yawTextGyr;

    private SensorManager sensorManager;

    private Sensor accelerometer;
    private Sensor gyroscope;
    private Sensor magnetometer;

    private float acclx = 0, accly = 0, acclz = 0, gyrox = 0, gyroy = 0, gyroz = 0;
    private float now = 0, before = 0, diff = 0;
    private float prevx = 0, prevy = 0, prevz = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = (Sensor) sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = (Sensor) sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        magnetometer = (Sensor) sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        pitchTextAcc = (EditText) findViewById(R.id.pitchTextboxAcc);
        rollTextAcc = (EditText) findViewById(R.id.rollTextboxAcc);
        yawTextAcc = (EditText) findViewById(R.id.yawTextboxAcc);

        pitchTextGyr = (EditText) findViewById(R.id.pitchTextboxGyr);
        rollTextGyr = (EditText) findViewById(R.id.rollTextboxGyr);
        yawTextGyr = (EditText) findViewById(R.id.yawTextboxGyr);

        sensorButton = (Button) findViewById(R.id.sensorbutton);

        sensorButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sensorbutton:
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
                sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
                sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometer);
        sensorManager.unregisterListener(this, gyroscope);
        sensorManager.unregisterListener(this, magnetometer);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            acclx = event.values[0];
            accly = event.values[1];
            acclz = event.values[2];
            //Log.d("DEBUG",new Float(acclx).toString());
            //AcclTask mytask = new AcclTask(acclx, accly, acclz);
            //myhandler.post(mytask);
            AcclMagTask mytask = new AcclMagTask(acclx, accly, acclz, 0, 0, 0);
            myhandler.post(mytask);
        }

        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            now = (float) event.timestamp / (float) 1000000000;
            //Log.d("timestamp",new Float(now).toString());
            diff = now - before;
            gyrox = event.values[0];
            gyroy = event.values[1];
            gyroz = event.values[2];
            GyroTask mytask = new GyroTask(gyrox, gyroy, gyroz, diff);
            myhandler.post(mytask);
            before = now;
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class AcclMagTask implements Runnable{
        float anglePitch, angleRoll, angleYaw;
        public AcclMagTask(float accx, float accy, float accz, float magx, float magy, float magz){
            // Find Pitch
            // Special cases when 0, 90, -90, 180, -180
            if(accz == 0){
                if(accy > 0){
                    this.anglePitch = 0;
                } else if (accy < 0) {
                    this.anglePitch = 180; // Or -180
                }
            } else if (accy == 0) {
                if(accz > 0){
                    this.anglePitch = -90;
                } else if (accz < 0) {
                    this.anglePitch = 90;
                }
            }
            // Four quadrants
            if(accy > 0 && accz < 0){ // 0 to 90
                this.anglePitch = (float) Math.toDegrees(Math.atan2(Math.abs(accz), accy));
            } else if (accy > 0 && accz > 0) { // 0 to -90
                this.anglePitch = (float) Math.toDegrees(Math.atan2(-accz, accy));
            } else if (accy < 0 && accz < 0) { // 90 to 180
                this.anglePitch = 180 - (float) Math.toDegrees(Math.atan2(accz, accy));
            } else if (accy < 0 && accz > 0){ // -90 to -180
                this.anglePitch = (float) Math.abs(Math.toDegrees(Math.atan2(Math.abs(accz), accy))) - 180;
            }

            // Find Roll
            // Special cases when 0, 90, -90, 180, -180
            if(accx == 0){
                if(accy > 0){
                    this.angleRoll = 0;
                } else if (accy < 0) {
                    this.angleRoll = 180; // Or -180
                }
            } else if (accy == 0) {
                if(accx> 0){
                    this.angleRoll = 90;
                } else if (accx < 0) {
                    this.angleRoll = -90;
                }
            }
            // Four quadrants
            if(accy > 0 && accx > 0){ // 0 to 90
                this.angleRoll = (float) Math.toDegrees(Math.atan2(accx, accy));
            } else if (accy > 0 && accx < 0) { // 0 to -90
                this.angleRoll = (float) Math.toDegrees(Math.atan2(accx, accy));
            } else if (accy < 0 && accx > 0) { // 90 to 180
                this.angleRoll = (float) Math.toDegrees(Math.atan2(accx, accy)) + 180;
            } else if (accy < 0 && accx < 0){ // -90 to -180
                this.angleRoll = (float) Math.toDegrees(Math.atan2(accx, accy)) - 180;
            }
        }

        @Override
        public void run() {
            pitchTextAcc.setText(new Float(this.anglePitch).toString());
            rollTextAcc.setText(new Float(this.angleRoll).toString());
        }
    }

    private class GyroTask implements Runnable{
        float anglePitch, angleRoll, angleYaw;
        public GyroTask(float gyrx, float gyry, float gyrz, float diff){
            float deltax = (float) Math.toDegrees(gyrx * diff);
            float deltay = (float) Math.toDegrees(gyry * diff);
            float deltaz = (float) Math.toDegrees(gyrz * diff);

            deltax = deltax % 180;
            deltay = deltay % 180;
            deltaz = deltaz % 180;

            // Validating the angles
            float newPitch = deltax + prevx;
            float newRoll = deltaz + prevz;
            float newYaw = deltay + prevy;

            if(newPitch > 180){
                newPitch = newPitch -360;
            } else if (newPitch < -180) {
                newPitch = newPitch + 360;
            }
            if(newRoll > 180){
                newRoll = newRoll -360;
            } else if (newRoll < -180) {
                newRoll = newRoll + 360;
            }
            if(newYaw > 180){
                newYaw = newYaw -360;
            } else if (newYaw < -180) {
                newYaw = newYaw + 360;
            }

            prevx = newPitch;
            prevy = newRoll;
            prevz = newYaw;

            this.anglePitch = newPitch;
            this.angleRoll = newRoll;
            this.angleYaw = newYaw;
        }

        @Override
        public void run() {
            pitchTextGyr.setText(new Float(this.anglePitch).toString());
            rollTextGyr.setText(new Float(this.angleRoll).toString());
            yawTextGyr.setText(new Float(this.angleYaw).toString());
        }
    }
}