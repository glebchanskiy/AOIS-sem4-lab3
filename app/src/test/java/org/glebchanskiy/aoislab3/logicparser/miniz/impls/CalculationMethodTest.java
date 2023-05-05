package org.glebchanskiy.aoislab3.logicparser.miniz.impls;

import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.CalculationMethod;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculationMethodTest {
    static CalculationMethod minimizator;
    static List<List<String>> example1;
    static List<List<String>> example1Result;
    static List<List<String>> example2;
    static List<List<String>> example2Result;

    @BeforeAll
    static void setUp() {
        minimizator = new CalculationMethod();

        // (!a !b c) (!a b !c) (!a b c) (a !b c)
        example1 = new ArrayList<>(List.of(
                new ArrayList<>(List.of("!a", "!b", "c")),
                new ArrayList<>(List.of("!a", "b", "!c")),
                new ArrayList<>(List.of("!a", "b", "c")),
                new ArrayList<>(List.of("a", "!b", "c"))
        ));

        // (!a !b !c) (!a !b c) (!a b !c) (!a b c)
        example2 = new ArrayList<>(List.of(
                new ArrayList<>(List.of("!a", "!b", "!c")),
                new ArrayList<>(List.of("!a", "!b", "c")),
                new ArrayList<>(List.of("!a", "b", "!c")),
                new ArrayList<>(List.of("!a", "b", "c"))
        ));
    }

    @Test
    void minimiseTest1() {
        assertEquals(
                "[[!a, c], [!b, c], [!a, b]]",
                minimizator.minimise(
                        example1,
                        FormulaType.PCNF
                ).toString());
    }

    @Test
    void minimiseTest2() {
        assertEquals(
                "[[!a, !b], [!a, !c], [!a, c], [!a, b]]",
                minimizator.minimise(
                        example2,
                        FormulaType.PDNF
                ).toString());
    }

    @Test
    void gluingTest1() {
    }

    @Test
    void gluingTest2() {
    }

    @Test
    void createValuesMapTest1() {
        assertEquals(
                "{!a=1, a=0, !b=1, b=0, !c=0, c=1}",
                minimizator.createValuesMap(
                        example1.get(0), FormulaType.PDNF).toString());
    }
}
