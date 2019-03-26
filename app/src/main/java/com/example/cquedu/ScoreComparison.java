package com.example.cquedu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ScoreComparison extends AppCompatActivity {
    Intent intent;
    private String myuser;
    MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_comparison);

        application = (MyApplication)this.getApplication();

        intent = getIntent();
        myuser = intent.getStringExtra("CQUID");
        TextView signInfo = findViewById(R.id.c_tv_signinfo);
        signInfo.setText("Current User: "+ myuser);

        Button btn1 = findViewById(R.id.c_bt_return);
        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button myButton = findViewById(R.id.c_bt_return);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        myButton.setBackgroundResource(R.drawable.buttonopy);
                        break;
                    case MotionEvent.ACTION_UP:
                        myButton.setBackgroundResource(R.drawable.buttonbgy);
                        onReturn();
                        break;
                }
                return true;
            }
        });

        Button btn2 = findViewById(R.id.c_bt_compare);
        btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button myButton = findViewById(R.id.c_bt_compare);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        myButton.setBackgroundResource(R.drawable.buttonopy);
                        break;
                    case MotionEvent.ACTION_UP:
                        myButton.setBackgroundResource(R.drawable.buttonbgy);
                        onCompare();
                        break;
                }
                return true;
            }
        });
    }

    public void onCompare(){
        Spinner sp = findViewById(R.id.c_sp_term);
        int element = sp.getSelectedItemPosition();
        String term = String.valueOf((2018-((element+1)/2))*10+(element%2));
        EditText et = findViewById(R.id.c_et_cquid);
        String cquID = et.getText().toString();
        getScore(term, myuser, true);
        getScore(term, cquID, false);
    }

    private void getScore(String term, String user, boolean isMe){
        //getting info
        RequestBody payload = new FormBody.Builder()
                .add("chk","1")
                .add("chkrad","1")
                .add("sel_xnxq", term)
                .add("sel_xs", user)
                .add("txt_xs", user)
                .build();
        Request score = new Request.Builder()
                .url("http://jxgl.cqu.edu.cn/xscj/f_xscjtzd_rpt.aspx")
                .post(payload)
                .addHeader("origin", "http://jxgl.cqu.edu.cn")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .build();
        String data = "";
        try {
            data = new String(application.license.newCall(score).execute().body().bytes(),"gbk");
        }
        catch (Exception e){
            return;
        }

        //initialization
        String stuName = findContent(data, "姓名：", "</td>");
        TextView tv1, tv2;
        if(isMe){
            tv1 = findViewById(R.id.c_tv_name1);
            tv2 = findViewById(R.id.c_tv_GPA1);
        }
        else{
            tv1 = findViewById(R.id.c_tv_name2);
            tv2 = findViewById(R.id.c_tv_GPA2);
        }
        if(stuName == "N/A"){
            tv1.setText("Not at School!");
            tv2.setText("N/A");
            return;
        }
        tv1.setText("["+user+"]"+stuName);
        tv2.setText("N/A");

        data = data.substring(data.indexOf("</table>")+8);
        data = data.substring(data.indexOf("</table>")+8);

        //parsing info and calculate GPA
        double creditT = 0;
        double scoreCreditT = 0;
        int left = data.indexOf("<tr ");
        while (left>=0){
            data = data.substring(left);
            String item = findContent(data, "<tr ", "<tr ");
            if(item == "N/A")
                item = findContent(data, "<tr ", "</table>");
            item = item.substring(item.indexOf("<td")+3);
            item = item.substring(item.indexOf("<td")+3);
            //get credit of the score
            double credit = Double.valueOf(findContent(item, ">","<br>"));
            //get raw score
            for(int i=0; i<8; i++)
                item = item.substring(item.indexOf("<td")+3);
            String scoreRaw = findContent(item, ">","<br>");

            //transform raw score in to grade point for the course
            double scoreCredit;
            switch (scoreRaw) {
                case "优秀":
                    scoreCredit = 4;
                    break;   // Grade A stands for 4.0
                case "良好":
                    scoreCredit = 3.5;
                    break; // Grade B stands for 3.5
                case "中等":
                    scoreCredit = 2.5;
                    break; // Grade C stands for 2.5
                case "及格":
                    scoreCredit = 1;
                    break;   // Grade D stands for 1.0
                case "不及格":
                    scoreCredit = 0;
                    break; // Grade F stands for 0.0
                case "合格":
                    scoreCredit = 3.5;
                    break; // Grade Pass stands for 3.5
                case "不合格":
                    scoreCredit = 0;
                    break; // Grade Fail stands for 0.0
                // If a score is not decided, this course will not be calculated in his/her GPA.
                case "未录入":
                    scoreCredit = 0;
                    credit = 0;
                    break;
                // If a score is given as a number, it will be rounded and transformed
                default:
                    int intScore = (int) (Double.valueOf(scoreRaw) + 0.5);
                    // Scores above 90 are all standing for 4.0
                    if (intScore >= 90)
                        scoreCredit = 4;
                        // Scores between 60 and 90 are given 1.0 - 4.0 each
                    else if (intScore >= 60)
                        scoreCredit = (intScore - 50) * 0.1;
                        // Scores below 60 are all standing for 0.0
                    else
                        scoreCredit = 0;
                    break;
            }
            // adding to the total
            creditT += credit;
            scoreCreditT += scoreCredit*credit;
            // find next item
            data = data.substring(1);
            left = data.indexOf("<tr ");
        }
        // calculate overall GPA as accurate as 0.001
        double termGPA = scoreCreditT/creditT;
        termGPA = (double)Math.round(termGPA * 1000) / 1000;
        String termGPAs=String.valueOf(termGPA);
        while(termGPAs.length()<5)
            termGPAs += "0";
        tv2.setText(termGPAs);
        return;
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

    public void onReturn(){
        finish();
    }
}
