import java.io.BufferedReader;
import java.io.FileReader;

public class Extraction {

    public static void main(String[] args){
        Promotion promo = new Promotion() ;

        try{FileReader file = new FileReader(args[0]) ;
            BufferedReader buff = new BufferedReader(file) ;
            String line = buff.readLine() ;
            while (line != null){
                int firstComma = line.indexOf(',') ;
                String lastName = line.substring(0,firstComma) ;
                int sndComma = line.indexOf(',', firstComma +1) ;
                String firstName = line.substring(firstComma + 1 , sndComma) ;
                int thirdComma = line.indexOf(',', sndComma +1) ;
                String groupName = line.substring(sndComma +1, thirdComma) ;
                String promoName = line.substring(thirdComma + 1) ;

                StudentGroup group = promo.findGroup(groupName) ;
                if (group == null){
                    group = new StudentGroup(groupName) ;
                    promo.addGroup(group) ;
                }

                Student student = promo.findStudent(group, firstName, lastName) ;
                if(student == null){
                    if (promoName == "Etranger"){
                        student = new ExchangeStudent(firstName, lastName) ;
                    }
                    else {
                        student = new LocalStudent(firstName, lastName, promoName);
                    }
                    group.addStudent(student) ;
                }

                line = buff.readLine();

            }
        }
        catch (Exception e ){
            e.printStackTrace() ;
        }

        promo.presenceList() ;

    }
}


// One of the main flow is a lot of thing that are in fact not check: a student can be in more than one group, the
// promoName is not validated. A way to bypass this issue (which I guess are not the only ones) is to just implement
// those checks (all will basically be inside the Student class).