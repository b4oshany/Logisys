package OPERATIONS;

/**
 * Write a description of class Payroll here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Payroll 
{
    private static double rate;//normal pay rate
    private static double maxHrs;//total hours worked
    private static double overtime_rate;//overtime rate
    private static double totalHrs;//normal work hours
    private static double overtime_Hrs;//overtime
    private static double payee;//overtime
    Employee emp;

    public Payroll(Employee emp){
        this.emp = emp;
        rate = 300;//normal pay rate
        maxHrs = 40;//total hours worked
        overtime_rate = 450;//overtime rate
        int diffHours = 0; 
        int diffMinutes = 0;
        int diffSeconds = 0;
        try{
            for(int i = 0; i <= emp.getLogoutTimes().size()-1; i++){
                long diff = emp.getLogoutTimes().get(i).getTime() - emp.getLoginTimes().get(i).getTime();
                diffSeconds += diff / 1000 % 60;
                diffMinutes += diff / (60 * 1000) % 60;
                diffHours += diff / (60 * 60 * 1000);
            }
        }
        catch(Exception e){}
        totalHrs=diffHours+(diffMinutes/60.0) +(diffSeconds/60.0);
    }

    public static void setHourly_rate(double r){
        rate=r;
    }

    public static void setoverTimeRate(double or){
        overtime_rate=or;
    }

    public static double getTotalHrs(){
        return totalHrs;
    }

    public static double getPayee(){
        if (totalHrs <= maxHrs){
            payee = totalHrs * rate;
        }
        else{
            payee = (overtime_Hrs*overtime_rate)+(maxHrs*rate);
        }
        return payee;
    }

    public static double getOvertimeHrs(){
        if(totalHrs > maxHrs){
            overtime_Hrs=totalHrs - maxHrs;
        }
        else{
            overtime_Hrs=0.0;
        }
        return overtime_Hrs;
    }

    public String toString(){
        return "<<<<< |||||Employee name: " + emp.getFName() + " " + emp.getLName() + " ||||| >>>>>\n\n" +
        "Employee total hours worked: " + getTotalHrs() + "\n\n" +
        "Employee Pending requests: N/A" + "\n\n" +
        "Employee Over time horurs: " + getOvertimeHrs() + "\n\n" +
        "Employee PAYEE: $" +  getPayee() + "\n\n" +
        "Employee Last Loged in: " + emp.getDateLoged().get(emp.getDateLoged().size()-1) + " " +  emp.getLoginTimes().get(emp.getLoginTimes().size()-1) + "\n\n" +
        "Employee Last Loged out: " + emp.getLogoutTimes().get(emp.getLogoutTimes().size()-1);

    }
}
