package cn.com.sherhom.reno.common.utils;

import cn.com.sherhom.reno.common.exception.RenoException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Sherhom
 * @date 2020/9/8 20:12
 */
public class CsvWriter {
    BufferedWriter writeText;
    File file;
    String path;
    Boolean opened=false;
    Boolean shouldCreate=false;
    CSVLine csvLine;
    public CsvWriter(String path,CSVLine csvLine){
        this.path=path;
        this.csvLine=csvLine;
    }
    public void open(){
        File tmpFile=new File(path);
        shouldCreate=!tmpFile.exists();
        file=FileUtil.openAndCreateFile(path);
        Asset.notNull(file,"The file is opened failed");
        try {
            writeText=new BufferedWriter(new FileWriter(file,true));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RenoException(e);
        }
        this.opened=true;
    }

    public void close(){
        if(opened){
           this.opened=false;
            try {
                writeText.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void writeHeader(){
        try {
            writeText.write(csvLine.getHeader());
            writeText.newLine();
            writeText.flush();
        } catch (IOException e) {
            LogUtil.printStackTrace(e);
            throw new RenoException(e);
        }
    }
    public void writeLine(Object o){
        try {
            String line = csvLine.getLine(o);
            if(org.apache.commons.lang3.StringUtils.isNotBlank(line)){
                writeText.write(line);
                writeText.newLine();
                writeText.flush();
            }
        } catch (IOException e) {
            LogUtil.printStackTrace(e);
            throw new RenoException(e);
        }
    }
    public void flush(){
        try {
            writeText.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean isOpened() {
        return opened;
    }
    public Boolean isShouldCreate() {
        return shouldCreate;
    }
}
