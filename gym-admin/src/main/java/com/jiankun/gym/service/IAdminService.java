package com.jiankun.gym.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiankun.gym.pojo.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiankun.gym.pojo.query.AdminQuery;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Li
 * @since 2025-04-14
 */
public interface IAdminService extends IService<Admin> {

    IPage<Admin> list(AdminQuery adminQuery);

    Map<String, Object> selectAssignedRole(Long adminId);

    void assignRole(Long adminId, Long[] roleIds);
}
