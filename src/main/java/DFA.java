import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DFA {

    public List<DFATransition> dfaTransitions;
    public List<Integer> dfaStates = new ArrayList<Integer>();
    public List<Integer> acceptingState;
    public static HashMap<Integer, List> statesAndEclosures = new HashMap<Integer, List>();


    //used for creating an empty NFA to combine 2 or more NFA's
    public DFA() {
        //NFA consists of a series of transitions and states
        dfaTransitions = new ArrayList<DFATransition>();
        dfaStates = new ArrayList<Integer>();
        //depends on first+secondSize-1
        this.acceptingState = new ArrayList<Integer>();
    }


    public void addStates(int size) {
        for (int x = 0; x < size; x++) {
            this.dfaStates.add(x);
        }
    }

    public static List<List<Integer>> eclosure(NFA nfa) throws IOException {

        Transition currentTransition = null;
        List<List<Integer>> eclosureSetGlobal = new ArrayList<List<Integer>>();

        for (int a = 0; a < nfa.states.size(); a++) {
            List<Integer> eclosureSet = new ArrayList<Integer>();
            eclosureSet.add(a);
            for (int x = 0; x < nfa.transitions.size(); x++) {
                if (nfa.transitions.get(x).prior == a) {
                    currentTransition = nfa.transitions.get(x);
                    if (currentTransition.label == 'e') {
                        if (!eclosureSet.contains(currentTransition.next)) {
                            eclosureSet.add(currentTransition.next);
                        }
                    }
                }
            }
            statesAndEclosures.put(eclosureSet.get(0), eclosureSet);
            eclosureSetGlobal.add(eclosureSet);
            System.out.println(eclosureSet);
        }
        System.out.println(statesAndEclosures);
        return eclosureSetGlobal;
    }

    public static List<Integer> returnEclosureSet(int state) {
        List eclosureOfState = statesAndEclosures.get(state);
        return eclosureOfState;
    }

    public static DFA createDFA(NFA nfa) {

        DFA dfa = new DFA();

        //Get start state of NFA
        int startState = nfa.states.get(0);

        //Get eclosure of start state to begin constructing transitions
        List<Integer> eclosure = returnEclosureSet(startState);

        System.out.println("Start state eclosure is " + eclosure);

        for (int y = 0; y < Ingestion.alphabet.size(); y++) {
            //Get the first symbol in the alphabet
            char currentChar = Ingestion.alphabet.get(y);
            System.out.println("current character is " + currentChar);
            List<Integer> returnedStates = new ArrayList<Integer>();
            //Get each value in the eclosure set
            for (int x = 0; x < eclosure.size(); x++) {
                System.out.println("Current value in eclosure set " + eclosure.get(x));
                //Check if the transitions have that label
                for (int z = 0; z < nfa.transitions.size(); z++) {
                    if (nfa.transitions.get(z).prior == eclosure.get(x)) {
                        if (nfa.transitions.get(z).label == currentChar) {
                            if (!returnedStates.contains(nfa.transitions.get(x).next)) {
                                List<Integer> eclosureOfNextState = returnEclosureSet(nfa.transitions.get(z).next);
                                System.out.println("State is " + nfa.transitions.get(z).next + " eclosure is " + eclosureOfNextState);
                                for (int a = 0; a < eclosureOfNextState.size(); a++) {
                                    returnedStates.add(eclosureOfNextState.get(a));
                                }
                            }
                        }
                    }
                }
            }
            if(returnedStates.isEmpty()) {
                //-1 = error state
                returnedStates.add(-1);
            }
            dfa.dfaTransitions.add(new DFATransition(eclosure, returnedStates, currentChar));
        }
        for(int b = 0; b < dfa.dfaTransitions.size(); b++) {
            System.out.println(dfa.dfaTransitions.get(b).prior + " " + dfa.dfaTransitions.get(b).next + " " + dfa.dfaTransitions.get(b).label);
        }
        return dfa;
    }
}



