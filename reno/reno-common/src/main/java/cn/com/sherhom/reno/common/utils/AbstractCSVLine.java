package cn.com.sherhom.reno.common.utils;

/**
 * @author Sherhom
 * @date 2020/9/8 18:35
 */
public abstract class AbstractCSVLine implements CSVLine {
    protected final String SEPARATOR;
    public AbstractCSVLine(String separator){
        SEPARATOR=separator;
    }

    protected void separateLink(StringBuilder sb,Object content){
        sb.append(content).append(SEPARATOR);
    }
}
