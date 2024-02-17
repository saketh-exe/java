import java.time.LocalDate;
import java.time.Period;

public class test {
    public static void main(String[] args) {
        // Assuming DueDate is a LocalDate variable representing the due date of some task
        LocalDate DueDate = LocalDate.of(2024, 2, 10); // Example due date

        // Calculate the period between DueDate and the current date
        Period period = Period.between(DueDate, LocalDate.now());

        // Print the period
        System.out.println("Period between DueDate and current date: " + period);
    }
}
