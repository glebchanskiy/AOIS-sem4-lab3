package org.glebchanskiy.aoislab3.logicparser.minimalization.impls;

import org.glebchanskiy.aoislab3.logicparser.FormulasOperations;
import org.glebchanskiy.aoislab3.logicparser.LogicFormula;
import org.glebchanskiy.aoislab3.logicparser.LogicParser;
import org.glebchanskiy.aoislab3.logicparser.minimalization.Minimizator;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;
import org.glebchanskiy.aoislab3.logicparser.util.TruthTable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TabularMethod implements Minimizator {

    @Override
    public List<List<String>> minimise(List<List<String>> constituents, FormulaType type) {
        LogicFormula formula = LogicParser.parse(FormulasOperations.fromListToString(constituents, (type == FormulaType.PDNF)));
        return minimiseByKarno(makeKarnoTable(formula.getTruthTable(), type), type);
    }

    private List<List<String>> minimiseByKarno(List<List<Boolean>> karnoTable, FormulaType type) {
        printTable(karnoTable);
        return validate(mapToImplicants(filterRedundancy(filterOccurrences(getAllOccurrences(toStingTable(karnoTable), getTemplates())))), type);
    }

    private List<List<String>> validate(List<List<String>> result, FormulaType type) {
        return type == FormulaType.PDNF ? reverse(result) : result;
    }

    private List<List<String>> mapToImplicants(List<String> occurrences) {
        Map<String, List<String>> mappings = getMappings();
        return occurrences.stream()
                .map(mappings::get)
                .toList();
    }

    private List<List<String>> reverse(List<List<String>> toReverse) {
        return toReverse.stream()
                .map(implicant -> implicant.stream()
                        .map(term -> term.startsWith("!") ? term.substring(1) : '!' + term)
                        .toList())
                .toList();
    }

    private List<String> filterRedundancy(List<String> occurrences) {
        List<String> result = new ArrayList<>(occurrences);

        for (String oc1 : occurrences) {
            for (String oc2 : occurrences) {
                for (String oc3 : occurrences) {
                    if (!oc2.equals(oc3) && !oc1.equals(oc2) && !oc1.equals(oc3))
                        if (isOccurrence(glue(oc3, oc2), oc1))
                            result.remove(oc1);
                }
            }
        }
        return result;
    }

    private String glue(String first, String second) {
        StringBuilder glued = new StringBuilder();
        glued.append("00000000");
        for (int i = 0; i<8; i++) {
            if (first.charAt(i) == '1' || second.charAt(i) == '1')
                glued.setCharAt(i, '1');
        }
        return glued.toString();
    }

    private List<String> filterOccurrences(List<String> occurrences) {
        return occurrences.stream()
                .filter(f -> occurrences.stream()
                        .noneMatch(o -> !o.equals(f) && isOccurrence(o, f)))
                .toList();
    }

    private List<String> getAllOccurrences(String karnoTable, List<String> templates) {
        return templates.stream()
                .filter(template -> isOccurrence(karnoTable, template))
                .toList();
    }

    private boolean isOccurrence(String karnoTable, String template) {
        int karnoBits = Integer.parseInt(karnoTable, 2);  // вернёт true, при условии что karnoBits содержит
        int templateBits = Integer.parseInt(template, 2); // все биты, из templateBits
        return (karnoBits & templateBits) == templateBits;
    }

    private List<List<Boolean>> makeKarnoTable(TruthTable truthTable, FormulaType type) {
        return fusionKarno(convertTruthTableToMap(truthTable, type), getHelperTable());
    }

    private List<List<Boolean>> fusionKarno(Map<String, Boolean> truthTableMap, Map<List<Integer>, String> helper) {
        return IntStream.range(0, 2)
                .mapToObj(i -> IntStream.range(0, 4)
                        .mapToObj(j -> truthTableMap.get(helper.get(List.of(i, j))))
                        .toList())
                .toList();

    }

    private Map<String, Boolean> convertTruthTableToMap(TruthTable truthTable, FormulaType type) {
        Boolean[] indexes = truthTable.getIndex();
        List<Boolean[]> rows = truthTable.getAllRows();

        Map<String, Boolean> map = new HashMap<>();

        for (Boolean[] row : rows) {
            map.put(boolToString(row), type == FormulaType.PDNF ? indexes[rows.indexOf(row)] : !indexes[rows.indexOf(row)]);
        }
        return map;
    }

    private String boolToString(Boolean[] row) {
        return boolStreamToString(Arrays.stream(row));
    }

    private String boolToString(List<Boolean> row) {
        return boolStreamToString(row.stream());
    }

    private String boolStreamToString(Stream<Boolean> stream) {
        return stream.map(b -> b ? 1 : 0)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private String toStingTable(List<List<Boolean>> karnoTable) {
        return karnoTable.stream()
                .map(this::boolToString)
                .collect(Collectors.joining());
    }

    private List<String> getTemplates() {
        return List.of(
                "11111111", "11110000", "00001111",
                "11001100", "01100110", "00110011",
                "10011001", "11000000", "01100000",
                "00110000", "10010000", "00001100",
                "00000110", "00000011", "00001001",
                "10001000", "01000100", "00100010",
                "00010001", "10000000", "01000000",
                "00100000", "00010000", "00001000",
                "00000100", "00000010", "00000001"
        );
    }

    private Map<String, List<String>> getMappings() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("11111111", List.of());
        map.put("11110000", List.of("A"));
        map.put("00001111", List.of("!A"));
        map.put("11001100", List.of("B"));
        map.put("01100110", List.of("!C"));
        map.put("00110011", List.of("!B"));
        map.put("10011001", List.of("C"));

        map.put("11000000", List.of("A", "B"));
        map.put("01100000", List.of("A", "!C"));
        map.put("00110000", List.of("A", "!B"));
        map.put("10010000", List.of("A", "C"));

        map.put("00001100", List.of("!A", "B"));
        map.put("00000110", List.of("!A", "!C"));
        map.put("00000011", List.of("!A", "!B"));
        map.put("00001001", List.of("!A", "C"));

        map.put("10001000", List.of("B", "C"));
        map.put("01000100", List.of("B", "!C"));
        map.put("00100010", List.of("!B", "!C"));
        map.put("00010001", List.of("!B", "C"));

        map.put("10000000", List.of("A", "B", "C"));
        map.put("01000000", List.of("A", "B", "!C"));
        map.put("00100000", List.of("A", "!B", "!C"));
        map.put("00010000", List.of("A", "!B", "C"));
        map.put("00001000", List.of("!A", "B", "C"));
        map.put("00000100", List.of("!A", "B", "!C"));
        map.put("00000010", List.of("!A", "!B", "!C"));
        map.put("00000001", List.of("!A", "!B", "C"));
        return map;
    }

    private Map<List<Integer>, String> getHelperTable() {
        Map<List<Integer>, String> helper = new HashMap<>();

        helper.put(List.of(0, 0), "000");
        helper.put(List.of(0, 1), "001");
        helper.put(List.of(0, 2), "011");
        helper.put(List.of(0, 3), "010");
        helper.put(List.of(1, 0), "100");
        helper.put(List.of(1, 1), "101");
        helper.put(List.of(1, 2), "111");
        helper.put(List.of(1, 3), "110");
        return helper;
    }

    public void printTable(List<List<Boolean>> karnoTable) {

        StringBuilder output = new StringBuilder();

        output.append("-------------------".repeat(karnoTable.get(0).size()))
                .append('\n').append('\t').append('|');

        output.append("\t");
        for (String term : List.of("B", "B", "!B", "!B")) {
            output.append(term).append("\t\t");
        }

        output.append('\n').append('\t').append('|');

        output.append("\t");
        for (String term : List.of("C", "!C", "!C", "C")) {
            output.append(term).append("\t\t");
        }

        output.append('\n').append("-------------------".repeat(karnoTable.get(0).size()))
                .append('\n');

        for (int i = 0; i < karnoTable.size(); i++) {
            if (i == 0) output.append("A\t").append('|').append('\t');
            if (i == 1) output.append("!A\t").append('|').append('\t');
            for (int j = 0; j < karnoTable.get(i).size(); j++) {
                output.append(Boolean.TRUE.equals(karnoTable.get(i).get(j)) ? 1 : 0).append("\t\t");
            }
            output.append('\n');
        }
        output.append('\n');

        System.out.println(output);
    }
}
