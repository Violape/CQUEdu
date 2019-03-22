package com.example.cquedu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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

        Button btn = findViewById(R.id.i_bt_return);
        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button mybtn = findViewById(R.id.i_bt_return);
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
    }

    private void parseContent(String user){
        Request parsemyinfo = new Request.Builder()
                .url("http://jxgl.cqu.edu.cn/xsxj/R_XJDA_CKXSDA_Detail.aspx?id=" + user)
                .get()
                .addHeader("host", "jxgl.cqu.edu.cn")
                .addHeader("connection", "keep-alive")
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
            case 1101: return "Beijing";
            case 1201: return "Tianjin";
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
            case 2101: return "Shenyang, Liaoning";
            case 2102: return "Dalian, Liaoning";
            case 2103: return "Anshan, Liaoning";
            case 2104: return "Fushun, Liaoning";
            case 2105: return "Benxi, Liaoning";
            case 2106: return "Dandong, Liaoning";
            case 2107: return "Jinzhou, Liaoning";
            case 2108: return "Yingkou, Liaoning";
            case 2109: return "Fuxin, Liaoning";
            case 2110: return "Liaoyang, Liaoning";
            case 2111: return "Panjin, Liaoning";
            case 2112: return "Tieling, Liaoning";
            case 2113: return "Chaoyang, Liaoning";
            case 2114: return "Huludao, Liaoning";
            case 2201: return "Changchun, Jilin";
            case 2202: return "Jilin City, Jilin";
            case 2203: return "Siping, Jilin";
            case 2204: return "Liaoyuan, Jilin";
            case 2205: return "Tonghua, Jilin";
            case 2206: return "Baishan, Jilin";
            case 2207: return "Songyuan, Jilin";
            case 2208: return "Baicheng, Jilin";
            case 2224: return "Yanbian, Jilin";
            case 2301: return "Harbin, Heilongjiang";
            case 2302: return "Qiqihar, Heilongjiang";
            case 2303: return "Jixi, Heilongjiang";
            case 2304: return "Hegang, Heilongjiang";
            case 2305: return "Shuangyashan, Heilongjiang";
            case 2306: return "Daqing, Heilongjiang";
            case 2307: return "Yichun, Heilongjiang";
            case 2308: return "Jiamusi, Heilongjiang";
            case 2309: return "Qitaihe, Heilongjiang";
            case 2310: return "Mudanjiang, Heilongjiang";
            case 2311: return "Heihe, Heilongjiang";
            case 2312: return "Suihua, Heilongjiang";
            case 2327: return "Daxing'anling, Heilongjiang";
            case 3101: return "Shanghai";
            case 3201: return "Nanjing, Jiangsu";
            case 3202: return "Wuxi, Jiangsu";
            case 3203: return "Xuzhou, Jiangsu";
            case 3204: return "Changzhou, Jiangsu";
            case 3205: return "Suzhou, Jiangsu";
            case 3206: return "Nantong, Jiangsu";
            case 3207: return "Lianyungang, Jiangsu";
            case 3208: return "Huai'an, Jiangsu";
            case 3209: return "Yancheng, Jiangsu";
            case 3210: return "Yangzhou, Jiangsu";
            case 3211: return "Zhenjiang, Jiangsu";
            case 3212: return "Taizhou, Jiangsu";
            case 3213: return "Suqian, Jiangsu";
            case 3301: return "Hangzhou, Zhejiang";
            case 3302: return "Ningbo, Zhejiang";
            case 3303: return "Wenzhou, Zhejiang";
            case 3304: return "Jiaxing, Zhejiang";
            case 3305: return "Huzhou, Zhejiang";
            case 3306: return "Shaoxing, Zhejiang";
            case 3307: return "Jinhua, Zhejiang";
            case 3308: return "Quzhou, Zhejiang";
            case 3309: return "Zhoushan, Zhejiang";
            case 3310: return "Taizhou, Zhejiang";
            case 3311: return "Lishui, Zhejiang";
            case 3401: return "Hefei, Anhui";
            case 3402: return "Wuhu, Anhui";
            case 3403: return "Bengbu, Anhui";
            case 3404: return "Huainan, Anhui";
            case 3405: return "Ma'anshan, Anhui";
            case 3406: return "Huaibei, Anhui";
            case 3407: return "Tongling, Anhui";
            case 3408: return "Anqing, Anhui";
            case 3410: return "Huangshan, Anhui";
            case 3411: return "Chuzhou, Anhui";
            case 3412: return "Fuyang, Anhui";
            case 3413: return "Suzhou, Anhui";
            case 3414: return "Chaohu, Anhui";
            case 3415: return "Lu'an, Anhui";
            case 3416: return "Bozhou, Anhui";
            case 3417: return "Chizhou, Anhui";
            case 3418: return "Xuancheng, Anhui";
            case 3501: return "Fuzhou, Fujian";
            case 3502: return "Xiamen, Fujian";
            case 3503: return "Putian, Fujian";
            case 3504: return "Sanming, Fujian";
            case 3505: return "Quanzhou, Fujian";
            case 3506: return "Zhangzhou, Fujian";
            case 3507: return "Nanping, Fujian";
            case 3508: return "Longyan, Fujian";
            case 3509: return "Ningde, Fujian";
            case 3601: return "Nanchang, Jiangxi";
            case 3602: return "Jingdezhen, Jiangxi";
            case 3603: return "Pingxiang, Jiangxi";
            case 3604: return "Jiujiang, Jiangxi";
            case 3605: return "Xinyu, Jiangxi";
            case 3606: return "Yingtan, Jiangxi";
            case 3607: return "Ganzhou, Jiangxi";
            case 3608: return "Ji'an, Jiangxi";
            case 3609: return "Yichun, Jiangxi";
            case 3610: return "Fuzhou, Jiangxi";
            case 3611: return "Shangrao, Jiangxi";
            case 3701: return "Jinan, Shandong";
            case 3702: return "Qingdao, Shandong";
            case 3703: return "Zibo, Shandong";
            case 3704: return "Zaozhuang, Shandong";
            case 3705: return "Dongying, Shandong";
            case 3706: return "Yantai, Shandong";
            case 3707: return "Weifang, Shandong";
            case 3708: return "Jining, Shandong";
            case 3709: return "Tai'an, Shandong";
            case 3710: return "Weihai, Shandong";
            case 3711: return "Rizhao, Shandong";
            case 3712: return "Laiwu, Shandong";
            case 3713: return "Linyi, Shandong";
            case 3714: return "Dezhou, Shandong";
            case 3715: return "Liaocheng, Shandong";
            case 3716: return "Binzhou, Shandong";
            case 3717: return "Heze, Shandong";
            case 4101: return "Zhengzhou, Henan";
            case 4102: return "Kaifeng, Henan";
            case 4103: return "Zhengzhou, Henan";
            case 4104: return "Pingdingshan, Henan";
            case 4105: return "Anyang, Henan";
            case 4106: return "Hebi, Henan";
            case 4107: return "Xinxiang, Henan";
            case 4108: return "Jiaozuo, Henan";
            case 4109: return "Puyang, Henan";
            case 4110: return "Xuchang, Henan";
            case 4111: return "Luohe, Henan";
            case 4112: return "Sanmenxia, Henan";
            case 4113: return "Nanyang, Henan";
            case 4114: return "Shangqiu, Henan";
            case 4115: return "Xinyang, Henan";
            case 4116: return "Zhoukou, Henan";
            case 4117: return "Zhumadian, Henan";
            case 4190: return "Jiyuan, Henan";
            case 4201: return "Wuhan, Hubei";
            case 4202: return "Huangshi, Hubei";
            case 4203: return "Shiyan, Hubei";
            case 4204: return "Shashi, Hubei";
            case 4205: return "Yichang, Hubei";
            case 4206: return "Xiangyang, Hubei";
            case 4207: return "E'zhou, Hubei";
            case 4208: return "Jingmen, Hubei";
            case 4209: return "Xiaogan, Hubei";
            case 4210: return "Jingzhou, Hubei";
            case 4211: return "Huanggang, Hubei";
            case 4212: return "Xianning, Hubei";
            case 4213: return "Suizhou, Hubei";
            case 4228: return "Enshi, Hubei";
            case 4290: return "Hubei";
            case 4301: return "Changsha, Hunan";
            case 4302: return "Zhuzhou, Hunan";
            case 4303: return "Xiangtan, Hunan";
            case 4304: return "Hengyang, Hunan";
            case 4305: return "Shaoyang, Hunan";
            case 4306: return "Yueyang, Hunan";
            case 4307: return "Changde, Hunan";
            case 4308: return "Zhangjiajie, Hunan";
            case 4309: return "Yiyang, Hunan";
            case 4310: return "Chenzhou, Hunan";
            case 4311: return "Yongzhou, Hunan";
            case 4312: return "Huaihua, Hunan";
            case 4313: return "Loudi, Hunan";
            case 4331: return "Xiangxi, Hunan";
            case 4401: return "Guangzhou, Guangdong";
            case 4402: return "Shaoguan, Guangdong";
            case 4403: return "Shenzhen, Guangdong";
            case 4404: return "Zhuhai, Guangdong";
            case 4405: return "Shantou, Guangdong";
            case 4406: return "Foshan, Guangdong";
            case 4407: return "Jiangmen, Guangdong";
            case 4408: return "Zhanjiang, Guangdong";
            case 4409: return "Maoming, Guangdong";
            case 4412: return "Zhaoqing, Guangdong";
            case 4413: return "Huizhou, Guangdong";
            case 4414: return "Meizhou, Guangdong";
            case 4415: return "Shanwei, Guangdong";
            case 4416: return "Heyuan, Guangdong";
            case 4417: return "Yangjiang, Guangdong";
            case 4418: return "Qingyuan, Guangdong";
            case 4419: return "Dongguan, Guangdong";
            case 4420: return "Zhongshan, Guangdong";
            case 4451: return "Chaozhou, Guangdong";
            case 4452: return "Jieyang, Guangdong";
            case 4453: return "Yunfu, Guangdong";
            case 4501: return "Nanning, Guangxi";
            case 4502: return "Liuzhou, Guangxi";
            case 4503: return "Guilin, Guangxi";
            case 4504: return "Wuzhou, Guangxi";
            case 4505: return "Beihai, Guangxi";
            case 4506: return "Fangchenggang, Guangxi";
            case 4507: return "Qinzhou, Guangxi";
            case 4508: return "Guigang, Guangxi";
            case 4509: return "Yulin, Guangxi";
            case 4510: return "Baise, Guangxi";
            case 4511: return "Hezhou, Guangxi";
            case 4512: return "Hechi, Guangxi";
            case 4513: return "Laibin, Guangxi";
            case 4514: return "Chongzuo, Guangxi";
            case 4601: return "Haikou, Hainan";
            case 4602: return "Sanya, Hainan";
            case 4603: return "Sansha, Hainan";
            case 4604: return "Danzhou, Hainan";
            case 4690: return "Hainan";
            case 5001: return "Chongqing - Urban";
            case 5002: return "Chongqing - Counties";
            case 5101: return "Chengdu, Sichuan";
            case 5103: return "Zigong, Sichuan";
            case 5104: return "Panzhihua, Sichuan";
            case 5105: return "Luzhou, Sichuan";
            case 5106: return "Deyang, Sichuan";
            case 5107: return "Mianyang, Sichuan";
            case 5108: return "Guangyuan, Sichuan";
            case 5109: return "Suining, Sichuan";
            case 5110: return "Neijiang, Sichuan";
            case 5111: return "Leshan, Sichuan";
            case 5113: return "Nanchong, Sichuan";
            case 5114: return "Meishan, Sichuan";
            case 5115: return "Yibin, Sichuan";
            case 5116: return "Guang'an, Sichuan";
            case 5117: return "Dazhou, Sichuan";
            case 5118: return "Ya'an, Sichuan";
            case 5119: return "Bazhong, Sichuan";
            case 5120: return "Ziyang, Sichuan";
            case 5132: return "Aba, Sichuan";
            case 5133: return "Ganzi, Sichuan";
            case 5134: return "Liangshan, Sichuan";
            case 5201: return "Guiyang, Guizhou";
            case 5202: return "Liupanshui, Guizhou";
            case 5203: return "Zunyi, Guizhou";
            case 5204: return "Anshun, Guizhou";
            case 5205: return "Bijie, Guizhou";
            case 5206: return "Tongren, Guizhou";
            case 5223: return "Qianxinan, Guizhou";
            case 5226: return "Qiandongnan, Guizhou";
            case 5227: return "Qiannan, Guizhou";
            case 5301: return "Kunming, Yunnan";
            case 5302: return "Kunming, Yunnan";
            case 5303: return "Qujing, Yunnan";
            case 5304: return "Yuxi, Yunnan";
            case 5305: return "Baoshan, Yunnan";
            case 5306: return "Zhaotong, Yunnan";
            case 5307: return "Lijiang, Yunnan";
            case 5308: return "Pu'er, Yunnan";
            case 5309: return "Lincang, Yunnan";
            case 5323: return "Chuxiong, Yunnan";
            case 5325: return "Honghe, Yunnan";
            case 5326: return "Wenshan, Yunnan";
            case 5328: return "Xishuangbanna, Yunnan";
            case 5329: return "Dali, Yunnan";
            case 5331: return "Dehong, Yunnan";
            case 5333: return "Nujiang, Yunnan";
            case 5334: return "Diqing, Yunnan";
            case 5401: return "Lhasa, Tibet";
            case 5402: return "Shigatse, Tibet";
            case 5403: return "Qamdo, Tibet";
            case 5404: return "Nyingchi, Tibet";
            case 5405: return "Lhoka, Tibet";
            case 5406: return "Nagqu, Tibet";
            case 5425: return "Ngari, Tibet";
            case 6101: return "Xi'an, Shaanxi";
            case 6102: return "Tongchuan, Shaanxi";
            case 6103: return "Baoji, Shaanxi";
            case 6104: return "Xianyang, Shaanxi";
            case 6105: return "Weinan, Shaanxi";
            case 6106: return "Yan'an, Shaanxi";
            case 6107: return "Hanzhong, Shaanxi";
            case 6108: return "Yulin, Shaanxi";
            case 6109: return "Ankang, Shaanxi";
            case 6110: return "Shangluo, Shaanxi";
            case 6201: return "Lanzhou, Gansu";
            case 6202: return "Jiayuguan, Gansu";
            case 6203: return "Jinchang, Gansu";
            case 6204: return "Baiyin, Gansu";
            case 6205: return "Tianshui, Gansu";
            case 6206: return "Wuwei, Gansu";
            case 6207: return "Zhangye, Gansu";
            case 6208: return "Pingliang, Gansu";
            case 6209: return "Jiuquan, Gansu";
            case 6210: return "Qingyang, Gansu";
            case 6211: return "Dingxi, Gansu";
            case 6212: return "Longnan, Gansu";
            case 6229: return "Linxia, Gansu";
            case 6230: return "Gannan, Gansu";
            case 6301: return "Xining, Qinghai";
            case 6302: return "Haidong, Qinghai";
            case 6322: return "Haibei, Qinghai";
            case 6323: return "Huangnan, Qinghai";
            case 6325: return "Hainan, Qinghai";
            case 6326: return "Guoluo, Qinghai";
            case 6327: return "Yushu, Qinghai";
            case 6328: return "Haixi, Qinghai";
            case 6401: return "Yinchuan, Ningxia";
            case 6402: return "Shizuishan, Ningxia";
            case 6403: return "Wuzhong, Ningxia";
            case 6404: return "Guyuan, Ningxia";
            case 6405: return "Zhongwei, Ningxia";
            case 6501: return "Urumqi, Xinjiang";
            case 6502: return "Karamay, Xinjiang";
            case 6504: return "Turpan, Xinjiang";
            case 6505: return "Hami, Xinjiang";
            case 6523: return "Changji, Xinjiang";
            case 6527: return "Bortala, Xinjiang";
            case 6528: return "Bayingol, Xinjiang";
            case 6529: return "Aksu, Xinjiang";
            case 6530: return "Kizilsu, Xinjiang";
            case 6531: return "Kashgar, Xinjiang";
            case 6532: return "Hotan, Xinjiang";
            case 6540: return "Ili, Xinjiang";
            case 6542: return "Tarbagatay, Xinjiang";
            case 6543: return "Altay, Xinjiang";
            case 6590: return "Xinjiang";
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

    public void onReturn(){
        finish();
    }
}
