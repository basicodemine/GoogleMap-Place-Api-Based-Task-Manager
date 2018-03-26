package gorev.yerservis.com.gorevgo;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AnaMenu extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Button saat_al;
    TextView acik_Adres;
    Button haritadan_bul;
    Button gorev_kaydet;
    EditText basliks, iceriks;
    TaskSQLite taskSQLite;
    SeekBar mesafeolcer;
    Button tarih_al;
    TextView mesafegoster;
    String mesafe = "200";
    String lng = "", lat = "", adres = "";
    String saat = "";
    String tarih = "";

    @SuppressLint("ShortAlarm")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        acik_Adres = (TextView) findViewById(R.id.acik_adres);
        haritadan_bul = (Button) findViewById(R.id.haritadan_bul);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();    //we re setting google apis that we will use

        haritadan_bul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AnaMenu.this), PLACE_PICKER_REQUEST);  //picking place intent will ready for start
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.e("Hata", e.toString());
                }
            }
        });

        if (alarmManager == null) {   // here is where we call background service
            startService(new Intent(AnaMenu.this, MyService.class));
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, UyariAl.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 600000,  //this 600000ms is important. It has been set up to 10min
                    pendingIntent);                                                                 // every 10min program will check location and time. Less then 5min will not work!!!
        }
        saatGetir();
    }

    public String saatGetir() {  //This fucn will takes current time
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

    private void initViews() {
        taskSQLite = new TaskSQLite(this);  //calling our sql to save task
        tarih_al = (Button) findViewById(R.id.tarih_al);
        saat_al = (Button) findViewById(R.id.saat_al);
        mesafegoster = (TextView) findViewById(R.id.mesafegoster);
        mesafeolcer = (SeekBar) findViewById(R.id.mesafeolcer);
        mesafeolcer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mesafe = "" + i * 10;
                mesafegoster.setText("Warn at " + mesafe + "m distance");  //progress bar getting distance that you decide.
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        iceriks = (EditText) findViewById(R.id.icerik);
        basliks = (EditText) findViewById(R.id.baslik);
        gorev_kaydet = (Button) findViewById(R.id.GorevKaydet);
        gorev_kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lng.equals("") || lat.equals("") || basliks.equals("") || iceriks.equals("") || tarih.equals("") || saat.equals("")) {
                    Toast.makeText(AnaMenu.this, "Make sure you enter all the information and try again!", Toast.LENGTH_SHORT).show();
                } else {
                    taskSQLite.bildirimEkle(new TaskItem(basliks.getText().toString(), iceriks.getText().toString(), lng, lat, tarih_al.getText().toString(), zamanGetir(), adres, saat_al.getText().toString(), mesafe, "NO"));
                    /**here is important we will add task item on SQLite database with clicking create button you can change and modify class or control parameters here**/
                    finish();
                }
            }
        });
        setSupportActionBar(toolbar);
        saat_al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(AnaMenu.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String minu = "0";
                        if (selectedMinute < 10) {
                            minu = "0" + selectedMinute;
                        } else minu = "" + selectedMinute;


                        String hh = "0";
                        if (selectedHour < 10) {
                            hh = "0" + selectedHour;
                        } else hh = "" + selectedHour;

                        saat_al.setText(hh + ":" + minu);
                        saat = hh + ":" + minu;
                    }
                }, hour, minute, true);//true 24 saatli sistem için
                timePicker.setTitle("Select Time");
                timePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Set", timePicker);
                timePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", timePicker);
                timePicker.show(); // calling and setting time picker dialog
            }
        });

        tarih_al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int year = mcurrentTime.get(Calendar.YEAR);//Güncel Yılı alıyoruz
                int month = mcurrentTime.get(Calendar.MONTH);//Güncel Ayı alıyoruz
                int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);//Güncel Günü alıyoruz

                DatePickerDialog datePicker;//Datepicker objemiz
                datePicker = new DatePickerDialog(AnaMenu.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        String dd = "0";
                        if (dayOfMonth < 10) {
                            dd = "0" + dayOfMonth;
                        } else dd = "" + dayOfMonth;

                        String mm = "0";
                        if (monthOfYear < 9) {
                            mm = "0" + (Integer) (monthOfYear + 1);
                        } else mm = "" + (Integer) (monthOfYear + 1);

                        tarih_al.setText(dd + "/" + mm + "/" + year);//Ayarla butonu tıklandığında textview'a yazdırıyoruz
                        tarih = dd + "/" + mm + "/" + year;
                    }
                }, year, month, day);//başlarken set edilcek değerlerimizi atıyoruz
                datePicker.setTitle("Select Date");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Set", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", datePicker);
                datePicker.show(); // calling and setting date picker dialog
            }
        });
    }

    public String zamanGetir() { //This function takes current date !
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate = sdf.format(cal.getTime());
        System.out.println("Current date in String Format: " + strDate);

        SimpleDateFormat sdf1 = new SimpleDateFormat();
        sdf1.applyPattern("dd/MM/yyyy HH:mm");
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

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Error var: ", connectionResult.getErrorMessage().toString());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  // after place picker activity(INTENT) ends selected location informations will appear here!
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                acik_Adres.setText(address);
                adres = address;
                lng = longitude;
                lat = latitude;
            }
        }
    }


}
