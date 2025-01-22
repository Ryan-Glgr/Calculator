import javax.swing.*;
import java.awt.*;
import java.util.*;

public class CalcGUI {

    public ArrayList<JPanel> buttonRows;
    public JPanel displayPanel;
    public JTextArea displayArea;
    public JPanel buttonPanel;
    public StringBuilder equation;
    public boolean inverseMode = false;         // used for inverse sin cos, etc.

    public JButton[] mathFunctionButtons;


    public CalcGUI() {



        equation = new StringBuilder();

        // initialize our rows for the math functions
        // it will go like this.
        // inverseMode button      log base 10          ln
        // power button            root button          clear
        // mode button will flip the 6 trig functions to inverse mode. as well as parenthesis into curly brackets.
        // operation buttons vvv
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



        // make our math functions row
        // add all our digit buttons now.
        rows.add(makeDigitButtons(new String[]{"1", "2", "3"}));
        rows.add(makeDigitButtons(new String[]{"4", "5", "6"}));
        rows.add(makeDigitButtons(new String[]{"7", "8", "9"}));
        rows.add(makeDigitButtons(new String[]{"0", ".", "(-)"}));

        return rows;

    }

    // we must catch the parsing error that is possible where if we have 123~123 that should be a syntax error.
    // we will just print out the syntax error just like a regular calculator does.
    public JPanel makeDigitButtons(String[] buttonLabels) {
        JPanel row = new JPanel();
        row.setLayout(new GridLayout(buttonLabels.length, 1));
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            // now we add our action listener, all it does, is
            button.addActionListener(e -> {

                if (button.getText().equals("(-)"))
                    equation.append("~");
                else {
                    if (equation.length() == 0)
                        equation.append(button.getText());
                        // if the most recent character is a digit, that means that we can just add the digit without a space.
                        // if it is not a digit, we add a space, to delimit the string properly.
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
        }
        return row;
    }
}