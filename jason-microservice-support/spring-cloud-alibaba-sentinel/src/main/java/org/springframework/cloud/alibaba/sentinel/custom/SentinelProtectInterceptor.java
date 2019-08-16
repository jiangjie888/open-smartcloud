//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel.custom;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import org.springframework.cloud.alibaba.sentinel.annotation.SentinelRestTemplate;
import org.springframework.cloud.alibaba.sentinel.rest.SentinelClientHttpResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

public class SentinelProtectInterceptor implements ClientHttpRequestInterceptor {
    private final SentinelRestTemplate sentinelRestTemplate;
    private final RestTemplate restTemplate;

    public SentinelProtectInterceptor(SentinelRestTemplate sentinelRestTemplate, RestTemplate restTemplate) {
        this.sentinelRestTemplate = sentinelRestTemplate;
        this.restTemplate = restTemplate;
    }

    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        URI uri = request.getURI();
        String hostResource = request.getMethod().toString() + ":" + uri.getScheme() + "://" + uri.getHost() + (uri.getPort() == -1 ? "" : ":" + uri.getPort());
        String hostWithPathResource = hostResource + uri.getPath();
        boolean entryWithPath = true;
        if (hostResource.equals(hostWithPathResource)) {
            entryWithPath = false;
        }

        Entry hostEntry = null;
        Entry hostWithPathEntry = null;
        ClientHttpResponse response = null;

        ClientHttpResponse var12;
        try {
            hostEntry = SphU.entry(hostResource, EntryType.OUT);
            if (entryWithPath) {
                hostWithPathEntry = SphU.entry(hostWithPathResource, EntryType.OUT);
            }

            response = execution.execute(request, body);
            if (this.restTemplate.getErrorHandler().hasError(response)) {
                Tracer.trace(new IllegalStateException("RestTemplate ErrorHandler has error"));
            }

            return response;
        } catch (Throwable var16) {
            if (!BlockException.isBlockException(var16)) {
                Tracer.trace(var16);
                return response;
            }

            var12 = this.handleBlockException(request, body, execution, (BlockException)var16);
        } finally {
            if (hostWithPathEntry != null) {
                hostWithPathEntry.exit();
            }

            if (hostEntry != null) {
                hostEntry.exit();
            }

        }

        return var12;
    }

    private ClientHttpResponse handleBlockException(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException ex) {
        Object[] args = new Object[]{request, body, execution, ex};
        Method blockHandler;
        if (this.isDegradeFailure(ex)) {
            blockHandler = this.extractFallbackMethod(this.sentinelRestTemplate.fallback(), this.sentinelRestTemplate.fallbackClass());
            return (ClientHttpResponse)(blockHandler != null ? this.methodInvoke(blockHandler, args) : new SentinelClientHttpResponse());
        } else {
            blockHandler = this.extractBlockHandlerMethod(this.sentinelRestTemplate.blockHandler(), this.sentinelRestTemplate.blockHandlerClass());
            return (ClientHttpResponse)(blockHandler != null ? this.methodInvoke(blockHandler, args) : new SentinelClientHttpResponse());
        }
    }

    private ClientHttpResponse methodInvoke(Method method, Object... args) {
        try {
            return (ClientHttpResponse)method.invoke((Object)null, args);
        } catch (IllegalAccessException var4) {
            throw new RuntimeException(var4);
        } catch (InvocationTargetException var5) {
            throw new RuntimeException(var5);
        }
    }

    private Method extractFallbackMethod(String fallback, Class<?> fallbackClass) {
        return BlockClassRegistry.lookupFallback(fallbackClass, fallback);
    }

    private Method extractBlockHandlerMethod(String block, Class<?> blockClass) {
        return BlockClassRegistry.lookupBlockHandler(blockClass, block);
    }

    private boolean isDegradeFailure(BlockException ex) {
        return ex instanceof DegradeException;
    }
}
