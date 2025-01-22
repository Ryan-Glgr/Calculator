import java.lang.Math;
import java.util.HashMap;
import java.util.Stack;

public class Computations {

    // our mapping of which operators go before which.
    private static HashMap<String, Integer> precedenceMapping;

    public static void initialize(){

        precedenceMapping = new HashMap<>();
        precedenceMapping.put("+", 1);
        precedenceMapping.put("-", 1);
        precedenceMapping.put("*", 2);
        precedenceMapping.put("/", 2);
        precedenceMapping.put("^", 3);
        precedenceMapping.put("SQRT", 3);
        precedenceMapping.put("LOG", 4);
        precedenceMapping.put("LN", 4);
        precedenceMapping.put("SIN", 4);
        precedenceMapping.put("COS", 4);
        precedenceMapping.put("TAN", 4);
        precedenceMapping.put("COT", 4);
        precedenceMapping.put("SEC", 4);
        precedenceMapping.put("CSC", 4);
        precedenceMapping.put("ARCSIN", 4);
        precedenceMapping.put("ARCCOS", 4);
        precedenceMapping.put("ARCTAN", 4);
        precedenceMapping.put("ARCCOT", 4);
        precedenceMapping.put("ARCSEC", 4);
        precedenceMapping.put("ARCCSC", 4);
        // braces get -1 because they get special treatment.
        precedenceMapping.put("(", -1);
        precedenceMapping.put(")", -1);
        precedenceMapping.put("{", -1);
        precedenceMapping.put("}", -1);
    }

    private static boolean isOperator(String op){
        return precedenceMapping.containsKey(op);
    }

    private static String convertToPostFix(String input){

        /* TODO: handle negations here. When we come across a minus, we check if it is a negation or a subtraction. It is
        * negation if we look at the previous token and it is number or like a ) then we are working with subtraction. else its negation.
        * to handle negation, we just slap a - on the front of the next number.
        */
        
        // stupid regex to split on any whitespace.
        String[] tokens = input.split("\\s+");
        Stack<String> operators = new Stack<>();
        StringBuilder postFix = new StringBuilder();

        for (String token : tokens) {
            if (isOperator(token)) {
                System.out.println(token);
                // SPECIAL CASES, THE BRACKETS.
                // WHEN WE FIND AN OPENING BRACKET, STRAIGHT TO THE STACK
                if (token.equals("(") || token.equals("{")) {
                    operators.push(token);
                    continue;
                }

                // WHEN WE FIND A CLOSING, EMPTY IT UNTIL WE FIND IT'S PARTNER
                if (token.equals(")")){
                    while(!operators.peek().equals("(")){
                        postFix.append(operators.pop() + " ");
                    }
                    operators.pop();
                    continue;
                }
                // same thing as parenthesis
                if (token.equals("}")){
                    while(!operators.peek().equals("{")){
                        postFix.append(operators.pop() + " ");
                    }
                    operators.pop();
                    continue;
                }

                while (!operators.isEmpty() && precedenceMapping.get(token) < precedenceMapping.get(operators.peek())) {
                    postFix.append(operators.pop() + " ");
                }
                operators.push(token);
            }
            else {
                // If it's a number or variable, just append to the result
                postFix.append(token + " ");
            }
        }

        // now that we're done, we just empty the stack of operators.
        while(!operators.isEmpty()){
            postFix.append(operators.pop() + " ");
        }
        return postFix.toString();
    }

    // TODO: make a function which takes in a token(operator) and then the stack, and determines what to do.
    //              maybe we have a unary flag set. that way we know that

    public static String evaluateExpression(String expression) {
        String postFix = convertToPostFix(expression);
        System.out.println(postFix);

        Stack<String> myNums = new Stack<>();

        double result = 0;
        String[] tokens = postFix.split("\\s+");





        return postFix;
    }
}
