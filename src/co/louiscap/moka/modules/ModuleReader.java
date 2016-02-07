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
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * A reader for module objects and properties. Can get lists of the various
 * components, as well as module wide properties
 * @author Louis Capitanchik
 */
public class ModuleReader {
    public static final String LEX_DIR_IDENT = "lexer";
    public static final String PARSE_DIR_IDENT = "parser";
    public static final String INTERP_DIR_IDENT = "interpret";
    
    public static final String MODULE_FILE_NAME = "module.yml";
    
    /**
     * The Internal name used to identify the module until the name has been
     * loaded from the module.yml file
     */
    protected String internalName;
    /**
     * The File object that represents the module directory
     */
    protected File moduleDir;
    
    /**
     * The File object that represents the module.yml configuration file
     */
    protected File pathToConfig;
    
    /**
     * Create a new module from the specified directory. Should be a folder that 
     * contains a 'module.yml' file, plus three additional folders 
     * called 'lexer', 'parser' and 'interpret'
     * @param pathToModule The system path to module folder
     * @throws InvalidModuleException Thrown if the module folder does not exist
     * or its configuration is found to be invalid
     */
    public ModuleReader (String pathToModule) throws InvalidModuleException {
        this(new File(pathToModule));
    }
    
    /**
     * Create a new module from the specified directory. Should be a folder that 
     * contains a 'module.yaml' file, plus three additional folders 
     * called 'lexer', 'parser' and 'interpret'
     * @param moduleDir A File object that represents the module directory
     * @throws InvalidModuleException Thrown if the module folder does not exist
     * or its configuration is found to be invalid
     */
    public ModuleReader (File moduleDir) throws InvalidModuleException {
        internalName = moduleDir.getName();
        if(!moduleDir.exists()) {
            throw new InvalidModuleException(internalName, "Module directory"
                    + "does not exist");
        }
        if(!moduleDir.isDirectory()) {
            throw new InvalidModuleException(internalName, "Provided module path"
                    + "is not a supported directory");
        }
        String[] moduleFiles = moduleDir.list(new NameFileFilter(MODULE_FILE_NAME));
        for(String s : moduleFiles) {
            File f = new File(moduleDir, s);
            if(f.isFile() && f.getName().equals(MODULE_FILE_NAME)) {
                pathToConfig = f;
                break;
            }
        }
        if(pathToConfig == null) {
            throw new InvalidModuleException(internalName, "Directory contains no"
                    + MODULE_FILE_NAME + " file");
        }
        
        if(!hasRequiredFolders(moduleDir)) {
            throw new InvalidModuleException(internalName, "Missing required"
                    + "directories");
        }
        
        this.moduleDir = moduleDir;        
    }
    
    /**
     * Returns a list of strings that represent lexical files that are defined
     * within the 'lexer' module folder
     * @return A fixed length array of strings, each representing a path to a
     * .lex file within the 'lexer' module folder
     */
    public String[] getPathsToLexFiles() {
        return Arrays.stream((new File(moduleDir, LEX_DIR_IDENT))
                                    .list(new WildcardFileFilter("*.lex")))
                    .map(s -> (new File(moduleDir, LEX_DIR_IDENT + "/" + s)).toString())
                    .toArray(String[]::new);
    }
    
    /**
     * Returns a list of File objects that point to lexical files that are 
     * defined within the 'lexer' module folder
     * @return A fixed length array of Files, each representing a .lex file 
     * within the 'lexer' module folder that exists as of the invocation of this
     * method
     */
    public File[] getPathsToLexFilesAsFile() {
        return Arrays.stream(getPathsToLexFiles())
                .map(s -> new File(s)).toArray(File[]::new);
    }
    
    /**
     * Determine if the given directory contains folders called "lexer", "parser"
     * and "interpret"
     * @param root The root folder that should contain the other folders
     * @return Whether or not the aforementioned folders exist within the root
     * folder
     */
    private boolean hasRequiredFolders (File root) {
        boolean has = true;
        
        File cur = new File(root, LEX_DIR_IDENT);
        has = has && cur.exists() && cur.isDirectory();
        
        cur = new File(root, LEX_DIR_IDENT);
        has = has && cur.exists() && cur.isDirectory();
        
        cur = new File(root, LEX_DIR_IDENT);
        has = has && cur.exists() && cur.isDirectory();
        
        return has;
    }

    public File getModuleDir() {
        return moduleDir;
    }

    public File getPathToConfig() {
        return pathToConfig;
    }
}
