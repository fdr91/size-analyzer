package org.kusovf.sizeanalyzer.dtos;

import java.util.*;

public class EntityAnalyzeResult {
    private String name;
    private long selfSize;
    private Set<EntityAnalyzeResult> children = new HashSet<>();
    private Set<EntityAnalyzeResult> indexes = new HashSet<>();

    public EntityAnalyzeResult(String name) {
        this.name = Objects.requireNonNull(name,"Name shouldn't be null");
    }

    public String getName() {
        return name;
    }

    public void setSelfSize(long selfSize) {
        this.selfSize = selfSize;
    }

    public long getSelfSize() {
        return selfSize;
    }

    public long getFullSize() {
        return getSelfSize() + getChildrenSize() + getIndexSize();
    }

    public long getChildrenSize() {
        return getFullSize(getChildren());
    }

    private long getFullSize(Iterable<EntityAnalyzeResult> iterable) {
        long result = 0;
        for (EntityAnalyzeResult e : iterable) {
            result += e.getFullSize();
        }
        return result;
    }

    public long getIndexSize() {
        return getFullSize(getIndexes());
    }

    public Collection<EntityAnalyzeResult> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Collection<EntityAnalyzeResult> getIndexes() {
        return Collections.unmodifiableSet(indexes);
    }

    public void addChild(EntityAnalyzeResult entityAnalyzeResult) {
        this.children.add(entityAnalyzeResult);
    }

    public void addIndex(EntityAnalyzeResult entityAnalyzeResult) {
        this.indexes.add(entityAnalyzeResult);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityAnalyzeResult that = (EntityAnalyzeResult) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public static Map<String, EntityAnalyzeResult> toMap(Iterable<EntityAnalyzeResult> in) {
        Map<String, EntityAnalyzeResult> resultMap = new HashMap<>();
        for (EntityAnalyzeResult e : in) {
            resultMap.put(e.getName(), e);
        }
        return resultMap;
    }

    @Override
    public String toString() {
        return "EntityAnalyzeResult{" +
                "name='" + name + '\'' +
                ", selfSize=" + selfSize +
                '}';
    }
}
