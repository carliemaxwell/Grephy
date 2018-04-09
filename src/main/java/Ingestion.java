import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class Ingestion {


    public static List<Character> alphabet = new ArrayList<Character>();


    public static void alphabet(String file) {
        try {
//            System.out.println("Enter file name ");
//            Scanner scanner = new Scanner(System.in);
//            file = scanner.next();
            Scanner ingestedFile = new Scanner(new FileInputStream(file));
            while(ingestedFile.hasNextLine()) {
                String line = ingestedFile.nextLine();
                for (int y = 0; y < line.length(); y++) {
                    if (y < line.length()) {
                        if (line.charAt(y) == '/') {
                            if (line.charAt(y + 1) == 'n') {
                                y++;
                            }
                        } else {
                            if (!alphabet.contains(line.charAt(y)) && line.charAt(y) != ' ') {
                                alphabet.add(line.charAt(y));
                            }
                        }
                    }
                }
            }
            System.out.println("the alphabet is " + alphabet);
            ingestedFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }
}
