package com.jaysyko.wrestlechat.utils;

/**
 * ImageTools.java
 * Various methods for handling images
 *
 * @author Jay Syko
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import com.jaysyko.wrestlechat.application.eLog;
import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ImageTools {
    private static final String TAG = ImageTools.class.getSimpleName();
    private static final String GRAVATAR_LINK = "http://www.gravatar.com/avatar/";
    private static final String GRAVATAR_IMAGE_TYPE = "?d=identicon";
    private static final int RADIX = 16;
    private static final String ALGORITHM = "MD5";
    public static final int LIGHT_GREY = Color.parseColor("#a6a6a6");
    public static final int BLACK = Color.parseColor("#424242");
    public static final int DARK_GREY = BLACK;
    public static final int WHITE = Color.parseColor("#FAFAFA");

    /**
     * Loads an image from a URL into an ImageView
     *
     * @param context   Context
     * @param link      String
     * @param imageView ImageView
     */
    public static void loadImage(Context context, String link, ImageView imageView) {
        Picasso.with(context).load(link).fit().into(imageView);
    }

    /**
     * Generates a unique Image based off of an input string
     *
     * @param userId String
     * @return defaultProfileImageURL
     */
    public static String defaultProfileImage(String userId) {
        StringBuilder hex = new StringBuilder();
        try {
            final MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex.append(bigInt.abs().toString(RADIX));
        } catch (Exception e) {
            eLog.e(TAG, e.getMessage());
        }
        return GRAVATAR_LINK.concat(hex.toString()).concat(GRAVATAR_IMAGE_TYPE);
    }

    /**
     * Checks if a link is a link to a .jpg or a .png
     *
     * @param url String
     * @return true if is link to a .jpg or .png
     */
    public static Boolean isLinkToImage(String url) {
        if (url != null) {
            String patternToMatch = "\\.jpg|\\.png*";
            Pattern pattern = Pattern.compile(patternToMatch);
            Matcher matches = pattern.matcher(url);
            return matches.find();
        }
        return false;
    }

    /**
     *
     * @param bitmap Image
     * @return Color
     */
    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        if(color >= LIGHT_GREY){
            return DARK_GREY;
        }
        return color;
    }

    public static int getTextColour(String background){
        switch (background){
            case("0"):
                return BLACK;
            case("1"):
                return WHITE;
            case("2"):
                return BLACK;
            case("3"):
                return WHITE;
            default:
                return WHITE;
        }
    }

}
