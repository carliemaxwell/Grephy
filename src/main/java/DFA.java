import java.io.*;
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

    public static void eclosure(NFA nfa) throws IOException {

        Transition currentTransition = null;

        for (int a = 0; a < nfa.states.size(); a++) {
            List<Integer> eclosureSet = new ArrayList<Integer>();
            eclosureSet.add(a);
            System.out.println("Current state " + a);
            for (int x = 0; x < nfa.transitions.size(); x++) {
                if (nfa.transitions.get(x).prior == a) {
                    currentTransition = nfa.transitions.get(x);
                    if (currentTransition.label == 'e') {
                        System.out.println("current transition being added " + currentTransition.prior + " " + currentTransition.next);
                        if (!eclosureSet.contains(currentTransition.next)) {
                            eclosureSet.add(currentTransition.next);
                        }
                    }
                }
            }
            //ADDS STATE W/ ECLOSURE TO HASHMAP TO BE USED FOR SUBSET CONSTRUCTION
            statesAndEclosures.put(eclosureSet.get(0), eclosureSet);
        }

        for (int y = nfa.states.size() - 1; y >= 0; y--) {
            List<Integer> eclosures = returnEclosureSet(y);
            //For state 7, return inside nums one by one and replace w/ eclosure
            for(int z =0; z < eclosures.size(); z++) {
                int num = eclosures.get(z);
                List<Integer> returnSet = returnEclosureSet(num);
                for(int i = 0; i < returnSet.size(); i++) {
                    if(!eclosures.contains(returnSet.get(i))) {
                        eclosures.add(returnSet.get(i));
                    }
                }
                statesAndEclosures.put(y, eclosures);
            }
        }
        System.out.println(statesAndEclosures);
    }

    public static List<Integer> returnEclosureSet(int state) {
        List eclosureOfState = statesAndEclosures.get(state);
        return eclosureOfState;
    }

    public DFA createDFA(NFA nfa) throws IOException {

        //NEED TO CALL TO CREATE HASHMAP OF STATES, ECLOSURES (use in returnEclosureSet)
        eclosure(nfa);

        DFA dfa = new DFA();

        //Get start state of NFA
        int startState = nfa.states.get(0);

        //Create list of all the states ahead of time and then run that list through the outer alphabet loop?
        for (int y = 0; y < Ingestion.alphabet.size(); y++) {
            char currentChar = Ingestion.alphabet.get(y);
            List<Integer> returnedStates = new ArrayList<Integer>();
            List<Integer> eclosure = returnEclosureSet(startState);
            //Want to make global list of all states in DFA
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
            if (returnedStates.isEmpty()) {
                //-1 = error state
                returnedStates.add(-1);
            }

            dfa.dfaTransitions.add(new DFATransition(eclosure, returnedStates, currentChar));

            //CURRENTLY ONLY RUNS THE NEW RETURNED SET FOR THE CHARACTER ITS ON AND NOT ALL OF THEM
            if (returnedStates != eclosure) {
                for (int q = 0; q < Ingestion.alphabet.size(); q++) {
                    currentChar = Ingestion.alphabet.get(q);
                    List<Integer> nextSubset = new ArrayList<Integer>();
                    for (int i = 0; i < returnedStates.size(); i++) {
                        for (int t = 0; t < nfa.transitions.size(); t++) {
                            if (nfa.transitions.get(t).prior == returnedStates.get(i)) {
                                if (nfa.transitions.get(t).label == currentChar) {
                                    if (!returnedStates.contains(nfa.transitions.get(i).next)) {
                                        List<Integer> eclosureOfNextState = returnEclosureSet(nfa.transitions.get(t).next);
                                        System.out.println("State is " + nfa.transitions.get(t).next +
                                                " eclosure is " + eclosureOfNextState);
                                        for (int a = 0; a < eclosureOfNextState.size(); a++) {
                                            nextSubset.add(eclosureOfNextState.get(a));
                                        }
                                    }
                                }
                            }
                        }
                        if (nextSubset.isEmpty()) {
                            //-1 = error state
                            nextSubset.add(-1);
                        }
                    }
                    dfa.dfaTransitions.add(new DFATransition(returnedStates, nextSubset, currentChar));
                }
            }
        }

        for(int b = 0; b < dfa.dfaTransitions.size(); b++) {
            System.out.println(dfa.dfaTransitions.get(b).prior + " " + dfa.dfaTransitions.get(b).next + " " + dfa.dfaTransitions.get(b).label);
        }
        return dfa;
    }
}



