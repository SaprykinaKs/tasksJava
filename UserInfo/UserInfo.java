import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserInfo {
    public static void main(String[] args) {

        String fullName;
        String bDate;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите ФИО: ");
            fullName = scanner.nextLine().trim();

            System.out.print("Введите дату рождения (дд.мм.гггг или дд/мм/гггг): ");
            bDate = scanner.nextLine().trim();
        }

        try {
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("[dd.MM.yyyy][dd/MM/yyyy]");
            LocalDate currDate = LocalDate.now();
            LocalDate bDatePatt = LocalDate.parse(bDate, pattern);
            int age = currDate.getYear() - bDatePatt.getYear();

            if (bDatePatt.plusYears(age).isAfter(currDate)) {
                age--;
            }

            if (age < 0){
                System.out.println("неправильный формат даты");
                return;
            }

            String[] names = fullName.split("\\s+");
            if (names.length < 3) {
                System.out.println("введите полное ФИО");
                return;
            }

            String lastName = names[0];
            String firstName = names[1];
            String patronymic = names[2];
            String initials = lastName + " " + firstName.charAt(0) + "." + patronymic.charAt(0) + ".";

            String gen;
            if (patronymic.endsWith("ич")) {
                gen = "М";
            } else if (patronymic.endsWith("на")) {
                gen = "Ж";
            } else {
                gen = "ОПРЕДЕЛИТЬ_НЕ_УДАЛОСЬ";
            }

            String ageEnd;
            if (age % 10 == 1 && age % 100 != 11) {
                ageEnd = "год";
            } else if (age % 10 >= 2 && age % 10 <= 4 && (age % 100 < 10 || age % 100 >= 20)) {
                ageEnd = "года";
            } else {
                ageEnd = "лет";
            }

            System.out.println("Инициалы: " + initials);
            System.out.println("Пол: " + gen);
            System.out.println("Возраст: " + age + " " + ageEnd);

        } catch (DateTimeParseException e) {
            System.out.println("неправильный формат даты");
        }
    }
}