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
package co.louiscap.moka.modules;

import co.louiscap.moka.exceptions.InvalidModuleException;
import java.io.File;
import java.util.Arrays;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Louis Capitanchik
 */
public class ModuleReaderTest {
    
    static File TEST_ROOT,
                GOOD_MOD_PATH;
    
    public ModuleReaderTest() {
        
    }
    
    @BeforeClass
    public static void setUpClass() {
        TEST_ROOT = new File("./testFiles");
        GOOD_MOD_PATH = new File(TEST_ROOT, "GoodModule");
    }

    /**
     * Test of getPathsToLexFiles method, of class ModuleReader.
     * @throws InvalidModuleException
     */
    @Test
    public void testGetPathsToLexFiles() throws InvalidModuleException {
        System.out.println("getPathsToLexFilesAsFile");
        ModuleReader instance = new ModuleReader(GOOD_MOD_PATH);
        File LFolder = new File(TEST_ROOT, "GoodModule/lexer");
        String[] expResult = {
            new File(LFolder, "idents.lex").getPath(), 
            new File(LFolder, "strings.lex").getPath()
        };
        String[] result = instance.getPathsToLexFiles();
        
        Arrays.sort(result);
        Arrays.sort(expResult);
        
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getPathsToLexFilesAsFile method, of class ModuleReader.
     * @throws InvalidModuleException
     */
    @Test
    public void testGetPathsToLexFilesAsFile() throws InvalidModuleException {
        System.out.println("getPathsToLexFilesAsFile");
        ModuleReader instance = new ModuleReader(GOOD_MOD_PATH);
        File LFolder = new File(TEST_ROOT, "GoodModule/lexer");
        File[] expResult = {
            new File(LFolder, "idents.lex"),
            new File(LFolder, "strings.lex")
        };
        File[] result = instance.getPathsToLexFilesAsFile();
        
        Arrays.sort(result);
        Arrays.sort(expResult);
        
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getModuleDir method, of class ModuleReader.
     * @throws InvalidModuleException
     */
    @Test
    public void testGetModuleDir() throws InvalidModuleException {
        System.out.println("getModuleDir");
        ModuleReader instance = new ModuleReader(GOOD_MOD_PATH);
        File expResult = new File(TEST_ROOT, "GoodModule");
        File result = instance.getModuleDir();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPathToConfig method, of class ModuleReader.
     * @throws InvalidModuleException
     */
    @Test
    public void testGetPathToConfig() throws InvalidModuleException {
        System.out.println("getPathToConfig");
        ModuleReader instance = new ModuleReader(GOOD_MOD_PATH);
        File expResult = new File(TEST_ROOT, "GoodModule/module.yml");
        File result = instance.getPathToConfig();
        assertEquals(expResult, result);
    }
}