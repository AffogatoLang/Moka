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
import co.louiscap.moka.parser.LangRule;
import co.louiscap.moka.parser.Parser;
import co.louiscap.moka.utils.io.Logging;
import org.apache.commons.cli.CommandLine;

/**
 * @author Louis Capitanchik
 */
public class ParserCLI {
    /**
     * Runs the program up in parser only mode. Requires a pre-parsed command line
     * rather than the standard array of string args.
     * @param args A list of pre-parsed command line options. Globals like the
     * logging system should already be configured (although parser specific options
     * may override global configs for the purpose of running solely as a parser)
     */
    public static int main(CommandLine args) throws InvalidFormatException {
        Logging.LOGGER.println("[[ Running in Parser mode ]]", "debug");
        LangRule[] tmp = {
            new LangRule("S_ASSIGN : T_VAR T_WHITESPACE? T_IDENT T_WHITESPACE? T_ASSIGN T_WHITESPACE? S_DATA"),
            new LangRule("S_ASSIGN : T_IDENT T_WHITESPACE? T_ASSIGN T_WHITESPACE? S_DATA")
        };
        Parser p = new Parser(tmp);
        return 0;
    }
    
}
