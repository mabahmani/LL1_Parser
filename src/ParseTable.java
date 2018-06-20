import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParseTable {

    private HashMap<Integer, String> grammar;
    private HashMap<String, String[]> firstSet;
    private HashMap<String, String[]> followSet;
    private ArrayList<String> nonTerminals;

    ParseTable() {
        grammar = new HashMap<Integer, String>();
        firstSet = new HashMap<String, String[]>();
        followSet = new HashMap<String, String[]>();
        nonTerminals = new ArrayList<String>();

        this.grammar.put(1, "Program-> Statement");
        this.grammar.put(2, "Statement-> if Expression then Block");
        this.grammar.put(3, "Statement-> while Expression do Block");
        this.grammar.put(4, "Statement-> Expression");
        this.grammar.put(5, "Expression-> Term => identifier");
        this.grammar.put(6, "Expression-> isZero? Term");
        this.grammar.put(7, "Expression-> not Expression");
        this.grammar.put(8, "Expression-> ++ identifier");
        this.grammar.put(9, "Expression-> -- identifier");
        this.grammar.put(10, "Term-> constant");
        this.grammar.put(11, "Block-> Statement");
        this.grammar.put(12, "Block-> { Statements }");
        this.grammar.put(13, "Statements-> Statement Statements");
        this.grammar.put(14, "Statements-> ε");


        String[] Program_First = {"if", "while", "identifier", "constant", "isZero?", "not", "++", "--"};
        String[] Statement_First = {"if", "while", "identifier", "constant", "isZero?", "not", "++", "--"};
        String[] Expression_First = {"identifier", "constant", "isZero?", "not", "++", "--"};
        String[] Term_First = {"identifier", "constant"};
        String[] Block_First = {"if", "while", "identifier", "constant", "isZero?", "not", "++", "--", "{"};
        String[] Statements_First = {"if", "while", "identifier", "constant", "isZero?", "not", "++", "--", "ε"};

        firstSet.put("Program", Program_First);
        firstSet.put("Statement", Statement_First);
        firstSet.put("Expression", Expression_First);
        firstSet.put("Term", Term_First);
        firstSet.put("Block", Block_First);
        firstSet.put("Statements", Statements_First);


        String[] Program_Follow = {"$"};
        String[] Statement_Follow = {"$", "if", "while", "identifier", "constant", "isZero?", "not", "++", "--", "}"};
        String[] Expression_Follow = {"then", "do", "$", "if", "while", "identifier", "constant", "isZero?", "not", "++", "--", "}"};
        String[] Term_Follow = {"=>", "then", "do", "$", "if", "while", "identifier", "constant", "isZero?", "not", "++", "--", "}"};
        String[] Block_Follow = {"$", "if", "while", "identifier", "constant", "isZero?", "not", "++", "--", "}"};
        String[] Statements_Follow = {"}"};

        followSet.put("Program", Program_Follow);
        followSet.put("Statement", Statement_Follow);
        followSet.put("Expression", Expression_Follow);
        followSet.put("Term", Term_Follow);
        followSet.put("Block", Block_Follow);
        followSet.put("Statements", Statements_Follow);

        this.nonTerminals.add("Program");
        this.nonTerminals.add("Statement");
        this.nonTerminals.add("Expression");
        this.nonTerminals.add("Term");
        this.nonTerminals.add("Block");
        this.nonTerminals.add("Statements");

    }


    private String getLHS(String production) {
        return production.split("->")[0];
    }

    private String getRHS(String production) {
        return production.split("->")[1].split(" ")[1];
    }

    private String[] getFirstSet(String RHS) {
        if (nonTerminals.contains(RHS)) {
            return firstSet.get(RHS);
        }
        return new String[]{RHS};
    }

    public HashMap<String, HashMap<String, Integer>> generateTable() {

        HashMap<String, HashMap<String, Integer>> LL1Table = new HashMap<String, HashMap<String, Integer>>();

        for (Map.Entry<Integer, String> g : this.grammar.entrySet()) {

            String production = g.getValue();
            String LHS = getLHS(production);
            String RHS = getRHS(production);
            int productionNum = g.getKey();


            if (!RHS.equals("ε")) {
                HashMap<String, Integer> temp = new HashMap<String, Integer>();
                for (String s : getFirstSet(RHS)) {
                    temp.put(s, productionNum);
                }
                if (LL1Table.containsKey(LHS)) {
                    LL1Table.get(LHS).putAll(temp);
                } else {
                    LL1Table.putIfAbsent(LHS, temp);
                }
            } else if (RHS.equals("ε")) {
                HashMap<String, Integer> temp = new HashMap<String, Integer>();
                for (String f : followSet.get(LHS)) {
                    temp.put(f, productionNum);
                }
                if (LL1Table.containsKey(LHS)) {
                    LL1Table.get(LHS).putAll(temp);
                } else {
                    LL1Table.putIfAbsent(LHS, temp);
                }
            }
        }

        return LL1Table;
    }


    public boolean inputCheck(String input) {
        String [] temp = input.split(" ");
        ArrayList<String> state = new ArrayList<>();
        ArrayList<String> inputParse = new ArrayList<>(Arrays.asList(temp));
        HashMap<String, HashMap<String, Integer>> LL1Table = generateTable();

        state.add("Program");

        while (inputParse.size() > 0) {
            if (nonTerminals.contains(state.get(0))) {
                if(inputParse.get(0).equals("ε")){
                    inputParse.remove(0);
                    state.remove(0);
                }
                else {
                    try {
                        int id = LL1Table.get(state.get(0)).get(inputParse.get(0));
                        String[] RHS = this.grammar.get(id).split("->")[1].split(" ");

                        int i = 0;
                        state.remove(0);
                        for (String s : RHS) {
                            if (!s.equals("")) {
                                state.add(i, s);
                                i++;
                            }
                        }
                    }

                    catch (Exception e){
                        return false;
                    }
                }
            }
            else {
                if (state.get(0).equals(inputParse.get(0)) || state.get(0).equals("ε")) {
                    if (state.get(0).equals("ε"))
                        state.remove(0);
                    else {
                        state.remove(0);
                        inputParse.remove(0);
                    }
                    }
                }
            }

        return state.size() == 0;
    }
}