package Tests;

import Model.Account;
import Model.Exceptions.IllegalValueException;
import Model.Exceptions.NotEnoughMoneyException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by AndyTsang on 2017-07-01.
 */
public class AccountTest {
    private Account testAccount;
    private double temp = 0.0000000001;

    @Before
    public void runBefore() throws IllegalValueException {
        testAccount = new Account("Andy", "Tsang");
    }


    @Test
    public void testAccount() throws IllegalValueException {
        Account testFake = new Account("Andy", "Tsang");
        assertTrue(testFake != testAccount);
        assertEquals(1, testAccount.getAccountNumber());
        assertEquals("Andy", testAccount.getName());
        assertEquals(0,testAccount.getBalance(), temp);
    }

    @Test
    public void testAddnRemove() throws IllegalValueException, NotEnoughMoneyException {
        testAccount.depositMoney(100.00);
        assertEquals(100.00, testAccount.getBalance(), temp);
        testAccount.withdrawMoney(50.00);
        assertEquals(50, testAccount.getBalance(), temp);
        testAccount.withdrawMoney(50.00);
        assertEquals(0, testAccount.getBalance(), temp);

    }

    @Test (expected = NotEnoughMoneyException.class)
    public void depositMoneyFail1() throws IllegalValueException, NotEnoughMoneyException {
        testAccount.depositMoney(100.00);
        assertEquals(100.00, testAccount.getBalance(), temp);
        testAccount.withdrawMoney(50.00);
        assertEquals(50, testAccount.getBalance(), temp);
        testAccount.withdrawMoney(50.00);
        assertEquals(0, testAccount.getBalance(), temp);
        testAccount.withdrawMoney(50.00);
        assertEquals(0, testAccount.getBalance(), temp);
    }

    @Test (expected = IllegalValueException.class)
    public void depositMoneyFail2() throws IllegalValueException, NotEnoughMoneyException {
        testAccount.depositMoney(-100.00);
    }


    @Test (expected = IllegalValueException.class)
    public void depositMoneyFail3() throws IllegalValueException, NotEnoughMoneyException {
        testAccount.withdrawMoney(-100.00);
    }
}
