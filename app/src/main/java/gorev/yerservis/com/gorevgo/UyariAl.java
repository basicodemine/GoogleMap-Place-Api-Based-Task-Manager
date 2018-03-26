package gorev.yerservis.com.gorevgo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class UyariAl extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, MyService.class);
        context.startService(background);
    }
}
