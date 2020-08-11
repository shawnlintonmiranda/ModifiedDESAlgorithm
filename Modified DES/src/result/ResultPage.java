package result;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import dialogs.ProgressDialog;
import dialogs.ShowKeys;
import dialogs.ShowTime;
import dialogs.ShowFrequencyPlot;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.*;

import javax.swing.border.MatteBorder;

import algorithm.DES;
import algorithm.ModifiedDES;
import utilities.Utility;

import static main.Cryptography.*;

public class ResultPage extends JFrame {
	private static final long serialVersionUID = 1L;
	private static JFrame frame;
	private JPanel contentPane;
	
	private static JLabel key1,key2, pTitle1, pTitle2, cTitle1, cTitle2;
	private static JButton viewKeys1,viewKeys2;
	private static JTextArea input1,input2,result1,result2;
	private static JRadioButton plainRadio1,modifiedRadio1,plainRadio2,modifiedRadio2;
	private static ButtonGroup bg1,bg2;
	private static boolean flag1=true, flag2=true, dataLoaded=false;
	private DES alg1;
	private ModifiedDES alg2;
	
	private static SwingWorker<Void,Void> worker = null;
	private static ProgressDialog dialog;
	private static ShowFrequencyPlot showPlot;
	
	private static LinkedHashMap<String,Integer> odes=null,mdes=null;
	private static long odesCount=0,mdesCount=0;
	public static SwingWorker<Void,Void> worker2 = null;
	private JButton entropy1;
	private JButton entropy2;

