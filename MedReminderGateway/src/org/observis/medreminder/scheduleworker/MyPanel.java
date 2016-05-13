package org.observis.medreminder.scheduleworker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MyPanel extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6766331239529509003L;
	private Thread checker;
	private JTextField textField;
	private JTextField textField_1;
	private JPasswordField passwordField;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	JLabel lblNewLabel_3;
	JButton btnNewButton;
	JButton btnNewButton_1;
	SwingWorker<Void,Void> worker;
	
	private boolean isInterruptable = false;

	public static void main(String... args) {
				MyPanel startPoint = new MyPanel();
				startPoint.setVisible(true);
	}

	public MyPanel() {// 540 260
		setBounds(100, 100, 540, 260);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MedReminder Scheduler");
		getContentPane().setLayout(null);

		JLabel lblMysqlServer = new JLabel("MySQL server:");
		lblMysqlServer.setBounds(10, 11, 91, 14);
		getContentPane().add(lblMysqlServer);

		JLabel lblNewLabel = new JLabel("Login:");
		lblNewLabel.setBounds(10, 36, 66, 14);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setBounds(10, 61, 66, 14);
		getContentPane().add(lblNewLabel_1);

		JLabel lblApkey = new JLabel("API_SID:");
		lblApkey.setBounds(10, 86, 66, 14);
		getContentPane().add(lblApkey);

		JLabel lblNewLabel_2 = new JLabel("API_TOKEN:");
		lblNewLabel_2.setBounds(10, 111, 76, 14);
		getContentPane().add(lblNewLabel_2);

		textField = new JTextField();
		textField.setBounds(111, 8, 228, 20);
		getContentPane().add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setBounds(111, 33, 110, 20);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(111, 58, 110, 20);
		getContentPane().add(passwordField);

		textField_2 = new JTextField();
		textField_2.setBounds(111, 83, 228, 20);
		getContentPane().add(textField_2);
		textField_2.setColumns(10);

		textField_3 = new JTextField();
		textField_3.setBounds(111, 108, 228, 20);
		getContentPane().add(textField_3);
		textField_3.setColumns(10);

		btnNewButton = new JButton("STOP");
		btnNewButton.setBounds(10, 189, 133, 32);
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopThread();
			}
		});
		getContentPane().add(btnNewButton);

		btnNewButton_1 = new JButton("START");

		btnNewButton_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				startThread();
			}
		});
		btnNewButton_1.setBounds(391, 189, 133, 32);
		getContentPane().add(btnNewButton_1);

		lblNewLabel_3 = new JLabel("Success");
		lblNewLabel_3.setBounds(153, 198, 228, 14);
		getContentPane().add(lblNewLabel_3);

		JLabel lblPhone = new JLabel("Phone:");
		lblPhone.setBounds(10, 136, 46, 14);
		getContentPane().add(lblPhone);

		textField_4 = new JTextField();
		textField_4.setBounds(111, 133, 166, 20);
		getContentPane().add(textField_4);
		textField_4.setColumns(10);
		readIni();
	}

	void startThread() {
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		btnNewButton_1.setEnabled(false);
		btnNewButton.setEnabled(true);
		DatabaseConnector.setConnectionValues(textField.getText(),
				textField_1.getText(), new String(passwordField.getPassword()));
		MedReminderWorker.setGatewayAuth(textField_2.getText(),
				textField_3.getText(), textField_4.getText());

		lblNewLabel_3.setText("Thread running.");

		textField.setEditable(false);
		textField_1.setEditable(false);
		textField_2.setEditable(false);
		textField_3.setEditable(false);
		textField_4.setEditable(false);
		passwordField.setEditable(false);

		writeIni();
		worker =  new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				try {
					while (true) {
						isInterruptable = false;
						DatabaseConnector.openConnection();
						DatabaseConnector.timeCheck();
						DatabaseConnector.closeConnection();
						isInterruptable = true;
						Thread.sleep(15000);
					}
				} catch(InterruptedException e){
					System.out.println("Interrupting sleep interval.");
				} catch (Exception e) {
					e.printStackTrace();
					// checker.interrupt();
				}				
				return null;
			}
			
		};
		worker.execute();
	}

	void stopThread() {
		if (isInterruptable) {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			worker.cancel(true);
			textField.setEditable(true);
			textField_1.setEditable(true);
			textField_2.setEditable(true);
			textField_3.setEditable(true);
			textField_4.setEditable(true);
			passwordField.setEditable(true);
			lblNewLabel_3.setText("Thread stopped.");
			btnNewButton_1.setEnabled(true);
			btnNewButton.setEnabled(false);
		}
	}

	void readIni() {
		try {
			BufferedReader ini = new BufferedReader(new FileReader("saved.ini"));
			// read = ini.readLine();
			String a, b, c, d,e;
			a = ini.readLine();
			b = ini.readLine();
			c = ini.readLine();
			d = ini.readLine();
			e = ini.readLine();
			if (a != null && b != null && c != null && d != null && e!=null) {
				textField.setText(a);
				textField_1.setText(b);
				textField_2.setText(c);
				textField_3.setText(d);
				textField_4.setText(e);
			}
			ini.close();
		} catch (Exception e) {

		}
	}

	void writeIni() {
		try {
			BufferedWriter outi = new BufferedWriter(
					new FileWriter("saved.ini"));
			String a = textField.getText();
			String b = textField_1.getText();
			String c = textField_2.getText();
			String d = textField_3.getText();
			String e = textField_4.getText();
			outi.write(a + "\n" + b + "\n" + c + "\n" + d+"\n"+e);
			outi.flush();
			outi.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

}
