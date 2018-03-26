package gorev.yerservis.com.gorevgo;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;

public class MapShow extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TaskSQLite taskSQLite;
    private TextView icerik, baslik, tarih;
    private ListView haritaListview;
    private Button listviewAcKapat;
    private SeekBar mapzoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_show);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initViews();
    }

    void initViews() {
        listviewAcKapat = (Button) findViewById(R.id.listviewAcKapat);
        haritaListview = (ListView) findViewById(R.id.maplistview);
        icerik = (TextView) findViewById(R.id.gorev_aciklama);
        baslik = (TextView) findViewById(R.id.gorev_baslik);
        tarih = (TextView) findViewById(R.id.gorev_tarih);
        haritaListview.setVisibility(View.GONE);
        mapzoom = (SeekBar) findViewById(R.id.mapzoom);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (getIntent().hasExtra("lng") && getIntent().hasExtra("lat") && getIntent().hasExtra("class")) {
            TaskItem model = (TaskItem) getIntent().getSerializableExtra("class");
            LatLng dot = new LatLng(Double.parseDouble(getIntent().getExtras().getString("lng")), Double.parseDouble(getIntent().getExtras().getString("lat")));
            mMap.addMarker(new MarkerOptions().position(dot).title("Task Location!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(dot));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dot, 20));
            icerik.setText(model.getIcerik());
            tarih.setText(model.getBaslangictarih());
            baslik.setText(model.getBaslik());

        } else {
            taskSQLite = new TaskSQLite(this);
            final ArrayList<TaskItem> taskItems = taskSQLite.tumBildirimleriGetir();
            if (taskItems.size() > 0) { //setting all markers

                for (int i = 0; i < taskItems.size(); i++) {
                    LatLng dot = new LatLng(Double.parseDouble(taskItems.get(i).getBoylam()), Double.parseDouble(taskItems.get(i).getEnlem()));
                    Marker marker = mMap.addMarker(new MarkerOptions().position(dot).title(taskItems.get(i).getBaslik()));
                    marker.setTag("" + i);
                }
                LatLng dot = new LatLng(Double.parseDouble(taskItems.get(taskItems.size() - 1).getBoylam()), Double.parseDouble(taskItems.get(taskItems.size() - 1).getEnlem()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(dot));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dot, 20));
                icerik.setText(taskItems.get(taskItems.size() - 1).getIcerik());
                tarih.setText(taskItems.get(taskItems.size() - 1).getBaslangictarih());
                baslik.setText(taskItems.get(taskItems.size() - 1).getBaslik());
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        int position = Integer.parseInt(marker.getTag().toString());
                        icerik.setText(taskItems.get(position).getIcerik());
                        tarih.setText(taskItems.get(position).getBaslangictarih());
                        baslik.setText(taskItems.get(position).getBaslik());

                        return false;
                    }
                });
                FirstLetterListAdapter adapter = new FirstLetterListAdapter(taskItems, MapShow.this);
                haritaListview.setAdapter(adapter);
                listviewAcKapat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (haritaListview.getVisibility() == View.GONE) {
                            haritaListview.setVisibility(View.VISIBLE);
                        } else if (haritaListview.getVisibility() == View.VISIBLE) {
                            haritaListview.setVisibility(View.GONE);
                        }
                    }
                });

                haritaListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        LatLng dot = new LatLng(Double.parseDouble(taskItems.get(i).getBoylam()), Double.parseDouble(taskItems.get(i).getEnlem()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(dot));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dot, 20));
                        icerik.setText(taskItems.get(i).getIcerik());         //setting panel views
                        tarih.setText(taskItems.get(i).getBaslangictarih());  //with click markers
                        baslik.setText(taskItems.get(i).getBaslik());
                    }
                });
            }
            mapzoom.setMax((int) mMap.getMaxZoomLevel());
            mapzoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //map zoom with seekbar value
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(i));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }
    }
}
