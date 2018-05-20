package midi;

import javax.sound.midi.MidiEvent;

public class MetaMessageMidiEventDescriptor extends MidiEventDescriptor {

    MetaMessageMidiEventDescriptor(MidiEvent event) {
        super(event);
    }

    @Override
    public void processMessage () {
    }

}
