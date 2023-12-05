package ui;

import audio.AudioSignal;
import javafx.animation.AnimationTimer;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class SignalView extends LineChart<Number, Number>{
    private XYChart.Series<Number, Number> series;

    public SignalView(AudioSignal audioSignal, String title) {
        super(new NumberAxis(), new NumberAxis());

        setTitle(title);
        getXAxis().setLabel("Index");
        getYAxis().setLabel("Values");

        this.setCreateSymbols(false);

        getYAxis().setAutoRanging(false);
        ((NumberAxis) getYAxis()).setLowerBound(-1);
        ((NumberAxis) getYAxis()).setUpperBound(1);

        series = new XYChart.Series<>();
        series.setName("Data");

        {
            AtomicInteger i = new AtomicInteger(0);
            Arrays.stream(audioSignal.getSampleBuffer()).forEach(e -> {
                series.getData().add(new XYChart.Data<>(i, e));
                i.getAndIncrement();
            });
        }

        getData().add(series);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateData(audioSignal);
            }
        }.start();
    }

    public void updateData(AudioSignal audioSignal) {

        series.getData().clear();

        {
            double[] sampleBuffer = audioSignal.getSampleBuffer();
            for (int i = 0; i < sampleBuffer.length; i++) {
                series.getData().add(new XYChart.Data<>(i, sampleBuffer[i]));
            }
        }
    }


}