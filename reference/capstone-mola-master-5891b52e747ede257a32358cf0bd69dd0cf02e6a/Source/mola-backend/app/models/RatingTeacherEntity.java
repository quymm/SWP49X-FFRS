package models;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
@Entity
@Table(name = "RatingTeacher", schema = "mola", catalog = "")
@IdClass(RatingTeacherEntityPK.class)
public class RatingTeacherEntity {
    private int id;
    private Integer rating;
    private int teacherId;
    private String comment;
    private String username;
    private Timestamp time;

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
    @Column(name = "Rating")
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Id
    @Column(name = "Teacher_ID")
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Basic
    @Column(name = "Comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Id
    @Column(name = "Username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "Time")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RatingTeacherEntity that = (RatingTeacherEntity) o;

        if (id != that.id) return false;
        if (teacherId != that.teacherId) return false;
        if (rating != null ? !rating.equals(that.rating) : that.rating != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (time != null ? !time.equals(that.time) : that.time != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        result = 31 * result + teacherId;
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (time != null ? time.hashCode() : 0);
        return result;
    }
}
