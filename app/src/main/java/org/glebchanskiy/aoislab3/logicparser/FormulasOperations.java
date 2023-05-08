package org.glebchanskiy.aoislab3.logicparser;


import org.glebchanskiy.aoislab3.logicparser.ast.*;
import org.glebchanskiy.aoislab3.logicparser.util.TableRow;

import java.util.*;
import java.util.stream.Collectors;


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
                output.append(variable ? "1" : "0").append("    ");
            output.append(row.isFunctionTakeTrueValue() ? "1" : "0").append("\n");
        }

        return output.toString();
    }


    public static String getIndex(LogicFormula logicFormula) {

        String index = Arrays.stream(logicFormula.getTruthTable().getIndex())
                .map(b -> b ? "1" : "0")
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
                output.append(values.get(i)[j] ? "!" + (char)(65+j) : (char)(65+j));
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

        for (int i = 0; i < values.size(); i++) {
            List<String> inner = new ArrayList<>();
            for (int j = 0; j < values.get(i).length; j++) {
                inner.add(values.get(i)[j] ? "!" + (char)(65+j) : (char)(65+j) + "");
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
                output.append(values.get(i)[j] ? (char)(65+j) : "!" + (char)(65+j));
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

        for (int i = 0; i < values.size(); i++) {
            List<String> inner = new ArrayList<>();
            for (int j = 0; j < values.get(i).length; j++) {
                inner.add(Boolean.TRUE.equals(values.get(i)[j]) ? (char)(65+j)+"" : "!" + (char)(65+j));
            }
            pdnf.add(inner);
        }
        return pdnf;
    }

    public static String getPdnfBin(LogicFormula logicFormula) {
        List<Boolean[]> pdnf = logicFormula.getTruthTable().getTruths();
        List<String> pdnf_inner_brackets = new ArrayList<>();

        pdnf.forEach(row ->
                pdnf_inner_brackets.add(
                        Arrays.stream(row)
                                .map(b -> b ? "1" : "0")
                                .reduce((accum, bin) -> accum += bin)
                                .orElse("")
                )
        );

        StringBuilder result = new StringBuilder();

        for (String bracket : pdnf_inner_brackets)
            if (result.isEmpty())
                result.append("PDNF: ").append("(").append(bracket).append(")");
            else
                result.append("|").append("(").append(bracket).append(")");

        return result.toString();

    }

    public static String getPdnfDig(LogicFormula logicFormula) {
        List<Boolean[]> pdnf = logicFormula.getTruthTable().getTruths();
        List<Integer> pdnf_inner_brackets_dig = new ArrayList<>();

        pdnf.forEach(row ->
                pdnf_inner_brackets_dig.add(
                        toDecimal(
                                Arrays.stream(row)
                                        .map(b -> b ? "1" : "0")
                                        .reduce((accum, bin) -> accum += bin)
                                        .orElse("")
                        )
                )
        );

        return "PDNF: " + pdnf_inner_brackets_dig;
    }

    public static String getPcnfBin(LogicFormula logicFormula) {
        List<Boolean[]> pcnf = logicFormula.getTruthTable().getFalses();
        List<String> pcnf_inner_brackets = new ArrayList<>();

        pcnf.forEach(row ->
                pcnf_inner_brackets.add(
                        Arrays.stream(row)
                                .map(b -> b ? "1" : "0")
                                .reduce((accum, bin) -> accum += bin)
                                .orElse("")
                )
        );

        StringBuilder result = new StringBuilder();

        for (String bracket : pcnf_inner_brackets)
            if (result.isEmpty())

                result.append("PCNF: ").append("(").append(bracket).append(")");
            else
                result.append("&").append("(").append(bracket).append(")");

        return result.toString();

    }

    public static String getPcnfDig(LogicFormula logicFormula) {
        List<Boolean[]> pcnf = logicFormula.getTruthTable().getFalses();
        List<Integer> pcnf_inner_brackets_dig = new ArrayList<>();

        pcnf.forEach(row ->
                pcnf_inner_brackets_dig.add(
                        toDecimal(
                                Arrays.stream(row)
                                        .map(b -> b ? "1" : "0")
                                        .reduce((accum, bin) -> accum += bin)
                                        .orElse("")
                        )
                )
        );

        return "PCNF: " + pcnf_inner_brackets_dig;
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

    public static String fromListToString(List<List<String>> formula, boolean isPdnf) {
        Set<List<String>> orderedFormula = new TreeSet<>(
                (list1, list2) -> {
                    int size1 = list1.size();
                    int size2 = list2.size();
                    int minSize = Math.min(size1, size2);
                    if (size1 == size2) {
                        for (int i = 0; i < minSize; i++) {
                            int cmp = list1.get(i).compareTo(list2.get(i));
                            if (cmp != 0) {
                                return cmp;
                            }
                        }
                    }
                    return Integer.compare(size1, size2);
                }
                );

        orderedFormula.addAll(formula);

        String internalConnection = isPdnf ? "&" : "|";
        String externalConnection = !isPdnf ? "&" : "|";
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
