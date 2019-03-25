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
        String term = String.valueOf((element+1)*5+(element%2));
        EditText et = findViewById(R.id.c_et_cquid);
        String cquID = et.getText().toString();
        double myScore = getScore(term, myuser);
        double yrScore = getScore(term, cquID);
    }

    private double getScore(String term, String user){
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
        String infores = "";
        try {
            infores = new String(application.license.newCall(score).execute().body().bytes(),"gbk");
        }
        catch (Exception e){
            return -1;
        }
        return 0;
    }

    public void onReturn(){
        finish();
    }
}
