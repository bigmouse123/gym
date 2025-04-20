package com.jiankun.gym.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiankun.gym.pojo.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiankun.gym.pojo.query.CourseQuery;
import com.jiankun.gym.pojo.vo.CourseVO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Li
 * @since 2025-04-14
 */
public interface ICourseService extends IService<Course> {
    IPage<CourseVO> list(CourseQuery courseQuery);

    void exportExcel(HttpServletResponse response);

    void importExcel(MultipartFile file);
}
