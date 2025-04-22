package com.jiankun.gym.pojo.vo;

import com.jiankun.gym.pojo.entity.Permission;
import lombok.Data;

import java.util.List;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/22 15:17
 */
@Data
public class PermissionVO extends Permission {
    //是否有子节点
    private Boolean hasChildren;
    //所有的子节点
    private List<PermissionVO> children;
}
