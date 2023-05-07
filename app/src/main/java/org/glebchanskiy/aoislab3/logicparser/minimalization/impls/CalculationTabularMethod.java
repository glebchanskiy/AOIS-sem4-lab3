package org.glebchanskiy.aoislab3.logicparser.minimalization.impls;

import org.glebchanskiy.aoislab3.logicparser.minimalization.Minimizator;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.utils.Gluer;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;

import java.util.*;

public class CalculationTabularMethod implements Minimizator {

    Gluer gluer;

    public CalculationTabularMethod() {
        this.gluer = new Gluer();
    }

    @Override
    public List<List<String>> minimise(List<List<String>> constituents, FormulaType type){
        List<List<String>> glued = gluer.gluing(gluer.gluing(constituents));

        List<List<String>> result =  new ArrayList<>();

        int[][] table = generateTable(glued, constituents);

        print(table, glued, constituents);

        for(int k = 0; k < glued.size(); k++) {
            boolean unique = false;
            for (int i = 0; i < constituents.size(); i++) {
                List<Integer> coincidences = findCoincidences(glued, table, i, k);

                if(coincidences.size() == 1){
                    unique = true;
                }
            }
            if(unique){
                result.add(glued.get(k));
            }
            else {
                for (int i = 0; i < constituents.size(); i++) {
                    table[k][i] = 0;
                }
            }
        }

        return result;
    }

    public void print(int[][] table, List<List<String>>  glued, List<List<String>> constituents){
        System.out.print("\t\t");
        for(List<String> implicant: constituents){
            System.out.print(implicant + "\t");
        }

        System.out.println();

        Iterator<List<String>> iterator = glued.iterator();
        for (int[] ints : table) {
            System.out.print(iterator.next() + "\t");
            for (int anInt : ints) {
                System.out.print("\t\t" + anInt);
            }
            System.out.println();
        }
    }

    private int[][] generateTable(List<List<String>> minimizeList,List<List<String>> constituents){
        int[][] table = new int[minimizeList.size()][constituents.size()];

        for (int[] value : table) {
            Arrays.fill(value, 0);
        }

        for (int i =0; i < minimizeList.size(); i++){
            for(int j = 0; j < constituents.size(); j++){
                if(new HashSet<>(constituents.get(j)).containsAll(minimizeList.get(i))){
                    table[i][j] = 1;
                }
            }
        }
        return table;
    }

    public List<Integer> findCoincidences(List<List<String>> minimizeList, int[][] table, int i, int k){
        List<Integer> coincidences = new ArrayList<>();
        for (int j = k; j < minimizeList.size(); j++) {
            if(table[j][i] == 1 && table[k][i] == 1){
                coincidences.add(1);
            }
        }
        for (int j = 0; j < k; j++) {
            if(table[j][i] == 1 && table[k][i] == 1){
                coincidences.add(1);
            }
        }
        return coincidences;
    }
}
