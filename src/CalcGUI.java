import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.*;

public class CalcGUI {

    public ArrayList<JPanel> buttonRows;
    public JPanel displayPanel;
    public JTextArea displayArea;
    public JPanel buttonPanel;
    public StringBuilder equation;
    public boolean inverseMode = false;         // used for inverse sin cos, etc.
    public ArrayList<JButton> buttonsWithNamesToChange;  // these are the buttons whose labels we change when we click the mode button. sin inverse, () become {} and so on.

    public JButton[] mathFunctionButtons;


    public CalcGUI() {

        equation = new StringBuilder();
        buttonRows = new ArrayList<>();
        buttonsWithNamesToChange = new ArrayList<>();

        // initialize our rows for the math functions
        // it will go like this.
        // inverseMode button      log base 10/ln          power/root
        // +                        -                   *
        // /                        (                   )


        // the reason that they are fields, is so that we can update them very easily from the inverse mode action listener.

        buttonRows = makeButtonRows();
        // make our button panel with a new gridLayout. we are just going to have one column per row, since we are making a whole row of buttons at once.
        buttonPanel = new JPanel(new GridLayout(buttonRows.size(), 1));
    }


    public ArrayList<JPanel> makeButtonRows() {
        // adding the digits buttons.
        ArrayList<JPanel> rows = new ArrayList<>();

        // add our six trig functions
        rows.add(makeTrigButtons(new String[]{"SIN", "COS", "TAN"}));
        rows.add(makeTrigButtons(new String[]{"CSC", "SEC", "COT"}));
        // make our extra buttons for log, power, mode
        rows.add(makeExtraButtons());

        // make our math functions row
        // add all our digit buttons now.
        rows.add(makeDigitButtons(new String[]{"1", "2", "3"}));
        rows.add(makeDigitButtons(new String[]{"4", "5", "6"}));
        rows.add(makeDigitButtons(new String[]{"7", "8", "9"}));
        rows.add(makeDigitButtons(new String[]{"0", ".", "CLEAR"}));

        return rows;

    }

    // we must catch the parsing error that is possible where if we have 123~123 that should be a syntax error.
    // we will just print out the syntax error just like a regular calculator does.
    public JPanel makeDigitButtons(String[] buttonLabels) {

        JPanel row = new JPanel();
        row.setLayout(new GridLayout(buttonLabels.length, 1));
        for (String buttonLabel : buttonLabels) {
            JButton button = new JButton(buttonLabel);
            // now we add our action listener, all it does, is
            button.addActionListener(e -> {

                // if the button is the clear button, then we just clear, obviously.
                if (button.getText().equals("CLEAR"))
                    equation.delete(0, equation.length());
                else {
                    if (equation.isEmpty())
                        equation.append(button.getText());

                    else if (Character.isDigit(equation.charAt(equation.length() - 1))) {

                        // if we're adding digits, add it without a space.
                        // if we're adding something that's not a digit, we just add a space then the thing.
                        if (Character.isDigit(button.getText().charAt(0)) || button.getText().equals("."))
                            equation.append(button.getText());
                        else
                            equation.append(" " + button.getText());
                    }
                    // if it's not a digit at the end, we can just simply add the button we pushed.
                    else
                        equation.append(" " + button.getText());
                }
                displayArea.setText(equation.toString());
            });
            row.add(button);
        }
        return row;
    }

    public JPanel makeTrigButtons(String[] buttonLabels){
        JPanel row = new JPanel();
        row.setLayout(new GridLayout(buttonLabels.length, 1));

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
        row.setLayout(new GridLayout(3, 1));

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
        return row;
    }

    // function that is called by our mode button, which updates all the labels of buttons with multiple functions
    public void updateButtonLabels(){

        for(JButton button : buttonsWithNamesToChange){
            // check through all the possible values, and flip from sin to inverse sin, and so on.
            switch (button.getText()) {
                case "SIN" -> button.setText("INVERSE\nSIN");
                case "INVERSE\nSIN" -> button.setText("SIN");
                case "COS" -> button.setText("INVERSE\nCOS");
                case "INVERSE\nCOS" -> button.setText("COS");
                case "TAN" -> button.setText("INVERSE\nTAN");
                case "INVERSE\nTAN" -> button.setText("TAN");
                case "CSC" -> button.setText("INVERSE\nCSC");
                case "INVERSE\nCSC" -> button.setText("CSC");
                case "SEC" -> button.setText("INVERSE\nSEC");
                case "COT" -> button.setText("INVERSE\nCOT");
                case "INVERSE\nCOT" -> button.setText("COT");
                case "LOG" -> button.setText("LN");
                case "LN" -> button.setText("LOG");
                case "^" -> button.setText("SQRT");
                case "SQRT" -> button.setText("^");
                default -> {
                }
            }
        }
    }


}