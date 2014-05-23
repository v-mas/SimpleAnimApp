package com.concisesoftware.myanimationsapplication.animapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.concisesoftware.myanimationsapplication.animapp.widgets.MyYTLikeLayout;
import com.concisesoftware.myanimationsapplication.animapp.widgets.YTLikeSecond;


public class YouTubeLikeActivity extends Activity {

    private final static String TAG = "YTlike";

    MyYTLikeLayout ytOverlay;
    YTLikeSecond ytOverlaySecond;

    Context mContext;

    MediaPlayer player;
    boolean isPrepared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_like);
        mContext = this;

        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        Button button = (Button) findViewById(R.id.yt_show);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ytOverlaySecond != null) {
                    ((ViewGroup)ytOverlaySecond.getParent()).removeView(ytOverlaySecond);
                    ytOverlaySecond = null;
                }
                if(ytOverlay == null) {
                    ytOverlay = new MyYTLikeLayout(mContext);

//                    RelativeLayout topView = (RelativeLayout) inflater.inflate(R.layout.video_holder, null);
//                    VideoView vv = (VideoView) topView.findViewById(R.id.video);
//                    MediaController controller = new MediaController(mContext);
//                    vv.setMediaController(controller);
//                    Log.d(TAG, "VideoView creation time:" + System.currentTimeMillis());
//                    vv.setVideoPath("http://redbullflow-staging.herokuapp.com/api/v2/disciplines/532038d437c2be0002000005/flow.m3u8");
////                    vv.setVideoPath("http://rb_vod_universal.redbull.com/i/redbullflow/videos/1395351024.mp4/segment1_0_av.ts?e=b471643725c47acd");
//                    vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        @Override
//                        public void onPrepared(MediaPlayer mp) {
//                            mp.start();
//                            Log.d(TAG, "VideoView prepared time:"+System.currentTimeMillis());
//                            Log.i(TAG, "Video prepared: height["+mp.getVideoHeight()+"] width["+mp.getVideoWidth()+"] duration["+mp.getDuration()+"]");
//                        }
//                    });
//                    vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                        @Override
//                        public boolean onError(MediaPlayer mp, int what, int extra) {
//                            Log.d(TAG, "VideoView error time:"+System.currentTimeMillis());
//                            Log.d(TAG, "player error what[" + what + "] extra[" + extra + "]");
//                            switch(extra) {
//                                case -1010:
//                                    Log.d(TAG, "Unsupported media format");
//                            }
//                            return true;
//                        }
//                    });

                    View bottomView = new View(mContext);
                    bottomView.setBackgroundColor(Color.rgb(0, 0, 127));



                    View topView = new View(mContext);
                    topView.setBackgroundColor(Color.rgb(128, 0, 0));
//
//                    LinearLayout bottomView = (LinearLayout) inflater.inflate(R.layout.controll_view, null);
//
//                    final Button ctrlBtn = (Button) bottomView.findViewById(R.id.btn_play_pause);
//                    ctrlBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (player != null && isPrepared) {
//                                if (player.isPlaying()) {
//                                    player.pause();
//                                    ctrlBtn.setText("Play");
//                                } else {
//                                    player.start();
//                                    ctrlBtn.setText("Pause");
//                                }
//                            }
//                        }
//                    });
//                    final SeekBar seekBar = (SeekBar) bottomView.findViewById(R.id.seek_bar);
//                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//                        @Override
//                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//                        }
//
//                        @Override
//                        public void onStartTrackingTouch(SeekBar seekBar) {
//
//                        }
//
//                        @Override
//                        public void onStopTrackingTouch(SeekBar seekBar) {
//                            if(player != null && isPrepared) {
//                                player.seekTo(seekBar.getProgress()*1000);
//                                Log.d(TAG, "seek to " + seekBar.getProgress() + "s");
//                            }
//                        }
//                    });
//
//
//                    RelativeLayout topView = (RelativeLayout) inflater.inflate(R.layout.surface_holder, null);
//                    SurfaceView sv = (SurfaceView) topView.findViewById(R.id.video);
//                    final SurfaceHolder sh = sv.getHolder();
//                    sh.addCallback(new SurfaceHolder.Callback() {
//                        @Override
//                        public void surfaceCreated(final SurfaceHolder holder) {
//                            if(player != null) {
//                                player.stop();
//                                player.release();
//                            }
//                            player = new MediaPlayer();
//                            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                            player.setDisplay(sh);
//                            player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                                @Override
//                                public boolean onError(MediaPlayer mp, int what, int extra) {
//                                    Log.d(TAG, "player error what:[" + what + "] extra:[" + extra + "]");
//                                    return true;
//                                }
//                            });
//                            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                                @Override
//                                public void onPrepared(MediaPlayer mp) {
//                                    isPrepared = true;
//                                    mp.start();
//                                    ctrlBtn.setText("Pause");
//                                    ctrlBtn.setEnabled(true);
//                                    Log.i(TAG, "Video prepared: height["+mp.getVideoHeight()+"] width["+mp.getVideoWidth()+"] duration["+mp.getDuration()+"]");
//                                }
//                            });
//                            player.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
//                                @Override
//                                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//                                    mp.start();
//                                    if(mp.getDuration() != 0) {
//                                        seekBar.setMax(mp.getDuration()/1000);
//                                        seekBar.setEnabled(true);
//                                    } else {
//                                        seekBar.setEnabled(false);
//                                    }
//                                    Log.i(TAG, "Video size changed: height["+mp.getVideoHeight()+"] width["+mp.getVideoWidth()+"] duration["+mp.getDuration()+"]");
//                                }
//                            });
//                            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
//                                @Override
//                                public void onBufferingUpdate(MediaPlayer mp, int percent) {
//                                    Log.d(TAG, "buffer update: percent["+percent+"]");
//                                }
//                            });
//                            try {
//                                player.setDataSource("http://redbullflow-staging.herokuapp.com/api/v2/disciplines/532038d437c2be0002000005/flow.m3u8");
////                                player.setDataSource("http://rb_vod_universal.redbull.com/i/redbullflow/videos/1395351024.mp4/segment1_0_av.ts?e=b471643725c47acd");
//                            } catch (IOException e) {
//                                Toast.makeText(mContext, "error during requesting data source", Toast.LENGTH_LONG).show();
//                                e.printStackTrace();
//                                player.release();
//                                player = null;
//                            }
//                            if(player != null) {
//                                player.prepareAsync();
//                            }
//                        }
//
//                        @Override
//                        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                        }
//
//                        @Override
//                        public void surfaceDestroyed(SurfaceHolder holder) {
//                            ctrlBtn.setEnabled(false);
//                            player.stop();
//                            player.release();
//                        }
//                    });



                    ytOverlay.setTopView(topView);
                    ytOverlay.setBottomView(bottomView);
                    addContentView(ytOverlay, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                } else {
                    ytOverlay.backToFullScreen();
                }
            }
        });

        Button button2 = (Button) findViewById(R.id.yt_show_second);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ytOverlay != null) {
                    ((ViewGroup)ytOverlay.getParent()).removeView(ytOverlay);
                    ytOverlay = null;
                }
                if(ytOverlaySecond == null) {
                    ytOverlaySecond = new YTLikeSecond(mContext);

                    View bottomView = new View(mContext);
                    bottomView.setBackgroundColor(Color.rgb(0, 127, 127));

                    RelativeLayout topView = new RelativeLayout(mContext);
                    topView.setBackgroundColor(Color.rgb(127, 127, 0));

                    Button buttonOnTop = new Button(mContext);
                    buttonOnTop.setText("minimize");
                    buttonOnTop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(ytOverlaySecond!=null) {
                                Log.d(TAG, "clicked button on top view");
                                ytOverlaySecond.minimize();
                            }
                        }
                    });
                    topView.addView(buttonOnTop, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    ytOverlaySecond.setTopView(topView);
                    ytOverlaySecond.setBottomView(bottomView);
                    addContentView(ytOverlaySecond, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                } else {
                    ytOverlaySecond.maximize();
                }
            }
        });
    }
}
