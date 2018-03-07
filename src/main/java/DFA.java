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

        DFA dfa = new DFA();
        Transition currentTransition = null;


        //might need to change to be until there are no more next states left instead of for loop
        int y = 0;
        boolean inSameState = true;


        for (int x = 0; x < nfa.transitions.size(); x++) {
            //need it to completely restart to check all pairs again
            System.out.println("outside if statement " + nfa.transitions.get(x).prior + " " + nfa.transitions.get(x).next);
            if (nfa.transitions.get(x).prior == y) {
                System.out.println("inside if statement " + nfa.transitions.get(x).prior + " " + nfa.transitions.get(x).next);
                //want to keep same list if still one same state
                List<Integer> eclosureSet = new ArrayList<Integer>();
                //don't want to add in start state twice either
                eclosureSet.add(y);
                currentTransition = nfa.transitions.get(x);
                System.out.println("current transition " + currentTransition.prior + " " + currentTransition.next);
                if (currentTransition.label == 'e') {
                    eclosureSet.add(currentTransition.next);
                    //don't want to transition yet if there is more than 1 e per state
                    y = currentTransition.next;
                    System.out.print("eclosure set new " + eclosureSet);
                } else {
                    System.out.println(currentTransition.label);
                }
            }
        }
    }
}


