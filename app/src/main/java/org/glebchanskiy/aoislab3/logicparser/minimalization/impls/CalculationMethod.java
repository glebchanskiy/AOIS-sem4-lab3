package org.glebchanskiy.aoislab3.logicparser.minimalization.impls;


import org.glebchanskiy.aoislab3.logicparser.minimalization.Minimizator;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;

import java.util.*;
import java.util.stream.Collectors;

public class CalculationMethod implements Minimizator {
    @Override
    public List<List<String>> minimise(List<List<String>> constituents, FormulaType type) {
        List<List<String>> gl = gluing(constituents);
        System.out.println(type.name() + " after gluing:" + gl);
        gl = removeRedundancy(gl, type);
        gl = gluing(gl);
        return gl;
    }


    public List<List<String>> gluing(List<List<String>> constituents) {
        Set<List<String>> result = new HashSet<>(constituents);
        Set<List<String>> exclude = new HashSet<>();

        for (int i = 0; i < constituents.size(); i++) {
            List<String> implicantI = constituents.get(i);

            for (int j = 0; j < constituents.size(); j++) {
                if (i == j)
                    continue;
                List<String> implicantJ = constituents.get(j);
                List<String> fusion = tryGluing(implicantI, implicantJ);

//                System.out.println("try gl:  " + implicantI + " : " + implicantJ + "   ->  " + fusion);
                if (fusion != null) {
//                    System.out.println("add: " + fusion);
                    exclude.add(implicantI);
                    exclude.add(implicantJ);
                    result.add(fusion);

                }
            }
        }
        result.removeAll(exclude);
        return result.stream().toList();
    }

    public boolean isOpposite(String firstTerm, String secondTerm) {
//        System.out.println(firstTerm + "  " + secondTerm);
        return Objects.equals("!"+firstTerm, secondTerm) || Objects.equals(firstTerm, "!" + secondTerm);
    }

    public List<String> tryGluing(List<String> firstImplicant, List<String> secondImplicant) {
        int differencesCount = 0;
        int differenceIndex = -1;
        if (firstImplicant.size() == 1 || secondImplicant.size() == 1)
            return null;

        for (int i = 0; i < firstImplicant.size(); i++) {
            String term1 = firstImplicant.get(i);
            String term2 = secondImplicant.get(i);

            if (!term1.equals(term2)) {
                if (isOpposite(term1, term2)) {
//
                    differencesCount++;
                    differenceIndex = i;

                    if (differencesCount > 1) {
                        return null;
                    }
                } else return null;
            }
        }

        if (differencesCount == 1) {
            List<String> result = new ArrayList<>(firstImplicant);
            result.remove(firstImplicant.get(differenceIndex));
            return result;
        } else {
            return null;
        }
    }

    public List<List<String>> removeRedundancy(List<List<String>> constituents, FormulaType type) {
        if (constituents.size() == 1)
            return constituents;

        List<List<String>> result = new ArrayList<>(constituents);
        Set<List<String>> exclude = new HashSet<>();


        for (int i = 0; i < constituents.size(); i++) {
            List<String> implicantI = constituents.get(i);
            Map<String, Integer> valuesMap = createValuesMap(implicantI, type);

            boolean formulaValue = formulaValue(
                    constituents.stream().filter(c -> c != implicantI).collect(Collectors.toList()), valuesMap, type);

            if (formulaValue) {
                exclude.add(implicantI);
            }
        }
//        System.out.println("before remove: " + result);
        result.removeAll(exclude);
//        System.out.println("after remove: " + result);
        return result;
    }

    private boolean formulaValue(List<List<String>> constituents, Map<String, Integer> valuesMap, FormulaType type) {
        List<Boolean> tempList = new ArrayList<>(List.of(false));
        List<Boolean> test = new ArrayList<>();
//        System.out.println( "impl:" + constituents);

        for (List<String> implicant : constituents) {
            tempList.add(implicantValue(implicant, valuesMap, type));

//            if (tempList.size() == 2) {
            if(type == FormulaType.PDNF) {
                tempList.set(0, tempList.get(0) && tempList.get(1));
                test.add(tempList.get(0) && tempList.get(1));
            }
            else {
                tempList.set(0, tempList.get(0) || tempList.get(1));
                test.add(tempList.get(0) || tempList.get(1));
            }
            tempList.remove(1);
//            }
        }
//        System.out.println("bools: " + test);
        return type == FormulaType.PDNF ? tempList.get(0) : !tempList.get(0);
    }

    public Map<String, Integer> createValuesMap(List<String> implicant, FormulaType type) {
        Map<String, Integer> valuesMap = new HashMap<>();

        for (String term : implicant) {
            if (term.startsWith("!")) {
                if (type == FormulaType.PDNF) {
                    valuesMap.put(term, 1);
                    valuesMap.put(term.substring(1), 0);
                } else if (type == FormulaType.PCNF) {
                    valuesMap.put(term, 0);
                    valuesMap.put(term.substring(1), 1);
                }
            } else {
                if (type == FormulaType.PDNF) {
                    valuesMap.put(term, 1);
                    valuesMap.put("!" + term, 0);
                } else if (type == FormulaType.PCNF) {
                    valuesMap.put(term, 0);
                    valuesMap.put("!" + term, 1);
                }
            }
        }
        return valuesMap;
    }

//    public Map<String, Integer> createValuesMap(List<List<String>> constituents, List<String> implicantI) {
//        Map<String, Integer> valuesMap = new HashMap<>();
//        for (List<String> implicant : constituents) {
//            for (String term : implicant) {
//                if (term.startsWith("!")) {
//                    valuesMap.put(term, 1);
//                    valuesMap.put(term.substring(1), 0);
//                } else {
//                    valuesMap.put(term, 1);
//                    valuesMap.put("!" + term, 0);
//                }
//            }
//        }
//        return valuesMap;
//    }

    public boolean implicantValue(List<String> implicant, Map<String, Integer> valuesMap, FormulaType type) {
        switch (type) {
            case PDNF -> {
                return disjunction(implicant, valuesMap) == 1;
            }
            case PCNF -> {
                return conjunction(implicant, valuesMap) == 0;
            }
            default -> {
                return false;
            }
        }
    }

    public Integer disjunction(List<String> implicant, Map<String, Integer> valuesMap) {
        Integer first = valuesMap.get(implicant.get(0));
        Integer second = valuesMap.get(implicant.get(0));
//        System.out.println("dis(" + valuesMap.get(implicant.get(0)) + ":" + valuesMap.get(implicant.get(1)) + ")");
        if (first == null) {
            first = implicant.get(0).startsWith("!") ? 1 : 0;
        }
        if (second == null) {
            second = implicant.get(1).startsWith("!") ? 1 : 0;
        }

        return first * second;
    }

    public Integer conjunction(List<String> implicant, Map<String, Integer> valuesMap) {
        Integer first = valuesMap.get(implicant.get(0));
        Integer second = valuesMap.get(implicant.get(0));
//        System.out.println("dis(" + valuesMap.get(implicant.get(0)) + ":" + valuesMap.get(implicant.get(1)) + ")");
        if (first == null) {
            first = implicant.get(0).startsWith("!") ? 1 : 0;
        }
        if (second == null) {
            second = implicant.get(1).startsWith("!") ? 1 : 0;
        }

        return first + second >= 1 ? 1 : 0;
    }
}
