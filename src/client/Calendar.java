/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import Test.MainWindow;

/**
 *
 * @author Umlore
 */
public class Calendar extends Thread{
    private MainWindow _window;
    public Calendar(MainWindow window){
        _window = window;
    }
    
    @Override
    public void run() {
        while (true) {
            _window.UpdateInfo();
            try {
                sleep(7500);
            } catch (InterruptedException ex) {
                System.out.println("HE YCHYL!");
            }
        }
    }
}
