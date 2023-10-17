import java.util.ArrayList;

public class Promotion {

    ArrayList<StudentGroup> glist ;

    public Promotion(){
        this.glist = new ArrayList<>() ;
    }

    public void addGroup(StudentGroup group){
        this.glist.add(group) ;
    }

    public StudentGroup findGroup(String name){
        for(StudentGroup group : this.glist){
            if (group.getName().equals(name)){
                return group ;
            }
        }
        return null ;
    }

    public Student findStudent(StudentGroup group, String firstName, String lastName){
        ArrayList<Student> slist = group.getAlist() ;
        if (slist == null){
            return null ;
        }
        for (Student student : slist){
            if (student.getFirstName().equals(firstName) && student.getLastName().equals(lastName)){
                return student ;
            }
        }
        return null ;
    }

    public void presenceList(){
        ArrayList<StudentGroup> glist = this.glist ;
        if (glist == null){
            return ;
        }
        for (StudentGroup group : glist){
            System.out.println();
            group.displayPresenceList();
        }
    }


    public static void main(String[] args){
        Promotion promo = new Promotion() ;
        StudentGroup group = null ;
        try{group = new StudentGroup("1G1TD1TP1") ;}
        catch (Exception e){
            e.printStackTrace() ;
        }

        promo.addGroup(group) ;

        group.addStudent(new LocalStudent("Antoine", "TAUVEL", null)) ;
        group.addStudent(new LocalStudent("Sylvain", "REYNAL", null)) ;
        group.addStudent(new LocalStudent("Christophe", "BARES", null)) ;

        promo.presenceList();

        System.out.println(promo.findGroup("1G1TD1TP1")) ;
        System.out.println(promo.findGroup("1G1TD1TP2")) ;
        System.out.println(promo.findStudent(promo.findGroup("1G1TD1TP1"), "Antoine", "TAUVEL")) ;
        System.out.println(promo.findStudent(promo.findGroup("1G1TD1TP1"), "Antoine", "REYNAL")) ;
    }
}
