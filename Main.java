package atm;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
	static boolean on;
	static boolean success;
	static boolean exit = false;
	static int ac_no;
	static boolean valid;
	static int pin;
	static Customer current;
	static Customer Ac_Transfer;
	static boolean s = false;
	static boolean o = false;
	static boolean w = false;
	static boolean d = false;
	static boolean c = false;
	static boolean a = false;
	static boolean t = true;
	static boolean tt = true;
	static boolean ttt = true;
	static boolean tttt = true;
	static boolean ttttt = true;
	static boolean tttttt = true;
	static boolean proceed = true;

	static int[] dbac_no;
	static int[] dbpin;
	static float[] dbbalance;
	static int[] dbifsc;
	static String[] dbname;
	static int size;

	private static void getNumberOfUsers() {
		// lets do this...
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String sql  = "select count(Ac_no) from BankAccounts ";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			// this will give us the size
			size = rs.getInt(1);



		} catch(SQLException e) {
			System.out.println(e.toString());
		} finally {
			// close connection
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static void readAllData() {
		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM BankAccounts";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			int count = 0;

			while(rs.next()) {
				int Ac_no = rs.getInt("Ac_no");
				int pin = rs.getInt("pin");
				int balance  = rs.getInt("balance");
				int ifsc = rs.getInt("ifsc");
				String name = rs.getString("name");

				dbac_no[count]=Ac_no;
				dbpin[count] = pin;
				dbbalance[count] = balance;
				dbifsc[count] = ifsc;
				dbname[count] = name;

				count ++;




			}
		} catch(SQLException e) {
			System.out.println(e.toString());
		} finally {
			try {
				rs.close();
				ps.close();
				con.close();
			} catch(SQLException e) {
				System.out.println(e.toString());
			}
		}


	}

	private static void updateDetails(int sPin,int sBalance,int sAc_no) {

		Connection con = DbConnection.connect();
		PreparedStatement ps = null;
		try {
			String sql = "UPDATE BankAccounts set pin = ?,balance = ? WHERE Ac_no = ? ";
			ps = con.prepareStatement(sql);
			ps.setInt(1, sPin);
			ps.setInt(2, sBalance);
			ps.setInt(3, sAc_no);
			ps.execute();

		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}
	public static void main(String[] args) {

		getNumberOfUsers();

		dbac_no = new int[size];
		dbpin = new int[size];
		dbbalance = new float[size];
		dbifsc = new int[size];
		dbname = new String[size];

		readAllData();

		CashDispenser one = new CashDispenser(100000);// a cash dispenser object is created with the initial amount loaded in it
		ATM atm = new ATM(one);//an atm object is created with a cash dispenser added to it


		BankAccount[] account = new BankAccount[size];
		Customer[] customer = new Customer[size];
		Scanner sc = new Scanner(System.in);
		//various bank accounts are created
		int co = 0;
		while(co < size){

			account[co] = new BankAccount(dbac_no[co], dbpin[co], dbbalance[co],dbifsc[co]);
			customer[co] = new Customer(account[co], dbname[co]);//various customers are linked to bank accounts
			atm.customers.add(customer[co]);//the customers are added to the arraylist to the atm
			co++;
		}

		

		on = true;//while atm is on

		while(on) {

			exit = false;
			while(!exit) {
				exit = true;
				//success = false;
				//ac_no = 0;
				valid = true;
				//pin = 0;

				//login GUI
				class Login extends JFrame implements ActionListener {

					JLabel head;
					JLabel un, pwd;
					JTextField t1, t2;
					JButton b1, b2, b3;
					//Container c = getContentPane();
					Login() {
						Container c = getContentPane();
						head = new JLabel("----Login----");
						un = new JLabel("Account Number:");

						pwd = new JLabel("             Pin:");
						t1 = new JTextField(10);
						t2 = new JPasswordField(10);
						b1 = new JButton("Submit");
						b2 = new JButton("Reset");
						b3 = new JButton("OFF");



						c.setLayout(new FlowLayout(FlowLayout.CENTER));
						head.setFont(new Font("courier new", Font.BOLD, 30));
						c.add(head);
						c.add(un);
						c.add(t1);
						c.add(pwd);
						c.add(t2);
						c.add(b1);
						c.add(b2);
						c.add(b3);
						setSize(260, 210);
						setVisible(true);
						b1.addActionListener(this);
						b2.addActionListener(this);
						b3.addActionListener (new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								setVisible(false);
								System.exit(0);
							}
						});

					}
					// action performed for the action event
					public void actionPerformed(ActionEvent ae) {
						//if(t1.getText().equals("") && t2.getText().equals(""))
						String s1 = t1.getText();
						String s2 = t2.getText();
						ac_no = Integer.parseInt(s1);
						pin = Integer.parseInt(s2);
						success = false;
						String str = ae.getActionCommand();
						success = atm.LogIn(ac_no, pin, atm.customers);
						if (str.equals("Submit")) {
							if (success && s1.length() == 5 && s2.length() == 5) {
								setVisible(false);
								JOptionPane.showMessageDialog(null, "Login Successful:)");
								s = true;




							} else

								JOptionPane.showMessageDialog(null, "Invalid Login Details!!\n" + "Please enter valid 5 digit Account number and Pin");
						}
						if (str.equals("Reset")) {
							t1.setText("");
							t2.setText("");
						}


					}


				}

				Login lo = new Login();//login object is created and login window appears

				class Set extends JFrame implements ActionListener {
					JLabel n;
					JTextField t1,t2;
					JButton b1;
					Set(){
						Container c = getContentPane();
						n = new JLabel("Enter a number between 0 and 100");
						t1 = new JTextField(5);
						b1 = new JButton("Submit");
						c.setLayout(new FlowLayout(FlowLayout.CENTER));
						c.add(n);
						c.add(t1);
						c.add(b1);
						setSize(260, 210);
						setVisible(true);
						b1.addActionListener(this);


					}

					@Override
					public void actionPerformed(ActionEvent e) {
						String s1 = t1.getText();
						int n = Integer.parseInt(s1);
						String str = e.getActionCommand();
						if (str.equals("Submit")) {
							if(n > 0 && n < 100){
								setVisible(false);
								current = null;//current customer using the atm
								for (int i = 0; i < atm.customers.size(); i++) {
									Customer c = atm.customers.get(i);
									if (c.CustomerAccount.getAc_no() == ac_no && c.CustomerAccount.getPin() == pin)
										current = atm.customers.get(i);//current customer is fetched from the arraylist of customers

								}
								JOptionPane.showMessageDialog(null, "Welcome " + current.getCustomerName());
								//System.out.println("Welcome " + current.getCustomerName());//welcome message is shown with the customer name
                                o = true;
							}
							else{
								setVisible(false);
								JOptionPane.showMessageDialog(null, "Error!! Transaction cancelled");
								exit = true;
								tt = false;
								ttt = false;
								tttt = false;
								ttttt = false;
								tttttt = false;
								proceed = false;
							}
						}
					}
				}
                t = true;
				tt = true;
				ttt = true;
				tttt = true;
				ttttt = true;
				tttttt = true;
				proceed = true;

				while(t){
					System.out.println("");
					if (s == true) {
						Set se = new Set();
						t = false;
						s = false;
					}

				}


				class Options extends JFrame implements ActionListener {

					JLabel n;
					JButton b1,b2,b3,b4,b5,b6;
					Options(){
						Container c = getContentPane();
						n = new JLabel("Please select one of the following option");
						b1 = new JButton("1)Check my Balance");
						b2 = new JButton("2)Cash withdrawal");
						b3 = new JButton("3)Cash Deposit");
                        b4 = new JButton("4)Pin change");
						b5 = new JButton("5)Account Transfer");
						b6 = new JButton("6)Exit");
						c.setLayout(new FlowLayout(FlowLayout.CENTER));
						c.add(n);
						c.add(b1);
						c.add(b2);
						c.add(b3);
						c.add(b4);
						c.add(b5);
						c.add(b6);
						setSize(260, 210);
						setVisible(true);
						b1.addActionListener(this);
						b2.addActionListener(this);
						b3.addActionListener(this);
						b4.addActionListener(this);
						b5.addActionListener(this);
						b6.addActionListener(this);

					}
					@Override
					public void actionPerformed(ActionEvent e) {
						String str = e.getActionCommand();
						if (str.equals("1)Check my Balance")) {
							setVisible(false);
							JOptionPane.showMessageDialog(null,"Account Balance : " + current.CustomerAccount.getBalance() + "\n ---Thank You---");
							exit = true;
							ttt = false;
							tttt = false;
							ttttt = false;
							tttttt = false;
							proceed = false;
						}
						if (str.equals("2)Cash withdrawal")) {
							setVisible(false);
							tttt = false;
							ttttt = false;
							tttttt = false;
							w = true;
						}
						if (str.equals("3)Cash Deposit")) {
							setVisible(false);
							ttt = false;
							ttttt = false;
							tttttt = false;
							d = true;

						}
						if(str.equals("4)Pin change")){
							setVisible(false);
							ttt = false;
							tttt = false;
							tttttt = false;
							c = true;
						}
						if(str.equals("5)Account Transfer")){
							setVisible(false);
							ttt = false;
							tttt = false;
							ttttt = false;
							a = true;
						}
						if (str.equals("6)Exit")) {
							setVisible(false);
							ttt = false;
							tttt = false;
							ttttt = false;
							tttttt = false;
							exit = true;
							proceed = false;
						}

					}
				}


				while (tt) {
					System.out.println("");
					if (o == true) {
						Options op = new Options();
						tt = false;
						o = false;
					}
				}



				class CashWithdrawal extends JFrame implements ActionListener {
					JLabel n;
					JTextField t1;
					JButton b1, b2, b3;

					CashWithdrawal() {
						Container c = getContentPane();
						n = new JLabel("Enter the amount you want to withdraw in multiples of 100");
						t1 = new JTextField(5);
						b1 = new JButton("Withdraw");
						b2 = new JButton("Reset");
						b3 = new JButton("Exit");
						c.setLayout(new FlowLayout(FlowLayout.CENTER));
						c.add(n);
						c.add(t1);
						c.add(b1);
						c.add(b2);
						c.add(b3);
						setSize(340, 210);
						setVisible(true);
						b1.addActionListener(this);
						b2.addActionListener(this);
						b3.addActionListener(this);

					}

					@Override
					public void actionPerformed(ActionEvent e) {
						String s1 = t1.getText();
						int n = Integer.parseInt(s1);
						String str = e.getActionCommand();
						if (str.equals("Withdraw")) {
							if(n % 100 ==0){
								if (atm.dispenser.dispense(n)) {//checked whether there is sufficient amount in the dispenser
									if(current.Widhdraw(current.CustomerAccount, n)){//withdraw method is called
										setVisible(false);
										JOptionPane.showMessageDialog(null,"Please collect your money\n"+"Remaining Account Balance : " + (float) current.CustomerAccount.getBalance() + "\n ---Thank You---");
										exit = true;
										proceed = false;
									}
									else{
										JOptionPane.showMessageDialog(null,"Not enough balance in your account, Please choose a smaller amount!");
									}
								}
								else{
									JOptionPane.showMessageDialog(null,"Sorry!!! sufficient cash is not available in the ATM, please select a lower amount");
								}

							}
							else{
								JOptionPane.showMessageDialog(null,"Please enter the amount in multiples of 100");
							}
						}
						if (str.equals("Reset")) {
							t1.setText("");

						}
						if (str.equals("Exit")){
							setVisible(false);
							ttt = false;
							tttt = false;
							ttttt = false;
							tttttt = false;
							exit = true;
							proceed = false;
						}

					}
				}



				while (ttt) {
					System.out.println("");
					if (w == true) {
						CashWithdrawal cw = new CashWithdrawal();
						ttt = false;
						w = false;
					}
				}


				class CashDeposit extends JFrame implements ActionListener {
					JLabel n;
					JTextField t1, t2;
					JButton b1, b2, b3;

					CashDeposit() {
						Container c = getContentPane();
						n = new JLabel("Enter the amount you want to Deposit in multiples of 100");
						t1 = new JTextField(5);
						b1 = new JButton("Submit");
						b2 = new JButton("Reset");
						b3 = new JButton("Exit");
						c.setLayout(new FlowLayout(FlowLayout.CENTER));
						c.add(n);
						c.add(t1);
						c.add(b1);
						c.add(b2);
						c.add(b3);
						setSize(340, 210);
						setVisible(true);
						b1.addActionListener(this);
						b2.addActionListener(this);
						b3.addActionListener(this);

					}

					@Override
					public void actionPerformed(ActionEvent e) {
						String s1 = t1.getText();
						int n = Integer.parseInt(s1);
						String str = e.getActionCommand();
						if (str.equals("Submit")) {
							if(n % 100 ==0){
								setVisible(false);
								current.Deposit(current.CustomerAccount, n);//amount is deposited to the account
								JOptionPane.showMessageDialog(null,"Account Balance : " + (float) current.CustomerAccount.getBalance() + "\n ---Thank You---");
								exit = true;
								proceed = false;
							}
							else{
								JOptionPane.showMessageDialog(null,"Please enter the amount in multiples of 100");
						    }
					    }
						if (str.equals("Reset")) {
						t1.setText("");

						}

						if (str.equals("Exit")) {
							setVisible(false);
							ttt = false;
							tttt = false;
							ttttt = false;
							tttttt = false;
							exit = true;
							proceed = false;
						}
						}

					}


				while (tttt) {
					System.out.println("");
					if (d == true) {
						CashDeposit cd = new CashDeposit();
						tttt = false;
						d = false;
					}
				}


				class PinChange extends JFrame implements ActionListener{
					JLabel n,m,o;
					JTextField t1, t2, t3;
					JButton b1, b2, b3;

					PinChange() {
						Container c = getContentPane();
						n = new JLabel("Enter previous 5 digit Pin");
						t1 = new JPasswordField(7);
						m = new JLabel("Enter New 5 digit Pin");
						t2 =  new JPasswordField(7);
						o = new JLabel("Confirm Pin");
						t3 = new JPasswordField(7);
						b1 = new JButton("Submit");
						b2 = new JButton("Reset");
						b3 = new JButton("Exit");
						c.setLayout(new FlowLayout(FlowLayout.CENTER));
						c.add(n);
						c.add(t1);
						c.add(m);
						c.add(t2);
						c.add(o);
						c.add(t3);
						c.add(b1);
						c.add(b2);
						c.add(b3);
						setSize(260, 210);
						setVisible(true);
						b1.addActionListener(this);
						b2.addActionListener(this);
						b3.addActionListener(this);

					}
					@Override
					public void actionPerformed(ActionEvent e) {
						String s1 = t1.getText();
						String s2 = t2.getText();
						String s3 = t3.getText();
						int n = Integer.parseInt(s1);
						int j = Integer.parseInt(s2);
						int l = Integer.parseInt(s3);
						String str = e.getActionCommand();
						if(str.equals("Submit")){
							if(current.CustomerAccount.getPin() == n) {
								if (s2.length() == 5 && s3.length() == 5) {
									if(j == l){
										setVisible(false);
										current.CustomerAccount.setPin(j);
										JOptionPane.showMessageDialog(null,"Pin changed Successfully");
										exit = true;
										proceed = false;
									}
									else{
										JOptionPane.showMessageDialog(null,"Pin and Confirm Pin do not match");
									}
								}
								else{
									JOptionPane.showMessageDialog(null,"Please enter 5 digit pin only");
								}
							}
							else{
								JOptionPane.showMessageDialog(null,"Plese enter valid pin of your account");
							}

						}
						if (str.equals("Reset")) {
							t2.setText("");
							t3.setText("");

						}
						if (str.equals("Exit")) {
							setVisible(false);
							ttt = false;
							tttt = false;
							ttttt = false;
							tttttt = false;
							exit = true;
							proceed = false;
						}


					}
				}



				while (ttttt) {
					System.out.println("");
					if (c == true) {
						PinChange pc = new PinChange();
						ttttt = false;
						c = false;
					}
				}

				class AccountTransfer extends JFrame implements ActionListener{
					JLabel n,m,o;
					JTextField t1, t2,t3;
					JButton b1, b2, b3;

					AccountTransfer() {
						Container c = getContentPane();
						n = new JLabel("Enter Acount Number to transfer to");
						t1 = new JTextField(7);
						m = new JLabel("Enter IFSC code");
						t2 =  new JTextField(7);
						o = new JLabel("Enter Amount to transfer");
						t3 =  new JTextField(7);
						b1 = new JButton("Transfer");
						b2 = new JButton("Reset");
						b3 = new JButton("Exit");
						c.setLayout(new FlowLayout(FlowLayout.CENTER));
						c.add(n);
						c.add(t1);
						c.add(m);
						c.add(t2);
						c.add(o);
						c.add(t3);
						c.add(b1);
						c.add(b2);
						c.add(b3);
						setSize(300, 210);
						setVisible(true);
						b1.addActionListener(this);
						b2.addActionListener(this);
						b3.addActionListener(this);


					}
					@Override
					public void actionPerformed(ActionEvent e) {
						String s1 = t1.getText();
						String s2 = t2.getText();
						String s3 = t3.getText();
						t2.setText("*");
						t1.setText("*");
						t3.setText("*");
						int n = Integer.parseInt(s1);
						int j = Integer.parseInt(s2);
						float w = Float.parseFloat(s3);
						String str = e.getActionCommand();
						if(str.equals("Transfer")){
							if (s1.length() == 5 && s2.length() == 3) {
								Ac_Transfer = null;
								for (int i = 0; i < atm.customers.size(); i++) {
									Customer f = atm.customers.get(i);
									if (f.CustomerAccount.getAc_no() == n && f.CustomerAccount.getIfsc() == j)
										Ac_Transfer = atm.customers.get(i);

								}
								if(Ac_Transfer != null){
									if(current.CustomerAccount.getBalance() >= w){
										setVisible(false);
										float cb = (float)current.CustomerAccount.getBalance();
										current.CustomerAccount.setBalance(cb - w);
										float at = (float)Ac_Transfer.CustomerAccount.getBalance();
										Ac_Transfer.CustomerAccount.setBalance(at + w);
										JOptionPane.showMessageDialog(null,"Money transferred successfully");
										exit = true;
										proceed = false;
									}
									else{
										JOptionPane.showMessageDialog(null,"Not enough balance in your account to transfer,please choose a lower amount");
									}
								}
								else{
									JOptionPane.showMessageDialog(null,"Invalid Account details,please enter valid account details");
								}
							}
							else{
								JOptionPane.showMessageDialog(null,"Please enter 5 digit Account Number and 3 digit IFSC code only");
							}



						}
						if (str.equals("Reset")) {
							t2.setText("");
							t1.setText("");
							t3.setText("");

						}




					}
				}

				while (tttttt) {
					System.out.println("");
					if (a == true) {
						AccountTransfer at = new AccountTransfer();
						tttttt = false;
						a = false;
					}
				}


				while (proceed){
					System.out.println("");
				}
				co = 0;
				while(co < size){
					updateDetails(account[co].getPin(), (int)account[co].getBalance(), account[co].getAc_no());
					co++;
				}


			}



			}

		sc.close();
		}
		
	}


