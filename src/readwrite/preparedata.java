package readwrite;

import Classes.Course;
import Classes.Student;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class preparedata {

    public static void readAndWriteToCSV() throws IOException {
        //Read and prepare students list and courses data requirement:
        //Student part of requirement
        String studentdatapath = "src/files/studentinfo.text";
        String studentcsvpath = "src/files/studentdata.csv";
        String studentrawdata = readfrompath(studentdatapath);
        int studentnumber = studentcount(studentrawdata);
        String[] studentcsv = formatstudenttocsv(studentrawdata, studentnumber);
        Student[] students = new Student[studentnumber];
        makeStudents(studentcsv, students);
        writetocsvfile(studentcsv, studentcsvpath);

        //Courses part of requirement
        String coursecsvpath="src/files/coursedata.csv";
        String coursedatapath="src/files/coursedata.xml";
        String courserawdata=readfrompath(coursedatapath);
        int coursenumber=coursecount(courserawdata);
        String[] coursecsv=formatcoursetocsv(courserawdata,coursenumber);
        Course[] courses=new Course[coursenumber];
        makeCourses(coursecsv,courses);
        writetocsvfile(coursecsv,coursecsvpath);





    }
    public static JSONObject preparejson() throws FileNotFoundException {
        String studentcoursesdetails="src/files/Student course details.json";
        String studentcoursesraw=readfrompath(studentcoursesdetails);
        JSONObject json = new JSONObject(studentcoursesraw);
        return json;
    }
    public static Course[] preparecourses() throws FileNotFoundException {
        String coursedatapath="src/files/coursedata.xml";
        String courserawdata=readfrompath(coursedatapath);
        int coursenumber=coursecount(courserawdata);
        String[] coursecsv=formatcoursetocsv(courserawdata,coursenumber);
        Course[] courses=new Course[coursenumber];
        makeCourses(coursecsv,courses);
        return courses;
    }
    public static Student[] preparestudents() throws FileNotFoundException {
        String studentdatapath="src/files/studentinfo.text";
        String studentrawdata=readfrompath(studentdatapath);
        int studentnumber=studentcount(studentrawdata);
        String[] studentcsv=formatstudenttocsv(studentrawdata,studentnumber);
        Student[] students=new Student[studentnumber];
        makeStudents(studentcsv,students);
        return students;
    }
    public static void writetojson(String jsonstring) throws IOException {
        FileWriter jwriter=new FileWriter("src/files/Student course details.json");
        jwriter.write(jsonstring);
        jwriter.close();
    }









    public static String readfrompath(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scan = new Scanner(file);
        String scanned = "";
        while (scan.hasNextLine()) {
            scanned = scanned + scan.nextLine();
        }
        return scanned;
    }
    public static void writetocsvfile(String[] csvbyline,String path) throws IOException {
        FileWriter csvwriter=new FileWriter(path);
        String csv="";
        for(int i=0;i< csvbyline.length;i++){
            csv=csv+csvbyline[i]+"\n";
        };
        csvwriter.write(csv);
        csvwriter.close();
    }
    public static int studentcount(String temp) {
        //to get number of students-1
        int numberOfStudents = 0;
        while (temp.contains("$")) {
            char[] chars = temp.toCharArray();
            int index = temp.indexOf("$");
            chars[index] = ' ';
            String go = null;
            for (int i = 0; i < chars.length; i++) {
                go = go + chars[i];
            }
            temp = go;
            numberOfStudents++;
        }
        ;
        return numberOfStudents;
    }
    public static String[] formatstudenttocsv(String studentrawdata, int studentnumber) {
        studentrawdata=studentrawdata.replace("#",",");
        studentrawdata=studentrawdata+"$";
        String[] data= new String[studentnumber+1];
        for(int i=0;i<studentnumber+1;i++){
            data[i]=studentrawdata.substring(0,studentrawdata.indexOf("$"));
            data[i]=Integer.toString(i)+","+data[i];
            studentrawdata=studentrawdata.substring(studentrawdata.indexOf("$")+1);
        };
        data[0]=data[0].replaceFirst("0","id");
        return data;

    }
    public static void makeStudents(String[] studentcsv, Student[] students) {
        for(int i=0;i<students.length;i++){
            String data=studentcsv[i+1];
            students[i]= new Student();
            students[i].id=i+1;
            data=data.replaceFirst(students[i].id+",","");
            students[i].name=data.substring(0,data.indexOf(","));
            data=data.replaceFirst(students[i].name+",","");
            students[i].grade=data.substring(0,data.indexOf(","));
            data=data.replaceFirst(students[i].grade+",","");
            students[i].email=data.substring(0,data.indexOf(","));
            data=data.replaceFirst(students[i].email+",","");
            students[i].country=data.substring(data.lastIndexOf(",")+1);
            data=data.replaceFirst(","+students[i].country,"");
            students[i].region=data.substring(data.lastIndexOf(",")+1);
            data=data.replaceFirst(","+students[i].region,"");
            students[i].address=data;

        };
    }
    public static int coursecount(String c) {
        int numberOfCourses=0;
        while(c.contains("<row>")) {
            c = c.replaceFirst("<row>", "");
            numberOfCourses++;
        }
        return numberOfCourses;
    }
    public static String[] formatcoursetocsv(String courserawdata, int coursenumber) {
        courserawdata=courserawdata.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");
        courserawdata=courserawdata.replace("<root>","");
        courserawdata=courserawdata.replace("<row>","");
        courserawdata=courserawdata.replace("<id>","");
        courserawdata=courserawdata.replace("</id>",",");
        courserawdata=courserawdata.replace("<Course Name>","");
        courserawdata=courserawdata.replace("</Course Name>",",");
        courserawdata=courserawdata.replace("<Instructor>","");
        courserawdata=courserawdata.replace("</Instructor>",",");
        courserawdata=courserawdata.replace("<Course duration>","");
        courserawdata=courserawdata.replace("</Course duration>",",");
        courserawdata=courserawdata.replace("<Course time>","");
        courserawdata=courserawdata.replace("</Course time>",",");
        courserawdata=courserawdata.replace("<Location>","");
        courserawdata=courserawdata.replace("</Location>","");
        courserawdata=courserawdata.replace("</row>","\n");
        courserawdata=courserawdata.replace("</root>","");
        courserawdata=courserawdata.replace("        ","");
        String[] course=new String[coursenumber+1];
        for(int i=1;i<coursenumber+1;i++){
            course[i]=courserawdata.substring(0,courserawdata.indexOf("\n")).trim();
            courserawdata=courserawdata.substring(courserawdata.indexOf("\n")+1);

        };
        course[0]="id,Course Name,Instructor,Course duration,Course time,Location";
        return course;
    }
    public static void makeCourses(String[] coursecsv, Course[] courses) {
        for(int i=0;i<courses.length;i++){
            String data=coursecsv[i+1];
            courses[i]= new Course();
            courses[i].id=i+1;
            data=data.replaceFirst(courses[i].id+",","");
            courses[i].name=data.substring(0,data.indexOf(","));
            data=data.replaceFirst(courses[i].name+",","");
            courses[i].location=data.substring(data.lastIndexOf(",")+1);
            data=data.replaceFirst(","+courses[i].location,"");
            courses[i].time=data.substring(data.lastIndexOf(",")+1);
            data=data.replaceFirst(","+courses[i].time,"");
            courses[i].duration=data.substring(data.lastIndexOf(",")+1);
            data=data.replaceFirst(","+courses[i].duration,"");
            courses[i].instructor=data;
        };
    }



}
