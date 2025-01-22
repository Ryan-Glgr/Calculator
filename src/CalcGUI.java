import javax.swing.*;
import java.awt.*;
import java.util.*;

public class CalcGUI {

    public JFrame frame;
    public ArrayList<JPanel> buttonRows;
    public JPanel displayPanel;
    public JTextArea displayArea;
    public JPanel guiPanel;
    public StringBuilder equation;
    public boolean inverseMode = false;         // used for inverse sin cos, etc.
    public ArrayList<JButton> buttonsWithNamesToChange;  // these are the buttons whose labels we change when we click the mode button. sin inverse, () become {} and so on.

    public JButton[] mathFunctionButtons;


    public CalcGUI() {
        equation = new StringBuilder();
        buttonsWithNamesToChange = new ArrayList<>();
        buttonRows = new ArrayList<>();
        displayArea = new JTextArea(2, 20); // Example display area dimensions

        // Make our list of panels
        makeButtonRows();

        // Create display panel with a stupid grid layout, so that the display fills the whole box
        displayPanel = new JPanel(new GridLayout(1,1));
        displayPanel.add(displayArea);

        // Create the main panel (button panel with grid layout)
        guiPanel = new JPanel(new GridLayout(buttonRows.size() + 1, 1));
        guiPanel.add(displayPanel);
        for (JPanel p : buttonRows) {
            guiPanel.add(p);
        }

        // Create the JFrame (main window)
        frame = new JFrame("Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application when the window is closed
        frame.setSize(400, 600); // Set the size of the window (adjust as needed)
        frame.setLayout(new BorderLayout()); // Use a BorderLayout for the frame
        frame.add(guiPanel, BorderLayout.CENTER); // Add the main panel to the center

        // Make the window visible
        frame.setVisible(true);
    }

    public void makeButtonRows() {

        buttonRows = new ArrayList<>();
        // add our six trig functions
        buttonRows.add(makeTrigButtons(new String[]{"SIN", "COS", "TAN"}));
        buttonRows.add(makeTrigButtons(new String[]{"CSC", "SEC", "COT"}));

        // make our extra buttons for log, power, mode
        buttonRows.add(makeExtraButtons());
        // + - * buttons
        buttonRows.add(makeOperatorButtons());

        // / ( ) buttons
        buttonRows.add(makeOtherOperators());

        // add all our digit buttons now.
        buttonRows.add(makeDigitButtons(new String[]{"1", "2", "3"}));
        buttonRows.add(makeDigitButtons(new String[]{"4", "5", "6"}));
        buttonRows.add(makeDigitButtons(new String[]{"7", "8", "9"}));
        buttonRows.add(makeDigitButtons(new String[]{"0", ".", "CLEAR"}));

        // make the enter button on it's own panel so it's full size.
        JPanel enterButtonPanel = new JPanel(new GridLayout(1, 1));
        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(e -> {
            displayArea.setText(Computations.evaluateExpression(equation.toString()));
        });
        enterButtonPanel.add(enterButton);
        buttonRows.add(enterButtonPanel);
    }

    // we must catch the parsing error that is possible where if we have 123ARC123 that should be a syntax error.
    // we will just print out the syntax error just like a regular calculator does.
    public JPanel makeDigitButtons(String[] buttonLabels) {

        JPanel row = new JPanel();
        row.setLayout(new GridLayout(1, buttonLabels.length));
        for (String buttonLabel : buttonLabels) {
            JButton button = new JButton(buttonLabel);
            // now we add our action listener, all it does, is
            button.addActionListener(e -> {

                // if the button is the clear button, then we just clear, obviously.
                if (button.getText().equals("CLEAR")) {
                    equation.delete(0, equation.length());
                }
                else {

                    if (equation.isEmpty()){
                        equation.append(button.getText());
                    }
                    else{
                        char lastChar = equation.charAt(equation.length() - 1);
                        if (Character.isDigit(lastChar) || lastChar == '.'){
                            equation.append(button.getText());
                        }
                        else{
                            equation.append(" " + button.getText());
                        }
                    }
                }
                displayArea.setText(equation.toString());
            });
            row.add(button);
        }
        return row;
    }

    public JPanel makeTrigButtons(String[] buttonLabels){
        JPanel row = new JPanel();
        row.setLayout(new GridLayout(1, buttonLabels.length));

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.addActionListener(e -> {
                equation.append(" " + button.getText() + " ");
                displayArea.setText(equation.toString());
            });
            row.add(button);
            buttonsWithNamesToChange.add(button);
        }
        return row;
    }

