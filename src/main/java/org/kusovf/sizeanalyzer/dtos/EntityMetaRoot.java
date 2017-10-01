package org.kusovf.sizeanalyzer.dtos;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class EntityMetaRoot {
    private String indexPattern;
    private List<EntityMeta> entities = Collections.emptyList();
    private List<String> exclusionPatterns = Collections.emptyList();

    public List<EntityMeta> getEntities() {
        return entities;
    }

    public void setEntities(List<EntityMeta> entities) {
        this.entities = entities;
    }

    public List<String> getExclusionPatterns() {
        return exclusionPatterns;
    }

    public void setExclusionPatterns(List<String> exclusionPatterns) {
        this.exclusionPatterns = exclusionPatterns;
    }

    public String getIndexPattern() {
        return indexPattern;
    }

    public EntityMetaRoot setIndexPattern(String indexPattern) {
        this.indexPattern = indexPattern;
        return this;
    }
}
