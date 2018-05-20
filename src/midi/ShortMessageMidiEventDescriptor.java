package midi;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class ShortMessageMidiEventDescriptor extends MidiEventDescriptor {

    private int quanti;
	private boolean noteonstate ;
	private boolean noteoffstate ;
	
    ShortMessageMidiEventDescriptor(MidiEvent event, int quanti, boolean noteonstate, boolean noteoffstate) {
        super(event);
        this.noteonstate = noteonstate;
        this.noteoffstate = noteoffstate;
        this.quanti = quanti;
    }

    @Override
    public void processMessage () {
        ShortMessage sm = (ShortMessage) midiMessage;
    	if(sm.getCommand() == NOTE_ON) {
            int key = sm.getData1();
            int octave = (key / 12);
            int note = key % 12;
	   	   
            if(event.getTick()%quanti!=0&&noteonstate){
	   	    	long tick=searchrighttick(event.getTick(),quanti);
	   	    	event.setTick(tick);

	   	    }
            String noteName = NOTES[note];
            int velocity = sm.getData2();
        } else if (sm.getCommand() == NOTE_OFF) {
            int key = sm.getData1();
            int octave = (key / 12);
            int note = key % 12;
            String noteName = NOTES[note];
            int velocity = sm.getData2();
           if(event.getTick()%quanti!=0&&noteoffstate){
               long tick=searchrighttickoff(event.getTick(),quanti);
               event.setTick(tick);
            }
        }
    }
    
    
    
    
    private static long searchrighttickoff(long tick, int quanti) {
    	long tickmin = 0;
		long tickmax = 0;
    	for (int i=-quanti; i<=0; i++) {
			long ticklocal=tick+i;
    		if(ticklocal%quanti==0){
    			tickmin=ticklocal;	
    		}	
	    }
    	for (int i=0; i<=quanti; i++){
    		long ticklocalmax=tick+i;
		    if(ticklocalmax%quanti==0){
		    	tickmax=ticklocalmax;	
		    }	
		}	    		
	    if(Math.abs(tickmin-tick)<Math.abs(tickmax-tick)){
	    	tick=tickmin;
	    	//System.out.println("tickmin"+tick);
	    } else {
	    	tick=tickmax;
	    	//System.out.println("tickmax"+tick);
	    }
		return tick;
	}
    
	public static long searchrighttick(long tick,int quanti){	 
		long tickmin = 0;
		long tickmax = 0;
		for (int i=-quanti; i<=0; i++){
			long ticklocal=tick+i;
    		if(ticklocal%quanti==0){
    			tickmin=ticklocal;	
    		}
    	}
		for (int i=1; i<=quanti; i++) {
			long ticklocalmax=tick+i;
	    	if(ticklocalmax%quanti==0) {
	    			tickmax=ticklocalmax;	
	    	}	
	    }    		
    	if(Math.abs(tick-tickmin)<Math.abs(tickmax-tick)) {
    		tick=tickmin;
    	} else {
    		tick=tickmax;
    	}
    	return tick;
    }
	public String toString() {
    	String string = "";
        if (midiMessage instanceof ShortMessage) {
        	ShortMessage sm = (ShortMessage) midiMessage;
        	if(sm.getCommand() == NOTE_ON) {
        		string += "Ch: " + sm.getChannel() + " ";	
               int key = sm.getData1();
               int octave = (key / 12);
               int note = key % 12;
                string += "@" + event.getTick() + " ";
                String noteName = NOTES[note];
                int velocity = sm.getData2();
                string += "Note on, " + noteName + octave + " key=" + key + " vel: " + velocity+"\n";
            } else if (sm.getCommand() == NOTE_OFF) {
            	string += "Ch: " + sm.getChannel() + " ";
            	string += "@" + event.getTick() + " ";
                int key = sm.getData1();
                int octave = (key / 12);
                int note = key % 12;
                String noteName = NOTES[note];
                int velocity = sm.getData2();
                string += "Note off, " + noteName + octave + " key=" + key + " vel: " + velocity+"\n";
          }
       }
       return string;
    }
    
}
