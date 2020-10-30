package com.xds.project.data.beanv2;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author XWG
 * @todo
 * @date 2020/10/30.
 */
@Entity
public class SelfStudy implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id(autoincrement = true)
    private Long couId;
    private String userId;
    private String title;
    private String content;
    private Integer model;
    private Integer state;
    private Integer minute;
    private Integer second;
    private Date date;
    private Boolean deleted = false;

    @Generated(hash = 1916290876)
    public SelfStudy(Long couId, String userId, String title, String content,
            Integer model, Integer state, Integer minute, Integer second, Date date,
            Boolean deleted) {
        this.couId = couId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.model = model;
        this.state = state;
        this.minute = minute;
        this.second = second;
        this.date = date;
        this.deleted = deleted;
    }

    @Generated(hash = 1324325844)
    public SelfStudy() {
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }

    public Long getCouId() {
        return couId;
    }

    public void setCouId(Long couId) {
        this.couId = couId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
