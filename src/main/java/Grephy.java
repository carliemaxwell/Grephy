import java.io.IOException;

public class Grephy {

    public static void main(String[] args) throws IOException {


        String regex = "";
        String file = "";
        String fileNameNFA = "";
        String fileNameDFA = "";

        if (args.length >= 2 && args.length <= 6) {
            if (args.length == 6) {
                file = args[5];
                regex = args[4];
                fileNameNFA = args[1];
                fileNameDFA = args[3];
            } else if (args.length == 4) {
                file = args[3];
                regex = args[2];
                if(args[0].equals("-n")) {
                    fileNameNFA = args[1];
                } else {
                    fileNameNFA = "/Users/carliemaxwell/GrephyFinalProject/src/main/Output/Output";
                }
                if(args[0].equals("-d")) {
                    fileNameDFA = args[1];
                } else {
                    fileNameDFA = "/Users/carliemaxwell/GrephyFinalProject/src/main/Output/Output";
                }
            } else if (args.length == 2) {
                file = args[1];
                regex = args[0];
                fileNameNFA = "/Users/carliemaxwell/GrephyFinalProject/src/main/Output/Output";
                fileNameDFA = "/Users/carliemaxwell/GrephyFinalProject/src/main/Output/Output";
            }
        } else {
            System.out.println("Acceptable parameters = [-n NFATransition] [-d DFA] Regex File");
        }

        System.out.println("regex is " + regex);

        //Creates alphabet used for DFA construction
        Ingestion.alphabet(file);
//
//        //Create basic NFAs to use methods on - testing
        NFA a = new NFA('a');
        NFA b = new NFA('b');
        NFA c = new NFA('c');
//
//        //Proper code for when readRegex is complete
        NFA nfa = NFATransition.readRegex(regex);
//        //Used for testing

        // ab | c
//        NFA nfa = NFATransition.union(NFATransition.concat(a,b), c);
//
//        //createDFA calls eclosure(nfa) - makes Hashmap of eclosures for subset construction
//        //Writer methods in main method bc need args params for fileName
        Writer.writeToFileNFA(nfa, fileNameNFA);
        Writer.writeToFileDFA(DFA.createDFA(nfa), fileNameDFA);
//
//        //createLine calls testDFA to check for/output accepted strings
        System.out.println("Accepted lines from the file: ");
        DFA.createLine(file);

    }
}
