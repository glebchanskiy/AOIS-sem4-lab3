package org.glebchanskiy.aoislab3.logicparser.util;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class TruthTable implements Iterable<TableRow> {
    private final List<TableRow> rows;

    public TruthTable(List<TableRow> rows) {
        this.rows = rows;
    }

    public List<Boolean[]> getTruths() {
        return rows.stream()
                .filter(TableRow::isFunctionTakeTrueValue)
                .map(TableRow::getVariablesValues)
                .toList();
    }

    public List<Boolean[]> getFalses() {
        return rows.stream()
                .filter(Predicate.not(TableRow::isFunctionTakeTrueValue))
                .map(TableRow::getVariablesValues)
                .toList();
    }

    public List<Boolean[]> getAllRows() {
        return rows.stream()
                .map(TableRow::getVariablesValues)
                .toList();
    }

    public Boolean[] getIndex() {
        return rows.stream()
                .map(TableRow::isFunctionTakeTrueValue)
                .toArray(Boolean[]::new);
    }


    @Override
    public Iterator<TableRow> iterator() {
        return new TruthTableIterator();
    }

    private class TruthTableIterator implements Iterator<TableRow> {
        private int cursor;

        public TruthTableIterator() {
            cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return cursor < rows.size();
        }

        @Override
        public TableRow next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            TableRow tableRow = new TableRow(
                    rows.get(cursor).getVariablesValues().clone(),
                    rows.get(cursor).isFunctionTakeTrueValue()
            );
            cursor++;
            return tableRow;
        }
    }
}

