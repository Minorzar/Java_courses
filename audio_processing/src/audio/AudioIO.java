package audio;

import javax.sound.sampled.*;
import java.util.Arrays;

public class AudioIO {
    public static void printAudioMixers() {
        System.out.println("Mixers:");
        Arrays.stream(AudioSystem.getMixerInfo()).forEach(e -> System.out.println("- name=\"" + e.getName() + "\" description=\"" + e.getDescription() + " by " + e.getVendor() + "\""));
    }
    public static Mixer.Info getMixerInfo(String mixerName) {
        return Arrays.stream(AudioSystem.getMixerInfo()).filter(e -> e.getName().equalsIgnoreCase(mixerName)).findFirst().get();
    }

    public static TargetDataLine obtainAudioInput(String mixerName, int sampleRate) throws LineUnavailableException {
        Mixer.Info mixerInfo = getMixerInfo(mixerName);
        if (mixerInfo.getName().equals(mixerName)) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            Line.Info[] targetLineInfos = mixer.getTargetLineInfo();
            for (Line.Info targetLineInfo : targetLineInfos) {
                if (targetLineInfo instanceof DataLine.Info) {
                    DataLine.Info dataLineInfo = (DataLine.Info) targetLineInfo;
                    AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);

                    if (dataLineInfo.isFormatSupported(format)) {
                        TargetDataLine targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
                        return targetDataLine;
                    }
                }
            }
        }
        throw new LineUnavailableException("Unable to obtain audio input line with specified parameters.");
    }


    public static SourceDataLine obtainAudioOutput(String mixerName, int sampleRate)  throws LineUnavailableException {
        Mixer.Info mixerInfo = getMixerInfo(mixerName);

        if (mixerInfo.getName().equals(mixerName)) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);

            Line.Info[] sourceLineInfos = mixer.getSourceLineInfo();
            for (Line.Info sourceLineInfo : sourceLineInfos) {
                if (sourceLineInfo instanceof DataLine.Info) {
                    DataLine.Info dataLineInfo = (DataLine.Info) sourceLineInfo;
                    AudioFormat format = new AudioFormat(sampleRate, 16, 2, true, false);

                    if (dataLineInfo.isFormatSupported(format)) {
                        SourceDataLine sourceDataLine = (SourceDataLine) mixer.getLine(dataLineInfo);
                        return sourceDataLine;
                    }
                }
            }
        }
        throw new LineUnavailableException("Unable to obtain audio output line with specified parameters.");
    }

}
