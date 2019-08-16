//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.springframework.cloud.alibaba.sentinel.feign;

import feign.Contract;
import feign.MethodMetadata;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SentinelContractHolder implements Contract {
    private final Contract delegate;
    public static final Map<String, MethodMetadata> metadataMap = new HashMap();

    public SentinelContractHolder(Contract delegate) {
        this.delegate = delegate;
    }

    public List<MethodMetadata> parseAndValidatateMetadata(Class<?> targetType) {
        List<MethodMetadata> metadatas = this.delegate.parseAndValidatateMetadata(targetType);
        metadatas.forEach((metadata) -> {
            MethodMetadata var10000 = (MethodMetadata)metadataMap.put(targetType.getName() + metadata.configKey(), metadata);
        });
        return metadatas;
    }
}
