import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class Ingestion {


    private static String file;
    public static List<Character> alphabet = new ArrayList<Character>();
    private static char character;

    public static List<Character> alphabet() {
        try {
            System.out.println("Enter file name ");
            Scanner scanner = new Scanner(System.in);
            file = scanner.next();
            Scanner ingestedFile = new Scanner(new FileInputStream(file));
            while(ingestedFile.hasNextLine()) {
                String line = ingestedFile.nextLine();
                System.out.println("the line is " + line);
                for(int y = 0; y < line.length(); y++) {
                    character = line.charAt(y);
                    System.out.println("the char is " + character);
                    //NEED TO FIX TO EXCLUDE EMPTY STRING
                    if(!alphabet.contains(character) && !String.valueOf(character).equals("")) {
                        alphabet.add(character);
                        System.out.println(alphabet);
                    }
                }
            }
            System.out.println("the alphabet is " + alphabet);
            ingestedFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        return alphabet;
    }
}
