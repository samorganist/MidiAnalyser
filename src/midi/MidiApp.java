package midi;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.Component;
import javax.swing.Box;
import javax.swing.JPanel;

import org.eclipse.swt.events.SelectionEvent;

import java.awt.Panel;
import javax.swing.JScrollBar;
import java.awt.ScrollPane;
import java.awt.List;


public class MidiApp {
	final static int[]STEPVALUES = new int[]{384,96,48,32,24,16,12,6,4};
	private JFrame frame;
	private JTextField textField;
	private JButton btnExtit;
	private JButton btnSaveMidi;
	private File path;
	private MidiSequence sequenceOriginal;
	private MidiSequence SequenceAnalysé;
	private boolean noteon;
	private boolean noteoff;
	private boolean chordson;
	private int pas;
	private JTextPane textPane_1;
	private JTextPane textPane;
	private JComboBox<String> step;
	private JCheckBox chckbxNoteOff;
	private JCheckBox chckbxNoteOn;
	private JCheckBox chboxchords;
	private JButton btnAnalyseMidi;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {							
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//	UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsFieldCaret");
					MidiApp window = new MidiApp();
					window.frame.setVisible(true);
					window.frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MidiApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 729, 414);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		final JLabel lblChoseFile = new JLabel("Chose File");
		lblChoseFile.setBounds(513, 15, 61, 14);
		frame.getContentPane().add(lblChoseFile);
		
