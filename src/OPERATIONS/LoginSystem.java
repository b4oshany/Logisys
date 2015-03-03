package OPERATIONS;
import javax.swing.*;
/**
 * Write a description of class LoginSystem here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class LoginSystem 
{
    public static void main(String[] args){
        JFrame frame = new JFrame("MY SYSTEM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("icon.png");
        frame.setIconImage(img.getImage());
        
        LogGui  log = new LogGui(frame); 
        
        frame.setVisible(true);  
        frame.pack();
        frame.setSize(frame.getMaximumSize());
    }
}

