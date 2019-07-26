package com.central.core.base.warpper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.central.core.model.page.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 控制器查询结果的包装类基类
 */
public abstract class BaseControllerWrapper {

    private Page<Map<String, Object>> page = null;

    private PageResult<Map<String, Object>> pageResult = null;

    private Map<String, Object> single = null;

    private List<Map<String, Object>> multi = null;

    public BaseControllerWrapper(Map<String, Object> single) {
        this.single = single;
    }

    public BaseControllerWrapper(List<Map<String, Object>> multi) {
        this.multi = multi;
    }

    public BaseControllerWrapper(Page<Map<String, Object>> page) {
        if (page != null && page.getRecords() != null) {
            this.page = page;
            this.multi = page.getRecords();
        }
    }

    public BaseControllerWrapper(PageResult<Map<String, Object>> pageResult) {
        if (pageResult != null && pageResult.getData() != null) {
            this.pageResult = pageResult;
            this.multi = pageResult.getData();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T wrap() {

        /**
         * 包装结果
         */
        if (single != null) {
            wrapTheMap(single);
        }
        if (multi != null) {
            for (Map<String, Object> map : multi) {
                wrapTheMap(map);
            }
        }

        /**
         * 根据请求的参数响应
         */
        if (page != null) {
            return (T) page;
        }
        if (pageResult != null) {
            return (T) pageResult;
        }
        if (single != null) {
            return (T) single;
        }
        if (multi != null) {
            return (T) multi;
        }

        return null;
    }

    protected abstract void wrapTheMap(Map<String, Object> map);
}
