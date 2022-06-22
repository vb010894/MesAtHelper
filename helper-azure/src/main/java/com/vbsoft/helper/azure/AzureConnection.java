package com.vbsoft.helper.azure;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Component
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@PropertySource("classpath:azure.properties")
public final class AzureConnection {

    /**
     * Azure server url.
     */
    @NonNull
    @Value("${azure.server.url}")
    String url;

    /**
     * Azure collection.
     */
    @NonNull
    @Value("${azure.collection}")
    String collection;

    /**
     * Azure project
     */
    @NonNull
    @Value("${azure.project}")
    String project;

    /**
     * Azure username.
     */
    @NonNull
    @Value("${azure.username}")
    String username;

    /**
     * Azure token.
     */
    @NonNull
    @Value("${azure.token}")
    String token;

    /**
     * Http client.
     */
    OkHttpClient client;

    /**
     * Get request with authorization header.
     * @return Request builder
     */
    public Request.Builder getRequest() {
        return new Request
                .Builder()
                .header("Authorization", Credentials.basic(this.getUsername(), this.getToken()));
    }

    /**
     * Get request response.
     * @param request Request
     * @return Server Response
     */
    public Response getResponse (Request request) {
        try {
            Call call = this.getClient().newCall(request);
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get configured client.
     * @return Http client
     */
    private OkHttpClient getClient() {
        if(this.client == null) {
            OkHttpClient client = new OkHttpClient();
            OkHttpClient.Builder clientBuilder = client.newBuilder();
            clientBuilder.sslSocketFactory(socketFactory, (X509TrustManager) trustCerts[0]);
            clientBuilder.hostnameVerifier(((hostname, session) -> {
                System.out.println(hostname);
                return true;
            }));
            client = clientBuilder.build();
            this.client = client;
        }
        return this.client;
    }

    /**
     * Trusted certificate wallet.
     */
    private static final TrustManager[] trustCerts = new TrustManager[] {
            new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[] {};
                }
            }

    };

    /**
     * Certs ssl context.
     */
    private static SSLContext certsContext;

    static {
        try {
            certsContext = SSLContext.getInstance("SSL");
            certsContext.init(null, trustCerts, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {

        }
    }

    /**
     * SSL socket factory.
     */
    private static final SSLSocketFactory socketFactory = certsContext.getSocketFactory();

}
