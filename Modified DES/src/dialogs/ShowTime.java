package dialogs;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import static main.Cryptography.*;

public class ShowTime extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel t1,t2,t3,t4;

	public ShowTime(JDialog parent) {
		super(parent,"Execution Time");
		getContentPane().setBackground(Color.WHITE);
		setSize(800,277);
		setModal(true);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setForeground(Color.GRAY);
		separator.setBackground(Color.LIGHT_GRAY);
		separator.setBounds(389, 25, 1, 328);
		getContentPane().add(separator);
		
		JLabel label = new JLabel("Original DES Algorithm");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.RED);
		label.setFont(new Font("High Tower Text", Font.BOLD, 25));
		label.setBackground(new Color(255, 228, 181));
		label.setBounds(10, 19, 369, 43);
		getContentPane().add(label);
		
		JLabel label_1 = new JLabel("   Modified DES Algorithm");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setForeground(Color.BLUE);
		label_1.setFont(new Font("High Tower Text", Font.BOLD, 25));
		label_1.setBackground(new Color(255, 228, 181));
		label_1.setBounds(400, 23, 376, 39);
		getContentPane().add(label_1);
		
		t1 = new JLabel("");
		t1.setHorizontalTextPosition(SwingConstants.LEADING);
		t1.setVerticalAlignment(SwingConstants.TOP);
		t1.setFont(new Font("Consolas", Font.PLAIN, 16));
		t1.setBackground(new Color(255, 228, 181));
		t1.setBounds(10, 72, 369, 116);
		getContentPane().add(t1);
		
		t2 = new JLabel("");
		t2.setVerticalAlignment(SwingConstants.TOP);
		t2.setHorizontalTextPosition(SwingConstants.LEADING);
		t2.setFont(new Font("Consolas", Font.PLAIN, 16));
		t2.setBackground(new Color(255, 228, 181));
		t2.setBounds(400, 72, 376, 116);
		getContentPane().add(t2);
		
		t3 = new JLabel("");
		t3.setVerticalAlignment(SwingConstants.BOTTOM);
		t3.setFont(new Font("Consolas", Font.BOLD, 18));
		t3.setBackground(new Color(255, 228, 181));
		t3.setBounds(10, 198, 369, 26);
		getContentPane().add(t3);
		
		t4 = new JLabel("");
		t4.setVerticalAlignment(SwingConstants.BOTTOM);
		t4.setFont(new Font("Consolas", Font.BOLD, 18));
		t4.setBackground(new Color(255, 228, 181));
		t4.setBounds(407, 198, 369, 26);
		getContentPane().add(t4);
		setLocationRelativeTo(null);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		showExecutionTime();
		setVisible(true);
	}
	
	private static String printTime(long nanos) {
		int hours,minutes,seconds;
		StringBuffer sb = new StringBuffer();
		seconds = (int)(nanos/1000000000);
		minutes = seconds/60;
		seconds = seconds % 60;
		hours = minutes/60;
		minutes = minutes % 60;
		if(hours>0) sb.append(hours).append("h ");
		if(minutes>0) sb.append(minutes).append("m ");
		if(seconds>0) sb.append(seconds).append("s ");
		if(nanos>=1000000)
			sb.append(nanos/1000000).append("ms");
		else if(nanos>=1000)
			sb.append(nanos/1000).append("us");
		else 
			sb.append(nanos).append("ns");
		return sb.toString();
		
	}
	
	public void showExecutionTime() {
		 String s;
		 s= "<html>Key Generation : "+printTime(kg1)+"<br/>Modify Text : "+printTime(mt1)+"<br/>Encrypt Text : "+printTime(et1)+"<br/>Decrypt Text : "+printTime(dt1)+"<br/>Recover Text : "+printTime(rt1)+"</html>";
		 t1.setText(s);
		 s= "<html>Key Generation : "+printTime(kg2)+"<br/>Modify Text : "+printTime(mt2)+"<br/>Encrypt Text : "+printTime(et2)+"<br/>Decrypt Text : "+printTime(dt2)+"<br/>Recover Text : "+printTime(rt2)+"</html>";
		 t2.setText(s);
		 t3.setText("Total time : "+printTime(kg1+mt1+et1+dt1+rt1));
		 t4.setText("Total time : "+printTime(kg2+mt2+et2+dt2+rt2));
	}
	

}
