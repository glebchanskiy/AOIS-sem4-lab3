package org.glebchanskiy.aoislab3.logicparser;


import org.glebchanskiy.aoislab3.logicparser.ast.*;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaOrderComparator;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;
import org.glebchanskiy.aoislab3.logicparser.util.TableRow;

import java.util.*;


public class FormulasOperations {
    private FormulasOperations() {
    }

    public static String getTruthTable(LogicFormula logicFormula) {
        StringBuilder output = new StringBuilder();

        for (String variableName : logicFormula.getVariables())
            output.append(variableName).append("    ");

        output.append(" \n");

        for (TableRow row : logicFormula.getTruthTable()) {
            for (Boolean variable : row.getVariablesValues())
                output.append(Boolean.TRUE.equals(variable) ? "1" : "0").append("    ");
            output.append(Boolean.TRUE.equals(row.isFunctionTakeTrueValue()) ? "1" : "0").append("\n");
        }

        return output.toString();
    }


    public static String getIndex(LogicFormula logicFormula) {

        String index = Arrays.stream(logicFormula.getTruthTable().getIndex())
                .map(b -> Boolean.TRUE.equals(b) ? "1" : "0")
                .reduce((a, b) -> a + b)
                .orElse("");

        return "index: " + index + " : " + toDecimal(index);
    }

