package com.frequem.abb.tests;

import com.frequem.abb.ABBConnector;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author frequem(+florian)
 */
public class JByteSender {
    public static void main(String[] args){
        final ABBConnector connector = new ABBConnector();
        
        JFrame frame = new JFrame("JByteSender");
        frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                connector.close();
                System.exit(0);
            }
        });
        
        final JTextField tfInput = new JTextField();
        
        final JButton btnSend = new JButton("Send!");
        btnSend.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int data = 0;
                try{
                    data = Integer.parseInt(tfInput.getText());
                    if(!(data >= 0 && data <= 255))
                        throw new Exception("Invalid input given! 0 <= x <= 255");
                }catch(Exception ex){
                    System.out.println("Error sending data: " + ex.getMessage());
                    return;
                }
                connector.sendByte((byte)data);
            }
            
        });
		
        frame.getContentPane().setLayout(new GridLayout(1, 2));
        frame.getContentPane().add(tfInput);
        frame.getContentPane().add(btnSend);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		
    }
}
