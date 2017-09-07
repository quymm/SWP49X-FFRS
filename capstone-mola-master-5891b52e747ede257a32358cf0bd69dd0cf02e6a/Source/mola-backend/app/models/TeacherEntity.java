package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
@Entity
@Table(name = "Teacher", schema = "mola", catalog = "")
public class TeacherEntity {
    private int id;
    private Integer expYears;
    private String expDescription;
    private String introduction;
    private Integer numOfRate = 0;
    private Double rating = 0d;
    private String username;
    private String status;
    private Timestamp timeRegistered;

    public TeacherEntity() {
    }

    public TeacherEntity(String username, Integer expYears, String expDescription) {
        this.expYears = expYears;
        this.introduction = expDescription;
        this.username = username;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ExpYears")
    public Integer getExpYears() {
        return expYears;
    }

    public void setExpYears(Integer expYears) {
        this.expYears = expYears;
    }

    @Basic
    @Column(name = "ExpDescription")
    public String getExpDescription() {
        return expDescription;
    }

    public void setExpDescription(String expDescription) {
        this.expDescription = expDescription;
    }

    @Basic
    @Column(name = "Introduction")
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Basic
    @Column(name = "NumOfRate", columnDefinition = "int default 0")
    public Integer getNumOfRate() {
        return numOfRate;
    }

    public void setNumOfRate(Integer numOfRate) {
        this.numOfRate = numOfRate;
    }

    @Basic
    @Column(name = "Rating")
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    @Basic
    @Column(name = "Username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "TimeRegistered")
    public Timestamp getTimeRegistered() {
        return timeRegistered;
    }

    public void setTimeRegistered(Timestamp timeRegistered) {
        this.timeRegistered = timeRegistered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeacherEntity that = (TeacherEntity) o;

        if (id != that.id) return false;
        if (expYears != null ? !expYears.equals(that.expYears) : that.expYears != null) return false;
        if (expDescription != null ? !expDescription.equals(that.expDescription) : that.expDescription != null)
            return false;
        if (introduction != null ? !introduction.equals(that.introduction) : that.introduction != null) return false;
        if (numOfRate != null ? !numOfRate.equals(that.numOfRate) : that.numOfRate != null) return false;
        if (rating != null ? !rating.equals(that.rating) : that.rating != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (expYears != null ? expYears.hashCode() : 0);
        result = 31 * result + (expDescription != null ? expDescription.hashCode() : 0);
        result = 31 * result + (introduction != null ? introduction.hashCode() : 0);
        result = 31 * result + (numOfRate != null ? numOfRate.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    public void addRating(int newRate) {
        rating = ((double) Math.round(((rating * numOfRate)+newRate)/(++numOfRate)*10))/10;
    }
}
