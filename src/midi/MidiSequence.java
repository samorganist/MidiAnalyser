package midi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiSequence {
    public  final static String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public  final static int NOTE_ON = 0x90;
    private  final static int NOTE_OFF = 0x80 ;
    private  final static int TEMPO = 0x51 ;
    private  final static int PITCH_BEND = 0xE0;


    private String midiFilePath;
    private Sequence sequence;
    private Vector<MidiTrackDescriptor> tracks;
    private int quanti;
    private boolean chords;
	private boolean noteonstate;
	private boolean noteoffstate;
	
    public MidiSequence(String midiFilePath, int quant, boolean noteonstate, boolean noteoffstate, boolean chords) throws MidiException {
        this.noteonstate = noteonstate;
        this.noteoffstate = noteoffstate;
        this.chords = chords;       
    	try {
            this.midiFilePath = midiFilePath;
            sequence = MidiSystem.getSequence(new File(midiFilePath));
            checkresolution(quant);
            tracks = new Vector<MidiTrackDescriptor>();
            int trackNumber = 0;
            for (Track track :  sequence.getTracks()) {
            	trackNumber++;
                tracks.addElement(new MidiTrackDescriptor(track, trackNumber, quanti, noteonstate, noteoffstate));
            }
        } catch (InvalidMidiDataException ex) {
            throw new MidiException(ex.getMessage());
        } catch (IOException ex) {
            throw new MidiException(ex.getMessage());
        }
    }

    public Sequence getSequence() {
        return sequence;
    }

    public int getTracksCount() {
        return tracks.size();
    }

    public MidiTrackDescriptor getMidiTrackDescriptor(int index) {
        return tracks.get(index);
    }

    public void processMessages() {
        for (MidiTrackDescriptor midiTrackDescriptor :  tracks) {
            for(int i = 0; i < midiTrackDescriptor.getEventsCount(); i++) {
                MidiEventDescriptor midiEventDescriptor = midiTrackDescriptor.getMidiEventDescriptor(i);
                midiEventDescriptor.processMessage();
            }
        }
    }
    public void saveMidiFileAs(File f) throws IOException {    	   	 
			MidiSystem.write(sequence,1,f);	
    }	
	private void checkresolution(int quant) {
		int resolution = sequence.getResolution();
		switch (resolution){
			case 48:
				quanti=(int) (quant/2);
			break;
			case 96:
				quanti=quant;
			break;
			case 240:
				quanti=(int) (quant*2.5);
			break;
			case 384:
				quanti=(int) (quant*4);
			break;
			case 480:
				quanti=(int) (quant*5);
			break;
		}		
	}
    public void quantifier () {
    	if(chords)
    		for(MidiTrackDescriptor track : tracks)
    			track.quantifier();
    }    
    public String toString() {
    	String string = ""; 		
		try {
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.setSequence(sequence);	    
		  	float tempo=sequencer.getTempoInBPM();
		 	System.out.println("Tempo: "+tempo);
		    System.out.println("Resolution: "+sequence.getResolution());
		    int trackNumber = 0 ;
		    for(MidiTrackDescriptor track : tracks) {
		    	trackNumber++;
		    	string += track.toString();
		    }
		    return string;
		} catch (MidiUnavailableException e) {
			return e.getMessage();
		} catch (InvalidMidiDataException e) {
			return e.getMessage();
		}
	}
}