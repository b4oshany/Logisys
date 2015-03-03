package OPERATIONS;

import java.lang.*;
//import java.text.*;
import java.util.*;
/**
 * Abstract class User - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class User{
    protected String username;
    protected String password;

    public User(String username,String pwd){
        this.username=username;
        this.password= pwd;
    }
    

    public String getUserName(){
        return username;
    }
    
    
    public String getPassword(){
        return password;
    }
    
         
    public String toString(){
        return username;
    }
}