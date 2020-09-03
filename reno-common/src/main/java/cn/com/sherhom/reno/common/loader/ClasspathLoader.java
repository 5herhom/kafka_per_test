package cn.com.sherhom.reno.common.loader;

import cn.com.sherhom.reno.common.utils.ConfUtil;

import java.io.InputStream;

import static cn.com.sherhom.reno.common.contants.ProtocolType.CLASSPATH_PROTOCOL;

public class ClasspathLoader extends AbstractURLLoader implements URLLoader {
    public ClasspathLoader(String path) {
        super(path);
        this.protocol=CLASSPATH_PROTOCOL;
    }

    @Override
    public InputStream getInStream() {
        return ClasspathLoader.class.getResourceAsStream(this.path);
    }

    @Override
    public String getAbsolutePath() {
        return ClasspathLoader.class.getResource(this.path).getPath();
    }
}
