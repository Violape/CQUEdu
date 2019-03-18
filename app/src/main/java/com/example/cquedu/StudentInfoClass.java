package com.example.cquedu;

public class StudentInfoClass {
    public String cquid;
    public String name;
    public String gender;
    public int doby;
    public int dobm;
    public int dobd;
    public String idcard;
    public String origin;
    public String profession;
    public String grade;
    public String classno;
    public StudentInfoClass(String xh, String xm, String xb, String sfz, String zy, String nj, String bj){
        cquid = xh;
        name = xm;
        parseGender(xb);
        parseIDCard(sfz);
        parseProfession(zy);
        grade = nj;
        classno = Integer.valueOf(bj.substring(bj.length()-2)).toString();
    }

    public void parseGender(String xb){
        if(xb.equals("男"))
            gender = "Male";
        else if(xb.equals("女"))
            gender = "Female";
        else
            gender = "Unknown";
    }

    public void parseIDCard(String sfz){
        idcard = sfz;
        if(sfz.length()==18){
            doby = Integer.valueOf(sfz.substring(6,9));
            dobm = Integer.valueOf(sfz.substring(10,11));
            dobd = Integer.valueOf(sfz.substring(12,13));
            origin = getCityfromZip(Integer.valueOf(sfz.substring(0,5)));
        }
        else{
            doby = 0;
            dobm = 0;
            dobd = 0;
            origin = "N/A";
        }
    }

    public void parseProfession(String zy){
        switch (zy){
            case "软件工程": profession = "Software Engineering";
            //other cases
            default: profession = "N/A";
        }
    }

    public String getCityfromZip(int code){
        switch (code){
            case 320602: return "Nantong, Jiangsu";
            //other cases
            default: return "N/A";
        }
    }
}
