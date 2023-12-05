package audio;

import javax.sound.sampled.*;

public class AudioProcessor implements Runnable {
    private AudioSignal inputSignal, outputSignal;
    private TargetDataLine audioInput;
    private SourceDataLine audioOutput;
    private boolean isThreadRunning;

    public AudioProcessor(TargetDataLine audioInput, SourceDataLine audioOutput, int frameSize) {
        inputSignal = new AudioSignal(frameSize);
        outputSignal = new AudioSignal(inputSignal);
        this.audioInput = audioInput;
        this.audioOutput = audioOutput;
    }

    @Override
    public void run() {
        isThreadRunning = true;
        while (isThreadRunning) {
            inputSignal.recordFrom(audioInput);

            outputSignal.setFrom(inputSignal);

            outputSignal.playTo(audioOutput, true);
        }
    }


    public void terminateAudioThread() {
        this.isThreadRunning = false;
        this.audioOutput.close();
        this.audioInput.close();
    }


    public AudioSignal getInputSignal() {
        return inputSignal;
    }

    public void setInputSignal(AudioSignal inputSignal) {
        this.inputSignal = inputSignal;
    }

    public void setFrameSize(int FrameSize) {
        this.inputSignal.setFrameSize(FrameSize);
        this.outputSignal.setFrameSize(FrameSize);
    }

    public AudioSignal getOutputSignal() {
        return outputSignal;
    }

    public void setOutputSignal(AudioSignal outputSignal) {
        this.outputSignal = outputSignal;
    }

    public TargetDataLine getAudioInput() {
        return audioInput;
    }

    public void setAudioInput(TargetDataLine audioInput) {
        this.audioInput = audioInput;
    }

    public SourceDataLine getAudioOutput() {
        return audioOutput;
    }

    public void setAudioOutput(SourceDataLine audioOutput) {
        this.audioOutput = audioOutput;
    }

    public static void main(String[] args) {
        AudioIO.printAudioMixers();
        AudioFormat audioFormat = new AudioFormat(16000.0f, 16, 1, true,
                true);
        TargetDataLine inLine = AudioIO.obtainAudioInput("Headset Microphone (Realtek(R)) ", 16000);
        SourceDataLine outLine = AudioIO.obtainAudioOutput("Headphone (Realtek(R) Audio)", 16000);
        AudioProcessor as = new AudioProcessor(inLine, outLine, 1024);

        try {
            inLine.open(audioFormat);
            inLine.start();
            outLine.open(audioFormat);
            outLine.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        new Thread(as).start();

        System.out.println("New thread");
    }
}