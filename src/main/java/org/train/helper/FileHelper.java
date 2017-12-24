package org.train.helper;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class FileHelper {
    public static String canonicalPath(String path) {
        if (Paths.get(path).isAbsolute()) {
            return path;
        }

        try {
            return Paths.get(FileHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                    .getParent().resolve(path).toString() + "/";
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
