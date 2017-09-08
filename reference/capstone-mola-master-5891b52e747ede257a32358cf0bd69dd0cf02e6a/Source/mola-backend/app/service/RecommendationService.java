package service;

import dao.*;
import javafx.util.Pair;
import models.*;
import play.cache.CacheApi;
import utils.Const;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rocks on 6/9/2017.
 */
public class RecommendationService {

    public static Map initDataSet() {
//        List<RatingCourseEntity> ratings = RatingCourseDAO.getAllRatings();
//
//        Map<String, Double> rating;
//        Map<String, Map<String, Double>> userBasedDataSet = new HashMap<>();
//        for (RatingCourseEntity ratingCourseEntity: ratings) {
//            String username = ratingCourseEntity.getUsername();
//            int courseId = ratingCourseEntity.getCourseId();
//            int ratingValue = ratingCourseEntity.getRating();
//            int teacherId = CourseDAO.getCourseById(courseId).getTeacherId();
//
//            List<RatingTeacherEntity> teacherRatings = RatingTeacherDAO.getRatingsByTeacherId(teacherId);
//            int registerCourseCount = RegisterCourseDAO.countByCourseId(courseId);
//            Timestamp courseTimeCreated = CourseDAO.getCourseById(courseId).getTimeCreated();
//            int teacherRegisterCourseCount = RegisterCourseDAO.countByTeacherId(teacherId);
//            TeacherEntity teacherEntity = TeacherDAO.getTeacherById(teacherId);
//            Timestamp teacherTimeRegistered = teacherEntity.getTimeRegistered();
//            int numberOfCourses = CourseDAO.getByTeacher(teacherEntity.getUsername()).size();
//
//            double normalizedRating = ratingValue * Const.COURSE_RATING_FACTOR
//                    + CalculatingService.averageTeacherRating(teacherRatings) * Const.TEACHER_RATING_FACTOR
//                    + CalculatingService.courseRegisterByWeekPoint(registerCourseCount, courseTimeCreated)
//                    * Const.COURSE_REGISTER_BY_WEEK_FACTOR
//                    + CalculatingService.teacherAverageCourseRegisterByWeekPoint
//                    (teacherRegisterCourseCount, teacherTimeRegistered, numberOfCourses)
//                    * Const.TEACHER_REGISTER_BY_WEEK_FACTOR;
//
//            if (userBasedDataSet.containsKey(username)) {
//                rating = userBasedDataSet.get(username);
//            } else {
//                rating = new HashMap<>();
//            }
//            rating.put(String.valueOf(courseId), normalizedRating);
//            userBasedDataSet.put(username, rating);
//        }
//        return userBasedDataSet;

        // Rework
        Map<String, Double> coursesScores;
        Map<String, Map<String, Double>> userBasedDataSet = new HashMap<>();

        List<UserEntity> allLearners = UserDAO.getAllLearner();

        for (UserEntity userEntity : allLearners
                ) {
            String username = userEntity.getUsername();
            coursesScores = new HashMap<>();

            List<CourseEntity> courses = CourseDAO.getRegisteredCourses(username);

            for (CourseEntity course : courses
                    ) {
                int courseId = course.getId();
                List<LessonEntity> allLessons = new ArrayList<>();

                List<ChapterEntity> chapters = ChapterDAO.getChapterByCourseId(courseId);
                for (ChapterEntity chapter : chapters
                        ) {
                    int chapterId = chapter.getId();
                    List<LessonEntity> lessons = LessonDAO.getLessonByChapterId(chapterId);
                    allLessons.addAll(lessons);
                } // end for chapters

                double score = 0;
                if (!allLessons.isEmpty()) {
                    int completed = 0;
                    for (LessonEntity lesson : allLessons
                            ) {
                        int lessonId = lesson.getId();
                        if (SessionDAO.checkFinishedLesson(username, lessonId)) {
                            completed += 1;
                        }
                    }
                    double a = Const.PERCENTAGE_FINISH_LESSON_FACTOR *
                            CalculatingService.percentageFinishedLessonPoint(allLessons.size(), completed);
                    score += a;
                }

                int teacherId = course.getTeacherId();
                TeacherEntity teacherEntity = TeacherDAO.getTeacherById(teacherId);
                score += Const.TEACHER_RATING_FACTOR * teacherEntity.getRating();

                int registerCourseCount = RegisterCourseDAO.countByCourseId(courseId);
                Timestamp courseTimeCreated = course.getTimeCreated();
                score += Const.COURSE_REGISTER_BY_WEEK_FACTOR *
                        CalculatingService.courseRegisterByWeekPoint(registerCourseCount, courseTimeCreated);

                double price = (double) course.getPrice();
                score += Const.PRICE_FACTOR *
                        CalculatingService.pricePoint(price);
                double normalizedScore = CalculatingService.scoreDivideByHour(score, courseTimeCreated);

                coursesScores.put(String.valueOf(courseId), normalizedScore);
            } // end for courses

            userBasedDataSet.put(username, coursesScores);
        } // end for learners

        return userBasedDataSet;
    }

