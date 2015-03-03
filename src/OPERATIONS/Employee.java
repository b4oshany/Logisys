package OPERATIONS;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.Time;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 * Write a description of class Employee here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Employee extends User{
    private ArrayList<Time> employeeLogoutTimes;
    private ArrayList<Time> employeeLoginTimes;
    private ArrayList<Date> employeeLogedDated;
    private int id;
    private boolean status;    
    private String fname;
    private String lname;
    /**
     * Constructor for objects of class Employee
     */
    public Employee(String fname,String lname,String username,String pwd,int id, boolean status)
    {
        super(username,pwd);
        this.id = id;
        this.status = status;
        this.fname = fname;
        this.lname = lname;        
        employeeLogoutTimes =new ArrayList<Time>();
        employeeLoginTimes = new ArrayList<Time>();
        employeeLogedDated = new ArrayList<Date>();
    }

    
    public boolean getStatus(){
        return status;
    }

    public void setStatus(){
        status = !getStatus(); //true if logged in
    }   
    
    
    public String getFName(){
        return fname;
    }
    
    
    public String getLName(){
        return lname;
    }
    
    
    public int getId(){
        return id;
    }    
    
    
    public ArrayList<Time> getLoginTimes(){
        return employeeLoginTimes;
    }
    
    
    public  ArrayList<Time> getLogoutTimes(){
        return employeeLogoutTimes;
    }
    
    
    public  ArrayList<Date> getDateLoged(){
        return employeeLogedDated;
    }    
    
    
    public String toString(){
        return "Name: "+fname+" "+lname;
    }
    
}
