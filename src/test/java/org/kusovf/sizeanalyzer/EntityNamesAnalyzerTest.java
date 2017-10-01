package org.kusovf.sizeanalyzer;

import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import net.lingala.zip4j.core.ZipFile;
import org.kusovf.sizeanalyzer.dtos.EntityAnalyzeResult;
import org.kusovf.sizeanalyzer.dtos.EntityMetaRoot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EntityNamesAnalyzerTest {

    @Test
    public void testEntityNameAnalyzis() {
    }

    @Test
    public void testDataGathering() {
        SizeAnalyzer sizeAnalyzer = new SizeAnalyzer(getTestDataPath(), getTestMeta());
        Set<EntityAnalyzeResult> result = new HashSet<>(sizeAnalyzer.calculate());
        assertEquals(2, result.size());
        Map<String, EntityAnalyzeResult> map = EntityAnalyzeResult.toMap(result);
        EntityAnalyzeResult entity=  map.get("com.org.AnotherEntity");
        checkEntity(entity, 222, 0, 106, 0, 2);
        Map<String, EntityAnalyzeResult> childrenMap = EntityAnalyzeResult.toMap(entity.getIndexes());
        EntityAnalyzeResult child = childrenMap.get("Index_com.org.AnotherEntity#code");
        checkEntity(child, 51, 0, 0, 0, 0);
        child = childrenMap.get("Index_com.org.AnotherEntity#name");
        checkEntity(child, 55, 0, 0, 0, 0);
        EntityAnalyzeResult entity2 = map.get("com.org.Entity");
        checkEntity(entity2, 345, 221, 31, 1, 1);
        childrenMap = EntityAnalyzeResult.toMap(entity2.getIndexes());
        child = childrenMap.get("Index_com.org.Entity#start");
        checkEntity(child, 31, 0, 0, 0, 0);
        childrenMap = EntityAnalyzeResult.toMap(entity2.getChildren());
        entity = childrenMap.get("com.org.LinkedEntity");
        checkEntity(entity, 221, 0, 125, 0, 1);
        childrenMap = EntityAnalyzeResult.toMap(entity.getIndexes());
        child = childrenMap.get("Index_com.org.LinkedEntity#code");
        checkEntity(child, 125, 0, 0, 0, 0);
    }

    @Test
    public void testFormatting() throws URISyntaxException, IOException {
        SizeAnalyzer sizeAnalyzer = new SizeAnalyzer(getTestDataPath(), getTestMeta());
        String result = CsvFormatter.format(sizeAnalyzer.calculate());
        File goalFile = Paths.get(getClass().getClassLoader().getResource("analyze-result.csv").toURI()).toFile();
        String goal = FileUtils.readFileToString(goalFile, "UTF-8");
        assertEquals(goal, result);
    }

    private void checkEntity(EntityAnalyzeResult entity, int fullSize, int childrenSize, int indeSizes, int childrenCount, int indexCount) {
        assertEquals(fullSize, entity.getFullSize());
        assertEquals(childrenSize, entity.getChildrenSize());
        assertEquals(indeSizes, entity.getIndexSize());
        assertEquals(childrenCount, Iterables.size(entity.getChildren()));
        assertEquals(indexCount, Iterables.size(entity.getIndexes()));
    }

    @Before
    public void unpackTestData() {
        try {
            URL testZip = getClass().getClassLoader().getResource("testData.zip");
            ZipFile zipFile = new ZipFile(Paths.get(testZip.toURI()).toFile());
            zipFile.extractAll(System.getProperty("java.io.tmpdir"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Path getTestDataPath() {
        return Paths.get(System.getProperty("java.io.tmpdir"), "testData");
    }

    private EntityMetaRoot getTestMeta() {
        try {
            URL uri = Objects.requireNonNull(this.getClass().getClassLoader().getResource("testMeta.json"));
            return new Gson().fromJson(FileUtils.readFileToString(Paths.get(uri.toURI()).toFile(), "UTF-8"), EntityMetaRoot.class);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @After
    public void removeTestData() {
        try {
            File file = getTestDataPath().toFile();
            if (file.exists()) {
                FileUtils.deleteDirectory(getTestDataPath().toFile());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
