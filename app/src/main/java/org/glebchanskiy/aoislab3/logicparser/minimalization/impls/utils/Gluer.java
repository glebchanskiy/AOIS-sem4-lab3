package org.glebchanskiy.aoislab3.logicparser.minimalization.impls.utils;

import java.util.*;

public class Gluer {
    public List<List<String>> gluing(List<List<String>> constituents) {
        Set<List<String>> result = new LinkedHashSet<>(constituents);

        for (List<String> implicantI : constituents) {
            for (List<String> implicantJ : constituents) {
                if (implicantI == implicantJ) // достаточно сравнения по ссылке
                    continue;

                List<String> fusion = tryGluing(implicantI, implicantJ);

                if (fusion != null) {
                    result.remove(implicantI);
                    result.remove(implicantJ);
                    result.add(fusion);
                }
            }
        }

        return result.stream().toList();
    }

    private List<String> tryGluing(List<String> firstImplicant, List<String> secondImplicant) {
        int differencesCount = 0;
        int differenceIndex = -1;

        for (int i = 0; i < firstImplicant.size(); i++) {
            String term1 = firstImplicant.get(i);
            String term2 = secondImplicant.get(i);

            if (!term1.equals(term2)) {
                if (isOpposite(term1, term2)) {
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
            result.remove(differenceIndex);
            return result;
        } else {
            return null;
        }
    }

    private boolean isOpposite(String firstTerm, String secondTerm) {
        return ("!" + firstTerm).equals(secondTerm) || (firstTerm.equals("!" + secondTerm));
    }
}
