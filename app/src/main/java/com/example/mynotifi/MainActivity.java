package com.example.mynotifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private  final String channelId="personal notification";
    public static final int uniqueId=40111;
    public static final String TEXT_REPLY ="Text_reply";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button =findViewById(R.id.button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createNotificationChannel();
                        Intent landingIntent=new Intent(v.getContext(),MainActivity.class);
                        landingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent landingPendingIntent=PendingIntent.getActivity(v.getContext(),0,landingIntent,PendingIntent.FLAG_ONE_SHOT);

                        Intent yesIntent = new Intent(v.getContext(),YesActivity.class);
                        yesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent yesPendingIntent=PendingIntent.getActivity(v.getContext(),0,yesIntent,PendingIntent.FLAG_ONE_SHOT);

                        Intent noIntent = new Intent(v.getContext(),NoActivity.class);
                        noIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent noPendingIntent=PendingIntent.getActivity(v.getContext(),0,noIntent,PendingIntent.FLAG_ONE_SHOT);


                        final NotificationCompat.Builder builder=new  NotificationCompat.Builder(v.getContext(),channelId);
                        builder.setSmallIcon(R.drawable.ic_get_app_black_24dp);
                        builder.setContentTitle("Image Download");
                        builder.setContentText("Download in progress");
                        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        builder.setContentIntent(landingPendingIntent);
                        builder.setAutoCancel(true);
                        builder.addAction(R.drawable.ic_sms_black,"yes",yesPendingIntent);
                        builder.addAction(R.drawable.ic_sms_black,"no",noPendingIntent);
                        final int max_progress = 100;
                        int current_progress=0;
                        builder.setProgress(max_progress,current_progress,false);
                        final NotificationManagerCompat nmc= NotificationManagerCompat.from(v.getContext());

                        Thread thread=new Thread() {
                            @Override
                            public void run() {
                                try {


                                    int count = 0;
                                    while (count <= 100) {
                                        count = count + 10;
                                        sleep(1000);
                                        builder.setProgress(max_progress,count,false);
                                        builder.setOnlyAlertOnce(true);
                                        nmc.notify(uniqueId,builder.build());


                                    }

                                    builder.setContentText("Download completed");
                                    builder.setProgress(0,0,false);
                                    builder.setOnlyAlertOnce(false);
                                    nmc.notify(uniqueId,builder.build());

                                } catch (InterruptedException e) {
                                }
                            }
                        };

                        thread.start();

                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
                        {
                            Log.d("vw","in main if");
                            RemoteInput remoteInput = new RemoteInput.Builder(TEXT_REPLY).setLabel("Reply").build();
                            Intent replyIntent = new Intent(v.getContext(),RemoteActivity.class);
                            replyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent replyPendingIntent = PendingIntent.getActivity(v.getContext(),0,replyIntent,PendingIntent.FLAG_ONE_SHOT);

                            NotificationCompat.Action action= new NotificationCompat.Action.Builder(R.drawable.ic_sms_black,"Reply"
                                    ,replyPendingIntent).addRemoteInput(remoteInput).build();
                            builder.addAction(action);


                        }

                        nmc.notify(uniqueId,builder.build());


                    }
                });
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name= "personal notifiction";
            String discription= "include all the personal notification";
            int importance=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel=new NotificationChannel(channelId,name,importance);
            notificationChannel.setDescription(discription);
            NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }

    }
}
