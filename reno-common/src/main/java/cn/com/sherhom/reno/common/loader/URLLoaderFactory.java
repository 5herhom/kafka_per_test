package cn.com.sherhom.reno.common.loader;

import cn.com.sherhom.reno.common.exception.RenoException;


import java.net.URL;

import static cn.com.sherhom.reno.common.contants.ProtocolType.CLASSPATH_PROTOCOL;
import static cn.com.sherhom.reno.common.contants.ProtocolType.FILE_PROTOCOL;

/**
 * @author Sherhom
 * @date 2020/9/2 20:12
 */
public class URLLoaderFactory {
    public static URLLoader getInstance(URL url) {
        return getInstance(url.getProtocol(), url.getPath());
    }

    public static URLLoader getInstance(String url) {
        int index = url.indexOf(':');
        if (index == -1)
            throw new RenoException(String.format("Url format is error:[%s]", url));

        String protocol = url.substring(0, index);
        String path = url.substring(index + 1);
        return getInstance(protocol, path);
    }

    public static URLLoader getInstance(String protocol, String path) {
        switch (protocol.toLowerCase()) {
            case CLASSPATH_PROTOCOL:
                return new ClasspathLoader(path);
            case FILE_PROTOCOL:
                return new FileLoader(path);
            default:
                return new ClasspathLoader(path);
        }
    }
}
