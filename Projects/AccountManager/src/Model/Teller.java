package Model;

import Model.Exceptions.AccountManagerException;
import Model.Exceptions.IllegalValueException;
import Model.Exceptions.NotEnoughMoneyException;
import Model.Exceptions.ParserException;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by AndyTsang on 2017-05-20.
 */
public class Teller {
    private static AccountManager accountManager;
    private static Scanner input;



    public static void main(String[] args) {
        accountManager = AccountManager.getInstance();
        try {
            accountManager.readXML("accounts.xml");
        } catch (ParserException e) {
            e.printStackTrace();
        }
        Boolean continueProgram = true;
        String command = null;
        try{
            initialize();

        }catch (IllegalValueException e) {
            System.out.println(e.getMessage());
            System.out.println("Program Ended.");
        }
        while (continueProgram) {
            try {
//                initialize();

                while (continueProgram) {
                    logInMenu();
                    command = input.next();
                    command = command.toLowerCase();
                    if (command.equals("d")) {
                        System.out.println("Enter Your Account ID: ");
                        int id = input.nextInt();
                        System.out.println("Enter Your Password");
                        String pass = input.next();
                        chooseAccount(id, pass);


                        if (accountManager.selectedAccount != null) {
                            accountMenu();
                            while (continueProgram) {
                                command = input.next();
                                command = command.toLowerCase();
                                if (command.equals("d")) {
                                    doDeposit();
                                    accountMenu();
                                } else if (command.equals("w")) {
                                    doWithdrawal();
                                    accountMenu();
                                } else if (command.equals("t")) {
                                    doTransfer();
                                    accountMenu();
                                } else if (command.equals("q")) {
                                    accountManager.writeXML();
                                    System.out.println("Thank You, Have a Nice Day!");
                                    continueProgram = false;
                                } else {
                                    accountMenu();
                                }
                            }
                        }


                    } else if (command.equals("w"))
                        createAccount();

                    else if (command.equals("q")) {
                        accountManager.writeXML();
                        System.out.println("Thank You, Have a Nice Day!");
                        continueProgram = false;
                    } else
                        System.out.println("Selection invalid");
                }

            } catch (InputMismatchException e) {
                System.out.println("Please Enter a Proper ID and Try Again");
            }
        }


    }


    //MODIFIES: this
    //EFFECT: chooses the account given the id and the password
    public static void chooseAccount(int id, String pass){
        try {
            accountManager.selectAccount(id, pass);
        } catch (AccountManagerException e) {
            System.out.println("No ID found or Password Incorrect Please Try Again");
        }
    }

    //EFFECT: Account menu with withdraw, deposit, transfer
    private static void accountMenu(){
        System.out.println("\nSelect from:");
        System.out.println("\td -> Deposit");
        System.out.println("\tw -> Withdraw");
        System.out.println("\tt -> Transfer");
        System.out.println("\tq -> Quit");

    }


    //EFFECT: log in menu with the option of logging in or creating a new account
    private static void logInMenu(){
        System.out.println("\nDo you want to:");
        System.out.println("\td -> Log in");
        System.out.println("\tw -> Create a new account");
        System.out.println("\tq -> Quit");

    }

    //EFFECT: create account menu giving name and password and returns that id of the new account
    private static void createAccount(){
        Account newAccount = null;
        System.out.println("\nEnter the Name for the Account:");
        String name = input.next();
        System.out.println("Please Enter Your Password for this Account");
        String pass = input.next();

        try {
            newAccount = new Account(name,pass);
            accountManager.addAccount(newAccount);
//            accountManager.writeXML("accounts.xml", newAccount);
        }
        catch (IllegalValueException e) {
            System.out.println(e.getMessage());
        }

        if (newAccount != null) {
            System.out.println("Account ID: " + newAccount.getAccountNumber());
            System.out.println("Owner: " + newAccount.getName());
            System.out.println("Balance: " + newAccount.getBalance());
        }

    }

    //REQUIRES:
    //MODIFIES:
    //EFFECT:
    private static void doDeposit(){
        Account choosenAccount = null;
        try {
            choosenAccount = accountManager.getSelectedInfo();
        }catch (AccountManagerException e){
            System.out.println("Selected Account is Invalid");
        }
        System.out.println("Enter the Amount to Deposit: ");
        double amount = input.nextDouble();

        try {
            choosenAccount.depositMoney(amount);

        }catch (IllegalValueException excep) {
            System.out.println(excep.getMessage());
        }

        printBalance(choosenAccount);


    }

    //REQUIRES:
    //MODIFIES:
    //EFFECT:
    private static void doWithdrawal(){
        Account choosenAccount = null;
        try {
            choosenAccount = accountManager.getSelectedInfo();
        }catch (AccountManagerException e){
            System.out.println("Selected Account is Invalid");
        }
        System.out.println("Enter the Amount to Withdraw: ");
        double amount = input.nextDouble();

        try {
            choosenAccount.withdrawMoney(amount);

        }catch (IllegalValueException e) {
            System.out.println(e.getMessage());
        }
        catch (NotEnoughMoneyException e) {
            System.out.println(e.getMessage());
        }

        printBalance(choosenAccount);
    }

    //REQUIRES:selectedaccount != null
    //MODIFIES:this
    //EFFECT:transfer a set amount of money from the selected account to another if the account is present.
    private static void doTransfer(){
     Account choosenAccount = null;
        try {
            choosenAccount = accountManager.getSelectedInfo();
        }catch (AccountManagerException e){
            System.out.println("Selected Account is Invalid");
        }
        System.out.println("Give the ID of the Account to Transfer Money to: ");
        int transID = input.nextInt();
        Account transAcc = accountManager.findAccount(transID);
        if (!transAcc.equals(null)){
            System.out.println("Enter the Amount to Transfer: ");
            double amount = input.nextDouble();

            try{
                choosenAccount.withdrawMoney(amount);
                transAcc.depositMoney(amount);
            }catch (IllegalValueException e){
                System.out.println("Invalid Amount");
            }catch (NotEnoughMoneyException e){
                System.out.println("Balance of " + choosenAccount.getBalance() + " not High Enough for Transfer of " + transAcc.getBalance());
            }

            System.out.println("Transfer Successful");

        }else
            System.out.println("Invalid Transfer Account");

    }

    private static void printBalance(Account selected) {
        System.out.printf("Balance: $%.2f\n", selected.getBalance());
    }

    private static void initialize() throws IllegalValueException{
//        Account testAccount = new Account("Andy", "Tsang");
//        accountManager.addAccount(testAccount);
        input = new Scanner(System.in);
    }
}
