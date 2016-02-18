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
package co.louiscap.moka.utils.io;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;

/**
 * A class for printing strings to multiple different streams
 * @author Louis Capitanchik &lt;contact@louiscap.co&gt;
 */
public class MultiplexedStringPrinter {
    private final HashMap<String, PrintStream> channels;
    
    /**
     * Creates a new empty MultiplexedStringPrinter, useful for printing one string to multiple output
     * sources
     */
    public MultiplexedStringPrinter() {
        this.channels = new HashMap<>();        
    }
    
    /**
     * Creates a new MultiplexedStringPrinter based on an existing HashMap, useful for printing one 
     * string to multiple output sources
     * @param channels A HashMap containing tagged print streams to form the basis of this printer
     */
    public MultiplexedStringPrinter(HashMap<String, PrintStream> channels){
        this.channels = channels;
    }
    
    /**
     * Add a new output that can be written to.
     * @param tag A unique name for this print stream, so that it can be issued individual commands
     * @param channel The print stream to add to this String Printer
     */
    public void addChannel (String tag, PrintStream channel) {
        channels.put(tag, channel);
    }
    
    /**
     * Remove one or more print stream(s) from this String Printer if it is no longer needed
     * @param tags The names of the channels to remove
     */
    public void removeChannels (String... tags){
        Arrays.stream(tags).forEach((tag) -> channels.remove(tag));
    }
    
    /**
     * Allows retrieval of a channel that has previously been assigned to this
     * MultiplexedStringPrinter instance
     * @param tag The tag of the channel to retrieve
     * @return The channel with the specified tag, or null if no such tag exists
     */
    public PrintStream getChannel (String tag){
        return channels.get(tag);
    }
    
    /**
     * Allows retrieval of multiple channels that have previously been assigned
     * to this MultiplexedStringPrinter instance
     * @param tags A list of tags to retrieve
     * @return An array containing all of the channels whose tags were specified
     * by the tags parameter <i>and</i> had previously been assigned to the
     * StringPrinter
     */
    public PrintStream[] getChannels (String... tags) {
        return Arrays.stream(tags)
                .map(tag -> channels.get(tag))
                .filter(channel -> channel != null)
                .toArray(PrintStream[]::new);
    }
    
    /**
     * Print a string and then a newline to every print stream in this MultiplexedStringPrinter
     * @param s The string to print out
     * @see java.io.PrintStream#println(java.lang.String)
     */
    public void println(String s) {
        channels.entrySet().forEach(e -> e.getValue().println(s));
    }
    
    /** 
     * Print a string and then a newline to the specified channels in this MultiplexedStringPrinter
     * @param s The string to print out
     * @param tags The names of the channels to print to. Null channels will be ignored
     * @see java.io.PrintStream#println(java.lang.String)
     */
    public void println(String s, String... tags) {
        Arrays.stream(tags).forEach((tag) -> {
            PrintStream p = channels.get(tag);
            if (p != null) {
                p.println(s);
            }
        });
    }
    
    /**
     * Print a string to every print stream in this MultiplexedStringPrinter
     * @param s The string to print out
     * @see java.io.PrintStream#print(java.lang.String)
     */
    public void print(String s) {
        channels.entrySet().forEach(e -> e.getValue().print(s));
    }
    
    /**
     * Print a string to the specified channels in this MultiplexedStringPrinter
     * @param s The string to print out
     * @param tags The names of the channels to print to
     * @see java.io.PrintStream#print(java.lang.String)
     */
    public void print(String s, String... tags) {
        Arrays.stream(tags).forEach((tag) -> {
            PrintStream p = channels.get(tag);
            if (p != null) {
                p.print(s);
            }
        });
    }

    /**
     * Close all the print streams in this MultiplexedStringPrinter and flush their contents
     * @see java.io.PrintStream#close() 
     */
    public void close() {
        channels.entrySet().forEach(e -> e.getValue().close());
    }
    
    /**
     * Close the specified print streams in this MultiplexedStringPrinter and flush their contents
     * @param tags The names of the print streams to close
     * @see java.io.PrintStream#close()
     */
    public void close(String... tags) {
        Arrays.stream(tags).forEach((tag) -> {
            PrintStream p = channels.get(tag);
            if (p != null) {
                p.close();
            }
        });
    }

    /**
     * Flush the contents of all print streams in this MultiplexedStringPrinter
     * @see java.io.PrintStream#flush() 
     */
    public void flush() {
        channels.entrySet().forEach(e -> e.getValue().flush());
    }
    
    /**
     * Flush the contents of the specified print streams in this MultiplexedStringPrinter
     * @param tags The names of the print streams to flush
     * @see java.io.PrintStream#flush() 
     */
    public void flush(String... tags) {
        Arrays.stream(tags).forEach((tag) -> {
            PrintStream p = channels.get(tag);
            if (p != null) {
                p.flush();
            }
        });
    }
    
    /**
     * Gets a subset of the current print streams in this MultiplexedStringPrinter and returns them
     * wrapped in a new MultiplexedStringPrinter. <br>
     * For example, if you had a MultiplexedStringPrinter with 15 channels, but only wanted to print 
     * to three labelled "out", "debug" and "file" repeatedly without having to provide the tags every
     * time, you could batch them into their own MultiplexedStringPrinter and call the bare methods
     * such as {@link MultiplexedStringPrinter#print(String)} and 
     * {@link MultiplexedStringPrinter#flush()} by  calling 
     * {@code example.getBatch("out", "debug", "file")}
     * @param tags The names of the channels to batch
     * @return A new MultiplexedStringPrinter containing only the specified print streams
     */
    public MultiplexedStringPrinter getBatch(String... tags) {
        HashMap<String, PrintStream> batch = new HashMap<>();
        Arrays.stream(tags).forEach((tag) -> {
            PrintStream cur = channels.get(tag);
            if(cur != null) {
                batch.put(tag, cur);
            }
        });
        return new MultiplexedStringPrinter(batch);
    }
}
