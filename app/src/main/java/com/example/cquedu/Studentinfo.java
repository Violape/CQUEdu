package com.example.cquedu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class Studentinfo extends AppCompatActivity {
    protected String field[] = new String[]{"aa","bb","cc","dd","ee","ff","gg","hh"};
    protected String data[] = new String[]{"AA","BB","CC","DD","EE","FF","GG","HH"};
    private ArrayList<StudentInfoPair> theList = new ArrayList<StudentInfoPair>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentinfo);
        generateContent();
        StudentInfoAdapter adapter = new StudentInfoAdapter(getBaseContext(), R.layout.layout_studentinfoitem, theList);
        ListView listView = findViewById(R.id.i_lv_infolist);
        listView.setAdapter(adapter);
    }

    private void generateContent(){
        for(int i=0; i < field.length; i++){
            String f = field[i];
            String d = data[i];
            theList.add(new StudentInfoPair(f,d));
        }
    }
}
