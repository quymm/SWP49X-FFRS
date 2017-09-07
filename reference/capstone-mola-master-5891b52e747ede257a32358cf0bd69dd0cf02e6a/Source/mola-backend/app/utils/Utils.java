package utils;

import org.apache.commons.codec.binary.Base64;
import play.mvc.Http;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;


public class Utils {
    public static String getSessionKey(){
        return Http.Context.current().session().get(Const.SESSION_KEY);
    }
    public static void setSessionKey(String key){

        Http.Context.current().session().put(Const.SESSION_KEY, key);
    }
    public static String genRandomPassword(){
//        Random random = new Random();
//        byte[] r = new byte[32]; //Means 2048 bit
//        random.nextBytes(r);
//        String s = Base64.encodeBase64String(r);
//        return s;
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}
