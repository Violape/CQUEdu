package com.example.cquedu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CourseScores extends AppCompatActivity {
    protected String course[] = new String[20];
    protected String score[] = new String[20];
    Intent intent;
    private String myuser;
    private ArrayList<StudentInfoPair> theList = new ArrayList<StudentInfoPair>();
    private StudentInfoAdapter adapter;
    private ListView listView;
    MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_scores);

        application = (MyApplication)this.getApplication();

        intent = getIntent();
        myuser = intent.getStringExtra("CQUID");
        TextView signinfo = findViewById(R.id.p_tv_signinfo);
        signinfo.setText("Current User: "+ myuser);

        Button btn = findViewById(R.id.p_bt_return);
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button mybtn = findViewById(R.id.p_bt_return);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mybtn.setBackgroundResource(R.drawable.buttonopo);
                        break;
                    case MotionEvent.ACTION_UP:
                        mybtn.setBackgroundResource(R.drawable.buttonbgo);
                        onReturn();
                        break;
                }
                return true;
            }
        });

        adapter = new StudentInfoAdapter(getBaseContext(), R.layout.layout_studentinfoitem, theList);
        listView = findViewById(R.id.p_lv_score);
        listView.setAdapter(adapter);
    }

    public void onSearch(View view){
        for(int i=0; i<20; i++){
            course[i] = null;
            score[i] = null;
        }
        Spinner yr = findViewById(R.id.p_sp_year);
        Spinner sm = findViewById(R.id.p_sp_semester);
        int year = 2018 - yr.getSelectedItemPosition();
        int term = sm.getSelectedItemPosition();
        RequestBody payload = new FormBody.Builder()
                .add("SJ","0")
                .add("SelXNXQ","2")
                .add("sel_xn",String.valueOf(year))
                .add("sel_xq",String.valueOf(term))
                .add("zfx_flag", "0")
                .build();
        Request myScore = new Request.Builder()
                .url("http://jxgl.cqu.edu.cn/xscj/Stu_MyScore_rpt.aspx")
                .post(payload)
                .addHeader("origin", "http://jxgl.cqu.edu.cn")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .build();
        String infores = "";
        try {
            infores = new String(application.license.newCall(myScore).execute().body().bytes(),"gbk");
        }
        catch (Exception e){
            return;
        }
        parseData(infores);
        generateContent();
        adapter.notifyDataSetChanged();
    }

    private void parseData(String data){
        String grades = findContent(data, "ID='ID_Table'", "</table>");
        if (grades == "N/A"){
            return;
        }
        grades = grades.substring(grades.indexOf("<tr")+3);
        grades = grades.substring(grades.indexOf("<tr")+3);
        int left = grades.indexOf("<tr ");
        int cnt = 0;
        while (left >= 0){
            String item = findContent(grades, "<tr ", "</tr>");
            item = item.substring(item.indexOf("<td")+3);
            item = item.substring(item.indexOf("<td")+3);
            course[cnt] = findContent(item, "]","<br>");
            item = item.substring(item.indexOf("<td")+3);
            course[cnt] += "\n(" + findContent(item, ">", "<br>") + " credits)";
            item = item.substring(item.indexOf("<td")+3);
            item = item.substring(item.indexOf("<td")+3);
            item = item.substring(item.indexOf("<td")+3);
            item = item.substring(item.indexOf("<td")+3);
            score[cnt] = findContent(item, ">", "<br>");
            cnt++;
            grades = grades.substring(grades.indexOf("</tr>")+5);
            left = grades.indexOf("<tr ");
        }
    }

    private void generateContent(){
        theList.clear();
        for(int i = 0; i < course.length; i++){
            if(course[i] == null)
                break;
            String f = course[i];
            String d = score[i];
            theList.add(new StudentInfoPair(f,d));
        }
    }

    public void onReturn(){
        finish();
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
}
