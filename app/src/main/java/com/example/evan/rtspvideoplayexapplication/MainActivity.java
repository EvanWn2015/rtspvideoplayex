package com.example.evan.rtspvideoplayexapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Context context;
    private EditText etUrl;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        etUrl = (EditText) findViewById(R.id.etUrl);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn) {
            Intent intent = new Intent(context, IPCamActivity.class);
            if (!etUrl.getText().equals("") && !etUrl.getText().toString().isEmpty()){
                String rtspUrl = "rtsp://" + etUrl.getText().toString();
                Log.i(TAG, rtspUrl);
                intent.putExtra("rtspUrl", rtspUrl);
                startActivity(intent);
            }else {
                Toast.makeText(context,"check url", Toast.LENGTH_LONG).show();
            }
        }
    }
}
