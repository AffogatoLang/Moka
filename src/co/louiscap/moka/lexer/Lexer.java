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

import co.louiscap.moka.exceptions.LanguageSyntaxException;
import co.louiscap.moka.utils.data.Location;
import co.louiscap.moka.utils.io.Logging;
import co.louiscap.moka.utils.number.IntUtils;
import co.louiscap.moka.utils.string.StringChunker;
import co.louiscap.moka.utils.string.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.regex.MatchResult;

/**
 * The Lexer is used to split a given source String into a sequence of tokens
 * using previously created LexRules
 * @author Louis Capitanchik
 */
public class Lexer {
    private LexRule[] rules;
    private boolean stripWhitespace = false;
    
    /**
     * Create a new Lexer with the specified rule set. Rules should be in the
     * order by which they should be tried against the source program, but this
     * ordering is left up to the calling code.
     * @param rules An array of LexRule objects that will be used to create
     * tokens out of a provided source string
     */
    public Lexer(LexRule[] rules) {
        this.rules = rules;
    }
    
    /**
     * Create a new Lexer with the specified rule set. Rules should be in the
     * order by which they should be tried against the source program, but this
     * ordering is left up to the calling code.
     * @param rules Any Collection of LexRule objects that will be used to
     * create tokens out of a provided source string
     */
    public Lexer(Collection<LexRule[]> rules) {
        this(rules.stream().toArray(i -> new LexRule[i]));
    }
    
    /**
     * Takes a given source program and creates a list of tokens that represents
     * that program in terms of the laxical rules that this Lexer was created
     * with. Each call to this version of process will assign the source program
     * a unique identifier 
     * @param src The source program to be tokenised
     * @return A sequence of tokens to be parsed
     * @throws LanguageSyntaxException Thrown if there is an element of the source
     * string that can't be parsed
     */
    public Token[] process(String src) throws LanguageSyntaxException {
        return process(src, UUID.randomUUID() + ".source");
    }
    
    /**
     * Takes a given source program and creates a list of tokens that represents
     * that program in terms of the laxical rules that this Lexer was created
     * with. The given source name will be used for all token locations generated
     * by this call to process.
     * @param src The source program to be tokenised
     * @param name The name of the source program being tokenised
     * @return A sequence of tokens to be parsed
     * @throws LanguageSyntaxException Thrown if there is an element of the source
     * string that can't be parsed
     */
    public Token[] process(String src, String name) throws LanguageSyntaxException {
        if(this.stripWhitespace) {
            src = src.trim();
        }
        final StringChunker sc = new StringChunker(src);
        final ArrayList<Token> tokens = new ArrayList<>();
        final int[] lineIndexes = StringUtils.getNewlineIndexes(src);
        MatchResult latest;
        Token t;
        LexRule curRule;
        Location curLocation;
        int c, indexOfLine;
        while(!sc.eof()) {
            c = 0;
            t = null;
            indexOfLine = IntUtils.getClosestLowerBoundIndex(sc.getPosition(), lineIndexes);
            if(indexOfLine == -1) {
                curLocation = new Location(name, 1, sc.getPosition());
            } else {
                curLocation = new Location(name, indexOfLine+2, sc.getPosition() - lineIndexes[indexOfLine]);
            }
            while(t == null && c < rules.length) {
                curRule = rules[c];
                latest = sc.chunkWith(curRule.getRegex());
                if(latest != null && !latest.group().equals("")) {
                    t = new Token(curRule.getOutToken(), latest.groupCount() > 0 ? latest.group(1) : latest.group(), curLocation);
                    tokens.add(t);
                    c = rules.length;
                }
                c += 1;
            }
            if(t == null) {
                throw new LanguageSyntaxException("Invalid syntax; no matching token", curLocation);
            }
            if(this.stripWhitespace) {
                sc.eatWhitespace();
            }
        }
        return tokens.stream().toArray(i -> new Token[i]);
    }
    
    public void setStripWhitespace(boolean sw) {
        this.stripWhitespace = sw;
    }
}
