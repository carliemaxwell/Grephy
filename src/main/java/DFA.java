import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DFA {

    public List<Transition> dfaTransitions;
    public List<Integer> dfaStates = new ArrayList<Integer>();
    public int acceptingState;
    private static String file;
    //    public static String alphabet = "";
    public static List<String> alphabet = new ArrayList<String>();
    private static char character;


    //used for creating an empty NFA to combine 2 or more NFA's
    public DFA() {
        //NFA consists of a series of transitions and states
        dfaTransitions = new ArrayList<Transition>();
        dfaStates = new ArrayList<Integer>();
        //depends on first+secondSize-1
        this.acceptingState = 0;
    }


    public void addStates(int size) {
        for (int x = 0; x < size; x++) {
            this.dfaStates.add(x);
        }
    }

    public static List<List<Integer>> eclosure(NFA nfa) throws IOException {

        Transition currentTransition = null;
        List<List<Integer>> eclosureSetGlobal = new ArrayList<List<Integer>>();

        for(int a = 0; a < nfa.states.size(); a++) {
            List<Integer> eclosureSet = new ArrayList<Integer>();
            eclosureSet.add(a);
            for (int x = 0; x < nfa.transitions.size(); x++) {
                if (nfa.transitions.get(x).prior == a) {
                    currentTransition = nfa.transitions.get(x);
                    if (currentTransition.label == 'e') {
                        if(!eclosureSet.contains(currentTransition.next)) {
                            eclosureSet.add(currentTransition.next);
                        }
                    }
                }
            }
            eclosureSetGlobal.add(eclosureSet);
            System.out.println(eclosureSet);
        }
        System.out.println(eclosureSetGlobal);
        return eclosureSetGlobal;
    }


    public void transitionTable(NFA nfa, List<List<Integer>> globalEnclosures, String regex) throws IOException {

        //need to move bc want 1 list for all 3 unions
        List<Character> regexCharacters = Transition.returnRegexCharacters(regex);
        List<Integer> firstTransition = new ArrayList<Integer>();

        //move bc want one for each state + symbol
        //maybe make 2D array for a matrix?


        //needed incase you start with concat (won't be epsilon transition)
        for(int x = 0; x < globalEnclosures.size(); x++) {
            if(globalEnclosures.get(x).size() > 1) {
                firstTransition = globalEnclosures.get(x);
            }
        }


        for(int a = 0; a < regexCharacters.size(); a++) {
            //need to add to bigger thing bc this is individual for symbol + state
            List<Integer> returnTransition = new ArrayList<Integer>();
            //want to go through each state in list and find their mapping in nfa transitions
            for (int y = 0; y < firstTransition.size(); y++) {
                for (int z = 0; z < nfa.transitions.size(); z++) {
                    //find a list of 0's in nfa --> list of 1's --> list of 4's
                    if (nfa.transitions.get(z).prior == firstTransition.get(y)) {
                        //need to fix regexCharacters (maybe another loop)
                        if (nfa.transitions.get(z).label == 'e' ||
                                nfa.transitions.get(z).label == regexCharacters.get(a)) {
                            returnTransition.add(nfa.transitions.get(z).next);
                        }
                    }
                }
            }
        }
    }
}


