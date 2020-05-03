package sample.Java.Util;

import java.io.File;

public class StringUtils {
    private static final String RESOURCE_PATH = System.getProperty("user.dir")+ File.separator +"src\\sample\\Resources\\";

    public static String getResourceString(String path){
        path=RESOURCE_PATH.concat(path);
        path=path.replaceAll("\\\\", "/");
        return path;
    }
}
