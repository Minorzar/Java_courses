package audio;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioProcessor implements Runnable {

    private AudioSignal inputSignal, outputSignal;
    private TargetDataLine audioInput;
    private SourceDataLine audioOutput;
    private boolean isThreadRunning; // makes it possible to "terminate" thread

    public AudioProcessor(TargetDataLine audioInput, SourceDataLine audioOutput, int frameSize) {
        this.inputSignal = new AudioSignal(frameSize) ;
        this.outputSignal = new AudioSignal(frameSize) ;
        this.audioOutput = audioOutput ;
        this.audioInput = audioInput ;

    }

    @Override
    public void run() {
        isThreadRunning = true;
        while (isThreadRunning) {
            inputSignal.recordFrom(audioInput);
            // your job: copy inputSignal to outputSignal with some audio effect

            outputSignal.playTo(audioOutput);
        }
    }

    public void terminateAudioThread() {}
        // todo here: all getters and setters

        /* an example of a possible test code */
        public static void main(String[] args) {
            try{
                TargetDataLine inLine = AudioIO.obtainAudioInput("Default Audio Device", 16000);
                SourceDataLine outLine = AudioIO.obtainAudioOutput("Default Audio Device", 16000);
                AudioProcessor as = new AudioProcessor(inLine, outLine, 1024);
                inLine.open(); inLine.start(); outLine.open(); outLine.start();
                new Thread(as).start();
                System.out.println("A new thread has been created!");
            }
            catch (Exception e){
                throw new RuntimeException("Something went wrong while running the program") ;
            }
        }
}