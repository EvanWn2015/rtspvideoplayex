package com.example.evan.rtspvideoplayexapplication;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class IPCamActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final String TAG = "";
    private Context context;
    private ProgressBar progressBar;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceholder;
    private MediaPlayer mp;
    private Handler handler;
    private String rtspUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipcam);
        context = this;
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        rtspUrl = getIntent().getStringExtra("url");

        handler = new Handler();
        surfaceholder = surfaceView.getHolder();
        surfaceholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "in onPostResume");
        surfaceholder.addCallback(IPCamActivity.this);
        show();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mp = new MediaPlayer();//產生MediaPlayer物件
        try {
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);//設定型態
            mp.setDataSource(rtspUrl);//路徑
            mp.setDisplay(surfaceholder);//用surfaceholder顯示畫面
            mp.prepareAsync();//要用非同步的準備，UI介面才不會停住
        } catch (Exception e) {
            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
        }
        // 設置邊播放邊緩存
        mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (percent == 100) {//如果緩衝完成就開始播放
                    handler.sendEmptyMessageDelayed(0, 1000 * 5);//開始更新進度條
                    hide();
                    mp.start();
                }
            }
        });
        //視頻準備完成 可以播放
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                hide();
                mp.start();
            }
        });
        // 播放器異常事件
        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                // TODO Auto-generated method stub
                hide();
                Toast.makeText(context, "URL Connection Error", Toast.LENGTH_SHORT).show();
                mp.release();
                return false;
            }
        });
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.e("Surface h w", String.valueOf(width) + " " + String.valueOf(height));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
//        lp.width = (int)(height * 1.33);
        lp.height = (int) (width * 0.75);
        surfaceView.setLayoutParams(lp);

        if (mp != null) {
            mp.setDisplay(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void hide(){
        progressBar.setVisibility(View.GONE);
        surfaceView.setVisibility(View.VISIBLE);
    }

    private void show(){
        progressBar.setVisibility(View.VISIBLE);
        surfaceView.setVisibility(View.GONE);
    }

}
