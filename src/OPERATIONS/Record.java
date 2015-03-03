package OPERATIONS;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Time;
import java.sql.Date;
import java.io.*;
import java.text.ParseException;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

public class Record
{ 
    private static ArrayList<Employee> listOfEmployees;

    //Variables used to access database
    public static String password; 
    public static String name;
    public static String url = "jdbc:mysql://localhost/payroll";

    //Add employee to the database
    public static void AddEmployeeData(Employee emp)
    { 
        connection();   //initialize database driver
        Statement st = null;
        ResultSet rs = null;
        try{
            Connection connect = DriverManager.getConnection(url, name , password);//Inititate connection with the database
            Statement stmt = null;
            if(!isEmployee(emp.getId())){ //Check if the employee already exist
                listOfEmployees.add(emp); //Add employee to the employee list
                //Indert employee data into database
                String query = "INSERT into employee_info (Id_number, password, first_name, last_name, user_name, status)" + " values (?, ?, ?, ?, ?, ?)";
                PreparedStatement prepareStmt = connect.prepareStatement (query);
                prepareStmt.setInt (1, emp.getId());
                prepareStmt.setString (2, emp.getPassword());
                prepareStmt.setString (3, emp.getFName());
                prepareStmt.setString (4, emp.getLName());
                prepareStmt.setString (5, emp.getUserName());
                prepareStmt.setBoolean(6, emp.getStatus());
                prepareStmt.execute();

                //Create a new table one a new employee was added to database
                try {
                    String myTable = "CREATE TABLE emp" + emp.getId()
                        + "(iD INT(64) NOT NULL AUTO_INCREMENT, "
                        + "CurrentDate DATE, " 
                        + "LoginTime  TIME, "  
                        + "LogoutTime  TIME, "
                        + "PRIMARY KEY(iD))";  
                    Statement statement = connect.createStatement();
                    statement.executeUpdate(myTable);
                }
                catch (SQLException e ) {}
                finally{
                    connect.close();
                }
            }
        }
        catch (Exception e){}
    }

    public static void clearLogFields(int iD){
        try{
            connection();
            Connection connect = DriverManager.getConnection(url, name , password);
            String query = "DROP TABLE emp"+iD;
            PreparedStatement preparedStmt = connect.prepareStatement(query);
            preparedStmt.execute();      

            String myTable = "CREATE TABLE emp" +  iD
                + "(iD INT(64) NOT NULL AUTO_INCREMENT, "
                + "CurrentDate DATE, " 
                + "LoginTime  TIME, "  
                + "LogoutTime  TIME, "
                + "PRIMARY KEY(iD))";  
            Statement statement = connect.createStatement();
            statement.execute(myTable);
            connect.close();

            connect = DriverManager.getConnection(url, name , password);
            query = "UPDATE employee_info  SET status, hours_worked = ?, ? WHERE Id_number = ?";
            preparedStmt = connect.prepareStatement(query);
            preparedStmt.setBoolean(1, false); //update the status row 

            preparedStmt.setDouble(2, 0.0);
            preparedStmt.setInt(3, iD); //Add to the last id/row
            preparedStmt.executeUpdate();  
            connect.close();
            LoadDatabase();
        }
        catch(Exception e){}
        LoadDatabase();
    }

    public static void deleteEmployee(int iD){
        connection();
        try{
            Connection connect = DriverManager.getConnection(url, name , password);
            if(isEmployee(iD)){
                String query = "DELETE FROM employee_info WHERE Id_number = ?";
                PreparedStatement preparedStmt = connect.prepareStatement(query);
                preparedStmt.setInt(1, iD);
                preparedStmt.execute();
                connect.close();                 
            }

            try{
                connection();
                connect = DriverManager.getConnection(url, name , password);
                String query = "DROP TABLE emp"+iD;
                PreparedStatement preparedStmt = connect.prepareStatement(query);
                preparedStmt.execute();
                connect.close();                 
            }
            catch(Exception e){}
            LoadDatabase();
        }
        catch(SQLException e){
            e.printStackTrace();
        }

    }

