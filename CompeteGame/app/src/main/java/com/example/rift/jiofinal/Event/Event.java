package com.example.rift.jiofinal.Event;


import java.io.Serializable;

public class Event implements Serializable {

    private String mRank;
    private String mName;
    private String mScore;


    /**
     * Construct {@link Event}
     *
     * @param rank
     * @param name
     * @param score
     * @paran description
     */
    public Event(String rank, String name, String score) {
        mRank =rank;
        mName = name;
        mScore = score;
    }


    /**
     * Return rank of event in String format.
     *
     * @return
     */
    public String getRank() {
        return mRank;
    }

    public void setRank(String rank) {
        this.mRank = rank;
    }

    /**
     * Return name in String format.
     *
     * @return
     */
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    /**
     * Return score in String format.
     *
     * @return
     */
    public String getScore() {
        return mScore;
    }

    public void setScore(String score) {
        this.mScore = score;
    }


}