package ulit;

import java.time.LocalDate;
// This util class created to correct Date for valid input type.
// Example:  2019-12-09 -> 09122019
public class DateCorrector {
    public static String correctDate(LocalDate invalidDate) {
        return invalidDate.toString().substring(8) +
                invalidDate.toString().substring(5, 7) + invalidDate.toString().substring(0, 4);
    }
}
