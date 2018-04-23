import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {



    public static void writeToFileNFA(NFA nfa, String fileName) throws IOException {
        BufferedWriter bw = null;
        try {
            String dotLanguage = "digraph graphname { ";
            //CHANGE PATH TO PARAMETER - FROM ARGS
//            File file = new File("./" + fileName);
            File file = new File(fileName);
//            System.out.println(System.getProperty("user.dir"));
            FileWriter fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write(dotLanguage);
            bw.newLine();
            for(int x=0; x< nfa.transitions.size(); x++) {
                bw.write(nfa.transitions.get(x).prior + "->" +
                        nfa.transitions.get(x).next + "[label=" + "\"" + nfa.transitions.get(x).label + "\""  + "];");
                bw.newLine();
            }
            bw.write(nfa.acceptingState + "[shape=doublecircle];");
            bw.newLine();
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
        System.out.println("Wrote NFA in DOT Language to " + fileName);
    }

    public static void writeToFileDFA(DFATransition dfa, String fileName) throws IOException {
        BufferedWriter bw = null;
        try {
            String dotLanguage = "digraph graphname { ";
            //CHANGE PATH TO PARAMETER - FROM ARGS
            File file = new File(fileName);
            FileWriter fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write(dotLanguage);
            bw.newLine();
            for (int x = 0; x < dfa.dfaTransitions.size(); x++) {

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < dfa.dfaTransitions.get(x).prior.size(); i++) {
                    builder.append(dfa.dfaTransitions.get(x).prior.get(i));
                }
                String result = builder.toString();


                StringBuilder builder2 = new StringBuilder();
                for (int m = 0; m < dfa.dfaTransitions.get(x).next.size(); m++) {
                    builder2.append(dfa.dfaTransitions.get(x).next.get(m));
                }
                String result2 = builder2.toString();

                //NEED TO FIX TO BE A STRING FOR EACH STATE
                bw.write(result + "->" + result2 + "[label=" + "\"" + dfa.dfaTransitions.get(x).label + "\""  + "];");
                bw.newLine();
            }
            //Do after so it only writes double circle once
            for(int k = 0; k < dfa.acceptingState.size(); k++) {
                StringBuilder builder3 = new StringBuilder();
//                System.out.println(dfa.acceptingState.get(k));
                for(int g = 0; g < dfa.acceptingState.get(k).size(); g++){
                    builder3.append(dfa.acceptingState.get(k).get(g));
                }
                String result3 = builder3.toString();
//                System.out.println(result3);
                bw.write(result3 + "[shape=doublecircle];");
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
        System.out.println("Wrote DFA in DOT Language to " + fileName);
    }
}
