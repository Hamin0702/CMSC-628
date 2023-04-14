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

    private float acclx = 0, accly = 0, acclz = 0, gyrox = 0, gyroy = 0, gyroz = 0, magx = 0, magy = 0, magz = 0;
    private float now = 0, before = 0, diff = 0;
    private float prevx = 90, prevy = 0, prevz = 0; // Assume we start at phone facing forward !!!!!

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
        }

        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE){
            now = (float) event.timestamp / (float) 1000000000;
            diff = now - before;
            gyrox = event.values[0];
            gyroy = event.values[1];
            gyroz = event.values[2];
            GyroTask mytask = new GyroTask(gyrox, gyroy, gyroz, diff);
            myhandler.post(mytask);
            before = now;
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magx = event.values[0];
            magy = event.values[1];
            magz = event.values[2];

            float pitch = getPitch(acclx,accly,acclz);
            float roll = getRoll(acclx,accly,acclz);
            float yaw = getYaw(pitch,roll,magx,magy,magz);

            AcclMagTask mytask = new AcclMagTask(pitch, roll, yaw);
            myhandler.post(mytask);
        }

    }

    private float getPitch(float accx, float accy, float accz){
        return (float) Math.toDegrees(Math.atan2(accy, accz));
    }

    private float getRoll(float accx, float accy, float accz){
        return (float) Math.toDegrees(Math.atan2(-accx, Math.sqrt(accy * accy + accz * accz)));
    }

    private float getYaw(float pitch, float roll, float magx, float magy, float magz){
        float Mx = (float) (magx*Math.cos(pitch) + magz*Math.sin(pitch));
        float My = (float) (magx*Math.sin(roll)*Math.sin(pitch) + magy*Math.cos(roll) - magz * Math.sin(roll) * Math.cos(pitch));
        return (float) Math.toDegrees(Math.atan2(Mx,My));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class AcclMagTask implements Runnable{
        float anglePitch, angleRoll, angleYaw;
        public AcclMagTask(float pitch, float roll, float yaw){

            this.anglePitch = pitch;
            this.angleRoll = roll;
            this.angleYaw = yaw;

            /*
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
                this.anglePitch = (float) Math.toDegrees(Math.atan(Math.abs(accz)/accy));
            } else if (accy > 0 && accz > 0) { // 0 to -90
                this.anglePitch = (float) Math.toDegrees(Math.atan(-accz/accy));
            } else if (accy < 0 && accz < 0) { // 90 to 180
                this.anglePitch = 180 - (float) Math.toDegrees(Math.atan(accz/accy));
            } else if (accy < 0 && accz > 0){ // -90 to -180
                this.anglePitch = (float) Math.abs(Math.toDegrees(Math.atan(Math.abs(accz)/accy))) - 180;
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
                this.angleRoll = (float) Math.toDegrees(Math.atan(accx/accy));
            } else if (accy > 0 && accx < 0) { // 0 to -90
                this.angleRoll = (float) Math.toDegrees(Math.atan(accx/accy));
            } else if (accy < 0 && accx > 0) { // 90 to 180
                this.angleRoll = (float) Math.toDegrees(Math.atan(accx/accy)) + 180;
            } else if (accy < 0 && accx < 0){ // -90 to -180
                this.angleRoll = (float) Math.toDegrees(Math.atan(accx/accy)) - 180;
            }

            this.angleRoll = (float) Math.toDegrees(Math.atan2(-accx, Math.sqrt(accy * accy + accz * accz)));
            this.anglePitch = (float) Math.toDegrees(Math.atan2(accy, accz));
            float Mx, My;
            Mx = (float) (magx*Math.cos(this.angleRoll) + magz*Math.sin(this.angleRoll));
            My = (float) (magx*Math.sin(this.anglePitch)*Math.sin(this.angleRoll) + magy*Math.cos(this.anglePitch) - magz * Math.sin(this.anglePitch) * Math.cos(this.angleRoll));
            this.angleYaw = (float) Math.toDegrees(Math.atan2(My,Mx));
             */
        }

        @Override
        public void run() {
            pitchTextAcc.setText(new Float(this.anglePitch).toString());
            rollTextAcc.setText(new Float(this.angleRoll).toString());
            yawTextAcc.setText(new Float(this.angleYaw).toString());
        }
    }

    private class GyroTask implements Runnable{
        float anglePitch, angleRoll, angleYaw;
        public GyroTask(float gyrx, float gyry, float gyrz, float diff){
            float deltax = (float) Math.toDegrees(gyrx * diff);
            float deltay = (float) Math.toDegrees(gyry * diff);
            float deltaz = (float) Math.toDegrees(gyrz * diff);

            deltax = deltax % 360;
            deltay = deltay % 360;
            deltaz = deltaz % 360;

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