package com.example.cquedu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ClassSchedule extends AppCompatActivity {
    private Intent intent;
    private String myuser;
    MyApplication application;
    String[] courseList = new String[15];
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_schedule);

        application = (MyApplication)this.getApplication();

        intent = getIntent();
        myuser = intent.getStringExtra("CQUID");
        TextView signinfo = findViewById(R.id.t_tv_signinfo);
        signinfo.setText("Current User: "+ myuser);

        Button btn = findViewById(R.id.t_bt_return);
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button mybtn = findViewById(R.id.t_bt_return);
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mybtn.setBackgroundResource(R.drawable.buttononpressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        mybtn.setBackgroundResource(R.drawable.buttonbg);
                        onReturn();
                        break;
                }
                return true;
            }
        });

        Spinner spinner = findViewById(R.id.t_sp_week);
        spinner.setSelection(4,true);
        resetTable();
        String page = getpage(5);
        page = parseData(page, false);
        page = parseData(page, true);
    }

    public void onReturn(){
        finish();
    }

    public void onFindSchedule(View view){
        resetTable();
        Spinner spinner = findViewById(R.id.t_sp_week);
        int item = spinner.getSelectedItemPosition()+1;
        String page = getpage(item);
        page = parseData(page, false);
        page = parseData(page, true);
    }

    private void resetTable(){
        for(int i=1; i<8; i++){
            for(int j=1; j<6; j++){
                String targetblock = "t_tv_c" + String.valueOf(i) + String.valueOf(j);
                int targetid = getResources().getIdentifier(targetblock,"id", getPackageName());
                TextView targetitem = findViewById(targetid);
                targetitem.setText("");
                if(i>5)
                    targetitem.setBackgroundColor(getResources().getColor(R.color.themec5l1));
                else{
                    if(i%2==1)
                        targetitem.setBackgroundColor(getResources().getColor(R.color.themec4l2));
                    else
                        targetitem.setBackgroundColor(getResources().getColor(R.color.themec4l1));
                }
            }
        }
        for(int i=0; i<15; i++)
            courseList[i] = null;
        cnt = 0;
    }

    private String getpage(int week){
        RequestBody payload = new FormBody.Builder()
                .add("Sel_XNXQ","20181")
                .add("px","1")
                .add("rad","on")
                .add("zc_flag","1")
                .add("zc_input", String.valueOf(week))
                .build();
        Request getSchedule = new Request.Builder()
                .url("http://jxgl.cqu.edu.cn/znpk/Pri_StuSel_rpt.aspx")
                .post(payload)
                .addHeader("origin", "http://jxgl.cqu.edu.cn")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .build();
        String infores = "";
        try {
            infores = new String(application.license.newCall(getSchedule).execute().body().bytes(),"gbk");
        }
        catch (Exception e){
            return null;
        }
        return infores;
    }

    private String parseData(String data, boolean exp){
        int left = data.indexOf("<TABLE class='page_table'>");
        if(left < 0)
            return data;
        data = data.substring(left);

        left = data.indexOf("<tbody>");
        if(left < 0)
            return data;
        data = data.substring(left);

        left = data.indexOf("</tbody>");
        while(left > 0){
            String item = findContent(data, "<tr >", "</tr>");
            item = item.substring(item.indexOf("<td")+3);
            item = item.substring(item.indexOf("<td")+3);
            String course = findContent(item, "' >", "<br>");
            if(course == ""){
                course = findContent(item, "hidevalue=\"", "\">");
            }
            else{
                if(exp){
                    for(int i = 0; i < 9; i++)
                        item = item.substring(item.indexOf("<td")+3);
                }
                else{
                    for(int i = 0; i < 10; i++)
                        item = item.substring(item.indexOf("<td")+3);
                }
            }
            String datetime = findContent(item, "' >", "<br>");
            item = item.substring(item.indexOf("<td")+3);
            String location = findContent(item, "' >", "<br>");

            course = course.substring(course.indexOf(']')+1);
            int day = 0;
            switch (datetime.substring(0,1)){
                case "一": day = 1; break;
                case "二": day = 2; break;
                case "三": day = 3; break;
                case "四": day = 4; break;
                case "五": day = 5; break;
                case "六": day = 6; break;
                case "日": day = 7; break;
            }
            int st = Integer.valueOf(findContent(datetime,"[","-"));
            int et = Integer.valueOf(findContent(datetime,"-","节"));
            if(et>10)
                et=10;
            int cur = 0;
            boolean flag = true;
            for(int i = 0; i < cnt; i++){
                if(courseList[i].equals(course)){
                    cur = i;
                    flag = false;
                }
            }
            if(flag) {
                courseList[cnt] = course;
                cur = cnt;
                cnt++;
            }
            String content = course + "\n" + location;
            int sb = (st+1)/2;
            int eb = et/2+1;
            String colorid = String.valueOf(cur%5+1);
            if(cur<5)
                colorid = "themec"+ colorid + "n";
            else
                colorid = "themec"+ colorid + "d" + String.valueOf(cur/5);
            int tcolorid = getResources().getIdentifier(colorid,"color", getPackageName());
            for(int i = sb; i < eb; i++){
                String targetblock = "t_tv_c" + String.valueOf(day) + String.valueOf(i);
                int targetid = getResources().getIdentifier(targetblock,"id", getPackageName());
                TextView targetitem = findViewById(targetid);
                targetitem.setText(content);
                try{
                    targetitem.setBackgroundColor(getResources().getColor(tcolorid));
                }
                catch (Exception e){
                    ;
                }
            }
            data = data.substring(data.indexOf("</tr>")+5);
            left = data.indexOf("</tbody>");
        }
        return data;
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

}
