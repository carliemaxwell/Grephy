/*
Thompson's Construction algorithm allows you to convert a regex to NFA.
I need to create output that allows you to convert it to DOT language eventually.
*/

import java.util.*;
import java.io.*;

public class NFATransition {

    public int prior;
    public int next;
    public char label;  //used the word label bc that is the character in DOT language


    public NFATransition(int from, int to, char label) {
        //A transition consists of from --> to with character label
        this.prior = from;
        this.next = to;
        this.label = label;
    }


    public static NFA concat(NFA first, NFA second) {

        //create new DFATransition to combine the DFAs
        NFA con = new NFA();

        //add in first nfa and all of second nfa except starting state
        con.addStates(first.states.size() + (second.states.size() - 1));

        //put all of first into new DFATransition
        for(int x = 0; x < first.transitions.size(); x++) {
            con.transitions.add(first.transitions.get(x));
        }

        //update second NFA to be shifted right first.states.size()-1 bc needs to start at end of first states
        for(int x = 0; x < second.transitions.size(); x++) {
            con.transitions.add(new NFATransition(second.transitions.get(x).prior + first.states.size()-1,
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



    //Can I have only 2 parameters or do I need more?
    public static NFA union(NFA first, NFA second) {

        //create new NFA to combine both NFA's into
        NFA unionNFA = new NFA();

        //number of states in new NFA is first states + second states + 2 bc of the 2 epsilon transitions
        unionNFA.addStates(first.states.size() + second.states.size() + 2);

        //shift first nfa up 1 and add to unionNFA
        for(int x = 0; x < first.transitions.size(); x++) {
            unionNFA.transitions.add(new NFATransition(first.transitions.get(x).prior + 1,
                    first.transitions.get(x).next + 1, first.transitions.get(x).label));
        }

        //shift second nfa up 1 + first state amount and add to unionNFA
        for(int y=0; y< second.transitions.size(); y++) {
            unionNFA.transitions.add(new NFATransition(second.transitions.get(y).prior + first.states.size()+1,
                    second.transitions.get(y).next + first.states.size()+1, second.transitions.get(y).label));
        }

        //add beginning epsilons, need to shift first by 1 and second by amount in first+1
        //NEED TO CHANGE E
        unionNFA.transitions.add(new NFATransition(0,1,'ε'));
        unionNFA.transitions.add(new NFATransition(0,first.states.size()+1,'ε'));

        //add ending epsilons
        unionNFA.transitions.add(new NFATransition(first.states.size(), unionNFA.states.size()-1, 'ε'));
        unionNFA.transitions.add(new NFATransition(unionNFA.states.size() - 2, unionNFA.states.size()-1, 'ε'));

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


    public static NFA star(NFA nfa) {
//        An ε-transition connects initial and final state of the NFA with the sub-NFA N(s) in between.
//        Another ε-transition from the inner final to the inner initial state of N(s) allows for repetition
//        of expression s according to the star operator

        //create new NFA to put original into
        NFA starNFA = new NFA();

        //new NFA has same states + 2 for the beginning and ending epsilon transitions
        starNFA.addStates(nfa.states.size() + 2);

        System.out.println("star NFA states = " + starNFA.states.size());

        //copy over the original transitions
        for(int x = 0; x< nfa.transitions.size(); x++) {
            starNFA.transitions.add(new NFATransition(nfa.transitions.get(x).prior + 1, nfa.transitions.get(x).next + 1, nfa.transitions.get(x).label));
        }

        //beginning to nfa epsilon transition
        starNFA.transitions.add(new NFATransition(0,1,'ε'));
        //beginning to end epsilon transition
        starNFA.transitions.add(new NFATransition(0,starNFA.states.size()-1,'ε'));
        //nfa end to nfa beginning epsilon transition
        starNFA.transitions.add(new NFATransition(nfa.states.size(),1,'ε'));
        //nfa to end epsilon transition
        starNFA.transitions.add(new NFATransition(nfa.states.size(), starNFA.states.size()-1, 'ε'));

        //declare accepting state
        starNFA.acceptingState = starNFA.states.size()-1;

        //TESTING
        System.out.println("star nfa size " + starNFA.states.size());
        System.out.println("star accepting state " + starNFA.acceptingState);
        System.out.println("star transition size " + starNFA.transitions.size());
        for (int x = 0; x < starNFA.transitions.size(); x++) {
            System.out.println("from " + starNFA.transitions.get(x).prior + " to " + starNFA.transitions.get(x).next + " symbol " + "to " + starNFA.transitions.get(x).label);
        }

        return starNFA;
    }

    public static NFA readRegex(String regex) {

        Stack<NFA> characterStack = new Stack();
        Stack symbolStack = new Stack();

        for(int x = 0; x < regex.length(); x++) {

            if (regex.charAt(x) == '(') {
                while (regex.charAt(x) != ')') {
                    while (regex.charAt(x) != '+' || regex.charAt(x) != '*') {
                        NFA nfa = new NFA(regex.charAt(x));
                        characterStack.add(nfa);
                    }
                }
            }
        }

            if(characterStack.size() == 1) {
                return characterStack.get(0);
            }

            if(characterStack.size() == 2) {
                NFA nfa = concat(characterStack.get(0), characterStack.get(1));
                return nfa;
            }

            return null;
    }


    public static void main(String[] args) throws IOException {

        //NEED TO DO ARGS FOR Grep [-n NFA-FILE] [-d DFATransition-FILE] REGEX FILE

        //GET ALPHABET FROM FILE
        Ingestion.alphabet();

        //CREATE NFA'S TO TEST METHODS WITH
        NFA a = new NFA('a');
        NFA b = new NFA('b');
        NFA c = new NFA('c');

//        NFA nfa = concat(a,b);

        DFATransition.createDFA(star(a));


        //NEED TO CALL ECLOSURE TO CREATE HASHMAP
//        DFATransition.eclosure(union(a,b));
//        DFATransition.eclosure(star(union(a,b)));
//        Writer.writeToFileNFA(star(union(a,b)));
//        DFATransition.eclosure(concat(a, union(star(a),b)));
//        DFATransition.eclosure(union(star(a),b));

        //NEED TO CALL CREATE DFATransition TO TEST METHOD
//        DFATransition.createDFA(union(star(a),b));


        //TEST OUTPUT METHODS
//      writeToFile(concat(a,union(a, b)));
//      DFATransition.writeToFileDFA(DFATransition.createDFA(union(a,b)));
    }
}



