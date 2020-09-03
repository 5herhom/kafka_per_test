package cn.com.sherhom.reno.common.loader;

import java.io.InputStream;

public interface URLLoader {
    InputStream getInStream();
    String getPath();
    String getUrl();
    String getAbsolutePath();
    String getProtocol();
}
