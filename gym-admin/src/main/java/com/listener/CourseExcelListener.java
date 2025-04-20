package com.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.jiankun.gym.mapper.CourseMapper;
import com.jiankun.gym.pojo.entity.Course;
import com.jiankun.gym.pojo.vo.CourseExcelVO;
import org.springframework.beans.BeanUtils;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/21 0:40
 */
public class CourseExcelListener extends AnalysisEventListener<CourseExcelVO> {
    private CourseMapper courseMapper;

    public CourseExcelListener(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    //一行一行读取excle中的内容，从第二行开始读取
    @Override
    public void invoke(CourseExcelVO courseExcelVO, AnalysisContext analysisContext) {
        System.out.println("CourseExcelListener.invoke");
        Course course = new Course();
        BeanUtils.copyProperties(courseExcelVO, course);
        //插入的数据避免和原有id冲突
        course.setId(null);
        courseMapper.insert(course);
    }

    //读取之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("CourseExcelListener.doAfterAllAnalysed");
    }
}