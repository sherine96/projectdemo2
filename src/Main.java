import Classes.Course;
import Classes.Student;
import datamanipulation.DataManipulation;
import exceptions.EnrollmentExceedingException;
import exceptions.InvalidCourseIdSelection;
import exceptions.InvalidStudentIdSelectionException;
import exceptions.UnenrollmentInAnyCourseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class Main extends DataManipulation{
    public static void main(String[] args) throws IOException {
        //As i was creating this project there were many things i don't understand how to do in java
        //to solve this issue i looked for solutions online through vaious means
        //however i can ensure that no code was copied and that i tried to understand the ideas provided online then write my code
        //an example of this is how to make sure that input data is numeric
        //i understood the idea of checking if an exception occurs when changing input to numbers and made my own code
        //Read and prepare students list and courses data requirement:
        //readwrite-preparedata-readAndWriteToCSV();
        //Implement data manipulation functionalities requirement:
        //datamanipulation-DataManipulation.java
        //Implement your application UI requirement:
        //main.java
       //homepage();














    }

    private static void unenrollmentpage(int studentid) throws IOException{
        JSONObject json=preparejson();
        Course[] courses=preparecourses();
        if (json.has(String.valueOf(studentid))){
            JSONArray enrolled=json.getJSONArray(String.valueOf(studentid));
            if (enrolled.length()==1){
                System.out.println("Student is enrolled in only one course.");
                menurepeat(studentid);

            }else{
                System.out.println("Please enter course id:");
                Scanner userinput=new Scanner(System.in);
                String m=userinput.next();
                if (isNumeric(m)){
                    int courseid=Integer.parseInt(m);
                    for (int i=0;i<enrolled.length();i++){
                        if(enrolled.toString().contains("["+courseid+",")||enrolled.toString().contains(","+courseid+"]")||enrolled.toString().contains(","+courseid+",")||enrolled.toString().contains("["+courseid+"]")){
                            for(int f=0;f<enrolled.length();f++){
                                if(enrolled.getInt(f)==courseid){
                                    enrolled.remove(f);
                                }
                            }
                            json.put(String.valueOf(studentid),enrolled);
                            writetojson(json.toString());
                            System.out.println("Unenrolled successfully from the "+courses[courseid-1].name+" course");
                            studentdetailspage(studentid);
                            menurepeat(studentid);
                        }else if(courseid<=courses.length&courseid>=1){
                            System.out.println("Faild to unenroll: Student is not enrolled in that course");
                            menurepeat(studentid);
                        }
                        else{
                            try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                                System.out.println(e.getMessage());
                            }
                            menurepeat(studentid);
                        }
                    }
                }else{
                    try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                        System.out.println(e.getMessage());
                    }
                    menurepeat(studentid);
                }

            }

        }else{
            try{throw new UnenrollmentInAnyCourseException();}catch (UnenrollmentInAnyCourseException e){
                System.out.println(e.getMessage());
            }
            menurepeat(studentid);

        }
    }
    private static void enrollmetpage(int studentid) throws IOException {
        Course[] courses=preparecourses();
        System.out.println("Enrollment page");
        System.out.println("====================================================================================================");
        enrollmentpagelistallcourses(courses);
        char r=enrollpagerepeat(studentid);
        while(r=='c'){
            r=enrollpagerepeat(studentid);
        }
        while(r=='d'){
            r=enrollpagerepeat(studentid);
        }


    }
    private static void menurepeat(int studentid) throws IOException {
        System.out.println("Please choose from the following:");
        System.out.println("a - Enroll in a course");
        System.out.println("d - Unenrollfrom an existing course");
        System.out.println("r - Replacing an existing course");
        System.out.println("b - Back to the main page");
        System.out.println("please select the required action:");
        Scanner userinput=new Scanner(System.in);
        String k=userinput.next();
        char choice=k.charAt(0);
        while(k.length()>1){
            System.out.println("Invalid choice");
            System.out.println("Please choose from the following:");
            System.out.println("a - Enroll in a course");
            System.out.println("d - Unenrollfrom an existing course");
            System.out.println("r - Replacing an existing course");
            System.out.println("b - Back to the main page");
            System.out.println("please select the required action:");
            k=userinput.next();
            choice=k.charAt(0);
        }
        menumove(choice,studentid);
    }

    private static void menumove(char choice,int studentid) throws IOException {
        switch(choice){
            case'b':
                homepage();
                break;
            case 'a':
                enrollmetpage(studentid);

                break;
            case'd':
                unenrollmentpage(studentid);
                break;
            case'r':
                replacementpage(studentid);
                break;
            default:
                System.out.println("Invalid choice");
                menurepeat(studentid);

        }
    }

    private static void homepage() throws IOException {
        Student[] students=preparestudents();
        System.out.println("Welcome to LMS");
        System.out.println("created by {Sherin Ahmed Maged Youssef}");
        System.out.println("====================================================================================");
        System.out.println("Home page");
        System.out.println("====================================================================================");
        homepagestudentlist();
        System.out.print("Please select the required student:");
        Scanner userinput=new Scanner(System.in);
        String input=userinput.next();
            while(isStudent(input,students)==false){
                try{
                    throw new InvalidStudentIdSelectionException();
                }catch (InvalidStudentIdSelectionException e){
                    System.out.println(e.getMessage());
                }
                input= userinput.next();
            }
            int studentid=Integer.parseInt(input);
            studentdetailspage(studentid);
            menurepeat(studentid);
        }

    private static char enrollpagerepeat(int studentid) throws IOException {
        Course[] courses=preparecourses();
        JSONObject json=preparejson();
        System.out.println("Please make one of the following:");
        System.out.println("Enter the course id that you want to enroll the student to");
        System.out.println("Enter b to go back to the home page");
        System.out.print("Please select the required action:");
        Scanner userinput=new Scanner(System.in);
        String k= userinput.next();
        if (k.charAt(0)!='b'){
            if(isNumeric(k)){
                int courseid=Integer.parseInt(k);
                if (validatecourseid(courseid,courses)){
                    if(json.has(String.valueOf(studentid))){
                        JSONArray s=json.getJSONArray(String.valueOf(studentid));
                        if (s.length()==6){
                            try{throw new EnrollmentExceedingException();}catch (EnrollmentExceedingException e){
                                System.out.println(e.getMessage());
                            }
                            return 'd';
                        }else{
                            boolean alreadyenrolled=false;
                            for(int i=0;i< s.length();i++){
                                if(s.getInt(i)==courseid){
                                    alreadyenrolled=true;
                                }
                            }
                            if (alreadyenrolled){
                                System.out.println("Student is already enrolled in course");
                                return 'd';
                            }else{
                                enrollmentfunctionality(studentid,courseid);
                                return 'c';
                            }
                        }
                    }else{
                        enrollmentfunctionality(studentid,courseid);
                        return 'c';
                    }

                }else{
                    try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                        System.out.println(e.getMessage());
                    }
                    return 'd';
                }

            }else{
                System.out.println("Invalid choice");
                return 'd';
            }

        }else{
            if (k.length()==1){
                homepage();
                return'd';
            }else {
                System.out.println("Invalid choice");
                return 'd';
            }
        }
    }
    private static void replacementpage(int studentid) throws IOException {
        Scanner userinput=new Scanner(System.in);
        JSONObject json=preparejson();
        Course[] courses=preparecourses();
        if (json.has(String.valueOf(studentid))){
            JSONArray enrolled=json.getJSONArray(String.valueOf(studentid));
            System.out.println("Please enter the course id to be replaced:");
            String input=userinput.next();
            if(isNumeric(input)){
                int replaced= Integer.parseInt(input);
                if(enrolled.toString().contains("["+replaced+",")||enrolled.toString().contains(","+replaced+"]")||enrolled.toString().contains(","+replaced+",")||enrolled.toString().contains("["+replaced+"]")){

                    System.out.println("Available courses");
                    System.out.println("====================================================================================================");
                    listallcourses(courses);
                    System.out.println("Please enter the required course id to replace: ");
                    input=userinput.next();
                    if (isNumeric(input)){
                        int courseid=Integer.parseInt(input);
                        if(courseid==replaced){
                            System.out.println("Failed to replace: Student is already enrolled in that course");
                            menurepeat(studentid);
                        }else if(courseid>=1&courseid<=courses.length){
                            if (enrolled.toString().contains("["+courseid+",")||enrolled.toString().contains(","+courseid+"]")||enrolled.toString().contains(","+courseid+",")||enrolled.toString().contains("["+courseid+"]"))
                            {
                                System.out.println("Failed to replace: Student is already enrolled in that course");
                                menurepeat(studentid);
                            }else{
                                for(int f=0;f<enrolled.length();f++){
                                    if(enrolled.getInt(f)==replaced){
                                        enrolled.remove(f);
                                    }
                                }
                                json.put(String.valueOf(studentid),enrolled);
                                writetojson(json.toString());
                                json.append(String.valueOf(studentid),courseid);
                                writetojson(json.toString());
                                System.out.println("Courses replaced successfully from the "+courses[replaced-1].name+"course to "+courses[courseid-1].name+"course");
                                studentdetailspage(studentid);
                                menurepeat(studentid);
                            }



                        }else{
                            try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                                System.out.println(e.getMessage());
                            }
                            menurepeat(studentid);
                        }
                    }




                }else{
                    try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                        System.out.println(e.getMessage());
                    }
                    menurepeat(studentid);
                }
            }else{
                try{throw new InvalidCourseIdSelection();}catch (InvalidCourseIdSelection e){
                    System.out.println(e.getMessage());
                }
                menurepeat(studentid);
            }


        }else{
            System.out.println("Faild to replace as the student hasn't enrolled in any course yet");
            menurepeat(studentid);

        }


    }

}