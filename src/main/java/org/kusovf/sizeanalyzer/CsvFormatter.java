package org.kusovf.sizeanalyzer;

import com.sun.istack.internal.Nullable;
import org.kusovf.sizeanalyzer.dtos.EntityAnalyzeResult;

import java.util.*;

public class CsvFormatter {
    private static final String HEADER = "EntityName,Full size(bytes),Linked Entityes size(bytes),Indexes size (bytes),Child of,Directory path\n";
    private static final Comparator<? super EntityAnalyzeResult> COMPARATOR = new Comparator<EntityAnalyzeResult>() {

        @Override
        public int compare(EntityAnalyzeResult o1, EntityAnalyzeResult o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
    private StringBuilder builder;

    private CsvFormatter() {
    }

    public static String toCsv(Collection<EntityAnalyzeResult> analyzeResults) {
        CsvFormatter csvFormatter = new CsvFormatter();
        csvFormatter.builder = new StringBuilder(HEADER);
        csvFormatter.appendImpl(analyzeResults, null);
        return csvFormatter.builder.toString();
    }

    private void appendImpl(Collection<EntityAnalyzeResult> in, @Nullable EntityAnalyzeResult parent) {
        List<EntityAnalyzeResult> sorted = new ArrayList<>(in);
        Collections.sort(sorted, COMPARATOR);
        for (EntityAnalyzeResult e : sorted) {
            append(e, parent);
        }
    }
    private void append(EntityAnalyzeResult e, @Nullable EntityAnalyzeResult parent) {
        append(e.getName()).append(e.getFullSize()).append(e.getChildrenSize()).append(e.getIndexSize());
        builder.append(parent == null ? "" : parent.getName()).append('\n');
        appendImpl(e.getIndexes(), e);
        appendImpl(e.getChildren(), e);
    }

    private CsvFormatter append(Object value) {
        builder.append(value).append(',');
        return this;
    }
}
