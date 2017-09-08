package models.front;

import java.util.List;

/**
 * Created by NHAT QUANG on 6/8/2017.
 */
public class BListLesson {
    int chapterId;
    List<BLessonInfo> listLesson;

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public List<BLessonInfo> getListLesson() {
        return listLesson;
    }

    public void setListLesson(List<BLessonInfo> listLesson) {
        this.listLesson = listLesson;
    }
}
