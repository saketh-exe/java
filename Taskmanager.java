import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

class Taskmanager {

    public static void main(String[] args) {
        tasks task1 = CreateTask();
        System.out.println(task1);

        AddNewUser(task1, "maya", "editor");
        
        System.out.println(task1.Users);
        
        System.out.println(task1.Users);
        
        task1.Display();
        
        System.out.println(task1.Users);
       
        Noti(task1);
        task1.WritetoCSV();
        task1.DisplayfromCSV();
        
    }



    public static tasks CreateTask(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Task Title: ");
        String TaskTitle = sc.nextLine();
        System.out.println("Enter Task Description: ");
        String TaskD = sc.nextLine();

        System.out.println("Please enter a date for the task (YYYY-MM-DD): ");
        LocalDate TaskDD = LocalDate.parse(sc.nextLine());

        System.out.println("What is Priority level ?");
        System.out.print("(P) for Priority and (N) for not a Priority : ");
        String Priority = sc.next();
        do {
            if (Priority.equalsIgnoreCase("p") || Priority.equalsIgnoreCase("n")) {
                break;
            } else {
                System.out.println("Enter as specified");
                Priority = sc.next();
            }
        } while (true);

        tasks task1 = new tasks();
        task1.creation(TaskTitle, TaskD, TaskDD, Priority);

        System.out.println("Task has been created");
        return task1;
    }

    public static void AddNewUser(tasks t , String a,String D){
        Scanner sc = new Scanner(System.in);
        User U1 = new User();
        U1.Creation(a);
        t.AddUsers(U1, D);
    }

    public static void ChangeMainUser(tasks t , User a){
        t.ChangeMainUser(a);
    }

    public static void ChangeStateofTask(tasks t , User a){
        a.CompleteTask(t);
    }

    public static void RemoveUser(tasks t , User a){
        t.RemoveUser(a);
    }

    public static void Noti(tasks a){

        int t = (a.TimeLeft().getDays());
        if(t < 0 ){
            if(t>-3){
                System.out.println("ONLY "+Math.abs(t)+" DAYS LEFT");
            }else{

            }
        }else if(t == 0){
            System.out.println("Submission is Today");
        }else{
            System.out.println("You are Past the due date");
        }
    }
}

    
class tasks {

    private String name;
    private String descript;
    private LocalDate DueDate;
    public String user;
    public String TaskPriority;
    public HashMap<User, String> Users = new HashMap<>();
    private boolean completed;

    @Override
    public String toString() {
        // for print function
        return this.name;
    }

    // constructor
    public void creation(String title, String description, LocalDate date, String Priority) {
        // For proper construction of the task
        this.name = title;
        this.descript = description;
        this.DueDate = date;

        // By default task completion is false
        this.completed = false;

        // Whenever a new task is created we create a new user called Main user and we will give his prelivage as Admin //  
        User Admin = new User();
        Admin.Creation("Main User");
        this.Users.put(Admin, "Admin");

        //sets the priority of the task based on input
        if (Priority.equalsIgnoreCase("p")) {
            this.TaskPriority = "Priority";
        } else {
            this.TaskPriority = "Not a Priority";
        }
    }

    public void Display() {
        // For displaying all details of the task at once
        System.out.println("Task : " + this.name);
        System.out.println("description : " + this.descript);
        System.out.println("Due Date : " + this.DueDate);
        System.out.println("Main User : " + this.user);
        System.out.println("Priority : " + this.TaskPriority);
        System.out.println("Users : " + this.Users);
        System.out.println("Task Status " + this.Status());
    }

    public User AdminName() {
        // Used to get the current User with Admin Privilage
        for (Entry<User, String> entry : this.Users.entrySet()) {
            if (entry.getValue().equals("Admin")) {
                return entry.getKey();
            }
        }
        return null; // If valueToFind is not found in the map
    }

    public void ChangeMainUser(User b) {
        // Used to cange the main User
        Scanner sc = new Scanner(System.in);
        User old = AdminName();
        System.out.println("Do you want to transfer Admin privilage from " + old + " to " + b);
        System.out.println("(Y/N)");
        String check = sc.next();
        do {
            if (check.equalsIgnoreCase("Y") || check.equalsIgnoreCase("yes")) {
                this.Users.put(b, "Admin");
                this.Users.put(old, "Co-editor");
                b.Setprivilage(this, "Admin");

                System.out.println("Main user is changed to " + AdminName());
                break;
            } else if (check.equalsIgnoreCase("N") || check.equalsIgnoreCase("No")) {
                System.out.println("Nothing is changed");
                break;
            } else {
                System.out.println("please enter correctly");
                System.out.println(check);
                check = sc.next();
            }
        } while (true);
    }

    public void AddUsers(User a, String b) {
        // Used to add users need to accept two arguments a user object and a privilage String
        this.Users.put(a, b);
        System.out.println(a + " Got assigned to " + this.name + " as " + b);
        a.Setprivilage(this, b);
    }

    public void TaskCompletion(User a) {
        if (!a.Tasks.get(this).equals("Admin")) {
            System.out.println("You need to be in Admin Privilege for setting a task completed");
        } else {
            this.completed = true;
            System.out.println("Your task has been closed. Hurray!!");
        }
    }

    public String Status() {
        if (this.completed == true) {
            return "Completed";
        }
        return "Not Completed";
    }

    public void RemoveUser(User a) {
        this.Users.remove(a);
        a.RemoveTask(this);
        System.out.println("the user " + a + " is removed");

    }
    public Period TimeLeft(){
        
       
        Period period = Period.between(this.DueDate, LocalDate.now());
            
        
      return period;
    }

    public void WritetoCSV(){

        try {
            FileWriter f = new FileWriter("DATA.csv");

            f.write("Task Name:,Task Description:,Due Date:,Users,State:\n"+this.name+","+this.descript+","+this.DueDate+","+this.Users+","+this.completed);
            f.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void DisplayfromCSV(){
        String file = "DATA.csv";
        BufferedReader reader = null;
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                
                String[] row = line.split(",");

                for(String index : row){
                    System.out.printf("%-10s",index);
                }
                System.out.println();
            }
        } catch (Exception e) {
           System.out.println("smt happened");
        }finally{
            try {
                reader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}


class User{
    public String UserName;
    public HashMap<tasks,String> Tasks = new HashMap<tasks,String>();


    public void Creation(String a){
        // for creating a user
        this.UserName = a;
    }

    public void Assign(tasks b, String x){
        // will add this user to a specified task
        b.AddUsers(this,x);
    }
   
    @Override
    public String toString(){
        // for print function
        return this.UserName;
    }

    public void Setprivilage(tasks a , String b){
        this.Tasks.put(a, b);
    }

    public void CompleteTask(tasks a){
        a.TaskCompletion(this);
    }
    public void RemoveTask(tasks a){
        this.Tasks.remove(a);
    }
}
// 1) figure out how to add new tasks 
// 2) correct that dict reading part 
// 3) create a new program for displaying 