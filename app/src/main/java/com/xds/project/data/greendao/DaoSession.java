package com.xds.project.data.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.xds.project.data.beanv2.CourseGroup;
import com.xds.project.data.beanv2.CourseV2;

import com.xds.project.data.greendao.CourseGroupDao;
import com.xds.project.data.greendao.CourseV2Dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig courseGroupDaoConfig;
    private final DaoConfig courseV2DaoConfig;

    private final CourseGroupDao courseGroupDao;
    private final CourseV2Dao courseV2Dao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        courseGroupDaoConfig = daoConfigMap.get(CourseGroupDao.class).clone();
        courseGroupDaoConfig.initIdentityScope(type);

        courseV2DaoConfig = daoConfigMap.get(CourseV2Dao.class).clone();
        courseV2DaoConfig.initIdentityScope(type);

        courseGroupDao = new CourseGroupDao(courseGroupDaoConfig, this);
        courseV2Dao = new CourseV2Dao(courseV2DaoConfig, this);

        registerDao(CourseGroup.class, courseGroupDao);
        registerDao(CourseV2.class, courseV2Dao);
    }
    
    public void clear() {
        courseGroupDaoConfig.clearIdentityScope();
        courseV2DaoConfig.clearIdentityScope();
    }

    public CourseGroupDao getCourseGroupDao() {
        return courseGroupDao;
    }

    public CourseV2Dao getCourseV2Dao() {
        return courseV2Dao;
    }

}
