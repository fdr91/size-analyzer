package org.kusovf.sizeanalyzer;

import com.google.gson.Gson;
import org.kusovf.sizeanalyzer.dto.MetaInfo;

public class MetaParser {
    public MetaInfo parse(String json) {
        return new Gson().fromJson(json, MetaInfo.class);
    }
}
