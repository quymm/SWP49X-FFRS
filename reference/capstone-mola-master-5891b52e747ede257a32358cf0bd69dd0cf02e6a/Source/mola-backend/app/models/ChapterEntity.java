package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
@Entity
@Table(name = "Chapter", schema = "mola", catalog = "")
public class ChapterEntity {
    private int id;
    private String title;
    private Integer number;
    private int courseId;
    private boolean active;

    @Basic
    @Column(name = "Active")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Transient
    private List<LessonEntity> listLesson;

    @Transient
    public List<LessonEntity> getListLesson() {
        return listLesson;
    }

    @Transient
    public void setListLesson(List<LessonEntity> listLesson) {
        this.listLesson = listLesson;
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
    @Column(name = "Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "Number")
    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Basic
    @Column(name = "Course_ID")
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChapterEntity that = (ChapterEntity) o;

        if (id != that.id) return false;
        if (courseId != that.courseId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + courseId;
        return result;
    }
}
