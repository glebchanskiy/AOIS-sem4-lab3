package org.glebchanskiy.aoislab3.logicparser.ast;

public abstract class BinaryOperationNode extends Node {
    private final Node left;
    private final Node right;

    public BinaryOperationNode(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }
}
