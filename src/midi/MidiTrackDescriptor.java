package midi;

import java.util.ArrayList;
import java.util.Vector;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

public class MidiTrackDescriptor {
    public  final static String[] NOTES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    private Vector<MidiEventDescriptor>events;
    private Track track;
    private int quanti, trackNumber;
	private boolean noteonstate = true;
	private boolean noteoffstate = true;

    public MidiTrackDescriptor(Track track, int trackNumber, int quanti, boolean noteonstate, boolean noteoffstate) throws MidiException {
        this.noteonstate = noteonstate;
        this.noteoffstate = noteoffstate;
        this.quanti = quanti;
        this.trackNumber=trackNumber;
        this.track = track;
        events = new Vector<MidiEventDescriptor>();        
        for (int i=0; i < track.size(); i++) {
                if(track.get(i).getMessage() instanceof ShortMessage)
                    events.addElement(new ShortMessageMidiEventDescriptor(track.get(i), quanti, noteonstate, noteoffstate));
                else if(track.get(i).getMessage() instanceof MetaMessage)
                    events.addElement(new MetaMessageMidiEventDescriptor(track.get(i)));
                else if(track.get(i).getMessage() instanceof SysexMessage)
                    events.addElement(new SysexMessageMidiEventDescriptor(track.get(i)));
                else
                	throw new MidiException("Unsupported message exception !!");
        }
    }

    public int getEventsCount() {
        return events.size();
    }

    public MidiEventDescriptor getMidiEventDescriptor(int index) {
        return events.get(index);
    }
    
    public void quantifier (){
    	ArrayList<MidiEvent> events=new ArrayList<MidiEvent>();
        for (int k=0; k < track.size(); k++) {
            MidiEvent event1 =track.get(k);
            if(event1.getMessage()instanceof ShortMessage){
            	events.add(event1);
            }
        }
        
        int i=0;
        while(i < events.size()){
            int j=i+1;
            boolean ok= true;
    	    	ArrayList<MidiEvent> eventToQ=new ArrayList<MidiEvent>();
            while(j<events.size() && ok){
                if(Math.abs(events.get(i).getTick()-events.get(j).getTick())<=quanti){
                            if(events.get(i).getTick()!=events.get(j).getTick()) {
                                eventToQ.add(events.get(j));                                       
                            }
                            j++;
            	} else {
                	ok=false;
                        }
                }
                if(eventToQ.size() >=2) {
        	    	ArrayList<ShortMessage> msg=new ArrayList<ShortMessage>();
        	    	ArrayList<String> notes=new ArrayList<String>();
                    String[] chords =new String[eventToQ.size()];
                    for(int k = 0; k < eventToQ.size(); k++) {
                        //System.out.println("fauuse"+"1tick"+events.get(i).getTick()+"emetick"+eventToQ.get(k).getTick());
                        eventToQ.get(k).setTick(events.get(i).getTick());
                      //  System.out.println("true"+"1tick"+events.get(i).getTick()+"emetick"+eventToQ.get(k).getTick());
                        msg.add((ShortMessage) eventToQ.get(k).getMessage());
                        int key = msg.get(k).getData1();
                        int note = key % 12;
                        notes.add(NOTES[note]);
                        chords[k]=NOTES[note];
                        }                             
                    i=j;
                } else {
                    i++;
                }
        }                
    	
    }
    
    public String toString() {
    	String string = "Track " + trackNumber + ": size = " + events.size()+"\n";
        for (int i=0; i < events.size(); i++) {
        	MidiEventDescriptor event = events.get(i);
        	if(event instanceof ShortMessageMidiEventDescriptor)
        		string += ((ShortMessageMidiEventDescriptor)event).toString();
        }
    	return string;
    }
    
}
