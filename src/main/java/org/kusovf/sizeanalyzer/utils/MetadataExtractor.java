package org.kusovf.sizeanalyzer.utils;

import org.kusovf.sizeanalyzer.dtos.EntityMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MetadataExtractor {

    public static final String NAME_IN_INDEX = "\\$\\{entityName\\}";

    public static Set<String> extractNames(Iterable<EntityMeta> meta2) {
        Set<String> result = new HashSet<>();
        for (EntityMeta i : meta2) {
            result.addAll(extractNames(i));
        }
        return result;
    }

    public static Set<String> extractNames(EntityMeta meta2) {
        Set<String> result = new HashSet<>();
        result.add(meta2.getName());
        for (EntityMeta entityMeta2 : meta2.getSubEntities()) {
            result.addAll(extractNames(entityMeta2));
        }
        return result;
    }

    public static Map<String, String> getIndexPatternToEntityMap(String indexPattern, Iterable<EntityMeta> entityMeta2s) {
        Map<String, String> result = new HashMap<>();
        for (String name : extractNames(entityMeta2s)) {
            result.put(indexPattern.replaceAll(NAME_IN_INDEX, name), name);
        }
        return result;
    }
}
