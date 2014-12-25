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
public class NoCardInserted extends Exception{
    // классический конструктор с сообщением о характере ошибки
    public NoCardInserted() {
        super("NoCardInserted");
    }
}
