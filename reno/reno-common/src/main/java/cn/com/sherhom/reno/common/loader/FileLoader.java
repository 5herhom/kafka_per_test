package cn.com.sherhom.reno.common.loader;

import cn.com.sherhom.reno.common.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static cn.com.sherhom.reno.common.contants.ProtocolType.FILE_PROTOCOL;
/**
 * @author Sherhom
 * @date 2020/9/2 20:12
 */
@Slf4j
public class FileLoader extends AbstractURLLoader {
    File file;
    public FileLoader(String path) {
        super(path);
        this.file=new File(path);
        this.protocol=FILE_PROTOCOL;
    }

    @Override
    public InputStream getInStream() {
        if(!file.exists()){
            return null;
        }
        try{
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LogUtil.printStackTrace(e);
            return null;
        }
    }


    @Override
    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }
}
