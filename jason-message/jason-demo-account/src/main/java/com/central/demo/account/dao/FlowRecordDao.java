package com.central.demo.account.dao;

import com.central.demo.api.account.model.FlowRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流水记录 Mapper 接口
 * </p>
 */
@Mapper
public interface FlowRecordDao {
    @Select("select * from flow_record t where t.id = #{id}")
    FlowRecord findById(Long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into flow_record(user_id,order_id,name,sum,CreationTime,CreatorUserId) "
            + "values(#{userId}, #{orderId}, #{name}, #{sum}, #{creationTime}, #{creatorUserId})")
    int save(FlowRecord flowRecord);

    int update(FlowRecord flowRecord);

    List<FlowRecord> findList(Map<String, Object> params);
}
