public class ExchangeStudent extends Student {

    @Override
    public boolean validation(){
        double sum = 0. ;
        for (double grade : grades){
            sum += grade ;
        }
        sum /= grades.length ;
        return sum >= 10 ;
    }

    public ExchangeStudent(String firstName, String lastName){
        super(firstName, lastName, "Etranger") ;
        this.yearValidation = this.validation() ;
        }
}
