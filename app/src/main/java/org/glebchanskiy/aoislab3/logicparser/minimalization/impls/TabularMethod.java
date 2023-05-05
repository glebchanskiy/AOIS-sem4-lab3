package org.glebchanskiy.aoislab3.logicparser.minimalization.impls;

import org.glebchanskiy.aoislab3.logicparser.FormulasOperations;
import org.glebchanskiy.aoislab3.logicparser.LogicFormula;
import org.glebchanskiy.aoislab3.logicparser.LogicParser;
import org.glebchanskiy.aoislab3.logicparser.minimalization.Minimizator;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.karnoutil.DataTable;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;

import java.util.*;

public class TabularMethod implements Minimizator {

    @Override
    public List<List<String>> minimise(List<List<String>> constituents, FormulaType type){
        initHelpIndexes();
        LogicFormula formula = LogicParser.parse(FormulasOperations.fromListToString(constituents, (type == FormulaType.PDNF ? true : false)));
        var truthTable = formula.getTruthTable().getAllRows();
        var tableIndex = formula.getTruthTable().getIndex();
        var symbols = formula.getVariables();

        Map<List<Integer>, Integer> map = new HashMap<>();
        for (int i = 0; i < truthTable.size(); i++) {
            map.put(convertBooleanArrayToIntList(truthTable.get(i)), Boolean.TRUE.equals(tableIndex[i]) ? 1 : 0);
        }

        List<List<String>> output = new ArrayList<>();

        int[][] karnoTable = new int[2][4];

        for(int i =0; i< karnoTable.length; i++){
            for(int j =0; j < karnoTable[i].length; j++){
                karnoTable[i][j] = map.get(helpIndexes.get(Arrays.asList(i,j)));
            }
        }

        DataTable table = new DataTable(karnoTable);

        if (type == FormulaType.PCNF) {
            table.reverse();
        }


        List<DataTable> figures = new ArrayList<>();
        for(DataTable dataTable: tables){
            if (table.contains(dataTable)){
                if(!figures.isEmpty()) {
                    List<Boolean> booleans = new ArrayList<>();
                    for (DataTable figure : figures) {
                        booleans.add(figure.contains(dataTable));
                    }
                    if(!booleans.contains(true)){
                        figures.add(dataTable);
                    }
                }
                else {
                    figures.add(dataTable);
                }
            }
        }

        for(DataTable dataTable: figures){
            List<List<Integer>> indexes = new ArrayList<>();
            for(int i =0; i < dataTable.table.length; i++){
                for(int j =0; j < dataTable.table[i].length; j++){
                    if(dataTable.table[i][j] == 1){
                        indexes.add(helpIndexes.get(Arrays.asList(i, j)));
                    }
                }
            }

            Map<Integer, Integer> commonIndexes = new HashMap<>();

            for (int i = 0; i < indexes.get(0).size(); i++) {
                boolean isCommon = true;
                int commonValue = indexes.get(0).get(i);
                for (int j = 1; j < indexes.size(); j++) {
                    if (indexes.get(j).get(i) != commonValue) {
                        isCommon = false;
                        break;
                    }
                }
                if (isCommon) {
                    commonIndexes.put(i, commonValue);
                }
            }
            List<String> stringIndex = new ArrayList<>();
            for(Integer index: commonIndexes.keySet()) {
                if(type == FormulaType.PDNF) {
                    if (commonIndexes.get(index) == 1) {
                        stringIndex.add(symbols.get(index));
                    } else {
                        stringIndex.add("!" + symbols.get(index));
                    }
                }
                else {
                    if (commonIndexes.get(index) == 0) {
                        stringIndex.add(symbols.get(index));
                    } else {
                        stringIndex.add("!" + symbols.get(index));
                    }
                }
            }
            output.add(stringIndex);

        }
        print(table);
        return output;
    }

    public List<Integer> convertBooleanArrayToIntList(Boolean[] booleanArray) {
        List<Integer> intList = new ArrayList<>();

        for (boolean bool : booleanArray) {
            intList.add(Boolean.TRUE.equals(bool) ? 1 : 0);
        }

        return intList;
    }

