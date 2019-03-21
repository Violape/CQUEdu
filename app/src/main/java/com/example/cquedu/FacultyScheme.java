package com.example.cquedu;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

public class FacultyScheme extends AppCompatActivity implements TbsReaderView.ReaderCallback {
    private TbsReaderView mTbsReaderView;
    private String tbsReaderTemp;
    private Intent intent;
    private String myuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_scheme);

        intent = getIntent();
        myuser = intent.getStringExtra("CQUID");
        TextView signinfo = findViewById(R.id.i_tv_signinfo);
        signinfo.setText("Current User: "+ myuser);

        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                //初始化完成回调接口
            }

            @Override
            public void onViewInitFinished(boolean b) {
                //初始化完成回调
            }
        });
        mTbsReaderView = new TbsReaderView(this, this);
        tbsReaderTemp = Environment.getExternalStorageDirectory() + "/TbsReaderTemp";
        RelativeLayout rel = findViewById(R.id.s_rl_pdf);
        rel.addView(mTbsReaderView, new RelativeLayout.LayoutParams(-1,-1));
    }

    @Override
    public void onCallBackAction(Integer i, Object o1, Object o2){
        ;
    }

    public void getScheme(View view){
        Spinner spinner = findViewById(R.id.s_sp_faculty);
        int item = spinner.getSelectedItemPosition();
        String targetpage = "";
        switch (item){
            case 0: targetpage = "01公管"; break;
            case 1: targetpage = "02经管"; break;
            case 2: targetpage = "03建管"; break;
            case 3: targetpage = "04外语"; break;
            case 4: targetpage = "05艺术"; break;
            case 5: targetpage = "06美视电影"; break;
            case 6: targetpage = "07新闻"; break;
            case 7: targetpage = "08法学院"; break;
            case 8: targetpage = "09软件"; break;
            case 9: targetpage = "10数统"; break;
            case 10: targetpage = "11机械"; break;
            case 11: targetpage = "12光电"; break;
            case 12: targetpage = "13材料"; break;
            case 13: targetpage = "14动力"; break;
            case 14: targetpage = "15电气"; break;
            case 15: targetpage = "16通信"; break;
            case 16: targetpage = "17自动化"; break;
            case 17: targetpage = "18计算机"; break;
            case 18: targetpage = "19建筑城规"; break;
            case 19: targetpage = "20土木"; break;
            case 20: targetpage = "21城环"; break;
            case 21: targetpage = "22化学"; break;
            case 22: targetpage = "23生物"; break;
            case 23: targetpage = "24资环"; break;
            case 24: targetpage = "25体育"; break;
            case 25: targetpage = "29生命科学"; break;
            case 26: targetpage = "30物理"; break;
            case 27: targetpage = "37航空"; break;
            case 28: targetpage = "38汽车"; break;
            default: break;
        }
        String filePath = "";
        String fileName = targetpage+".pdf";
        String bsReaderTemp = tbsReaderTemp;
        File bsReaderTempFile =new File(bsReaderTemp);
        if (!bsReaderTempFile.exists()) {
            boolean mkdir = bsReaderTempFile.mkdir();
        }
        Bundle bundle = new Bundle();
        bundle.putString("filePath", filePath);
        bundle.putString("tempPath", tbsReaderTemp);
        boolean result = mTbsReaderView.preOpen(getFileType(fileName), false);
        if (result)
            mTbsReaderView.openFile(bundle);
    }

    private String getFileType(String paramString) {
        String str = "";
        if (TextUtils.isEmpty(paramString)) {
            return str;
        }
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = paramString.substring(i + 1);
        return str;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTbsReaderView.onStop();
    }

    public void onReturn(View view){
        finish();
    }
}
