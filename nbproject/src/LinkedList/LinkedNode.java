/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LinkedList;

/**
 *
 * @author Diogo Lopes 8180121
 * @param <T> wildtype
 */



public class LinkedNode<T> {       
    private LinkedNode next;
    private T Objeto;
    
    
    public LinkedNode(){
        this.next = null;
        this.Objeto = null;
    }

    public LinkedNode(LinkedNode next,T Objeto) {
        this.next = next;
        this.Objeto = Objeto;
    }
     public LinkedNode(T Objeto) {
        this.next = null;
        this.Objeto = Objeto;
    }

    public LinkedNode getNext() {
        return next;
    }

    public void setNext(LinkedNode next) {
        this.next = next;
    }

    public T getObjeto() {
        return Objeto;
    }

    public void setObjeto(T Objeto) {
        this.Objeto = Objeto;
    }  
}