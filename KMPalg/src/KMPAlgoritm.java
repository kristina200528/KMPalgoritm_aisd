import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class KMPAlgoritm { 
    public static int k = 0;

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        int countFiles = random.nextInt(100) + 50;
        for (int i = 1; i < countFiles; i++) {
            try {
                FileWriter writer = new FileWriter("file.txt");
                generate(writer);
                BufferedReader reader = new BufferedReader(new FileReader("file.txt")); //читаем файл
                String text = reader.readLine();//основная строка
                String pattern = generateRandomString(1,10);//шаблон
                System.out.println();
                System.out.println("строка: " + text);
                System.out.println("подстрока: " + pattern);
                k=0;
                long startTime = System.nanoTime(); //время начала работы алгоритма
                int[] arrayPref = prefixFunction(pattern); // Массив префиксных значений
                ArrayList<Integer> kmpAlg = KMPSearch(text, pattern);
                long finalTime=System.nanoTime()-startTime;
                System.out.println("Массив префиксных значений: " + Arrays.toString(arrayPref));
                System.out.println("Результат: " + kmpAlg);
                System.out.println("Количество итераций: " + k);
                System.out.println();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Генерация случайной строки длиной от minLength до maxLength
    public static String generateRandomString(int minLength, int maxLength) {
        Random random = new Random();
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public static void generate(FileWriter writer) {
        String str1 = generateRandomString(100, 10000);
        String fileData = str1;
        try {
            writer.write(fileData);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // Функция для вычисления префиксной функции
    public static int[] prefixFunction(String pattern) { //принимающей на вход шаблонную строку
        int[] pref = new int[pattern.length()]; //создаем массив длиной шаблона
        for (int i = 1; i < pattern.length(); i++) { //идем по шаблону
            int j = 0;
            k++;
            while ((i + j) < pattern.length() && pattern.charAt(j) == pattern.charAt(i + j)) {
                k++;
                pref[i + j] = Math.max(pref[i + j], j + 1);
                j++;
            }
        }
        return pref;//возвращает массив префиксных значений
    }

    // Функция для поиска подстроки с использованием алгоритма KMP
    public static ArrayList<Integer> KMPSearch(String text, String pattern) {
        ArrayList<Integer> found = new ArrayList<>();
        int[] pref = prefixFunction(pattern);
        int i = 0; //позиция внутри текста
        int j = 0; //позиция внутри шаблона
        while (i < text.length()) {
            k++;
            if (text.charAt(i) == pattern.charAt(j)) {//Если текущие символы в тексте и шаблоне совпадают
                i++;// увеличиваем позиции в обоих строках.
                j++;
            }
            if (j == pattern.length()) { //Если все символы в шаблоне совпали с символами в тексте
                found.add(i - j); // добавляем позицию начала найденного вхождения в список found
                // обновляем позицию j согласно префиксной функции.
                j = pref[j - 1]; //позиция на которую нужно вернуться, чтобы не потерять наложенное вхождение
            } else if (i < text.length() && text.charAt(i) != pattern.charAt(j)) {//Если текущие символы не совпадают
                if (j != 0) { //не первый символ образца
                    j = pref[j - 1]; //возвращаемся на позицию в то место, с которого нужно начать поиск
                } else {
                    i++;
                }
            }
        }
        return found; //возвращаем список found, содержащий позиции найденных вхождений подстроки в тексте.
    }
}
