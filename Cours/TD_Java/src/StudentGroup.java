import java.util.ArrayList;
import java.util.regex.Pattern;


public class StudentGroup {
    private static int numberOfGroups = 0 ;
    private final String name ;
    private ArrayList<Student> alist ;

    public StudentGroup(String name){
        if (!Pattern.matches("[1-2]G[1-3]TD[1-4]TP[1-8]", name)) {
            throw new IllegalArgumentException(name + " is not a real ENSEA name") ;
        }
        this.name = name ;
        numberOfGroups++ ;
        this.alist = new ArrayList<>() ;
    }

    public void addStudent(Student student){
        this.alist.add(student) ;
    }

    public void displayPresenceList(){
        System.out.println("List of presence for " + this.name);
        for(Student student : this.alist){
            System.out.println(student) ;
        }
    }


    @Override
    public String toString() {
        return "Group " + this.name + ", number of students: " + this.alist.size();
    }


    public String getName() {
        return this.name ;
    }


    public static int getNumberOfGroups() {
        return numberOfGroups ;
    }

    public ArrayList<Student> getAlist(){
        return alist ;
    }


    public static void main(String[] args) {
        Student tauvel = new LocalStudent("Antoine", "TAUVEL", null) ;
        Student bares = new LocalStudent("Christophe", "BARES", null) ;
        StudentGroup a = null, b = null, c = null ;
        try {
            a = new StudentGroup("1G1TD1TP1") ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }

        try {
            b = new StudentGroup("3G1TD1TP1") ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }

        try {
            c = new StudentGroup("1G1TD1TP2") ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }


        System.out.println(a) ;
        System.out.println(b) ;
        System.out.println(c) ;
        System.out.println("Total number of groups: " + StudentGroup.getNumberOfGroups()) ;

        a.addStudent(tauvel) ;
        a.addStudent(bares) ;
        a.displayPresenceList() ;

        System.out.println(a) ;
    }
}

// a would be created but not b ('cause it will fail the test) nor c ('cause the exception for b will be caught and the
// program will go out the try-catch bloc.
// If a, b and c where defined in the try-catch bloc, they wouldn't be accessible outside it.