    public static void updateLogFields(Employee emp){
        connection();
        Statement st = null;
        ResultSet rs = null;
        try{    

            //try to establish a connection
            Connection connect = DriverManager.getConnection(url, name , password); 
            //Determine if employee exists 
            
            if(isEmployee(emp.getId())){
                
                if(emp.getStatus() == false){  //log in user if his last activity was loggin out               
                    String query = "INSERT INTO emp" +  emp.getId() +"(CurrentDate, LoginTime) VALUES (?, ?)";
                    //pass querry to database
                    PreparedStatement preparedStmt = connect.prepareStatement(query);
                    preparedStmt.setDate(1, new Date(new java.util.Date().getDate()));  
                    preparedStmt.setTime(2, new Time(new java.util.Date().getTime()));
                    preparedStmt.execute();
                    connect.close();

                    connect = DriverManager.getConnection(url, name , password); 
                    query = "UPDATE employee_info  SET status = ? WHERE Id_number = ?";
                    preparedStmt = connect.prepareStatement(query);
                    preparedStmt.setBoolean(1, true); //update the status row 
                    preparedStmt.setInt(2, emp.getId()); //Add to the last id/row
                    preparedStmt.executeUpdate();  
                    connect.close();

                    getListOfEmployees().get(getIndex(emp)).getDateLoged().add( new Date(new java.util.Date().getDate()));
                    getListOfEmployees().get(getIndex(emp)).getLoginTimes().add(new Time(new java.util.Date().getTime()));
                    getListOfEmployees().get(getIndex(emp)).setStatus(); //change status once employee loggs 
                }
                else{
                    try{   
                        int id = 0;
                        String query = "UPDATE emp" + emp.getId() + " SET LogoutTime = ? WHERE iD = ?";
                        PreparedStatement preparedStmt = connect.prepareStatement(query);
                        rs = preparedStmt.executeQuery("SELECT * FROM emp" + emp.getId() );
                        //Get the last row logged
                        while(rs.next()){
                            id = rs.getInt("iD");
                        }
                        //Add log out time to database
                        preparedStmt.setTime(1, new Time(new java.util.Date().getTime()));
                        preparedStmt.setInt(2, id); //Add to the last id/row
                        preparedStmt.executeUpdate();
                        connect.close();
                        
                        connect = DriverManager.getConnection(url, name , password); 
                        query = "UPDATE employee_info  SET status = ? WHERE Id_number = ?";
                        preparedStmt = connect.prepareStatement(query);
                        preparedStmt.setBoolean(1, false); //update the status row 
                        preparedStmt.setInt(2, emp.getId()); //Add to the last id/row
                        preparedStmt.executeUpdate();  
                        connect.close();
                        getListOfEmployees().get(getIndex(emp)).getLogoutTimes().add(new Time(new java.util.Date().getTime()));
                        getListOfEmployees().get(getIndex(emp)).setStatus(); //change status once employee loggs 
                    } 
                    catch(SQLException e){
                    }                     
                }   
                connect.close();
            }
        }catch(SQLException e){}
    }

    public static void updateEmployee(int key, Employee emp){
        connection();
        String column = "password, first_name, last_name, user_name, status";
        if(isEmployee(emp.getId())){
            try{
                Connection connect = DriverManager.getConnection(url, name , password);
                String query = "UPDATE employee_info SET (" + column + ") = ?, ?, ?, ?, ? WHERE Id_number = ?";
                PreparedStatement prepareStmt = connect.prepareStatement(query);            
                prepareStmt.setString(1, emp.getPassword());
                prepareStmt.setString(2, emp.getFName());
                prepareStmt.setString(3, emp.getLName());
                prepareStmt.setString(4, emp.getUserName());
                prepareStmt.setBoolean(5, emp.getStatus());

                prepareStmt.setInt(6,emp.getId());
                prepareStmt.executeUpdate();
                connect.close();
                getListOfEmployees().add(key, emp);
            }
            catch(SQLException e){}
        }
    }

    //Validate that the ID number belongs to an employee
    public static boolean isEmployee(int id){
        connection();
        Statement st = null;
        ResultSet rs = null;
        boolean exist = false;
        try{
            Connection connect = DriverManager.getConnection(url, name , password);
            st = connect.createStatement();
            rs = st.executeQuery("SELECT * FROM employee_info");
            while(rs.next()){
                if(id == rs.getInt("Id_number")){
                    exist = true; 
                    break;
                }                   
            }
            connect.close();
        }
        catch(SQLException e){}
        return exist;
    }   

    //Get a ist of all the employees 
    public static ArrayList<Employee> getListOfEmployees(){
        return listOfEmployees;
    }

    public static boolean LoadDatabase(){
        listOfEmployees=new ArrayList<Employee>();
        connection();
        Statement st = null;
        ResultSet rs = null;
        boolean exist = false;
        try{
            Connection connect = DriverManager.getConnection(url, name , password); 
            st = connect.createStatement();
            rs = st.executeQuery("SELECT * FROM employee_info");
            while(rs.next()){      
                //Add every row of data to a new employee list
                listOfEmployees.add(new Employee(rs.getString("first_name"), rs.getString("last_name"), rs.getString("user_name"), rs.getString("password"),rs.getInt("Id_number"), rs.getBoolean("status")));
            }
            connect.close();    
        }
        catch(SQLException e){}
        connection();
        try{
            Connection connect = DriverManager.getConnection(url, name , password);
            for(int i = 0; i<= listOfEmployees.size()-1; i++){
                st = connect.createStatement();
                rs = st.executeQuery("SELECT CurrentDate, LoginTime FROM emp" +listOfEmployees.get(i).getId());
                while(rs.next()){
                    listOfEmployees.get(i).getLoginTimes().add(rs.getTime("LoginTime"));
                    listOfEmployees.get(i).getDateLoged().add(rs.getDate("CurrentDate"));
                }  

                rs = st.executeQuery("SELECT LogoutTime FROM emp" +listOfEmployees.get(i).getId());
                while(rs.next()){
                    listOfEmployees.get(i).getLogoutTimes().add(rs.getTime("LogoutTime"));
                } 

            }
            connect.close(); 
            exist = true;
        } catch(SQLException e){ }
        return exist;
    }

    public static int getIndex(Employee emp){
        if(isEmployee(emp.getId())){
            for(int i = 0 ; i < getListOfEmployees().size() ; i ++){
                if(getListOfEmployees().get(i).getId() == emp.getId()){
                    return i;
                }
            }
        }
        return -1;       
    }

    //Connect to the database
    private static void connection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");           
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try{ 
            File f = new File("data.txt");
            Scanner scan = new Scanner(f);
            name =  scan.nextLine().trim();
            password = scan.nextLine().trim(); 
        }catch(Exception e){}
    }
}