package utils;


public class Const {
    public static final String ROLE_STAFF = "staff";
    public static final String SESSION_KEY = "seskey";
    public static final String ROLE_LEARNER = "learner";
    public static final String ROLE_TEACHER = "teacher";
    public static final String CLIP_URL = "http://112.78.4.97/fpt/mola/introclips/";
    public static final String IMG_URL = "http://112.78.4.97/fpt/mola/images/";
    public static final String HEADER_AUTHENTICATION = "Authorization";
    //    Secret Key
    public static final String sharedSecret = "A71A2E1D7C317B801D05C321CEF52754096963DF55B4DB2BAA607CA07D8804DB";
    //    Issuer
    public static final String issuer = "fpt.mola";

    public static final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/v2.9/me";
    public static final String FACEBOOK_PERMISSION = "id,name,first_name,last_name,birthday,gender,picture.type(large){url}";

    public static final int MINS_BUFFERED_EACH_TIMESLOT = 15;

    public static final double COURSE_RATING_FACTOR = 0.5;
    public static final double TEACHER_RATING_FACTOR = 0.3;
    public static final double COURSE_REGISTER_BY_WEEK_FACTOR = 0.2;
    public static final double TEACHER_REGISTER_BY_WEEK_FACTOR = 0.1;
    public static final double PERCENTAGE_FINISH_LESSON_FACTOR = 0.4;
    public static final double PRICE_FACTOR = 0.1;
    public static final int COURSE_REGISTER_EACH_STAR = 5;
    public static final int TEACHER_AVERAGE_COURSE_REGISTER_EACH_STAR = 5;
    public static final double PERCENTAGE_FINISH_LESSON_EACH_STAR = 0.2;
    public static final double MAX_PRICE = 20;

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN = "dd-MM-yyyy";

    public static final String[] LANGUAGE_LIST = {
            "Arabic",
            "English",
            "French",
            "German",
            "Italian",
            "Japanese",
            "Portuguese",
            "Russian",
            "Spanish",
            "Vietnamese",
            "Chinese"
    };
    public static final String[] COVER_PICTURE_LIST = {
            "http://112.78.4.97/fpt/mola/images/cover_1.jpg",
            "http://112.78.4.97/fpt/mola/images/cover_2.jpg",
            "http://112.78.4.97/fpt/mola/images/cover_3.jpg",
            "http://112.78.4.97/fpt/mola/images/cover_4.jpg",
            "http://112.78.4.97/fpt/mola/images/cover_5.jpg",
            "http://112.78.4.97/fpt/mola/images/cover_6.jpg",
            "http://112.78.4.97/fpt/mola/images/cover_7.jpg",
            "http://112.78.4.97/fpt/mola/images/cover_8.jpg",
            "http://112.78.4.97/fpt/mola/images/cover_9.jpg",
            "http://112.78.4.97/fpt/mola/images/cover_10.jpg",
    };

    public static final String USER_BASED_DATA_SET_CACHE_KEY = "USER_BASED_DATA_SET";
    public static final String ITEM_BASED_DATA_SET_CACHE_KEY = "ITEM_BASED_DATA_SET";
    public static final String SIMILAR_ITEMS_DICTIONARY_CACHE_KEY = "SIMILAR_ITEMS_DICTIONARY";
    public static final String NEW_USER_RECOMMEND_CACHE_KEY = "NEW_USER_RECOMMEND_";
    public static final String DEFAULT_LANGUAGE = "English";
    public static final int DICTIONARY_ENTRY_AMOUNT = 15;
    public static final long MIN_TIME_SESSION_MINUTE = 30;

    public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    public static final String FCM_SERVER_KEY = "AAAAx0fyGVU:APA91bHnWeEjdcfovNGPgjuCyfdQtlNbLlnQ-p3CsvxFy0aKg9dO6QGeuFAwh9bc2qYUa7M78iFffBkAjhW-TZtKEJNKJdAmJZnCjQrUQAJoQIx3elMGN5iFCLB1Em8fUibLAsbBYkAy";

    public static final long TOKEN_EXPIRATION_DAYS = 30;
}
