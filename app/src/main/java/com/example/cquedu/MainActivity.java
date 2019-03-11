package com.example.cquedu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSignIn(View v){
        EditText id = findViewById(R.id.TypeCQUID);
        String sid = id.getText().toString();
        EditText psw = findViewById(R.id.TypePassword);
        String spsw = psw.getText().toString();
        if(sid.length()==0){
            Toast tot = Toast.makeText(this, "Please input your CQUID!", Toast.LENGTH_SHORT);
            tot.show();
            return;
        }
        if(spsw.length()==0){
            Toast tot = Toast.makeText(this, "Please input your password!", Toast.LENGTH_SHORT);
            tot.show();
            return;
        }
        if(matchpassword(sid, spsw)){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, MainFrame.class);
            startActivity(intent);
        }
        return;
    }

    private boolean matchpassword(String id, String psw){
        return true;
    }


}
