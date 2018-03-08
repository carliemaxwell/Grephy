import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.*;

public class Ingestion {


    private static String file;
//    public static String alphabet = "";
    List<String> alphabet = new ArrayList<String>();
    private static char character;

    public List<String> alphabet() {
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
                    if(!this.alphabet.contains(character)) {
                        this.alphabet.add(String.valueOf(character));
                    }
//                    System.out.println("INDEX FOUND EQUALS " + indexFound);
                    //WANT INDEX FOUND TO BE -1 BC THAT MEANS THE CHARACTER ISN'T IN THE ALPHABET YET
                    //DON'T WANT TO COUNT A SPACE IN THE TEXT AS PART OF THE ALPHABET

                    //MAY NOT NEED DEPENDING ON IF WE WANT CHARACTERS OR A PATTERN
//                    if(indexFound == -1 && !Character.isWhitespace(character)) {
//                        System.out.println("entering indexFound if statement");
//                        alphabet.character;
//                    }
                }
            }
            System.out.println("the alphabet is " + this.alphabet);
            ingestedFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
        return this.alphabet;
    }
}
