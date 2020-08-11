package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import algorithm.*;
import dialogs.ProgressDialog;
import utilities.*;
import result.ResultPage;

public class Cryptography {
	private static JFrame frame;
	private static JTextField keyInput,path;
	private static JRadioButton text,file;
	private static ButtonGroup bg;
	private static JTextArea textArea;
	private static JButton proceed, browse, clear;
	private static JPanel filePanel;
	

	private static String key,binaryKey;
	public static File plainText,convertedText1,convertedText2,cipherText1,cipherText2,recoveredText1,recoveredText2;
	public static long startTime,kg1,kg2,mt1,mt2,et1,et2,dt1,dt2,rt1,rt2,totalTime1,totalTime2;
	
	private static SwingWorker<Void,Void> worker = null;
	private static ProgressDialog dialog;

	public Cryptography() {
		initialize();
		Utility.createMap();  //Define character set
	}

	private void initialize() {
		frame = new JFrame("Modified DES Algorithm");
		frame.setUndecorated(true);
		frame.getContentPane().setBackground(new Color(250, 255, 210));
		frame.getContentPane().setLayout(null);
		frame.setBounds(100, 100, 500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
		frame.setAlwaysOnTop(false);
		
		JPanel title_container = new JPanel();
		title_container.setBounds(0, 0, 500, 80);
		frame.getContentPane().add(title_container);
		title_container.setBackground(Color.BLUE);
		title_container.setLayout(null);
		
		JLabel title = new JLabel("Data Encryption Standard");
		title.setVerticalAlignment(SwingConstants.TOP);
		title.setForeground(new Color(255, 255, 255));
		title.setFont(new Font("Georgia Pro Light", Font.BOLD, 33));
		title.setHorizontalAlignment(SwingConstants.CENTER);;
		title.setBounds(0, 26, 500, 44);
		title_container.add(title);
		
		JLabel close = new JLabel("X");
		close.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				System.exit(0);
			}
		});
		close.setHorizontalAlignment(SwingConstants.CENTER);
		close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		close.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		close.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
		close.setOpaque(true);
		close.setForeground(Color.WHITE);
		close.setBackground(Color.RED);
		close.setHorizontalTextPosition(SwingConstants.CENTER);
		close.setBounds(467, 0, 33, 21);
		title_container.add(close);
		
		JLabel minimize = new JLabel("-");
		minimize.setBounds(430, 0, 33, 21);
		minimize.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		minimize.setPreferredSize(new Dimension(8, 16));
		minimize.setToolTipText("Minimize");
		minimize.setHorizontalAlignment(SwingConstants.CENTER);
		minimize.setForeground(new Color(0, 0, 0));
		minimize.setFont(new Font("Gill Sans Ultra Bold", Font.BOLD, 16));
		minimize.setBackground(Color.WHITE);
		minimize.setOpaque(true);
		minimize.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				frame.setState(Frame.ICONIFIED);
			}
		});
		title_container.add(minimize);
		
				
		JLabel keyLabel = new JLabel("Plase enter 64-bit key");
		keyLabel.setOpaque(true);
		keyLabel.setBackground(new Color(255, 228, 181));
		keyLabel.setBounds(10, 90, 480, 26);
		frame.getContentPane().add(keyLabel);
		keyLabel.setFont(new Font("Consolas", Font.BOLD, 15));
		keyLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		keyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		keyInput = new JTextField("");
		keyInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(keyInput.getText().length()>=11)
					e.consume();
			}
		});
		keyInput.setBounds(153, 126, 192, 26);
		frame.getContentPane().add(keyInput);
		keyInput.setSelectionColor(new Color(0, 120, 215));
		keyInput.setToolTipText("Key");
		keyInput.requestFocus();
		keyInput.setHorizontalAlignment(SwingConstants.CENTER);
		keyInput.setFont(new Font("Mongolian Baiti", Font.PLAIN, 16));
		keyInput.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		keyInput.setBackground(new Color(245, 255, 250));
		keyInput.setColumns(10);
		
		JLabel dataLabel = new JLabel("Input the data to be encrypted");
		dataLabel.setOpaque(true);
		dataLabel.setBackground(new Color(255, 228, 181));
		dataLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		dataLabel.setHorizontalAlignment(SwingConstants.CENTER);
		dataLabel.setFont(new Font("Consolas", Font.BOLD, 15));
		dataLabel.setBounds(10, 165, 480, 26);
		frame.getContentPane().add(dataLabel);
		
		text = new JRadioButton("Text box");
		text.setSelected(true);
		text.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textArea.setVisible(true);
				clear.setVisible(true);
				filePanel.setVisible(false);
				textArea.requestFocus();
			}
		});
		text.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		text.setOpaque(false);
		text.setFocusPainted(false);
		text.setBounds(220, 201, 73, 21);
		frame.getContentPane().add(text);
		
		file = new JRadioButton("File");
		file.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				filePanel.setVisible(true);
				filePanel.requestFocus();
				textArea.setVisible(false);
				clear.setVisible(false);
			}
		});
		file.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		file.setOpaque(false);
		file.setFocusPainted(false);
		file.setBounds(307, 201, 66, 21);
		frame.getContentPane().add(file);
		
		bg = new ButtonGroup();
		bg.add(text);
		bg.add(file);
		
		JLabel labelinput = new JLabel("Input mode  :");
		labelinput.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		labelinput.setBounds(132, 201, 82, 21);
		frame.getContentPane().add(labelinput);
		
		filePanel = new JPanel();
		filePanel.setBackground(new Color(255, 222, 173));
		filePanel.setOpaque(false);
		filePanel.setBorder(null);
		filePanel.setVisible(false);
		filePanel.setBounds(10, 228, 480, 178);
		frame.getContentPane().add(filePanel);
		filePanel.setLayout(null);
		
		path = new JTextField();
		path.setBackground(Color.WHITE);
		path.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		path.setEditable(false);
		path.setBounds(10, 24, 460, 22);
		filePanel.add(path);
		path.setColumns(10);
		
		browse = new JButton("Browse");
		browse.setFocusPainted(false);
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				JDialog wrapper = new JDialog(frame);
				fileChooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES","txt");
				fileChooser.addChoosableFileFilter(filter);
				int x=fileChooser.showOpenDialog(wrapper);
				if(x==JFileChooser.APPROVE_OPTION) {
					path.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		browse.setForeground(new Color(0, 0, 0));
		browse.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 14));
		browse.setFocusTraversalPolicyProvider(true);
		browse.setBounds(189, 61, 91, 27);
		filePanel.add(browse);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setBounds(10, 228, 480, 178);
		textArea.setFont(new Font("Mongolian Baiti", Font.PLAIN, 16));
		textArea.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		frame.getContentPane().add(textArea);
		
		clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
			}
		});
		clear.setForeground(Color.BLACK);
		clear.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 12));
		clear.setFocusTraversalPolicyProvider(true);
		clear.setFocusPainted(false);
		clear.setBounds(202, 406, 84, 27);
		frame.getContentPane().add(clear);
		
		proceed = new JButton("Proceed >");
		proceed.setForeground(Color.WHITE);
		proceed.setFont(new Font("Century Gothic", Font.BOLD, 15));
		proceed.setFocusTraversalPolicyProvider(true);
		proceed.setFocusPainted(false);
		proceed.setDefaultCapable(false);
		proceed.setBorder(null);
		proceed.setBackground(new Color(0, 0, 255));
		proceed.setBounds(202, 455, 91, 35);
		frame.getContentPane().add(proceed);

		proceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				key = keyInput.getText();
				if(key.equals("") || key.length()!=11) {
					JOptionPane.showMessageDialog(frame,"Please enter valid 11-character key!", "Imcomplete input",JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if((text.isSelected() && textArea.getText().length()==0) || (file.isSelected() && path.getText().isEmpty())) 
					JOptionPane.showMessageDialog(frame,"Please enter input text in the text area or choose valid text file!", "Imcomplete input",JOptionPane.ERROR_MESSAGE);
				else {
					if(text.isSelected())
						proceed(0);
					else if(file.isSelected())
						proceed(1);
				}
			}
		});
		frame.setVisible(true);
	}
	
	private void proceed(final int inputType) {
		proceed.setVisible(false);
		final DES alg1 = new DES();
		final ModifiedDES alg2 = new ModifiedDES();
		
		worker = new SwingWorker<Void,Void>() {
			ResultPage obj = new ResultPage(frame,alg1,alg2);
			
			@Override
			protected Void doInBackground()  {
				try {
						dialog.t.setText("Processing key for encryption"); 
						binaryKey=Utility.toBinary(key).substring(0,64); //Consider 64 bit only  
						dialog.p.setValue(1);
						
						dialog.t.setText("Processing input data and converting to binary");
						if(inputType==0)
							plainText = Utility.toBinary(new StringBuffer(textArea.getText()), dialog.p);
						else if(inputType==1) 
							plainText = Utility.toBinary(new File(path.getText()), dialog.p);
						dialog.p.setValue(3);
						if(worker.isCancelled()) {proceed.setVisible(true); return null;}
					
					startTime = System.nanoTime();
						dialog.t.setText("Original DES : Generating Keys");
						alg1.generateKeys(binaryKey, dialog.p);  //Generate 16 keys
						dialog.p.setValue(5); 
					kg1 = System.nanoTime() - startTime;
					if(worker.isCancelled()) {proceed.setVisible(true); return null;}
						
					startTime = System.nanoTime();
						dialog.t.setText("Original DES : Modifying input text to standard format");
						convertedText1 = Utility.convertText(plainText, new File("generated/Original DES/convertedText.txt"), 64, dialog.p, worker);
						dialog.p.setValue(8);
					mt1 = System.nanoTime() - startTime;
					if(worker.isCancelled()) {proceed.setVisible(true); return null;}
						
					startTime = System.nanoTime();
						dialog.t.setText("Original DES : Encrypting the data");
						cipherText1=alg1.encrypt(convertedText1, new File("generated/Original DES/cipherText.txt"), dialog.p, worker);   //Generate cipher text
						dialog.p.setValue(63); 
					et1 = System.nanoTime() - startTime;
					if(worker.isCancelled()) {proceed.setVisible(true); return null;}
					
					startTime = System.nanoTime();
						dialog.t.setText("Modified DES : Generating Keys");
						alg2.generateKeys(binaryKey+binaryKey);  //Generate 5 keys
						dialog.p.setValue(64); 
					kg2 = System.nanoTime() - startTime;
					if(worker.isCancelled()) {proceed.setVisible(true); return null;}
						
					startTime = System.nanoTime();
						dialog.t.setText("Modified DES : Modifying input text to standard format");
						convertedText2 = Utility.convertText(plainText, new File("generated/Modified DES/convertedText.txt"), 128, dialog.p, worker);
						dialog.p.setValue(67);
					mt2 = System.nanoTime() - startTime;
					if(worker.isCancelled()) {proceed.setVisible(true); return null;}
						
					startTime = System.nanoTime();
						dialog.t.setText("Modified DES : Encrypting the data");
						cipherText2=alg2.encrypt(convertedText2, new File("generated/Modified DES/cipherText.txt"), dialog.p, worker);   //Generate cipher text
						dialog.p.setValue(92);
					et2 = System.nanoTime() - startTime;
					if(worker.isCancelled()) {proceed.setVisible(true); return null;}
						
						dialog.t.setText("Loading data to GUI");
						obj.loadData(dialog.p, worker);  
						dialog.p.setValue(100);
						
				} 
				catch(UnsupportedCharacterException e) {
					JOptionPane.showMessageDialog(null, "Data contains unsupported characters!!","Unsupported Data",JOptionPane.ERROR_MESSAGE);
					if(dialog!=null && dialog.isShowing()) {dialog.dispose(); dialog.closed=true;}
					worker.cancel(false);
					proceed.setVisible(true);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
			protected void done() {
		        if(dialog!=null && !dialog.closed) {
		        	proceed.setVisible(true);
		        	obj.setVisible(true);
		        	dialog.dispose();
		        	frame.setVisible(false);
		        }
			}
			
		};
		dialog = new ProgressDialog(frame, worker);
		dialog.setVisible(true);
		worker.execute();
	}
	
	public static String getKey() {
		return key;
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Cryptography();
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
