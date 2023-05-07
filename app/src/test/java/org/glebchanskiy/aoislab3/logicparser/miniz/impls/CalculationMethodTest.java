package org.glebchanskiy.aoislab3.logicparser.miniz.impls;

import org.glebchanskiy.aoislab3.logicparser.FormulasOperations;
import org.glebchanskiy.aoislab3.logicparser.LogicParser;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.CalculationMethod;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.utils.Gluer;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculationMethodTest {
    static CalculationMethod minimizator;
    static Gluer gluer;

    static List<List<String>> example0pcnf;
    static List<List<String>> example0Result;
    static List<List<String>> example1pdnf;
    static List<List<String>> example1Result;
    static List<List<String>> example2pcnf;
    static List<List<String>> example2Result;

    static List<List<String>> example3pcnf;
    static List<List<String>> example3Result;

    static List<List<String>> example4pdnf;
    static List<List<String>> example4Result;

    @BeforeAll
    static void setUp() {
        minimizator = new CalculationMethod();
        gluer = new Gluer();

        example0pcnf = FormulasOperations.getPcnfAsList(LogicParser.parse("(A|B|C)&(A|!B|C)&(!A|B|C)"));
        example0Result = List.of(List.of("B", "C"), List.of("A", "C"));

        example1pdnf = FormulasOperations.getPdnfAsList(LogicParser.parse("(!A&B&C)|(A&!B&C)|(A&B&C)"));
        example1Result = List.of(List.of("B", "C"), List.of("A", "C"));

        example2pcnf = FormulasOperations.getPcnfAsList(LogicParser.parse("(A|B|C)&(A|B|!C)&(A|!B|C)&(!A|B|C)&(!A|!B|C)"));
        example2Result = List.of(List.of("A", "B"), List.of("!A", "C"));


        example3pcnf = FormulasOperations.getPcnfAsList(LogicParser.parse("(A|B|C)&(!A|B|C)"));
        example3Result = List.of(List.of("A", "C"), List.of("B", "C"));

        example4pdnf = FormulasOperations.getPcnfAsList(LogicParser.parse("(!A&!B&C)|(!A&B&!C)|(!A&B&C)|(A&!B&C)|(A&B&!C)|(A&B&C)"));
        example4Result = List.of(List.of("B", "C"));

    }

    @Test
    void minimiseTest0() {
        assertTrue(
                example0Result.containsAll(
                        minimizator.minimise(example0pcnf, FormulaType.PCNF)
                ));
    }

    @Test
    void minimiseTest1() {
        assertTrue(
                example1Result.containsAll(
                        minimizator.minimise(example1pdnf, FormulaType.PDNF)
                ));
    }

    @Test
    void minimiseTest2() {
        System.out.println(example2Result);
        System.out.println("SUKA:" + minimizator.minimise(example2pcnf, FormulaType.PCNF));
        assertTrue(
                example2Result.containsAll(
                        minimizator.minimise(example2pcnf, FormulaType.PCNF)
                ));
    }

    @Test
    void minimiseTest3() {
        System.out.println(example3Result);
        System.out.println(minimizator.minimise(example0pcnf, FormulaType.PCNF));
        assertTrue(
                example3Result.containsAll(
                        minimizator.minimise(example0pcnf, FormulaType.PCNF)
                ));
    }

    @Test
    void minimiseTest4() {
        assertTrue(
                example4Result.containsAll(
                        minimizator.minimise(example4pdnf, FormulaType.PDNF)
                ));
    }

    @Test
    void gluingTest1() {
        assertEquals(
                "[[A, B], [A, C], [B, C], [!B, C], [!A, C]]",
                gluer.gluing(example2pcnf).toString());
    }

    @Test
    void gluingTest2() {
        assertEquals(
                "[[B, C], [A, C]]",
                gluer.gluing(example1pdnf).toString());
    }
}
