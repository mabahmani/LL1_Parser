import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class Draw {

    private static void draw (String[] columns , String[][] data){

        JFrame jFrame = new JFrame();
        JPanel jPanelTable = new JPanel();
        JPanel jPanelGrammar = new JPanel();
        JPanel jPanelInput = new JPanel();
        JTextArea jTextAreaInput = new JTextArea(2,40);
        JButton jButton = new JButton("Check");
        JLabel jLabel = new JLabel("Waiting for Input...");
        JTable jTable = new JTable(data,columns);
        JScrollPane jScrollPane = new JScrollPane(jTable);
        JTextArea jTextAreaGrammar = new JTextArea(
                "1: Program -> Statement\n"+
                "2: Statement -> if Expression then Block\n"+
                "3: Statement -> while Expression do Block\n"+
                "4: Statement -> Expression\n"+
                "5: Expression -> Term => identifier\n"+
                "6: Expression -> isZero? Term\n"+
                "7: Expression -> not Expression\n"+
                "8: Expression -> ++ identifier\n"+
                "9: Expression -> -- identifier\n"+
                "10: Term -> constant\n"+
                "11: Block -> Statement\n"+
                "12: Block -> { Statements }\n"+
                "13: Statements -> Statement Statements\n"+
                "14: Statements -> ε"
        );

        jButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = jTextAreaInput.getText();
                ParseTable parseTable = new ParseTable();
                if(parseTable.inputCheck(input)){
                    jLabel.setText("Accepted");
                    jLabel.setForeground(Color.GREEN);
                }
                else{
                    jLabel.setText("Rejected");
                    jLabel.setForeground(Color.RED);
                }
            }
        });

        jFrame.setSize(1368,720);
        jTable.setPreferredScrollableViewportSize(jTable.getPreferredSize());
        jTable.setEnabled(false);
        jTextAreaGrammar.setEditable(false);
        jPanelGrammar.add(jTextAreaGrammar);
        jPanelTable.add(jScrollPane);
        jPanelInput.add(jTextAreaInput);
        jPanelInput.add(jButton);
        jPanelInput.add(jLabel);
        jFrame.add(jPanelGrammar, BorderLayout.WEST);
        jFrame.add(jPanelTable,BorderLayout.NORTH);
        jFrame.add(jPanelInput,BorderLayout.CENTER);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

    }

    private static String [] getColumns(){

        return new String[]{" ","if", "then", "while", "do", "=>", "identifier", "isZero?", "not", "++", "--", "constant", "{", "}", "$" };
    }

    private static String[][] getData(){
        ParseTable parseTable = new ParseTable();
        HashMap<String,HashMap<String,Integer>> LL1Table = parseTable.generateTable();
        String data [][] = new String[6][15];

        int i = 0;
        for (Map.Entry<String, HashMap<String, Integer>> nonTerminals: LL1Table.entrySet()){
            data[i][0] = nonTerminals.getKey();
            for (Map.Entry<String,Integer> terminals: nonTerminals.getValue().entrySet()) {
                for (int j = 0; j< getColumns().length; j++){
                    if(getColumns()[j].equals(terminals.getKey())){
                        data[i][j] = String.valueOf(terminals.getValue());
                        break;
                    }
                }
            }
            i++;
        }

        return data;
    }

    public static void main(String args[]){
        draw(getColumns(),getData());
    }
}
