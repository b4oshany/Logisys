package OPERATIONS;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;

public class GUIEmp{

    private JPanel centralPanel;
    private JPanel passPanel;
    private JPanel namePanel;
    private JPanel viewSummaryPanel;
    private JPanel panel;


    private JLabel logHere;
    private JLabel title;    
    private JButton RETURN;  
    private JButton viewSummary; 
    private GridBagConstraints gbc;
    private Border border;
    private JFrame frame;

    public GUIEmp(Employee emp, JFrame frame){  
        centralPanel = new JPanel();
        passPanel = new JPanel();
        namePanel = new JPanel();
        viewSummaryPanel = new JPanel();
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        logHere = new JLabel("EMPLOYEE DETAILS");
        title = new JLabel("EMPLOYEE LOG SCREEN");    
        RETURN= new JButton("RETURN TO LOG SCREEN");  
        this.frame = frame;
        panel.setVisible(true);
        border = BorderFactory.createLineBorder(Color.BLUE, 5);
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;  

        ReturnListener returnListen = new ReturnListener();      

        /*Userinfo*/
        centralPanel.add(logHere);
        centralPanel.add(namePanel);
        centralPanel.add(passPanel);

        viewSummaryPanel.setLayout(new BoxLayout(viewSummaryPanel, BoxLayout.PAGE_AXIS));
        viewSummaryPanel.setPreferredSize(new Dimension(400,400));  
        viewSummaryPanel.setBorder(border);
        
        
        try{
            emp = Record.getListOfEmployees().get(Record.getIndex(emp));
            Payroll pay = new Payroll(emp);
            viewSummaryPanel.add(new JLabel("<<<<< |||||Employee name: " + emp.getFName() + " " + emp.getLName() + " ||||| >>>>>"));
            viewSummaryPanel.add(Box.createRigidArea(new Dimension(10, 30)));
            viewSummaryPanel.add(new JLabel("Employee total hours worked: " + pay.getTotalHrs()));
            viewSummaryPanel.add(Box.createRigidArea(new Dimension(10, 30)));
            viewSummaryPanel.add(new JLabel("Employee Pending requests: N/A"));
            viewSummaryPanel.add(Box.createRigidArea(new Dimension(10, 30)));
            viewSummaryPanel.add(new JLabel("Employee Over time horurs: " + pay.getOvertimeHrs()));
            viewSummaryPanel.add(Box.createRigidArea(new Dimension(10, 30)));
            viewSummaryPanel.add(new JLabel("Employee PAYEE: $" +  pay.getPayee()));
            viewSummaryPanel.add(Box.createRigidArea(new Dimension(10, 30)));
            if(emp.getDateLoged().size()>0){
                try{
                    viewSummaryPanel.add(new JLabel("Employee Last Loged in: " + emp.getDateLoged().get(emp.getDateLoged().size()-1) + " " +  emp.getLoginTimes().get(emp.getLoginTimes().size()-1)));
                    viewSummaryPanel.add(Box.createRigidArea(new Dimension(10, 30)));
                }catch(Exception e){}
                if(emp.getLogoutTimes().get(emp.getLogoutTimes().size()-1) == null){    //Used to fix a bug, index number was out of bound
                    try{
                        viewSummaryPanel.add(new JLabel("Employee Last Loged out: " + emp.getLogoutTimes().get(emp.getLogoutTimes().size()-2))); 
                    }catch(Exception e){}
                }
                else{
                    try{
                        viewSummaryPanel.add(new JLabel("Employee Last Loged out: " + emp.getLogoutTimes().get(emp.getLogoutTimes().size()-1)));
                    }catch(Exception e){}
                }
            }
        }catch(Exception e){}
        RETURN.addActionListener(returnListen);  
        centralPanel.add(RETURN);
        panel.add(centralPanel, gbc); 
        panel.add(viewSummaryPanel, gbc);
        frame.add(panel);
    }

    class ReturnListener implements ActionListener{
        public void actionPerformed (ActionEvent event){
            //Validate that the ID number belongs to an employee
            panel.setVisible(false);
            frame.remove(panel);
            frame.repaint();
            LogGui log = new LogGui(frame);
        }
    }

    class BgPanel extends JPanel {
        Image bg = new ImageIcon("background.jpg").getImage();
        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(bg, getWidth(), getHeight(), this); 
        }
    }
}


  
        
      