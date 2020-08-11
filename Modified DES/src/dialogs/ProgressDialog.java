package dialogs;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

public class ProgressDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private SwingWorker<Void,Void> worker = null;
	public JProgressBar p;
	public JLabel t;
	public boolean closed = false;
	
	public ProgressDialog(JFrame parent, SwingWorker<Void,Void> w) {
		super(parent,"Task Progress");
		worker = w;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closed = true;
				try { worker.cancel(false); } catch(NullPointerException exc) {}
			}
		});
		getContentPane().setBackground(new Color(255, 255, 204));
		setSize(300,120);
		getContentPane().setLayout(new GridLayout(3,1));
		setLocationRelativeTo(null);
		
		t = new JLabel("");
		t.setFont(new Font("Tahoma", Font.PLAIN, 12));
		t.setBorder(new EmptyBorder(8, 6, 2, 3));
		getContentPane().add(t);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setOpaque(false);
		getContentPane().add(panel);
		panel.setLayout(new GridLayout(1, 1, 0, 0));
		
		p = new JProgressBar(1,100);
		p.setBorder(null);
		p.setEnabled(false);
		p.setAlignmentY(Component.TOP_ALIGNMENT);
		p.setAlignmentX(Component.LEFT_ALIGNMENT);
		p.setBounds(10, 10, 266, 18);
		p.setStringPainted(true);
		panel.add(p);
		p.setVisible(true);
		p.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		p.setForeground(new Color(34, 139, 34));
		p.setBackground(new Color(255, 255, 255));
		
		JLabel lblNewLabel = new JLabel("Please wait ...");
		lblNewLabel.setBorder(new EmptyBorder(3, 0, 3, 0));
		lblNewLabel.setFont(new Font("Monotype Corsiva", Font.PLAIN, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblNewLabel);
	}

}
