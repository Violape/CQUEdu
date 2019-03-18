package com.example.cquedu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Studentinfo extends AppCompatActivity {
    protected String field[] = new String[]{"CQU ID","Chinese Name","Gender","Date of Birth","Origin","Profession","Grade","Class Number"};
    protected String data[] = new String[]{"N/A","N/A","N/A","N/A","N/A","N/A","N/A","N/A"};
    private ArrayList<StudentInfoPair> theList = new ArrayList<StudentInfoPair>();
    private Intent intent;
    private String user, myuser;
    private OkHttpClient okhttp;
    private StudentInfoAdapter adapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentinfo);

        intent = getIntent();
        myuser = intent.getStringExtra("CQUID");
        user = intent.getStringExtra("CQUID");

        okhttp = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }
                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                .build();

        TextView signinfo = findViewById(R.id.i_tv_signinfo);
        signinfo.setText("Current User: "+ myuser);

        parseContent();
        generateContent();

        adapter = new StudentInfoAdapter(getBaseContext(), R.layout.layout_studentinfoitem, theList);
        listView = findViewById(R.id.i_lv_infolist);
        listView.setAdapter(adapter);
    }

    private void parseContent(){
        Request parsemyinfo = new Request.Builder()
                .url("http://jxgl.cqu.edu.cn/xsxj/R_XJDA_CKXSDA_Detail.aspx?id=" + user)
                .get()
                .addHeader("host", "jxgl.cqu.edu.cn")
                .addHeader("connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                .build();
        String infores = "";
        try {
            infores = new String(okhttp.newCall(parsemyinfo).execute().body().bytes(),"gbk");
        }
        catch (Exception e){
            return;
        }
        infores += "";
        adapter.notifyDataSetChanged();
    }

    private void generateContent(){
        theList.clear();
        for(int i = 0; i < field.length; i++){
            String f = field[i];
            String d = data[i];
            theList.add(new StudentInfoPair(f,d));
        }
    }
}
