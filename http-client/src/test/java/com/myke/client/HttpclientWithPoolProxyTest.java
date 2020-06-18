package com.myke.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.net.Proxy;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 使用连接池测试
 *
 * @author 五月的仓颉https://www.cnblogs.com/xrq730/p/10963689.html
 */
public class HttpclientWithPoolProxyTest extends BaseHttpClientTest {

    private CloseableHttpClient httpClient = null;

    @Before
    public void before() {
        initHttpClient();
    }

    @Test
    public void test() throws Exception {
        startUpAllThreads(getRunThreads(new HttpThread()));
        // 等待线程运行
        for (; ; ) ;
    }

    private class HttpThread implements Runnable {

        @Override
        public void run() {
            HttpGet httpGet = new HttpGet("https://www.baidu.com/");
            // 长连接标识，不加也没事，HTTP1.1默认都是Connection: keep-alive的
            //http的keep-alive是为了复用已有连接
            httpGet.addHeader("Connection", "keep-alive");

            //设置代理IP、端口、协议
            HttpHost host = new HttpHost("127.0.0.1", 80, Proxy.Type.HTTP.name());

            RequestConfig requestConfig =
                    RequestConfig.custom()
                            .setExpectContinueEnabled(false)
                            //设置代理
                            .setProxy(host)
                            // setConnectTimeout表示设置建立连接的超时时间
                            .setConnectTimeout(1000)
                            // setConnectionRequestTimeout表示从连接池中拿连接的等待超时时间
                            .setConnectionRequestTimeout(2000)
                            // setSocketTimeout表示发出请求后等待对端应答的超时时间
                            .setSocketTimeout(3000)
                            .build();

            httpGet.setConfig(requestConfig);

            //设置请求头消息
            httpGet.setHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");

            long startTime = System.currentTimeMillis();
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response != null) {
                    //获取返回实体
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        System.out.println("网页内容为:" + EntityUtils.toString(entity, "utf-8"));
                    }
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                addCost(System.currentTimeMillis() - startTime);
                if (NOW_COUNT.incrementAndGet() == REQUEST_COUNT) {
                    System.out.println(EVERY_REQ_COST.toString());
                }
            }
        }

    }

    private void initHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // 总连接池数量
        connectionManager.setMaxTotal(1);
        // 可为每个域名设置单独的连接池数量

        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("www.baidu.com")), 1);
        // setConnectTimeout表示设置建立连接的超时时间
        // setConnectionRequestTimeout表示从连接池中拿连接的等待超时时间
        // setSocketTimeout表示发出请求后等待对端应答的超时时间
        RequestConfig requestConfig =
                RequestConfig.custom()
                        .setConnectTimeout(1000)
                        .setConnectionRequestTimeout(2000)
                        .setSocketTimeout(3000)
                        .build();
        // 重试处理器，StandardHttpRequestRetryHandler这个是官方提供的，
        // 看了下感觉比较挫，很多错误不能重试，可自己实现 HttpRequestRetryHandler接口去做
        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();

        httpClient = HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler)
                .build();

        // 服务端假设关闭了连接，对客户端是不透明的，HttpClient为了缓解这一问题，
        // 在某个连接使用前会检测这个连接是否过时，如果过时则连接失效，但是这种做法会为每个请求
        // 增加一定额外开销，因此有一个定时任务专门回收长时间不活动而被判定为失效的连接，
        // 可以某种程度上解决这个问题
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // 关闭失效连接并从连接池中移除
                    connectionManager.closeExpiredConnections();
                    // 关闭20秒钟内不活动的连接并从连接池中移除，空闲时间从交还给连接管理器时开始
                    connectionManager.closeIdleConnections(20, TimeUnit.SECONDS);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }, 0, 1000 * 5);
    }

}