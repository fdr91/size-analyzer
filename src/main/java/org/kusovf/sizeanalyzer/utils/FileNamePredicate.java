package org.kusovf.sizeanalyzer.utils;

import com.google.common.base.Predicate;

import java.io.File;
import java.util.regex.Pattern;

/**
 * @author Ivan
 * @date 01.10.2017.
 */
public class FileNamePredicate implements Predicate<File> {
    private final Pattern pattern;

    public FileNamePredicate(String pattern) {
        this.pattern = Pattern.compile(".*" + pattern);
    }

    @Override
    public boolean apply(File file) {
        return !pattern.matcher(file.getName()).matches();
    }
}
