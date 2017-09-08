package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
@Entity
@Table(name = "Lesson", schema = "mola", catalog = "")
public class LessonEntity {
    private int id;
    private String description;
    private String title;
    private Integer chapterId;
    private Integer unstructureCourseId;
    private int number;
    private boolean active;
    private int duration = 30;

    @Transient
    private boolean finished;

    @Basic
    @Column(name = "Duration")
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Basic
    @Column(name = "Active")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
    @Column(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    @Column(name = "Chapter_ID")
    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    @Basic
    @Column(name = "UnstructureCourse_ID")
    public Integer getUnstructureCourseId() {
        return unstructureCourseId;
    }

    public void setUnstructureCourseId(Integer unstructureCourseId) {
        this.unstructureCourseId = unstructureCourseId;
    }

    @Basic
    @Column(name = "Number")
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Transient
    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LessonEntity that = (LessonEntity) o;

        if (id != that.id) return false;
        if (number != that.number) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (chapterId != null ? !chapterId.equals(that.chapterId) : that.chapterId != null) return false;
        if (unstructureCourseId != null ? !unstructureCourseId.equals(that.unstructureCourseId) : that.unstructureCourseId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (chapterId != null ? chapterId.hashCode() : 0);
        result = 31 * result + (unstructureCourseId != null ? unstructureCourseId.hashCode() : 0);
        result = 31 * result + number;
        return result;
    }
}
