package com.example;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.ssl.SSLContexts;


public class HCTest {

    public static void main(final String[] args) {
        try {
            doGet("https://192.168.88.200");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Do HTTP GET with the given URL
     * @param url url to get with
     * @throws IOException
     * @throws ParseException
     */
    public static void doGet(String url) throws IOException, ParseException {
        SSLConnectionSocketFactory scsf = null;
        try {
            scsf = new SSLConnectionSocketFactory(SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(), NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        final HttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(scsf).build();
        try (final CloseableHttpClient httpclient = HttpClients.custom().setConnectionManager(cm).build()) {
            final HttpGet httpGet = new HttpGet(url);
            try (final CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                System.out.println(response1.getCode() + " " + response1.getReasonPhrase());
                final HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                String rspBody=EntityUtils.toString(entity1);
                System.out.println( rspBody);
            }
        }
    }

}
