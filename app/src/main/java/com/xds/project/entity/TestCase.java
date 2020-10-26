package com.xds.project.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @TODO
 * @data
 * @email
 */
public class TestCase implements Parcelable {

    /**
     * id : 520bbd27-df54-4292-8d38-7babaff73564
     * name : null
     * content : 输入的查询条件为数据库中存在的数据，看是否能正确地查出相应得数据
     * keyss : 未建立索引
     * typeid : 6f491c006ac0
     * typename : 性能测试
     * createTime : null
     */

    public String id;
    public String name;
    public String content;
    public String keyss;
    public String typeid;
    public String typename;
    public String createTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.content);
        dest.writeString(this.keyss);
        dest.writeString(this.typeid);
        dest.writeString(this.typename);
        dest.writeString(this.createTime);
    }

    public TestCase() {
    }

    protected TestCase(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.content = in.readString();
        this.keyss = in.readString();
        this.typeid = in.readString();
        this.typename = in.readString();
        this.createTime = in.readString();
    }

    public static final Parcelable.Creator<TestCase> CREATOR = new Parcelable.Creator<TestCase>() {
        @Override
        public TestCase createFromParcel(Parcel source) {
            return new TestCase(source);
        }

        @Override
        public TestCase[] newArray(int size) {
            return new TestCase[size];
        }
    };
}
