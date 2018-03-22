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
//        List<Integer> eclosure = returnEclosureSet(startState);

        //Create list of all the states ahead of time and then run that list through the outer alphabet loop?

        for (int y = 0; y < Ingestion.alphabet.size(); y++) {
            char currentChar = Ingestion.alphabet.get(y);
            List<Integer> returnedStates = new ArrayList<Integer>();
            List<Integer> eclosure = returnEclosureSet(startState);
            //want to make global list of all states in DFA
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


    public static void writeToFileDFA(DFA dfa) throws IOException {
        BufferedWriter bw = null;
        try {
            String dotLanguage = "digraph graphname { ";
            File file = new File("/Users/carliemaxwell/GrephyFinalProject/src/main/Output/Output");
            FileWriter fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write(dotLanguage);
            bw.newLine();
            for(int x=0; x< dfa.dfaTransitions.size(); x++) {

                StringBuilder builder2 = new StringBuilder();
                for (int m = 0; m < dfa.dfaTransitions.get(x).next.size(); m++) {
                    builder2.append( dfa.dfaTransitions.get(x).next.get(m));
                }
                String result2 = builder2.toString();

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < dfa.dfaTransitions.get(x).prior.size(); i++) {
                    builder.append( dfa.dfaTransitions.get(x).prior.get(i));
                }
                String result = builder.toString();



                System.out.println("next result " + result2);

                //NEED TO FIX TO BE A STRING FOR EACH STATE
                bw.write(result + "->" + result2 + "[label=" + dfa.dfaTransitions.get(x).label + "];");
                bw.newLine();
            }
            bw.write("}");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (Exception ex) {
                System.out.println("Error");
            }
        }
    }
}



