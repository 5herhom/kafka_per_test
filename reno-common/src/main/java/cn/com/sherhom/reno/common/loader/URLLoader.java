package cn.com.sherhom.reno.common.loader;

import java.io.InputStream;
/**
 * @author Sherhom
 * @date 2020/9/2 20:12
 */
public interface URLLoader {
    InputStream getInStream();
    String getPath();
    String getUrl();
    String getAbsolutePath();
    String getProtocol();
}