    public static Map initDataSet(EntityManager em) {
//        List<RatingCourseEntity> ratings = RatingCourseDAO.getAllRatings(em);
//
//        Map<String, Double> rating;
//        Map<String, Map<String, Double>> userBasedDataSet = new HashMap<>();
//        for (RatingCourseEntity ratingCourseEntity : ratings) {
//            String username = ratingCourseEntity.getUsername();
//            int courseId = ratingCourseEntity.getCourseId();
//            int ratingValue = ratingCourseEntity.getRating();
//            int teacherId = CourseDAO.getCourseById(courseId, em).getTeacherId();
//
//            List<RatingTeacherEntity> teacherRatings = RatingTeacherDAO.getRatingsByTeacherId(teacherId, em);
//            int registerCourseCount = RegisterCourseDAO.countByCourseId(courseId, em);
//            Timestamp courseTimeCreated = CourseDAO.getCourseById(courseId, em).getTimeCreated();
//            int teacherRegisterCourseCount = RegisterCourseDAO.countByTeacherId(teacherId, em);
//            TeacherEntity teacherEntity = TeacherDAO.getTeacherById(teacherId, em);
//            Timestamp teacherTimeRegistered = teacherEntity.getTimeRegistered();
//            int numberOfCourses = CourseDAO.getByTeacher(teacherEntity.getUsername(), em).size();
//
//            double normalizedRating = ratingValue * Const.COURSE_RATING_FACTOR
//                    + CalculatingService.averageTeacherRating(teacherRatings) * Const.TEACHER_RATING_FACTOR
//                    + CalculatingService.courseRegisterByWeekPoint(registerCourseCount, courseTimeCreated)
//                    * Const.COURSE_REGISTER_BY_WEEK_FACTOR
//                    + CalculatingService.teacherAverageCourseRegisterByWeekPoint
//                    (teacherRegisterCourseCount, teacherTimeRegistered, numberOfCourses)
//                    * Const.TEACHER_REGISTER_BY_WEEK_FACTOR;
//
//            if (userBasedDataSet.containsKey(username)) {
//                rating = userBasedDataSet.get(username);
//            } else {
//                rating = new HashMap<>();
//            }
//            rating.put(String.valueOf(courseId), normalizedRating);
//            userBasedDataSet.put(username, rating);
//        }
//        return userBasedDataSet;

        // Rework
        Map<String, Double> coursesScores;
        Map<String, Map<String, Double>> userBasedDataSet = new HashMap<>();

        List<UserEntity> allLearners = UserDAO.getAllLearner(em);

        for (UserEntity userEntity : allLearners
                ) {
            String username = userEntity.getUsername();
            coursesScores = new HashMap<>();

            List<CourseEntity> courses = CourseDAO.getRegisteredCourses(username, em);

            for (CourseEntity course : courses
                    ) {
                int courseId = course.getId();
                List<LessonEntity> allLessons = new ArrayList<>();

                List<ChapterEntity> chapters = ChapterDAO.getChapterByCourseId(courseId, em);
                for (ChapterEntity chapter : chapters
                        ) {
                    int chapterId = chapter.getId();
                    List<LessonEntity> lessons = LessonDAO.getLessonByChapterId(chapterId, em);
                    allLessons.addAll(lessons);
                } // end for chapters

                double score = 0;
                if (!allLessons.isEmpty()) {
                    int completed = 0;
                    for (LessonEntity lesson : allLessons
                            ) {
                        int lessonId = lesson.getId();
                        if (SessionDAO.checkFinishedLesson(username, lessonId, em)) {
                            completed += 1;
                        }
                    }
                    double a = Const.PERCENTAGE_FINISH_LESSON_FACTOR *
                            CalculatingService.percentageFinishedLessonPoint(allLessons.size(), completed);
                    score += a;
                }

                int teacherId = course.getTeacherId();
                TeacherEntity teacherEntity = TeacherDAO.getTeacherById(teacherId, em);
                score += Const.TEACHER_RATING_FACTOR * teacherEntity.getRating();

                int registerCourseCount = RegisterCourseDAO.countByCourseId(courseId, em);
                Timestamp courseTimeCreated = course.getTimeCreated();
                score += Const.COURSE_REGISTER_BY_WEEK_FACTOR *
                        CalculatingService.courseRegisterByWeekPoint(registerCourseCount, courseTimeCreated);

                double price = (double) course.getPrice();
                score += Const.PRICE_FACTOR *
                        CalculatingService.pricePoint(price);
                double normalizedScore = CalculatingService.scoreDivideByHour(score, courseTimeCreated);

                coursesScores.put(String.valueOf(courseId), normalizedScore);
            } // end for courses

            userBasedDataSet.put(username, coursesScores);
        } // end for learners

        return userBasedDataSet;
    }

