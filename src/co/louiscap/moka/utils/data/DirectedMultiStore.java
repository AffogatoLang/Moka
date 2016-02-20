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

import co.louiscap.moka.utils.data.DirectedMultiStore.Relation;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * A sorted set of relationships between various entities that can be identified
 * by a singular value. Every relationship has an object, a subject and a relation
 * between the two; this relationship is stored multiple times in various
 * permutations to make range queries on the data set easier
 * @author Louis Capitanchik
 * @param <RTYPE>
 */
public class DirectedMultiStore<RTYPE extends Enum> {
    private final TreeSet<String> relations;

    public DirectedMultiStore() {
        this.relations = new TreeSet<>();
    }
    
    public void add(String object, RTYPE relation, String subject) {
        relations.add(toEntry(object, relation.toString(), subject));
        relations.add(toEntry(object, subject, relation.toString()));
        relations.add(toEntry(relation.toString(), object, subject));
    }
    
    /**
     * Retrieve the relations of a specific type from one object. This will
     * return all relationships with the specified object and relation, and
     * all relevant subjects
     * @param object
     * @param relation
     * @return 
     */
    public Relation[] getRelationsFrom(String object, RTYPE relation) {
        Set<String> results = relations.subSet(toQuery(object, relation.toString()),
                                                false,
                                                toQuery(object, relation.toString()) + "~",
                                                false);
        return results.stream()
                .map(Relation::fromORSString)
                .toArray(Relation[]::new);
    }
    
    public Relation[] getRelationsBetween(String object, String subject) {
        Set<String> results = relations.subSet(toQuery(object, subject),
                                                false,
                                                toQuery(object, subject) + "~",
                                                false);
        return results.stream()
                .map(Relation::fromOSRString)
                .toArray(Relation[]::new);
    }
    
    public Relation[] getRelationsOfType(RTYPE relation) {
        Set<String> results = relations.subSet(relation.toString() + "::",
                                                false,
                                                relation.toString() + "::~",
                                                false);
        return results.stream()
                .map(Relation::fromROSString)
                .toArray(Relation[]::new);
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.relations);
        return hash;
    }

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
        final DirectedMultiStore<?> other = (DirectedMultiStore<?>) obj;
        if (!Objects.equals(this.relations, other.relations)) {
            return false;
        }
        return true;
    }
    
            
    private String toQuery(String first, String second) {
        return first + "::" + second + "::";
    }
    
    private String toEntry(String first, String second, String third) {
        return first + "::" + second + "::" + third;
    }
    
    /**
     * A struct that provides read only access to a relationship retrieved from
     * the MultiStore. As data in the MultiStore is serialised, a Relation will
     * only provide object/relation/subject data as a String; other conversions
     * are the responsibility of the calling code.
     */
    public static class Relation {
        public final String object, relation, subject;
        private Relation(String object, String relation, String subject) {
            this.object = object;
            this.relation = relation;
            this.subject = subject;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 79 * hash + Objects.hashCode(this.object);
            hash = 79 * hash + Objects.hashCode(this.relation);
            hash = 79 * hash + Objects.hashCode(this.subject);
            return hash;
        }

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
            final Relation other = (Relation) obj;
            if (!Objects.equals(this.object, other.object)) {
                return false;
            }
            if (!Objects.equals(this.relation, other.relation)) {
                return false;
            }
            if (!Objects.equals(this.subject, other.subject)) {
                return false;
            }
            return true;
        }
        
        /**
         * Creates a new relation from a MultiStore relationship string with 
         * O R S ordering
         * @param src The ORS ordered string to parse
         * @return A relation that represents the given string
         */
        private static Relation fromORSString(String src) {
            String[] parts = src.split("::");
            return new Relation(parts[0], parts[1], parts[2]);
        }
        /**
         * Creates a new relation from a MultiStore relationship string with 
         * O S R ordering
         * @param src The OSR ordered string to parse
         * @return A relation that represents the given string
         */
        private static Relation fromOSRString(String src) {
            String[] parts = src.split("::");
            return new Relation(parts[0], parts[2], parts[1]);
        }
        /**
         * Creates a new relation from a MultiStore relationship string with 
         * R O S ordering
         * @param src The ROS ordered string to parse
         * @return A relation that represents the given string
         */
        private static Relation fromROSString(String src) {
            String[] parts = src.split("::");
            return new Relation(parts[1], parts[0], parts[2]);
        }
    }
}
