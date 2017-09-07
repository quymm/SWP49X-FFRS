package models;

import javax.persistence.*;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
@Entity
@Table(name = "LanguageTeach", schema = "mola", catalog = "")
@IdClass(LanguageTeachEntityPK.class)
public class LanguageTeachEntity {
    private int teacherId;
    private String language;
    private String introClip;
    private String status;

    public LanguageTeachEntity() {
    }

    public LanguageTeachEntity(int teacherId, String language, String introClip) {
        this.teacherId = teacherId;
        this.language = language;
        this.introClip = introClip;
        this.status = "pending";
    }

    @Id
    @Column(name = "Teacher_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    @Id
    @Column(name = "Language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Basic
    @Column(name = "IntroClip")
    public String getIntroClip() {
        return introClip;
    }

    public void setIntroClip(String introClip) {
        this.introClip = introClip;
    }

    @Basic
    @Column(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LanguageTeachEntity that = (LanguageTeachEntity) o;

        if (teacherId != that.teacherId) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (introClip != null ? !introClip.equals(that.introClip) : that.introClip != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = teacherId;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (introClip != null ? introClip.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
