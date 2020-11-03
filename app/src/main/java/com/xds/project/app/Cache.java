package com.xds.project.app;

import android.content.Context;

import com.xds.project.data.beanv2.CourseV2;
import com.xds.project.data.greendao.*;
import com.xds.project.entity.User;
import com.xds.project.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class Cache {
    private Context mContext;
    private CourseGroupDao mCourseGroupDao;
    private CourseV2Dao mCourseV2Dao;
    private CourseV2Dao mLocalDataDao;

    private ThingsDao mThingsDao;
    private SelfStudyDao mSelfStudyDao;
    private UserDao mUserDao;

    private Cache() {
    }

    private static final class Holder {
        private static final Cache instance = new Cache();
    }

    public static Cache instance() {
        return Holder.instance;
    }

    public void init(Context context) {
        mContext = context;
        initGreenDao(context);
        initLocalDao(context);
    }

    private void initGreenDao(Context context) {
        MyOpenHelper devOpenHelper = new MyOpenHelper(
                context, "coursev2.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();

        mCourseGroupDao = daoSession.getCourseGroupDao();
        mCourseV2Dao = daoSession.getCourseV2Dao();
        mThingsDao = daoSession.getThingsDao();
        mSelfStudyDao = daoSession.getSelfStudyDao();
        mUserDao = daoSession.getUserDao();
    }

    private void initLocalDao(Context context) {
        MyOpenHelper devOpenHelper = new MyOpenHelper(
                context, "localData.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        User user = new User();
        user.setUsername("test");
        user.setPassword("123");
        user.setEmail("123@gmail.com");
        user.setPhone("048555555");
        mUserDao.insert(user);
        mLocalDataDao = daoSession.getCourseV2Dao();
        mLocalDataDao.deleteAll();
        List<CourseV2> localData = mLocalDataDao.loadAll();
        if (localData == null || localData.isEmpty()) {
            localData = new ArrayList<>();
            CourseV2 defaultCourse = new CourseV2().setCouOnlyId(AppUtils.createUUID())
                    .setCouAllWeek(Constant.DEFAULT_ALL_WEEK)
                    .setCouWeek(1)
                    .setCouStartNode(2)
                    .setCouNodeCount(1)
                    .setCouName("ACCT5001\nAccounting Principles\n" +
                            "Semester 2 (S2C)")
                    .setCouTeacher("Lecture")
                    .setCouLocation("Online Pre-Recorded")
                    .init();
            CourseV2 defaultCourse1 = new CourseV2().setCouOnlyId(AppUtils.createUUID())
                    .setCouAllWeek(Constant.DEFAULT_ALL_WEEK)
                    .setCouWeek(2)
                    .setCouStartNode(2)
                    .setCouNodeCount(2)
                    .setCouName("ACCT1006\nAccounting and Financial Management\n" +
                            "Semester 2 (S2C)")
                    .setCouTeacher("Tutorial")
                    .setCouLocation("Abercrombie Business School\n" +
                            "wks 1 to 12")
                    .init();
            CourseV2 defaultCourse2 = new CourseV2().setCouOnlyId(AppUtils.createUUID())
                    .setCouAllWeek(Constant.DEFAULT_ALL_WEEK)
                    .setCouWeek(3)
                    .setCouStartNode(3)
                    .setCouNodeCount(5)
                    .setCouName("ACCT3015\nData Analytics for Accounting\n" +
                            "Semester 2 (S2C)")
                    .setCouTeacher("Workshop\n" +
                            "WORK [F16A]")
                    .setCouLocation("Online-Live\n" +
                            "wks 1 to 12")
                    .init();
            CourseV2 defaultCourse3 = new CourseV2().setCouOnlyId(AppUtils.createUUID())
                    .setCouAllWeek(Constant.DEFAULT_ALL_WEEK)
                    .setCouWeek(4)
                    .setCouStartNode(5)
                    .setCouNodeCount(5)
                    .setCouName("ACCT3400\nIndustry and Community Project\n" +
                            "Semester 1 (S1C)")
                    .setCouTeacher("Group Case Assignment\n" +
                            "GRP [F09A]")
                    .setCouLocation("Online-Live\n" +
                            "wks 4, 9, 12")
                    .init();
            localData.add(defaultCourse);
            localData.add(defaultCourse1);
            localData.add(defaultCourse2);
            localData.add(defaultCourse3);
            mLocalDataDao.insertInTx(localData);
        }

    }

    public Context getContext() {
        return mContext;
    }

    public Cache setContext(Context context) {
        mContext = context;
        return this;
    }

    public CourseGroupDao getCourseGroupDao() {
        return mCourseGroupDao;
    }

    public Cache setCourseGroupDao(CourseGroupDao courseGroupDao) {
        mCourseGroupDao = courseGroupDao;
        return this;
    }

    public CourseV2Dao getCourseV2Dao() {
        return mCourseV2Dao;
    }

    public CourseV2Dao getLocalDataDao() {
        return mLocalDataDao;
    }

    public ThingsDao getThingsDao() {
        return mThingsDao;
    }

    public SelfStudyDao getSelfStudyDao() {
        return mSelfStudyDao;
    }

    public UserDao getUserDao() {
        return mUserDao;
    }

    public Cache setCourseV2Dao(CourseV2Dao courseV2Dao) {
        mCourseV2Dao = courseV2Dao;
        return this;
    }

}
