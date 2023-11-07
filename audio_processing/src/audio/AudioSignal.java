package audio;

import javax.sound.sampled.* ;
import java.util.Arrays ;

import math.Complex ;

import static java.lang.Math.*;

public class AudioSignal {
    private double[] sampleBuffer;
    private double dBlevel;

    public AudioSignal(int frameSize) {
        sampleBuffer = new double[frameSize];
        dBlevel = 0.0;
    }

    public void setFrom(AudioSignal other) {
        int newFrameSize = other.getFrameSize() ;
        this.sampleBuffer = new double[newFrameSize] ;

        for (int i = 0; i < newFrameSize ; i++) {
            double sample = other.getSample(i);
            setSample(i, sample);

            this.dBlevel = other.getdBlevel() ;
        }
    }

    public boolean recordFrom(TargetDataLine audioInput) {
        byte[] byteBuffer = new byte[sampleBuffer.length * 2];

        if (audioInput.read(byteBuffer, 0, byteBuffer.length) == -1) {
            return false;
        }

        for (int i = 0; i < sampleBuffer.length; i++) {
            sampleBuffer[i] = ((byteBuffer[2 * i] << 8) + byteBuffer[2 * i + 1]) / 32768.0;
        }

        double energie = Arrays.stream(sampleBuffer).map(element -> abs(element * element)).sum() ;
        dBlevel = 10*log10(energie/10e-12) ;

        return  true ;
    }

    public boolean playTo(SourceDataLine audioOutput) {
        return true ;
    }

    public double getdBlevel() {
        return dBlevel;
    }

    public void setSample(int i, double value) {
        this.sampleBuffer[i] = value;
    }

    public double getSample(int i) {
        return sampleBuffer[i];
    }

    public int getFrameSize(){
        return this.sampleBuffer.length ;
    }

    Complex[] computeFFT(){
        int n = this.getFrameSize() ;
        double[] data = this.sampleBuffer ;
        Complex[] fft = new Complex[this.getFrameSize()] ;

        for(int i = 0 ; i < n ; i++){
            double real = 0, imaginary = 0 ;

            for(int j = 0 ; j < n ; j++){
                real += data[j]*cos(-2*i*PI*j/n) ;
                imaginary += data[j]*sin(-2*i*PI*j/n) ;
            }

            fft[i] = new Complex(real, imaginary) ;
        }

        return fft ;
    }
}