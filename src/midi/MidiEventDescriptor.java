package midi;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

public abstract class MidiEventDescriptor {
    public  final static String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public  final static int NOTE_ON = 0x90;
    public  final static int NOTE_OFF = 0x80 ;
    protected MidiEvent event;
    protected MidiMessage midiMessage;

    protected MidiEventDescriptor(MidiEvent event) {
        this.event = event;
        midiMessage = event.getMessage();
    }

    public MidiEvent getEvent() {
        return event;
    }

    public MidiMessage getMidiMessage() {
        return midiMessage;
    }

    public abstract void processMessage();
}
