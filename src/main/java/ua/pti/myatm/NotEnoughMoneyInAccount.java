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
public class NotEnoughMoneyInAccount extends Exception{
    
    public NotEnoughMoneyInAccount() {
        super("Not enough money in account");
    }
}
