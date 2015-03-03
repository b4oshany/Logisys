package GUI;

import OPERATIONS.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.sql.*;

/**
 * Write a description of class LogGui here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class StartupGui{

    JPanel centralPanel = new JPanel();
    JPanel passPanel = new JPanel();
    JPanel namePanel = new JPanel();
    JPanel panel = new JPanel(new GridBagLayout());
    JPanel butPan = new JPanel();

    JLabel logHere = new JLabel("CREATE ADMINISTRATOR");
    JLabel title = new JLabel("LOG SCREEN");
    JButton blogin = new JButton("SUBMIT DATA");
    JTextField txuser = new JTextField(15);
    JPasswordField pass = new JPasswordField(15);
    GridBagConstraints gbc;
    protected JFrame frame;
    public String[] args;

    public StartupGui(JFrame frame){
        panel = new BgPanel();
        this.frame = frame;
        Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        centralPanel = new BgPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.PAGE_AXIS));
        centralPanel.setBorder(border);
        panel.setOpaque(true);

        LoginListener loginListen = new LoginListener();
        panel.setVisible(true);
        Font font = new Font("Cooper",Font.BOLD,70);
        logHere.setFont(font);
        logHere.setForeground(Color.RED);

        /*
         *User login info
         */
        centralPanel.add(Box.createRigidArea(new Dimension(10, 20)));
        namePanel.add( new JLabel("SET USER NAME"));
        namePanel.add(Box.createRigidArea(new Dimension(10, 50)));
        namePanel.add(txuser);
        namePanel.add(Box.createRigidArea(new Dimension(10, 50)));

        passPanel.add(new JLabel("SET PASSWORD"));
        passPanel.add(Box.createRigidArea(new Dimension(10, 50)));
        passPanel.add(pass);

        passPanel.add(Box.createRigidArea(new Dimension(10, 50)));

        butPan.add(Box.createRigidArea(new Dimension(10, 50)));
        blogin.addActionListener(loginListen);
        butPan.add(blogin);
        centralPanel.add(logHere);
        centralPanel.add(namePanel);
        centralPanel.add(passPanel);
        centralPanel.add(butPan);

        txuser.setFocusable(true);
        panel.add(centralPanel, gbc);
        panel.setVisible(true);
        panel.setSize(frame.getMaximumSize());
        ImageIcon img = new ImageIcon("icon.png");
        frame.setIconImage(img.getImage());

        frame.add(panel);
    }

    public class LoginListener  implements ActionListener{
        public void actionPerformed (ActionEvent event){
            String puname = txuser.getText().toLowerCase();
            String ppaswd = pass.getText();
            frame.doLayout();
            frame.setLayout(new FlowLayout());

            txuser.requestFocus();
            try
            {
                File f=new File("../res/data.txt");
                f.createNewFile();


                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connect = DriverManager.getConnection("jdbc:mysql://localhost", puname, ppaswd);
                    Statement st = connect.createStatement();
                    String que = "CREATE DATABASE IF NOT EXISTS payroll";

                    st.executeUpdate(que);
                    connect.close();

                    PrintWriter fw = new PrintWriter(new FileWriter(f,true));

                    fw.println(puname);
                    fw.println(ppaswd);
                    fw.close();

                    Statement statement = null;
                    String url = "jdbc:mysql://localhost/payroll";
                    Connection connection = DriverManager.getConnection(url, puname, ppaswd);
                    statement = connection.createStatement();
                   String query = "CREATE TABLE employee_info "
                    + " (id_number INT NOT NULL, "
                        + " password VARCHAR(45), "
                        + " first_name VARCHAR(45), "
                        + " last_name VARCHAR(45), "
                        + " user_name VARCHAR(45), "
                        + " hours_worked DOUBLE, "
                        + " status TINYINT(1), "
                        + " PRIMARY KEY(id_number))";
                    statement.executeUpdate(query);
                    connection.close();


            }
            catch(Exception e)
            {

            }
            if(puname.isEmpty() || ppaswd.isEmpty()){
            txuser.setText("");
            pass.setText("");
            JOptionPane.showMessageDialog(null, "PLEASE SET USERNAME AND PASSWORD");
            txuser.requestFocus();
            pass.requestFocus();
            }
            else{
            txuser.setText("");
            pass.setText("");
            panel.setVisible(false);
            frame.remove(panel);
            frame.repaint();
            //System.exit(3000);
            LogGui  log = new LogGui(frame);
        }

        }
    }


    class BgPanel extends JPanel {
        Image bg = new ImageIcon("../res/background.jpg").getImage();
        @Override
        public void paintComponent(Graphics g) {
            g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

