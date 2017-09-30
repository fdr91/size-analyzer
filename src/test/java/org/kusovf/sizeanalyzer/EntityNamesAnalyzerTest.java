package org.kusovf.sizeanalyzer;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.lingala.zip4j.core.ZipFile;

public class EntityNamesAnalyzerTest {

    @Test
    public void testEntityNameAnalyzis() {
        try {
            unpackTestData();
        } finally {
            removeTestData();
        }
    }

    private Path unpackTestData() {
        try {
            URL testZip = getClass().getClassLoader().getResource("testData.zip");
            ZipFile zipFile = new ZipFile(Paths.get(testZip.toURI()).toFile());
            zipFile.extractAll(System.getProperty("java.io.tmpdir"));
            return getTestDataPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Path getTestDataPath() {
        return Paths.get(System.getProperty("java.io.tmpdir"), "testData");
    }

    private void removeTestData() {
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