    public void print(DataTable table){
        List<String> leftSymbols = new ArrayList<>(Arrays.asList("!A", "A"));

        List<List<String>> terms = List.of(List.of("!B", "!C"), List.of("!B", "C"), List.of("B", "C"), List.of("B", "!C"));
        System.out.print("\t");
        for(List<String> term: terms){
            System.out.print(term + "\t\t");
        }
        System.out.println();
        Iterator<String> iterator = leftSymbols.iterator();
        for(int i =0; i < table.table.length; i++){
            System.out.print(iterator.next());
            for(int j =0; j< table.table[i].length; j++){
                System.out.print("\t\t" + table.table[i][j]);
            }
            System.out.println();
        }
    }

    private static void initHelpIndexes(){
        helpIndexes.put(Arrays.asList(0,0), Arrays.asList(0,0,0));
        helpIndexes.put(Arrays.asList(0,1), Arrays.asList(0,0,1));
        helpIndexes.put(Arrays.asList(0,2), Arrays.asList(0,1,1));
        helpIndexes.put(Arrays.asList(0,3), Arrays.asList(0,1,0));
        helpIndexes.put(Arrays.asList(1,0), Arrays.asList(1,0,0));
        helpIndexes.put(Arrays.asList(1,1), Arrays.asList(1,0,1));
        helpIndexes.put(Arrays.asList(1,2), Arrays.asList(1,1,1));
        helpIndexes.put(Arrays.asList(1,3), Arrays.asList(1,1,0));
    }

    private static final Map<List<Integer>, List<Integer>> helpIndexes = new HashMap<>();
    private static final List<DataTable> tables = List.of(
            new DataTable(new int[][]{{1, 1, 1, 1}, {1, 1, 1, 1}}),
            new DataTable(new int[][]{{1, 1, 1, 1}, {0, 0, 0, 0}}),
            new DataTable(new int[][]{{0, 0, 0, 0}, {1, 1, 1, 1}}),
            new DataTable(new int[][]{{1, 1, 0, 0}, {1, 1, 0, 0}}),
            new DataTable(new int[][]{{0, 1, 1, 0}, {0, 1, 1, 0}}),
            new DataTable(new int[][]{{0, 0, 1, 1}, {0, 0, 1, 1}}),
            new DataTable(new int[][]{{1, 0, 0, 1}, {1, 0, 0, 1}}),
            new DataTable(new int[][]{{1, 1, 0, 0}, {0, 0, 0, 0}}),
            new DataTable(new int[][]{{0, 1, 1, 0}, {0, 0, 0, 0}}),
            new DataTable(new int[][]{{0, 0, 1, 1}, {0, 0, 0, 0}}),
            new DataTable(new int[][]{{1, 0, 0, 1}, {0, 0, 0, 0}}),
            new DataTable(new int[][]{{0, 0, 0, 0}, {1, 1, 0, 0}}),
            new DataTable(new int[][]{{0, 0, 0, 0}, {0, 1, 1, 0}}),
            new DataTable(new int[][]{{0, 0, 0, 0}, {0, 0, 1, 1}}),
            new DataTable(new int[][]{{0, 0, 0, 0}, {1, 0, 0, 1}}),
            new DataTable(new int[][]{{1, 0, 0, 0}, {1, 0, 0, 0}}),
            new DataTable(new int[][]{{0, 1, 0, 0}, {0, 1, 0, 0}}),
            new DataTable(new int[][]{{0, 0, 1, 0}, {0, 0, 1, 0}}),
            new DataTable(new int[][]{{0, 0, 0, 1}, {0, 0, 0, 1}}),
            new DataTable(new int[][]{{1, 0, 0, 0}, {0, 0, 0, 0}}),
            new DataTable(new int[][]{{0, 1, 0, 0}, {0, 0, 0, 0}}),
            new DataTable(new int[][]{{0, 0, 1, 0}, {0, 0, 0, 0}}),
            new DataTable(new int[][]{{0, 0, 0, 1}, {0, 0, 0, 0}}),
            new DataTable(new int[][]{{0, 0, 0, 0}, {1, 0, 0, 0}}),
            new DataTable(new int[][]{{0, 0, 0, 0}, {0, 1, 0, 0}}),
            new DataTable(new int[][]{{0, 0, 0, 0}, {0, 0, 1, 0}}),
            new DataTable(new int[][]{{0, 0, 0, 0}, {0, 0, 0, 1}}));
}
