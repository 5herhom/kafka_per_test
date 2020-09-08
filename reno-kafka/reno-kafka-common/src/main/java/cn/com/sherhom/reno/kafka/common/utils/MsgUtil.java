package cn.com.sherhom.reno.kafka.common.utils;

/**
 * @author Sherhom
 * @date 2020/9/7 17:10
 */
public class MsgUtil {
    public static final byte A_BYTE='a';
    public static final byte A2Z_OFFSET='z'-'a'+1;

    public static byte[] getBytes(int size) {
        byte[] bytes=new byte[size];
        for(int i=0;i<size;i++){
            bytes[i]= (byte) (A_BYTE+ i&A2Z_OFFSET);
        }
        return bytes;
    }
}
