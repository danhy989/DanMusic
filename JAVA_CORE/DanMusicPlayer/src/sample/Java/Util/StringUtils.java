package sample.Java.Util;

import java.io.File;

public class StringUtils {
    private static final String RESOURCE_PATH = System.getProperty("user.dir")+ File.separator +"src\\sample\\Resources\\";

    public static String getResourceString(String path){
        path=RESOURCE_PATH.concat(path);
        path=path.replaceAll("\\\\", "/");
        return path;
    }

    public static String getInnerProjectResourceString(String path){
        if(path.contains("/")){
            path = path.replaceAll("/","\\\\");
        }
        path = path.replace(RESOURCE_PATH,"");
        return path;
    }
}
