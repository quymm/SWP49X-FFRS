package models.front;

import models.LanguageTeachEntity;

import java.util.List;

/**
 * Created by NGOCHIEU on 2017-06-30.
 */
public class ExtraTeacherInfo {
    private long numCourse;
    private long numStudent;
    private List<LanguageTeachEntity> languageTeach;
    private List<String> languageSpeak;
    private boolean learned;
    private Integer numOfRate;
    private Double rating;

    public Integer getNumOfRate() {
        return numOfRate;
    }

    public void setNumOfRate(Integer numOfRate) {
        this.numOfRate = numOfRate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public boolean isLearned() {
        return learned;
    }

    public void setLearned(boolean learned) {
        this.learned = learned;
    }

    public List<LanguageTeachEntity> getLanguageTeach() {
        return languageTeach;
    }

    public void setLanguageTeach(List<LanguageTeachEntity> languageTeach) {
        this.languageTeach = languageTeach;
    }

    public List<String> getLanguageSpeak() {
        return languageSpeak;
    }

    public void setLanguageSpeak(List<String> languageSpeak) {
        this.languageSpeak = languageSpeak;
    }

    public long getNumCourse() {
        return numCourse;
    }

    public void setNumCourse(long numCourse) {
        this.numCourse = numCourse;
    }

    public long getNumStudent() {
        return numStudent;
    }

    public void setNumStudent(long numStudent) {
        this.numStudent = numStudent;
    }


}
