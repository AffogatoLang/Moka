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
package co.louiscap.moka.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author commander-lol
 */
public class MultiplexedStringPrinterTest {
    
    private static ByteArrayOutputStream fakeOut;
    private static ByteArrayOutputStream fakeErr;
    private static ByteArrayOutputStream debug;
    
    public MultiplexedStringPrinterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
//
//    /**
//     * Test of addChannel method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testAddChannel() {
//        System.out.println("addChannel");
//        String tag = "";
//        PrintStream channel = null;
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        instance.addChannel(tag, channel);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of removeChannels method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testRemoveChannels() {
//        System.out.println("removeChannels");
//        String[] tags = null;
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        instance.removeChannels(tags);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getChannel method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testGetChannel() {
//        System.out.println("getChannel");
//        String tag = "";
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        PrintStream expResult = null;
//        PrintStream result = instance.getChannel(tag);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getChannels method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testGetChannels() {
//        System.out.println("getChannels");
//        String[] tags = null;
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        PrintStream[] expResult = null;
//        PrintStream[] result = instance.getChannels(tags);
//        assertArrayEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of println method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testPrintln_String() {
//        System.out.println("println");
//        String s = "";
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        instance.println(s);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of println method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testPrintln_String_StringArr() {
//        System.out.println("println");
//        String s = "";
//        String[] tags = null;
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        instance.println(s, tags);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of print method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testPrint_String() {
//        System.out.println("print");
//        String s = "";
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        instance.print(s);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of print method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testPrint_String_StringArr() {
//        System.out.println("print");
//        String s = "";
//        String[] tags = null;
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        instance.print(s, tags);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of close method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testClose_0args() {
//        System.out.println("close");
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        instance.close();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of close method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testClose_StringArr() {
//        System.out.println("close");
//        String[] tags = null;
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        instance.close(tags);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of flush method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testFlush_0args() {
//        System.out.println("flush");
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        instance.flush();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of flush method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testFlush_StringArr() {
//        System.out.println("flush");
//        String[] tags = null;
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        instance.flush(tags);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getBatch method, of class MultiplexedStringPrinter.
//     */
//    @Test
//    public void testGetBatch() {
//        System.out.println("getBatch");
//        String[] tags = null;
//        MultiplexedStringPrinter instance = new MultiplexedStringPrinter();
//        MultiplexedStringPrinter expResult = null;
//        MultiplexedStringPrinter result = instance.getBatch(tags);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//    
}
