package com.jaysyko.wrestlechat.network.requestData;

/**
 * @author Jay Syko on 2016-07-13.
 */
public class UpdateUserImageData {

    private int user_id;
    private String profile_image;

    public UpdateUserImageData(int user_id, String profile_image){
        this.user_id = user_id;
        this.profile_image = profile_image;
    }

    public int getUserId() {
        return user_id;
    }

    public String getProfileImage() {
        return profile_image;
    }
}
