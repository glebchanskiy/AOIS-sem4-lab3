package org.glebchanskiy.aoislab3.logicparser.miniz.impls;

import org.glebchanskiy.aoislab3.logicparser.FormulasOperations;
import org.glebchanskiy.aoislab3.logicparser.LogicParser;
import org.glebchanskiy.aoislab3.logicparser.minimalization.Minimizator;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.CalculationTabularMethod;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.TabularMethod;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.utils.Gluer;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculationTabularMethodTest {
    static Minimizator minimizator;

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
        minimizator = new TabularMethod();

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
    void tabularMinimiseTest0() {
        assertEquals(
                example0Result,
                FormulasOperations.fromListToString(minimizator.minimise(example0pcnf, FormulaType.PCNF), false)
        );
    }

    @Test
    void tabularMinimiseTest1() {
        assertEquals(
                example1Result,
                FormulasOperations.fromListToString(minimizator.minimise(example1pdnf, FormulaType.PDNF), true)
        );
    }

    @Test
    void tabularMinimiseTest2() {
        assertEquals(
                example2Result,
                FormulasOperations.fromListToString(minimizator.minimise(example2pcnf, FormulaType.PCNF), false)
        );
    }

    @Test
    void tabularMinimiseTest3() {
        assertEquals(
                example3Result,
                FormulasOperations.fromListToString(minimizator.minimise(example3pcnf, FormulaType.PCNF), false)
        );
    }

    @Test
    void tabularMinimiseTest4() {
        assertEquals(
                example4Result,
                FormulasOperations.fromListToString(minimizator.minimise(example4pdnf, FormulaType.PDNF), true)
        );
    }
}
