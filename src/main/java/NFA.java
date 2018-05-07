import javafx.animation.Transition;

import java.util.*;

public class NFA {

    public List<NFATransition> transitions;
    public List<Integer> states = new ArrayList<Integer>();
    public int acceptingState;

    //used for creating an empty NFA to combine 2 or more NFA's
    public NFA() {
        //NFA consists of a series of transitions and states
        transitions = new ArrayList<NFATransition>();
        states = new ArrayList<Integer>();
        //depends on first+secondSize-1
        this.acceptingState = 0;
    }

    //used to create an initial NFA from a regex character
    public NFA(char symbol) {
        transitions = new ArrayList<NFATransition>();
        states = new ArrayList<Integer>();
        //create initial transition for single characters
        this.transitions.add(new NFATransition(0, 1, symbol));
        //a single transition includes 2 states when its for a single character
        this.addStates(2);
    }


    public void addStates(int size) {
        for(int x = 0; x < size; x++) {
            this.states.add(x);
        }
    }

    public void conAddTransitions(NFA con, NFA first, NFA second) {
        //put all of first into new DFA
        for (int x = 0; x < first.transitions.size(); x++) {
            con.transitions.add(first.transitions.get(x));
        }

        //update second NFA to be shifted right first.states.size()-1 bc needs to start at end of first states
        for (int x = 0; x < second.transitions.size(); x++) {
            con.transitions.add(new NFATransition(second.transitions.get(x).exit + first.states.size() - 1,
                    second.transitions.get(x).enter + first.states.size() - 1, second.transitions.get(x).label));
        }
    }

    public void unionAddTransitions(NFA unionNFA, NFA first, NFA second) {

            //shift first nfa up 1 and add to unionNFA
            for (int x = 0; x < first.transitions.size(); x++) {
                unionNFA.transitions.add(new NFATransition(first.transitions.get(x).exit + 1,
                        first.transitions.get(x).enter + 1, first.transitions.get(x).label));
            }

            //shift second nfa up 1 + first state amount and add to unionNFA
            for (int y = 0; y < second.transitions.size(); y++) {
                unionNFA.transitions.add(new NFATransition(second.transitions.get(y).exit + first.states.size() + 1,
                        second.transitions.get(y).enter + first.states.size() + 1, second.transitions.get(y).label));
            }

            //add beginning epsilons, need to shift first by 1 and second by amount in first+1
            unionNFA.transitions.add(new NFATransition(0, 1, 'ε'));
            unionNFA.transitions.add(new NFATransition(0, first.states.size() + 1, 'ε'));

            //add ending epsilons
            unionNFA.transitions.add(new NFATransition(first.states.size(), unionNFA.states.size() - 1, 'ε'));
            unionNFA.transitions.add(new NFATransition(unionNFA.states.size() - 2, unionNFA.states.size() - 1, 'ε'));

    }

    public void starAddTransitions(NFA starNFA, NFA nfa) {
        //copy over the original transitions
        for (int x = 0; x < nfa.transitions.size(); x++) {
            starNFA.transitions.add(new NFATransition(nfa.transitions.get(x).exit + 1, nfa.transitions.get(x).enter + 1, nfa.transitions.get(x).label));
        }

        //beginning to nfa epsilon transition
        starNFA.transitions.add(new NFATransition(0, 1, 'ε'));
        //beginning to end epsilon transition
        starNFA.transitions.add(new NFATransition(0, starNFA.states.size() - 1, 'ε'));
        //nfa end to nfa beginning epsilon transition
        starNFA.transitions.add(new NFATransition(nfa.states.size(), 1, 'ε'));
        //nfa to end epsilon transition
        starNFA.transitions.add(new NFATransition(nfa.states.size(), starNFA.states.size() - 1, 'ε'));
    }
}
