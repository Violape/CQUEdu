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

        Button btn = findViewById(R.id.c_bt_return);
        btn.setOnTouchListener(new View.OnTouchListener() {
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
    }

    public void onCompare(View view){
        Spinner sp = findViewById(R.id.c_sp_term);
        int element = sp.getSelectedItemPosition();
        String term = String.valueOf((2018-((element+1)/2))*10+(element%2));
        EditText et = findViewById(R.id.c_et_cquid);
        String cquID = et.getText().toString();
        getScore(term, myuser, true);
        getScore(term, cquID, false);
    }

    private void getScore(String term, String user, boolean isMe){
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
            double credit = Double.valueOf(findContent(item, ">","<br>"));

            for(int i=0; i<8; i++)
                item = item.substring(item.indexOf("<td")+3);
            String scoreRaw = findContent(item, ">","<br>");

            double scoreCredit;
            switch (scoreRaw){
                case "优秀": scoreCredit = 4; break;
                case "良好": scoreCredit = 3.5; break;
                case "中等": scoreCredit = 2.5; break;
                case "及格": scoreCredit = 1; break;
                case "不及格": scoreCredit = 0; break;
                case "合格": scoreCredit = 3.5; break;
                case "不合格": scoreCredit = 0; break;
                case "未录入": scoreCredit = 0; credit = 0; break;
                default:
                    int intScore = (int)(Double.valueOf(scoreRaw)+0.5);
                    if(intScore>=90)
                        scoreCredit = 4;
                    else if(intScore>=60)
                        scoreCredit = (intScore-50)*0.1;
                    else
                        scoreCredit = 0;
                    break;
            }

            creditT += credit;
            scoreCreditT += scoreCredit*credit;

            data = data.substring(1);
            left = data.indexOf("<tr ");
        }

        double termGPA = scoreCreditT/creditT;
        termGPA = (double)Math.round(termGPA * 1000) / 1000;
        tv2.setText(String.valueOf(termGPA));
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
