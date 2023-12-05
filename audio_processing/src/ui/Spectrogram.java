package ui;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import audio.AudioSignal;
import math.Complex;

public class Spectrogram extends Canvas {
    private AudioSignal audioSignal;
    private double minFrequency;
    private double maxFrequency;
    private float sampleRate;

    public Spectrogram(double width, double height, AudioSignal audioSignal, float sampleRate) {
        super(width, height);
        this.audioSignal = audioSignal;
        this.sampleRate = sampleRate;
        this.minFrequency = 0;
        this.maxFrequency = sampleRate / 2.0;

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        }.start();
    }

    public void setFrequencyRange(double minFrequency, double maxFrequency) {
        this.minFrequency = Math.max(0, minFrequency);
        this.maxFrequency = Math.min(this.sampleRate / 2.0, maxFrequency);
    }

    public void update() {
        GraphicsContext gc = getGraphicsContext2D();


        Complex[] fftResult = audioSignal.computeFFT();


        gc.clearRect(0, 0, getWidth(), getHeight());


        drawSpectrogram(gc, fftResult);
    }

    private void drawSpectrogram(GraphicsContext gc, Complex[] fftResult) {
        double sampleRate = this.sampleRate;
        double scaleX = getWidth() / (maxFrequency - minFrequency);
        double scaleY = getHeight() / 50;


        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.0);
        gc.strokeLine(40, 0, 40, getHeight());


        gc.setFill(Color.BLACK);
        gc.fillText("Magnitude", 10, 20);


        gc.fillText(String.format("%.2f Hz", minFrequency), 40, getHeight() + 20);
        gc.fillText(String.format("%.2f kHz", maxFrequency / 1000), getWidth() - 40, getHeight() + 20);


        gc.setStroke(Color.BLUE);
        gc.setLineWidth(40 * scaleX);

        int startIndex = (int) (minFrequency * fftResult.length / sampleRate);
        int endIndex = (int) (maxFrequency * fftResult.length / sampleRate);

        for (int i = startIndex; i < endIndex; i++) {
            double frequency = i * sampleRate / fftResult.length;
            double value = fftResult[i].abs();
            double x = (frequency - minFrequency) * scaleX + 40;
            double y = getHeight() - value * scaleY;
            gc.strokeLine(x, getHeight(), x, y);

            if (i % 20 == 0) {
                gc.setFill(Color.BLACK);
                if (value > 2.0f)
                    gc.fillText(String.format("%.2f", value), 10, y);
            }
        }
    }
}