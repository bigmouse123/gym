package com.jiankun.gym.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/5/4 22:36
 */
@Data
public class RouterVO {
    private String name;
    private String icon;
    //只有菜单权限有path
    private String path;
    //只有目录权限有children
    private List<RouterVO> children;
}