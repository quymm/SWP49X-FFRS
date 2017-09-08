package service;

import models.CourseEntity;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import play.db.jpa.JPA;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by stark on 12/06/2017.
 */
public class SearchService {
    public List<CourseEntity> searchCourses(String keyword){
        if(keyword.length() < 2){
            return new ArrayList<>();
        }
        FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(JPA.em());

        QueryBuilder qb = fullTextEm.getSearchFactory()
                .buildQueryBuilder().forEntity(CourseEntity.class).get();
        org.apache.lucene.search.Query query = qb
                .keyword().onFields("title")
                .matching(keyword.toLowerCase())
                .createQuery();

        FullTextQuery hibQuery =
                fullTextEm.createFullTextQuery(query, CourseEntity.class);
        List<CourseEntity> courses = hibQuery.getResultList();
        return courses.stream().filter(course -> course.isActive()).collect(Collectors.toList());
    }

    public List<CourseEntity> searchCourses(String keyword, String[] params) {
        List<CourseEntity> courses = searchCourses(keyword);


        return null;
    }

}
