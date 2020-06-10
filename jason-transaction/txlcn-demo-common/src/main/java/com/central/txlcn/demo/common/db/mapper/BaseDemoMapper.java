package com.central.txlcn.demo.common.db.mapper;


import com.central.db.mapper.SuperMapper;
import com.central.txlcn.demo.common.db.domain.Demo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@Mapper
public interface BaseDemoMapper extends SuperMapper<Demo> {

    @Insert("insert into t_demo(kid, demo_field, group_id, create_time,app_name) values(#{kid}, #{demoField}, #{groupId}, #{createTime},#{appName})")
    void save(Demo demo);

    /*@Delete("delete from t_demo where id=#{id}")
    void deleteById(Long id);*/
}
