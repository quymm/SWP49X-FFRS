package models;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
@Entity
@Table(name = "RegisterCourse", schema = "mola", catalog = "")
public class RegisterCourseEntity {
    private int id;
    private int courseId;
    private String username;
    private String status;
    private Timestamp timeRequest;

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
    @Column(name = "Course_ID")
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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
    @Column(name = "TimeRequest")
    public Timestamp getTimeRequest() {
        return timeRequest;
    }

    public void setTimeRequest(Timestamp timeRequest) {
        this.timeRequest = timeRequest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisterCourseEntity that = (RegisterCourseEntity) o;

        if (id != that.id) return false;
        if (courseId != that.courseId) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + courseId;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
