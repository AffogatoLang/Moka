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

import co.louiscap.moka.exceptions.InvalidFormatException;
import co.louiscap.moka.exceptions.InvalidModuleException;
import co.louiscap.moka.exceptions.LanguageSyntaxException;
import co.louiscap.moka.lexer.LexFile;
import co.louiscap.moka.lexer.LexRule;
import co.louiscap.moka.lexer.Lexer;
import co.louiscap.moka.lexer.Token;
import co.louiscap.moka.modules.Module;
import co.louiscap.moka.modules.ModuleReader;
import co.louiscap.moka.parser.LangFile;
import co.louiscap.moka.parser.LangRule;
import co.louiscap.moka.parser.Parser;
import co.louiscap.moka.parser.RuleMerger;
import co.louiscap.moka.utils.io.Logging;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * Main entry point for Moka compiler. Parses options and invokes modules based
 * upon inputs.
 * @author Louis Capitanchik
 */
public class MokaCLI {

//    public static final MultiplexedStringPrinter PRINTER = new MultiplexedStringPrinter();
    public static CommandLine PROGOPTS;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidFormatException, IOException {
        
        CommandLineParser cliparse = new DefaultParser();
        Options opts = setupCommandLine();
        boolean asArchive = false;
        try {
            PROGOPTS = cliparse.parse(opts, args);
        } catch (ParseException ex) {
            Logging.LOGGER.println(ex.getMessage(), "err");
        }
        
        if(PROGOPTS.hasOption("h")) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("moka", opts, true);
            System.exit(0);
        }
        
        if(PROGOPTS.hasOption("v")) {
            Logging.LOGGER.addChannel("debug", System.out);
        }
        
        if(PROGOPTS.hasOption("m")) {
            String opt = PROGOPTS.getOptionValue("m");
            Logging.LOGGER.println("Running in mode " + opt, "debug");
            switch(opt) {
                case "lexer":
                    System.exit(LexerCLI.main(PROGOPTS));
                    break;
                case "parser":
                    System.exit(ParserCLI.main(PROGOPTS));
                    break;
                default:
                    Logging.LOGGER.println("No such mode " + opt, "err");
                    System.exit(101);
            }
        } else {
            if(PROGOPTS.hasOption("i")) {
                asArchive = PROGOPTS.hasOption("a");
                File f = new File(PROGOPTS.getOptionValue("i"));
                if(asArchive) {
                    throw new RuntimeException("Whoah there partner, not doing archives yet");
                } else {
                    if(!f.isDirectory()) {
                        Logging.LOGGER.println("Input argument not directory and archive flag not set", "err");
                        System.exit(103);
                    }
                    Logging.LOGGER.println("Using Moka module at location " + f.getCanonicalPath(), "debug");
                    ModuleReader validation = null;
                    try {
                        validation = new ModuleReader(f);
                    } catch (InvalidModuleException ex) {
                        Logging.LOGGER.println("Invalid module definition given", "err");
                        ex.printStackTrace(Logging.LOGGER.getChannel("err"));
                        System.exit(104);
                    }
                    Module module = null;
                    try {
                        module = validation.asModule();
                    } catch (InvalidModuleException ex) {
                        ex.printStackTrace(Logging.LOGGER.getChannel("err"));
                        System.exit(105);
                    }
                    
                    Collection<LexFile> lexFiles = module.getAllLexFiles().values();
                    Set<LexRule> lexSet = new HashSet<>();
                    lexFiles.forEach(file -> lexSet.addAll(Arrays.asList(file.getRules())));
                    
                    Lexer lexer = new Lexer(lexSet.stream().toArray(LexRule[]::new));
                    if(PROGOPTS.hasOption("s")) {
                        File srcFile = new File(PROGOPTS.getOptionValue("s"));
                        if(!srcFile.exists()) {
                            Logging.LOGGER.println("Invalid source file provided", "err");
                            System.exit(106);
                        }
                        Logging.LOGGER.println("Reading in source file " + FilenameUtils.getName(srcFile.getPath()), "debug");
                        String source = FileUtils.readFileToString(srcFile);
                        Token[] fileTokens = null;
                        try {
                            fileTokens = lexer.process(source, FilenameUtils.getName(srcFile.getPath()));
                        } catch (LanguageSyntaxException ex) {
                            ex.printStackTrace(Logging.LOGGER.getChannel("err"));
                            System.exit(107);
                        }
                        Logging.LOGGER.println("Created Token stream:", "debug");
                        Logging.LOGGER.println(Arrays.toString(fileTokens), "debug");
                        
                        Collection<LangFile> langSet = module.getAllLangFiles().values();
                        RuleMerger merger = new RuleMerger();
                        langSet.stream()
                                .flatMap(lang -> Arrays.stream(lang.getRules()))
                                .forEach(rule -> merger.addRule(rule));
                        
                        Logging.LOGGER.println("Created the following language rules:", "debug");
                        Logging.LOGGER.println(merger.toString(), "debug");
                        Parser parser = new Parser(merger.getRules().stream().toArray(LangRule[]::new));
                    } else {
                        Logging.LOGGER.println("Currently required to provide source file with s argument", "err");
                        System.exit(201);
                    }
                }
            } else {
                Logging.LOGGER.println("Missing Moka module argument `i`", "err");
                System.exit(102);
            }
        }
        
        System.exit(0);
    }
    
    private static Options setupCommandLine() {
        Options options = new Options();
        options.addOption("v", false, "Verbose; Print debug info to stdout");
        options.addOption("t", "target", true, "~Target output file for compiling"
                + "the source code. Requires `source` to be set.");
        options.addOption("p", "partial", true, "~Specify the precompiled partial"
                + " to be used for parsing the source program");
        options.addOption("b", false, "~Build a Moka partial that includes "
                + "configurations up to and including the segment specified with "
                + "the `mode` option, or a full partial if no `mode` is specified.");
        options.addOption("e", true, "~File encoding. Defaults to UTF-8");
        options.addOption("h", "help", false, "Print out this help text");
        options.addOption("m", "mode", true, "Set the mode; only run a "
                + "sub-section of Moka. "
                + "Requires `chunk` to be specified for the pre-compiled sub"
                + "section being used");
        options.addOption("c", "chunk", true, "~Specifies a Moka chunk to be used"
                + " with the `mode` argument");
        options.addOption("l", "language", true, "~Use the specified pre-compiled"
                + " language core instead of a Moka Module");
        options.addOption("s", "source", true, "The source program to process "
                + "with a set of Moka language files");
        options.addOption("i", "input", true, "The input Moka Module to use. "
                + "Expects the target to be a folder, unless the `a` argument"
                + " is also provided, which will change expectations to a "
                + ".moka archive");
        options.addOption("a", false, "~Archive; The provided moka input is "
                + "stored in a .moka archive instead of pointing to a directory");
        return options;
    }
    
}
