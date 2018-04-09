import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DFATransition {

    public static List<DFA> dfaTransitions;
    public static List<Integer> dfaStates = new ArrayList<Integer>();
    public static List<List<Integer>> acceptingState;
    public static HashMap<Integer, List> statesAndEclosures = new HashMap<Integer, List>();
    public static List<List<Integer>> newStates = new ArrayList<>();


    //used for creating an empty NFA to combine 2 or more NFA's
    public DFATransition() {
        //NFA consists of a series of transitions and states
        dfaTransitions = new ArrayList<DFA>();
        dfaStates = new ArrayList<Integer>();
        //depends on first+secondSize-1
        this.acceptingState = new ArrayList<>();
    }

    public static void eclosure(NFA nfa) throws IOException {

        NFATransition currentTransition = null;

        for (int a = 0; a < nfa.states.size(); a++) {
            List<Integer> eclosureSet = new ArrayList<Integer>();
            eclosureSet.add(a);
            for (int x = 0; x < nfa.transitions.size(); x++) {
                if (nfa.transitions.get(x).prior == a) {
                    currentTransition = nfa.transitions.get(x);
                    if (currentTransition.label == 'ε') {
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
        System.out.println("Hashmap of states w/ eclosures " + statesAndEclosures);
    }


    public static List<Integer> returnEclosureSet(int state) {
        List eclosureOfState = statesAndEclosures.get(state);
        return eclosureOfState;
    }


    public static void findSubset(NFA nfa, List<Integer> eclosure) {

        //Create list of all the states ahead of time and then run that list through the outer alphabet loop?
        for (int y = 0; y < Ingestion.alphabet.size(); y++) {

            char currentChar = Ingestion.alphabet.get(y);
            List<Integer> newSubset = new ArrayList<Integer>();

            //Want to make global list of all states in DFATransition
            for (int x = 0; x < eclosure.size(); x++) {
                //Check if the transitions have that label
                for (int z = 0; z < nfa.transitions.size(); z++) {
                    if (nfa.transitions.get(z).prior == eclosure.get(x)) {
                        if (nfa.transitions.get(z).label == currentChar) {
                            if (!newSubset.contains(nfa.transitions.get(x).next)) {
                                List<Integer> eclosureOfNextState = returnEclosureSet(nfa.transitions.get(z).next);
                                for (int a = 0; a < eclosureOfNextState.size(); a++) {
                                    newSubset.add(eclosureOfNextState.get(a));
                                }
                            }
                        }
                    }
                }
            }

            if (newSubset.isEmpty()) {
                //-1 = error state
                newSubset.add(-1);
            }

            dfaTransitions.add(new DFA(eclosure, newSubset, currentChar));


            if (newSubset.contains(nfa.acceptingState)) {
                if (!acceptingState.contains(newSubset)) {
                    acceptingState.add(newSubset);
                }
            }

            if (eclosure.contains(nfa.acceptingState)) {
                if (!acceptingState.contains(eclosure)) {
                    acceptingState.add(eclosure);
                }
            }
            if(!newStates.contains(newSubset)) {
                newStates.add(newSubset);
            }
        }
    }




    public static DFATransition createDFA(NFA nfa) throws IOException {


        //NEED TO CALL TO CREATE HASHMAP OF STATES, ECLOSURES (use in returnEclosureSet)
        eclosure(nfa);

        DFATransition dfa = new DFATransition();

        List<Integer> eclosure = new ArrayList<>();

        //Get start state of NFA
        int startState = nfa.states.get(0);

        //Create list of all the states ahead of time and then run that list through the outer alphabet loop?
        for (int y = 0; y < Ingestion.alphabet.size(); y++) {
            char currentChar = Ingestion.alphabet.get(y);
            List<Integer> returnedStates = new ArrayList<Integer>();
            eclosure = returnEclosureSet(startState);

            //Want to make global list of all states in DFATransition
            for (int x = 0; x < eclosure.size(); x++) {
                //Check if the transitions have that label
                for (int z = 0; z < nfa.transitions.size(); z++) {
                    if (nfa.transitions.get(z).prior == eclosure.get(x)) {
                        if (nfa.transitions.get(z).label == currentChar) {
                            if (!returnedStates.contains(nfa.transitions.get(x).next)) {
                                List<Integer> eclosureOfNextState = returnEclosureSet(nfa.transitions.get(z).next);
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


            if(!newStates.contains(eclosure)) {
                newStates.add(eclosure);
            }

            if(!newStates.contains(returnedStates)) {
                newStates.add(returnedStates);
            }


            dfaTransitions.add(new DFA(eclosure, returnedStates, currentChar));


            if(returnedStates.contains(nfa.acceptingState)) {
                if(!acceptingState.contains(returnedStates)) {
                    acceptingState.add(returnedStates);
                }
            }

            if(eclosure.contains(nfa.acceptingState)) {
                if(!acceptingState.contains(eclosure)) {
                    acceptingState.add(eclosure);
                }
            }

        }


        //add to newStates in findSubset ends up doing all of the states eventually
        for(int g = 1; g < newStates.size(); g++) {
            System.out.println("new state size " + newStates.size());
            System.out.println(g);
            findSubset(nfa, newStates.get(g));
        }


        System.out.println("DFATransitions:");
        for(int b = 0; b < dfaTransitions.size(); b++) {
            System.out.println(dfaTransitions.get(b).prior + " " + dfa.dfaTransitions.get(b).next + " " + dfa.dfaTransitions.get(b).label);
        }

        Writer.writeToFileDFA(dfa);
        return dfa;
    }


    public static void createLine(Scanner ingestedFile) {
        String allChar = "";
        //Need to fix to get from file (testing for now)
        while(ingestedFile.hasNextLine()) {
            String line = ingestedFile.nextLine();
            for(int y = 0; y < line.length(); y++) {
                char character = line.charAt(y);
                allChar += character;
            }
        }
        ingestedFile.close();
        String[] lines = allChar.split("/n");
        for (String line : lines) {
            System.out.println(line);
            testDFA(line);
        }
    }

    public static void testDFA(String line) {
        String acceptOrReject = "";

        List<Integer> startState;

        List<List<Integer>> statesIncluded = new ArrayList<>();

        int y = 0;
        startState = dfaTransitions.get(0).prior;
        statesIncluded.add(startState);
        for (int x = 0; x < dfaTransitions.size(); x++) {
            if (dfaTransitions.get(x).prior == startState) {
                System.out.println(dfaTransitions.get(x).prior + " " + dfaTransitions.get(x).next);
                if(y < line.length()) {
                    if (dfaTransitions.get(x).label == line.charAt(y)) {
                        //move on to next character
                        y++;
                        //find next transition w/ next symbol
                        startState = dfaTransitions.get(x).next;
                        statesIncluded.add(startState);
                    }
                }
            }
        }

        System.out.println("All states " + statesIncluded);

        if(acceptingState.contains(statesIncluded.get(statesIncluded.size()-1))) {
            acceptOrReject = "accept";
        } else {
            acceptOrReject = "reject";
        }

        System.out.println(acceptOrReject);
    }
}





