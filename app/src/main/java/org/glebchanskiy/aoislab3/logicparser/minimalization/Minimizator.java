package org.glebchanskiy.aoislab3.logicparser.minimalization;

import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;

import java.util.List;

public interface Minimizator {
    List<List<String>> minimise(List<List<String>> constituents, FormulaType type);
}
