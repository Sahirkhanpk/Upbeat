package utils;


import android.util.Patterns;

public class TextUtils {

    //Check empty edit text
    public static boolean isEmpty(String strText) {
        return strText.length() == 0;
    }

    //check Valid Mail address
    public final static boolean isValidEmail(String strText) {
        return (!TextUtils.isEmpty(strText) && Patterns.EMAIL_ADDRESS.matcher(strText).matches());
    }

}
