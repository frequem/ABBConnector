/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frequem.abb;

/**
 *
 * @author user
 */
public class Test {
    public static void main(String[] args){
        ABBConnector abb = new ABBConnector();
        for(int i=0;i<256;i++){
            abb.sendByte((byte) i);
        }
    }
}
