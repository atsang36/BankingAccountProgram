package Model;

import Model.Exceptions.IllegalValueException;
import Model.Exceptions.NotEnoughMoneyException;

/**
 * Created by AndyTsang on 2017-05-13.
 */
public class Account {
    private long number;
    private static int nextNumber = 1;
    private String owner;
    private String pass;
    private double balance;


// create an account with number, name of owner, and password with an initial balance of 0.
    public Account(String owner, String pass) throws IllegalValueException {
        if (owner.length() == 0){
            throw new IllegalValueException("Invalid Name");
        }
        this.number = nextNumber;
        nextNumber++;
        this.owner = owner;
        this.pass = pass;
        balance = 0;

    }

    //EFFECT: get id of account
    public String getName(){
        return owner;
    }

    //EFFECT: retrieves the account number of account
    public long getAccountNumber(){
        return number;
    }

    //EFFECT: get balance
    public double getBalance(){
        return balance;
    }

    public String getPass(){
        return pass;
    }

    public double depositMoney(double amount) throws IllegalValueException {
        if (amount < 0){
            throw new IllegalValueException("Cannot Deposit an Amount Less Than Zero");
        }
        balance += amount;
        return balance;
    }

    public double withdrawMoney(double amount) throws IllegalValueException, NotEnoughMoneyException {
        if (amount < 0){
            throw new IllegalValueException("Cannot WithDraw a Negative Amount");
        }

        if (balance - amount <0 ){
            throw new NotEnoughMoneyException("Account only contains " + balance + ".");
        }
        balance -= amount;
        return balance;
    }

    @Override
    public String toString(){
        return "Model.Account: " + number + "\n" + "Owner: " + owner + "\n" + "Balance: " + balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return number == account.number;
    }

    @Override
    public int hashCode() {
        return (int) (number ^ (number >>> 32));
    }
}
