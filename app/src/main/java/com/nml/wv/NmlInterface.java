package com.nml.wv;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * @ProjectName: WebViewDemo
 * @Package: com.nml.wv
 * @ClassName: NmlInterface
 * @Description: 步骤2：js接口类
 * 必须加入注解JavascriptInterface，js才能识别
 * @Author: admin
 */
public class NmlInterface {

    private static final String TAG = "NmlInterface";

    private JsBridge mJsBridge;

    public NmlInterface(JsBridge mJsBridge) {
        this.mJsBridge = mJsBridge;
    }

    /**
     * 这个方法不在主线程执行
     * @param value
     */
    @JavascriptInterface
    public void setValue(String value){
        Log.d(TAG,"value="+value);
//        if (mJsBridge!=null){
//            mJsBridge.setTextViewValue(value);
//        }else{
//            Log.d(TAG,"JsBridge no register!");
//        }
        EventBusUtil.post(value);
    }

}
