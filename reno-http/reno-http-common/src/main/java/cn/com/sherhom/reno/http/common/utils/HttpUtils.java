package cn.com.sherhom.reno.http.common.utils;

import cn.com.sherhom.reno.common.utils.LogUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.util.Map;

/**
 * @author Sherhom
 * @date 2020/10/28 10:46
 */
public class HttpUtils {
    public static String get(String url){
        return get(url,null);
    }
    public static String get(String url,Map<String,String> param){
        return parseAndCloseResponse(getWithReturnResponse(url,param));
    }
    public static CloseableHttpResponse getWithReturnResponse(String url){
        return getWithReturnResponse(url,null);
    }
    public static CloseableHttpResponse getWithReturnResponse(String url,Map<String,String> param){
        CloseableHttpClient httpClient= null;
        CloseableHttpResponse response=null;
        try{
            URIBuilder uriBuilder=new URIBuilder(url);
            if(param!=null)
                param.forEach((k,v)->uriBuilder.setParameter(k,v));

            httpClient=HttpClients.createDefault();
            HttpGet httpPost=new HttpGet(uriBuilder.build());
            response=httpClient.execute(httpPost);
            return response;
        } catch (Throwable e) {
            LogUtil.printStackTrace(e);
            return null;
        }
        finally {
            IOUtils.closeQuietly(httpClient);
        }
    }
    public static String postJson(String url, Object param,Map<String,String>header){
        return postJson(url, JSONObject.toJSONString(param),header);
    }
    public static String postJson(String url, Object param){
        return postJson(url, JSONObject.toJSONString(param),null);
    }
    public static String parseAndCloseResponse(CloseableHttpResponse response){
        try{
            if(response!=null){
                HttpEntity entity=response.getEntity();
                return EntityUtils.toString(entity);
            }
            return null;
        }
        catch (Throwable e){
            LogUtil.printStackTrace(e);
            return null;
        }
        finally {
            IOUtils.closeQuietly(response);
        }
    }
    public static String postJson(String url, String jsonStr){
        return postJson(url,jsonStr,null);
    }
    public static String postJson(String url,String jsonString,Map<String,String> headers){
        return parseAndCloseResponse(postJsonReturnReponse(url,jsonString,headers));
    }
    public static CloseableHttpResponse postJsonReturnReponse(String url, Object param,Map<String,String>header){
        return postJsonReturnReponse(url, JSONObject.toJSONString(param),header);
    }
    public static CloseableHttpResponse postJsonReturnReponse(String url, Object param){
        return postJsonReturnReponse(url, JSONObject.toJSONString(param),null);
    }
    public static CloseableHttpResponse postJsonReturnReponse(String url, String jsonStr){
        return postJsonReturnReponse(url,jsonStr,null);
    }
    public static CloseableHttpResponse postJsonReturnReponse(String url, String jsonStr, Map<String,String> headers){
        CloseableHttpClient httpClient= null;
        CloseableHttpResponse response=null;
        try{
            httpClient=HttpClients.createDefault();
            HttpPost httpPost=new HttpPost(url);
            httpPost.setEntity(new StringEntity(jsonStr,"utf-8"));
            httpPost.setHeader("Content-Type","application/json");
            if(headers!=null)
                headers.forEach((k,v)->httpPost.setHeader(k,v));
            response=httpClient.execute(httpPost);
            return response;
        } catch (Throwable e) {
            LogUtil.printStackTrace(e);
            return null;
        }
        finally {
            IOUtils.closeQuietly(httpClient);
        }
    }
}
