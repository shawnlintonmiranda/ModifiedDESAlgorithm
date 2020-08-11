package dialogs;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import algorithm.*;
import utilities.Utility;

public class ShowKeys extends JDialog {
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	public ShowKeys(int keys) {
		getContentPane().setBackground(new Color(255, 255, 255));
		setTitle("Generated Keys");
		setBounds(100, 100, 300, keys*30);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new GridLayout(keys,1));
		contentPanel.setLayout(new FlowLayout());
		setVisible(true);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		String[] key=null;
		if(keys==16) key = DES.getKeys();
		else if(keys==5) key = ModifiedDES.getKeys();
		
		JLabel[] label = new JLabel[keys];
		for(int i=0; i<keys; i++) {
			label[i] = new JLabel("Key Used : ");
			label[i].setVerticalAlignment(SwingConstants.BOTTOM);
			label[i].setFont(new Font("Segoe UI Semibold", Font.PLAIN, 15));
			label[i].setBackground(new Color(255, 228, 181));
			label[i].setHorizontalAlignment(SwingConstants.CENTER);
			label[i].setText("Key-"+ (i+1)+" : "+Utility.toString(key[i]));
			getContentPane().add(label[i]);
		}

	}

}
