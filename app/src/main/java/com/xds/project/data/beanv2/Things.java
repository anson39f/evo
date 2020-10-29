package com.xds.project.data.beanv2;

import com.freelib.multiitem.item.BaseItemData;
import com.xds.project.R;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * @author
 * @todo
 * @date 2020/10/29.
 */
@Entity
public class Things extends BaseItemData implements Serializable, Comparable<Things> {
    private static final long serialVersionUID = 1L;
    @Id(autoincrement = true)
    private Long couId;
    private String userId;
    private String name;
    private String content;
    private Integer level;
    private Integer state;//3 todolist 2 processing 1 done
    private Date date;
    private Boolean deleted = false;
    private Boolean history = false;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getHistory() {
        return history;
    }

    public void setHistory(Boolean history) {
        this.history = history;
    }

    @Generated(hash = 1715286268)
    public Things() {
    }

    @Generated(hash = 1422807291)
    public Things(Long couId, String userId, String name, String content,
                  Integer level, Integer state, Date date, Boolean deleted,
                  Boolean history) {
        this.couId = couId;
        this.userId = userId;
        this.name = name;
        this.content = content;
        this.level = level;
        this.state = state;
        this.date = date;
        this.deleted = deleted;
        this.history = history;
    }

    public Long getCouId() {
        return couId;
    }

    public void setCouId(Long couId) {
        this.couId = couId;
    }

    public String getName() {
        return name;
    }

    public String getLevelName() {
        switch (level) {
            case 2:
                return "high";
            case 1:
                return "middle";
            default:
                return "low";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public Integer getLevel() {
        return level;
    }

    public int getLevelColor() {
        switch (level) {
            case 2:
                return R.color.red_btn_press;
            case 1:
                return R.color.refuse_btn_default;
            default:
                return R.color.finish_btn_clickable_color;
        }
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    @Override
    public int compareTo(Things o) {
        if (getState().equals(o.getState())) {
            if (o.getLevel().equals(getLevel())) {
                return o.getDate().compareTo(getDate());
            }
            return o.getLevel() - getLevel();
        }
        return o.getState() - getState();
    }
}
