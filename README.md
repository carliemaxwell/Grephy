# Grephy

Welcome to Grephy!

CLI commands:
1. cd to /Users/carliemaxwell/GrephyFinalProject and do mvn clean install to create the target/classes folder
2. move the testFile into the classes folder
3. cd into /Users/carliemaxwell/GrephyFinalProject/target/classes
4. enter java Grephy -n nfaFileName -d dfaFileName "regex" testFile
   - (-n nfaFileName -d dfaFileName are all optional params)
5. For regex - use parenthesis around what you want grouped, use | for union, use nothing for concat, use * for kleene star
  ie) If you want a concactenated to b all unioned to c it would be --> ab|c = (ab)|c
6. For nfa/dfa file names, enter the name you want - It will output it to the Output folder w/ that name. If nothing is specified, it will output to the Output folder in the Output text file
7. The terminal should print out the regex, the alphabet from the file, where the nfa/dfa DOT language is being printed to, and the accepted lines from the file
  - regex is (ab)|c
  - the alphabet is [a, b, c, h, q]
  - Wrote NFA in DOT Language to /Users/carliemaxwell/GrephyFinalProject/src/main/Output/nfaFile
  - Wrote DFATransition in DOT Language to /Users/carliemaxwell/GrephyFinalProject/src/main/Output/dfaFile
  - Accepted lines from the file: 