    public static String getPcnf(LogicFormula logicFormula) {
        List<Boolean[]> values = logicFormula.getTruthTable().getFalses();

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < values.size(); i++) {
            output.append('(');
            for (int j = 0; j < values.get(i).length; j++) {
                output.append(Boolean.TRUE.equals(values.get(i)[j]) ? "!" + (char)(65+j) : (char)(65+j));
                if (j != values.get(i).length - 1)
                    output.append("|");
            }
            output.append(')');
            if (i != values.size() - 1)
                output.append("&");
        }
        return output.toString();
    }

    public static List<List<String>> getPcnfAsList(LogicFormula logicFormula) {
        List<Boolean[]> values = logicFormula.getTruthTable().getFalses();

        List<List<String>> pcnf = new ArrayList<>();

        for (Boolean[] value : values) {
            List<String> inner = new ArrayList<>();
            for (int j = 0; j < value.length; j++) {
                inner.add(Boolean.TRUE.equals(value[j]) ? "!" + (char) (65 + j) : (char) (65 + j) + "");
            }
            pcnf.add(inner);
        }
        return pcnf;
    }

    public static String getPdnf(LogicFormula logicFormula) {
        List<Boolean[]> values = logicFormula.getTruthTable().getTruths();

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < values.size(); i++) {
            output.append('(');
            for (int j = 0; j < values.get(i).length; j++) {
                output.append(Boolean.TRUE.equals(values.get(i)[j]) ? (char)(65+j) : "!" + (char)(65+j));
                if (j != values.get(i).length - 1)
                    output.append("&");
            }
            output.append(')');
            if (i != values.size() - 1)
                output.append("|");
        }
        return output.toString();
    }

    public static List<List<String>> getPdnfAsList(LogicFormula logicFormula) {
        List<Boolean[]> values = logicFormula.getTruthTable().getTruths();

        List<List<String>> pdnf = new ArrayList<>();

        for (Boolean[] value : values) {
            List<String> inner = new ArrayList<>();
            for (int j = 0; j < value.length; j++) {
                inner.add(Boolean.TRUE.equals(value[j]) ? (char) (65 + j) + "" : "!" + (char) (65 + j));
            }
            pdnf.add(inner);
        }
        return pdnf;
    }

    public static String getPdnfBin(LogicFormula logicFormula) {
        List<Boolean[]> pdnf = logicFormula.getTruthTable().getTruths();
        List<String> pdnfInnerBrackets = new ArrayList<>();

        pdnf.forEach(row ->
                pdnfInnerBrackets.add(
                        Arrays.stream(row)
                                .map(b -> Boolean.TRUE.equals(b) ? "1" : "0")
                                .reduce((accum, bin) -> accum += bin)
                                .orElse("")
                )
        );

        StringBuilder result = new StringBuilder();

        for (String bracket : pdnfInnerBrackets)
            if (result.isEmpty())
                result.append("PDNF: ").append("(").append(bracket).append(")");
            else
                result.append("|").append("(").append(bracket).append(")");

        return result.toString();

    }

    public static String getPdnfDig(LogicFormula logicFormula) {
        List<Boolean[]> pdnf = logicFormula.getTruthTable().getTruths();
        List<Integer> pdnfInnerBracketsDig = new ArrayList<>();

        pdnf.forEach(row ->
                pdnfInnerBracketsDig.add(
                        toDecimal(
                                Arrays.stream(row)
                                        .map(b -> Boolean.TRUE.equals(b) ? "1" : "0")
                                        .reduce((accum, bin) -> accum += bin)
                                        .orElse("")
                        )
                )
        );

        return "PDNF: " + pdnfInnerBracketsDig;
    }

    public static String getPcnfBin(LogicFormula logicFormula) {
        List<Boolean[]> pcnf = logicFormula.getTruthTable().getFalses();
        List<String> pcnfInnerBrackets = new ArrayList<>();

        pcnf.forEach(row ->
                pcnfInnerBrackets.add(
                        Arrays.stream(row)
                                .map(b -> Boolean.TRUE.equals(b) ? "1" : "0")
                                .reduce((accum, bin) -> accum += bin)
                                .orElse("")
                )
        );

        StringBuilder result = new StringBuilder();

        for (String bracket : pcnfInnerBrackets)
            if (result.isEmpty())

                result.append("PCNF: ").append("(").append(bracket).append(")");
            else
                result.append("&").append("(").append(bracket).append(")");

        return result.toString();

    }

    public static String getPcnfDig(LogicFormula logicFormula) {
        List<Boolean[]> pcnf = logicFormula.getTruthTable().getFalses();
        List<Integer> pcnfInnerBracketsDig = new ArrayList<>();

        pcnf.forEach(row ->
                pcnfInnerBracketsDig.add(
                        toDecimal(
                                Arrays.stream(row)
                                        .map(b -> Boolean.TRUE.equals(b) ? "1" : "0")
                                        .reduce((accum, bin) -> accum += bin)
                                        .orElse("")
                        )
                )
        );

        return "PCNF: " + pcnfInnerBracketsDig;
    }

    private static int toDecimal(String row) {
        int decimal = 0;
        int power = 0;
        for (int i = row.length() - 1; i >= 0; i--) {
            int bit = row.charAt(i) - '0';
            decimal += bit * Math.pow(2, power);
            power++;
        }
        return decimal;
    }

    public static String getTree(LogicFormula logicFormula) {
        StringBuilder output = new StringBuilder();
        treeTraversal(logicFormula.getAstRoot(), output, 0);
        return output.toString();
    }

    private static void treeTraversal(Node node, StringBuilder output, int deep) {
        StringBuilder tabs = new StringBuilder();

        tabs.append(" ".repeat(Math.max(0, deep)));

        if (node instanceof VariableNode)
            output.append(tabs).append(((VariableNode) node).getName()).append("\n");
        if (node instanceof UnaryOperationNode) {
            output.append(tabs).append("!").append("\n");
            treeTraversal(((UnaryOperationNode) node).getOperand(), output, deep + 3);
        }
        if (node instanceof BinaryOperationNode) {
            output.append(tabs).append("[").append("\n");
            treeTraversal(((BinaryOperationNode) node).getLeft(), output, deep + 3);
            if (node instanceof AndNode)
                output.append(tabs).append("and").append("\n");
            else if (node instanceof OrNode)
                output.append(tabs).append("or").append("\n");
            treeTraversal(((BinaryOperationNode) node).getRight(), output, deep + 3);
            output.append(tabs).append("]").append("\n");
        }
    }

    public static String fromListToString(List<List<String>> formula, FormulaType type) {
        Set<List<String>> orderedFormula = new TreeSet<>(new FormulaOrderComparator());

        orderedFormula.addAll(formula);

        String internalConnection = type == FormulaType.PDNF ? "&" : "|";
        String externalConnection = type == FormulaType.PCNF ? "&" : "|";

        StringBuilder output = new StringBuilder();
        for (List<String> i : orderedFormula) {
            output.append("(");
            for (String j : i) {
                output.append(j);
                output.append(internalConnection);
            }
            output.replace(output.length() - 1, output.length(), "");
            output.append(")");
            output.append(externalConnection);
        }
        output.replace(output.length() - 1, output.length(), "");
        return output.toString();
    }
}

