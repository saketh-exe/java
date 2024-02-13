
public class test {

    private int ssn;
    private String empname;
    private int empage;
    
    public int getempssh(){
        return ssn;
    }
    public String getempname(){
        return empname;
    }
    public int getempage(){
        return empage;
    }

    public void setempage(int a){
        empage = a;
    }
    public void setempname(String b){
        empname = b;
    }
    public void setempssh(int a){
        ssn = a;
    }
    public static void main(String[] args) {

       test t = new test();

       t.setempage(0);
       t.setempname("dave");
       t.setempssh(0);

        System.out.println(t.getempage());
        System.out.println(t.getempname());
        System.out.println(t.getempssh());
    }
}
