import java.util.*;

public class NFA {

    public List<Transition> transitions;
    public List<Integer> states = new ArrayList<Integer>();
    public int acceptingState;

    //used for creating an empty NFA to combine 2 or more NFA's
    public NFA() {
        //NFA consists of a series of transitions and states
        transitions = new ArrayList<Transition>();
        states = new ArrayList<Integer>();
        //depends on first+secondSize-1
        this.acceptingState = 0;
    }

    //used to create an initial NFA from a regex character
    public NFA(char symbol) {
        transitions = new ArrayList<Transition>();
        states = new ArrayList<Integer>();
        //create initial transition for single characters
        this.transitions.add(new Transition(0, 1, symbol));
        //a single transition includes 2 states when its for a single character
        this.addStates(2);
    }

    public void addStates(int size) {
        for(int x = 0; x < size; x++) {
            this.states.add(x);
        }
    }
}
