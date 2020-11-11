package atm;
//bank account class to store multiple bank accounts
public class BankAccount {
	private int Ac_no;//account number
	private int pin;//pin
	private double balance;//balance
	private int Ifsc;
	
	public BankAccount(int Ac_no,int pin,double balance,int Ifsc) {
		this.Ac_no = Ac_no;
		this.pin = pin;
		this.balance = balance;
		this.Ifsc = Ifsc;
	}
	//get and set methods for account number,pin,balance
	public void setAc_No(int accountNumber) {
		Ac_no = accountNumber;
	}
	
	public int getAc_no() {
		return Ac_no;
	}
	
	public void setPin(int pin) {
		this.pin = pin;
	}
	
	public int getPin() {
		return pin;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public double getBalance() {
		return balance;
	}

	public int getIfsc() {
		return Ifsc;
	}


}
