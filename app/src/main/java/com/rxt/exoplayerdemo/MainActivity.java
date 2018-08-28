package com.rxt.exoplayerdemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    public static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final String TAG = "ExoPlayer";
    @BindView(R.id.rv_music_list)
    RecyclerView rvMusicList;
    private MusicListAdapter mAdapter;
    private AlertDialog mPermissionRationaleDialog;
    private SimpleExoPlayer mSimpleExoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        printSomething();
        ButterKnife.bind(this);
        initRecycler();
        checkPermissionNecessary();
    }

    private void printSomething() {
        float fontScale = getResources().getConfiguration().fontScale;
        Log.d(TAG, "fontScale: " + fontScale);
    }

    private void initRecycler() {
        rvMusicList.setLayoutManager(new LinearLayoutManager(this));
        rvMusicList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter = new MusicListAdapter();
        rvMusicList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Uri musicUri) {
                playMusic(musicUri);
            }
        });
    }

    private void playMusic(Uri musicUri) {

        if (mSimpleExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getApplicationContext(), trackSelector);
        }

        mSimpleExoPlayer.release();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory
                (getApplicationContext(), Util.getUserAgent(getApplicationContext(), "ExoPlayerDemo"));
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(musicUri);
        mSimpleExoPlayer.prepare(mediaSource);

//        mSimpleExoPlayer
    }

    private void checkPermissionNecessary() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("提示");
                builder.setMessage("需要读取内部存储权限来查找音乐文件");
                builder.setNegativeButton("取消", this);
                builder.setPositiveButton("继续", this);
                mPermissionRationaleDialog = builder.show();
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            loadData();
        }
    }

    private void loadData() {
        List<Music> musicList = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d(TAG, cursor.toString());
                String musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                Uri contentUri = MediaStore.Audio.Media.getContentUri(musicName);
                musicList.add(new Music(musicName, contentUri));
            }
            cursor.close();
        }
        mAdapter.setMusicList(musicList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
                break;
            default:
                break;
        }
        if (mPermissionRationaleDialog != null) {
            mPermissionRationaleDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                loadData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSimpleExoPlayer.release();
    }
}
