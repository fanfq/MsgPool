/*
Copyright 2012 fangqing.fan@hotmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.fanfq.util.mail;

import android.R;
import android.content.Context;
import android.content.SharedPreferences;

import com.fanfq.util.mail.MultiMailsender.MultiMailSenderInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendMail {

    private static String host = "smtp.126.com";
    private static String port = "25";
    private static String account = "@126.com";
    private static String pwd = "";

//	private static String host = "smtp.qq.com";
//	private static String port = "25";
//	private static String account = "@qq.com";
//	private static String pwd = "";



    public static String SendMail(Context context, String subject, String content,String from) {

        SharedPreferences mPreferences = context.getSharedPreferences("msgpool_setting", 0);
        String device_no = mPreferences.getString("device_no", "Fred's");
        String mail_to = mPreferences.getString("mail_to", "@qq.com");
        String self_phone_no = mPreferences.getString("self_phone_no", "");

        //这个类主要是设置邮件
        MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
        mailInfo.setMailServerHost(host);
        mailInfo.setMailServerPort(port);
        mailInfo.setValidate(true);
        mailInfo.setUserName(account);
        mailInfo.setPassword(pwd);//您的邮箱密码
        mailInfo.setFromAddress(account);
        mailInfo.setToAddress(mail_to);

        if(subject.equals(null)||subject=="") {
            subject = getYzmFromSms(content);
        }
        mailInfo.setSubject(device_no + " " + self_phone_no+" -[" + subject + "]");
        mailInfo.setContent(device_no + " " + self_phone_no+ "\n\n-" + content + "\n\n-" + from);
//	      设置CC对象
//	      String[] receivers = new String[]{context.getResources().getString(string.mailto)};
//	      String[] ccs = receivers; 
//	      mailInfo.setReceivers(receivers);
//	      mailInfo.setCcs(ccs);
        //这个类主要来发送邮件
        MultiMailsender sms = new MultiMailsender();
        boolean status = sms.sendTextMail(mailInfo);//发送文体格式
//	      MultiMailsender.sendHtmlMail(mailInfo);//发送html格式
//	      MultiMailsender.sendMailtoMultiCC(mailInfo);//发送抄送
        return status ? "1" : "0";
    }

    private static String getYzmFromSms(String smsBody) {
        Pattern pattern = Pattern.compile("\\d{6}");
        Matcher matcher = pattern.matcher(smsBody);

        if (matcher.find()) {
            return matcher.group();
        }

        pattern = Pattern.compile("\\d{4}");
        matcher = pattern.matcher(smsBody);

        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }
}
