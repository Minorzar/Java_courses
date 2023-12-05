package audio;

import math.Complex;
import math.FFT;

import javax.sound.sampled.*;

public class AudioSignal {

    private double[] sampleBuffer;
    private double dBlevel;

    public AudioSignal(int frameSize) {
        sampleBuffer = new double[frameSize];
    }

    public AudioSignal(AudioSignal other) {
        this.sampleBuffer = other.sampleBuffer;
        this.dBlevel = other.dBlevel;
    }

    public void setFrom(AudioSignal other) {
        this.dBlevel = other.dBlevel;
        this.sampleBuffer = other.sampleBuffer;
    }

    public void recordFrom(TargetDataLine audioInput) {
        byte[] byteBuffer = new byte[sampleBuffer.length*2];
        if (audioInput.read(byteBuffer, 0, byteBuffer.length)==-1) return;

        for (int i=0; i<sampleBuffer.length; i++)
            sampleBuffer[i] = ((byteBuffer[2*i]<<8)+byteBuffer[2*i+1]) / 32768.0;

        double sum = 0.0;
        for (double sample : sampleBuffer) {
            sum += sample * sample;
        }
        double rms = Math.sqrt(sum / sampleBuffer.length);

        this.dBlevel = 20 * Math.log10(rms);

    }

    public byte[] convertDoublesToBytes(double[] doubles, int SizeInBits) {
        if (SizeInBits == 16) {
            byte[] bytes = new byte[doubles.length * 2];
            for (int i = 0; i < doubles.length; i++) {

                short scaledValue = (short) (doubles[i] * Short.MAX_VALUE);

                bytes[2 * i] = (byte) ((scaledValue >> 8) & 0xFF);
                bytes[2 * i + 1] = (byte) (scaledValue & 0xFF);

            }
            return bytes;
        }

        if (SizeInBits == 8) {
            byte[] bytes = new byte[doubles.length];
            for (int i = 0; i < doubles.length; i++) {

                short scaledValue = (short) (doubles[i] * 127);

                bytes[i] = (byte) scaledValue;

            }
            return bytes;
        }

        throw new RuntimeException("Unhandle sample byte size");
    }


    public void playTo(SourceDataLine audioOutput) {

        audioOutput.start();

        audioOutput.write(convertDoublesToBytes(this.sampleBuffer, audioOutput.getFormat().getSampleSizeInBits()),
                0, this.sampleBuffer.length * 2);

        audioOutput.drain();
        audioOutput.close();

    }

    public void playTo(SourceDataLine audioOutput, boolean continuous) {

        if(!continuous) audioOutput.start();

        audioOutput.write(convertDoublesToBytes(this.sampleBuffer, audioOutput.getFormat().getSampleSizeInBits()),
                0, this.sampleBuffer.length * 2);

        if(!continuous) audioOutput.drain();
        if(!continuous) audioOutput.close();

    }

    public double getSample(int i) {
        return sampleBuffer[i];
    }

    public void setSample(int i, double value) {
        sampleBuffer[i] = value;
    }

    public double getdBlevel() {
        return dBlevel;
    }

    public int getFrameSize() {
        return sampleBuffer.length;
    }

    public void setFrameSize(int FrameSize) {
        this.sampleBuffer = new double[FrameSize];
    }

    public double[] getSampleBuffer() {
        return sampleBuffer;
    }


    public void playTestSin(SourceDataLine sourceDataLine) {
        if(sourceDataLine.getFormat().getSampleSizeInBits() == 16) {

            int bufferSize = this.getFrameSize();
            double[] buffer = new double[bufferSize];


            for (int i = 0; i < bufferSize/2; i++) {
                double angle = 2.0 * Math.PI * 1000 * i / sourceDataLine.getFormat().getSampleRate();
                double sampleValue = Math.sin(angle);

                buffer[i] = sampleValue;
            }

            this.sampleBuffer = buffer;

            this.playTo(sourceDataLine);
            sourceDataLine.drain();
            sourceDataLine.close();

        } else {
            throw new RuntimeException("Not a 16 bits audio");
        }
    }

    public void playTestSin(SourceDataLine sourceDataLine, boolean continuous) {
        if(sourceDataLine.getFormat().getSampleSizeInBits() == 16) {

            int bufferSize = this.getFrameSize();
            double[] buffer = new double[bufferSize];


            for (int i = 0; i < bufferSize/2; i++) {
                double angle = 2.0 * Math.PI * 1000 * i / sourceDataLine.getFormat().getSampleRate();
                double sampleValue = Math.sin(angle);

                buffer[i] = sampleValue;
            }

            this.sampleBuffer = buffer;

            this.playTo(sourceDataLine, continuous);

        } else {
            throw new RuntimeException("Not a 16 bits audio");
        }
    }


    public static void main(String[] args) throws LineUnavailableException {

        AudioFormat audioFormat = new AudioFormat(8000.0f, 16, 1, true,
                true);


        AudioSignal audioSignal = new AudioSignal(32000);
        AudioSignal audioSignal2 = new AudioSignal(audioSignal);


        SourceDataLine sourceDataLine;
        TargetDataLine targetDataLine;


        try {
            sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
            targetDataLine = AudioSystem.getTargetDataLine(audioFormat);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }


        System.out.println(sourceDataLine.getLineInfo());
        System.out.println(targetDataLine.getLineInfo());


        try {
            targetDataLine.open(audioFormat);
            targetDataLine.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Start");
        audioSignal.recordFrom(targetDataLine); System.out.println("End");


        System.out.println("Start on sourceDataLine"); sourceDataLine.open(audioFormat);
        sourceDataLine.start();
        audioSignal.playTo(sourceDataLine); System.out.println("End on sourceDataLine");


        audioSignal2.setFrom(audioSignal);


        System.out.println("Start on sourceDataLine of audioSignal2"); sourceDataLine.open(audioFormat);
        sourceDataLine.start();
        audioSignal2.playTo(sourceDataLine); System.out.println("End on sourceDataLine of audioSignal2 !");


        System.out.println("Start test"); sourceDataLine.open(audioFormat);
        sourceDataLine.start();
        audioSignal.playTestSin(sourceDataLine); System.out.println("End test");

    }

    public Complex[] computeFFT() {
        int fftSize = 1;
        while (fftSize < sampleBuffer.length) {
            fftSize *= 2;
        }

        double[] fftBuffer = new double[fftSize];
        System.arraycopy(sampleBuffer, 0, fftBuffer, 0, sampleBuffer.length);


        return FFT.fft(Complex.fromArray(fftBuffer));
    }
}