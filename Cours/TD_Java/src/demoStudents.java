public class demoStudents {

    public static void main (String[] args){
        Student tauvel = new LocalStudent("Antoine", "TAUVEL", null) ;
        Student bares = new LocalStudent("Christophe", "BARES", null) ;
        Student argument = new LocalStudent(args[0], args[1], null) ;

        System.out.println(tauvel);
        System.out.println(bares);
        System.out.println(argument);

        System.out.printf("Total number of student %d%n\r\n", tauvel.getTotalNumberOfStudents()) ;

        bares = argument ;

        System.gc() ;

        System.out.println(tauvel);
        System.out.println(bares);
        System.out.println(argument);

        System.out.printf("Total number of student %d%n\r\n", tauvel.getTotalNumberOfStudents()) ;

    }
}


// args[0] and args[1] are the first two element enter as parameter in the running configuration.
// There is 3 instances and 3 references to Student object before line 14.
// After line 14 there are 3 references and 2 instances to Student.
// The line "java demoStudents 10" gave me an error (main not found).
