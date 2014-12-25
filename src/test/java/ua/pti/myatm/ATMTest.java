/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pti.myatm;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InOrder;
import static org.mockito.Mockito.*;

/**
 *
 * @author andrii
 */
public class ATMTest {

    @Test
    public void testInitializeATMWithNormalMoney() {
        ATM instance = new ATM(5000);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInitializeATMWithIllegalMoney() {
        ATM instance = new ATM(-1);
    }
    
    @Test
    public void testInitializeATMWithBoundaryMoney() {
        ATM instance = new ATM(0);
    }

    /**
     * Test of getMoneyInATM method, of class ATM.
     */
    @Test
    public void testGetMoneyInATM() {
        ATM instance = new ATM(5000);
        double result = instance.getMoneyInATM();
        assertEquals(5000, result, 0.0);
    }


    /**
     * Test of validateCard method, of class ATM.
     */
    @Test
    public void testValidateCardWithRightData() {
        Card card = mock(Card.class);
        ATM instance = new ATM(1000);
        int pinCode = 1234;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        boolean result = instance.validateCard(card, pinCode);
        assertEquals(true, result);
    }
    
    @Test
    public void testValidateCardWithIncorrectPinCode() {
        Card card = mock(Card.class);
        ATM instance = new ATM(1000);
        int pinCode = 1234;
        when(card.checkPin(pinCode)).thenReturn(false);
        when(card.isBlocked()).thenReturn(false);
        boolean result = instance.validateCard(card, pinCode);
        assertEquals(false, result);
    }
    
    @Test
    public void testValidateCardWithBlockedCard() {
        Card card = mock(Card.class);
        ATM instance = new ATM(1000);
        int pinCode = 1234;
        when(card.checkPin(pinCode)).thenReturn(true);
        when(card.isBlocked()).thenReturn(true);
        boolean result = instance.validateCard(card, pinCode);
        assertEquals(false, result);
    }

    /**
     * Test of checkBalance method, of class ATM.
     */
    @Test
    public void testCheckBalance() throws NoCardInserted, NotEnoughMoneyInATM, NotEnoughMoneyInAccount{
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        ATM instance = new ATM(card, 1000);
        when(account.getBalance()).thenReturn(500.0);
        when(card.getAccount()).thenReturn(account);
        double result = instance.checkBalance();
        assertEquals(500, result, 0.0);
    }
    
    @Test(expected = NoCardInserted.class)
    public void testCheckBalanceFromNoInsertedCard() throws NoCardInserted, NotEnoughMoneyInATM, NotEnoughMoneyInAccount{
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        ATM instance = new ATM(null, 1000);
        instance.checkBalance();
    }

    /**
     * Test of getCash method, of class ATM.
     */
     @Test
    public void testGetCash() throws NotEnoughMoneyInATM, NotEnoughMoneyInAccount, NoCardInserted{
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        
        ATM instance = new ATM(card, 1000);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(500.0);
        when(account.withdraw(200.0)).thenReturn(200.0);
        
        
        instance.getCash(200);
        double money = instance.getMoneyInATM();
        InOrder order = inOrder(account);
        
        order.verify(account, times(1)).getBalance();
        order.verify(account, times(1)).withdraw(200);
        order.verify(account, times(1)).getBalance();
        
        assertEquals(800, money, 0.0);
    }
    
    @Test(expected = NoCardInserted.class)
    public void testGetCashWithNoCardInserted() throws NotEnoughMoneyInATM, NotEnoughMoneyInAccount, NoCardInserted, IllegalArgumentException{
        System.out.println("testGetCashWithNoCardInserted");
        Account account = mock(Account.class);
        ATM instance = new ATM(null, 1000);
        double result = instance.getCash(800);
    }
    
    @Test(expected = NotEnoughMoneyInAccount.class)
    public void testGetCashWithMoreAmountThanOnAccount() throws NotEnoughMoneyInATM, NotEnoughMoneyInAccount, NoCardInserted, IllegalArgumentException{
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        ATM instance = new ATM(card, 1000);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(300.0);
        double result = instance.getCash(800);
    }

    @Test(expected = NotEnoughMoneyInATM.class)
    public void testGetCashWithMoreAmountThanInATM() throws NotEnoughMoneyInATM, NotEnoughMoneyInAccount, NoCardInserted, IllegalArgumentException{
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        ATM instance = new ATM(card, 1000);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(2000.0);
        double result = instance.getCash(1500);
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetCashWithIllegalAmount() throws NotEnoughMoneyInATM, NotEnoughMoneyInAccount, NoCardInserted, IllegalArgumentException{
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        ATM instance = new ATM(card, 5000);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(5000.0);
        double result = instance.getCash(4000.1);
    }
    
 
    
    @Test
    public void testGetCashBoundaryTop() throws NotEnoughMoneyInATM, NotEnoughMoneyInAccount, NoCardInserted, IllegalArgumentException{
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        ATM instance = new ATM(card, 4000);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(4000.0);
        when(account.withdraw(4000)).thenReturn(4000.0);
        instance.getCash(4000);
        
        InOrder order = inOrder(account);

        order.verify(account, times(1)).getBalance();
        order.verify(account, times(1)).withdraw(4000);
        order.verify(account, times(1)).getBalance();
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testGetCashBoundaryBottom() throws NotEnoughMoneyInATM, NotEnoughMoneyInAccount, NoCardInserted, IllegalArgumentException{
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        ATM instance = new ATM(card, 4000);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(4000.0);
        double result = instance.getCash(0.0);
    }
    
    @Test
    public void validateIllegalCard3(){
        Card card = mock(Card.class);
        ATM instance = new ATM(1000);
        
        when(card.checkPin(1111)).thenReturn(false);
        when(card.isBlocked()).thenReturn(false);
        
        instance.validateCard(card, 1111);
        verify(card, times(0)).block();
        assertEquals(1, instance.getIndexOfIllegalInput(), 0.0);
     
        instance.validateCard(card, 1111);
        verify(card, times(0)).block();
        assertEquals(2, instance.getIndexOfIllegalInput(), 0.0);
        
        instance.validateCard(card, 1111);
        verify(card, times(1)).block();
        assertEquals(0, instance.getIndexOfIllegalInput(), 0.0);
    }
    
    @Test
    public void validateIllegalCardWith2IllegalTry(){
        Card card = mock(Card.class);
        ATM instance = new ATM(1000);
        
        when(card.checkPin(1111)).thenReturn(false);
        when(card.checkPin(0000)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        
        instance.validateCard(card, 1111);
        verify(card, times(0)).block();
        assertEquals(1, instance.getIndexOfIllegalInput(), 0.0);
        
        instance.validateCard(card, 1111);
        verify(card, times(0)).block();
        assertEquals(2, instance.getIndexOfIllegalInput(), 0.0);
        
        instance.validateCard(card, 0000);
        verify(card, times(0)).block();
        assertEquals(0, instance.getIndexOfIllegalInput(), 0.0);
        
        instance.validateCard(card, 1111);
        verify(card, times(0)).block();
        assertEquals(1, instance.getIndexOfIllegalInput(), 0.0);
    }
    
    @Test
    public void validateIllegalCardWithRightAndIllegalTry(){
        Card card = mock(Card.class);
        ATM instance = new ATM(1000);
        
        when(card.checkPin(1111)).thenReturn(false);
        when(card.checkPin(0000)).thenReturn(true);
        when(card.isBlocked()).thenReturn(false);
        
        instance.validateCard(card, 0000);
        verify(card, times(0)).block();
        assertEquals(0, instance.getIndexOfIllegalInput(), 0.0);
        
        instance.validateCard(card, 1111);
        verify(card, times(0)).block();
        assertEquals(1, instance.getIndexOfIllegalInput(), 0.0);
        
        instance.validateCard(card, 1111);
        verify(card, times(0)).block();
        assertEquals(2, instance.getIndexOfIllegalInput(), 0.0);
        
        instance.validateCard(card, 1111);
        verify(card, times(1)).block();
        assertEquals(0, instance.getIndexOfIllegalInput(), 0.0);
    }
    
    @Test
    public void validateIllegalCardWith2Cards(){
        Card card = mock(Card.class);
        Card card2 = mock(Card.class);
        ATM instance = new ATM(1000);
        
        when(card.checkPin(1111)).thenReturn(false);
        when(card2.checkPin(1111)).thenReturn(false);
        when(card.isBlocked()).thenReturn(false);
        
        instance.validateCard(card2, 1111);
        verify(card, times(0)).block();
        assertEquals(1, instance.getIndexOfIllegalInput(), 0.0);
        
        instance.validateCard(card2, 1111);
        verify(card, times(0)).block();
        assertEquals(2, instance.getIndexOfIllegalInput(), 0.0);
        
        instance.validateCard(card, 1111);
        verify(card, times(0)).block();
        assertEquals(1, instance.getIndexOfIllegalInput(), 0.0);
        
        instance.validateCard(card, 1111);
        verify(card, times(0)).block();
        assertEquals(2, instance.getIndexOfIllegalInput(), 0.0);
        
        instance.validateCard(card, 1111);
        verify(card, times(1)).block();
        assertEquals(0, instance.getIndexOfIllegalInput(), 0.0);
    }
    
    @Test
    public void validateIllegalCardVerifyCheckPin(){
        Card card = mock(Card.class);
        ATM instance = new ATM(1000);
        
        when(card.checkPin(0000)).thenReturn(false);
        when(card.isBlocked()).thenReturn(false);
        
        
        instance.validateCard(card, 1111);
  
        InOrder order = inOrder(card);
        
        order.verify(card, times(1)).checkPin(1111);
        order.verify(card, times(0)).block();
        
        instance.validateCard(card, 1111);
        
        order.verify(card, times(1)).checkPin(1111);
        order.verify(card, times(0)).block();
        
        instance.validateCard(card, 1111);
        
        order.verify(card, times(1)).checkPin(1111);
        order.verify(card, times(1)).block();
    }
    
}
