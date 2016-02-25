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

import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class for stepping through a string and returning certain elements ("chunks") of it
 * @author Louis Capitanchik &lt;contact@louiscap.co&gt;
 */
public class StringChunker {
    
    private int position, sourceLength;
    private String source;
    
    /**
     * Create a new instance of StringChunker with the target string as the source for future
     * operations
     * @param source The string that will be split in to chunks
     */
    public StringChunker(String source) {
        this.source = source;
        this.sourceLength = source.length();
        this.position = 0;
    }
    
    /**
     * Gets the current index of the String that the StringChunker is sitting on.
     * This will be equal to the number of characters processed - 1 (0 based
     * index).
     * @return An int specifying how far through the String this StringChunker
     * has gotten
     */
    public int getPosition() {
        return this.position;
    }
    
    /**
     * Return the position of the StringChunker to the beginning of the existing source string
     */
    public void reset() {
        this.position = 0;
    }
    
    /**
     * Change the source string to the provided value and reset the state of this StringChunker as
     * if it was newly instantiated
     * @param newSource The new String to use as the source for future operations
     */
    public void reset(String newSource) {
        this.source = newSource;
        this.sourceLength = newSource.length();
        this.position = 0;
    }
    
    /**
     * Step backwards from the current position by the specified distance. If this operation would
     * cause the position of the StringChunker to be a negative value, it is set to 0 instead.
     * @param length The distance to travel back along the source string (towards index 0)
     */
    public void reverse(int length) {
        position -= length;
        if (position < 0) {
            position = 0;
        }
    }
    /**
     * Step forward through the source String by the specified distance, ignoring anything that is
     * passed over. If this operation would cause the position of the StringChunker to be set past
     * the last index of the source String, it is set to target the last index instead.
     * @param length The distance to travel forward along the source String (towards index 
     * {@code source.length()}).
     */
    public void skip(int length) {
        position += length;
        if(position >= sourceLength) {
            position = sourceLength - 1;
        }
    }
    
    /**
     * Checks to see whether or not there are still characters in the String after its current position
     * @return Wether or not the StringChunker has reached the end of the current source String
     */
    public boolean hasNext() {
        return position < (sourceLength);
    }
    
    /**
     * Collects everything at the start of the string until it encounters the given needle, and returns
     * all that has been collected. This version of the method skips over the needle so that the next 
     * action is performed at the end of the target string (analogous to calling
     * {@link StringChunker#getUntil(String, boolean)} with false as the second parameter). Increments
     * StringChunker position
     * @param needle The target string to search for. Acts as a delimiter.
     * @return A {@link java.lang.String} containing every character in the string until the needle is 
     * found. If the needle is not found, or it is null, the String will contain every character from the 
     * current position of the chunker until the end of the source String.
     */
    public String getUntil(String needle) {
        return getUntil(needle, false);
    }
    
    /**
     * Collects everything at the start of the string until it encounters the given needle, and returns
     * all that has been collected. This version allows the needle to be included in the returned
     * value, and will position the Chunker instance at the end of the needle. Increments
     * StringChunker position.
     * @param needle The target string to search for. Acts as a delimiter.
     * @param includeNeedle Whether or not to include the needle in the return value
     * @return A {@link String} containing every character in the string as well as the needle. If
     * the needle is not found, or it is null, the String will contain every character from the current 
     * position of the chunker until the end of the source String.
     */
    public String getUntil(String needle, boolean includeNeedle) {
        boolean found = false; // If found isn't included, the needle gets appended to the
                               // result when it isn't present
        if(needle == null) {
            return tail();
        }
        
        StringBuilder contents = new StringBuilder();
        String sub = source.substring(position);
        
        while(!(found = sub.startsWith(needle)) && sub.length() > 0) {
            contents.append(sub.charAt(0));
            sub = sub.substring(1);
            position += 1;
        }
        
        if(includeNeedle && found) {
            contents.append(needle);
            position += needle.length();
        }
        
        return contents.toString();
    }
    
    /**
     * Collects everything from the current position of the StringChunker until it encounters the 
     * given needle, and returns all that has been collected. This version allows the needle to be 
     * included in the returned value, and will return the position of the StringChunker to its position 
     * prior to the method being invoked.
     * @param needle The target string to search for. Acts as a delimiter.
     * @return A {@link String} containing every character in the string as well as the needle. If
     * the needle is not found, or it is null, the String will contain every character from the current 
     * position of the chunker until the end of the source String.
     */
    public String peekUntil(String needle) {
        return peekUntil(needle, false);
    }
    
