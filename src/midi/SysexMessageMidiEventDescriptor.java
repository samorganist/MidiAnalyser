package midi;

import javax.sound.midi.MidiEvent;

public class SysexMessageMidiEventDescriptor extends MidiEventDescriptor {

    SysexMessageMidiEventDescriptor(MidiEvent event) {
        super(event);
    }

    @Override
    public void processMessage () {
    }

}
