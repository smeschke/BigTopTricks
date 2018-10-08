package com.example.stephen.bigtoptricks;

import android.os.Parcel;
import android.os.Parcelable;

public class Trick implements Parcelable {

    // Created with the help of AndroidProgrammer tutor from Slack
    public static final Parcelable.Creator<Trick> CREATOR = new Parcelable.Creator<Trick>() {
        @Override
        public Trick createFromParcel(Parcel in) {
            return new Trick(in);
        }

        @Override
        public Trick[] newArray(int size) {
            return new Trick[size];
        }
    };
    private String pr, time_trained, description, name, meta, hit, miss, record, prop_type, goal,
            siteswap, animation, source, difficulty, capacity, tutorial, id, location;

    public Trick(String pr, String time_trained, String description, String name, String meta,
                 String hit, String miss, String record, String prop_type, String goal,
                 String siteswap, String animation, String source, String difficulty, String capacity,
                 String tutorial, String id, String location) {
        this.animation = animation;
        this.capacity = capacity;
        this.description = description;
        this.difficulty = difficulty;
        this.goal = goal;
        this.hit = hit;
        this.meta = meta;
        this.miss = miss;
        this.name = name;
        this.pr = pr;
        this.siteswap = siteswap;
        this.prop_type = prop_type;
        this.source = source;
        this.record = record;
        this.time_trained = time_trained;
        this.tutorial = tutorial;
        this.id = id;
        this.location = location;
    }

    private Trick(Parcel in) {
        pr = in.readString();
        time_trained = in.readString();
        description = in.readString();
        name = in.readString();
        meta = in.readString();
        hit = in.readString();
        miss = in.readString();
        record = in.readString();
        prop_type = in.readString();
        goal = in.readString();
        siteswap = in.readString();
        animation = in.readString();
        source = in.readString();
        difficulty = in.readString();
        capacity = in.readString();
        tutorial = in.readString();
        id = in.readString();
        location = in.readString();
    }

    public Trick() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pr);
        dest.writeString(time_trained);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeString(meta);
        dest.writeString(hit);
        dest.writeString(miss);
        dest.writeString(record);
        dest.writeString(prop_type);
        dest.writeString(goal);
        dest.writeString(siteswap);
        dest.writeString(animation);
        dest.writeString(source);
        dest.writeString(difficulty);
        dest.writeString(capacity);
        dest.writeString(tutorial);
        dest.writeString(id);
        dest.writeString(location);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPr() {
        return pr;
    }

    public void setPr(String pr) {
        this.pr = pr;
    }

    public String getTime_trained() {
        return time_trained;
    }

    public void setTime_trained(String time_trained) {
        this.time_trained = time_trained;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHit() {
        return hit;
    }

    public String getMiss() {
        return miss;
    }

    public String getRecord() {
        return record;
    }

    public String getProp_type() {
        return prop_type;
    }

    public void setProp_type(String prop_type) {
        this.prop_type = prop_type;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getSiteswap() {
        return siteswap;
    }

    public void setSiteswap(String siteswap) {
        this.siteswap = siteswap;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }

    public String getId() {
        return id;
    }

}
