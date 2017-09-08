package models.front;

/**
 * Created by NHAT QUANG on 6/8/2017.
 */
public class BCourseInfo {
    int id;
    String title;
    String language;
    boolean structure;
    String introduction;
    int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isStructure() {
        return structure;
    }

    public void setStructure(boolean structure) {
        this.structure = structure;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
