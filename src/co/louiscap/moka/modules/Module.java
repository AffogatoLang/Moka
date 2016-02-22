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

import co.louiscap.moka.exceptions.InvalidFormatException;
import co.louiscap.moka.exceptions.InvalidModuleException;
import co.louiscap.moka.lexer.LexFile;
import co.louiscap.moka.parser.LangFile;
import co.louiscap.moka.translator.InterpFile;
import co.louiscap.moka.utils.io.Logging;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 * Represents a Moka module, loaded into memory
 * @author Louis Capitanchik
 */
public class Module {
    public static String[] requiredProps = {"name", "version", "core"};
    
    private final HashMap<String, String> lexSource,
                                    langSource,
                                    interpSource;
    
    private final HashMap<String, LexFile> lexFiles;
    private final HashMap<String, LangFile> langFiles;
    private final HashMap<String, InterpFile> interpFiles;
    
    private final File directory;
    
    private final Map properties;
    
    private String tmpid, id;
    
    public Module(File verifiedSourceDir) throws InvalidModuleException, InvalidFormatException {
        directory = verifiedSourceDir;
        
        tmpid = directory.getName();
        
        lexSource = new LinkedHashMap<>();
        langSource = new LinkedHashMap<>();
        interpSource = new LinkedHashMap<>();
        
        lexFiles = new LinkedHashMap<>();
        langFiles = new LinkedHashMap<>();
        interpFiles = new LinkedHashMap<>();
        
        String propString;
        
        try {
            propString = FileUtils.readFileToString(new File(directory, ModuleReader.MODULE_FILE_NAME), "utf-8");
        } catch (IOException ex) {
            ex.printStackTrace(Logging.LOGGER.getChannel("err"));
            throw new InvalidModuleException(tmpid, "Cannot load " + ModuleReader.MODULE_FILE_NAME, ex);
        }
        
        loadDirectory(ModuleReader.LEX_DIR_IDENT, lexSource);
        loadDirectory(ModuleReader.PARSE_DIR_IDENT, langSource);
        loadDirectory(ModuleReader.INTERP_DIR_IDENT, interpSource);
        
        Yaml yaml = new Yaml();
        properties = yaml.loadAs(propString, Map.class);
        
        validatePropertiesFile(properties);
        id = (String)properties.get("name");
    }
    
    public String getID() {
        return id;
    }
    
    public HashMap<String, String> getAllLexSources() {
        return lexSource;
    }
    
    public HashMap<String, String> getAllLangSources() {
        return langSource;
    } 
    
    public HashMap<String, String> getAllInterpSources() {
        return interpSource;
    }
    
    public HashMap<String, LexFile> getAllLexFiles() {
        if (lexSource.size() != lexFiles.size()) {
            Set<String> lexKeys = lexSource.keySet();
            lexKeys.removeAll(lexFiles.keySet());
            lexKeys.forEach(s -> loadLexFile(s));
        }
        return lexFiles;
    }
    
    public HashMap<String, LangFile> getAllLangFiles() {
        if (langSource.size() != langFiles.size()) {
            Set<String> langKeys = langSource.keySet();
            langKeys.removeAll(langFiles.keySet());
            langKeys.forEach(s -> {
                try {
                    loadLangFile(s);
                } catch (InvalidFormatException ex) {
                    ex.printStackTrace(Logging.LOGGER.getChannel("err"));
                }
            });
        }
        return langFiles;
    }
    
    public HashMap<String, InterpFile> getAllInterpFiles() {
        if (interpSource.size() != interpFiles.size()) {
            Set<String> interpKeys = interpSource.keySet();
            interpKeys.removeAll(interpFiles.keySet());
            interpKeys.forEach(s -> loadInterpFile(s));
        }
        return interpFiles;
    }
    
    public String getLexSourceByName(String name) {
        return lexSource.get(name);
    }
    
    public String getLangSourceByName(String name) {
        return lexSource.get(name);
    }
    
    public String getInterpSourceByName(String name) {
        return lexSource.get(name);
    }
    
    public LexFile getLexFileByName(String name) {
        LexFile file = lexFiles.get(name);
        if(file == null) {
            loadLexFile(name);
            file = lexFiles.get(name);
        }
        return file;
    }
    
    public LangFile getLangFileByName(String name) throws InvalidFormatException {
        LangFile file = langFiles.get(name);
        if(file == null) {
            loadLangFile(name);
            file = langFiles.get(name);
        }
        return file;
    }
    
    public InterpFile getInterpFileByName(String name) {
        InterpFile file = interpFiles.get(name);
        if(file == null) {
            loadInterpFile(name);
            file = interpFiles.get(name);
        }
        return file;
    }
    
    /**
     * When someone requests the lexical file and it is not already in the
     * LexFiles map, it needs to be loaded and placed in the map
     * @param name The name of the lexical file being loaded
     */
    private void loadLexFile (String name) {
        lexFiles.put(name, new LexFile(id, lexSource.get(name)));
    }
     /**
     * When someone requests the language file and it is not already in the
     * LangFiles map, it needs to be loaded and placed in the map
     * @param name The name of the language file being loaded
     */
    private void loadLangFile (String name) throws InvalidFormatException {
        langFiles.put(name, new LangFile(id, langSource.get(name)));
    }
     /**
     * When someone requests the interpreter file and it is not already in the
     * InterpFiles map, it needs to be loaded and placed in the map
     * @param name The name of the interpreter file being loaded
     */
    private void loadInterpFile (String name) {
        interpFiles.put(name, new InterpFile(id, interpSource.get(name)));
    }
    
    /**
     * Grabs the contents of a directory and adds the contents of each file
     * in that directory to the specified storage map, where the key to each
     * entry is the basename of the file being stored
     * @param name The name of the directory to load (relative to the module
     * root)
     * @param storage The map in which the contents of the file should be stored
     */
    private void loadDirectory(String name, HashMap<String, String> storage) {
        File curDir = new File(directory, name);
        String[] fileNames = curDir.list();

        for (String fname : fileNames) {
            File cur = new File(curDir, fname);
            try {
                storage.put(FilenameUtils.getName(cur.getPath()), FileUtils.readFileToString(cur, "utf-8"));
            } catch (IOException ex) {
                Logging.LOGGER.println("Failed to load lexical file " + cur.getAbsolutePath(), "err");
                ex.printStackTrace(Logging.LOGGER.getChannel("err"));
                Logger.getLogger(Module.class.getName()).log(Level.INFO, "Failed to load lexical file {0}", cur.getAbsolutePath());
                Logger.getLogger(Module.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void validatePropertiesFile(Map fileContents) throws InvalidFormatException {
        for(String s : requiredProps) {
            if(!fileContents.containsKey(s)) {
                throw new InvalidFormatException(directory.getAbsolutePath() + 
                        ModuleReader.MODULE_FILE_NAME,
                        ModuleReader.MODULE_FILE_NAME + 
                        "Does not contain the required property " + 
                        s);
            }
        }
        
        
        
        if(!fileContents.containsKey("deps")) {
            fileContents.put("deps", new ArrayList());
        }
    }
}
