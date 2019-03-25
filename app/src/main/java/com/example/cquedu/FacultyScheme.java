package com.example.cquedu;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import java.io.File;

public class FacultyScheme extends AppCompatActivity{
    private Intent intent;
    private String myuser;
    MyApplication application;
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_scheme);

        intent = getIntent();
        myuser = intent.getStringExtra("CQUID");
        TextView signinfo = findViewById(R.id.s_tv_signinfo);
        signinfo.setText("Current User: "+ myuser);

        mWebView = findViewById(R.id.s_wb_pdf);
        application = (MyApplication)this.getApplication();

        Button btn = findViewById(R.id.s_bt_return);
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button mybtn = findViewById(R.id.s_bt_return);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mybtn.setBackgroundResource(R.drawable.buttonopg);
                        break;
                    case MotionEvent.ACTION_UP:
                        mybtn.setBackgroundResource(R.drawable.buttonbgg);
                        onReturn();
                        break;
                }
                return true;
            }
        });
    }

    public void getScheme(View view){
        Spinner spinner = findViewById(R.id.s_sp_faculty);
        int item = spinner.getSelectedItemPosition();
        String targetpage = "";
        switch (item){
            case 0: targetpage = "01公管"; break;
            case 1: targetpage = "02经管"; break;
            case 2: targetpage = "03建管"; break;
            case 3: targetpage = "04外语"; break;
            case 4: targetpage = "05艺术"; break;
            case 5: targetpage = "06美视电影"; break;
            case 6: targetpage = "07新闻"; break;
            case 7: targetpage = "08法学院"; break;
            case 8: targetpage = "09软件"; break;
            case 9: targetpage = "10数统"; break;
            case 10: targetpage = "11机械"; break;
            case 11: targetpage = "12光电"; break;
            case 12: targetpage = "13材料"; break;
            case 13: targetpage = "14动力"; break;
            case 14: targetpage = "15电气"; break;
            case 15: targetpage = "16通信"; break;
            case 16: targetpage = "17自动化"; break;
            case 17: targetpage = "18计算机"; break;
            case 18: targetpage = "19建筑城规"; break;
            case 19: targetpage = "20土木"; break;
            case 20: targetpage = "21城环"; break;
            case 21: targetpage = "22化学"; break;
            case 22: targetpage = "23生物"; break;
            case 23: targetpage = "24资环"; break;
            case 24: targetpage = "25体育"; break;
            case 25: targetpage = "29生命科学"; break;
            case 26: targetpage = "30物理"; break;
            case 27: targetpage = "37航空"; break;
            case 28: targetpage = "38汽车"; break;
            default: break;
        }
        String dlpg = "http://jxgl.cqu.edu.cn/_data/NEWS/kcdg/"+targetpage+".pdf";
        download(dlpg);
    }

    private void download(String url) {
        PDFDownloadUtil.download(application.license, url, getCacheDir() + "/temp.pdf", new PDFDownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(final String path) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        preView(path);
                    }
                });
            }

            @Override
            public void onDownloading(int progress) {

            }

            @Override
            public void onDownloadFailed(String msg) {

            }
        });
    }

    private void preView(String pdfUrl) {
        mWebView.loadUrl("file:///android_asset/web/viewer.html?file=" + pdfUrl);
    }

    private String getFileType(String paramString) {
        String str = "";
        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = paramString.substring(i + 1);
        return str;
    }

    public void onReturn(){
        finish();
    }
}
