package cn.com.sherhom.reno.http.common.test;

import cn.com.sherhom.reno.common.utils.LogUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

/**
 * @author Sherhom
 * @date 2020/10/29 11:28
 */
public class HttpTeest {
    @Test
    public void test(){
        String url="http://localhost:18080/sunLayer/Test/test1";
        CloseableHttpClient httpClient= null;
        CloseableHttpResponse response=null;
        try{
            URIBuilder uriBuilder=new URIBuilder(url);
            httpClient= HttpClients.createDefault();
            HttpGet httpPost=new HttpGet(uriBuilder.build());
            response=httpClient.execute(httpPost);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Throwable e) {
            LogUtil.printStackTrace(e);
        }
        finally {
//            IOUtils.closeQuietly(response);
//            IOUtils.closeQuietly(httpClient);
        }

        try{
            URIBuilder uriBuilder=new URIBuilder(url);
            httpClient= HttpClients.createDefault();
            HttpGet httpPost=new HttpGet(uriBuilder.build());
            response=httpClient.execute(httpPost);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Throwable e) {
            LogUtil.printStackTrace(e);
        }
        finally {
//            IOUtils.closeQuietly(httpClient);
        }
    }
}
