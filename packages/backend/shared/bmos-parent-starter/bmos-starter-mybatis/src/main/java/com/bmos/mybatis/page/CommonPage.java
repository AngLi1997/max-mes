package com.bmos.mybatis.page;

import cn.hutool.core.lang.func.Func;
import com.github.pagehelper.PageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Function;

/**
 * 分页数据封装类
 */
@Getter
@Setter
public class CommonPage<T> {
    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 总条数
     */
    private Integer total;

    /**
     * 分页数据
     */
    private List<T> list;

    /**
     * 将PageHelper分页后的list转为分页信息
     */
    public static <T> CommonPage<T> convertPage(List<T> list) {
        CommonPage<T> result = new CommonPage<T>();
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        result.setTotalPage(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal((int) pageInfo.getTotal());
        result.setList(pageInfo.getList());
        return result;
    }

    public static <T, R> CommonPage<R> convertPage(List<T> list, Function<List<T>, List<R>> function) {
        CommonPage<R> result = new CommonPage<R>();
        PageInfo<T> pageInfo = new PageInfo<T>(list);
        result.setTotalPage(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal((int) pageInfo.getTotal());
        result.setList(function.apply(list));
        return result;
    }

    /**
     * 分页后的list转为分页信息
     */
    public static <T> CommonPage<T> convertPage(PageInfo<T> pageInfo) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal((int) pageInfo.getTotal());
        result.setList(pageInfo.getList());
        return result;
    }

    public static <O extends BasePage, T> CommonPage<T> CommonPage(List<T> list, Long total, O basePage) {
        CommonPage<T> result = new CommonPage<T>();
        result.setList(list);
        result.setTotal(total.intValue());
        result.setPageNum(basePage.getPageNum());
        result.setPageSize(basePage.getPageSize());
        if (result.getPageSize() > 0) {
            int totalPage = (int) (total / (long) result.getPageSize() + (long) (total % (long) result.getPageSize() == 0L ? 0 : 1));
            result.setTotalPage(totalPage);
        } else {
            result.setTotalPage(0);
        }
        return result;
    }
}
