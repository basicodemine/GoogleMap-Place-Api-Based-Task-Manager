package gorev.yerservis.com.gorevgo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.gcm.Task;

import java.util.ArrayList;



public class TaskAdapter extends PagerAdapter {

    ArrayList<TaskItem> taskItems = new ArrayList<>();
    Activity activity;
    TaskSQLite taskSQLite;

    public TaskAdapter(ArrayList<TaskItem> taskItems, Activity activity) {
        this.taskItems = taskItems;
        this.activity = activity;
        taskSQLite = new TaskSQLite(activity);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(activity).inflate(R.layout.task_row, null);
        TextView gorev_baslik = (TextView) view.findViewById(R.id.gorev_baslik);
        TextView gorev_icerik = (TextView) view.findViewById(R.id.gorev_aciklama);
        TextView gorev_tarih = (TextView) view.findViewById(R.id.gorev_tarih);
        TextView haritaac = (TextView) view.findViewById(R.id.haritada_gor);
        final CheckBox gorevbitir = (CheckBox) view.findViewById(R.id.gorev_bitir);


        haritaac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, MapShow.class)
                        .putExtra("class", taskItems.get(position))
                        .putExtra("lng", taskItems.get(position).getBoylam())
                        .putExtra("lat", taskItems.get(position).getEnlem()));
            }
        });


        gorev_baslik.setText(taskItems.get(position).getBaslik());
        gorev_icerik.setText(taskItems.get(position).getIcerik());
        gorev_tarih.setText(taskItems.get(position).getBitistarih());
        if (!taskItems.get(position).getDurum().equals("NO")) {
            gorev_baslik.append(" (DONE)");
            gorevbitir.setChecked(true);
        }

        gorevbitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(activity);
                }

                builder.setTitle("Warning!")
                        .setMessage("Are you sure you want to finish this task and delete it ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                taskSQLite.bildirimSil(taskItems.get(position));
                                RefreshCallback.getInstance().changeState(1);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                gorevbitir.setChecked(false);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return taskItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }


    public String dayParse(String gunIng) {
        String gun = "";

        if (gunIng.equals("Sun")) {
            gun = "Pazar";
        }
        if (gunIng.equals("Mon")) {
            gun = "Pzt";
        }
        if (gunIng.equals("Tue")) {
            gun = "Salı";
        }
        if (gunIng.equals("Wed")) {
            gun = "Çarş";
        }
        if (gunIng.equals("Thu")) {
            gun = "Perş";
        }
        if (gunIng.equals("Fri")) {
            gun = "Cuma";
        }
        if (gunIng.equals("Sat")) {
            gun = "Cmts";
        }

        return gun;

    }

    public String ayParse(String ayIng) {
        String ay = "";
        if (ayIng.equals("Jan")) {
            ay = "Ocak";
        }
        if (ayIng.equals("Feb")) {
            ay = "Şubat";
        }
        if (ayIng.equals("Mar")) {
            ay = "Mart";
        }
        if (ayIng.equals("Apr")) {
            ay = "Nisan";
        }
        if (ayIng.equals("May")) {
            ay = "Mayıs";
        }
        if (ayIng.equals("Jun")) {
            ay = "Haziran";
        }
        if (ayIng.equals("Jul")) {
            ay = "Temmuz";
        }
        if (ayIng.equals("Aug")) {
            ay = "Ağustos";
        }
        if (ayIng.equals("Sep")) {
            ay = "Eylül";
        }
        if (ayIng.equals("Oct")) {
            ay = "Ekim";
        }
        if (ayIng.equals("Nov")) {
            ay = "Kasım";
        }
        if (ayIng.equals("Dec")) {
            ay = "Aralık";
        }

        return ay;
    }
}
