package gorev.yerservis.com.gorevgo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class SeeAllAdapter extends BaseAdapter {
    String ayint = "";
    ArrayList<TaskItem> taskItems = new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;

    public SeeAllAdapter(ArrayList<TaskItem> taskItems, Activity activity) {
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
        View v = inflater.inflate(R.layout.seeall_row, null);
        TaskItem tas = taskItems.get(i);
        TextView gorev_baslik = (TextView) v.findViewById(R.id.titleSee);
        TextView gorev_icerik = (TextView) v.findViewById(R.id.contentSee);
        TextView gorev_tarihgun = (TextView) v.findViewById(R.id.gunSee);
        TextView gorev_tarihay = (TextView) v.findViewById(R.id.aySee);

        gorev_baslik.setText(tas.getBaslik());
        gorev_icerik.setText(tas.getIcerik());
        gorev_tarihgun.setText("" + tas.getBitistarih().split("/")[0]);
        gorev_tarihay.setText("" + ayParse(tas.getBitistarih().split("/")[1]));

        return v;
    }

    public String ayParse(String ayIng) {
        String ay = "";
        if (ayIng.equals("01")) {
            ay = "Jan";
            ayint = "1";
        }
        if (ayIng.equals("02")) {
            ay = "Feb";
            ayint = "2";
        }
        if (ayIng.equals("03")) {
            ay = "Mar";
            ayint = "3";
        }
        if (ayIng.equals("04")) {
            ay = "Apr";
            ayint = "4";
        }
        if (ayIng.equals("05")) {
            ay = "May";
            ayint = "5";
        }
        if (ayIng.equals("06")) {
            ay = "Jun";
            ayint = "6";
        }
        if (ayIng.equals("07")) {
            ay = "Jul";
            ayint = "7";
        }
        if (ayIng.equals("08")) {
            ay = "Aug";
            ayint = "8";
        }
        if (ayIng.equals("09")) {
            ay = "Sep";
            ayint = "9";
        }
        if (ayIng.equals("10")) {
            ay = "Oct";
            ayint = "10";
        }
        if (ayIng.equals("11")) {
            ay = "Nov";
            ayint = "11";
        }
        if (ayIng.equals("12")) {
            ay = "Dec";
            ayint = "12";
        }

        return ay;
    }
}
