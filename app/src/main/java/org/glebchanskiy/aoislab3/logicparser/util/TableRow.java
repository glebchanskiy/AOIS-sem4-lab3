package org.glebchanskiy.aoislab3.logicparser.util;

public final class TableRow {
    private final Boolean[] variablesValue;
    private final Boolean functionValue;

    public TableRow(Boolean[] variablesValue, Boolean functionValue) {
        this.variablesValue = variablesValue;
        this.functionValue = functionValue;
    }

    public Boolean[] getVariablesValues() {
        return variablesValue.clone();
    }

    public Boolean isFunctionTakeTrueValue() {
        return functionValue;
    }
}
