package com.example.cquedu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainFrame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_frame);
    }

    private DialogInterface.OnClickListener click1=new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface arg0,int arg1)
        {
            //quit current log in
            //return to the login page
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };

    private DialogInterface.OnClickListener click2=new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface arg0,int arg1)
        {
            arg0.cancel();
        }
    };

    public void onlogout(View view) {
        AlertDialog.Builder alertdialogbuilder=new AlertDialog.Builder(this);
        alertdialogbuilder.setMessage("Are you sure to log out?");
        alertdialogbuilder.setPositiveButton("Yes",click1);
        alertdialogbuilder.setNegativeButton("No",click2);
        AlertDialog alertdialog1=alertdialogbuilder.create();
        alertdialog1.show();
    }

}
