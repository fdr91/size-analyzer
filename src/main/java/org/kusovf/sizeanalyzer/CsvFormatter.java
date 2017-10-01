package org.kusovf.sizeanalyzer;

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

    public static String format(Collection<EntityAnalyzeResult> analyzeResults) {
        return new CsvFormatter().doFormat(analyzeResults).getResult();
    }

    public CsvFormatter doFormat(Collection<EntityAnalyzeResult> analyzeResults) {
        this.builder = new StringBuilder(HEADER);
        return formatImpl(analyzeResults, null);
    }

    private CsvFormatter formatImpl(Collection<EntityAnalyzeResult> in, EntityAnalyzeResult parent) {
        List<EntityAnalyzeResult> sorted = new ArrayList<>(in);
        Collections.sort(sorted, COMPARATOR);
        for (EntityAnalyzeResult e : sorted) {
            append(e, parent);
        }
        return this;
    }
    private void append(EntityAnalyzeResult e, EntityAnalyzeResult parent) {
        append(e.getName()).append(e.getFullSize()).append(e.getChildrenSize()).append(e.getIndexSize());
        builder.append(parent == null ? "" : parent.getName()).append('\n');
        formatImpl(e.getIndexes(), e);
        formatImpl(e.getChildren(), e);
    }

    private CsvFormatter append(Object value) {
        builder.append(value).append(',');
        return this;
    }

    public String getResult() {
        return builder.toString();
    }
}
