package org.glebchanskiy.aoislab3.logicparser.minimalization.impls;

import org.glebchanskiy.aoislab3.logicparser.minimalization.Minimizator;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.utils.Gluer;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CalculationTabularMethod implements Minimizator {

    Gluer gluer;

    public CalculationTabularMethod() {
        this.gluer = new Gluer();
    }

    @Override
    public List<List<String>> minimise(List<List<String>> constituents, FormulaType type) {
        return quine(
                gluer.gluing(gluer.gluing(constituents)),
                constituents
        );
    }

    public List<List<String>> quine(List<List<String>> simpleImplicants, List<List<String>> constituents) {
        List<List<Boolean>> implicantTable = makeImplicantTable(simpleImplicants, constituents);
        printImplicantTable(implicantTable, constituents, simpleImplicants);
        return findUniqueImplicants(
                simpleImplicants,
                implicantTable
        );
    }

    public List<List<String>> findUniqueImplicants(List<List<String>> simpleImplicants, List<List<Boolean>> implicantTable) {
        return simpleImplicants.stream()
                .filter(implicant -> isImplicantUnique(simpleImplicants.indexOf(implicant), implicantTable))
                .toList();
    }

    private boolean isImplicantUnique(int implicantIndex, List<List<Boolean>> implicantTable) {
        return IntStream.range(0, implicantTable.get(0).size())
                .anyMatch(constituentIndex -> isMatchFound(implicantIndex, constituentIndex, implicantTable));
    }

    public boolean isMatchFound(int implicantIndex, int constituentIndex, List<List<Boolean>> implicantTable) {
        return Stream.concat(
                        implicantTable.subList(implicantIndex, implicantTable.size()).stream(),
                        implicantTable.subList(0, implicantIndex).stream()
                )
                .filter(list -> list.get(constituentIndex) && implicantTable.get(implicantIndex).get(constituentIndex))
                .count() == 1;
    }

    private List<List<Boolean>> makeImplicantTable(List<List<String>> simpleImplicants, List<List<String>> constituents) {
        return simpleImplicants.stream()
                .map(simpleImplicant -> constituents.stream()
                        .map(implicant -> isImplicantContainsSimpleImplicant(simpleImplicant, implicant))
                        .toList()
                ).toList();
    }

    private boolean isImplicantContainsSimpleImplicant(List<String> simpleImplicant, List<String> implicant) {
        return new HashSet<>(implicant).containsAll(simpleImplicant);
    }

    private void printImplicantTable(List<List<Boolean>> implicantTable, List<List<String>> constituents, List<List<String>> simpleImplicants) {
        StringBuilder output = new StringBuilder();

        output.append("-------------------".repeat(constituents.size())).append('\n');

        output.append('\t').append('|');
        // исходные конституэнты
        for (List<String> implicant : constituents)
            output.append('\t').append(implicant);

        output.append('\n').append("-------------------".repeat(constituents.size())).append('\n');

        Iterator<List<Boolean>> row = implicantTable.listIterator();
        Iterator<List<String>> simpleImplicant = simpleImplicants.listIterator();

        while (row.hasNext()) {
            output.append(simpleImplicant.next()).append('\t').append('|').append('\t');

            for (Boolean values : row.next())
                output.append("   ").append(Boolean.TRUE.equals(values) ? 1 : 0).append("\t\t");
            output.append('\n');
        }
        System.out.println(output);
    }
}
