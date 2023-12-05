package math;

public record Complex(double real, double imag) {

    public static Complex[] fromArray(double[] array) {
        Complex[] result = new Complex[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = new Complex(array[i], 0);
        }
        return result;
    }

    public Complex plus(Complex b) {
        return new Complex(this.real + b.real, this.imag + b.imag);
    }

    public Complex minus(Complex b) {
        return new Complex(this.real - b.real, this.imag - b.imag);
    }

    public Complex times(Complex b) {
        return new Complex(this.real * b.real - this.imag * b.imag, this.real * b.imag + this.imag * b.real);
    }

    public Complex conjugate() {
        return new Complex(this.real, -this.imag);
    }

    public double abs() {
        return Math.sqrt(this.real * this.real + this.imag * this.imag);
    }


}