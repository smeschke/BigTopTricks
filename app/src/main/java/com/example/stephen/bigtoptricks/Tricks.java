package com.example.stephen.bigtoptricks;

import android.os.Parcel;
import android.os.Parcelable;

public class Tricks implements Parcelable{

    private String pr, time_trained, description, name, meta, hit, miss, record, prop_type, goal,
            siteswap, animation, source, difficulty, capacity, tutorial, id;

    public Tricks(String pr, String time_trained, String description, String name, String meta,
                  String hit, String miss, String record, String prop_type, String goal,
                  String siteswap, String animation, String source, String difficulty, String capacity,
                  String tutorial, String id) {
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
    }

    protected Tricks(Parcel in) {
        pr = in.readString();
        time_trained  = in.readString();
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
    }


    public static final Parcelable.Creator<Tricks> CREATOR = new Parcelable.Creator<Tricks>() {
        @Override
        public Tricks createFromParcel(Parcel in) {
            return new Tricks(in);
        }

        @Override
        public Tricks[] newArray(int size) {
            return new Tricks[size];
        }
    };

    public Tricks() {}

    public String getName() {
        return name;
    }

    public String getPr(){ return pr;}

    public String getTime_trained() {
        return time_trained;
    }

    public String getDescription() {
        return description;
    }

    public String getMeta() { return meta;}

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

    public String getGoal() {
        return goal;
    }

    public String getSiteswap() {
        return siteswap;
    }

    public String getAnimation() {
        return animation;
    }

    public String getSource() {
        return source;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getTutorial() {
        return tutorial;
    }

    public String getId() { return id; }

    public void setPr(String pr) { this.pr = pr; }

    public void setTime_trained(String time_trained) { this.time_trained = time_trained; }

    public void setDescription(String description) { this.description = description; }

    public void setName(String name) { this.name = name; }

    public void setMeta(String meta) { this.meta = meta; }

    public void setHit(String hit) { this.hit = hit; }

    public void setMiss(String miss) { this.miss = miss;}

    public void setRecord(String record) { this.record = record; }

    public void setProp_type(String prop_type) { this.prop_type = prop_type; }

    public void setGoal(String goal) { this.goal = goal; }

    public void setSiteswap(String siteswap) {this.siteswap = siteswap; }

    public void setAnimation(String animation) {this.animation = animation; }

    public void setSource(String source) {this.source = source; }

    public void setDifficulty(String difficulty) {this.difficulty = difficulty; }

    public void setCapacity(String capacity) {this.capacity = capacity;}

    public void setTutorial(String tutorial) {this.tutorial = tutorial;}

    public void setId(String id) {this.id = id;}
}
