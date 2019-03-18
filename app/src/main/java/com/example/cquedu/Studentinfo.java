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
    protected String field[] = new String[]{"CQU ID","Chinese Name","Gender","Date of Birth","Origin","Faculty","Grade","Class Number"};
    protected String data[] = new String[]{"N/A","N/A","N/A","N/A","N/A","N/A","N/A","N/A"};
    private ArrayList<StudentInfoPair> theList = new ArrayList<StudentInfoPair>();
    private Intent intent;
    private String user, myuser;
    private StudentInfoAdapter adapter;
    private ListView listView;
    MyApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentinfo);

        intent = getIntent();
        myuser = intent.getStringExtra("CQUID");
        user = intent.getStringExtra("CQUID");

        application = (MyApplication)this.getApplication();

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
            infores = new String(application.license.newCall(parsemyinfo).execute().body().bytes(),"gbk");
        }
        catch (Exception e){
            return;
        }

        data[0] = findContent(infores,"号</td><td width='130'>", "<br></td>");
        data[1] = findContent(infores,"名</td><td colspan='2'>", "<br></td>");

        String rawIDCard = findContent(infores,"号</td><td colspan='2' >", "<br></td>");
        if(rawIDCard.length()==18){
            data[2] = findGender(rawIDCard);
            data[3] = findDOB(rawIDCard);
            data[4] = findOrigin(rawIDCard);
        }
        else{
            data[2] = "N/A";
            data[3] = "N/A";
            data[4] = "N/A";
        }

        data[5] = findContent(infores,"部</td><td>", "<br></td>");

        String rawClass = findContent(infores,"班级</td><td>", "<br></td>");
        if(rawClass.equals("N/A")){
            data[6] = "N/A";
            data[7] = "N/A";
        }
        else{
            data[6] = "20"+rawClass.substring(0,1);
            data[7] = Integer.valueOf(rawClass.substring(rawClass.length()-2)).toString();
        }

        adapter.notifyDataSetChanged();
    }

    private String findContent(String src, String start, String end){
        int left = src.indexOf(start)+start.length();
        if(left < 0)
            return "N/A";
        String sub = src.substring(left);
        int right = sub.indexOf(end);
        if(right < 0)
            return "N/A";
        else
            return sub.substring(0, right);
    }

    private String findGender(String id){
        int digit = Integer.valueOf(id.charAt(16));
        if(digit % 2 == 1)
            return "Male";
        else
            return "Female";
    }

    private String findOrigin(String id){
        int zip = Integer.valueOf(id.substring(0,3));
        switch (zip){
            case 3206: return "Nantong, Jiangsu";
            default: return "N/A";
        }
    }

    private String findDOB(String id){
        int year = Integer.valueOf(id.substring(6,7));
        int month = Integer.valueOf(id.substring(8,9));
        int day = Integer.valueOf(id.substring(10,11));
        String y,m,d;
        y = String.valueOf(year);
        switch (month){
            case 1:
                m = "Jan";
                break;
            case 2:
                m = "Feb";
                break;
            case 3:
                m = "Mar";
                break;
            case 4:
                m = "Apr";
                break;
            case 5:
                m = "May";
                break;
            case 6:
                m = "Jun";
                break;
            case 7:
                m = "Jul";
                break;
            case 8:
                m = "Aug";
                break;
            case 9:
                m = "Sep";
                break;
            case 10:
                m = "Oct";
                break;
            case 11:
                m = "Nov";
                break;
            case 12:
                m = "Dec";
                break;
            default:
                m = "N/A";
        }
        d = String.valueOf(day);
        if(m.equals("N/A"))
            return "N/A";
        else
            return m+" "+d+", "+y;
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
