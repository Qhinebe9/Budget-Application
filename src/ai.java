import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ai {
    public static void main(String[] args) {
        // Get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        // Print in default format
        System.out.println("Current Date and Time: " + currentDateTime);

        // To format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        System.out.println("Formatted Date and Time: " + formattedDateTime);
    }
}
