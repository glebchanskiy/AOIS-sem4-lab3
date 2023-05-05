package org.glebchanskiy.aoislab3.logicparser;


import org.glebchanskiy.aoislab3.logicparser.ast.Node;
import org.glebchanskiy.aoislab3.logicparser.util.TruthTable;

import java.util.List;

public class LogicFormula {
    private final String expression;
    private final List<String> variables;
    private final TruthTable truthTable;

    private final Node root;

    public LogicFormula(String expression, List<String> variables, TruthTable truthTable, Node root) {
        this.expression = expression;
        this.variables = variables;
        this.truthTable = truthTable;
        this.root = root;
    }

    public String getExpression() {
        return expression;
    }

    public List<String> getVariables() {
        return variables;
    }

    public TruthTable getTruthTable() {
        return truthTable;
    }

    public Node getAstRoot() {
        return root;
    }
}
