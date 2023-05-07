package org.glebchanskiy.aoislab3.logicparser.minimalization.impls;


import org.glebchanskiy.aoislab3.logicparser.minimalization.Minimizator;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.utils.Gluer;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;

import java.util.*;

public class CalculationMethod implements Minimizator {

    Gluer gluer;

    public CalculationMethod() {
        this.gluer = new Gluer();
    }

    @Override
    public List<List<String>> minimise(List<List<String>> constituents, FormulaType type) {
        List<List<String>> glued = gluer.gluing(constituents);
        System.out.println(type.name() + " after gluing:" + glued); // для вывода ответа
        return gluer.gluing(removeRedundancy(glued, type));
    }

    public List<List<String>> removeRedundancy(List<List<String>> constituents, FormulaType type) {
        if (constituents.size() == 1) return constituents;

        List<List<String>> result = new ArrayList<>(constituents);

        for (List<String> implicant : constituents) {
            // удаляем, если импликант был избыточным для формулы
            if (isSelfSufficientFormula(excludeImplicant(result, implicant), createValuesMap(implicant, type), type)) {
                result.remove(implicant);
            }
        }

        return result;
    }


    private boolean isSelfSufficientFormula(List<List<String>> constituents,
                                            Map<String, Boolean> implicantVariableValues,
                                            FormulaType type) {
        if (constituents.isEmpty())
            return false;

        boolean formulaValue = false;

        for (List<String> implicant : constituents) {
            formulaValue = formulaValue || implicantValue(implicant, implicantVariableValues, type);
        }
        return formulaValue;
    }

    public Map<String, Boolean> createValuesMap(List<String> implicant, FormulaType type) {
        Map<String, Boolean> valuesMap = new HashMap<>();

        for (String term : implicant) {
            if (term.startsWith("!")) {
                if (type == FormulaType.PDNF) {
                    valuesMap.put(term, true);
                    valuesMap.put(term.substring(1), false);
                } else if (type == FormulaType.PCNF) {
                    valuesMap.put(term, false);
                    valuesMap.put(term.substring(1), true);
                }
            } else {
                if (type == FormulaType.PDNF) {
                    valuesMap.put(term, true);
                    valuesMap.put("!" + term, false);
                } else if (type == FormulaType.PCNF) {
                    valuesMap.put(term, false);
                    valuesMap.put("!" + term, true);
                }
            }
        }
        return valuesMap;
    }

    public boolean implicantValue(List<String> implicant, Map<String, Boolean> valuesMap, FormulaType type) {
        boolean result = switch (type) {
            case PDNF -> disjunction(implicant, valuesMap);
            case PCNF -> conjunction(implicant, valuesMap);
        };
        return type == FormulaType.PDNF ? result : !result;
    }

    public Boolean disjunction(List<String> implicant, Map<String, Boolean> valuesMap) {
        Boolean first = valuesMap.get(implicant.get(0));
        Boolean second = valuesMap.get(implicant.get(1));

        if (first == null) {
            first = implicant.get(0).startsWith("!");
        }
        if (second == null) {
            second = implicant.get(1).startsWith("!");
        }

        return first && second;
    }

    public Boolean conjunction(List<String> implicant, Map<String, Boolean> valuesMap) {
        Boolean first = valuesMap.get(implicant.get(0));
        Boolean second = valuesMap.get(implicant.get(1));

        if (first == null) {
            first = !implicant.get(0).startsWith("!");
        }
        if (second == null) {
            second = !implicant.get(1).startsWith("!");
        }

        return first || second;
    }

    public List<List<String>> excludeImplicant(List<List<String>> constituents, List<String> implicant) {
        return constituents.stream().filter(c -> c != implicant).toList();
    }
}