    public static Map<String, Map<String, Double>> flipDataSet(Map<String, Map<String, Double>> dataSet1) {
        Map<String, Map<String, Double>> dataSet2 = new HashMap<>();
        Map<String, Double> object_rating;
        for (Map.Entry<String, Map<String, Double>> objectEntry : dataSet1.entrySet()
                ) {
            String object = objectEntry.getKey();
            for (Map.Entry<String, Double> itemEntry : objectEntry.getValue().entrySet()
                    ) {
                String item = itemEntry.getKey();
                if (dataSet2.containsKey(item)) {
                    object_rating = dataSet2.get(item);
                } else {
                    object_rating = new HashMap<>();
                }
                object_rating.put(object, itemEntry.getValue());
                dataSet2.put(item, object_rating);
            }
        }
        return dataSet2;
    }

    public static List userBasedRecommendations(Map<String, Map<String, Double>> dataSet, String object) {
        Map<String, Double> totals = new HashMap<>(); // total ratings for a thing to be recommend
        Map<String, Double> scores = new HashMap<>(); // total score of similar objects to the recommend thing
        List<Pair> rankings = new ArrayList<>(); // ranked not interactive list

        for (Map.Entry<String, Map<String, Double>> entry : dataSet.entrySet()) {
            String objectName = entry.getKey();
            if (objectName != object) { // each other object
                double score = CalculatingService.similarPearson(dataSet, object, objectName); // score between this object and the object to get recommendation
                if (score > 0) {
                    for (Map.Entry<String, Double> recommendEntry : dataSet.get(objectName).entrySet() // each recommend for this object
                            ) {
                        String recommend = recommendEntry.getKey();
                        if (!dataSet.get(object).containsKey(recommend)) { // not interactive
                            if (!totals.containsKey(recommend)) {
                                totals.put(recommend, Double.valueOf(0));
                            }
                            totals.put(recommend, totals.get(recommend) + recommendEntry.getValue() * score);

                            if (!scores.containsKey(recommend)) {
                                scores.put(recommend, Double.valueOf(0));
                            }
                            scores.put(recommend, scores.get(recommend) + score);
                        } // end not interactive
                    } // end each recommend
                }
            } // end each other object
        } // end the loop

        for (String recommend : totals.keySet()) {
            rankings.add(new Pair(recommend, totals.get(recommend) / scores.get(recommend)));
        }

        // sort and reverse the list
        rankings.sort(Comparator.comparingDouble(pair -> Double.valueOf(pair.getValue().toString())));
        Collections.reverse(rankings);

        return rankings;
    }

    // find best n matched objects for a certain object
    public static List topMatches(Map<String, Map<String, Double>> dataSet, String object, int n) {
        // the list to be return
        List<Pair> matches = new ArrayList<>();

        // calculate Score for each other object
        for (Map.Entry<String, Map<String, Double>> entry : dataSet.entrySet()
                ) {
            String key = entry.getKey();
            if (!key.equals(object)) {
                matches.add(new Pair(key, CalculatingService.similarPearson(dataSet, object, key)));
            }
        }

        // sort and reverse the list
        matches.sort(Comparator.comparingDouble(pair -> Double.valueOf(pair.getValue().toString())));
        Collections.reverse(matches);

        return matches.subList(0, n);
    }

