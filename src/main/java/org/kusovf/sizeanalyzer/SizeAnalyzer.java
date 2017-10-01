package org.kusovf.sizeanalyzer;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.sun.istack.internal.Nullable;
import org.kusovf.sizeanalyzer.dtos.EntityAnalyzeResult;
import org.kusovf.sizeanalyzer.dtos.EntityMeta;
import org.kusovf.sizeanalyzer.dtos.EntityMetaRoot;
import org.kusovf.sizeanalyzer.utils.MetadataExtractor;
import org.kusovf.sizeanalyzer.utils.FileNamePredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class SizeAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SizeAnalyzer.class);

    private final EntityMetaRoot meta;
    private final Path folder;
    private final Iterable<Predicate<File>> nameFilters;
    private Map<String, EntityAnalyzeResult> resultMap;
    private Set<String> entityNames;
    private Map<String, String> indexPatternToEntity;

    SizeAnalyzer(Path folder, EntityMetaRoot meta) {
        this.folder = Objects.requireNonNull(folder);
        this.meta = Objects.requireNonNull(meta);
        this.entityNames = MetadataExtractor.extractNames(meta.getEntities());
        this.indexPatternToEntity = MetadataExtractor.getIndexPatternToEntityMap(meta.getIndexPattern(), meta.getEntities());
        this.nameFilters = Iterables.transform(meta.getExclusionPatterns(), new Function<String, Predicate<File>>() {
            @Override
            public Predicate<File> apply(String s) {
                return new FileNamePredicate(s);
            }
        });

    }

    public List<EntityAnalyzeResult> calculate() {
        File dir = folder.toFile();
        Preconditions.checkArgument(dir.exists(), "Folder %s not found", folder);
        Preconditions.checkArgument(dir.isDirectory(), "%s is not directory", folder);
        List<EntityAnalyzeResult> results = buildEntitiesHierarchy(meta);
        collectStats(dir, null);
        return results;
    }

    private void collectStats(File inFile, @Nullable EntityAnalyzeResult parent) {
        if (inFile.isDirectory()) {
            LOGGER.info("Visiting dir {}", inFile.getAbsolutePath());
            File[] fileArray = inFile.listFiles();
            Iterable<File> files = fileArray == null ? Collections.<File>emptyList() : filterFiles(Arrays.asList(fileArray));
            for (File file : files) {
                EntityAnalyzeResult nextParent = resolveNextParent(parent, file);
                collectStats(file, nextParent);
            }
        } else if (parent != null) {
            LOGGER.info("Found file {}", inFile.getAbsolutePath());
            long fileLength = inFile.length();
            parent.setSelfSize(fileLength + parent.getSelfSize());
        }
    }

    private EntityAnalyzeResult tryResolveIndex(File file) {
        String relativePath = folder.relativize(file.toPath()).toString();
        for (String indexPattern : indexPatternToEntity.keySet()) {
            if (relativePath.matches(".*" + indexPattern)) {
                return getOrCreateIndexEntry(relativePath, indexPattern);
            }
        }
        return null;
    }

    private EntityAnalyzeResult getOrCreateIndexEntry(String name, String indexPattern) {
        EntityAnalyzeResult parent = resultMap.get(indexPatternToEntity.get(indexPattern));
        if (parent == null) {
            throw new IllegalStateException("Parent for index " + indexPattern + " not found");
        }
        EntityAnalyzeResult result = new EntityAnalyzeResult(name);
        parent.addIndex(result);
        return result;
    }

    @Nullable
    private String getEntityName(Path path) {
        Path parent = path;
        do {
            parent = parent.getParent();
            String relativePath = parent.relativize(path).toString();
            if (entityNames.contains(relativePath)) {
                return relativePath;
            }
        } while (!parent.equals(folder));
        return null;
    }

    private EntityAnalyzeResult resolveNextParent(EntityAnalyzeResult curParent, File file) {
        if (!file.isDirectory()) {
            return curParent;
        }
        EntityAnalyzeResult nextParent;
        String entityName = getEntityName(file.toPath());
        if (entityName != null) {
            nextParent = resultMap.get(entityName);
        } else {
            nextParent = tryResolveIndex(file);
        }
        return nextParent == null ? curParent : nextParent;
    }

    private Iterable<File> filterFiles(Iterable<File> unfiltered) {
        return Iterables.filter(unfiltered, Predicates.and(nameFilters));
    }

    private List<EntityAnalyzeResult> buildEntitiesHierarchy(EntityMetaRoot in) {
        resultMap = new HashMap<>();
        List<EntityAnalyzeResult> result = new ArrayList<>();
        for (EntityMeta current : in.getEntities()) {
            result.add(buildEntityTree(current, null));
        }
        return result;
    }

    private EntityAnalyzeResult buildEntityTree(EntityMeta meta, @Nullable EntityAnalyzeResult parent) {
        EntityAnalyzeResult result = resultMap.get(meta.getName());
        if (result != null) {
            return result;
        }
        result = new EntityAnalyzeResult(meta.getName());
        if (parent != null) {
            parent.addChild(result);
        }
        resultMap.put(result.getName(), result);
        for (EntityMeta child : meta.getSubEntities()) {
            result.addChild(buildEntityTree(child, result));
        }
        return result;
    }
}
