package com.xds.project.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author .
 * @TODO
 * @email
 */
@Entity
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id(autoincrement = true)
    private Long id;
    private String username;
    private String headPic;
    private String sex;
    private String birthday;
    private String age;
    private String email;
    private String address;
    private String phone;
    private String createTime;
    private String updateTime;
    private String type;
    private String password;
    private String subject;
    private String college;
    private String specialty;

    @Generated(hash = 1435834075)
    public User(Long id, String username, String headPic, String sex,
            String birthday, String age, String email, String address, String phone,
            String createTime, String updateTime, String type, String password,
            String subject, String college, String specialty) {
        this.id = id;
        this.username = username;
        this.headPic = headPic;
        this.sex = sex;
        this.birthday = birthday;
        this.age = age;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.type = type;
        this.password = password;
        this.subject = subject;
        this.college = college;
        this.specialty = specialty;
    }


    @Generated(hash = 586692638)
    public User() {
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }


    public Long getId() {
        return this.id;
    }
}
