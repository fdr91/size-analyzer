package org.kusovf.sizeanalyzer.dto;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MetaInfo {
    private String path;
    private List<EntityMeta> entities = Collections.emptyList();
    private List<String> exclusionPatterns = Collections.emptyList();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<EntityMeta> getEntities() {
        return new ArrayList<>(entities);
    }

    public void setEntities(List<EntityMeta> entities) {
        this.entities = ImmutableList.copyOf(entities);
    }

    public List<String> getExclusionPatterns() {
        return exclusionPatterns;
    }

    public void setExclusionPatterns(List<String> exclusionPatterns) {
        this.exclusionPatterns = ImmutableList.copyOf(exclusionPatterns);;
    }
}
