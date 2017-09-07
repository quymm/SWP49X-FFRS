package models;

import org.apache.lucene.analysis.charfilter.MappingCharFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.ngram.NGramFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by NGOCHIEU on 2017-06-06.
 */
@Entity
@Indexed
@AnalyzerDef(name = "customAnalyzer",
    charFilters = {
            @CharFilterDef(factory = MappingCharFilterFactory.class)
    },
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = SnowballPorterFilterFactory.class,
                        params = { @Parameter(name = "language", value = "English") }),
                @TokenFilterDef(factory = NGramFilterFactory.class,
                        params = {
                                @Parameter(name = "minGramSize", value = "2"),
                                @Parameter(name = "maxGramSize", value = "15") } )

        }
)
@Table(name = "Course", schema = "mola", catalog = "")
public class CourseEntity {
    private int id;
    @Field(index= Index.YES, store= Store.NO, analyzer = @Analyzer(definition ="customAnalyzer"))
    private String title;
    private String introduction;
    private Boolean structured;
    private int teacherId;
    private Integer numOfRate = 0;
    private Double rating = 0d;
    private String language;
    private boolean active;
    private Integer price;
    private Timestamp timeCreated;
    private String cover;

    @Basic
    @Column(name = "Cover")
    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Basic
    @Column(name = "TimeCreated")
    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Basic
    @Column(name = "Price", columnDefinition = "int default 0")
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Basic
    @Column(name = "Active")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Basic
    @Column(name = "Language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Transient
    private List<ChapterEntity> chapterList;
    @Transient
    private LessonEntity unstructuredLesson;

    @Transient
    public LessonEntity getUnstructuredLesson() {
        return unstructuredLesson;
    }

    @Transient
    public void setUnstructuredLesson(LessonEntity unstructuredLesson) {
        this.unstructuredLesson = unstructuredLesson;
    }

    @Transient
    public List<ChapterEntity> getChapterList() {
        return chapterList;
    }

    @Transient
    public void setChapterList(List<ChapterEntity> chapterList) {
        this.chapterList = chapterList;
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
    @Column(name = "Introduction")
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Basic
    @Column(name = "Structured")
    public Boolean getStructured() {
        return structured;
    }

    public void setStructured(Boolean structured) {
        this.structured = structured;
    }

    @Basic
    @Column(name = "Teacher_ID")
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
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
    @Column(name = "Rating", columnDefinition = "double default 0")
    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseEntity that = (CourseEntity) o;

        if (id != that.id) return false;
        if (teacherId != that.teacherId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (introduction != null ? !introduction.equals(that.introduction) : that.introduction != null) return false;
        if (structured != null ? !structured.equals(that.structured) : that.structured != null) return false;
        if (numOfRate != null ? !numOfRate.equals(that.numOfRate) : that.numOfRate != null) return false;
        if (rating != null ? !rating.equals(that.rating) : that.rating != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (introduction != null ? introduction.hashCode() : 0);
        result = 31 * result + (structured != null ? structured.hashCode() : 0);
        result = 31 * result + teacherId;
        result = 31 * result + (numOfRate != null ? numOfRate.hashCode() : 0);
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        return result;
    }
}
