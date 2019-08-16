//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel.feign;

import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import feign.Contract.Default;
import feign.InvocationHandlerFactory.MethodHandler;
import feign.hystrix.FallbackFactory;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

public class SentinelFeign {
    public SentinelFeign() {
    }

    public static SentinelFeign.Builder builder() {
        return new SentinelFeign.Builder();
    }

    public static final class Builder extends feign.Feign.Builder implements ApplicationContextAware {
        private Contract contract = new Default();
        private ApplicationContext applicationContext;
        private FeignContext feignContext;

        public Builder() {
        }

        public feign.Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
            throw new UnsupportedOperationException();
        }

        public SentinelFeign.Builder contract(Contract contract) {
            this.contract = contract;
            return this;
        }

        public Feign build() {
            super.invocationHandlerFactory(new InvocationHandlerFactory() {
                public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
                    Object feignClientFactoryBean = Builder.this.applicationContext.getBean("&" + target.type().getName());
                    Class fallback = (Class)Builder.this.getFieldValue(feignClientFactoryBean, "fallback");
                    Class fallbackFactory = (Class)Builder.this.getFieldValue(feignClientFactoryBean, "fallbackFactory");
                    String name = (String)Builder.this.getFieldValue(feignClientFactoryBean, "name");
                    if (Void.TYPE != fallback) {
                        Object fallbackInstance = this.getFromContext(name, "fallback", fallback, target.type());
                        return new SentinelInvocationHandler(target, dispatch, new feign.hystrix.FallbackFactory.Default(fallbackInstance));
                    } else if (Void.TYPE != fallbackFactory) {
                        FallbackFactory fallbackFactoryInstance = (FallbackFactory)this.getFromContext(name, "fallbackFactory", fallbackFactory, FallbackFactory.class);
                        return new SentinelInvocationHandler(target, dispatch, fallbackFactoryInstance);
                    } else {
                        return new SentinelInvocationHandler(target, dispatch);
                    }
                }

                private Object getFromContext(String name, String type, Class fallbackType, Class targetType) {
                    Object fallbackInstance = Builder.this.feignContext.getInstance(name, fallbackType);
                    if (fallbackInstance == null) {
                        throw new IllegalStateException(String.format("No %s instance of type %s found for feign client %s", type, fallbackType, name));
                    } else if (!targetType.isAssignableFrom(fallbackType)) {
                        throw new IllegalStateException(String.format("Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s", type, fallbackType, targetType, name));
                    } else {
                        return fallbackInstance;
                    }
                }
            });
            super.contract(new SentinelContractHolder(this.contract));
            return super.build();
        }

        private Object getFieldValue(Object instance, String fieldName) {
            Field field = ReflectionUtils.findField(instance.getClass(), fieldName);
            field.setAccessible(true);

            try {
                return field.get(instance);
            } catch (IllegalAccessException var5) {
                return null;
            }
        }

        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
            this.feignContext = (FeignContext)this.applicationContext.getBean(FeignContext.class);
        }
    }
}
