# MsgPool
短信转发至指定邮箱

还记得早期公司因为产品的测试要求，采购了20多部当时主流的旗舰手机。
其中有几部手机是插了sim卡，主要是用于各平台账号注册用的。
由于测试手机都是流转用的不会固定在某个人手中导致我们平时接收短信验证码比较麻烦，从而产生了这个工具。

代码不在老，重在能跑。我看很多api的接口已经废弃了，好在还能在最新的Android10上运行起来。

## 配置发送邮件的smtp参数

```java

public class SendMail {

    //用于发送邮件的smtp参数，这里以126为例
    private static String host = "smtp.126.com";
    private static String port = "25";
    private static String account = "*@126.com";
    private static String pwd = "*";

    public static String SendMail(Context context, String subject, String content,String from) {

        //默认的用于接收邮件的地址,可以在软件里面手动配置
        SharedPreferences mPreferences = context.getSharedPreferences("msgpool_setting", 0);
        String device_no = mPreferences.getString("device_no", "Fred's");
        String mail_to = mPreferences.getString("mail_to", "*@qq.com");
        String self_phone_no = mPreferences.getString("self_phone_no", "");

        ...
    }
    
```

### 编译打包安卓并设置权限

![pre](https://github.com/fanfq/MsgPool/blob/master/1.png)

### 手动配置接收邮件的

![pre](https://github.com/fanfq/MsgPool/blob/master/2.png)

### 测试

![pre](https://github.com/fanfq/MsgPool/blob/master/3.png)

这里开了自启动，所以每次手机重启都会收到邮件的推送。

当接收到新的短信时，也会收到推送。


