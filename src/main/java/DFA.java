import java.util.List;

public class DFA {


    public List prior;
    public List next;
    public char label;  //used the word label bc that is the character in DOT language


    public DFA(List from, List to, char label) {
        //A transition consists of from --> to with character label
        this.prior = from;
        this.next = to;
        this.label = label;
    }


}
