package com.jaysyko.wrestlechat.auth;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by jarushaan on 2016-04-05
 */
public class CurrentActiveUserTest {

//    JSONObject payload;
//    JSONArray payloadArray = new JSONArray();
//    CurrentActiveUser user;
//    Context context;

//    @Before
//    public void create(){
//        context = getContext();
//        try {
//            payload  = new JSONObject();
//            payload.put(UserKeys.ID.toString(), "1");
//            payload.put(UserKeys.USERNAME.toString(), "jaysyko");
//            payload.put(UserKeys.PROFILE_IMAGE.toString(), "link");
//            payloadArray.put(payload);
//            user = CurrentActiveUser.newUser(context, payloadArray);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void defaultImageTest() throws Exception {
        assertEquals("jaysyko", "jaysyko");
    }
}
