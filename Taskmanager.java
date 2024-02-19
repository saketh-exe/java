import java.io.BufferedReader;
import java.io.File;
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

        User a = AddNewUser(task1);

        System.out.println(task1.Users);

        System.out.println(task1.Users);

        task1.Display();

        ChangeAdmin(task1,a);
        System.out.println(task1.Users);


       SaveToCsv(task1);
        Display(task1);

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
        Noti(task1);
       sc.close();
        return task1;
    }

    public static User AddNewUser(tasks t ){
        Scanner sc = new Scanner(System.in);
        System.out.println("Name of the User: ");
        String a = sc.nextLine();
        System.out.println("Name of the Desigination: ");
        String D = sc.nextLine();
        User U1 = new User();
        U1.Creation(a);
        t.AddUsers(U1, D);
        sc.close();
        return U1;
    }

    public static void GetMainUser(tasks t){
        t.AdminName();
    }

    public static void ChangeAdmin(tasks t , User a){
        t.ChangeMainUser(a);
    }

    public static void ChangeStateofTask(tasks t , User a){
        a.CompleteTask(t);
    }

    public static void RemoveUser(tasks t , User a, User b){
        b.RemoveUser(a,t);
    }

    public static void SaveToCsv(tasks t){
    t.WritetoCSV();
}

    public static void Display(tasks a){
    a.DisplayfromCSV();
}

    public static void Noti(tasks a){

        int t = (a.TimeLeft());
        if(t < 0 ){

                System.out.println("ONLY "+Math.abs(t)+" DAYS LEFT");

        }else if(t == 0){
            System.out.println("DueDate Reached");
            a.completed = true;
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
    boolean completed;

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

    void Display() {
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

                System.out.println("Admin is changed to " + AdminName());
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
        sc.close();
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
//
    private String Status() {
        if (this.completed == true) {
            return "Completed";
        }
        return "Not Completed";
    }

    public void RemoveUser(User a,User b) {
        if (!b.Tasks.get(this).equals("Admin")) {
            System.out.println("You need to be in Admin Privilege for removing Another User");
        } else {
            this.Users.remove(a);
            a.RemoveTask(this);
            System.out.println("the user " + a + " is removed");
        }

    }
 //  
    public int TimeLeft(){


        Period period = Period.between(this.DueDate, LocalDate.now());

            if(period.getDays()<=0){
            return period.getDays();
            }return 0;

    }

    public void WritetoCSV(){

        try {
            String filePath = "DATA.csv"; // Path to your CSV file

            File file = new File(filePath);

            // Check if the file already exists
            boolean fileExists = file.exists();


            FileWriter f = new FileWriter("DATA.csv" , true);

            if (!fileExists) {
                f.write("Task Name:,Task Description:,Due Date:,Admin:,State:,Priority:,TimeLeft:\n");
            }

            // Write data
            f.write(this.name + "," + this.descript + "," + this.DueDate + "," + this.AdminName() + "," + this.Status() + ","+this.TaskPriority+","+Math.abs(TimeLeft())+"\n");
            f.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

   public void DisplayfromCSV() {
    String file = "DATA.csv";
    BufferedReader reader = null;
    String line = "";

    try {
        reader = new BufferedReader(new FileReader(file));

        while ((line = reader.readLine()) != null) {
            String[] row = line.split(",");
            for (String index : row) {
                System.out.printf("%-20s", index);
            }
            System.out.println();
        }
    } catch (Exception e) {
        System.out.println("Something went wrong: " + e.getMessage());
    } finally {
        try {
            if (reader != null)
                reader.close();
        } catch (IOException e) {
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
    void RemoveUser(User a, tasks b){
        b.RemoveUser(a,this);
    }
}


// 3) create a new program for displaying 