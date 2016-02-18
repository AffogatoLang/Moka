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
package co.louiscap.moka.utils.data;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Louis Capitanchik
 */
public class Semver {
    
    private static boolean doPrebake = true;
    private static Map<String, Semver> prebake = new HashMap<>();
    private static final Pattern SEMVER_PATTERN = Pattern.compile("^(\\^)?(\\d*)\\.(\\d*)\\.(\\d*)$");
    
    private int major, minor, patch;
    private boolean fuzzy;
    
    private Semver (String semver) {
        Matcher match = SEMVER_PATTERN.matcher(semver);
        if(!match.find()) {
            throw new IllegalArgumentException("Invalid semver " + semver);
        } else {
            this.fuzzy = match.group(1) == null;
            this.major = Integer.parseInt(match.group(2));
            this.minor = Integer.parseInt(match.group(3));
            this.patch = Integer.parseInt(match.group(4));
        }
    }
    
    public Semver (int major, int minor, int patch) {
        this(major, minor, patch, false);
    }
    
    public Semver (int major, int minor, int patch, boolean fuzzy) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.fuzzy = fuzzy;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    /**
     * The fuzzy
     * @return 
     */
    public boolean isFuzzy() {
        return fuzzy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.major;
        hash = 53 * hash + this.minor;
        hash = 53 * hash + this.patch;
        hash = 53 * hash + (this.fuzzy ? 1 : 0);
        return hash;
    }

    /**
     * Two Semver objects are considered equal if they both contain the same
     * version number <b>and</b> have an equal value for fuzzy.<br>
     * e.g. ^1.2.0 is equal to another Semver containing ^1.2.0 but NOT to a 
     * Semver containing 1.2.0 because the latter points to the version 1.2.0
     * but the former points to the latest patch of that major.minor combination
     * (sometimes written as 1.2.* or 1.2.X)
     * @inheritDoc
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
        final Semver other = (Semver) obj;
        if (this.major != other.major) {
            return false;
        }
        if (this.minor != other.minor) {
            return false;
        }
        if (this.patch != other.patch) {
            return false;
        }
        return this.fuzzy == other.fuzzy;
    }
    
    

    @Override
    public String toString() {
        return String.format("%s%d.%d.%d",
                this.fuzzy ? "^" : "",
                this.major,
                this.minor,
                this.patch);
    }    
    
    public static boolean isValid(String semver) {
        semver = semver.trim();
        semver = semver.replaceFirst("v|V|=", "");
        
        Matcher match = SEMVER_PATTERN.matcher(semver);
        boolean valid = match.find();
        
        if(doPrebake && valid) {
            prebake.put(semver, new Semver(semver));
        }
        
        return valid;
    }
    
    /**
     * Create a Semver object from a string. The string should conform to one of
     * the following formats:<br>
     * <ul>
     *  <li>(Major).(Minor).(Patch)</li>
     *  <li>v(Major).(Minor).(Patch)</li>
     *  <li>V(Major).(Minor).(Patch)</li>
     *  <li>=(Major).(Minor).(Patch)</li>
     *  <li>^(Major).(Minor).(Patch)</li>
     * </ul>
     * All of the formats with the exception of the last will parse into equal
     * Semver objects. The last format sets the "fuzzy" flag to true, as denoted
     * by the caret in front of the patch number
     * @param semver
     * @return 
     */
    public static Semver fromString(String semver) {
        if(!prebake.containsKey(semver)) {
            prebake.put(semver, new Semver(semver));
        }
        return prebake.get(semver);
    }
    
    public static boolean togglePrebake() {
        doPrebake = !doPrebake;
        return doPrebake;
    }
}
