package com.central.core.model.page;

import lombok.Data;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;
import java.util.List;

/**
 * @program: open-smartcloud
 * @description:封装分页结果集
 * @author: jason
 * @create: 2018-11-15 14:34
 **/
@Data
public class PageResultPlus<T> implements Serializable {

    private static final long serialVersionUID = -4071521319254024213L;

    private Long current = 1L;// 要查找第几页
    private Long size = 20L;// 每页显示多少条
    //private Long totalPage = 0L;// 总页数
    private Long total = 0L;// 总记录数
    private List<T> rows;// 结果集

    public PageResultPlus() {
    }

    public PageResultPlus(Page<T> page) {
        this.setRows(page.getRecords());
        this.setTotal(page.getTotal());
        this.setCurrent(page.getCurrent());
        this.setSize(page.getSize());

        /*if(page.getSize()>0) {
            this.setTotalPage((long)Math.ceil((double)this.getTotalRows()/ this.pageSize));
        }*/
    }

}
