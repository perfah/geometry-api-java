package com.esri.core.geometry;

import junit.framework.TestCase;
import org.junit.Test;

public class TestBranchCoveragePrintHook extends TestCase {
    @Test
    public void testPrintHook() {
        System.out.println("Enabling branch coverage print hook!");
        BranchCoverage.printOnExit();
        assertTrue(true);
    }
}