	public ResultPage(JFrame parent, DES alg1,ModifiedDES alg2) {
		super("Modified DES Algorithm");
		this.alg1 = alg1;
		this.alg2 = alg2;
		frame=this;
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screen=Toolkit.getDefaultToolkit().getScreenSize();
		screen.setSize(screen.width, screen.height-40);
		setMinimumSize(screen);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setFont(new Font("Tahoma", Font.PLAIN, 19));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(250,250,250));
		panel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		panel.setBounds(0, 0, 1536, 27);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel close = new JLabel("X");
		close.setBounds(1500, 0, 26, 27);
		panel.add(close);
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				parent.setVisible(true);
				dataLoaded=false; flag1=true; flag2=true;
				frame.dispose();
			}
		});
		close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		close.setFont(new Font("Mongolian Baiti", Font.BOLD, 16));
		close.setBackground(new Color(255, 255, 255));
		close.setForeground(new Color(255, 0, 0));
		close.setHorizontalAlignment(SwingConstants.CENTER);
		close.setToolTipText("Close\r\n");
		
		JLabel minimize = new JLabel("-");
		minimize.setBounds(1464, 0, 26, 27);
		panel.add(minimize);
		minimize.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		minimize.setPreferredSize(new Dimension(8, 16));
		minimize.setToolTipText("Minimize");
		minimize.setHorizontalAlignment(SwingConstants.CENTER);
		minimize.setForeground(new Color(0, 0, 0));
		minimize.setFont(new Font("Gill Sans Ultra Bold", Font.BOLD, 16));
		minimize.setBackground(Color.WHITE);
		minimize.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setState(Frame.ICONIFIED);
			}
		});
		
		JLabel lblModifiedDesAlgorithm = new JLabel("   Modified DES Algorithm");
		lblModifiedDesAlgorithm.setFont(new Font("Consolas", Font.PLAIN, 15));
		lblModifiedDesAlgorithm.setBackground(new Color(255, 228, 181));
		lblModifiedDesAlgorithm.setVerticalAlignment(SwingConstants.BOTTOM);
		lblModifiedDesAlgorithm.setBounds(0, 0, 480, 26);
		panel.add(lblModifiedDesAlgorithm);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(762, 94, 2, 614);
		contentPane.add(separator);
		
		JLabel label1 = new JLabel("Original DES Algorithm");
		label1.setForeground(Color.RED);
		label1.setHorizontalAlignment(SwingConstants.CENTER);
		label1.setFont(new Font("High Tower Text", Font.BOLD, 45));
		label1.setBackground(new Color(255, 228, 181));
		label1.setBounds(0, 45, 752, 65);
		contentPane.add(label1);
		
		JLabel label2 = new JLabel("   Modified DES Algorithm");
		label2.setForeground(Color.BLUE);
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setFont(new Font("High Tower Text", Font.BOLD, 45));
		label2.setBackground(new Color(255, 228, 181));
		label2.setBounds(771, 45, 755, 65);
		contentPane.add(label2);
		
		key1 = new JLabel("Key Used : ");
		key1.setVerticalAlignment(SwingConstants.BOTTOM);
		key1.setFont(new Font("Consolas", Font.BOLD, 16));
		key1.setBackground(new Color(255, 228, 181));
		key1.setBounds(10, 108, 480, 26);
		contentPane.add(key1);
		
		key2 = new JLabel("Key Used : ");
		key2.setVerticalAlignment(SwingConstants.BOTTOM);
		key2.setFont(new Font("Consolas", Font.BOLD, 16));
		key2.setBackground(new Color(255, 228, 181));
		key2.setBounds(791, 108, 480, 26);
		contentPane.add(key2);
		
		viewKeys1 = new JButton("View generated keys");
		viewKeys1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ShowKeys(16);
			}
		});
		viewKeys1.setForeground(new Color(255, 255, 255));
		viewKeys1.setFont(new Font("Century Gothic", Font.BOLD, 15));
		viewKeys1.setFocusTraversalPolicyProvider(true);
		viewKeys1.setFocusPainted(false);
		viewKeys1.setDefaultCapable(false);
		viewKeys1.setBorder(null);
		viewKeys1.setBackground(new Color(0, 100, 0));
		viewKeys1.setBounds(511, 108, 217, 27);
		contentPane.add(viewKeys1);
		
		viewKeys2 = new JButton("View generated keys");
		viewKeys2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ShowKeys(5);
			}
		});
		viewKeys2.setForeground(new Color(255, 255, 255));
		viewKeys2.setFont(new Font("Century Gothic", Font.BOLD, 15));
		viewKeys2.setFocusTraversalPolicyProvider(true);
		viewKeys2.setFocusPainted(false);
		viewKeys2.setDefaultCapable(false);
		viewKeys2.setBorder(null);
		viewKeys2.setBackground(new Color(0, 100, 0));
		viewKeys2.setBounds(1309, 107, 217, 27);
		contentPane.add(viewKeys2);
		
		pTitle1 = new JLabel("Plain Text : ");
		pTitle1.setVerticalAlignment(SwingConstants.BOTTOM);
		pTitle1.setFont(new Font("Consolas", Font.BOLD, 16));
		pTitle1.setBackground(new Color(255, 228, 181));
		pTitle1.setBounds(10, 157, 179, 26);
		contentPane.add(pTitle1);
		
		plainRadio1 = new JRadioButton("Plain Text");
		plainRadio1.setSelected(true);
		plainRadio1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				pTitle1.setText("Plain Text : ");
				Utility.loadTextBox(plainText, input1, null); 
			}
		});
		plainRadio1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		plainRadio1.setOpaque(false);
		plainRadio1.setFocusPainted(false);
		plainRadio1.setBounds(194, 161, 96, 21);
		frame.getContentPane().add(plainRadio1);
		
		modifiedRadio1 = new JRadioButton("Modified Text");
		modifiedRadio1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				pTitle1.setText("Modified Text : ");
				Utility.loadTextBox(convertedText1, input1, null); 
			}
		});
		modifiedRadio1.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		modifiedRadio1.setOpaque(false);
		modifiedRadio1.setFocusPainted(false);
		modifiedRadio1.setBounds(292, 161, 110, 21);
		frame.getContentPane().add(modifiedRadio1);
		
		bg1 = new ButtonGroup();
		bg1.add(plainRadio1);
		bg1.add(modifiedRadio1);
		
		pTitle2 = new JLabel("Plain Text : ");
		pTitle2.setVerticalAlignment(SwingConstants.BOTTOM);
		pTitle2.setFont(new Font("Consolas", Font.BOLD, 16));
		pTitle2.setBackground(new Color(255, 228, 181));
		pTitle2.setBounds(791, 157, 179, 26);
		contentPane.add(pTitle2);
		
		plainRadio2 = new JRadioButton("Plain Text");
		plainRadio2.setSelected(true);
		plainRadio2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				pTitle2.setText("Plain Text : ");
				Utility.loadTextBox(plainText, input2, null); 
			}
		});
		plainRadio2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		plainRadio2.setOpaque(false);
		plainRadio2.setFocusPainted(false);
		plainRadio2.setBounds(976, 161, 96, 21);
		frame.getContentPane().add(plainRadio2);
		
		modifiedRadio2 = new JRadioButton("Modified Text");
		modifiedRadio2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				pTitle2.setText("Modified Text : ");
				Utility.loadTextBox(convertedText2, input2, null); 
			}
		});
		modifiedRadio2.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		modifiedRadio2.setOpaque(false);
		modifiedRadio2.setFocusPainted(false);
		modifiedRadio2.setBounds(1074, 161, 110, 21);
		frame.getContentPane().add(modifiedRadio2);
		
		bg2 = new ButtonGroup();
		bg2.add(plainRadio2);
		bg2.add(modifiedRadio2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 193, 710, 170);
		contentPane.add(scrollPane);
		
		input1 = new JTextArea();
		input1.setFont(new Font("Monospaced", Font.PLAIN, 15));
		scrollPane.setViewportView(input1);
		input1.setLineWrap(true);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(791, 193, 710, 170);
		contentPane.add(scrollPane_1);
		
		input2 = new JTextArea();
		input2.setFont(new Font("Monospaced", Font.PLAIN, 15));
		input2.setLineWrap(true);
		scrollPane_1.setViewportView(input2);
		
		cTitle1 = new JLabel("Cipher Text : ");
		cTitle1.setVerticalAlignment(SwingConstants.BOTTOM);
		cTitle1.setFont(new Font("Consolas", Font.BOLD, 16));
		cTitle1.setBackground(new Color(255, 228, 181));
		cTitle1.setBounds(10, 394, 179, 26);
		contentPane.add(cTitle1);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(21, 431, 708, 168);
		contentPane.add(scrollPane_2);
		
		result1 = new JTextArea();
		result1.setFont(new Font("Monospaced", Font.PLAIN, 15));
		scrollPane_2.setViewportView(result1);
		result1.setText((String) null);
		result1.setLineWrap(true);
		
		cTitle2 = new JLabel("Cipher Text : ");
		cTitle2.setVerticalAlignment(SwingConstants.BOTTOM);
		cTitle2.setFont(new Font("Consolas", Font.BOLD, 16));
		cTitle2.setBackground(new Color(255, 228, 181));
		cTitle2.setBounds(774, 395, 179, 26);
		contentPane.add(cTitle2);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(786, 433, 706, 166);
		contentPane.add(scrollPane_3);
		
		result2 = new JTextArea();
		result2.setFont(new Font("Monospaced", Font.PLAIN, 15));
		scrollPane_3.setViewportView(result2);
		result2.setText((String) null);
		result2.setLineWrap(true);
		
		JButton btnPerformDecryption = new JButton("Perform Decryption");
		btnPerformDecryption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performTask1(e);
			}
		});
		btnPerformDecryption.setForeground(Color.WHITE);
		btnPerformDecryption.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnPerformDecryption.setFocusTraversalPolicyProvider(true);
		btnPerformDecryption.setFocusPainted(false);
		btnPerformDecryption.setDefaultCapable(false);
		btnPerformDecryption.setBorder(null);
		btnPerformDecryption.setBackground(new Color(255, 0, 0));
		btnPerformDecryption.setBounds(273, 619, 217, 27);
		contentPane.add(btnPerformDecryption);
		
		JButton button = new JButton("Perform Decryption");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performTask2(e);
			}
		});
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Century Gothic", Font.BOLD, 15));
		button.setFocusTraversalPolicyProvider(true);
		button.setFocusPainted(false);
		button.setDefaultCapable(false);
		button.setBorder(null);
		button.setBackground(new Color(0, 0, 205));
		button.setBounds(1054, 619, 217, 27);
		contentPane.add(button);
		
		JButton btnAnalysis = new JButton("Frequency Analysis");
		btnAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawFrequencyDistributionGraph();
			}
		});
		btnAnalysis.setForeground(Color.WHITE);
		btnAnalysis.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnAnalysis.setFocusTraversalPolicyProvider(true);
		btnAnalysis.setFocusPainted(false);
		btnAnalysis.setDefaultCapable(false);
		btnAnalysis.setBorder(null);
		btnAnalysis.setBackground(new Color(0, 128, 0));
		btnAnalysis.setBounds(656, 769, 217, 38);
		contentPane.add(btnAnalysis);
		
		JButton btnExecutionTime = new JButton("Execution Time");
		btnExecutionTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawTimeGraph();
			}
		});
		btnExecutionTime.setForeground(Color.WHITE);
		btnExecutionTime.setFont(new Font("Century Gothic", Font.BOLD, 15));
		btnExecutionTime.setFocusTraversalPolicyProvider(true);
		btnExecutionTime.setFocusPainted(false);
		btnExecutionTime.setDefaultCapable(false);
		btnExecutionTime.setBorder(null);
		btnExecutionTime.setBackground(new Color(218, 112, 214));
		btnExecutionTime.setBounds(656, 721, 217, 38);
		contentPane.add(btnExecutionTime);
		
		entropy1 = new JButton("Calculate Entropy");
		entropy1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculateEntropy(0);
			}
		});
		entropy1.setForeground(Color.WHITE);
		entropy1.setFont(new Font("Century Gothic", Font.BOLD, 15));
		entropy1.setFocusTraversalPolicyProvider(true);
		entropy1.setFocusPainted(false);
		entropy1.setDefaultCapable(false);
		entropy1.setBorder(null);
		entropy1.setBackground(Color.ORANGE);
		entropy1.setBounds(429, 753, 217, 27);
		contentPane.add(entropy1);
		
		entropy2 = new JButton("Calculate Entropy");
		entropy2.setForeground(Color.WHITE);
		entropy2.setFont(new Font("Century Gothic", Font.BOLD, 15));
		entropy2.setFocusTraversalPolicyProvider(true);
		entropy2.setFocusPainted(false);
		entropy2.setDefaultCapable(false);
		entropy2.setBorder(null);
		entropy2.setBackground(Color.ORANGE);
		entropy2.setBounds(883, 753, 217, 27);
		contentPane.add(entropy2);
		entropy2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculateEntropy(1);
			}
		});
	}
	
	public void loadData(JProgressBar p, SwingWorker<Void,Void> worker) {
		key1.setText(key1.getText()+getKey()); 
		key2.setText(key2.getText()+getKey()); 
		Utility.loadTextBox(plainText, input1, p);  if(worker.isCancelled()) return;
		Utility.loadTextBox(plainText, input2, p);  if(worker.isCancelled()) return;
		Utility.loadTextBox(cipherText1, result1, p);  if(worker.isCancelled()) return;
		Utility.loadTextBox(cipherText2, result2, p );
	}
	
	private void performTask1(ActionEvent ae) {
		if(ae.getActionCommand()=="Perform Decryption") {  //Perform decryption
			worker = new SwingWorker<Void,Void>() {
				@Override
				protected Void doInBackground()  {
					try {	
						startTime = System.nanoTime();
							dialog.t.setText("Original DES : Decrypting the data");
							recoveredText1=alg1.decrypt(cipherText1, new File("generated/Original DES/recoveredText.txt"), dialog.p, worker);   //Generate cipher text
							dialog.p.setValue(100); 
						dt1 = System.nanoTime() - startTime;
						if(worker.isCancelled())  return null;
						else ((JButton)ae.getSource()).setText("Recover Original Text");
						Utility.loadTextBox(recoveredText1, result1, dialog.p);
					} 
					catch(Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				
				@Override
				protected void done() {
			        if(dialog!=null && !dialog.closed) 
			        	dialog.dispose();
				}
			};
			dialog = new ProgressDialog(frame, worker);
			dialog.setVisible(true);
			worker.execute();
		}
		else {  //Load
			if(flag1) {
				JOptionPane.showMessageDialog(this, "This process is unreversible!\nRecovering the text will give the original sent message as output. Before sending the text will be"
						+ "modified into some format. Recovering will extract original text from this modified text.","Warning - Unreversible operation",JOptionPane.WARNING_MESSAGE);
				flag1=false;
			}
			else {
				worker = new SwingWorker<Void,Void>() {
					@Override
					protected Void doInBackground()  {
						try {	
							startTime = System.nanoTime();
								dialog.t.setText("Original DES : Recovering original message from decrypted text");
								Utility.recoverText(recoveredText1);   //Recover orignal text from modified one
								dialog.p.setValue(100); 
							rt1 = System.nanoTime() - startTime;
							if(worker.isCancelled())  return null;
							else ((JButton)ae.getSource()).setVisible(false);
							Utility.loadTextBox(recoveredText1, result1, dialog.p);
						} 
						catch(Exception e) {
							e.printStackTrace();
						}
						return null;
					}
					
					@Override
					protected void done() {
				        if(dialog!=null && !dialog.closed) 
				        	dialog.dispose();
					}
				};
				dialog = new ProgressDialog(frame, worker);
				dialog.setVisible(true);
				worker.execute();
			}
		}
	}
	
	private void performTask2(ActionEvent ae) {
		if(ae.getActionCommand()=="Perform Decryption") {  //Perform decryption
			worker = new SwingWorker<Void,Void>() {
				@Override
				protected Void doInBackground()  {
					try {	
						startTime = System.nanoTime();
							dialog.t.setText("Modified DES : Decrypting the data");
							recoveredText2=alg2.decrypt(cipherText2, new File("generated/Modified DES/recoveredText.txt"), dialog.p, worker);   //Generate cipher text
							dialog.p.setValue(100); 
						dt2 = System.nanoTime() - startTime;
						if(worker.isCancelled())  return null;
						else ((JButton)ae.getSource()).setText("Recover Original Text");
						Utility.loadTextBox(recoveredText2, result2, dialog.p);
					} 
					catch(Exception e) {
						e.printStackTrace();
					}
					return null;
				}
				
				@Override
				protected void done() {
			        if(dialog!=null && !dialog.closed) 
			        	dialog.dispose();
				}
			};
			dialog = new ProgressDialog(frame, worker);
			dialog.setVisible(true);
			worker.execute();
		}
		else {  //Load
			if(flag2) {
				JOptionPane.showMessageDialog(this, "This process is unreversible!\nRecovering the text will give the original sent message as output. Before sending the text will be"
						+ "modified into some format. Recovering will extract original text from this modified text.","Warning - Unreversible operation",JOptionPane.WARNING_MESSAGE);
				flag2=false;
			}
			else {
				worker = new SwingWorker<Void,Void>() {
					@Override
					protected Void doInBackground()  {
						try {	
							startTime = System.nanoTime();
								dialog.t.setText("Original DES : Recovering original message from decrypted text");
								Utility.recoverText(recoveredText2);   //Recover orignal text from modified one
								dialog.p.setValue(100); 
							rt2 = System.nanoTime() - startTime;
							if(worker.isCancelled())  return null;
							else ((JButton)ae.getSource()).setVisible(false);
							Utility.loadTextBox(recoveredText2, result2, dialog.p);
						} 
						catch(Exception e) {
							e.printStackTrace();
						}
						return null;
					}
					
					@Override
					protected void done() {
				        if(dialog!=null && !dialog.closed) 
				        	dialog.dispose();
					}
				};
				dialog = new ProgressDialog(frame, worker);
				dialog.setVisible(true);
				worker.execute();
			}

		}
	}
	
	private void drawFrequencyDistributionGraph() {
		worker = new SwingWorker<Void,Void>() {
			@Override
			protected Void doInBackground()  {
				try {	
					if(!dataLoaded) {
						odes=new LinkedHashMap<String,Integer>();
						mdes=new LinkedHashMap<String,Integer>();
						
						for(int i=0; i<64; i++) {
							odes.put(String.valueOf(Utility.value.get(i)),0);
							mdes.put(String.valueOf(Utility.value.get(i)),0);
						}
						odes.put("[\\2]",0);	mdes.put("[\\2]",0);	odes.put("[\\4]",0);	mdes.put("[\\4]",0);
						
						String ch;
						Reader reader = null;
				        char buffer[] = new char[6];
				        
				        reader = new FileReader(cipherText1.getAbsolutePath());
				        while(reader.read(buffer) != -1) {
				    		String text=String.valueOf(buffer);  //Take 64 bit data
				        	ch=String.valueOf(Utility.value.get(Integer.parseUnsignedInt(text, 2))); 
				        	odes.replace(ch, odes.get(ch)+1);
				        	odesCount++;
				        }
				        reader.close();
				        reader = new FileReader(cipherText2.getAbsolutePath());
				        while(reader.read(buffer) != -1) {
				        	String text=String.valueOf(buffer);  //Take 64 bit data
				        	ch=String.valueOf(Utility.value.get(Integer.parseUnsignedInt(text, 2)));
				        	mdes.replace(ch, mdes.get(ch)+1);
				        	mdesCount++;
				        }
				        reader.close();
				        dataLoaded = true;    
					}
					if(worker.isCancelled())  return null;
					
					DefaultCategoryDataset dcd = new DefaultCategoryDataset();
					
					for(Map.Entry<String, Integer> m : odes.entrySet()) {
						String character = m.getKey();
						int count = m.getValue();
						if(character.equals("[\\2]")) character = "E2";
						else if(character.equals("[\\4]")) character = "E4";
						else if(character.equals(" ")) character = "SP";
						dcd.setValue(count, "Original DES", character);
					}
					
					for(Map.Entry<String, Integer> m : mdes.entrySet()) {
						String character = m.getKey();
						int count = m.getValue();
						if(character.equals("[\\2]")) character = "E2";
						else if(character.equals("[\\4]")) character = "E4";
						else if(character.equals(" ")) character = "SP";
						dcd.setValue(count, "Modified DES", character);
						
					}
					
					JFreeChart jchart = ChartFactory.createBarChart("Frequency Distribution", "Characters", "Occurences", dcd, PlotOrientation.VERTICAL, true, true, false);
					CategoryPlot plot = jchart.getCategoryPlot();
					plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
					
					CategoryAxis domainAxis = plot.getDomainAxis();
					domainAxis.setLowerMargin(0.0);
					domainAxis.setUpperMargin(0.0);
					domainAxis.setCategoryMargin(0f);
					domainAxis.setLabelFont(new Font("Calibri", Font.PLAIN, 10));
					domainAxis.setTickLabelFont(new Font("Calibri", Font.PLAIN, 8));
					plot.setDomainAxis(domainAxis);
					
					ValueAxis rangeAxis = plot.getRangeAxis();
					rangeAxis.setLowerMargin(0.0);
					rangeAxis.setLabelFont(new Font("Calibri", Font.PLAIN, 10));
					rangeAxis.setTickLabelFont(new Font("Calibri", Font.PLAIN, 8));
					plot.setRangeAxis(rangeAxis);
					
					BarRenderer renderer = (BarRenderer) plot.getRenderer();
					renderer.setDrawBarOutline(false);
					
					renderer.setItemMargin(-0.6);
					renderer.setItemLabelAnchorOffset(0);
					renderer.setMaxBarWidth(0.002);
					plot.setRenderer(renderer);
					
					plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
					
					plot.setRangeGridlinesVisible(true);
					plot.setDomainGridlinesVisible(false);
					
					if(worker.isCancelled())  return null;
					ChartPanel chartPane = new ChartPanel(jchart);
	
					showPlot = new ShowFrequencyPlot(frame,"Frequency Analysis",chartPane);
					
				} 
				catch(Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void done() {
		        if(dialog!=null && !dialog.closed) {
		        	dialog.dispose();
		        	showPlot.setVisible(true);
		        }
			}
		};
		dialog = new ProgressDialog(frame, worker);
		dialog.setVisible(true);
		worker.execute();

	}	
	
	private void drawTimeGraph() {
		worker = new SwingWorker<Void,Void>() {
			@Override
			protected Void doInBackground()  {
				try {	
					DefaultCategoryDataset dcd = new DefaultCategoryDataset();
					
					dcd.setValue(kg1/1000000, "Original DES", "Key Generation");
					dcd.setValue(mt1/1000000, "Original DES", "Modify Text");
					dcd.setValue(et1/1000000, "Original DES", "Encyption");
					dcd.setValue(dt1/1000000, "Original DES", "Decryption");
					dcd.setValue(rt1/1000000, "Original DES", "Recover Text");
					
					dcd.setValue(kg2/1000000, "Modified DES", "Key Generation");
					dcd.setValue(mt2/1000000, "Modified DES", "Modify Text");
					dcd.setValue(et2/1000000, "Modified DES", "Encyption");
					dcd.setValue(dt2/1000000, "Modified DES", "Decryption");
					dcd.setValue(rt2/1000000, "Modified DES", "Recover Text");
					
					JFreeChart jchart = ChartFactory.createBarChart("Execution Time", "Process", "Execution time (ms)", dcd, PlotOrientation.VERTICAL, true, true, false);
					CategoryPlot plot = jchart.getCategoryPlot();
					plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
					
					
					
					CategoryAxis domainAxis = plot.getDomainAxis();
					domainAxis.setCategoryMargin(0.1f);
					domainAxis.setLabelFont(new Font("Calibri", Font.PLAIN, 14));
					domainAxis.setTickLabelFont(new Font("Calibri", Font.PLAIN, 12));
					plot.setDomainAxis(domainAxis);
					
					ValueAxis rangeAxis = plot.getRangeAxis();
					rangeAxis.setLowerMargin(0.0);
					rangeAxis.setLabelFont(new Font("Calibri", Font.PLAIN, 14));
					rangeAxis.setTickLabelFont(new Font("Calibri", Font.PLAIN, 12));
					plot.setRangeAxis(rangeAxis);
					
					BarRenderer renderer = (BarRenderer) plot.getRenderer();
					renderer.setDrawBarOutline(false);
					
					renderer.setItemMargin(0);
					renderer.setItemLabelAnchorOffset(0);
					renderer.setMaxBarWidth(0.09);
					plot.setRenderer(renderer);
					
					plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
					
					plot.setRangeGridlinesVisible(true);
					plot.setDomainGridlinesVisible(false);
					
					if(worker.isCancelled())  return null;
					ChartPanel chartPane = new ChartPanel(jchart);
					chartPane.addChartMouseListener(new ChartMouseListener() {
						public void chartMouseClicked(ChartMouseEvent e) {
							new ShowTime(null);
						}

						@Override
						public void chartMouseMoved(ChartMouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}
					});

					showPlot = new ShowFrequencyPlot(frame,"Execution Time Analysis",chartPane);
					
				} 
				catch(Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void done() {
		        if(dialog!=null && !dialog.closed) {
		        	dialog.dispose();
		        	showPlot.setVisible(true);
		        }
			}
		};
		dialog = new ProgressDialog(frame, worker);
		dialog.setVisible(true);
		worker.execute();

	}	
	
	private void calculateEntropy(int i) {
		if(!dataLoaded) {
			JOptionPane.showMessageDialog(null, "Please perform frequency analysis then try again.","Messege",JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		double entropy=0.0;
		if(i==0) {
			for(Map.Entry<String, Integer> map : odes.entrySet()) {
				int m = map.getValue();
				if(m!=0) {
					double n = (double)m/odesCount;
					entropy+= (-1)*n*(Math.log10(n)/Math.log10(2));
				}
			}
			JOptionPane.showMessageDialog(null, "Entropy of cipher text : "+ String.format("%.2f", entropy),"Original DES",JOptionPane.INFORMATION_MESSAGE);
		}
		else if(i==1) {
			for(Map.Entry<String, Integer> map : mdes.entrySet()) {
				int m = map.getValue();
				if(m!=0) {
					double n = (double)m/mdesCount;
					entropy+= (-1)*n*(Math.log10(n)/Math.log10(2));
				}
			}
			JOptionPane.showMessageDialog(null, "Entropy of cipher text : "+ String.format("%.2f", entropy),"Modified DES",JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