    // find n most similar items for every item
    public static Map similarItemsDictionary(Map<String, Map<String, Double>> dataSet, int n) {
        // the result list to be returned
        Map<String, Map<String, Double>> result = new HashMap<>();

        for (Map.Entry<String, Map<String, Double>> objectEntry : dataSet.entrySet()
                ) {
            String object = objectEntry.getKey();
            List<Pair> items = topMatches(dataSet, object, n);

            // Transform List<Pair> into Map<String, Double> to put in redis cache
            Map<String, Double> itemsToMap = new HashMap<>();
            for (Pair item : items
                    ) {
                itemsToMap.put(item.getKey().toString(), Double.valueOf(item.getValue().toString()));
            }

            result.put(object, itemsToMap);
        }

        return result;
    }

    // item-based recommendation for a certain object
    public static List getItemBasedRecommendations(Map<String, Map<String, Double>> dataSet,
                                                   Map<String, Map<String, Double>> dictionary, String object) {
        Map<String, Double> objectRatings = dataSet.get(object); // ratings of this object
        Map<String, Double> totals = new HashMap<>(); // total similar products rating value for an item
        Map<String, Double> scores = new HashMap<>(); // total similar score for an item
        List<Pair> rankings = new ArrayList<>(); // ranked not interactive list

        for (Map.Entry<String, Double> ratingEntry : objectRatings.entrySet() // loop all item interactive by this object
                ) {
            String item = ratingEntry.getKey();
            double rating = ratingEntry.getValue(); // rating of this object to current item
            Map<String, Double> similarItems = dictionary.get(item); // similar items to current item
            for (Map.Entry<String, Double> similarItemEntry : similarItems.entrySet()) { // loop each similar item
                String similarItem = similarItemEntry.getKey();
                double similarValue = similarItemEntry.getValue();
                if (!objectRatings.containsKey(similarItem)) { // not interactive by current object
                    if (!totals.containsKey(similarItem)) {
                        totals.put(similarItem, Double.valueOf(0));
                    }
                    totals.put(similarItem, totals.get(similarItem) + similarValue * rating);

                    if (!scores.containsKey(similarItem)) {
                        scores.put(similarItem, Double.valueOf(0));
                    }
                    scores.put(similarItem, scores.get(similarItem) + similarValue);
                }
            } // end loop each similar
        } // end loop all item

        for (String item : totals.keySet()
                ) {
            rankings.add(new Pair(item, scores.get(item) > 0 ? totals.get(item) / scores.get(item) : 0));
        }

        // sort and reverse the list
        rankings.sort(Comparator.comparingDouble(pair -> Double.valueOf(pair.getValue().toString())));
        Collections.reverse(rankings);

        return rankings;
    }

    // Recommendation for new user or user who has few ratings
    public static List getNewUserRecommendations(String language) {
        List<CourseEntity> courseEntityList = CourseDAO.getByLanguage(language);
        List<Pair> result = new ArrayList<>();
        for (CourseEntity course : courseEntityList
                ) {
            int courseId = course.getId();
            int teacherId = course.getTeacherId();

            List<RatingCourseEntity> courseRatings = RatingCourseDAO.getRatingsByCourseId(courseId);
            List<RatingTeacherEntity> teacherRatings = RatingTeacherDAO.getRatingsByTeacherId(teacherId);
            int registerCourseCount = RegisterCourseDAO.countByCourseId(courseId);
            Timestamp courseTimeCreated = CourseDAO.getCourseById(courseId).getTimeCreated();
            int teacherRegisterCourseCount = RegisterCourseDAO.countByTeacherId(teacherId);
            TeacherEntity teacherEntity = TeacherDAO.getTeacherById(teacherId);
            Timestamp teacherTimeRegistered = teacherEntity.getTimeRegistered();
            int numberOfCourses = CourseDAO.getByTeacher(teacherEntity.getUsername()).size();

            double normalizedRating =
                    CalculatingService.averageCourseRating(courseRatings) * Const.COURSE_RATING_FACTOR
                            + CalculatingService.averageTeacherRating(teacherRatings) * Const.TEACHER_RATING_FACTOR
                            + CalculatingService.courseRegisterByWeekPoint(registerCourseCount, courseTimeCreated)
                            * Const.COURSE_REGISTER_BY_WEEK_FACTOR
                            + CalculatingService.teacherAverageCourseRegisterByWeekPoint
                            (teacherRegisterCourseCount, teacherTimeRegistered, numberOfCourses)
                            * Const.TEACHER_REGISTER_BY_WEEK_FACTOR;
            result.add(new Pair(courseId, normalizedRating));
        }

        // sort and reverse the list
        result.sort(Comparator.comparingDouble(pair -> Double.valueOf(pair.getValue().toString())));
        Collections.reverse(result);
        return result.size() >= 5 ? result.subList(0, 5) : result;
    }

