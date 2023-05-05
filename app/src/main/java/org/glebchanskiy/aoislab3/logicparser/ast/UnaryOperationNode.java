package org.glebchanskiy.aoislab3.logicparser.ast;

public abstract class UnaryOperationNode extends Node {
    private final Node operand;

    public UnaryOperationNode(Node operand) {
        this.operand = operand;
    }

    public Node getOperand() {
        return operand;
    }
}
