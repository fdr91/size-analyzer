package org.kusovf.sizeanalyzer;

import org.junit.Test;
import org.kusovf.sizeanalyzer.dto.EntityMeta;
import org.kusovf.sizeanalyzer.dto.MetaInfo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MetaTest {

    @Test
    public void testJsonParser() {
        String in = "{" +
                "  path: 'C:\\test'," +
                "  entities: [" +
                "       {" +
                "           name: com.org.Entity," +
                "           subEntities: []," +
                "           indexes: [{name: index1, regexp: Index_index1}]" +
                "        }" +
                "  ]," +
                "  exclusionPatterns: ['excludeMe']" +
                "}";
        MetaInfo metaInfo = new MetaParser().parse(in);
        assertNotNull(metaInfo);
        assertNotNull(metaInfo.getPath());
        assertFalse(metaInfo.getEntities().isEmpty());
        assertFalse(metaInfo.getExclusionPatterns().isEmpty());
        assertFalse(metaInfo.getExclusionPatterns().isEmpty());
        EntityMeta meta = metaInfo.getEntities().get(0);
        assertNotNull(meta.getName());
        assertTrue(meta.getSubEntityes().isEmpty());
        assertFalse(meta.getIndexes().isEmpty());
        EntityMeta index = meta.getIndexes().get(0);
        assertNotNull(index.getName());
        assertNotNull(index.getRegexp());
    }
}
