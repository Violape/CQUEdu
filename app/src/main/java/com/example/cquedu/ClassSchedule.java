package com.example.cquedu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ClassSchedule extends AppCompatActivity {
    private Intent intent;
    private String myuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);

        intent = getIntent();
        myuser = intent.getStringExtra("CQUID");
        TextView signinfo = findViewById(R.id.i_tv_signinfo);
        signinfo.setText("Current User: "+ myuser);
    }

    public void onReturn(View view){
        finish();
    }
}
