package com.nml.wv;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

/**
 * Android与WebView的js交互
 * @author admin
 */
public class MainActivity extends AppCompatActivity implements JsBridge{

    private static final String TAG = "MainActivity";

    private WebView webView;
    private TextView tvResult;
    private EditText etContent;
    private Button btnInvokeJs;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate()");
        EventBusUtil.register(this);

        findViews();
        init();
        addListener();

    }

    private void findViews() {
        webView = findViewById(R.id.webView);
        tvResult = findViewById(R.id.tv_result);

        etContent = findViewById(R.id.et_content);
        btnInvokeJs = findViewById(R.id.btn_invoke_js);
    }

    private void init() {
        mHandler = new Handler();

        //步骤1：允许WebView加载js代码
        webView.getSettings().setJavaScriptEnabled(true);

        //步骤3：给WebView添加js接口
        webView.addJavascriptInterface(new NmlInterface(this),"android");

        webView.loadUrl("file:///android_asset/index.html");

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            //打开允许调试的开关 API19及以上
            webView.setWebContentsDebuggingEnabled(true);
        }

    }

    @Override
    public void setTextViewValue(final String value) {
        //在主线程执行 更新UI
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tvResult.setText(value);
            }
        });
    }

    private void addListener() {
        //发送
        btnInvokeJs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(str)){
                    Toast.makeText(MainActivity.this,"请填写发送的内容！",Toast.LENGTH_SHORT).show();
                    return;
                }
                webView.loadUrl("javascript:if(window.remote){window.remote('"+str+"')}");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    @Subscribe
    public void setText(String value){
        Log.d(TAG,"setText()");
        if (!TextUtils.isEmpty(value)){
            tvResult.setText(value);
        }
    }

}
