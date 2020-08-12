package com.ht.msgpool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.fanfq.util.mail.SendMail;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.d("BootReceiver", "system boot completed");

            // context, AutoRun.class
            Intent newIntent = new Intent(context, MainActivity.class);

            /* MyActivity action defined in AndroidManifest.xml */
            newIntent.setAction("android.intent.action.MAIN");

            /* MyActivity category defined in AndroidManifest.xml */
            newIntent.addCategory("android.intent.category.LAUNCHER");

            /*
             * If activity is not launched in Activity environment, this flag is
             * mandatory to set
             */
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            /* if you want to start a service, follow below method */
            //context.startActivity(newIntent);
            Intent startIntent = new Intent(context, MsgService.class);
            context.startService(startIntent);

            final Context context_ = context;
            new Thread(new Runnable() {
            @Override
            public void run() {
                SendMail.SendMail(context_,"上线了！","","");
            }
        }).start();

        }
    }
}
