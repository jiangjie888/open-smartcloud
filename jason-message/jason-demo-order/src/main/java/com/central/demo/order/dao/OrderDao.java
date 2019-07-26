package com.central.demo.order.dao;

import com.central.demo.api.order.model.GoodsOrder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderDao {


    @Select("select * from goods_order t where t.id = #{id}")
    GoodsOrder findById(Long id);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into goods_order(goodsName,count,sum,userId,status,CreationTime,CreatorUserId) "
            + "values(#{goodsName}, #{count}, #{sum}, #{userId}, #{status}, #{creationTime}, #{creatorUserId})")
    int save(GoodsOrder goodsOrder);

    int update(GoodsOrder goodsOrder);
}
