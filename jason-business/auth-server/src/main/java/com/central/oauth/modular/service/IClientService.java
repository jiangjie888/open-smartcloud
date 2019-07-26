package com.central.oauth.modular.service;

import com.central.core.model.page.PageResult;
import com.central.core.model.reqres.response.ResponseData;
import com.central.core.service.ISuperService;
import com.central.oauth.modular.model.Client;

import java.util.Map;

public interface IClientService extends ISuperService<Client> {

    /*
    *
    * */
    ResponseData saveClient(Client clientDto);

    /**
     * 查询应用列表
     * @param params
     * @param isPage 是否分页
     */
    PageResult<Client> listClient(Map<String, Object> params, boolean isPage);


    void delClient(long id);
}
