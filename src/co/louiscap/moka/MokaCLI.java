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
package co.louiscap.moka;

import co.louiscap.moka.utils.io.MultiplexedStringPrinter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Main entry point for Moka compiler. Parses options and invokes modules based
 * upon inputs.
 * @author Louis Capitanchik
 */
public class MokaCLI {

    public static final MultiplexedStringPrinter PRINTER = new MultiplexedStringPrinter();
    public static CommandLine PROGOPTS;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PRINTER.addChannel("out", System.out);
        PRINTER.addChannel("err", System.err);
        
        CommandLineParser cliparse = new DefaultParser();
        Options opts = setupCommandLine();
        try {
            PROGOPTS = cliparse.parse(opts, args);
        } catch (ParseException ex) {
            PRINTER.println(ex.getMessage(), "err");
        }
        
        if(PROGOPTS.hasOption("h")) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("moka", opts, true);
            System.exit(0);
        }
        
        if(PROGOPTS.hasOption("v")) {
            PRINTER.addChannel("debug", System.out);
        }
        
        System.exit(312);
    }
    
    private static Options setupCommandLine() {
        Options options = new Options();
        options.addOption("v", false, "Verbose; Print debug info to stdout");
        options.addOption("o", "out", true, "Outfile; the compilation target");
        options.addOption("e", true, "File encoding. Defaults to UTF-8");
        options.addOption("h", "help", false, "Print out this help text");
        return options;
    }
    
}
