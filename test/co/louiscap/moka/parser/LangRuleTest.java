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
package co.louiscap.moka.parser;

import co.louiscap.moka.exceptions.InvalidFormatException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Louis Capitanchik
 */
public class LangRuleTest {
    
    public LangRuleTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }

    @Test
    public void testConstruction() throws InvalidFormatException {
        LangRule r = new LangRule("S_Pram : T_dave ? (T_Whitespace (T_ADD) T_Ident)? That Is Just (The Worst)");
        Set<String[]> expResult = new HashSet<>();
        expResult.add(new String[]{"T_dave", "T_Whitespace", "T_ADD", "T_Ident", "That", "Is", "Just", "The", "Worst"});
        expResult.add(new String[]{"T_Whitespace", "T_ADD", "T_Ident", "That", "Is", "Just", "The", "Worst"});
        expResult.add(new String[]{"T_dave", "That", "Is", "Just", "The", "Worst"});
        expResult.add(new String[]{"That", "Is", "Just", "The", "Worst"});
        assertEquals(r.target, "S_Pram");
        Set<String[]> seqs = r.sequences;
        Counter c = new Counter();
        expResult.forEach(rule -> {
            final MutableBoolean setContains = new MutableBoolean(false);
            seqs.forEach(sRule -> setContains.or(Arrays.equals(rule, sRule)));
            System.out.println("\tChecking " + Arrays.toString(rule));
            assertTrue(setContains.state);
            c.increment();
        });
        assertEquals("Incorrect number of rules", c.c, seqs.size());
    }
    private class MutableBoolean {
        public boolean state;
        private MutableBoolean(boolean state) {
            this.state = state;
        }
        public boolean or(boolean other) {
            state |= other;
            return state;
        }
    }
    private class Counter {
        public int c;
        private Counter() {
            c = 0;
        }
        public void increment() {
            c += 1;
        }
    }
}
