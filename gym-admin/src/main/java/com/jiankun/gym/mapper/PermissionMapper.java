package com.jiankun.gym.mapper;

import com.jiankun.gym.pojo.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Li
 * @since 2025-04-21
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> selectPermissionByAdminId(Integer id);
}
