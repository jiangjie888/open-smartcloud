//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.AbstractClientHttpResponse;

public class SentinelClientHttpResponse extends AbstractClientHttpResponse {
    private String blockResponse = "RestTemplate request block by sentinel";

    public SentinelClientHttpResponse() {
    }

    public SentinelClientHttpResponse(String blockResponse) {
        this.blockResponse = blockResponse;
    }

    public int getRawStatusCode() throws IOException {
        return HttpStatus.OK.value();
    }

    public String getStatusText() throws IOException {
        return this.blockResponse;
    }

    public void close() {
    }

    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(this.blockResponse.getBytes());
    }

    public HttpHeaders getHeaders() {
        Map<String, List<String>> headers = new HashMap();
        headers.put("Content-Type", Arrays.asList("application/json;charset=UTF-8"));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(headers);
        return httpHeaders;
    }
}
