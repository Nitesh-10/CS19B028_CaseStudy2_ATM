package atm;

import java.util.ArrayList;

//Atm class
public class ATM {
	
	ArrayList<Customer> customers= new ArrayList<Customer>();//arraylist of customers that can be added to the atm
	CashDispenser dispenser;//cash dispenser is added to the atm
	
	public ATM(CashDispenser dispenser) {//constuctor which takes object of cash dispenser
		super();
		this.customers = new ArrayList<Customer>() ;
		this.dispenser = dispenser;
	}

	//Login method returns boolean value,to verify the login details
	public boolean LogIn(int ac_no, int pin, ArrayList<Customer> listOfcustomers){
		int customerAc_No;
		int customerPin;
		boolean verified = false;
	
		for(int i=0; i<listOfcustomers.size(); i++){
			
			Customer cust = listOfcustomers.get(i);
			
			BankAccount account = cust.CustomerAccount;
			
			customerAc_No = account.getAc_no();
			customerPin = account.getPin();
			
			if(customerAc_No == ac_no && customerPin == pin){
				verified = true;
			}
		}
		return verified;
	}

}