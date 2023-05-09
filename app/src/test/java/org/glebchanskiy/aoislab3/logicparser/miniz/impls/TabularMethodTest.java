package org.glebchanskiy.aoislab3.logicparser.miniz.impls;

import org.glebchanskiy.aoislab3.logicparser.FormulasOperations;
import org.glebchanskiy.aoislab3.logicparser.LogicParser;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.CalculationMethod;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TabularMethodTest {
    static CalculationMethod minimizator;

    static List<List<String>> example0pcnf;
    static String example0Result;
    static List<List<String>> example1pdnf;
    static String example1Result;
    static List<List<String>> example2pcnf;
    static String example2Result;

    static List<List<String>> example3pcnf;
    static String example3Result;

    static List<List<String>> example4pdnf;
    static String example4Result;

    @BeforeAll
    static void setUp() {
        minimizator = new CalculationMethod();

        example0pcnf = FormulasOperations.getPcnfAsList(LogicParser.parse("(A&B)|C"));
        example0Result = "(A|C)&(B|C)";

        example1pdnf = FormulasOperations.getPdnfAsList(LogicParser.parse("C&(A|B)"));
        example1Result = "(A&B)|(A&C)";

        example2pcnf = FormulasOperations.getPcnfAsList(LogicParser.parse("(A&B)|(!C&!A)"));
        example2Result = "(!A|B)&(A|!C)";


        example3pcnf = FormulasOperations.getPcnfAsList(LogicParser.parse("(A&B)|(C&A)"));
        example3Result = "(A)&(B|C)";

        example4pdnf = FormulasOperations.getPcnfAsList(LogicParser.parse("(A|B)|C"));
        example4Result = "(A&B&C)";

    }

    @Test
    void calculationMinimiseTest0() {
        assertEquals(
                example0Result,
                FormulasOperations.fromListToString(minimizator.minimise(example0pcnf, FormulaType.PCNF), FormulaType.PCNF)
        );
    }

    @Test
    void calculationMinimiseTest1() {
        assertEquals(
                example1Result,
                FormulasOperations.fromListToString(minimizator.minimise(example1pdnf, FormulaType.PDNF), FormulaType.PDNF)
        );
    }

    @Test
    void calculationMinimiseTest2() {
        assertEquals(
                example2Result,
                FormulasOperations.fromListToString(minimizator.minimise(example2pcnf, FormulaType.PCNF), FormulaType.PCNF)
        );
    }

    @Test
    void calculationMinimiseTest3() {
        assertEquals(
                example3Result,
                FormulasOperations.fromListToString(minimizator.minimise(example3pcnf, FormulaType.PCNF), FormulaType.PCNF)
        );
    }

    @Test
    void calculationMinimiseTest4() {
        assertEquals(
                example4Result,
                FormulasOperations.fromListToString(minimizator.minimise(example4pdnf, FormulaType.PDNF), FormulaType.PDNF)
        );
    }
}
