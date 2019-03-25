package com.example.cquedu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreComparison extends AppCompatActivity {
    Intent intent;
    private String myuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_comparison);

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

    public void onReturn(){
        finish();
    }
}
