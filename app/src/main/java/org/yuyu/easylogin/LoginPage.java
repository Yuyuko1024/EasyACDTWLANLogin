package org.yuyu.easylogin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import org.yuyu.easylogin.login.LoginCore;
import org.yuyu.easylogin.util.CallbackInterface;

public class LoginPage extends AppCompatActivity implements CallbackInterface {

    AppCompatImageButton back;
    WebView webView;
    MaterialTextView mMsgTxt,mSrvIP,mWebTitle;
    static ProgressBar progressBar;
    String webUrl = "";
    String Msg = "";
    String authIPAddr= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        LoginCore loginCore = new LoginCore();
        loginCore.setCallbackInterface(this);
        back=findViewById(R.id.back);
        back.setOnClickListener(new BackFunc());
        progressBar=findViewById(R.id.status_bar);
        webView=findViewById(R.id.webview);
        mMsgTxt=findViewById(R.id.status_text);
        mSrvIP=findViewById(R.id.status_text_2);
        mWebTitle=findViewById(R.id.status_text_3);
        WebViewSettings();
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setIndeterminate(false);
                progressBar.setProgress(newProgress*100);
                String webTitle=getString(R.string.web_title_text)+webView.getTitle();
                mWebTitle.setText(webTitle);
            }
        });
    }

    private void WebViewSettings(){
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    private class BackFunc implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                case 1:
                    String respMsg=getString(R.string.srv_resp_text)+Msg;
                    String srvIPAdr=getString(R.string.srv_ip_text)+authIPAddr;
                    mMsgTxt.setText(respMsg);
                    mSrvIP.setText(srvIPAdr);
                    webView.loadUrl(webUrl);
                    break;
            }
        }
    };

    @Override
    public void resposeMsg(String url, String msg,String authIP, boolean isOk) {
        Log.e("GetMsg",msg);
        Msg=msg;
        webUrl=url;
        authIPAddr=authIP;
        if (isOk){
            handler.sendEmptyMessage(0);
        }else{
            handler.sendEmptyMessage(1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}