    public static List getNewUserRecommendations(String language, EntityManager em) {
        List<CourseEntity> courseEntityList = CourseDAO.getByLanguage(language, em);
        List<Pair> result = new ArrayList<>();
        for (CourseEntity course : courseEntityList
                ) {
            int courseId = course.getId();
            int teacherId = course.getTeacherId();

            List<RatingCourseEntity> courseRatings = RatingCourseDAO.getRatingsByCourseId(courseId, em);
            List<RatingTeacherEntity> teacherRatings = RatingTeacherDAO.getRatingsByTeacherId(teacherId, em);
            int registerCourseCount = RegisterCourseDAO.countByCourseId(courseId, em);
            Timestamp courseTimeCreated = CourseDAO.getCourseById(courseId, em).getTimeCreated();
            int teacherRegisterCourseCount = RegisterCourseDAO.countByTeacherId(teacherId, em);
            TeacherEntity teacherEntity = TeacherDAO.getTeacherById(teacherId, em);
            Timestamp teacherTimeRegistered = teacherEntity.getTimeRegistered();
            int numberOfCourses = CourseDAO.getByTeacher(teacherEntity.getUsername(), em).size();

            double normalizedRating =
                    CalculatingService.averageCourseRating(courseRatings) * Const.COURSE_RATING_FACTOR
                            + CalculatingService.averageTeacherRating(teacherRatings) * Const.TEACHER_RATING_FACTOR
                            + CalculatingService.courseRegisterByWeekPoint(registerCourseCount, courseTimeCreated)
                            * Const.COURSE_REGISTER_BY_WEEK_FACTOR
                            + CalculatingService.teacherAverageCourseRegisterByWeekPoint
                            (teacherRegisterCourseCount, teacherTimeRegistered, numberOfCourses)
                            * Const.TEACHER_REGISTER_BY_WEEK_FACTOR;
            result.add(new Pair(courseId, normalizedRating));
        }

        // sort and reverse the list
        result.sort(Comparator.comparingDouble(pair -> Double.valueOf(pair.getValue().toString())));
        Collections.reverse(result);
        return result.size() >= 5 ? result.subList(0, 5) : result;
    }

    public static List getSimilarCourse(int courseId, CacheApi cache){
        Map<String, Map<String, Double>> itemBaseDataset = cache.get(Const.SIMILAR_ITEMS_DICTIONARY_CACHE_KEY);
        Map<String, Double> similarItemMap = itemBaseDataset.get(String.valueOf(courseId));
        List<Pair<String, Double>> listSimilarCourseId = new ArrayList<>();
        for (String itemId : similarItemMap.keySet()) {
            listSimilarCourseId.add(new Pair<>(itemId, similarItemMap.get(itemId)));
        }
        Collections.sort(listSimilarCourseId, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        return listSimilarCourseId;
    }

    public static List getRecommendation(String object, CacheApi cache) {
        Map<String, Map<String, Double>> userBasedDataSet = cache.get(Const.USER_BASED_DATA_SET_CACHE_KEY);
        if (userBasedDataSet == null) {
            userBasedDataSet = initDataSet();
            cache.set(Const.USER_BASED_DATA_SET_CACHE_KEY, userBasedDataSet);
        }

        Map<String, Double> objectRatings = userBasedDataSet.get(object);
        if (objectRatings != null) {
            if (objectRatings.size() >= 5) {
                return userBasedRecommendations(userBasedDataSet, object);
            }
        }

        List<Pair> newUserRecommend = new ArrayList<>();
        Map<String, Double> onCacheNewUserRecommend =
                cache.get(Const.NEW_USER_RECOMMEND_CACHE_KEY + Const.DEFAULT_LANGUAGE.toUpperCase());
        if (onCacheNewUserRecommend == null) {
            newUserRecommend = getNewUserRecommendations(Const.DEFAULT_LANGUAGE);

            onCacheNewUserRecommend = new HashMap<>();
            for (Pair pair : newUserRecommend) {
                onCacheNewUserRecommend.put(pair.getKey().toString(), Double.valueOf(pair.getValue().toString()));
            }
            cache.set(Const.NEW_USER_RECOMMEND_CACHE_KEY + Const.DEFAULT_LANGUAGE.toUpperCase(), onCacheNewUserRecommend);
        } else {
            for (Map.Entry<String, Double> entry : onCacheNewUserRecommend.entrySet()) {
                newUserRecommend.add(new Pair(entry.getKey(), entry.getValue()));
            }
        }
        return newUserRecommend;
    }


}
