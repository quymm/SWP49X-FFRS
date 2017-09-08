package utils;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import play.db.jpa.JPA;

import javax.persistence.EntityManager;

/**
 * Created by stark on 12/06/2017.
 */
public class LuceneInitialIndexed {
    public static void indexExistedCourses(){
        try {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(JPA.em());

        fullTextEntityManager.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
