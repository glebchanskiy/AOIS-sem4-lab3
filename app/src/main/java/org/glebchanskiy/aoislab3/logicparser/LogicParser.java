package org.glebchanskiy.aoislab3.logicparser;


import org.glebchanskiy.aoislab3.logicparser.ast.*;
import org.glebchanskiy.aoislab3.logicparser.util.TableRow;
import org.glebchanskiy.aoislab3.logicparser.util.TruthTable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogicParser {
    private static int pos;

    private LogicParser() {
    }

    public static LogicFormula parse(String expression) {
        List<String> tokens = tokenize(expression);
        List<String> variables = identifyVariables(tokens);
        Node root = buildAst(tokens);
        TruthTable truthTable = makeTruthTable(variables, root);

        return new LogicFormula(expression, variables, truthTable, root);
    }

    private static List<String> tokenize(String formula) {
        List<String> tokens = new ArrayList<>();

        String regex = "\\(|\\)|!|&|\\||\\w+"; // match for: ( or ) or ! or && or || or \w
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(formula);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        String[] variableNames = formula.split(regex);
        for (String variableName : variableNames) {
            variableName = variableName.trim();
            if (!variableName.isEmpty()) {
                tokens.add(variableName);
            }
        }
        return tokens;
    }

    private static List<String> identifyVariables(List<String> tokens) {
        return tokens.stream()
                .filter(token -> token.matches("\\w+"))
                .distinct()
                .toList();
    }

    private static Node buildAst(List<String> tokens) {
        pos = 0;
        return parseExpression(tokens);
    }

    private static Node parseExpression(List<String> tokens) {
        Node left = parseTerm(tokens);

        if (pos + 1 < tokens.size()) {
            String op = tokens.get(pos + 1);

            if (op.equals("&")) {
                pos += 2;
                Node right = parseExpression(tokens);
                return new AndNode(left, right);
            } else if (op.equals("|")) {
                pos += 2;
                Node right = parseExpression(tokens);
                return new OrNode(left, right);
            }
        }
        return left;
    }

    private static Node parseTerm(List<String> tokens) {
        String token = tokens.get(pos);

        if (token.equals("!")) {
            pos++;
            Node operand = parseTerm(tokens);
            return new NotNode(operand);
        } else if (token.matches("\\w+")) {
            return new VariableNode(token);
        } else if (token.equals("(")) {
            pos++;
            Node expression = parseExpression(tokens);
            pos++;
            return expression;
        } else {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
    }

    private static TruthTable makeTruthTable(List<String> variables, Node root) {
        List<TableRow> tableRows = new ArrayList<>();

        int numCombinations = 1 << variables.size();
        for (int i = 0; i < numCombinations; i++) {
            Boolean[] variablesTruthValue = new Boolean[variables.size()];
            for (int j = 0; j < variables.size(); j++) {
                int bit = (variables.size() - j - 1);
                variablesTruthValue[j] = ((i >> bit) & 1) == 1;
            }

            boolean functionTruthValue = evaluate(root, variablesTruthValue, variables);
            tableRows.add(new TableRow(variablesTruthValue, functionTruthValue));
        }
        return new TruthTable(tableRows);
    }

    private static boolean evaluate(Node node, Boolean[] values, List<String> variables) {
        if (node instanceof VariableNode) {
            VariableNode variableNode = (VariableNode) node;
            int index = findVariableIndex(variableNode.getName(), variables);
            return values[index];
        } else if (node instanceof NotNode) {
            NotNode notNode = (NotNode) node;
            boolean operandValue = evaluate(notNode.getOperand(), values, variables);
            return !operandValue;
        } else if (node instanceof AndNode) {
            AndNode andNode = (AndNode) node;
            boolean leftValue = evaluate(andNode.getLeft(), values, variables);
            boolean rightValue = evaluate(andNode.getRight(), values, variables);
            return leftValue && rightValue;
        } else if (node instanceof OrNode) {
            OrNode orNode = (OrNode) node;
            boolean leftValue = evaluate(orNode.getLeft(), values, variables);
            boolean rightValue = evaluate(orNode.getRight(), values, variables);
            return leftValue || rightValue;
        } else {
            throw new IllegalArgumentException("Invalid node type: " + node.getClass().getName());
        }
    }

    private static int findVariableIndex(String variable, List<String> variables) {
        int index = variables.indexOf(variable);

        if (index == -1)
            throw new IllegalArgumentException("Variable not found: " + variable);

        return index;
    }

}
