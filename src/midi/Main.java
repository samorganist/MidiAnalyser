
package midi;

import java.io.File;
import java.io.IOException;
import midi.MidiException;
import midi.MidiSequence;


public class Main {

	private static boolean noteon=true;
	private static boolean noteoff=true;
	private static boolean chordson=false;
	private static int pas=4;
   public static void main(String[] args) throws IOException {
    	
    	
    	try {
            MidiSequence seq = new MidiSequence("G:\\Takefive.mid",pas, noteon, noteoff, chordson);
            seq.processMessages();
           System.out.println(seq.toString());
           chordson=true;
            seq.quantifier();
            System.out.println("apreees");
            System.out.println(seq.toString());
            File f = new File("G:\\test30.mid");
			seq.saveMidiFileAs(f);	  
        } catch (MidiException ex) {
            System.out.println(ex.getMessage());
        }
    	
    
    	
    	
    }

}
