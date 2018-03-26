package gorev.yerservis.com.gorevgo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;


public class FirstLetterListAdapter extends BaseAdapter {
    ArrayList<TaskItem> taskItems = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    //Simple adapter that holds first letter of task!
    public FirstLetterListAdapter(ArrayList<TaskItem> taskItems, Activity activity) {
        this.taskItems = taskItems;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return taskItems.size();
    }

    @Override
    public TaskItem getItem(int i) {
        return taskItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = inflater.inflate(R.layout.first_row, null);
        TextView ilkhrf = (TextView) v.findViewById(R.id.firslettertxt);
        TextView durum = (TextView) v.findViewById(R.id.durum);

        ilkhrf.setText("" + taskItems.get(i).getBaslik().charAt(0));
        if (taskItems.get(i).getDurum().equals("OK")) {
            durum.setText("Done");

        } else if (taskItems.get(i).getDurum().equals("NO")) {
            durum.setText("Processing");
        }

        return v;
    }
}
