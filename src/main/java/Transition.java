/*
Thompson's Construction algorithm allows you to convert a regex to NFA.
I need to create output that allows you to convert it to DOT language eventually.
*/

import java.util.*;

public class Transition {

    public int prior;
    public int next;
    public char label;  //used the word label bc that is the character in DOT language


    public Transition(int from, int to, char label) {
        //A transition consists of from --> to with character label
        this.prior = from;
        this.next = to;
        this.label = label;
    }


    public static NFA concat(NFA first, NFA second) {

        //create new DFA to combine the DFAs
        NFA con = new NFA();

        //add in first nfa and all of second nfa except starting state
        con.addStates(first.states.size() + (second.states.size() - 1));

        //put all of first into new DFA
        for(int x = 0; x < first.transitions.size(); x++) {
            con.transitions.add(first.transitions.get(x));
        }

        //update second NFA to be shifted right first.states.size()-1 bc needs to start at end of first states
        for(int x = 0; x < second.transitions.size(); x++) {
            con.transitions.add(new Transition(second.transitions.get(x).prior + first.states.size()-1,
                    second.transitions.get(x).next + first.states.size()-1, second.transitions.get(x).label));
        }

        //accepting state is last state that second goes to
        con.acceptingState = con.states.size() - 1;

        //TESTING
        System.out.println("concat nfa size " + con.states.size());
        System.out.println("concat accepting state " + con.acceptingState);
        for (int x = 0; x < con.transitions.size(); x++) {
            System.out.println("from " + con.transitions.get(x).prior + " to " + con.transitions.get(x).next + " symbol " + "to " + con.transitions.get(x).label);
        }

        //return NFA to perform further operations
        return con;
    }



    public static NFA union(NFA first, NFA second) {

        //create new NFA to combine both NFA's into
        NFA unionNFA = new NFA();

        //number of states in new NFA is first states + second states + 2 bc of the 2 epsilon transitions
        unionNFA.addStates(first.states.size() + second.states.size() + 2);

        //shift first nfa up 1 and add to unionNFA
        for(int x = 0; x < first.transitions.size(); x++) {
            unionNFA.transitions.add(new Transition(first.transitions.get(x).prior + 1,
                    first.transitions.get(x).next + 1, first.transitions.get(x).label));
        }

        //shift second nfa up 1 + first state amount and add to unionNFA
        for(int y=0; y< second.transitions.size(); y++) {
            unionNFA.transitions.add(new Transition(second.transitions.get(y).prior + first.states.size()+1,
                    second.transitions.get(y).next + first.states.size()+1, second.transitions.get(y).label));
        }

        //add beginning epsilons, need to shift first by 1 and second by amount in first+1
        unionNFA.transitions.add(new Transition(0,1,'e'));
        unionNFA.transitions.add(new Transition(0,first.states.size()+1,'e'));

        //add ending epsilons
        unionNFA.transitions.add(new Transition(first.states.size(), unionNFA.states.size()-1, 'e'));
        unionNFA.transitions.add(new Transition(unionNFA.states.size() - 2, unionNFA.states.size()-1, 'e'));

        //declare accepting state for new NFA
        unionNFA.acceptingState = unionNFA.states.size() - 1;

        //TESTING
        System.out.println("union nfa size " + unionNFA.states.size());
        System.out.println("union accepting state " + unionNFA.acceptingState);
        for (int x = 0; x < unionNFA.transitions.size(); x++) {
            System.out.println("from " + unionNFA.transitions.get(x).prior + " to " + unionNFA.transitions.get(x).next + " symbol " + "to " + unionNFA.transitions.get(x).label);
        }

        //return NFA to perform further operations
        return unionNFA;
    }


    public static NFA star() {
        NFA nfa = new NFA();
//        An ε-transition connects initial and final state of the NFA with the sub-NFA N(s) in between.
//        Another ε-transition from the inner final to the inner initial state of N(s) allows for repetition
//        of expression s according to the star operator


        //copy transitions over
        //add two states
        //add epsilons
        return nfa;

    }

    public static void readRegex() {
        //need grouping (stack) to combine them all
        //ab(c*)
        //look at a, look at next character chance to concat
        //stacks
        //(ab)|c
        //    g1 - char(a) [.]?
        //    g2 b = yes concat
        //    replace w NFA and now do NFA | c
        int x = 0;
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        char c = line.charAt(x);
        NFA nfa = new NFA(c);
        x++;
    }

    public static void main(String[] args) {
        //TEST CASES
        //gave each character an NFA transition of 0 -> 1 for single character transition
        NFA a = new NFA('a');
        NFA b = new NFA('b');
        NFA nfa = concat(a,b);
        //ab|b
        NFA nfaUnion = union(nfa, b);
        //((a.b)|b).a
        concat(nfaUnion, a);
    }
}



