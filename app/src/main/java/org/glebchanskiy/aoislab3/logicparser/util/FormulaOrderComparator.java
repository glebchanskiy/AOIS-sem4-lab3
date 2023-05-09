package org.glebchanskiy.aoislab3.logicparser.util;

import java.util.Comparator;
import java.util.List;

public class FormulaOrderComparator implements Comparator<List<String>> {
    @Override
    public int compare(List<String> list1, List<String> list2) {
        int size1 = list1.size();
        int size2 = list2.size();
        int minSize = Math.min(size1, size2);
        if (size1 == size2) {
            for (int i = 0; i < minSize; i++) {
                int cmp = list1.get(i).compareTo(list2.get(i));
                if (cmp != 0) {
                    return cmp;
                }
            }
        }
        return Integer.compare(size1, size2);
    }
}
