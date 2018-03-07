import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DFA {

    public List<Transition> dfaTransitions;
    public List<Integer> dfaStates = new ArrayList<Integer>();
    public int acceptingState;

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

    public static void eclosure(NFA nfa) throws IOException {

        Transition currentTransition = null;
        //might need to change to be until there are no more next states left instead of for loop

        List<List<Integer>> eclosureSetGlobal = new ArrayList<List<Integer>>();

        for(int a = 0; a < nfa.states.size(); a++) {
            List<Integer> eclosureSet = new ArrayList<Integer>();
            eclosureSet.add(a);
            for (int x = 0; x < nfa.transitions.size(); x++) {
                //need it to completely restart to check all pairs again
                if (nfa.transitions.get(x).prior == a) {
                    //want to keep same list if still one same state
                    //don't want to add in start state twice either
                    currentTransition = nfa.transitions.get(x);
                    if (currentTransition.label == 'e') {
                        eclosureSet.add(currentTransition.next);
                    }
                }
            }
            eclosureSetGlobal.add(eclosureSet);
            System.out.println(eclosureSet);
        }
        System.out.println(eclosureSetGlobal);
    }
}


