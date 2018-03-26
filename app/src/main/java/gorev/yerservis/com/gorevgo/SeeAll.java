package gorev.yerservis.com.gorevgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class SeeAll extends AppCompatActivity {
    TaskSQLite sqLite;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);
        allinits();
    }

    private void allinits() {
        sqLite = new TaskSQLite(this);
        listView = (ListView) findViewById(R.id.seeAllListview);
        ArrayList<TaskItem> items = sqLite.tumBildirimleriGetir();
        SeeAllAdapter adapter = new SeeAllAdapter(items, this);
        listView.setAdapter(adapter);
        getSupportActionBar().hide();

    }
}