    // makes the row that contains the 2nd mode function, the log/ln button, and the root/power button
    public JPanel makeExtraButtons(){

        JPanel row = new JPanel();
        row.setLayout(new GridLayout(1, 3));

        JButton modeButton = new JButton("MODE");
        modeButton.addActionListener(e -> {
           inverseMode = !inverseMode;
           updateButtonLabels();
        });

        JButton logButton = new JButton("LOG");
        logButton.addActionListener(e -> {
           equation.append(" " + logButton.getText());
           displayArea.setText(equation.toString());
        });

        JButton powerButton = new JButton("^");
        powerButton.addActionListener(e -> {
           equation.append(" " + powerButton.getText());
           displayArea.setText(equation.toString());
        });

        buttonsWithNamesToChange.add(logButton);
        buttonsWithNamesToChange.add(powerButton);

        row.add(modeButton);
        row.add(logButton);
        row.add(powerButton);

        return row;
    }

    public JPanel makeOperatorButtons(){
        JPanel row = new JPanel();
        row.setLayout(new GridLayout(1, 3));

        JButton addButton = new JButton("+");
        addButton.addActionListener(e -> {
            equation.append(" " + addButton.getText() + " ");
            displayArea.setText(equation.toString());
        });

        JButton subButton = new JButton("-");
        subButton.addActionListener(e -> {
            equation.append(" " + subButton.getText());
            displayArea.setText(equation.toString());
        });

        JButton mulButton = new JButton("*");
        mulButton.addActionListener(e -> {
           equation.append(" " + mulButton.getText());
           displayArea.setText(equation.toString());
        });
        row.add(addButton);
        row.add(subButton);
        row.add(mulButton);
        
        return row;
    }

    public JPanel makeOtherOperators(){

        JPanel row = new JPanel();
        row.setLayout(new GridLayout(1, 3));

        JButton divideButton = new JButton("/");
        divideButton.addActionListener(e -> {
            equation.append(" " + divideButton.getText() + " ");
            displayArea.setText(equation.toString());
        });

        JButton leftParenthesisButton = new JButton("(");
        leftParenthesisButton.addActionListener(e -> {
            equation.append(" " + leftParenthesisButton.getText());
            displayArea.setText(equation.toString());
        });

        JButton rightParenthesisButton = new JButton(")");
        rightParenthesisButton.addActionListener(e -> {
            equation.append(" " + rightParenthesisButton.getText());
            displayArea.setText(equation.toString());
        });

        row.add(divideButton);
        row.add(leftParenthesisButton);
        row.add(rightParenthesisButton);

        // these two flip flop to curly braces as well.
        buttonsWithNamesToChange.add(leftParenthesisButton);
        buttonsWithNamesToChange.add(rightParenthesisButton);
        return row;
    }

    // function that is called by our mode button, which updates all the labels of buttons with multiple functions
    public void updateButtonLabels(){
        for(JButton button : buttonsWithNamesToChange){
            // check through all the possible values, and flip from sin to arc sin, and so on.
            switch (button.getText()) {
                case "SIN" -> button.setText("ARCSIN");
                case "ARCSIN" -> button.setText("SIN");
                
                case "COS" -> button.setText("ARCCOS");
                case "ARCCOS" -> button.setText("COS");
                
                case "TAN" -> button.setText("ARCTAN");
                case "ARCTAN" -> button.setText("TAN");
                
                case "CSC" -> button.setText("ARCCSC");
                case "ARCCSC" -> button.setText("CSC");
                
                case "SEC" -> button.setText("ARCSEC");
                case "ARCSEC" -> button.setText("SEC");
                
                case "COT" -> button.setText("ARCCOT");
                case "ARCCOT" -> button.setText("COT");
                
                case "LOG" -> button.setText("LN");
                case "LN" -> button.setText("LOG");
                
                case "^" -> button.setText("SQRT");
                case "SQRT" -> button.setText("^");

                case "(" -> button.setText("{");
                case "{" -> button.setText("(");

                case ")" -> button.setText("}");
                case "}" -> button.setText(")");

                default -> {}
            }
        }
    }

}