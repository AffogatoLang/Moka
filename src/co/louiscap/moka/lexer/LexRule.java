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

import co.louiscap.moka.utils.data.Location;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a singular lexical rule associating one regular expression with
 * a language token. The properties of LexRule are Immutable as it encapsulates
 * a line from a definition file, which would need to be reloaded and recreated
 * to show any changes.
 * @author Louis Capitanchik
 */
public class LexRule implements Comparable<LexRule> {

    private final Integer priority;
    private final String outToken;
    private final Pattern regex;
    
    public LexRule (int priority, String token, String pattern) {
        this.priority = priority;
        this.outToken = token;
        
        if(!pattern.startsWith("^")) {
            pattern = "^" + pattern;
        }
        
        this.regex = Pattern.compile(pattern);
    }
    
    @Override
    public int compareTo(LexRule o) {
        return this.priority - o.priority;
    }
    
    public Integer getPriority() {
        return this.priority;
    }
    
    public String getOutToken() {
        return this.outToken;
    }

    public Pattern getRegex() {
        return this.regex;
    }
    
    public Token apply(String src, Location currentLocation) {
        Matcher match = regex.matcher(src);
        if (!match.find()) {
            return null;
        } else {
            String content = "";
            if (match.groupCount() > 0) {
                content = match.group(1);
            }
            return new Token(this.outToken, content, currentLocation);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(priority);
        sb.append(" : ");
        sb.append(outToken);
        sb.append(" : ");
        sb.append(regex.toString());
        
        return sb.toString();
    }
    
    
}
