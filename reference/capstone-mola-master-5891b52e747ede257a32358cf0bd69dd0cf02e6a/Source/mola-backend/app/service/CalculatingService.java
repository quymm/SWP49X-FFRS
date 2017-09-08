package service;

import models.RatingCourseEntity;
import models.RatingTeacherEntity;
import utils.Const;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rocks on 6/12/2017.
 */
public class CalculatingService {
    private final static long TIME_UNIT_FROM_MILLISECOND = 1000 * 3600 * 24 * 7;
    private final static long HOUR_FROM_MILISECOND = 1000 * 3600;

    // Euclidean Distance Score between object1 and object2
    public static double similarDistance(Map<String, Map<String, Double>> dataSet, String object1, String object2) {
        // List similar to be recommending objects between 2 objects
        List<String> similar = new ArrayList<>();

        // Check data to get the similar list
        for (Map.Entry<String, Double> entry : dataSet.get(object1).entrySet()
                ) {
            String key = entry.getKey();
            if (dataSet.get(object2).containsKey(key)) {
                similar.add(key);
            }
        }

        // No similar, return 0
        if (similar.isEmpty()) {
            return 0;
        }

        // Euclidean Distance Formula
        double sumOfSquares = 0;
        for (int i = 0; i < similar.size(); i++) {
            String key = similar.get(i);
            sumOfSquares += Math.pow(dataSet.get(object1).get(key) - dataSet.get(object2).get(key), 2);
        }

        return 1 / (1 + sumOfSquares);
    }

    // Pearson Correlation Score between object1 and object2
    public static double similarPearson(Map<String, Map<String, Double>> dataSet, String object1, String object2) {
        // List similar to be recommending objects between 2 objects
        List<String> similar = new ArrayList<>();

        // Check data to get the similar list
        for (Map.Entry<String, Double> entry : dataSet.get(object1).entrySet()
                ) {
            String key = entry.getKey();
            if (dataSet.get(object2).containsKey(key)) {
                similar.add(key);
            }
        }

        // number of elements
        int n = similar.size();

        // No similar, return 0
        if (n == 0) {
            return 0;
        }

        // calculate Pearson formula values
        double sum1 = 0, sum2 = 0;
        double sumSquare1 = 0, sumSquare2 = 0;
        double sumProduct = 0;
        for (int i = 0; i < n; i++) {
            String key = similar.get(i);
            double value1 = dataSet.get(object1).get(key);
            double value2 = dataSet.get(object2).get(key);

            sum1 += value1;
            sum2 += value2;

            sumSquare1 += Math.pow(value1, 2);
            sumSquare2 += Math.pow(value2, 2);

            sumProduct += value1 * value2;
        }

        // calculate Pearson value
        double num = sumProduct - sum1 * sum2 / n;
        double den = Math.sqrt((sumSquare1 - Math.pow(sum1, 2) / n) * (sumSquare2 - Math.pow(sum2, 2) / n));
        if (den == 0) {
            return 0;
        }
        return num / den;
    }

    public static double averageCourseRating(List<RatingCourseEntity> courseRatings) {
        double averageRating = 0;
        if (courseRatings.size() >= 1) {
            for (RatingCourseEntity ratingCourse : courseRatings
                    ) {
                averageRating += ratingCourse.getRating();
            }
            averageRating /= courseRatings.size();
        }
        return averageRating;
    }

    public static double averageTeacherRating(List<RatingTeacherEntity> teacherRatings) {
        double averageRating = 0;
        if (teacherRatings.size() >= 1) {
            for (RatingTeacherEntity ratingTeacher : teacherRatings
                    ) {
                averageRating += ratingTeacher.getRating();
            }
            averageRating /= teacherRatings.size();
        }
        return averageRating;
    }

    public static double courseRegisterByWeekPoint(int registerCourseCount, Timestamp courseTimeCreated) {
        double courseExisted =
                new Timestamp(System.currentTimeMillis()).getTime() - courseTimeCreated.getTime();
        double courseExistedWeeks = courseExisted / TIME_UNIT_FROM_MILLISECOND;

        double result = registerCourseCount / (courseExistedWeeks * Const.COURSE_REGISTER_EACH_STAR);
        if (result > 5) {
            result = 5;
        }

        return result;
    }

    public static double teacherAverageCourseRegisterByWeekPoint
            (int teacherRegisterCourseCount, Timestamp teacherTimeRegistered, int numberOfCourses) {
        double teacherExisted =
                new Timestamp(System.currentTimeMillis()).getTime() - teacherTimeRegistered.getTime();
        double teacherExistedWeeks =teacherExisted / TIME_UNIT_FROM_MILLISECOND;

        double result = teacherRegisterCourseCount
                / (numberOfCourses * teacherExistedWeeks * Const.TEACHER_AVERAGE_COURSE_REGISTER_EACH_STAR);
        if (result > 5) {
            result = 5;
        }

        return result;
    }

    public static double percentageFinishedLessonPoint(int size, int completed) {
        return (double) completed / size / Const.PERCENTAGE_FINISH_LESSON_EACH_STAR;
    }

    public static double pricePoint(double price) {
        return (Const.MAX_PRICE - price) / (Const.MAX_PRICE / 5);
    }

    public static double scoreDivideByHour(double score, Timestamp courseTimeCreated) {
        double courseExisted =
                new Timestamp(System.currentTimeMillis()).getTime() - courseTimeCreated.getTime();
        double courseExistedHours = courseExisted / HOUR_FROM_MILISECOND;

        return score / courseExistedHours;
    }
}
