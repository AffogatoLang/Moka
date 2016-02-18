/*
Copyright (c) 2015, Louis Capitanchik
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this
  list of conditions and the following disclaimer.

* Redistributions in binary form must reproduce the above copyright notice,
  this list of conditions and the following disclaimer in the documentation
  and/or other materials provided with the distribution.

* Neither the name of Affogato nor the names of its associated properties or
  contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package co.louiscap.moka.utils.number;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author commander-lol
 */
public class IntUtilsTest {
    
    public IntUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Test of getClosestLowerBound method, of class IntUtils.
     */
    @Test
    public void testGetClosestLowerBound_normal() {
        System.out.println("getClosestLowerBound_normal");
        int target = 72;
        int[] bounds = {12, 26, 58, 124, 144};
        int expResult = 58;
        int result = IntUtils.getClosestLowerBound(target, bounds);
        assertEquals(expResult, result);
    }

    /**
     * Test of getClosestLowerBound method, of class IntUtils.
     */
    @Test
    public void testGetClosestLowerBound_lower() {
        System.out.println("getClosestLowerBound_lower");
        int target = 2;
        int[] bounds = {12, 26, 58, 124, 144};
        int expResult = Integer.MIN_VALUE;
        int result = IntUtils.getClosestLowerBound(target, bounds);
        assertEquals(expResult, result);
    }

    /**
     * Test of getClosestLowerBound method, of class IntUtils.
     */
    @Test
    public void testGetClosestLowerBound_higher() {
        System.out.println("getClosestLowerBound_higher");
        int target = 199;
        int[] bounds = {12, 26, 58, 124, 144};
        int expResult = 144;
        int result = IntUtils.getClosestLowerBound(target, bounds);
        assertEquals(expResult, result);
    }

    /**
     * Test of getClosestLowerBound method, of class IntUtils.
     */
    @Test
    public void testGetClosestLowerBound_equal() {
        System.out.println("getClosestLowerBound_normal");
        int target = 124;
        int[] bounds = {12, 26, 58, 124, 144};
        int expResult = 124;
        int result = IntUtils.getClosestLowerBound(target, bounds);
        assertEquals(expResult, result);
    }

    /**
     * Test of getClosestLowerBoundIndex method, in a typical use case where
     * the target value is not a number in the bounds array, but is contained 
     * within the implicit range of the array's values.
     */
    @Test
    public void testGetClosestLowerBoundIndex_normal() {
        System.out.println("getClosestLowerBoundIndex_normal");
        int target = 72;
        int[] bounds = {13, 27, 58, 124, 144};
        int expResult = 2;
        int result = IntUtils.getClosestLowerBoundIndex(target, bounds);
        assertEquals(expResult, result);
    }

    /**
     * Test of getClosestLowerBoundIndex method, for the case where the target
     * is larger than the largest number in the bounds array
     */
    @Test
    public void testGetClosestLowerBoundIndex_higher() {
        System.out.println("getClosestLowerBoundIndex_higher");
        int target = 189;
        int[] bounds = {13, 27, 58, 124, 144};
        int expResult = 4;
        int result = IntUtils.getClosestLowerBoundIndex(target, bounds);
        assertEquals(expResult, result);
    }

    /**
     * Test of getClosestLowerBoundIndex method, for the case where the target
     * is smaller than the smallest number in the bounds array
     */
    @Test
    public void testGetClosestLowerBoundIndex_lower() {
        System.out.println("getClosestLowerBoundIndex_lower");
        int target = 2;
        int[] bounds = {13, 27, 58, 124, 144};
        int expResult = -1;
        int result = IntUtils.getClosestLowerBoundIndex(target, bounds);
        assertEquals(expResult, result);
    }

    /**
     * Test of getClosestLowerBoundIndex method, for the case where the target
     * is an element in the array
     */
    @Test
    public void testGetClosestLowerBoundIndex_equal() {
        System.out.println("getClosestLowerBoundIndex_equal");
        int target = 124;
        int[] bounds = {13, 27, 58, 124, 144};
        int expResult = 3;
        int result = IntUtils.getClosestLowerBoundIndex(target, bounds);
        assertEquals(expResult, result);
    }
    
}
