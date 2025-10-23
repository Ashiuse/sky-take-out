package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface SetmealService {
    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐和对应的菜品
     * @param setmealDTO
     * @return
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐及其关联表数据
     * @param ids
     * @return
     */
    void deleteWithDish(List<Long> ids);
}
