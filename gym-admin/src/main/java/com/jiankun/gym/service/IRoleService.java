package com.jiankun.gym.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiankun.gym.pojo.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiankun.gym.pojo.query.RoleQuery;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Li
 * @since 2025-04-21
 */
public interface IRoleService extends IService<Role> {
    IPage<Role> list(RoleQuery roleQuery);
}
