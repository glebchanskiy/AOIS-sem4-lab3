package org.glebchanskiy.aoislab3.logicparser.miniz.impls;

import org.glebchanskiy.aoislab3.logicparser.FormulasOperations;
import org.glebchanskiy.aoislab3.logicparser.LogicParser;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.CalculationMethod;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculationMethodTest {
    static CalculationMethod minimizator;

    static List<List<String>> example0pcnf;
    static String example0Result;
    static List<List<String>> example1pdnf;
    static String example1Result;
    static List<List<String>> example2pcnf;
    static String example2Result;

    @BeforeAll
    static void setUp() {
        minimizator = new CalculationMethod();

        example0pcnf = FormulasOperations.getPcnfAsList(LogicParser.parse("(A|B|C)&(A|!B|C)&(!A|B|C)"));
        example0Result = "[[B, C], [A, C]]";

        example1pdnf = FormulasOperations.getPdnfAsList(LogicParser.parse("(!A&B&C)|(A&!B&C)|(A&B&C)"));
        example1Result = "[[B, C], [A, C]]";

        example2pcnf = FormulasOperations.getPcnfAsList(LogicParser.parse("(A|B|C)&(A|B|!C)&(A|!B|C)&(!A|B|C)&(!A|!B|C)"));
        example2Result = "[[A, B], [C]]";

    }

    @Test
    void minimiseTest0() {
        assertEquals(
                example0Result,
                minimizator.minimise(example0pcnf, FormulaType.PCNF).toString()
        );
    }

    @Test
    void minimiseTest1() {
        assertEquals(
                example1Result,
                minimizator.minimise(
                        example1pdnf,
                        FormulaType.PDNF
                ).toString());
    }

    @Test
    void minimiseTest2() {
        assertEquals(
                example2Result,
                minimizator.minimise(
                        example2pcnf,
                        FormulaType.PCNF
                ).toString());
    }

    @Test
    void gluingTest1() {
        assertEquals(
                "[[A, B], [B, C], [A, C], [!B, C], [!A, C]]",
                minimizator.gluing(
                        example2pcnf
                ).toString());
    }

    @Test
    void gluingTest2() {
        assertEquals(
                "[[B, C], [A, C]]",
                minimizator.gluing(
                        example1pdnf
                ).toString());
    }
}
