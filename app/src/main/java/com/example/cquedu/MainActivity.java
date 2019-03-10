package com.example.cquedu;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickSignIn(View v){
        EditText id = findViewById(R.id.TypeCQUID);
        String sid = id.getText().toString();
        EditText psw = findViewById(R.id.TypePassword);
        String spsw = id.getText().toString();
        if(sid.length()==0){
            Toast tot = Toast.makeText(context, "Please input your CQUID!", Toast.LENGTH_SHORT);
            tot.show();
            return;
        }
        if(spsw.length()==0){
            Toast tot = Toast.makeText(context, "Please input your password!", Toast.LENGTH_SHORT);
            tot.show();
            return;
        }
        matchpassword(sid, spsw);
        return;
    }

    private boolean matchpassword(String id, String psw){
        return true;
    }


}
