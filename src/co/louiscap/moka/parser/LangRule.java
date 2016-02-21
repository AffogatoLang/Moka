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
package co.louiscap.moka.parser;

import co.louiscap.moka.exceptions.InvalidFormatException;
import co.louiscap.moka.exceptions.MismatchedRuleTargetException;
import co.louiscap.moka.utils.io.Logging;
import co.louiscap.moka.utils.string.StringChunker;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Louis Capitanchik
 */
public class LangRule {
    public final String target;
    public final Set<String[]> sequences;
    private static final Pattern TOKEN_BREAKER = Pattern.compile("\\s*(\\(?\\w*\\w\\)?|\\?|\\(|\\))");
    
    public LangRule(String src) throws InvalidFormatException {
        ArrayDeque<RulePart> ruleParts = new ArrayDeque<>();
        StringChunker sc = new StringChunker(src);
        target = sc.getUntil(":", false).trim();
        while(!sc.eof() && !sc.tail().matches("^\\s*$")) {
            MatchResult chunk = sc.chunkWith(TOKEN_BREAKER);
            switch(chunk.group().trim()){
                case "?":
                    if(ruleParts.isEmpty()) {
                        throw new InvalidFormatException("Rule Parse " + target,
                        "Dangling optional operator; no prior token");
                    } else {
                        if(ruleParts.getLast().optional) {
                            Logging.LOGGER.println("Redundant optional operator for " + target + " rule", "debug");
                        } else {
                            ruleParts.getLast().optional = true;
                        }
                    }
                    break;
                case ")":
                case "(":
                    throw new InvalidFormatException("Rule Parse " + target,
                    "Unnatached bracket in rule; remove whitespace between bracket and target token");
                default:
                    String r = chunk.group().trim();
                    boolean startRule = r.startsWith("("), 
                            endRule = r.endsWith(")");
                    r = r.replace("(", "").replace(")", "");
                    AtomicRule token = new AtomicRule(r);
                    token.endsGroup = endRule;
                    token.startsGroup = startRule;
                    ruleParts.add(token);
                    break;
            }
        }
        ruleParts = collapse(ruleParts);
        sequences = new LinkedHashSet<>();
        createPermutations(ruleParts);
    }
    
    public void merge(LangRule other) throws MismatchedRuleTargetException {
        if(this.target.equals(other.target)) {
            this.sequences.addAll(other.sequences);
        } else {
            throw new MismatchedRuleTargetException(other.target, this.target);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Target: ");
        sb.append(target);
        sb.append("\n");
        sequences.forEach(seq -> {
            sb.append("\t");
            sb.append(Arrays.toString(seq));
            sb.append("\n");
        });
        sb.replace(sb.length()-1, sb.length(), "");
        return sb.toString();
    }
    
    private ArrayDeque<RulePart> collapse(ArrayDeque<RulePart> parts) {
        //TODO: Tidy up the logic here. Right mess, init?
        //TODO: Also, handle edge cases for incorrect formatting etc plz
        ArrayDeque<RulePart> collapsed = new ArrayDeque<>();
        while(!parts.isEmpty()) {
            RulePart rp = parts.pop();
            if(rp.endsGroup && !rp.startsGroup) {
                ArrayDeque<RulePart> grouping = new ArrayDeque<>();
                grouping.push(rp);
                RulePart next = collapsed.pop();
                while(!(next.startsGroup) || (next.startsGroup && next.endsGroup)) {
                    grouping.push(next);
                    next = collapsed.pop();
                }
                grouping.push(next);
                RuleGroup group = new RuleGroup();
                group.optional = rp.optional;
                group.startsGroup = true;
                group.endsGroup = true;
                while(!grouping.isEmpty()) {
                    group.push(grouping.pop());
                }
                collapsed.push(group);
            } else {
                collapsed.push(rp);
            }
        }
        ArrayDeque<RulePart> correctOrder = new ArrayDeque<>();
        while(!collapsed.isEmpty()) {
            correctOrder.push(collapsed.pop());
        }
        return correctOrder;
    }

    private void createPermutations(ArrayDeque<RulePart> ruleParts) {
        //TODO: Man, that's a lot of edge cases that need handled that just...haven't been, you know?
        //TODO: Also, need to handle sub level optionals. Currently only does the top level stuff. Cream of the crop etc.
        ArrayList<ArrayList<String>> seqs = new ArrayList<>();
        seqs.add(new ArrayList<>());
        while(!ruleParts.isEmpty()) {
            RulePart rp = ruleParts.removeFirst();
            if(!rp.optional) {
                seqs.forEach(s -> s.addAll(ruleToNameList(rp)));
            } else {
                ArrayList<ArrayList<String>> without = new ArrayList<>(seqs.size());
                seqs.forEach(s -> {
                    without.add((ArrayList<String>)s.clone());
                    s.addAll(ruleToNameList(rp));
                });
                seqs.addAll(without);
            }
        }
        seqs.forEach(s -> sequences.add(s.stream().toArray(String[]::new)));
    }
    
    private List<String> ruleToNameList(RulePart rp) {
        if(rp instanceof AtomicRule) {
            return Arrays.asList(((AtomicRule) rp).name);
        } else if (rp instanceof RuleGroup) {
            ArrayList<String> recurse = new ArrayList<>();
            ((RuleGroup) rp).parts.forEach(r -> recurse.addAll(ruleToNameList(r)));
            return recurse;
        } else {
            throw new RuntimeException("This can only be reached by gross incompetence by a future maintainer");
        }
    }
    
    private abstract class RulePart {
        boolean startsGroup = false;
        boolean endsGroup = false;
        boolean optional = false;
        public abstract String[] getAsListOfTokenNames();
    }
    
    private class AtomicRule extends RulePart {
        String name;
        public AtomicRule(String name) {
            this.name = name;
        }
        @Override
        public String[] getAsListOfTokenNames() {
            return new String[]{name};
        }
        @Override
        public String toString() {
            String s = "";
            s += this.startsGroup ? "(" : "";
            s += this.name;
            s += this.endsGroup ? ")" : "";
            s += this.optional ? "?" : "";
            return s;
        }
    }
    private class RuleGroup extends RulePart {
        final List<RulePart> parts;
        public RuleGroup(RulePart... rules) {
            parts = new ArrayList<>();
            parts.addAll(Arrays.asList(rules));
        }
        public void push(RulePart rp) {
            parts.add(rp);
        }

        @Override
        public String[] getAsListOfTokenNames() {
            return parts.stream()
                    .map(rp -> rp.getAsListOfTokenNames())
                    .flatMap(rpl -> Arrays.stream(rpl))
                    .collect(Collectors.toList())
                    .stream()
                    .toArray(String[]::new);
        }
        @Override
        public String toString() {
            final StringBuilder s = new StringBuilder();
            parts.forEach(p -> s.append(p.toString()));
            return s.toString();
        }
    }
}
