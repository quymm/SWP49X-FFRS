package models;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
public class RatingTeacherEntityPK implements Serializable {
    private int id;
    private int teacherId;
    private String username;

    @Column(name = "ID")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "Teacher_ID")
    @Id
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Column(name = "Username")
    @Id
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RatingTeacherEntityPK that = (RatingTeacherEntityPK) o;

        if (id != that.id) return false;
        if (teacherId != that.teacherId) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + teacherId;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
