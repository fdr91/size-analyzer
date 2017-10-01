package org.kusovf.sizeanalyzer.dtos;
import java.util.Collections;
import java.util.List;

public class EntityMeta {

    private String name;
    private List<EntityMeta> subEntities = Collections.emptyList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EntityMeta> getSubEntities() {
        return subEntities;
    }

    public void setSubEntities(List<EntityMeta> subEntities) {
        this.subEntities = subEntities;
    }
}
