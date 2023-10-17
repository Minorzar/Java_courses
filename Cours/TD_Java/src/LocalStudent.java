public class LocalStudent extends  Student{

    public LocalStudent(String firstName, String lastName, String promoName){
        super(firstName, lastName, promoName) ;
        this.yearValidation = this.validation() ;
    }
}
