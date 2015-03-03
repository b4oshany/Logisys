package OPERATIONS;
import GUI.StartupGui;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Time;
import java.io.*;
import java.text.ParseException;

/**
 * Write a description of class LogGui here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LogGui{

    private JPanel centralPanel;
    private JPanel passPanel;
    private JPanel namePanel;
    public JPanel panel;
    private JPanel LogPanel;

    private JLabel logHere;
    private JLabel title;    
    private JButton blogin;      
    private JTextField txuser;
    private JPasswordField pass;
    private GridBagConstraints gbc;
    public JFrame frame;

    public LogGui(JFrame frame){  
        this.frame = frame;
        LogPanel = new JPanel(new GridBagLayout());
        centralPanel = new JPanel();
        passPanel = new JPanel();
        namePanel = new JPanel();
        panel = new BgPanel();
        logHere = new JLabel("LOGIN HERE");
        title = new JLabel("LOG SCREEN");    
        blogin = new JButton("LOGIN");      
        txuser = new JTextField(15);
        pass = new JPasswordField(15);

        
        LogPanel.setVisible(true);
        Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;  

        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.PAGE_AXIS));
        centralPanel.setPreferredSize(new Dimension(400,200));  
        centralPanel.setBorder(border);
        if(Record.LoadDatabase() == true){
            LoginListener loginListen = new LoginListener();
            panel.setVisible(true);

            /*
             *User login info 
             */
            centralPanel.add(Box.createRigidArea(new Dimension(10, 20)));
            namePanel.add( new JLabel("USER NAME"));
            namePanel.add(Box.createRigidArea(new Dimension(10, 50)));
            namePanel.add(txuser);
            namePanel.add(Box.createRigidArea(new Dimension(10, 50)));

            passPanel.add(new JLabel("PASSWORD"));
            passPanel.add(Box.createRigidArea(new Dimension(10, 50)));
            passPanel.add(pass);

            passPanel.add(Box.createRigidArea(new Dimension(10, 50)));

            centralPanel.add(logHere);
            centralPanel.add(namePanel);
            centralPanel.add(passPanel);

            blogin.addActionListener(loginListen); 

            txuser.requestFocus();
            centralPanel.add(blogin);
            LogPanel.add(centralPanel, gbc); 
            panel.add(LogPanel);
            frame.add(panel);
        }
        else{
            File f= new File("data.txt");
            if(!f.exists()){
            StartupGui STPanel=new StartupGui(frame);
        }
  
        else{
            JLabel errorMessage[] = new JLabel[3];
            errorMessage[0] = new JLabel("CONNECTION FAILED!!!");
            errorMessage[1] = new JLabel("make sure the password and user name used to create");
            errorMessage[2] = new JLabel("the databse is the same as admin password");
            Font font = new Font("Cooper",Font.BOLD,40);
            for(int i = 0; i<3; i++){
                errorMessage[i].setFont(font);
                errorMessage[i].setForeground(Color.RED);
                errorMessage[i].add(Box.createRigidArea(new Dimension(10, 50)));
                LogPanel.add(errorMessage[i], gbc);
            }
            panel.add(LogPanel);
            frame.add(panel);
        }  
    }
}


    private class LoginListener  implements ActionListener{
        public void actionPerformed (ActionEvent event){
            String puname = txuser.getText().toLowerCase();
            String ppaswd = pass.getText();
            frame.doLayout();
            frame.setLayout(new FlowLayout());
            Object obj= Autentication(puname,ppaswd);

            if(obj instanceof Admin){       
                panel.setVisible(false);
                frame.remove(panel);
                frame.repaint();
                GUIAdmin log = new GUIAdmin(frame);  
            } else                
            if(obj instanceof Employee){
                int opt = 0;
                Employee emp = (Employee)obj;
                
                if(emp.getStatus() == true){
                    opt = JOptionPane.showConfirmDialog(panel, " You have logged Out \n Do you want to view work summary");
                }else
                {
                    opt = JOptionPane.showConfirmDialog(panel,"You have logged in \n Do you want to view work summary"); 
                }
                Record.updateLogFields(emp); 
                if(opt == 0){
                    panel.setVisible(false);
                    panel.remove(LogPanel);
                    frame.repaint();
                    GUIEmp log = new GUIEmp(emp, frame);
                }               
            } 
            else{
                JOptionPane.showMessageDialog(null,"Wrong user name and password"); 
            }
            txuser.setText("");
            pass.setText("");
            txuser.requestFocus();
        }
    }
    

    private static Object Autentication(String name,String pwd){  
        
        //Return a blank object if nothing was entered
        if(name.equals("")  || pwd.equals("")){
            return new Object();
        }
        
        //Return a employee object if data entered matches an employee
        for(Employee e: Record.getListOfEmployees()){
            if(e.getUserName().equals(((name.toLowerCase())).replaceAll(" ","")) && e.getPassword().equals(pwd)){ 
                return e;
            }
        }

        Admin admin;
        //Return a admin object if data entered matches the admin
        try{ 
            File f = new File("data.txt");
            Scanner scan = new Scanner(f);
            if(scan.hasNext())
            {
                admin = new Admin(scan.nextLine().trim(), scan.nextLine().trim());
                if(admin.getUserName().equals(name) && admin.getPassword().equals(pwd)){
                    return admin;
                } 
            }
        }catch(Exception e){}        
        //Return a blank object if name entered did not match employee or admi
        return new Object();
    }
    
    
    class BgPanel extends JPanel {
        Image bg = new ImageIcon("background.jpg").getImage();
        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(bg, 0, 0,frame.getWidth(),  frame.getHeight(),  this);
        }
    }
}









