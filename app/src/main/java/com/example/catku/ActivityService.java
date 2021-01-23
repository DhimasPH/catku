package com.example.catku;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.catku.db.sqLiteDb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityService extends Service {
    private sqLiteDb dbHelper;
    SQLiteDatabase db;
    SimpleDateFormat formater;
    SimpleDateFormat parser;
    int dataUserID;
    int data;
    String userID;
    String dateNow;
    String idCategory;
    String description;
    String date;
    String time;
    String amount;
    String type;
    String image = "";
    String memo;
    boolean loadNotif = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        formater =  new SimpleDateFormat("MM/dd/yyyy");
        parser = new SimpleDateFormat("MM/dd");
        dateNow = parser.format(new Date());
        dbHelper = new sqLiteDb(this);
        db = dbHelper.getReadableDatabase();
        Cursor cursorUserID = db.rawQuery("SELECT * FROM session_login ",null);
        cursorUserID.moveToFirst();
        dataUserID = cursorUserID.getCount();
        if (dataUserID > 0){
            for (int i = 0; i<cursorUserID.getCount(); i++) {
                cursorUserID.moveToPosition(i);
                userID = cursorUserID.getString(cursorUserID.getColumnIndex("id_user"));
            }
        }
        loadNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public void loadNotification(){
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM schedule",null);
            cursor.moveToFirst();
            data = cursor.getCount();
            if (data > 0){
                for (int i = 0; i<cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    Date dat = null;
                    try {
                        dat = formater.parse(cursor.getString(cursor.getColumnIndex("date")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String formattedDate = parser.format(dat);
                    if (formattedDate.equals(dateNow)){
                        idCategory = cursor.getString(cursor.getColumnIndex("id_category"));
                        time = cursor.getString(cursor.getColumnIndex("time"));
                        date = cursor.getString(cursor.getColumnIndex("date"));
                        description = cursor.getString(cursor.getColumnIndex("description"));
                        amount = cursor.getString(cursor.getColumnIndex("amount"));
                        type = cursor.getString(cursor.getColumnIndex("type"));
                        memo = "Schedule Transaction "+description;
                        loadNotif = true;
                    }
                }
            }
            if (loadNotif) {
                String sqlinsert = "INSERT INTO transaksi (" +
                        "id_user, " +
                        "id_category, " +
                        "type, " +
                        "image, " +
                        "amount, " +
                        "description, " +
                        "memo, " +
                        "date, " +
                        "time " +
                        ") VALUES"
                        + "('" + userID + "',"
                        + "'" + idCategory + "',"
                        + "'" + type + "',"
                        + "'" + image + "',"
                        + "'" + amount + "',"
                        + "'" + description + "',"
                        + "'" + memo + "',"
                        + "'" + date + "',"
                        + "'" + time + "');";
                db.execSQL(sqlinsert);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
                Intent intent = new Intent(ActivityService.this, MainActivity.class);
                intent.putExtra("id_user", userID);
                intent.putExtra("id_category", idCategory);
                intent.putExtra("type", type);
                intent.putExtra("amount", amount);
                intent.putExtra("description", description);
                intent.putExtra("time", time);
                intent.putExtra("date", date);
                intent.putExtra("Edit", "false");
                intent.putExtra("from_schedule", "true");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pIntent = PendingIntent.getActivity(ActivityService.this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                String CHANNEL_ID = formatter.format(new Date());
                CharSequence name = "Schedule Transaction";
                NotificationManager notificationManager = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    Notification noti = new Notification.Builder(ActivityService.this)
                            .setContentTitle("Schedule Transaction")
                            .setContentText("Schedule to add transaction " + description)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pIntent)
                            .setChannelId(CHANNEL_ID)
                            .setVibrate(new long[]{1000, 1000, 1000}).build();
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    noti.flags |= Notification.FLAG_AUTO_CANCEL;
                    noti.defaults |= Notification.DEFAULT_SOUND;
                    noti.defaults |= Notification.DEFAULT_VIBRATE;
                    notificationManager.createNotificationChannel(mChannel);
                    notificationManager.notify(0, noti);
                } else {

                    Notification noti = new Notification.Builder(ActivityService.this)
                            .setContentTitle("Schedule Transaction")
                            .setContentText("Schedule to add transaction " + description)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pIntent)
                            .setVibrate(new long[]{1000, 1000, 1000}).build();
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    // hide the notification after its selected
                    noti.flags |= Notification.FLAG_AUTO_CANCEL;
                    noti.defaults |= Notification.DEFAULT_SOUND;
                    noti.defaults |= Notification.DEFAULT_VIBRATE;
                    notificationManager.notify(0, noti);
                }
            }
            nextNotif();
        }catch (Exception e){
            Log.d("errorNotif", e.toString());
        }
    }

    public void nextNotif(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadNotification();
            }
        }, 360000);
    }
}
