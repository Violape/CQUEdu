package com.example.cquedu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Studentinfo extends AppCompatActivity {
    private String data[] = {"aa","bb","cc","dd","aa","bb","cc","dd","aa","bb","cc","dd","aa","bb","cc","dd"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentinfo);
        ListView listView = (ListView) findViewById(R.id.i_lv_infolist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,data);
        listView.setAdapter(adapter);
    }
}
