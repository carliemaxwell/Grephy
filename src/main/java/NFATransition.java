/*
Thompson's Construction algorithm allows you to convert a regex to NFA.
*/

import java.util.*;

public class NFATransition {


    public int exit;
    public int enter;
    public char label;  //used the word label bc that is the character in DOT language


    public NFATransition(int from, int to, char label) {

        //A transition consists of from --> to with character label
        this.exit = from;
        this.enter = to;
        this.label = label;
    }


    public static NFA concat(NFA first, NFA second) {

        //create new DFATransition to combine the DFAs
        NFA con = new NFA();

        //add in first nfa and all of second nfa except starting state
        con.addStates(first.states.size() + (second.states.size() - 1));

        con.conAddTransitions(con, first, second);

        //accepting state is last state that second goes to
        con.acceptingState = con.states.size() - 1;

        //return NFA to perform further operations
        return con;
    }


    public static NFA union(NFA first, NFA second) {

        //create new NFA to combine both NFA's into
        NFA unionNFA = new NFA();

        //number of states in new NFA is first states + second states + 2 bc of the 2 epsilon transitions
        unionNFA.addStates(first.states.size() + second.states.size() + 2);

        unionNFA.unionAddTransitions(unionNFA, first, second);

        //declare accepting state for new NFA
        unionNFA.acceptingState = unionNFA.states.size() - 1;

        //return NFA to perform further operations
        return unionNFA;
    }


    public static NFA star(NFA nfa) {

        //create new NFA to put original into
        NFA starNFA = new NFA();

        //new NFA has same states + 2 for the beginning and ending epsilon transitions
        starNFA.addStates(nfa.states.size() + 2);

        starNFA.starAddTransitions(starNFA, nfa);

        //declare accepting state
        starNFA.acceptingState = starNFA.states.size() - 1;

        return starNFA;
    }


    public static NFA readRegex(String regex) {

        Stack<Character> characters = new Stack<Character>();
        Stack<Character> symbols = new Stack<Character>();
        int parenthesisFrontIndex = 0;
        int parenthesisBackIndex = 0;

        for (int x = 0; x < regex.length(); x++) {
            char currentChar = regex.charAt(x);
            if (currentChar != '(' && currentChar != ')' && currentChar != '*' && currentChar != '|') {
                characters.push(currentChar);
            } else {
                switch (currentChar) {
                    case '(':
                        symbols.push(currentChar);
                        parenthesisFrontIndex = x;
                        break;
                    case ')':
                        symbols.push(currentChar);
                        parenthesisBackIndex = x;
                        break;
                    case '*':
                        symbols.push(currentChar);
                        break;
                    case '|':
                        symbols.push(currentChar);
                        break;
                }
            }
        }

        System.out.println("characters " + characters);
        System.out.println("symbols " + symbols);

        return null;
    }
}




