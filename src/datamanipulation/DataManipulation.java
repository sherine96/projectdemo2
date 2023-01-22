package datamanipulation;

import Classes.Course;
import Classes.Student;
import exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;
import readwrite.preparedata;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DataManipulation extends preparedata{
    //Implement data manipulation functionalities requirement:
    //1-Implement listing all students' functionality:
    //listingallstudentsfunctionality();
    //2-Implement view-specific student data functionality:
    //studentdetailspage(studentid);
    //3-Implement enrolling a student in a course functionality:
    //enrollmentfunctionality(studentid,courseid);
    //4-Implement unenrolling a student from a course functionality
    //unenrollmentfunctionality(studentid,courseid);
    //5-Implement replacement course functionality for one of the students
    //i wasn't sure if replacement should be done and exception msg displayed or just msg displayed
    // if the number of courses enrolld before enrollment was 1
    // i think logically replacement should be done with the msg as num. of courses after is bigger than 1
    //replacementfunctionality(studentid,course1id,course2id);
    //i made another replacement functionality so that if i misunderstood incorrectly this can substitute the other functionality
    //replacementfunctionality2(studentid,course1id,course2id);









    public static void replacementfunctionality2(int studentid, int course1id, int course2id) throws IOException{
        JSONObject json=preparejson();
        Course[] courses=preparecourses();
        Student[] students=preparestudents();
        if (json.has(String.valueOf(studentid))){
            JSONArray enrolled=json.getJSONArray(String.valueOf(studentid));
            if(enrolled.toString().contains("["+course1id+",")||enrolled.toString().contains(","+course1id+"]")||enrolled.toString().contains(","+course1id+",")||enrolled.toString().contains("["+course1id+"]")){

                if(course2id==course1id){
                    System.out.println("Failed to replace: Student is already enrolled in that course");
                }
                else if(course2id>=1&course2id<=courses.length){
                    if(enrolled.toString().contains("["+course2id+",")||enrolled.toString().contains(","+course2id+"]")||enrolled.toString().contains(","+course2id+",")||enrolled.toString().contains("["+course2id+"]")){
                        System.out.println("Failed to replace: Student is already enrolled in that course");
                    }else{
                        if (enrolled.length()==1){
                            try{throw new ReplacementException();}catch (ReplacementException e){
                                System.out.println(e.getMessage());
                            }

                        }else {
                        for(int f=0;f<enrolled.length();f++){
                            if(enrolled.getInt(f)==course1id){
                                enrolled.remove(f);
                            }
                        }
                        json.put(String.valueOf(studentid),enrolled);
                        writetojson(json.toString());
                        json.append(String.valueOf(studentid),course2id);
                        writetojson(json.toString());
                        System.out.println("Courses replaced successfully from the "+courses[course1id-1].name+"course to "+courses[course2id-1].name+"course");}
                    }

                }else{
                    try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                        System.out.println(e.getMessage());
                    }
                }


            }else{
                try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                    System.out.println(e.getMessage());
                }

            }

        }else if(validatestudentid(studentid,students)==false){
            try{throw new InvalidStudentIdSelectionException();}catch (InvalidStudentIdSelectionException e){
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("Faild to replace as the student hasn't enrolled in any course yet");

        }
    }
    public static void homepagestudentlist() throws FileNotFoundException {
        Student[] students=preparestudents();
        System.out.println("Student list:");
        System.out.println("id name             Grade       email               address                         region  country");
        for(int i=0;i<students.length;i++){
            System.out.println(i+1+"- "+students[i].name+",  "+students[i].grade+",  "+students[i].email+",  "+students[i].address+",  "+students[i].region+",  "+students[i].country);
        }
        System.out.println("------------------------------------------------------------------------------------");

    }
    public static void listingallstudentsfunctionality() throws FileNotFoundException {
        Student[] students=preparestudents();
        System.out.println("-------------------------------");
        System.out.println("Current Student List");
        System.out.println("-------------------------------");
        System.out.println("id name             Grade       email               address                         region  country");
        for(int i=0; i<students.length;i++){
            System.out.println(students[i].id+"  "+students[i].name+", "+students[i].grade+", "+students[i].email+", "+students[i].address+", "+students[i].region+", "+students[i].country);
        }

    }

    public static boolean validatestudentid(int i, Student[] students) {
        Boolean idexists=false;
        for(int k=0;k<students.length;k++){
            if (students[k].id == i){
                idexists=true;
            }
        };
        return idexists;
    }
    public static void studentdetailspage(int studentid) throws FileNotFoundException {
        Course[] courses=preparecourses();
        Student[] students=preparestudents();
        JSONObject json=preparejson();
        if(validatestudentid(studentid,students)){
            System.out.println("====================================================================================");
            System.out.println("Student Details page");
            System.out.println("====================================================================================");
            System.out.println("Name: "+students[studentid-1].name+"         Grade:"+students[studentid-1].grade+"                Email:"+students[studentid-1].email);
            System.out.println("------------------------------------------------------------------------------------");
            System.out.println("Enrolled courses.");
            if(json.has(String.valueOf(studentid))){
                JSONArray enrolled=json.getJSONArray(String.valueOf(studentid));
                for (int g=0;g< enrolled.length();g++){
                    int m= (int) enrolled.get(g)-1;
                    System.out.println(g+1+"- "+courses[m].id+",      "+courses[m].name+",    "+courses[m].instructor+",    "+courses[m].duration+",     "+courses[m].time+",      "+courses[m].location);
                }
                System.out.println("------------------------------------------------------------------------------------");
            }else{
                System.out.println("This student hasn't enrolled in any courses");
                System.out.println("--------------------------------------------------------------------------------");
            }
        }else{
            System.out.println("Invalid Student ID");
        }

    }
    public static void enrollmentfunctionality(int studentid, int courseid) throws IOException {
        JSONObject json=preparejson();
        Course[] courses=preparecourses();
        Student[] students=preparestudents();
        if(validatestudentid(studentid,students)&validatecourseid(courseid,courses)){
            if (json.has(String.valueOf(studentid))){
                JSONArray arr=json.getJSONArray(String.valueOf(studentid));
                boolean alreadyenrolled=false;
                if(arr.length()==6){
                    try{
                        throw new EnrollmentExceedingException();
                    }
                    catch (EnrollmentExceedingException e){
                        System.out.println(e.getMessage());
                    }

                }else{
                    for(int i=0;i< arr.length();i++){
                        if(arr.getInt(i)==courseid){
                            alreadyenrolled=true;
                        }
                    };
                    if (alreadyenrolled){
                        System.out.println("Student is already enrolled in course");
                    }else{
                        json.append(String.valueOf(studentid),courseid);
                        writetojson(json.toString());
                        System.out.println("Student is Enrolled Successfully in the "+courses[courseid-1].name+" course.");
                    }
                }

            }else {
                JSONArray ar=new JSONArray(1);
                ar.put(courseid);
                json.put(String.valueOf(studentid),ar);
                writetojson(json.toString());
                System.out.println("Student is Enrolled Successfully in the "+courses[courseid].name+" course.");
            }
        }else{
            if(validatecourseid(courseid,courses)==false){
                try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                    System.out.println(e.getMessage());
                }

            }else{
                try{throw new InvalidStudentIdSelectionException();}catch (InvalidStudentIdSelectionException e){
                    System.out.println(e.getMessage());
                }

            }
        }
    }
    public static boolean validatecourseid(int i, Course[] courses) {
        boolean idexists=false;
        for(int k=0;k<courses.length;k++){
            if(courses[k].id==i){
                idexists=true;
            }
        }
        return idexists;
    }
    public static void unenrollmentfunctionality(int studentid, int courseid) throws IOException {
        JSONObject json=preparejson();
        Course[] courses=preparecourses();
        Student[] students=preparestudents();
        if (json.has(String.valueOf(studentid))){
            JSONArray enrolled=json.getJSONArray(String.valueOf(studentid));
            if (enrolled.length()==1){
                System.out.println("Student is enrolled in only one course.");


            }else{
                    if(enrolled.toString().contains("["+courseid+",")||enrolled.toString().contains(","+courseid+"]")||enrolled.toString().contains(","+courseid+",")||enrolled.toString().contains("["+courseid+"]")){
                        for(int f=0;f<enrolled.length();f++){
                            if(enrolled.getInt(f)==courseid){
                                enrolled.remove(f);
                            }
                        }
                        json.put(String.valueOf(studentid),enrolled);
                        writetojson(json.toString());
                        System.out.println("Unenrolled successfully from the "+courses[courseid-1].name+" course");

                    }else if(courseid<=courses.length&courseid>=1){
                        System.out.println("Faild to unenroll: Student is not enrolled in that course");

                    }
                    else{
                        try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                            System.out.println(e.getMessage());
                        }

                    }
            }

        }else if (validatestudentid(studentid,students)==false){
            try{throw new InvalidStudentIdSelectionException();}catch (InvalidStudentIdSelectionException e){
                System.out.println(e.getMessage());
            }
        }
        else{
            try{throw new UnenrollmentInAnyCourseException();}catch (UnenrollmentInAnyCourseException e){
                System.out.println(e.getMessage());
            }


        }
    }
    public static void replacementfunctionality(int studentid, int course1id, int course2id) throws IOException{
        JSONObject json=preparejson();
        Course[] courses=preparecourses();
        Student[] students=preparestudents();
        if (json.has(String.valueOf(studentid))){
            JSONArray enrolled=json.getJSONArray(String.valueOf(studentid));
            if(enrolled.toString().contains("["+course1id+",")||enrolled.toString().contains(","+course1id+"]")||enrolled.toString().contains(","+course1id+",")||enrolled.toString().contains("["+course1id+"]")){

                if(course2id==course1id){
                    System.out.println("Failed to replace: Student is already enrolled in that course");
                }
                else if(course2id>=1&course2id<=courses.length){
                    if(enrolled.toString().contains("["+course2id+",")||enrolled.toString().contains(","+course2id+"]")||enrolled.toString().contains(","+course2id+",")||enrolled.toString().contains("["+course2id+"]")){
                        System.out.println("Failed to replace: Student is already enrolled in that course");
                    }else{
                       if (enrolled.length()==1){
                           try{throw new ReplacementException();}catch (ReplacementException e){
                               System.out.println(e.getMessage());
                           }
                       }
                        for(int f=0;f<enrolled.length();f++){
                            if(enrolled.getInt(f)==course1id){
                                enrolled.remove(f);
                            }
                        }
                        json.put(String.valueOf(studentid),enrolled);
                        writetojson(json.toString());
                        json.append(String.valueOf(studentid),course2id);
                        writetojson(json.toString());
                        System.out.println("Courses replaced successfully from the "+courses[course1id-1].name+"course to "+courses[course2id-1].name+"course");
                    }

                }else{
                    try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                        System.out.println(e.getMessage());
                    }
                }


            }else{
                try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                    System.out.println(e.getMessage());
                }

            }

        }else if(validatestudentid(studentid,students)==false){
            try{throw new InvalidStudentIdSelectionException();}catch (InvalidStudentIdSelectionException e){
                System.out.println(e.getMessage());
            }
        }
        else{
            System.out.println("Faild to replace as the student hasn't enrolled in any course yet");

        }
    }
    public static void enrollmentpagelistallcourses(Course[] courses) {
        System.out.println("id,     Course Name,         Instructor,        Course duration,        Course time,        Location");
        System.out.println("----------------------------------------------------------------------------------------------------");
        for(int i=0;i<courses.length;i++){
            System.out.println(courses[i].id+",  "+courses[i].name+",  "+courses[i].instructor+",  "+courses[i].duration+",  "+courses[i].time+",  "+courses[i].location);
        }
        System.out.println("----------------------------------------------------------------------------------------------------");
    }
    public static void listallcourses(Course[] courses) {
        System.out.println("id,     Course Name,         Instructor,        Course duration,        Course time,        Location");
        System.out.println("----------------------------------------------------------------------------------------------------");
        for(int i=0;i<courses.length;i++){
            System.out.println(courses[i].id+",  "+courses[i].name+",  "+courses[i].instructor+",  "+courses[i].duration+",  "+courses[i].time+",  "+courses[i].location);
        }
        System.out.println("----------------------------------------------------------------------------------------------------");
    }
    public static boolean isNumeric(String s){
        try{
            int j=Integer.parseInt(s);
        }catch (NumberFormatException nfe){return false;}
        return true;
    }
    public static boolean isStudent(String s,Student[]students){
        if (isNumeric(s)){
            int studentid=Integer.parseInt(s);
            if(validatestudentid(studentid,students)){
                return true;
            }else {
                return false;
            }
        }else{
            return false;
        }
    }



}