    /**
     * Collects everything at the start of the string until it encounters the given needle, and returns
     * all that has been collected. This version allows the needle to be included in the returned
     * value, and will return the position of the StringChunker to its position prior to the method 
     * being invoked.
     * @param needle The target string to search for. Acts as a delimiter.
     * @param includeNeedle Whether or not to include the needle in the return value
     * @return A {@link String} containing every character in the string as well as the needle. If
     * the needle is not found, or it is null, the String will contain every character from the current 
     * position of the chunker until the end of the source String.
     */
    public String peekUntil(String needle, boolean includeNeedle){
        String result = getUntil(needle, includeNeedle);
        reverse(result.length());
        return result;
    }
    
    /**
     * Get the next N characters from the source string and return them. If there are not N remaining
     * characters (from the current position of the StringChunker) then the rest of the string will
     * be returned. Increments StringChunker position.
     * @param n The number of characters to return from the source String
     * @return If {@code n} is less than or equal to the remaining number of characters in the source
     * String, a String of length {@code n}  containing the following {@code n} characters is returned.
     * Otherwise, the remainder of the source String is returned.
     */
    public String getNext(int n) {
        if(sourceLength - position < n) {
            n = sourceLength - position;
        }
        String result = source.substring(position, position + n);
        position += n;
        return result;
    }
    
    /**
     * Get the next N characters from the source string and return them. If there are not N remaining
     * characters (from the current position of the StringChunker) then the rest of the string will
     * be returned. Does not affect StringChunker position.
     * @param n The number of characters to return from the source String
     * @return If {@code n} is less than or equal to the remaining number of characters in the source
     * String, a String of length {@code n}  containing the following {@code n} characters is returned.
     * Otherwise, the remainder of the source String is returned.
     */
    public String peekNext(int n) {
        String result = getNext(n);
        reverse(result.length());
        return result;
    }
    
    /**
     * Use the provided regex to chunk the string. If the pattern matches, the
     * string chunker will proceed n characters from the current position, where
     * n is the last index of the match in the tail of the source (meaning that 
     * if the match is found after part way through the remainder of the source,
     * the position of the string chunker will skip over content that appears 
     * before).
     * The matcher object is returned to allow arbitrary processing of the output
     * @param regex A regular expression that will be used to proceed ahead in
     * the source string based on the position of the match
     * @return If the provided regex matches an element of the remaining string,
     * a {@link java.util.regex.Matcher} object with the results of the match.
     * Othewise, returns {@code null}.
     */
    public MatchResult chunkWith(Pattern regex) {
        Matcher m = regex.matcher(tail());
        if(m.find()){
            this.position += m.end();
            return m.toMatchResult();
        } else {
            return null;
        }
    }
    
    /**
     * Skips over the whitespace positioned at the start of the remaining string
     * and updates the position of the StringChunker accordingly
     */
    public void eatWhitespace() {
        this.chunkWith(Pattern.compile("\\s+"));
    }
    
    /**
     * Gets the contents of the source String prior to the current position of the StringChunker
     * @return A String containing all elements of the source string that have already been
     * processed; that is, all characters from the start of the source until the current position of
     * the StringChunker
     */
    public String head(){
        return source.substring(0, position);
    }
    /**
     * Gets the remaining content of the source String from the current position of the 
     * StringChunker. Does not affect StringChunker position.
     * @return A String containing all elements of the source String from the current position of
     * the StringChunker until the end of the source String
     */
    public String tail(){
        return source.substring(position);
    }
    
    /**
     * Check to see if the string chunker is sitting at the end of the source
     * string
     * @return True if the string chunker is positioned at the end of the source
     * string, False if the chunker is positioned at the start of the source
     */
    public boolean eof() {
        return this.position == source.length();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.source);
        return hash;
    }

    /**
     * Equality for a StringChunker means that the two objects are chunking the
     * same string. If you need to check that two chunkers are exactly equal
     * (i.e. same state of chunking), use 
     * {@link StringChunker#equalState(StringChunker)} instead.<br><br>
     * {@inheritDoc }
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StringChunker other = (StringChunker) obj;
        if (!Objects.equals(this.source, other.source)) {
            return false;
        }
        return true;
    }
    
    /**
     * A more thorough equality check for equality between two StringChunkers,
     * that also makes sure that they are also in the same state.
     * @param obj The object to which this string chunker should be compared
     * @return Whether or not the two chunkers are equal in both content and
     * state
     */
    public boolean equalState(Object obj) {
        return this.equals(obj) 
                && this.position == ((StringChunker)obj).position;
    }
}
