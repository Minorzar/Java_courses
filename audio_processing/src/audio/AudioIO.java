package audio;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Arrays;
public class AudioIO {
    public static void printAudioMixers() {
        System.out.println("Mixers:");
        Arrays.stream(AudioSystem.getMixerInfo())
                .forEach(e -> System.out.println("- name=\"" + e.getName() + "\" descr=\"" + e.getDescription() + " by "
                        + e.getVendor() + "\""));
    }


    public static ArrayList<Mixer.Info> getAudioMixers() {
        return new ArrayList<>(Arrays.asList(AudioSystem.getMixerInfo()));
    }


    public static Mixer.Info getMixerInfo(String mixerName) {
        return Arrays.stream(AudioSystem.getMixerInfo())
                .filter(e -> e.getName().equalsIgnoreCase(mixerName)).findFirst().get();
    }


    public static TargetDataLine obtainAudioInput(String mixerName, int sampleRate) {
        TargetDataLine targetDataLine;

        AudioFormat audioFormat = new AudioFormat((float) sampleRate, 16, 1, true,
                true);

        Mixer.Info mixerInfo = getMixerInfo(mixerName);

        try {
            targetDataLine = AudioSystem.getTargetDataLine(audioFormat, mixerInfo);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        return targetDataLine;
    }

    public static SourceDataLine obtainAudioOutput(String mixerName, int sampleRate) {
        SourceDataLine sourceDataLine;

        AudioFormat audioFormat = new AudioFormat((float) sampleRate, 16, 1, true,
                true);

        Mixer.Info mixerInfo = getMixerInfo(mixerName);

        try {
            sourceDataLine = AudioSystem.getSourceDataLine(audioFormat, mixerInfo);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        return sourceDataLine;
    }

    public static void main(String[] args) {

        int samplingRate = 8000;

        printAudioMixers();

        System.out.println(getMixerInfo("Headset Microphone (Realtek(R))"));
        System.out.println(obtainAudioInput("Headset Microphone (Realtek(R))", samplingRate));

        System.out.println(getMixerInfo("Headphone (Realtek(R) Audio)"));
        System.out.println(obtainAudioOutput("Headphone (Realtek(R) Audio)", samplingRate));
    }
}