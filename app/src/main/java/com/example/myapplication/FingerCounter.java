package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.example.myapplication.Client;

public class FingerCounter extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnonHand, btnoffHand;
    private TextView Status;

    private static final String TAG = "MainActivity5";
    private MqttAndroidClient mqttAndroidClient;
    private Toast mToast;
    String topic;
    String serverURI;
    String clientId ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finger_counter);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Finger Counter");

        initView();
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        btnonHand = findViewById(R.id.on_hand);

        btnoffHand = findViewById(R.id.off_hand);

        btnonHand.setOnClickListener(this);

        btnoffHand.setOnClickListener(this);

        Status = findViewById(R.id.Text);
        topic = Client.topic;
        serverURI = Client.serverURI;
        clientId = Client.clientId;
        connect();


    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
       if (id==R.id.home) {
           super.onBackPressed();
       }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.on_hand:
                publish("on");
                Status.setText("Enable Finger Counter");
                break;


            case R.id.off_hand:
                publish("off");
                Status.setText("Disable Finger Counter");
                break;
        }
    }

    private void connect() {
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverURI, clientId);
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.e(TAG, "connectionLost: ");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setAutomaticReconnect(true);

        try {
            mqttAndroidClient.connect(connectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    private void publish(String msg) {

        MqttMessage message = new MqttMessage();
        message.setQos(0);
        message.setRetained(false);
        message.setPayload((msg).getBytes());
        try {
            mqttAndroidClient.publish(topic, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}