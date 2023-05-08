package org.glebchanskiy.aoislab3.logicparser.miniz.impls;

import org.glebchanskiy.aoislab3.logicparser.FormulasOperations;
import org.glebchanskiy.aoislab3.logicparser.LogicParser;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.CalculationMethod;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.utils.Gluer;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GluerTest {
    static CalculationMethod minimizator;
    static Gluer gluer;

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
        gluer = new Gluer();

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
    void gluingTest0() {
        assertEquals(
                "[[A, C], [B, C]]",
                gluer.gluing(example0pcnf).toString());
    }

    @Test
    void gluingTest1() {
        assertEquals(
                "[[A, C], [A, B]]",
                gluer.gluing(example1pdnf).toString());
    }

    @Test
    void gluingTest2() {
        assertEquals(
                "[[A, !C], [B, !C], [!A, B]]",
                gluer.gluing(example2pcnf).toString());
    }
    @Test
    void gluingTest3() {
        assertEquals(
                "[[A, B], [A, C], [B, C], [A, !C], [A, !B]]",
                gluer.gluing(example3pcnf).toString());
    }
    @Test
    void gluingTest4() {
        assertEquals(
                "[[A, B, C]]",
                gluer.gluing(example4pdnf).toString());
    }
}
