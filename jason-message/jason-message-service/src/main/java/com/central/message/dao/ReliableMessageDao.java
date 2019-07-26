package com.central.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.central.message.api.model.ReliableMessage;
import com.central.message.core.SuperMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 */
//@Mapper
public interface ReliableMessageDao extends SuperMapper<ReliableMessage> {

    @Select("select * from reliable_message t where t.id = #{id}")
    ReliableMessage findById(Long id);

    /*@Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into reliable_message(message_id,message_body,message_data_type,consumer_queue,message_send_times,already_dead,status,remark,version,biz_unique_id,LastModificationTime,LastModifierUserId,CreationTime,CreatorUserId) "
            + "values(#{messageId}, #{messageBody}, #{messageDataType}, #{consumerQueue}, #{messageSendTimes}, #{alreadyDead}, #{status}, #{remark}, #{version}, #{bizUniqueId}, #{lastModificationTime}, #{lastModifierUserId}, #{creationTime}, #{creatorUserId})")
    boolean save(ReliableMessage reliableMessage);*/

    //int update(ReliableMessage reliableMessage);

    //int delete(Map<String, Object> params);

    List<ReliableMessage> findList(Map<String, Object> params);
}
