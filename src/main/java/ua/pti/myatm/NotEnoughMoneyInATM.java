/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.pti.myatm;

/**
 *
 * @author denis
 */
public class NotEnoughMoneyInATM extends Exception{
    
    public NotEnoughMoneyInATM() {
        super("Not enough money in ATM");
    }
}
