package com.example.tech2k8.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<EncapsulatedData> arrayList=new ArrayList<>();
    private ImageButton play,pause;
    private MediaPlayer player;
    private SeekBar seekBar;
    private ListView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view=findViewById(R.id.list);
        play=findViewById(R.id.play);
        pause=findViewById(R.id.pause);
        seekBar=findViewById(R.id.indicator);
        player=new MediaPlayer();
        //loadMusicFiles();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            checkForPermission();
        }
        else
        {
            loadMusicFiles();
        }



        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                playMusic(arrayList.get(position).getUri());
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMusic();
            }
        });
    }

    private void loadMusicFiles()
    {
        Uri musicUri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String columns[]={
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME
        };

        Cursor musicFiles=getContentResolver().query(musicUri,columns,null,null,null);

        if (musicFiles!=null)
        {
            if (musicFiles.moveToFirst())
            {
                do
                {EncapsulatedData ED=new EncapsulatedData();
                ED.setName(musicFiles.getString(1));
                ED.setUri(musicFiles.getString(0));
                arrayList.add(ED);

                }while(musicFiles.moveToNext());

                ListAdapter listAdapter=new ListAdapter(arrayList,MainActivity.this);
                view.setAdapter(listAdapter);
            }
        }
    }

    private void playMusic(String path)
    {
        try {
            player.reset();
            player.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to play this file", Toast.LENGTH_SHORT).show();
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to play this file", Toast.LENGTH_SHORT).show();
        }

        player.start();
        showProgress();
    }

    private void stopMusic()
    {
        player.pause();
        stopHandler();
    }

    private void checkForPermission()
    {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1001
                );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==1001)
        {
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                loadMusicFiles();
            }
        }
    }
    int i=0;
    Runnable runnable;
    Handler handler;
    private void showProgress()
    {

         handler =new Handler();
        runnable =new Runnable() {
            @Override
            public void run() {

                seekBar.setProgress(i);
                i++;
                handler.postDelayed(runnable,1000);
            }
        };

        handler.post(runnable);
    }

    private void stopHandler()
    {
        handler.removeCallbacks(runnable);
    }
}
