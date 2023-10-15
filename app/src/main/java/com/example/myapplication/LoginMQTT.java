package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class LoginMQTT extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    public static String getTopic;
    private Button btnConnect;
    private Button btnPublish;
    private MqttAndroidClient mqttAndroidClient;
    private Button btnSubscribe, btnChange;
    private TextView tvStatus;
    private EditText inputMsg,inputtopic,inputSURI;
    private long backPressTime;
    private Toast mToast;
    String topic ;
    String serverURI;
    String clientId = "MqttAndroid";
    int STT=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginmqtt);

        initView();
    }

    public void onBackPressed(){
        if (backPressTime+2000>System.currentTimeMillis()){
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startActivity(startMain);

        } else {
            Toast.makeText(LoginMQTT.this,"Press back again to exit the application", Toast.LENGTH_SHORT).show();
        }
        backPressTime = System.currentTimeMillis();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                  onBackPressed();
                        return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    private void initView() {
        btnConnect = findViewById(R.id.btn_connect);

        btnChange = findViewById(R.id.btn_change);

        btnConnect.setOnClickListener(this);


        btnChange.setOnClickListener(this);



        tvStatus = findViewById(R.id.text);

        inputtopic = findViewById(R.id.topic);
        inputSURI = findViewById(R.id.SURI);

    }
    
    public EditText getTopic()
    {
        return inputtopic;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_connect:
                topic = inputtopic.getText().toString();
                serverURI = inputSURI.getText().toString();
                tvStatus.setText("connect...");
                Client.topic = topic;
                Client.serverURI = serverURI;
                Client.clientId = clientId;
                connect();
                break;


            case R.id.btn_change:

//                startActivities(new Intent[]{new  Intent(MainActivity.this, MainActivity5.class)});
                if (STT==0)
                    Toast.makeText(LoginMQTT.this, "Please Connect MQTT", Toast.LENGTH_SHORT).show();
                else
                    change();
                break;
        }
    }
    private void change()
    {
        startActivities(new Intent[]{new  Intent(LoginMQTT.this, MainActivity2.class)});
    }



    //发布
    private void publish(String msg) {

        MqttMessage message = new MqttMessage();
        message.setQos(0);
        message.setRetained(false);
        message.setPayload((msg).getBytes());
        try {
            mqttAndroidClient.publish(topic, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    tvStatus.setText("publish onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    tvStatus.setText("publish onFailure");
                    Log.e(TAG, "onFailure: ");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


//    //订阅
//    private void subscribe() {
//        try {
//            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//
//                    Log.e(TAG, "onSuccess: " + asyncActionToken.getClient().getClientId());
//                    tvStatus.setText("subscribe onSuccess");
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//
//                    tvStatus.setText("subscribe onFailure:");
//                }
//            });
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//
//    }



    private void connect() {

        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverURI, clientId);

        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.e(TAG, "connectionLost: ");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                Log.e(TAG, "messageArrived: " + topic + ":" + message.toString());

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

                Log.e(TAG, "deliveryComplete: ");
            }
        });


        MqttConnectOptions connectOptions = new MqttConnectOptions();
        connectOptions.setAutomaticReconnect(true);

        try {
            mqttAndroidClient.connect(connectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    Log.e(TAG, "connect onSuccess: " + asyncActionToken.getClient().getClientId());

                    Toast.makeText(LoginMQTT.this, "connect onSuccess", Toast.LENGTH_SHORT).show();
                    tvStatus.setText("connect onSuccess");
//                    publish("Success");
//                    change();
                    STT=1;

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    tvStatus.setText("connect onFailure");
                    Log.e(TAG, "connect onFailure: " );
                    STT=0;
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


}