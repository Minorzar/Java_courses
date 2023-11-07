package math;

import static java.lang.Math.sqrt;

public class Complex {

    double real ;

    double imaginary ;

    public Complex(double real, double imaginary){
        this.imaginary = imaginary ;
        this.real = real ;
    }

    public Complex sum(Complex c1, Complex c2){
        return new Complex(c1.real + c2.real, c1.imaginary + c2.imaginary) ;
    }

    public Complex product(Complex c1, Complex c2){
        return new Complex(c1.real*c2.real-c1.imaginary*c2.imaginary,
                           c1.real*c2.imaginary + c1.imaginary*c2.real) ;
    }

    public double norme(Complex c){
        return sqrt(c.real*c.real+c.imaginary*c.imaginary) ;
    }

}
