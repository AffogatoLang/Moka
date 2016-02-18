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
package co.louiscap.moka.utils.string;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Louis Capitanchik
 */
public class StringChunkerTest {
    
    static final String testString = "I'm a test, with various anchor points! Can you guess what I'm using as an anchor? Punctuation, that's what.";
    
    public StringChunkerTest() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * Move the string chunker ahead and then ensure that resetting it returns
     * its state to that of a newly instantiated StringChunker
     */
    @Test
    public void testPositionReset() {
        System.out.println("Position Reset");
        StringChunker instance = new StringChunker(testString);
        instance.skip(15);
        instance.reset();
        assertTrue(instance.equalState(new StringChunker(testString)));
    }

    /**
     * Move the string chunker ahead and then ensure that it properly resets to
     * the root of a new source as if it had just been instantiated with the
     * second source
     */
    @Test
    public void testReset_String() {
        System.out.println("Reset Source");
        String newSource = "Alternative source right here";
        StringChunker instance = new StringChunker(testString);
        instance.skip(15);
        instance.reset(newSource);
        assertTrue(instance.equalState(new StringChunker(newSource)));
    }

    /**
     * Test of reverse method, of class StringChunker.
     */
    @Test
    public void testReverse() {
        System.out.println("Reverse");
        StringChunker instance = new StringChunker(testString);
        instance.skip(15);
        instance.reverse(5);
        assertEquals(instance.getPosition(), 10);
    }

    /**
     * Test of skip method, of class StringChunker.
     */
    @Test
    public void testSkip() {
        System.out.println("skip");
        int expectedPosition = 15;
        String nextThreeLetters = "h v";
        StringChunker instance = new StringChunker(testString);
        instance.skip(15);
        assertEquals(expectedPosition, instance.getPosition());
//        assertEquals(nextThreeLetters, instance.getNext(3)); 
//      Test overlap?
    }

    /**
     * Test of hasNext method, of class StringChunker.
     */
    @Test
    public void testHasNext() {
        System.out.println("hasNext");
        StringChunker instance = new StringChunker(testString);
        StringBuilder testBuilder = new StringBuilder();
        for(int i = 0; i < testString.length(); i += 1) {
            assertTrue("Not enough chars after iteration " + i + " of " + (testString.length() -1), instance.hasNext());
            instance.getNext(1);
        }
        System.out.println();
        assertFalse("String still has content: " + instance.tail(), instance.hasNext());
        instance.reset();
        while(instance.hasNext()){
            testBuilder.append(instance.getNext(1));
        }
        assertEquals(testBuilder.toString(), testString);
    }

    /**
     * Test of getUntil method with only a String parameter (Exclusive get)
     */
    @Test
    public void testGetUntil_String() {
        System.out.println("getUntil");
        String needle = ",";
        StringChunker instance = new StringChunker(testString);
        String expResult = "I'm a test";
        String result = instance.getUntil(needle);
        assertEquals(expResult, result);
    }
//
//    /**
//     * Test of getUntil method, of class StringChunker.
//     */
//    @Test
//    public void testGetUntil_String_boolean() {
//        System.out.println("getUntil");
//        String needle = "";
//        boolean includeNeedle = false;
//        StringChunker instance = null;
//        String expResult = "";
//        String result = instance.getUntil(needle, includeNeedle);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of peekUntil method, of class StringChunker.
//     */
//    @Test
//    public void testPeekUntil_String() {
//        System.out.println("peekUntil");
//        String needle = "";
//        StringChunker instance = null;
//        String expResult = "";
//        String result = instance.peekUntil(needle);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of peekUntil method, of class StringChunker.
//     */
//    @Test
//    public void testPeekUntil_String_boolean() {
//        System.out.println("peekUntil");
//        String needle = "";
//        boolean includeNeedle = false;
//        StringChunker instance = null;
//        String expResult = "";
//        String result = instance.peekUntil(needle, includeNeedle);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getNext method, of class StringChunker.
//     */
//    @Test
//    public void testGetNext() {
//        System.out.println("getNext");
//        int n = 0;
//        StringChunker instance = null;
//        String expResult = "";
//        String result = instance.getNext(n);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of peekNext method, of class StringChunker.
//     */
//    @Test
//    public void testPeekNext() {
//        System.out.println("peekNext");
//        int n = 0;
//        StringChunker instance = null;
//        String expResult = "";
//        String result = instance.peekNext(n);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of head method, of class StringChunker.
//     */
//    @Test
//    public void testHead() {
//        System.out.println("head");
//        StringChunker instance = null;
//        String expResult = "";
//        String result = instance.head();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of tail method, of class StringChunker.
//     */
//    @Test
//    public void testTail() {
//        System.out.println("tail");
//        StringChunker instance = null;
//        String expResult = "";
//        String result = instance.tail();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    
}