		JButton btnOpenFile = new JButton("Open Midi");
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				File fichier = null;
		    	String userDir = System.getProperty("user.home");
		        JFileChooser dialogue = new JFileChooser(new File(userDir +"/Desktop"));
		        dialogue.setFileFilter(new TypeFilter(".mid","MidiFile."  ));
		    	PrintWriter sortie = null;
		    	if (dialogue.showOpenDialog(null)== 
		    	    JFileChooser.APPROVE_OPTION) {
		    	    fichier = dialogue.getSelectedFile();
		    		textField.setEditable(false);
		    		path=fichier;
		    		pas=STEPVALUES[step.getSelectedIndex()];
		    		try {
						sequenceOriginal=new MidiSequence(path.getPath(),pas,false,false,false);
						//System.out.println("  path"+path.getAbsolutePath());
						sequenceOriginal.processMessages();
						//System.out.println("  pas   "+pas);

						textPane.setText(sequenceOriginal.toString())	;					
						
						
					} catch (MidiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	    textField.setText(fichier.getPath());}}
		});
		btnOpenFile.setBounds(614, 11, 89, 23);
		frame.getContentPane().add(btnOpenFile);
		
		textField = new JTextField();
		textField.setBounds(10, 12, 476, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		btnExtit = new JButton("Exit");
		btnExtit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
			
					Object[] options = {"Yes","Return"};
	        int n = JOptionPane.showOptionDialog(null,
	           "Are you sure want to Exit??",
	           "Exit",
	            JOptionPane.YES_NO_OPTION,
	            JOptionPane.QUESTION_MESSAGE,
	            null,     
	            options,  
	            options[0]); 
					if (n == JOptionPane.YES_OPTION) System.exit(0);
				
			}
														
		
		});
		btnExtit.setBounds(614, 341, 89, 23);
		frame.getContentPane().add(btnExtit);
		
		btnSaveMidi = new JButton("Save Midi");
		btnSaveMidi.setBounds(515, 341, 89, 23);
		frame.getContentPane().add(btnSaveMidi);
		btnSaveMidi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Specify a file to save");   
                fileChooser.setFileFilter(new TypeFilter(".mid","MidiFile."  ));
				int userSelection = fileChooser.showSaveDialog(frame);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
					
					
				    File fileToSave = fileChooser.getSelectedFile();
				    try {
						File fileOut =  new File(fileToSave+".mid");
						SequenceAnalysé.saveMidiFileAs(fileOut);

					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				 
					
					
    
				}

				
			}
				
		});
		JButton btnAnalyseMidi = new JButton("Analyse");		
		btnAnalyseMidi.setBounds(10, 307, 102, 23);
		frame.getContentPane().add(btnAnalyseMidi);
		btnAnalyseMidi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				noteon=chckbxNoteOn.isSelected();
				noteoff=chckbxNoteOff.isSelected();
				
				chordson=chboxchords.isSelected();
				pas=STEPVALUES[step.getSelectedIndex()];
				System.out.println("passss "+pas);
	    		try {
	    			SequenceAnalysé=new MidiSequence(path.getPath(),pas,noteon,noteoff,chordson);
	    			SequenceAnalysé.processMessages();
	    			SequenceAnalysé.quantifier();
					textPane_1.setText(SequenceAnalysé.toString())	;					
					
					
				} catch (MidiException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
													
	
			}});
		
		JButton btnNewButton = new JButton("Reset ");
		btnNewButton.setBounds(10, 341, 102, 23);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblStep = new JLabel("Step");
		lblStep.setBounds(10, 114, 46, 14);
		frame.getContentPane().add(lblStep);
		
		step = new JComboBox<String>();
		step.setBounds(45, 111, 67, 20);
		frame.getContentPane().add(step);
		step.addItem("Bar");//divisionsur 384
		step.addItem("Beat");//division sur 96
		step.addItem("1/2 beat");//division sur 48
		step.addItem("1/3 beat");//division sur 32
		step.addItem("1/4 beat");//division sur 24
		step.addItem("1/2 step");//division sur 12
		step.addItem("1/3 step");//division sur 8
		step.addItem("1/4 step");//division sur 6
		step.addItem("1/6 step");//division sur 6

		step.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		    	final int val=STEPVALUES[step.getSelectedIndex()];
		    	System.out.println(val);
		    			    		    	
					    }
		});
				
				JPanel panel_2 = new JPanel();
				panel_2.setBorder(BorderFactory.createTitledBorder("Quantize options"));
				panel_2.setBounds(10, 149, 127, 110);
				frame.getContentPane().add(panel_2);
						
			            chckbxNoteOff = new JCheckBox("Note Off");
						panel_2.add(chckbxNoteOff);
						noteoff=chckbxNoteOff.isSelected();
						
					   chckbxNoteOn = new JCheckBox("Note On");
					    panel_2.add(chckbxNoteOn);
					    noteon=chckbxNoteOn.isSelected();
					    chboxchords = new JCheckBox("Chords ");
						panel_2.add(chboxchords);
						chordson=chboxchords.isSelected();
				
						textPane_1 = new JTextPane();
                        textPane_1.setBounds(435, 63, 285, 288);
                      //  frame.getContentPane().add(textPane_1);
						
						JButton btnNewButton_1 = new JButton("Clear");
						btnNewButton_1.setBounds(10, 43, 99, 23);
						frame.getContentPane().add(btnNewButton_1);
					    textPane = new JTextPane();
                        //frame.getContentPane().add(textPane);
                        textPane.setBounds(134, 43, 285, 288);
						ScrollPane scrollPane = new ScrollPane();
						scrollPane.setBounds(437, 63, 260, 268);
						scrollPane.add(textPane_1);

						frame.getContentPane().add(scrollPane);
                        
                        
                        
                        ScrollPane scrollPane_1 = new ScrollPane();
                        scrollPane_1.setBounds(150, 63, 260, 267);
                        frame.getContentPane().add(scrollPane_1);
						scrollPane_1.add(textPane);

						JPanel panel = new JPanel();
						panel.setBounds(142, 49, 275, 290);
						frame.getContentPane().add(panel);
						panel.setBorder(BorderFactory.createTitledBorder("Original  Midi"));
						
						JPanel panel_1 = new JPanel();
						panel_1.setBorder(BorderFactory.createTitledBorder("Ananlysed  Midi"));
						panel_1.setBounds(430, 49, 275, 290);
						frame.getContentPane().add(panel_1);

                                
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}
