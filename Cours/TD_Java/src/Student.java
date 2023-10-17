import java.util.ArrayList;
import java.util.Random;

public abstract  class Student {


    // The different attributes
    private String firstName ;
    private String lastName ;
    private static int totalNumberOfStudent = 0 ;
    private String promoName ;

    protected boolean yearValidation ;
    protected double[] grades ;



    // The constructor
    public Student(String firstName, String lastName, String promoName){
        this.firstName = firstName ;
        this.lastName = lastName ;
        this.promoName = promoName ;
        this.yearValidation = false ;

        this.grades = new double[6] ;

        Random alea = new Random() ;
        for(int i = 0 ; i < 6 ; i++){
            this.grades[i] = Math.floor((15+alea.nextGaussian()*3)*10)/10 ;     // floor round down and nextGaussian
                                                                                // generate a number from a normal
                                                                                // distribution with a mean of 0 and a
                                                                                // standard deviation of 1.
            this.grades[i] = this.grades[i]>20?20:grades[i] ;   // Check if the grade is greater than 20.
        }

        totalNumberOfStudent++ ;    // Increase the number of student each time a new
                                    // one is created
    }


    // The different getter methods
    public String getFirstName(){
        return firstName ;
    }

    public String getLastName(){
        return lastName ;
    }

    public static int getTotalNumberOfStudents(){
        return totalNumberOfStudent ;
    }


    // Override of the toString() method
    @Override
    public String toString() {
        if (this.yearValidation ){
            if(this.promoName == null){
                return this.firstName + ' ' + this.lastName  + " has graduated." ;
            }
            return this.promoName + ' ' + this.firstName + ' ' + this.lastName + " has graduated." ;
        }
        if (this.promoName == null){
            return this.firstName + ' ' + this.lastName + " has failed." ;
        }
        return this.promoName + ' ' + this.firstName + ' ' + this.lastName + " has failed." ;
    }


    // Override of the finalize method
    // @Override
    // protected void finalize(){
    //    totalNumberOfStudent-- ;
    // }


    // Main method for student class
    public static void main(String[] args){
        Student student1 = new LocalStudent("Ash", "KETCHUM", null) ;
        Student student2 = new LocalStudent("Endou", "MAMORU", null) ;

        System.out.println(student1);
        System.out.println(student2);
        System.out.println("Total number of students: " + Student.getTotalNumberOfStudents());
    }

    public boolean validation(){
        for(double grade : this.grades){
            if (grade < 10){
                return false ;
            }
        }
        return true ;
    }
}

// If change to abstract, nothing is working anymore due to the fact that it's not possible to create new object from
// abstract classes. So all the "new Student ..." was not working, thus the errors.