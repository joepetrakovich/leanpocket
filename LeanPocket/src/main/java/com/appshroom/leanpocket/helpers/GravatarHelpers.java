package com.appshroom.leanpocket.helpers;

import android.content.Context;

import com.appshroom.leanpocket.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GravatarHelpers {

    public static String hex(byte[] array) {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < array.length; ++i) {

            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1, 3));
        }

        return sb.toString();
    }

    public static String md5Hex(String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex(md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    public static String getGravatarLink(String email, Context context) {

        String hash = md5Hex(email.toLowerCase());

        return buildGravatarUrl(hash, context.getResources().getInteger(R.integer.gravatar_grid_size));

    }

    public static String buildGravatarUrl(String gravatarHash, int size) {

        return Consts.GRAVATAR_URL_PREFIX + gravatarHash + Consts.GRAVATAR_URL_SIZE_PREFIX +
                size + "&" + Consts.GRAVATAR_URL_SUFFIX_WITH_MYSTERY_MAN_DEFAULT;

    }

    public static String buildGravatarUrlAfterRemovingOldSize(String gravatarHash, int size) {

        String gravatarLinkWithoutSize = gravatarHash.split("\\?")[0];

        return buildGravatarUrl(gravatarLinkWithoutSize, size);

    }

}

