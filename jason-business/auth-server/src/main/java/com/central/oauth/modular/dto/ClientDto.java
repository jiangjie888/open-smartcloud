package com.central.oauth.modular.dto;

import com.central.oauth.modular.model.Client;
import lombok.Data;
import java.util.List;
import java.util.Set;

@Data
public class ClientDto extends Client {
    private static final long serialVersionUID = 1475637288060027265L;

    private List<Long> permissionIds;

    private Set<Long> serviceIds;
}
