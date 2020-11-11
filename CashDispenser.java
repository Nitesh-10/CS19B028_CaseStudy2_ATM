package atm;

public class CashDispenser{
	private double Balance;
	public CashDispenser(double balance) {

		Balance = balance;
	}
	
	
	public double getBalance() {
		return Balance;
	}//get balance method of dispenser

	public void setBalance(double balance) {
		Balance = balance;
	}//set balance method of dispenser
	//method which checks whether sufficient cash is present in the dispenser to complete the withdrawal
	public boolean dispense(double amount_to_withdraw) {
		boolean success = false;
		if(amount_to_withdraw <= getBalance()) {
			setBalance(getBalance()-amount_to_withdraw);
			success =  true;	
		}

		return success;
	}

}

