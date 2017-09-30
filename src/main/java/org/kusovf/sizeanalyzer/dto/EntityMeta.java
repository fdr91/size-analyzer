package org.kusovf.sizeanalyzer.dto;

import java.util.Collections;
import java.util.List;

/**
 * @author Ivan
 * @date 01.10.2017.
 */
public class EntityMeta {
    private String name;
    private String regexp;
    private List<EntityMeta> subEntities = Collections.emptyList();
    private List<EntityMeta> indexes = Collections.emptyList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public List<EntityMeta> getSubEntityes() {
        return subEntities;
    }

    public void setSubEntityes(List<EntityMeta> subEntityes) {
        this.subEntities = subEntityes;
    }

    public List<EntityMeta> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<EntityMeta> indexes) {
        this.indexes = indexes;
    }
}
