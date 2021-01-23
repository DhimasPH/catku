package com.example.catku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.catku.db.sqLiteDb;
import com.example.catku.fragment.CalendarFragment;
import com.example.catku.fragment.HomeFragment;
import com.example.catku.fragment.ProfileFragment;
import com.example.catku.fragment.ReportFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MenuItem menuItem;
    String index = "0";
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            index = getIntent().getStringExtra("index");
        }catch (Exception e){
            index = "0";
        }

        //Menampilkan halaman Fragment yang pertama kali muncul
        getFragmentPage(new HomeFragment());

        /*Inisialisasi BottomNavigationView beserta listenernya untuk
         *menangkap setiap kejadian saat salah satu menu item diklik
         */
        final ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        final BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Menantukan halaman Fragment yang akan tampil
                switch (item.getItemId()){
                    case R.id.home:
                        viewPager.setCurrentItem(0);
                        break;

                    case R.id.calendar:
                        viewPager.setCurrentItem(1);
                        break;

                    case R.id.report:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.profile:
                        viewPager.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });

        try {
            if (index.equals("0")){
                MenuItem item = bottomNavigation.getMenu().findItem(R.id.home);
                item.setChecked(true);
            }else if (index.equals("1")){
                MenuItem item = bottomNavigation.getMenu().findItem(R.id.calendar);
                item.setChecked(true);
            }else if (index.equals("2")){
                MenuItem item = bottomNavigation.getMenu().findItem(R.id.report);
                item.setChecked(true);
            }else if (index.equals("3")){
                MenuItem item = bottomNavigation.getMenu().findItem(R.id.profile);
                item.setChecked(true);
            }
        }catch (Exception e){

        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                    bottomNavigation.getMenu().getItem(position).setChecked(true);
                    menuItem = bottomNavigation.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        loadNotification();
        if(!isMyServiceRunning(ActivityService.class)){
            startServices();
        } else {
            Log.d("Cekservice", "Started");
        }
    }

    public void startServices () {
        Intent intent = new Intent(getBaseContext(), ActivityService.class);
        startService(intent);
        Log.d("startService", "start new service");
    }

    public void stopServices(){
        stopService(new Intent(getBaseContext(), ActivityService.class));
        //  stopService(new Intent(getBaseContext(), MerchantService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    public void onDestroy(){
        super.onDestroy();
        stopServices();
    }

    //Menampilkan halaman Fragment
    private boolean getFragmentPage(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.page_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new CalendarFragment(), "Calendar");
        adapter.addFragment(new ReportFragment(), "Report");
        adapter.addFragment(new ProfileFragment(), "Profile");
        viewPager.setAdapter(adapter);
        int indexPage = 0;
        try {
            if (index.equals("0")){
                indexPage = 0;
            }else if (index.equals("1")){
                indexPage = 1;
            }else if (index.equals("2")){
                indexPage = 2;
            }else if (index.equals("3")){
                indexPage = 3;
            }
        }catch (Exception e){

        }

        viewPager.setCurrentItem( indexPage );
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void loadNotification(){
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
            Intent intent = new Intent(this, MainActivity.class);
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
            PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            String CHANNEL_ID = formatter.format(new Date());
            CharSequence name = "Schedule Transaction";
            NotificationManager notificationManager = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                Notification noti = new Notification.Builder(this)
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

                Notification noti = new Notification.Builder(this)
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

    }

}