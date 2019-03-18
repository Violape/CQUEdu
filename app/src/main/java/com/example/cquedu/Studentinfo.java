package com.example.cquedu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Studentinfo extends AppCompatActivity {
    protected String field[] = new String[]{"CQU ID","Chinese Name","Gender","Date of Birth","Origin","Faculty","Grade","Class Number"};
    protected String data[] = new String[]{"N/A","N/A","N/A","N/A","N/A","N/A","N/A","N/A"};
    private ArrayList<StudentInfoPair> theList = new ArrayList<StudentInfoPair>();
    private Intent intent;
    private String user, myuser;
    private StudentInfoAdapter adapter;
    private ListView listView;
    MyApplication application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentinfo);

        intent = getIntent();
        myuser = intent.getStringExtra("CQUID");
        user = intent.getStringExtra("CQUID");

        application = (MyApplication)this.getApplication();

        TextView signinfo = findViewById(R.id.i_tv_signinfo);
        signinfo.setText("Current User: "+ myuser);

        adapter = new StudentInfoAdapter(getBaseContext(), R.layout.layout_studentinfoitem, theList);
        listView = findViewById(R.id.i_lv_infolist);
        listView.setAdapter(adapter);

        parseContent(user);

    }

    private void parseContent(String user){
        Request parsemyinfo = new Request.Builder()
                .url("http://jxgl.cqu.edu.cn/xsxj/R_XJDA_CKXSDA_Detail.aspx?id=" + user)
                .get()
                .addHeader("host", "jxgl.cqu.edu.cn")
                .addHeader("connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .addHeader("upgrade-insecure-requests", "1")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36")
                .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                .build();

        String infores = "";

        try {
            infores = new String(application.license.newCall(parsemyinfo).execute().body().bytes(),"gbk");
        }
        catch (Exception e){
            return;
        }

        data[0] = findContent(infores,"号</td><td width='130'>", "<br></td>");
        data[1] = findContent(infores,"名</td><td colspan='2'>", "<br></td>");

        String rawIDCard = findContent(infores,"号</td><td colspan='2' >", "<br></td>");
        if(rawIDCard.length()==18){
            data[2] = findGender(rawIDCard);
            data[3] = findDOB(rawIDCard);
            data[4] = findOrigin(rawIDCard);
        }
        else{
            data[2] = "N/A";
            data[3] = "N/A";
            data[4] = "N/A";
        }

        data[5] = findFaculty(findContent(infores,"部</td><td>", "<br></td>"));

        String rawClass = findContent(infores,"班级</td><td>", "<br></td>");
        if(rawClass.equals("N/A")){
            data[6] = "N/A";
            data[7] = "N/A";
        }
        else{
            data[6] = "20"+rawClass.substring(0,2);
            data[7] = Integer.valueOf(rawClass.substring(rawClass.length()-2)).toString();
        }

        generateContent();
        adapter.notifyDataSetChanged();
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

    private String findGender(String id){
        int digit = Integer.valueOf(id.charAt(16));
        if(digit % 2 == 1)
            return "Male";
        else
            return "Female";
    }

    private String findOrigin(String id){
        int zip = Integer.valueOf(id.substring(0,4));
        switch (zip){
            case 1101: return "Beijing, Beijing";
            case 1201: return "Tianjin, Tianjin";
            case 1301: return "Shijiazhuang, Hebei";
            case 1302: return "Tangshan, Hebei";
            case 1303: return "Qinhuangdao, Hebei";
            case 1304: return "Handan, Hebei";
            case 1305: return "Xingtai, Hebei";
            case 1306: return "Baoding, Hebei";
            case 1307: return "Zhangjiakou, Hebei";
            case 1308: return "Chengde, Hebei";
            case 1309: return "Cangzhou, Hebei";
            case 1310: return "Langfang, Hebei";
            case 1311: return "Hengshui, Hebei";
            case 1401: return "Taiyuan, Shanxi";
            case 1402: return "Datong, Shanxi";
            case 1403: return "Yangquan, Shanxi";
            case 1404: return "Changzhi, Shanxi";
            case 1405: return "Jincheng, Shanxi";
            case 1406: return "Shuozhou, Shanxi";
            case 1407: return "Jinzhong, Shanxi";
            case 1408: return "Yuncheng, Shanxi";
            case 1409: return "Xinzhou, Shanxi";
            case 1410: return "Linfen, Shanxi";
            case 1411: return "Lyuliang, Shanxi";
            case 1501: return "Hohhot, Inner Mongolia";
            case 1502: return "Baotou, Inner Mongolia";
            case 1503: return "Wuhai, Inner Mongolia";
            case 1504: return "Chifeng, Inner Mongolia";
            case 1505: return "Tongliao, Inner Mongolia";
            case 1506: return "Ordos, Inner Mongolia";
            case 1507: return "Hulun Buir, Inner Mongolia";
            case 1508: return "Bayan Nur, Inner Mongolia";
            case 1509: return "Ulanqab, Inner Mongolia";
            case 1522: return "Hinggan, Inner Mongolia";
            case 1525: return "Xilin Gol, Inner Mongolia";
            case 1529: return "Alxa, Inner Mongolia";
            case 3206: return "Nantong, Jiangsu";
            default: return "N/A";
        }
    }

    private String findDOB(String id){
        int year = Integer.valueOf(id.substring(6,10));
        int month = Integer.valueOf(id.substring(10,12));
        int day = Integer.valueOf(id.substring(12,14));
        String y,m,d;
        y = String.valueOf(year);
        switch (month){
            case 1:
                m = "Jan";
                break;
            case 2:
                m = "Feb";
                break;
            case 3:
                m = "Mar";
                break;
            case 4:
                m = "Apr";
                break;
            case 5:
                m = "May";
                break;
            case 6:
                m = "Jun";
                break;
            case 7:
                m = "Jul";
                break;
            case 8:
                m = "Aug";
                break;
            case 9:
                m = "Sep";
                break;
            case 10:
                m = "Oct";
                break;
            case 11:
                m = "Nov";
                break;
            case 12:
                m = "Dec";
                break;
            default:
                m = "N/A";
        }
        d = String.valueOf(day);
        if(m.equals("N/A"))
            return "N/A";
        else
            return m+" "+d+", "+y;
    }

    private String findFaculty(String facultyname){
        switch (facultyname){
            case "公共管理学院": return "Public Affairs";
            case "经济与工商管理学院": return "Economics & BA";
            case "建设管理与房地产学院": return "CM & Real Estate";
            case "外国语学院": return "Foreign Languages";
            case "艺术学院": return "Arts";
            case "美视电影学院": return "Film";
            case "新闻学院": return "Journalism";
            case "法学院": return "Law";
            case "大数据与软件学院": return "Software Eng.";
            case "数学与统计学院": return "Maths & Statistics";
            case "机械工程学院": return "Mechanical Eng.";
            case "光电工程学院": return "Optoelectronic Eng.";
            case "材料科学与工程学院": return "Material Sci. & Eng.";
            case "能源与动力工程学院": return "Energy & Power Eng.";
            case "电气工程学院": return "Electrical Eng.";
            case "微电子与通信工程学院": return "Optoelectronic Eng.";
            case "自动化学院": return "Automation";
            case "计算机学院": return "Computer Sci.";
            case "建筑城规学院": return "Architecture & Urban Planning";
            case "土木工程学院": return "Civil Eng.";
            case "城市建设与环境工程学院": return "Urban Cons. & Env. Eng.";
            case "化学化工学院": return "Chemistry & Chemical Eng.";
            case "生物工程学院": return "Bioengineering";
            case "资源与环境科学学院": return "Resource & Env. Sci.";
            case "体育学院": return "Physical Education";
            case "生命科学学院": return "Life Science";
            case "物理学院": return "Physics";
            case "弘深学院": return "Hongshen Honors";
            case "博雅学院": return "Boya Liberal Arts";
            case "UC联合学院": return "CQU-UC Joint CO-OP";
            case "药学院": return "Pharmaceutical Sci.";
            case "航空航天学院": return "Aerospace Eng.";
            case "汽车工程学院": return "Automotive Eng.";
            default: return "N/A";
        }
    }

    private void generateContent(){
        theList.clear();
        for(int i = 0; i < field.length; i++){
            String f = field[i];
            String d = data[i];
            theList.add(new StudentInfoPair(f,d));
        }
    }

    public void searchStudent(View view){
        EditText edit = findViewById(R.id.i_te_search);
        String target = edit.getText().toString();
        parseContent(target);
    }
}
