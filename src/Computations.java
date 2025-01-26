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
        precedenceMapping.put("NEG", 4);
        precedenceMapping.put("^", 4);
        precedenceMapping.put("SQRT", 4);
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

    private static String convertToPostFix(String input) {

        // Split input into tokens based on whitespace trim because we may have leading whitespace.
        String[] tokens = input.trim().split("\\s+");
        Stack<String> operators = new Stack<>();
        StringBuilder postFix = new StringBuilder();

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (isOperator(token)) {
                // Handle parentheses and curly braces specially
                if (token.equals("(") || token.equals("{")) {

                    // if were are not at the first index, obviously. and also the last token is not an operator, so it's a number. we are going to slap a * in because we're doing multiplication in that case.
                    if (i != 0 && !isOperator(tokens[i - 1])) {
                        operators.push("*");
                    }
                    operators.push(token);
                    continue;
                }

                // we keep popping stuff off until we find the match
                if (token.equals(")")) {
                    while (!operators.peek().equals("(")) {
                        postFix.append(operators.pop() + " ");
                    }
                    operators.pop();
                    continue;
                }

                if (token.equals("}")) {
                    while (!operators.peek().equals("{")) {
                        postFix.append(operators.pop() + " ");
                    }
                    operators.pop();
                    continue;
                }

                // special case. we check the subtraction operator, if it is next to nothing, if it is, then we flag it as NEG, so it's a negation.
                if (token.equals("-")){
                    // now we must determine whether this is just a minus operator, or the negation. if it's a negation, we flag it with NEG.
                    // it is a negation if it the thing to the left is NOT a number
                    if (i == 0 || isOperator(tokens[i - 1])){
                        token = "NEG";
                    }
                }
                // now we go and get the higher precedence operators, or even precedence ones who showed up earlier off the stack.
                while (!operators.isEmpty() && precedenceMapping.get(token) <= precedenceMapping.get(operators.peek())) {
                    postFix.append(operators.pop() + " ");
                }
                operators.push(token);
            }
            // if it was just a number, not operator, just slap it in the string.
            else {
                // Append numbers or variables directly
                postFix.append(token + " ");
            }
        }

        // Empty the operator stack at the end.
        while (!operators.isEmpty()) {
            postFix.append(operators.pop() + " ");
        }

        return postFix.toString();
    }

    private static String evaluate(Stack<String> nums, String operator){

        double result;
        switch (operator) {
            case "+" -> result = Double.parseDouble(nums.pop()) + Double.parseDouble(nums.pop());
            case "-" -> {
                double second = Double.parseDouble(nums.pop());
                result = Double.parseDouble(nums.pop()) - second;
            }
            case "*" -> result = Double.parseDouble(nums.pop()) * Double.parseDouble(nums.pop());
            case "/" ->
                    {
                        double denom = Double.parseDouble(nums.pop());
                        result = Double.parseDouble(nums.pop()) / denom;
                    }
            case "NEG" -> result = -Double.parseDouble(nums.pop());
            case "^" -> {
                double exp = Double.parseDouble(nums.pop());
                result = Math.pow(Double.parseDouble(nums.pop()), exp);
            }
            case "SQRT" -> result = Math.sqrt(Double.parseDouble(nums.pop()));
            case "LOG" -> result = Math.log10(Double.parseDouble(nums.pop()));
            case "LN" -> result = Math.log(Double.parseDouble(nums.pop()));
            case "SIN" -> result = Math.sin(Double.parseDouble(nums.pop()));
            case "ARCSIN" -> result = Math.asin(Double.parseDouble(nums.pop()));
            case "COS" -> result = Math.cos(Double.parseDouble(nums.pop()));
            case "ARCCOS" -> result = Math.acos(Double.parseDouble(nums.pop()));
            case "TAN" -> result = Math.tan(Double.parseDouble(nums.pop()));
            case "ARCTAN" -> result = Math.atan(Double.parseDouble(nums.pop()));
            // now into the messy stuff
            case "CSC" -> result = 1 / Math.sin(Double.parseDouble(nums.pop()));
            case "ARCCSC" -> result = Math.asin(1 / Double.parseDouble(nums.pop()));
            case "SEC" -> result = 1 / Math.cos(Double.parseDouble(nums.pop()));
            case "ARCSEC" -> result = Math.acos(1 / Double.parseDouble(nums.pop()));
            case "COT" -> result = 1 / Math.tan(Double.parseDouble(nums.pop()));
            case "ARCCOT" -> result = Math.atan(1 / Double.parseDouble(nums.pop()));
            default -> {
                return "SYNTAX ERROR";
            }
        }
        return Double.toString(result);
    }

    public static String evaluateExpression(String expression) {

        try {
            String postFix = convertToPostFix(expression);
            Stack<String> myNums = new Stack<>();
            String[] tokens = postFix.split("\\s+");
            for (int i = 0; i < tokens.length; i++) {

                String token = tokens[i];
                if (isOperator(token)) {
                    myNums.push(evaluate(myNums, token));
                } else
                    myNums.push(token);
            }

            return (myNums.size() == 1) ? myNums.pop() : "SYNTAX ERROR";
        }
        // any kind of strange input will cause us an error and we just return Syntax error like a normal calculator
        catch(Exception e) {
            return "SYNTAX ERROR";
        }
    }
}
