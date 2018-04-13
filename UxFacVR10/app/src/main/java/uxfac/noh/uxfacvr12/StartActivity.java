package uxfac.noh.uxfacvr12;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity implements View.OnClickListener {

    Button button;
    Handler handler;
    Runnable runnable;
    voiceRecode voiceRecode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        button = (Button)findViewById(R.id.start_activity_button);
        button.setOnClickListener(this);

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"Camera Permission Ok", Toast.LENGTH_SHORT).show();
        } else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 1);
        }
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"Camera Permission Ok", Toast.LENGTH_SHORT).show();
        } else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, 1);
        }
/*        voiceRecode = new voiceRecode(this);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    voiceRecode.start();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (voiceRecode.voiceFlag == -1) {
                        voiceRecode.start();
                    }


                }
            }
        };*/

        //handler.post(runnable);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.start_activity_button){
            Intent i = new Intent(StartActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

}
