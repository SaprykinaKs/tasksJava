import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LetterCounter {
    public static void main(String[] args) {
       
        // пути к файлам
        String inputPath = "input.txt";
        String outputPath = "output.txt";

        // читаем
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            System.out.println("Файл не найден: " + inputPath);
            return;
        }

        int[] uppercase = new int[26];  
        int[] lowercase = new int[26]; 

        // считаем буковки
        try (BufferedReader inp = new BufferedReader(new FileReader(inputFile))) {
            int let;
            while ((let = inp.read()) != -1) { // читается интом
                char ch = (char) let; // перекидываем в чар
                if (ch >= 'A' && ch <= 'Z') {
                    uppercase[ch - 'A']++;
                } else if (ch >= 'a' && ch <= 'z') {
                    lowercase[ch - 'a']++;
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка чтения " + e.getMessage());
        }

        // записываем
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (int i = 0; i < 26; i++) {
                writer.write((char) ('A' + i) + ": " + uppercase[i]);
                writer.newLine();
                writer.write((char) ('a' + i) + ": " + lowercase[i]);
                writer.newLine();
            }
            System.out.println("Результаты записаны в файл " + outputPath);
        } catch (IOException e) {
            System.out.println("Ошибка записи " + e.getMessage());
        }
    }
}