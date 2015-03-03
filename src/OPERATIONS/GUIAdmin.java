package OPERATIONS;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

public class GUIAdmin extends JPanel{    

    private JScrollPane scrollPane;

    /**
     * text field Declaration
     */
    private JPanel AdminPanel, panel, Display, Display1, optionPanel, searchPAn;
    private JTextField searchArea;
    private JTextField namText;
    private JTable table;
    private Object data[][];

    /**
     * Buttons Declaration
     */
    private JButton add;
    private JButton edit;
    private JButton delete;
    private JButton view;
    private JButton Return;
    private JButton search;
    private JButton clear;
    private JButton statistics;
    private JPanel group;
    private JPanel ReturnPan;
    private JFrame frame;
    private DefaultTableModel model;

    public GUIAdmin(JFrame frame){      
        this.frame = frame;
        BorderLayout BL = new BorderLayout();
        panel = new BgPanel();
        AdminPanel = new JPanel(new GridBagLayout());
        Display = new JPanel();
        searchPAn = new JPanel();
        optionPanel = new JPanel();
        ReturnPan = new JPanel();
        AdminPanel.setLayout(BL);

        AdminPanel.setVisible(true);

        searchArea =new JTextField(15);

        /**
         *Option buttons
         ***/
        add = new JButton("ADD");
        edit = new JButton("EDIT");
        delete = new JButton("DELETE");
        view = new JButton("VIEW");
        search = new JButton("SEARCH");
        Return = new JButton("RETURN");
        clear = new JButton("CLEAR DATA");
        statistics  = new JButton("PRINT STATS DATA");
        
        /**
         * Group will hold all the the option buttons
         * */
        group = new JPanel();

        optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.LINE_AXIS));
        optionPanel.setAlignmentY(AdminPanel.LEFT_ALIGNMENT);

        optionPanel.add(add); 
        optionPanel.add(Box.createRigidArea(new Dimension(10, 20)));
        optionPanel.add(edit);
        optionPanel.add(Box.createRigidArea(new Dimension(10, 20)));
        optionPanel.add(view);
        optionPanel.add(Box.createRigidArea(new Dimension(10, 20)));
        optionPanel.add(delete);
        optionPanel.add(Box.createRigidArea(new Dimension(10, 20)));
        optionPanel.add(group);
        optionPanel.add(Box.createRigidArea(new Dimension(400, 20)));

        searchPAn.add(new JLabel("Search"));
        searchArea.setText("search by first name");
        searchPAn.add(searchArea);
        searchPAn.add(search);

        optionPanel.add(searchPAn, AdminPanel.RIGHT_ALIGNMENT);;

        /*
         *Create Button Listeners  
         */
        AddListener ad = new AddListener();
        EditListener edt = new EditListener();
        DeleteListener del = new DeleteListener();
        ViewListener vew = new ViewListener();
        ReturnListener sub = new ReturnListener();
        SearchListener srch = new SearchListener();
        ClearListener clr = new ClearListener();
        StatisticsListener stat = new StatisticsListener();
        
        /*
         *Add Button Listeners  
         */
        add.addActionListener(ad);
        edit.addActionListener(edt);
        delete.addActionListener(del);
        view.addActionListener(vew);
        Return.addActionListener(sub);  
        search.addActionListener(srch); 
        clear.addActionListener(clr); 
        statistics.addActionListener(stat); 

        /*
         *Set Display area Properties
         */

        Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
        Display.setLayout(new BoxLayout(Display, BoxLayout.PAGE_AXIS));
        Display.setBackground(Color.gray);
        Display.setBorder(border);

        ReturnPan.add(Return);
        ReturnPan.add(clear);
        ReturnPan.add(statistics);
        /*
         *Add all panels to the main admin panel
         */
        AdminPanel.add(Display,  BorderLayout.CENTER);
        AdminPanel.add(optionPanel, BorderLayout.NORTH);
        AdminPanel.add(ReturnPan, BorderLayout.SOUTH);
        panel.add(AdminPanel, BorderLayout.CENTER);
        frame.add(panel); //Add admin panel to the frame
        Tables(Record.getListOfEmployees());
        /*
         * Set frame properties
         */
    } //end constructor

    /*
     *  Create add button listener class 
     */
    private class AddListener implements ActionListener{
        protected JLabel inputLabel = new JLabel ();
        protected JTextField data;
        TextField tfInput[];  // single-line TextField to receive tfInput key
        protected JLabel inputLabel1 = new JLabel ();
        protected JPanel multiPanel;

        public void actionPerformed (ActionEvent event){
            Display.setVisible(false);
            multiPanel =new JPanel();
            try{
                int n = JOptionPane.showConfirmDialog(AdminPanel,"Are you sure you want to create emplppyee?");

                if (n==0){
                    multiPanel.setLayout(new BoxLayout(multiPanel, BoxLayout.Y_AXIS));
                    tfInput = new TextField[5];

                    tfInput[0]= new TextField(10);
                    tfInput[0].setText("  ");
                    multiPanel.add(new Label("Enter ID# number(Must be Numbers)"));
                    multiPanel.add(tfInput[0]);

                    tfInput[1]= new TextField(10);
                    tfInput[1].setText("  ");
                    multiPanel.add(new Label("Enter your Frist Name"));
                    multiPanel.add(tfInput[1]);

                    tfInput[2]= new TextField(10);
                    tfInput[2].setText("  ");
                    multiPanel.add(new Label("Enter your Last Name"));
                    multiPanel.add(tfInput[2]);

                    tfInput[3]= new TextField(10);
                    tfInput[3].setText("  ");
                    multiPanel.add(new Label("Enter your User Name"));
                    multiPanel.add(tfInput[3]);

                    tfInput[4]= new TextField(10);
                    tfInput[4].setText("  ");
                    multiPanel.add(new Label("Enter password")); 
                    multiPanel.add(tfInput[4]);

                    JOptionPane.showConfirmDialog(null,multiPanel, "Enter the data for Employee", JOptionPane.OK_CANCEL_OPTION);
                    Record.AddEmployeeData(new Employee(tfInput[1].getText().trim(), tfInput[2].getText().trim(),tfInput[3].getText().trim(), tfInput[4].getText().trim(), Integer.parseInt(tfInput[0].getText().trim()), false)); 

                    Display.removeAll();
                    Display.setVisible(false);  
                    Tables(Record.getListOfEmployees());
                    Display.setVisible(true);
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
            Display.removeAll();
            Display.setVisible(false);
            Tables(Record.getListOfEmployees());
            Display.setVisible(true);
        }
    }

    /*
     *  Create view button listener class 
     */
    private class ViewListener implements ActionListener{
        public void actionPerformed (ActionEvent event){

            int i = isSelected();
            if(i!= -1){
                JOptionPane.showMessageDialog(null,  new Payroll(Record.getListOfEmployees().get(i)).toString());
            }
            
            Display.removeAll();
            Display.setVisible(false);
            Tables(Record.getListOfEmployees());
            Display.setVisible(true);

            

        }
    }

    /*
     *  Create edit button listener class 
     */
    private class EditListener implements ActionListener{
        protected JLabel inputLabel = new JLabel ();
        protected JTextField data;
        TextField tfInput[];  // single-line TextField to receive tfInput key
        protected JLabel inputLabel1 = new JLabel ();
        protected JPanel multiPanel;

        public void actionPerformed (ActionEvent event){
            Display.setVisible(false);
            multiPanel =new JPanel();
            try{
                    multiPanel.setLayout(new BoxLayout(multiPanel, BoxLayout.Y_AXIS));
                    tfInput = new TextField[4];
                    int i = isSelected();
                    if(i!= -1){                        

                        tfInput[0]= new TextField(10);
                        tfInput[0].setText(table.getValueAt(i, 1).toString());
                        multiPanel.add(new Label("First Name"));
                        multiPanel.add(tfInput[0]);

                        tfInput[1]= new TextField(10);
                        tfInput[1].setText(table.getValueAt(i, 2).toString());
                        multiPanel.add(new Label("Last Name"));
                        multiPanel.add(tfInput[1]);

                        tfInput[2]= new TextField(10);
                        tfInput[2].setText(table.getValueAt(i, 3).toString());
                        multiPanel.add(new Label("User Name"));
                        multiPanel.add(tfInput[2]);

                        tfInput[3]= new TextField(10);
                        tfInput[3].setText(Record.getListOfEmployees().get(i).getPassword());
                        multiPanel.add(new Label("Password"));
                        multiPanel.add(tfInput[3]);

                        multiPanel.setLayout(new BoxLayout(multiPanel, BoxLayout.Y_AXIS));
                        
                        
                        int x = JOptionPane.showConfirmDialog(null,multiPanel, "Enter the data for the Organization", JOptionPane.OK_CANCEL_OPTION);
                       if(x == 0){
                           Record.updateEmployee(i, new Employee(tfInput[0].getText().trim(), tfInput[1].getText().trim(), tfInput[2].getText().trim(), tfInput[3].getText().trim(), Record.getListOfEmployees().get(i).getId(), Record.getListOfEmployees().get(i).getStatus()));
                       }
                       System.out.print(x);
                        multiPanel.removeAll();                         
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Select  An employee First");
                        Display.removeAll();
                        Display.setVisible(false);
                        Tables(Record.getListOfEmployees());
                        Display.setVisible(true);
                    }

                
            }
            catch(Exception e){
                System.out.println(e);
            }
            Display.removeAll();
            Display.setVisible(false);
            Tables(Record.getListOfEmployees());
            Display.setVisible(true);
        }
    }   

    /*
     *  Create delete button listener class 
     */
    private class DeleteListener implements ActionListener{
        public void actionPerformed (ActionEvent event){
            try{
                int n = JOptionPane.showConfirmDialog(AdminPanel,"Are you sure you want to Delete employee?");
                Display.setVisible(false);
                if (n==0){
                    int i = isSelected();
                    if(i!= -1){
                        Record.deleteEmployee(Integer.parseInt(table.getValueAt(i, 4).toString().trim()));
                        JOptionPane.showMessageDialog(null,table.getValueAt(i, 1).toString() + "Has Been deleted");
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null,"Select  An employee First");
                    Display.removeAll();
                    Display.setVisible(false);
                    Tables(Record.getListOfEmployees());
                    Display.setVisible(true);
                }
            }
            catch(Exception e){
            }
            Display.removeAll();
            Display.setVisible(false);
            Tables(Record.getListOfEmployees());
            Display.setVisible(true);
        }
    }   

    /*
     *  Create Return button listener class 
     */
    private class ReturnListener implements ActionListener{
        public void actionPerformed (ActionEvent event){
            //Validate that the ID number belongs to an employee
            AdminPanel.setVisible(false);
            frame.remove(AdminPanel);
            frame.repaint();
            LogGui log = new LogGui(frame);
        }
    }
    
    
     /*
     *  Create Statistics button listener class 
     */
    private class StatisticsListener implements ActionListener{
        public void actionPerformed (ActionEvent event){
            try{
                if(table.print()){
                JOptionPane.showMessageDialog(null, "Table was Printed");
                }else{
                JOptionPane.showMessageDialog(null, "Table wasn't Printed");
                }
            
            }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error: "+e);
            }
        }
    }
    
    
    private class ClearListener implements ActionListener{
        public void actionPerformed (ActionEvent event){
            int n = JOptionPane.showConfirmDialog(AdminPanel,"Are you sure you want to Delete all");
            Display.setVisible(false);
            if (n==0){
                if( Record.getListOfEmployees().size() >=1){
                    for(Employee emp : Record.getListOfEmployees()){
                        Record. clearLogFields(emp.getId());
                    }           
                    Display.removeAll();
                    Display.setVisible(false);
                    Tables(Record.getListOfEmployees());
                    Display.setVisible(true);
                    JOptionPane.showMessageDialog(null, "database cleared");
                }
            }
            Display.removeAll();
            Display.setVisible(false);
            Tables(Record.getListOfEmployees());
            Display.setVisible(true);
        }
    }

    private int isSelected(){
        for (int i = 0; i < table.getRowCount(); i++) {
            Boolean chked = Boolean.valueOf(table.getValueAt(i, 0).toString());
            if (chked) {
                for (int j = 0; j < table.getRowCount(); j++) {
                    table.setValueAt(false,j, 0);
                }
                return i;
            }
        }
        return -1;
    }

    /*
     *  Create Return button listener class 
     */
    private class SearchListener implements ActionListener{        
        public void actionPerformed (ActionEvent event){
            ArrayList <Employee> list = new ArrayList <Employee>();

            for(Employee emp : Record.getListOfEmployees()){    
                if(searchArea.getText().equals(emp.getFName())){ 
                    list.add(emp);   
                }  
            }  
            if(list.size() >=1){
                Display.removeAll();
                Display.setVisible(false);
                Tables(list);
                Display.setVisible(true);
            }
            else{
                JOptionPane.showMessageDialog(null,"Employee was not found");
            }
            searchArea.setText("");
        }
    }

    /*
     *Create table class to display table
     */
    private Component Tables(ArrayList <Employee> arraylist){  

        scrollPane = new JScrollPane();
        table = new JTable();
        scrollPane.setViewportView(table);
        Display.add(table.getTableHeader());
        Display.add(scrollPane);
        // Model for Table

        model = new DefaultTableModel() 
        {
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 1:
                    return String.class;
                    case 2:
                    return String.class;
                    case 3:
                    return String.class;
                    case 4:
                    return Integer.class;
                    default:
                    return Boolean.class;
                }
            }

        };  

        table.setModel(model);

        model.addColumn("check");

        model.addColumn("First Name");

        model.addColumn("Last Name");

        model.addColumn("User Name");

        model.addColumn("ID#");

        for(int i = 0; i<arraylist.size(); i++){
            model.addRow(new Object[i]);
            model.setValueAt(false, i, 0);
            model.setValueAt(arraylist.get(i).getFName(), i, 1);
            model.setValueAt(arraylist.get(i).getLName(), i, 2);
            model.setValueAt(arraylist.get(i).getUserName(), i, 3);
            model.setValueAt(arraylist.get(i).getId(), i, 4);      
        }
        return table;
    }
    
     class BgPanel extends JPanel {
        Image bg = new ImageIcon("background.jpg").getImage();
        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(bg,getWidth(), getHeight(), this);
        }
    }
}
