package com.example.byh.test1;

import android.app.Instrumentation;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextMessage;
    private static final String TAG = "test1---";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };
    private TextView message;
    private TextView txtVideo;
    private EditText edtVideo;
    private Button btnVideoStop;
    private LinearLayout content2;
    private TextView txtMp3;
    private EditText edtMp3;
    private Button btnMp3Play;
    private Button btnMp3Stop;
    private LinearLayout content3;
    private BottomNavigationView navigation;
    private LinearLayout container;

    private MediaPlayer mediaplayer;
    private Button btnReturn;
    private Button btnCloseInput;
    private MediaPlayer videolayer;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private LinearLayout content4;

    private int m_nVideoState;//0 null 1 play
    private int m_nMp3State;//0 null 1 play
    private SeekBar seekBar;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initView() {
        message = (TextView) findViewById(R.id.message);
        LinearLayout content = (LinearLayout) findViewById(R.id.content);
        txtVideo = (TextView) findViewById(R.id.txtVideo);
        edtVideo = (EditText) findViewById(R.id.edtVideo);
        Button btnVideoPlay = (Button) findViewById(R.id.btnVideoPlay);
        btnVideoStop = (Button) findViewById(R.id.btnVideoStop);
        content2 = (LinearLayout) findViewById(R.id.content2);
        txtMp3 = (TextView) findViewById(R.id.txtMp3);
        edtMp3 = (EditText) findViewById(R.id.edtMp3);
        btnMp3Play = (Button) findViewById(R.id.btnMp3Play);
        btnMp3Stop = (Button) findViewById(R.id.btnMp3Stop);
        content3 = (LinearLayout) findViewById(R.id.content3);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        container = (LinearLayout) findViewById(R.id.container);

        btnVideoPlay.setOnClickListener(this);
        btnVideoStop.setOnClickListener(this);
        btnMp3Play.setOnClickListener(this);
        btnMp3Stop.setOnClickListener(this);
        btnReturn = (Button) findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(this);
        btnCloseInput = (Button) findViewById(R.id.btnCloseInput);
        btnCloseInput.setOnClickListener(this);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.setOnClickListener(this);
        content4 = (LinearLayout) findViewById(R.id.content4);
        content4.setOnClickListener(this);

        holder = surfaceView.getHolder();
        holder.setFixedSize(400, 300);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // holder.addCallback(this);

        m_nVideoState = 0;
        m_nMp3State = 0;
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TAG, "Process is :" + i);

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (i*audioManager.getStreamMaxVolume(audioManager.STREAM_MUSIC))/100, AudioManager.FLAG_PLAY_SOUND);
                txtMp3.setText("" + i + "vol:" + (i*audioManager.getStreamMaxVolume(audioManager.STREAM_MUSIC))/100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        audioManager = (AudioManager)this.getSystemService(this.getApplicationContext().AUDIO_SERVICE);
    }

    private void closeVideo() {
        if (m_nVideoState != 0) {
            videolayer.stop();
            videolayer.release();
        }

        m_nVideoState = 0;
    }

    private void closeMp3() {
        if (m_nMp3State != 0) {
            mediaplayer.stop();
            mediaplayer.release();
        }

        m_nMp3State = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnVideoPlay:
                try {
                    closeVideo();

                    videolayer = new MediaPlayer();
                    videolayer.setDataSource(edtVideo.getText().toString());
                    videolayer.setDisplay(holder);
                    videolayer.setLooping(true);
                    videolayer.prepare();
                    videolayer.start();

                    m_nVideoState = 1;
                } catch (Exception e) {
                    Log.e(TAG, "error: " + e.getMessage(), e);
                }
                break;
            case R.id.btnVideoStop:
                closeVideo();
                break;
            case R.id.btnMp3Play:
                closeMp3();
                mediaplayer = MediaPlayer.create(this, Uri.parse(edtMp3.getText().toString()));
                mediaplayer.setLooping(true);
                mediaplayer.start();

                m_nMp3State = 1;
                break;
            case R.id.btnMp3Stop:
                closeMp3();
                break;
            case R.id.btnReturn:
                //  super.onKeyDown(KeyEvent.KEYCODE_BACK, event.);
//                Runtime runtime = Runtime.getRuntime();
//                runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK);
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                break;
            case R.id.btnCloseInput:
                InputMethodManager imm = (InputMethodManager) getSystemService(this.getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }


    
    private void submit() {
        // validate
        String edtVideoString = edtVideo.getText().toString().trim();
        if (TextUtils.isEmpty(edtVideoString)) {
            Toast.makeText(this, "edtVideoString不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String edtMp3String = edtMp3.getText().toString().trim();
        if (TextUtils.isEmpty(edtMp3String)) {
            Toast.makeText(this, "edtMp3String不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO vaFrameLayoutlidate success, do something


    }
}
