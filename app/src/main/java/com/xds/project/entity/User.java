package com.xds.project.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author .
 * @TODO
 * @email
 */
public class User implements Parcelable {

    /**
     * id : c29c283b-79b6-42b9-9f75-78c3071e35b2
     * username : 管理员
     * sex : 男
     * birthday :
     * age : null
     * email :
     * address : 222444
     * phone :
     * createTime : 2020-02-22T02:13:51.000+0000
     * updateTime : 2020-02-22T02:13:53.000+0000
     * type : 管理员
     * password : 1234
     * subject : null
     * college : null
     * specialty : null
     */

    public String id;
    public String username;
    public String sex;
    public String birthday;
    public String age;
    public String email;
    public String address;
    public String phone;
    public String createTime;
    public String updateTime;
    public String type;
    public String password;
    public String subject;
    public String college;
    public String specialty;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.sex);
        dest.writeString(this.birthday);
        dest.writeString(this.age);
        dest.writeString(this.email);
        dest.writeString(this.address);
        dest.writeString(this.phone);
        dest.writeString(this.createTime);
        dest.writeString(this.updateTime);
        dest.writeString(this.type);
        dest.writeString(this.password);
        dest.writeString(this.subject);
        dest.writeString(this.college);
        dest.writeString(this.specialty);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.sex = in.readString();
        this.birthday = in.readString();
        this.age = in.readString();
        this.email = in.readString();
        this.address = in.readString();
        this.phone = in.readString();
        this.createTime = in.readString();
        this.updateTime = in.readString();
        this.type = in.readString();
        this.password = in.readString();
        this.subject = in.readString();
        this.college = in.readString();
        this.specialty = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
