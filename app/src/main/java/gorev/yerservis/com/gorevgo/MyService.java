package gorev.yerservis.com.gorevgo;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service implements LocationListener {
    int MID = 0;
    private static final String TAG = "BookingTrackingService";
    private Context context;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 20000;  //notify interval
    public double track_lat = 0.0;
    public double track_lng = 0.0;
    public static String str_receiver = "gorev.yerservis.com.gorevgo";
    Intent intent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
        intent = new Intent(str_receiver);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.context = this;
        sureKontrol();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy <<");
        if (mTimer != null) {
            mTimer.cancel();
        }
        locationManager.removeUpdates(this);
    }

    private void trackLocation() {
        Log.e(TAG, "trackLocation");
        Map<String, String> params = new HashMap<>();
        params.put("latitude", "" + track_lat);
        params.put("longitude", "" + track_lng);
        Log.e(TAG, "param_track_location >> " + params.toString());
        uyariVer(track_lat, track_lng);
        stopSelf();
        mTimer.cancel();

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {
            Log.e(TAG, "CAN'T GET LOCATION");
            stopSelf();
        } else {
            if (isNetworkEnable) {
                location = null;
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 90, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e(TAG, "isNetworkEnable latitude" + location.getLatitude() + "\nlongitude" + location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        track_lat = latitude;
                        track_lng = longitude;
                    }
                }
            }

            if (isGPSEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 90, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {

                        Log.e(TAG, "isGPSEnable latitude" + location.getLatitude() + "\nlongitude" + location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        track_lat = latitude;
                        track_lng = longitude;
//                        fn_update(location);
                    }
                }
            }

            Log.e(TAG, "START SERVICE");
            trackLocation();

        }
    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

    private void fn_update(Location location) {

        intent.putExtra("latutide", location.getLatitude() + "");
        intent.putExtra("longitude", location.getLongitude() + "");
        sendBroadcast(intent);
    }


    private double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        float pk = (float) (180.f / Math.PI);

        float a1 = (float) (lat_a / pk);
        float a2 = (float) lng_a / pk;
        float b1 = (float) lat_b / pk;
        float b2 = (float) lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    private void uyariVer(double a, double b) {
        TaskSQLite taskSQLite = new TaskSQLite(this);
        ArrayList<TaskItem> taskItems = taskSQLite.tumBildirimleriGetir();
        for (int i = 0; i < taskItems.size(); i++) { // there is a task control distance between you and task location. if it is larger than decided distance notify user!
            Log.e("Taranan:", "Enlem/// " + taskItems.get(i).getEnlem() + "/// Boylam ///" + taskItems.get(i).getBoylam());
            Log.e("Mesafe:", "" + meterDistanceBetweenPoints(a, b, Double.parseDouble(taskItems.get(i).getBoylam()), Double.parseDouble(taskItems.get(i).getEnlem())));
            if (meterDistanceBetweenPoints(a, b, Double.parseDouble(taskItems.get(i).getBoylam()), Double.parseDouble(taskItems.get(i).getEnlem())) < Integer.parseInt(taskItems.get(i).getCap())
                    && taskItems.get(i).getDurum().equals("NO")) {
                Log.e("girdi", "YEP");
                NotificationManager notificationManager = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                Intent notificationIntent = new Intent(context, GirisEkran.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                        context).setSmallIcon(R.drawable.logo)
                        .setContentTitle(taskItems.get(i).getBaslik())
                        .setContentText(taskItems.get(i).getIcerik())
                        .setSound(alarmSound)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                notificationManager.notify(MID, mNotifyBuilder.build());
                MID++;
                taskItems.get(i).setDurum("OK");
                taskSQLite.bildirimGuncelle(taskItems.get(i));
            }
        }
    }

    private void sureKontrol() {
        TaskSQLite taskSQLite = new TaskSQLite(this);
        ArrayList<TaskItem> taskItems = taskSQLite.tumBildirimleriGetir();

        for (int i = 0; i < taskItems.size(); i++) {// there is a task control date between current and task date. if there is just one hour or less left notify user!!
            if (taskItems.get(i).getBitistarih().equals(zamanGetir()) && taskItems.get(i).getDurum().equals("NO")) {
                Log.e("Tarih", "Eşit");
                String[] onunsaat = taskItems.get(i).getSaat().split(":");
                if (Integer.parseInt(onunsaat[0]) - Integer.parseInt(saatGetir().split(":")[0]) < 1 && Integer.parseInt(onunsaat[0]) - Integer.parseInt(saatGetir().split(":")[0]) >= 0) {
                    Log.e("Saat girdi", "YEP");
                    NotificationManager notificationManager = (NotificationManager) context
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    Intent notificationIntent = new Intent(context, GirisEkran.class);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                            context).setSmallIcon(R.drawable.logo)
                            .setContentTitle("Time is approaching ! Task:" + taskItems.get(i).getBaslik())
                            .setContentText(taskItems.get(i).getIcerik())
                            .setSound(alarmSound)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                    notificationManager.notify(MID, mNotifyBuilder.build());
                    MID++;
                    taskItems.get(i).setDurum("OK");
                    taskSQLite.bildirimGuncelle(taskItems.get(i));
                } else {
                    Log.e("Saat farkı", "" + (Integer.parseInt(onunsaat[0]) - Integer.parseInt(saatGetir().split(":")[0])));
                }
            } else {
                Log.e("Tarih", "Eşit Değil");
                Log.e("Guncel::", taskItems.get(i).getBitistarih());
                Log.e("Görev::", zamanGetir());
            }
        }
    }


    public String zamanGetir() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = sdf.format(cal.getTime());

        SimpleDateFormat sdf1 = new SimpleDateFormat();
        sdf1.applyPattern("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf1.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String string = sdf1.format(date);
        Log.e("Gün Format: ", string);
        return string;
    }

    public String saatGetir() {
        Calendar now = Calendar.getInstance();
        System.out.println(now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));
        int selectedHour = now.get(Calendar.HOUR_OF_DAY);
        int selectedMinute = now.get(Calendar.MINUTE);
        String minu = "0";
        if (selectedMinute < 10) {
            minu = "0" + selectedMinute;
        } else minu = "" + selectedMinute;

        String hh = "0";
        if (selectedHour < 10) {
            hh = "0" + selectedHour;
        } else hh = "" + selectedHour;

        return hh + ":" + minu;
    }

}