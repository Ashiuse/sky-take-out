package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //复制购物车数据
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        //设置购物车所属的用户id
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //判断当前商品是否在购物车中
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList != null && shoppingCartList.size() == 1) {
            //购物车中已经存在该商品,则商品数量加1
            ShoppingCart cartService = shoppingCartList.get(0);//找到购物车条目(就一个)中的该商品对象
            cartService.setNumber(cartService.getNumber() + 1);
            //更新购物车数据
            shoppingCartMapper.updateNumberById(cartService);
        } else {
            //购物车中不存在该商品,则添加到购物车中
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                //添加到购物车中的是菜品
                Dish dish = dishMapper.getById(dishId);

                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                //添加到购物车中的是套餐
                Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());//获取套餐数据

                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            //设置商品其他参数
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            //最终将该商品所有数据插入购物车
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        return shoppingCartMapper.list(ShoppingCart.builder().
                userId(BaseContext.getCurrentId()).
                build());
    }


}
