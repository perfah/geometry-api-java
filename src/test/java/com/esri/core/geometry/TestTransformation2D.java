package com.esri.core.geometry;

import junit.framework.TestCase;
import org.junit.Test;

import javax.xml.crypto.dsig.Transform;

public class TestTransformation extends TestCase {
    @Test
    public void testEquals(){
        Transformation2D t1 = new Transformation2D();
        Transformation2D t2 = new Transformation2D();

        assertTrue(t1.equals(t2));
    }
}
