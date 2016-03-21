package com.jaysyko.wrestlechat.utils;

/**
 * ImageTools.java
 * Various methods for handling images
 *
 * @author Jay Syko
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ImageTools {
    private static final String TAG = ImageTools.class.getSimpleName();
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to internetCheck dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

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
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex.append(bigInt.abs().toString(16));
        } catch (Exception e) {
            Log.d("Profile Image", "Default profile image generator error");
        }
        return "http://www.gravatar.com/avatar/".concat(hex.toString()).concat("?d=identicon");
    }

    public static Boolean isLinkToImage(String url) {
        String patternToMatch = "\\.jpg|\\.png*";
        Pattern p = Pattern.compile(patternToMatch);
        Matcher m = p.matcher(url);
        return m.find();
    }

}
