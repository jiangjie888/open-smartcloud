package com.central.user.modular.service.impl;


import com.central.core.service.impl.SuperServiceImpl;
import com.central.user.modular.dao.ThirdDataMapper;
import com.central.user.modular.model.ThirdData;
import com.central.user.modular.service.IThirdDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Slf4j
@Service
public class ThirdDataServiceImpl extends SuperServiceImpl<ThirdDataMapper, ThirdData> implements IThirdDataService {

}
