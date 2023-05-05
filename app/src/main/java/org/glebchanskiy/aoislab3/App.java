/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.glebchanskiy.aoislab3;

import org.glebchanskiy.aoislab3.logicparser.*;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.TabularMethod;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.CalculationTabularMethod;
import org.glebchanskiy.aoislab3.logicparser.minimalization.Minimizator;
import org.glebchanskiy.aoislab3.logicparser.minimalization.impls.CalculationMethod;
import org.glebchanskiy.aoislab3.logicparser.util.FormulaType;

import java.util.Scanner;

public class App {

    public static String input() {
        Scanner sc = new Scanner(System.in);

        System.out.println("usage:\nand - &\nor  - |\nexample: (A&B)|C\n");
        System.out.print("Enter logic formula:\n>>> ");

        return sc.nextLine();
    }


    public static void main(String[] args) {
        String expression;

        if (args.length > 0) expression = args[0];
        else expression = input();

        LogicFormula formula = LogicParser.parse(expression);

        printLab2Part(formula);
        printLab3Part(formula);
    }

    private static void printLab3Part(LogicFormula formula) {
        System.out.println("\n\n_____________________________________________________________________");
        System.out.println("_______________________ Minimization ________________________________");

        System.out.println("\nCalculation Method\n");
        printMinimalFormula(new CalculationMethod(), formula);

        System.out.println("_____________________________________________________________________");
        System.out.println("\nCalculation Tabular Method\n");
        printMinimalFormula(new CalculationTabularMethod(), formula);

        System.out.println("_____________________________________________________________________");
        System.out.println("\nTabular Method\n");
        printMinimalFormula(new TabularMethod(), formula);
    }

    private static void printMinimalFormula(Minimizator minimizator, LogicFormula formula) {
        var pdnf = FormulasOperations.getPdnfAsList(formula);
        var pcnf = FormulasOperations.getPcnfAsList(formula);

        var minPdnf = minimizator.minimise(pdnf, FormulaType.PDNF);
        System.out.println("PDNF: " + FormulasOperations.fromListToString(minPdnf, true));

        System.out.println();

        var minPcnf = minimizator.minimise(pcnf, FormulaType.PCNF);
        System.out.println("PCNF: " + FormulasOperations.fromListToString(minPcnf, false));

    }


    private static void printLab2Part(LogicFormula formula) {
        System.out.println("\n\n_____________________________________________________________________");
        System.out.println("_________________________ Lab 2 Part ________________________________");
        System.out.println();


        System.out.println("AST of logic formula:");
        System.out.println(FormulasOperations.getTree(formula));
        System.out.println(formula.getExpression());
        System.out.println(FormulasOperations.getTruthTable(formula));

        System.out.println(FormulasOperations.getIndex(formula));

        System.out.println();

        System.out.println("PDNF: " + FormulasOperations.getPdnf(formula));
        System.out.println(FormulasOperations.getPdnfBin(formula));
        System.out.println(FormulasOperations.getPdnfDig(formula));

        System.out.println();

        System.out.println("PCNF: " + FormulasOperations.getPcnf(formula));
        System.out.println(FormulasOperations.getPcnfBin(formula));
        System.out.println(FormulasOperations.getPcnfDig(formula));

        System.out.println();
    }
}