package com.example.BookitApp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.Provider;

public class PlayService extends Service{

    MediaPlayer player;

    public PlayService()
    {

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this,"service on start",Toast.LENGTH_LONG).show();
        if ( player != null) {
            player.reset();
        }
        if (player == null || !player.isPlaying())
        {
            try {
                player = MediaPlayer.create(this, R.raw.uneven);
                player.setLooping(true);
                player.setVolume(100,100);
                player.start();
            }
            catch (Exception e)
            {
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player!=null)
        {
            if (player.isPlaying())
            {
                player.stop();
            }
            player.release();
        }
    }
}
