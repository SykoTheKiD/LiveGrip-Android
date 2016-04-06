package com.jaysyko.wrestlechat.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;


/**
 *
 */
public class ImageToolsTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void isLinkToImageNullTest() {
        Boolean linkNull = ImageTools.isLinkToImage(null);
        Boolean linkEmpty = ImageTools.isLinkToImage("");
        assertEquals(false, linkNull);
        assertEquals(false, linkEmpty);
    }

    @Test
    public void isLinkToImageTestValid() {
        Boolean linkJPG = ImageTools.isLinkToImage("http://thisiswalder.com/wp-content/uploads/2015/05/the-weeknd.png");
        Boolean linkPNG = ImageTools.isLinkToImage("http://static.celebuzz.com/uploads/2014/01/27/465263589.jpg");
        assertEquals(true, linkJPG);
        assertEquals(true, linkPNG);
    }

    @Test
    public void defaultImageTestError() {
        exception.expect(Exception.class);
        exception.expectMessage("");
        String defaultImageNull = ImageTools.defaultProfileImage(null);
        assertEquals(null, defaultImageNull);
    }

    @Test
    public void defaultImageTest() {
        String defaultImage = ImageTools.defaultProfileImage("jaysyko");
        assertEquals("http://www.gravatar.com/avatar/7091e1e6c8c03fe8690cbdefae736ec9?d=identicon", defaultImage);
    }

}
