package gorev.yerservis.com.gorevgo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class GirisEkran extends AppCompatActivity implements RefreshCallback.RefreshListener { //refresh callback
    ViewPager pagerGorev;
    TaskSQLite gorevlerSqLite;
    TextView bildirim_sayisi;
    TextView yeni_gorev;
    TextView duyuru_sayisi;
    TextView haritayagozat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_ekran);
        gorevlerSqLite = new TaskSQLite(this);
        GorevYukle();
        ListenerAta();
        izinAl();
        RefreshCallback.getInstance().setListener(this);
    }

    void izinAl() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //we need to take location permission to obtain lang and lot.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

    }


    void GorevYukle() {
        haritayagozat = (TextView) findViewById(R.id.haritayagozat);
        bildirim_sayisi = (TextView) findViewById(R.id.bildirim_sayisi);
        duyuru_sayisi = (TextView) findViewById(R.id.duyurusayisi);
        bildirim_sayisi.setText("" + gorevlerSqLite.okunmadiSayisi());
        ArrayList<TaskItem> taskItems = gorevlerSqLite.tumBildirimleriGetir();
        Collections.reverse(taskItems);
        pagerGorev = (ViewPager) findViewById(R.id.viewPagerGorev);
        duyuru_sayisi.setText("" + taskItems.size());
        PagerAdapter adapterGorev = new TaskAdapter(taskItems, this);
        pagerGorev.setPageTransformer(true, new SayfaGoruntuleyici(true));
        pagerGorev.setAdapter(adapterGorev);
        pagerGorev.setOffscreenPageLimit(3);
        pagerGorev.setPageMargin((int) getResources().getDimension(R.dimen.fab_margin));
        pagerGorev.setClipChildren(false);
    }


    void ListenerAta() {
        yeni_gorev = (TextView) findViewById(R.id.yeni_gorev);
        yeni_gorev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(GirisEkran.this, AnaMenu.class), 3);
            }
        });
        haritayagozat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GirisEkran.this, MapShow.class));
            }
        });
    }

    @Override
    public void stateChanged() {  //this interface warns us new task added so we refresh the pagerview to see new one(task)
        bildirim_sayisi = (TextView) findViewById(R.id.bildirim_sayisi);
        duyuru_sayisi = (TextView) findViewById(R.id.duyurusayisi);
        bildirim_sayisi.setText("" + gorevlerSqLite.okunmadiSayisi());
        ArrayList<TaskItem> taskItems = gorevlerSqLite.tumBildirimleriGetir();
        pagerGorev = (ViewPager) findViewById(R.id.viewPagerGorev);
        duyuru_sayisi.setText("" + taskItems.size());
        PagerAdapter adapterGorev = new TaskAdapter(taskItems, this);
        pagerGorev.setPageTransformer(true, new SayfaGoruntuleyici(true));
        pagerGorev.setAdapter(adapterGorev);
        pagerGorev.setOffscreenPageLimit(3);
        pagerGorev.setPageMargin((int) getResources().getDimension(R.dimen.fab_margin));
        pagerGorev.setClipChildren(false);
    }

    public void sellall(View view) {
        startActivity(new Intent(GirisEkran.this, SeeAll.class));
    } //see all button
}
