package com.sidhero.wifistate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView wifistatetext;
    Button checkstate;
    Button save;
    private static final String sFileName="wifistate";
    private static String sBody;
    private static FileOutputStream fos = null;
    private static int count=0;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifistatetext=(TextView)findViewById(R.id.textView);
        checkstate=(Button)findViewById(R.id.button);
        save=(Button)findViewById(R.id.button2);

        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                disp();
                savetofile();
                handler.postDelayed(this, 2000);
            }
        };

        handler.postDelayed(r, 1000);
    }

    private void disp() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo ();
        String ssid  = info.getSSID();
        int rssi = info.getRssi();
        String MacAddr=info.getMacAddress();
        int level = WifiManager.calculateSignalLevel(info.getRssi(), 5);

       wifistatetext.setText("\n\t\t###########\n\n\t\tSignal Strength of "+ ssid+"\n\n\t\tMac Address = "+ MacAddr+"\n\n\t\tRSSI = "+ rssi + " dbm \n\n\t\tLevel = "+ level + " out of 5");
//       wifistatetext.setVisibility(View.GONE);
        sBody= (String) wifistatetext.getText();
    }

    private void savetofile() {

        try {
            if(count==0){
                fos = openFileOutput(sFileName, MODE_PRIVATE);
                count++;
            }else {
                fos = openFileOutput(sFileName, MODE_APPEND);
            }
            if(sBody!=null){
                fos.write(sBody.getBytes());
            }else {
                fos.write(Integer.parseInt("    "));
            }
//            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + sFileName,
//                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void check(View view) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService (Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo ();
        String ssid  = info.getSSID();
        int rssi = info.getRssi();
        String MacAddr=info.getMacAddress();
        int level = WifiManager.calculateSignalLevel(info.getRssi(), 5);

        wifistatetext.setText("\n\t\t###########\n\n\t\tSignal Strength of "+ ssid+"\n\n\t\tMac Address = "+ MacAddr+"\n\n\t\tRSSI = "+ rssi + " dbm \n\n\t\tLevel = "+ level + " out of 5");
        wifistatetext.setVisibility(View.VISIBLE);
        sBody= (String) wifistatetext.getText();
   }

    public void StoreState(View view) {

//        Toast.makeText(this, sBody, Toast.LENGTH_SHORT).show();


        try {
            if(count==0){
                fos = openFileOutput(sFileName, MODE_PRIVATE);
                count++;
            }else {
                fos = openFileOutput(sFileName, MODE_APPEND);
            }
            fos.write(sBody.getBytes());

            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + sFileName,
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void Load(View view) {
        Intent intent =new Intent(getApplicationContext(),FileShow.class);
        startActivity(intent);
    }
}
