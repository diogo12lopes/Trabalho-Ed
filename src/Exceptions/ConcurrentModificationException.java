/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exceptions;


/**
 *
 * @author Diogo Lopes 8180121
 */
public class ConcurrentModificationException extends Exception{
    public ConcurrentModificationException(String message) {
        super(message);
    }
}
