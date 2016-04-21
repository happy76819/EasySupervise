package com.es.easysupervise.uil;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.es.easysupervise.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xiaoxi on 15-12-6.
 */
public class Util {

    public final static int TAKEPIC = 0;
    public final static int CHOOSEPIC = 1;
    public static String PATH = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/EasySupervise";
    public static File SYSPICFILE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    public static String SHAREDPREFERENCENAME = "COM.EASYSUPERVISE";//sharereference名字
    public static String HEADIMAGENAME = "headimagename";//头像图片名字
    public final static String USERNAME = "USERNAME";
    public final static String PWD = "PWD";
    public final static String USERID = "USERID";
    public final static int INFOCOLLECT = 0;
    public final static int TAKEPICTURE = 1;
    public final static int SEARCHDEAL = 2;
    public final static int LIVE = 3;

    public final static String MAINURL = "http://123.130.126.140/cjjcpt/services/client/";
    public final static String doREGISTE = "login_zhuce.action?";
    public final static String doLOGIN = "login_user.action?";
    public final static String doLOADMSG = "loadUserMsg.action?";
    public final static String doUPLOADMSG = "perfectUserMsg.action?";
    public final static String doForgetPWD = "revisePassword.action?";
    public final static String doAdByLocation = "advertLocation.action?";
    public final static String doGetAdMsg = "advertMsg.action?";
    public final static String doSupervise = "advertMsgPerfect.action";
    public final static String doLiveGetMsg = "xianchangqueren.action?";
    public final static String doLiveUpload = "lawCheckedMsg.action?";//确认合法 违法
    public final static String doCollect = "caijiapp.action?";
    public final static String doGETLAW = "fagui.action?";
    public final static String doCheckedMsg = "querenCheckedMsg.action?";
    public final static String doCheckGuanggaoMSG = "checkedGuanggaoMsg.action?";
    public final static String doYICAIJI = "yiCollectionMsg.action?";//已采集任务
    public final static String doWEICAIJI = "";
    public final static String doYIJIANCE = "yiJiance.action?";//已监测任务
    public final static String doWEIJIANCE = "weiJiance.action?";//未监测任务
    public final static String doYISHENHE = "yishenhe.action?";//已审核任务
    public final static String doWEISHENHE = "weishenhe.action?";//未审核任务

    public final static int REGISTE = 0;//注册
    public final static int LOGIN = 1;//登录
    public final static int LOADMSG = 2;//加载信息
    public final static int UPLOADMSG = 3;
    public final static int FORGETPWD = 4;//忘记密码
    public final static int ADBYLOACTION = 5;
    public final static int GETADMSG = 6;
    public final static int SUPERVISE = 7;
    public final static int LIVEGETMSG = 8;
    public final static int LIVEUPLOAD = 9;//确认合法 违法
    public final static int COLLECTAD = 10;
    public final static int LAW = 11;
    public final static int CHECKEDMSG = 12;
    public final static int CHECKGUANGGAO = 13;//根据经纬度查询待审核的
    public final static int GETYICAIJI = 14;//已采集任务
    public final static int GETWEICAIJI = 15;
    public final static int GETYIJIANCE = 16;//已监测任务
    public final static int GETWEIJIANCE = 17;//未监测任务
    public final static int GETYISHENHE = 18;//已审核任务
    public final static int GETWEISHENHE = 19;//未审核任务


    public final static int YICAIJI = 1;
    public final static int WEICAIJI = 2;
    public final static int YIJIANCE = 3;
    public final static int WEIJIANCE = 4;
    public final static int YISHENHE = 5;
    public final static int WEISHENHE = 6;


    public static final int MEM_CACHE_DEFAULT_SIZE = 6 * 1024 * 1024;//6M 图片内存缓存

    public static void toastShotShow(Context context,String text)
    {
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
    public static void toastLongShow(Context context,String text)
    {
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }


    public static boolean getNetStatus(Context context)
    {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED;

    }

    /**
     * 判断是否安装目标应用
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }


    public static String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }


    /**
     * 得到自定义的progressDialog
     * @param context
     * @param msg
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_anim);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        return loadingDialog;

    }


}
