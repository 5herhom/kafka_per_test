package cn.com.sherhom.reno.common.loader;

import cn.com.sherhom.reno.common.utils.StringUtils;

public abstract class AbstractURLLoader implements URLLoader {
    protected String path;
    protected String protocol;
    public AbstractURLLoader(String path){
        this.path= StringUtils.cleanPath(path);
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public String getUrl() {
        return this.protocol+":"+this.path;
    }

}
