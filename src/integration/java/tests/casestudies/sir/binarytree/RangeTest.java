package tests.casestudies.sir.binarytree;
import junit.framework.TestCase;
/*
 * RangeTest
 *
 *  - Feb 1, 2006
 *
 * Copyright (c) 2003 Kansas State University, Laboratory for the Specification,
 * Analysis, and Transformation of Software
 *
 * This software is licensed under the SAnToS Laboratory Open Academic License.  You
 * should have received a copy of the license with the distribution.  A copy can be
 * found at:
 * http://www.cis.ksu.edu/santos/license.shtml
 * or you can contact the lab at:
 * SAnToS Laboratory
 * 234 Nichols Hall
 * Manhattan, KS 66506, USA
 */

public class RangeTest extends TestCase {
    

    /*
     * Test method for 'Range.inRange(int)'
     */
    public void testInRange() {
        assert range.inRange(5);

    }

    /*
     * Test method for 'Range.setLower(int)'
     */
    public void testSetLower() {
        range.setLower(5).setUpper(10).inRange(6);
    }

    
    Range range;
    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        range = new Range();
    }

}
