package models;

import javax.persistence.*;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
@Entity
@Table(name = "LanguageLearn", schema = "mola", catalog = "")
@IdClass(LanguageLearnEntityPK.class)
public class LanguageLearnEntity {
    private String username;
    private String language;

    @Id
    @Column(name = "Username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id
    @Column(name = "Language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LanguageLearnEntity that = (LanguageLearnEntity) o;

        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }
}
