package com.xds.project.mvp;


import com.xds.project.data.beanv2.CourseV2;

import java.util.List;


public interface CourseContract {
    interface Presenter extends BasePresenter {
        void updateCourseViewData(long csNameId);
        void deleteCourse(long courseId);
    }

    interface View extends BaseView<Presenter> {
        void initFirstStart();
        void setCourseData(List<CourseV2> courses);
        void updateCoursePreference();
    }


}
