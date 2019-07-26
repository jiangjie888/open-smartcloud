package com.central.oauth.modular.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.db.mapper.SuperMapper;
import com.central.oauth.modular.model.Client;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface ClientMapper extends SuperMapper<Client> {
    List<Client> findList(Page<Client> page, @Param("params") Map<String, Object> params);
}
