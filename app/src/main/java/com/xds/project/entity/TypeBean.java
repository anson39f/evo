package com.xds.project.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author xds.
 * @TODO
 * @date 2020/7/29.
 * @email
 */
public class TypeBean implements Parcelable {

    /**
     * name : 11
     * id : 9ab84d3c-c2fe-45d6-b158-671993da5d42
     */

    public int level;
    public String name;
    public String id;
    public boolean show;
    public List<TypeBean> child;

    public TypeBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.level);
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeTypedList(this.child);
    }

    protected TypeBean(Parcel in) {
        this.level = in.readInt();
        this.name = in.readString();
        this.id = in.readString();
        this.child = in.createTypedArrayList(TypeBean.CREATOR);
    }

    public static final Creator<TypeBean> CREATOR = new Creator<TypeBean>() {
        @Override
        public TypeBean createFromParcel(Parcel source) {
            return new TypeBean(source);
        }

        @Override
        public TypeBean[] newArray(int size) {
            return new TypeBean[size];
        }
    };
}
