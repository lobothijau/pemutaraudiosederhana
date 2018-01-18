package com.lobothijau.pemutaraudiosederhana;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {
    SimpleExoPlayer simpleExoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Dipakai untuk menyiapkan data audio dan memutarnya di ExoPlayer
     */
    private void initExoPlayer() {
        // Persiapan
        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(
                this,
                null,
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
        );
        TrackSelector trackSelector = new DefaultTrackSelector();

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                renderersFactory,
                trackSelector
        );

        // Sebagai identifikasi player kita
        String userAgent = Util.getUserAgent(this, "Pemutar Audio Sederhana");

        // Menyiapkan MediaSource, jenis data yang dapat diputar oleh ExoPlayer
        ExtractorMediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse("asset:///detoursting.mp3"), // file audio ada di folder assets
                new DefaultDataSourceFactory(this, userAgent),
                new DefaultExtractorsFactory(),
                null,
                null
        );
        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.setPlayWhenReady(true);
    }

    /**
     * Dipakai untuk me-release ExoPlayer apabila sudah tidak dipergunakan
     * agar penggunaan memori lebih efisien.
     */
    private void releaseExoPlayer() {
        simpleExoPlayer.release();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Di Android Nougat ke atas, lalukan inisialisasi di onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            initExoPlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Apabila menggunakan Android Marshmallow ke bawah lakukan inisalisasi di onResume()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            initExoPlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Apabila menggunakan Android Marshmallow ke bawah lakukan release di onPause()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            releaseExoPlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Di Android Nougat ke atas, lalukan inisialisasi di onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            releaseExoPlayer();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
