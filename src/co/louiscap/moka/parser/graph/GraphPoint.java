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
package co.louiscap.moka.parser.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Louis Capitanchik
 */
public class GraphPoint {
    public final String id;
    
    protected Map<String, GraphPoint> connections;
    protected Set<String> terminals;
    
    public GraphPoint(String id) {
        this.id = id;
        this.connections = new HashMap<>();
        this.terminals = new HashSet<>();
    }
    
    /**
     * Adds a link from this graph point to another graph point
     * @param next 
     */
    public void addConnection(GraphPoint next) {
        this.connections.put(next.id, next);
    }

    /**
     * Gets the GraphPoint object connected to this GraphPoint that has the
     * specified ID. Will return null if this GraphPoint has no link to the 
     * given ID.
     * @param id
     * @return 
     */
    public GraphPoint getConnection(String id) {
        return connections.get(id);
    }
    
    /**
     * Gets the GraphPoint object connected to this GraphPoint that has the
     * same ID as the specified GraphPoint. This method does not guarantee
     * that the returned GraphPoint contains the same connections as the one
     * provided, but it will still respect the <code>.equals()</code> contract
     * for GraphPoints (ID equality)
     * @param gp
     * @return 
     */
    public GraphPoint getConnection(GraphPoint gp) {
        return this.getConnection(gp.id);
    }
    
    public void addTerminal(String terminalID) {
        
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final GraphPoint other = (GraphPoint) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
