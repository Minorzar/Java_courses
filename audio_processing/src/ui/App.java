package ui;

import audio.AudioIO;
import audio.AudioProcessor;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.sound.sampled.*;
import java.util.Objects;


public class App extends Application {
    private AudioProcessor audioProcessor;
    private AudioFormat audioFormat;
    private ToolBar toolBar;
    private BorderPane root;


    @Override
    public void start(Stage primaryStage) {

        try {
            root = new BorderPane();

            toolBar = createToolbar();
            root.setTop(toolBar);

        } catch(Exception e) {
            e.printStackTrace();
        }

        this.audioFormat = new AudioFormat(44000.0f, 16, 1, true, true);

        TargetDataLine audioInput = TargetDataLineFromToolBar(this.toolBar);
        SourceDataLine audioOutput = SourceDataLineFromToolBar(this.toolBar);
        int FrameSize = FrameSizeFromToolBar(this.toolBar);

        this.audioProcessor = new AudioProcessor(audioInput, audioOutput, FrameSize);


        try {
            Node statusBar = createStatusbar();
            root.setBottom(statusBar);

            Node mainContent = createMainContent();
            root.setCenter(mainContent);

            Scene scene = new Scene(root,1500,800);

            primaryStage.setScene(scene);
            primaryStage.setTitle("JavaFX audio processor");
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProcessor() {
        TargetDataLine audioInput = TargetDataLineFromToolBar(this.toolBar);
        SourceDataLine audioOutput = SourceDataLineFromToolBar(this.toolBar);
        int FrameSize = FrameSizeFromToolBar(this.toolBar);

        this.audioProcessor.setAudioInput(audioInput);
        this.audioProcessor.setAudioOutput(audioOutput);
        this.audioProcessor.setFrameSize(FrameSize);
    }

    private ToolBar createToolbar(){
        Button button = new Button("Start");
        button.setOnAction(event -> {
            if(button.getText().equals("Start")) {
                updateProcessor();

                try {
                    this.audioProcessor.getAudioOutput().open(this.audioFormat);
                    this.audioProcessor.getAudioOutput().start();
                    this.audioProcessor.getAudioInput().open(this.audioFormat);
                    this.audioProcessor.getAudioInput().start();
                } catch (LineUnavailableException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Starting");
                button.setText("Stop");
                new Thread(audioProcessor).start();

            } else if(button.getText().equals("Stop")) {

                System.out.println("Stopping");
                button.setText("Start");
                audioProcessor.terminateAudioThread();

            }
        });

        ToolBar tb = new ToolBar(button);

        ComboBox<String> cbInputs = new ComboBox<>();
        ComboBox<String> cbOutputs = new ComboBox<>();
        TextField frameSizeTextField = new TextField("1024");

        Label Input = new Label("Input Device : ");
        Label Output = new Label("Output Device : ");
        Label FrameSize = new Label("FrameSize : ");

        tb.getItems().addAll(new Separator(), Input, cbInputs);
        tb.getItems().addAll(new Separator(), Output, cbOutputs);
        tb.getItems().addAll(new Separator(), FrameSize, frameSizeTextField);

        AudioIO.getAudioMixers().stream().filter(e -> e.getDescription().contains("Capture")).forEach(e ->
                cbInputs.getItems().add(e.getName()));
        AudioIO.getAudioMixers().stream().filter(e -> e.getDescription().contains("Playback")).forEach(e ->
                cbOutputs.getItems().add(e.getName()));

        cbInputs.setValue(cbInputs.getItems().stream().findFirst().orElse(null));
        cbOutputs.setValue(cbOutputs.getItems().stream().findFirst().orElse(null));

        return tb;
    }

    private Node createStatusbar(){
        HBox statusbar = new HBox();
        statusbar.getChildren().addAll(new Label("Name:"), new TextField("What do you want from me"));
        return statusbar;
    }

    private Node createMainContent(){
        SignalView inputSignalView = new SignalView(audioProcessor.getInputSignal(), "Input Signal");
        SignalView outputSignalView = new SignalView(audioProcessor.getOutputSignal(), "Output Signal");
        VuMeter vuMeter = new VuMeter(50, 200, audioProcessor.getInputSignal());
        Spectrogram spectrogram = new Spectrogram(400, 200, audioProcessor.getInputSignal(),
                audioProcessor.getAudioInput().getFormat().getSampleRate());
        Spectrogram spectrogramZoom = new Spectrogram(400, 200, audioProcessor.getInputSignal(),
                audioProcessor.getAudioInput().getFormat().getSampleRate());
        spectrogramZoom.setFrequencyRange(20, 4000);


        HBox hbox1 = new HBox(inputSignalView, outputSignalView, vuMeter);
        HBox hbox2 = new HBox(spectrogram, spectrogramZoom);
        VBox vBox = new VBox(hbox1, hbox2);

        return new Group(vBox);
    }


    private TargetDataLine TargetDataLineFromToolBar(ToolBar toolBar) {
        ComboBox comboBox = getComboBoxFromToolBar(toolBar, "Input Device : ");
        TargetDataLine targetDataLine;
        try {
            assert comboBox != null;
            targetDataLine = AudioSystem.getTargetDataLine(this.audioFormat, AudioIO.getMixerInfo(
                    (String) comboBox.getValue()));
        } catch (LineUnavailableException e) {
            targetDataLine = null;
            System.out.println("TargetDataLine Unavailable");
        }
        return targetDataLine;
    }
    private SourceDataLine SourceDataLineFromToolBar(ToolBar toolBar) {
        ComboBox comboBox = getComboBoxFromToolBar(toolBar, "Output Device : ");
        SourceDataLine sourceDataLine;
        try {
            assert comboBox != null;
            sourceDataLine = AudioSystem.getSourceDataLine(this.audioFormat, AudioIO.getMixerInfo(
                    (String) comboBox.getValue()));
        } catch (LineUnavailableException e) {
            sourceDataLine = null;
            System.out.println("SourceDataLine Unavailable");
        }
        return sourceDataLine;
    }
    private int FrameSizeFromToolBar(ToolBar toolBar) {
        return Integer.parseInt(Objects.requireNonNull(getTextFieldFromToolBar(toolBar)).getText());
    }

    private ComboBox getComboBoxFromToolBar(ToolBar toolBar, String labelText) {
        boolean comboBoxFound = false;
        for (Node item : toolBar.getItems()) {
            if (comboBoxFound && item instanceof ComboBox) {
                return (ComboBox) item;
            }

            if (item instanceof Label && ((Label) item).getText().equals(labelText)) {
                comboBoxFound = true;
            }
        }
        return null;
    }

    private TextField getTextFieldFromToolBar(ToolBar toolBar) {
        boolean textFieldFound = false;
        for (Node item : toolBar.getItems()) {
            if (textFieldFound && item instanceof TextField) {
                return (TextField) item;
            }

            if (item instanceof Label && ((Label) item).getText().equals("FrameSize : ")) {
                textFieldFound = true;
            }
        }
        return null;
    }


}