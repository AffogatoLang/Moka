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
package co.louiscap.moka.lexer;

import co.louiscap.moka.utils.io.Logging;
import co.louiscap.moka.utils.io.ModuleFile;
import co.louiscap.moka.utils.string.StringChunker;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * A file that holds all of the lexical rules for its given lexical scope
 * @author Louis Capitanchik
 */
public class LexFile implements ModuleFile {

    private final String source, moduleID;
    
    private final LinkedList<LexRule> rules;
    
    public LexFile (String moduleID, String source) {
        this.moduleID = moduleID;
        this.source = source;
        
        Logging.LOGGER.println("Creating language file in " + moduleID, "debug");
        
        String[] lines = source.split("\\n|\\r|\\n\\r|\\r\\n");
        
        this.rules = new LinkedList<>();
        Arrays.stream(lines).forEach(line -> {
            StringChunker sc = new StringChunker(line);
            int priority = Integer.parseInt(sc.getUntil(":").trim());
            sc.skip(1);
            String token = sc.getUntil(":").trim();
            sc.skip(1);
            String regex = sc.tail().trim();
            rules.add(new LexRule(priority, token, regex));
        });
    }
    
    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getModuleID() {
        return moduleID;
    }
    
    public LexRule[] getRules() {
        return rules.stream().toArray(LexRule[]::new);
    }
    
}
