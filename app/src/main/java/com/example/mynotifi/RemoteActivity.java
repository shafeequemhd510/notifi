package com.example.mynotifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import android.app.NotificationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class RemoteActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
        Log.d("vw","inRemoteActivity oncreate");

        textView=findViewById(R.id.textView);

        Bundle remoteReply = RemoteInput.getResultsFromIntent(getIntent());

        if (remoteReply!=null){
            Log.d("vw","inRemoteActivity inside if");

            String message = remoteReply.getCharSequence(MainActivity.TEXT_REPLY).toString();
            textView.setText(message);

        }

        NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(MainActivity.uniqueId);

        }

    }

