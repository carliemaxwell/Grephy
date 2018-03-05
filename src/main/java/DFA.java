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


    public static DFA eclosure(NFA nfa) throws IOException {
        //eclosure = stateItself --> next state reached by E --> next state reached by E ect.
        DFA dfa = new DFA();
        boolean noEClosure = false;
        List<String> dfaStates = new ArrayList<String>();

        for(int x = 0; x < nfa.transitions.size(); x++) {
            if(nfa.transitions.get(x).label != 'e') {
                noEClosure = true;
            } else {
                noEClosure = false;
            }
        }

        if(noEClosure == true) {
            //If there are no epsilons it will be the same bc Thompson's construction doesn't have multiple transitions per symbol
            Transition.writeToFile(nfa);
        } else {
            //Find eclosures for NFA
            for (int x = 0; x < nfa.transitions.size(); x++) {
                if (nfa.transitions.get(x).label != 'e') {
                    dfaStates.add(String.valueOf(nfa.transitions.get(x).next));
                } else {
                    dfaStates.add(String.valueOf(nfa.transitions.get(x).prior + "," + nfa.transitions.get(x).next));
                }
                System.out.println("DFA state = " + dfaStates.get(x));
            }
        }
        return dfa;
    }
}
