
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

class Taskmanager{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String TaskTitle = sc.nextLine();
        String TaskD = sc.nextLine();
        String TaskDD = sc.nextLine();
        System.out.println("What is Priority level ?");
        System.out.print("(P) for Priority and (N) for not a Priority : ");
        String Priority = sc.next();
        do {
            if(Priority.equalsIgnoreCase("p")||Priority.equalsIgnoreCase("n")){
                break;
            }
            else{
                System.out.println("Enter as specified");
                Priority = sc.next();
            }
        } while (true);
        
        
        
        
        
        
        tasks task1 = new tasks();
        task1.creation(TaskTitle, TaskD, TaskDD , Priority);
        System.out.println(task1);
        User U1 = new User();
        System.out.println("Enter user name : ");
        sc.nextLine();
       String Name = sc.nextLine(); 
        U1.Creation(Name);
        task1.AddUsers(U1,"Editor");
        System.out.println(task1.Users);
        task1.ChangeMainUser(U1);
        System.out.println(task1.Users);
        U1.CompleteTask(task1);
        task1.Display();
       
    }





}


class tasks{

    private String name;
    private String descript;
    private String DueDate;
    public String user;
    public String TaskPriority;
    public HashMap<User,String> Users = new HashMap<User,String>();
    private boolean completed;

    @Override
    public String toString(){
        // for print function
        return this.name;
    }
    // constructor
    public void creation(String title,String description,String date, String Priority) {
     // For proper construction of the task
        this.name = title;
        this.descript = description;
        this.DueDate = date;

        // By default task completion is false
        this.completed = false;

        // Whenever a new task is created we create a new user called Main user and we will give his prelivage as Admin //  
        User Admin = new User();
        Admin.Creation("Main User");
        this.Users.put(Admin,"Admin");

        //sets the priority of the task based on input
        if (Priority.equalsIgnoreCase("p")) {
            this.TaskPriority = "Priority";
        }
        else{
            this.TaskPriority = "Not a Priority";
        }
    }

    public void Display(){
        // For displaying all details of the task at once
        System.out.println("Task : " + this.name);
        System.out.println("description : " + this.descript);
        System.out.println("Due Date : " + this.DueDate);
        System.out.println("Main User : "+ this.user);
        System.out.println("Priority : " + this.TaskPriority);
        System.out.println("Users : " + this.Users);
        System.out.println("Task Status "+ this.Status());
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
    
    public void ChangeMainUser(User b){
        // Used to cange the main User
        Scanner sc = new Scanner(System.in);
        User old = AdminName();
        System.out.println("Do you want to transfer Admin privilage from " + old + " to " + b);
        System.out.println("(Y/N)");
        String check = sc.next();
        do {
            if(check.equalsIgnoreCase("Y") || check.equalsIgnoreCase("yes")){
                this.Users.put(b,"Admin");
                this.Users.put(old,"Co-editor");
                b.Setprivilage(this,"Admin");

                System.out.println("Main user is changed to "+ AdminName());
                break;
            }
            else if(check.equalsIgnoreCase("N") || check.equalsIgnoreCase("No")){
                System.out.println("Nothing is changed");
                break;
            }
            else{
                System.out.println("please enter correctly");
                System.out.println(check);
                check = sc.next();
            } 
        } while (true);
    }

    public void AddUsers(User a , String b){
        // Used to add users needs accepts two arguments a userobject and a privilage String
        this.Users.put(a,b);
        System.out.println(a + " Got assigned to "+ this.name+ " as "+ b);
        a.Setprivilage(this, b);
    }

    public void TaskCompletion(User a){
        if (!a.Tasks.get(this).equals("Admin")) {
            System.out.println("You need to be in Admin Privilege for setting a task completed");
        } else {
            this.completed = true;
            System.out.println("Your task has been closed. Hurray!!");
        }
             
    }

    public String Status(){
        if(this.completed == true){
            return "Completed";
        }return "Not Completed";
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
}