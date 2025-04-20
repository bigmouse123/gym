package com.jiankun.gym.mapper;

import com.jiankun.gym.pojo.entity.Coach;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiankun.gym.pojo.vo.CoachCountVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Li
 * @since 2025-04-14
 */
public interface CoachMapper extends BaseMapper<Coach> {

    List<CoachCountVO> selectCoachCount();
}
