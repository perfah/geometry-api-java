package com.esri.core.geometry;

import junit.framework.TestCase;
import org.junit.Test;

import javax.xml.crypto.dsig.Transform;

public class TestTransformation2D extends TestCase {
    @Test
    public void testEquals(){
        Transformation2D t1 = new Transformation2D(1.0);
        Transformation2D t2 = new Transformation2D(1.0);
        Transformation2D t3 = new Transformation2D(0.5);
        Integer otherObj = 5;

        // Same object:
        assertTrue(t1.equals(t1));

        // Identical transformations:
        assertTrue(t1.equals(t2));

        // Different transformations:
        assertFalse(t1.equals(t3));

        // Unknown object type:
        assertFalse(t1.equals(otherObj));
    }
}


