//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel.feign;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import feign.Feign;
import feign.MethodMetadata;
import feign.Target;
import feign.Util;
import feign.InvocationHandlerFactory.MethodHandler;
import feign.Target.HardCodedTarget;
import feign.hystrix.FallbackFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class SentinelInvocationHandler implements InvocationHandler {
    private final Target<?> target;
    private final Map<Method, MethodHandler> dispatch;
    private FallbackFactory fallbackFactory;
    private Map<Method, Method> fallbackMethodMap;

    SentinelInvocationHandler(Target<?> target, Map<Method, MethodHandler> dispatch, FallbackFactory fallbackFactory) {
        this.target = (Target)Util.checkNotNull(target, "target", new Object[0]);
        this.dispatch = (Map)Util.checkNotNull(dispatch, "dispatch", new Object[0]);
        this.fallbackFactory = fallbackFactory;
        this.fallbackMethodMap = toFallbackMethod(dispatch);
    }

    SentinelInvocationHandler(Target<?> target, Map<Method, MethodHandler> dispatch) {
        this.target = (Target)Util.checkNotNull(target, "target", new Object[0]);
        this.dispatch = (Map)Util.checkNotNull(dispatch, "dispatch", new Object[0]);
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args)
            throws Throwable {
        if ("equals".equals(method.getName())) {
            try {
                Object otherHandler = args.length > 0 && args[0] != null
                        ? Proxy.getInvocationHandler(args[0])
                        : null;
                return equals(otherHandler);
            }
            catch (IllegalArgumentException e) {
                return false;
            }
        }
        else if ("hashCode".equals(method.getName())) {
            return hashCode();
        }
        else if ("toString".equals(method.getName())) {
            return toString();
        }

        Object result;
        MethodHandler methodHandler = this.dispatch.get(method);
        // only handle by HardCodedTarget
        if (target instanceof Target.HardCodedTarget) {
            Target.HardCodedTarget hardCodedTarget = (Target.HardCodedTarget) target;
            MethodMetadata methodMetadata = SentinelContractHolder.metadataMap
                    .get(hardCodedTarget.type().getName()
                            + Feign.configKey(hardCodedTarget.type(), method));
            // resource default is HttpMethod:protocol://url
            if (methodMetadata == null) {
                result = methodHandler.invoke(args);
            }
            else {
                String resourceName = methodMetadata.template().method().toUpperCase()
                        + ":" + hardCodedTarget.url() + methodMetadata.template().path();
                Entry entry = null;
                try {
                    ContextUtil.enter(resourceName);
                    entry = SphU.entry(resourceName, EntryType.OUT, 1, args);
                    result = methodHandler.invoke(args);
                }
                catch (Throwable ex) {
                    // fallback handle
                    if (!BlockException.isBlockException(ex)) {
                        Tracer.trace(ex);
                    }
                    if (fallbackFactory != null) {
                        try {
                            Object fallbackResult = fallbackMethodMap.get(method)
                                    .invoke(fallbackFactory.create(ex), args);
                            return fallbackResult;
                        }
                        catch (IllegalAccessException e) {
                            // shouldn't happen as method is public due to being an
                            // interface
                            throw new AssertionError(e);
                        }
                        catch (InvocationTargetException e) {
                            throw new AssertionError(e.getCause());
                        }
                    }
                    else {
                        // throw exception if fallbackFactory is null
                        throw ex;
                    }
                }
                finally {
                    if (entry != null) {
                        entry.exit(1, args);
                    }
                    ContextUtil.exit();
                }
            }
        }
        else {
            // other target type using default strategy
            result = methodHandler.invoke(args);
        }

        return result;
    }

/*    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("equals".equals(method.getName())) {
            try {
                Object otherHandler = args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
                return this.equals(otherHandler);
            } catch (IllegalArgumentException var21) {
                return false;
            }
        } else if ("hashCode".equals(method.getName())) {
            return this.hashCode();
        } else if ("toString".equals(method.getName())) {
            return this.toString();
        } else {
            MethodHandler methodHandler = (MethodHandler)this.dispatch.get(method);
            Object result;
            if (this.target instanceof HardCodedTarget) {
                HardCodedTarget hardCodedTarget = (HardCodedTarget)this.target;
                MethodMetadata methodMetadata = (MethodMetadata)SentinelContractHolder.metadataMap.get(method.getDeclaringClass().getName() + Feign.configKey(method.getDeclaringClass(), method));
                String resourceName = methodMetadata.template().method().toUpperCase() + ":" + hardCodedTarget.url() + methodMetadata.template().path();
                Entry entry = null;

                Object var12;
                try {
                    Throwable ex;
                    try {
                        ContextUtil.enter(resourceName);
                        entry = SphU.entry(resourceName, EntryType.OUT, 1, args);
                        result = methodHandler.invoke(args);
                        return result;
                    } catch (Throwable var22) {
                        ex = var22;
                        if (!BlockException.isBlockException(var22)) {
                            Tracer.trace(var22);
                        }
                    }

                    if (this.fallbackFactory == null) {
                        throw var22;
                    }

                    try {
                        Object fallbackResult = ((Method)this.fallbackMethodMap.get(method)).invoke(this.fallbackFactory.create(ex), args);
                        var12 = fallbackResult;
                    } catch (IllegalAccessException var19) {
                        throw new AssertionError(var19);
                    } catch (InvocationTargetException var20) {
                        throw new AssertionError(var20.getCause());
                    }
                } finally {
                    if (entry != null) {
                        entry.exit(1, args);
                    }

                    ContextUtil.exit();
                }

                return var12;
            } else {
                result = methodHandler.invoke(args);
                return result;
            }
        }
    }*/

    public boolean equals(Object obj) {
        if (obj instanceof SentinelInvocationHandler) {
            SentinelInvocationHandler other = (SentinelInvocationHandler)obj;
            return this.target.equals(other.target);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.target.hashCode();
    }

    public String toString() {
        return this.target.toString();
    }

    static Map<Method, Method> toFallbackMethod(Map<Method, MethodHandler> dispatch) {
        Map<Method, Method> result = new LinkedHashMap();
        Iterator var2 = dispatch.keySet().iterator();

        while(var2.hasNext()) {
            Method method = (Method)var2.next();
            method.setAccessible(true);
            result.put(method, method);
        }

        return result;
    }
}
