package atm;

public class Customer {

	BankAccount CustomerAccount;//a bank account is linked to the customer
	private String CustomerName;//customers name
	
	// Constructor accepts account object and name
	public Customer(BankAccount customerAccount, String customerName) {
		super();
		CustomerAccount = customerAccount;
		CustomerName = customerName;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	// Method that handles customer withdrawals. Also checks if there is enough money in account to complete transaction
	public boolean Widhdraw(BankAccount account, double widhdrawalAmount){
		
		double balanceAfterWidhdrawal;
		boolean valid = false;
		if(widhdrawalAmount<=account.getBalance()){
		balanceAfterWidhdrawal = account.getBalance() - widhdrawalAmount;
		account.setBalance(balanceAfterWidhdrawal);
		valid = true;
		}else{
		valid = false;
		}
		return valid;
		
	}
	
	// Method that handles customer deposit
	public void Deposit(BankAccount account, double depositedAmount){
		double balanceAfterDeposit;
		balanceAfterDeposit = account.getBalance() + depositedAmount;
		account.setBalance(balanceAfterDeposit);
	}
}