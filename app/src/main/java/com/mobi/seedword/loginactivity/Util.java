package com.mobi.seedword.loginactivity;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static String getIMEI(Context ctx)
    {

        TelephonyManager mngr = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
        return mngr.getDeviceId();
    }

    public static  boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String getDateTimeString(String Format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(Format);

        Date dt=new Date();

        return sdf.format(dt);

    }

    static public String getVF(Context ctx,String SNTM)
    {
        String  password  = "wrfsu73h63gvsjwnt72gxmqb3239jsvwnsghjunbb";

        String EMAIL = Util.getSetting(ctx,"EMAIL");
        String SNID = Util.getSetting(ctx,"SNID");


        //SNTM
        String a = SNID.substring(Integer.parseInt(SNTM.substring(12,14)) % 9,Integer.parseInt(SNTM.substring(12,14)) % 9 + 1) ;

        String f = SNID.substring(Integer.parseInt(SNTM.substring(11,13)) % 9,Integer.parseInt(SNTM.substring(11,13)) % 9 + 1) ;

        String b = password.substring(Integer.parseInt(SNTM.substring(9,12)) % 23,Integer.parseInt(SNTM.substring(9,12)) % 23 + 1) ;

        String e = password.substring(Integer.parseInt(SNTM.substring(7,10)) % 23,Integer.parseInt(SNTM.substring(7,10)) % 23+1) ;

        String VF = a+b+EMAIL.substring(0,1) + EMAIL.substring(EMAIL.length() - 1,EMAIL.length())+e+f;

        return VF;
    }

    static public String getVF(Context ctx,String SNTM,String SNID,String EMAIL)
    {
        String  password  = "wrfsu73h63gvsjwnt72gxmqb3239jsvwnsghjunbb";

        String a = SNID.substring(Integer.parseInt(SNTM.substring(12,14)) % 9,Integer.parseInt(SNTM.substring(12,14)) % 9 + 1) ;

        String f = SNID.substring(Integer.parseInt(SNTM.substring(11,13)) % 9,Integer.parseInt(SNTM.substring(11,13)) % 9 + 1) ;

        String b = password.substring(Integer.parseInt(SNTM.substring(9,12)) % 23,Integer.parseInt(SNTM.substring(9,12)) % 23 + 1) ;

        String e = password.substring(Integer.parseInt(SNTM.substring(7,10)) % 23,Integer.parseInt(SNTM.substring(7,10)) % 23+1) ;

        String VF = a+b+EMAIL.substring(0,1) + EMAIL.substring(EMAIL.length() - 1,EMAIL.length())+e+f;

        return VF;
    }

    public static void addSetting(Context ctx,String Key,String Value)
    {
        SharedPreferences.Editor editor =
                PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        // 儲存預設顏色
        editor.putString(Key, Value);
        // 寫入設定值
        editor.commit();
    }

    public static String getSetting(Context ctx,String Key)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sharedPreferences.getString(Key,"");
    }

}
