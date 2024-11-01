// просто генерит буковки в input.txt для теста

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class LetterGenerator {
    public static void main(String[] args) {
        String outputFilePath = "input.txt";
        Random random = new Random();
        int count = 250;
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (int i = 0; i < count; i++) {
                char let;
                if (random.nextBoolean()) {
                    let = (char) ('A' + random.nextInt(26));
                } else {
                    let = (char) ('a' + random.nextInt(26));
                }
                writer.write(let);
            }
            System.out.println("Буквы успешно записаны в файл " + outputFilePath);
        } catch (IOException e) {
            System.out.println("Ошибка записи " + e.getMessage());
        }
    }
}
