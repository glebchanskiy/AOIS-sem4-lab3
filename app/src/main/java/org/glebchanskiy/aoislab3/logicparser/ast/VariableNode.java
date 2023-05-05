package org.glebchanskiy.aoislab3.logicparser.ast;

public class VariableNode extends Node {
    private final String name;

    public VariableNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
