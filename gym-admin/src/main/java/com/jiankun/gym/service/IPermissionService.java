package com.jiankun.gym.service;

import com.jiankun.gym.pojo.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiankun.gym.pojo.vo.PermissionVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Li
 * @since 2025-04-21
 */
public interface IPermissionService extends IService<Permission> {

    List<PermissionVO> selectPermissionTree();

    Map<String, Object> selectAssignedPermission(Long roleId);

    void assignPermission(Long roleId, Long[] permissionIds);
}
