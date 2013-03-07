// $ANTLR 3.4 /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g 2012-02-21 14:12:45

package grammar;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class CliLexer extends Lexer {
    public static final int EOF=-1;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__19=19;
    public static final int T__20=20;
    public static final int T__21=21;
    public static final int T__22=22;
    public static final int T__23=23;
    public static final int T__24=24;
    public static final int T__25=25;
    public static final int T__26=26;
    public static final int T__27=27;
    public static final int T__28=28;
    public static final int T__29=29;
    public static final int T__30=30;
    public static final int T__31=31;
    public static final int T__32=32;
    public static final int T__33=33;
    public static final int T__34=34;
    public static final int T__35=35;
    public static final int T__36=36;
    public static final int T__37=37;
    public static final int T__38=38;
    public static final int T__39=39;
    public static final int T__40=40;
    public static final int T__41=41;
    public static final int T__42=42;
    public static final int T__43=43;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int T__50=50;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int T__55=55;
    public static final int T__56=56;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int Character=4;
    public static final int Comment=5;
    public static final int Decimal=6;
    public static final int Digit=7;
    public static final int EscapeSpace=8;
    public static final int Hex=9;
    public static final int HexDigit=10;
    public static final int Identifier=11;
    public static final int NewLine=12;
    public static final int Register=13;
    public static final int String=14;
    public static final int WS=15;


    public class SyntaxErrorException extends RuntimeException {}

    public void emitErrorMessage (String msg) {
      throw new SyntaxErrorException ();
    }


    // delegates
    // delegators
    public Lexer[] getDelegates() {
        return new Lexer[] {};
    }

    public CliLexer() {} 
    public CliLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CliLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);
    }
    public String getGrammarFileName() { return "/Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g"; }

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:14:7: ( '+' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:14:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:15:7: ( '-' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:15:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:16:7: ( ':' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:16:9: ':'
            {
            match(':'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:17:7: ( '=' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:17:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "T__20"
    public final void mT__20() throws RecognitionException {
        try {
            int _type = T__20;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:18:7: ( 'a' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:18:9: 'a'
            {
            match('a'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__20"

    // $ANTLR start "T__21"
    public final void mT__21() throws RecognitionException {
        try {
            int _type = T__21;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:19:7: ( 'b' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:19:9: 'b'
            {
            match('b'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__21"

    // $ANTLR start "T__22"
    public final void mT__22() throws RecognitionException {
        try {
            int _type = T__22;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:20:7: ( 'break' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:20:9: 'break'
            {
            match("break"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__22"

    // $ANTLR start "T__23"
    public final void mT__23() throws RecognitionException {
        try {
            int _type = T__23;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:21:7: ( 'clear' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:21:9: 'clear'
            {
            match("clear"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__23"

    // $ANTLR start "T__24"
    public final void mT__24() throws RecognitionException {
        try {
            int _type = T__24;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:22:7: ( 'cpu' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:22:9: 'cpu'
            {
            match("cpu"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__24"

    // $ANTLR start "T__25"
    public final void mT__25() throws RecognitionException {
        try {
            int _type = T__25;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:23:7: ( 'dat' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:23:9: 'dat'
            {
            match("dat"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__25"

    // $ANTLR start "T__26"
    public final void mT__26() throws RecognitionException {
        try {
            int _type = T__26;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:24:7: ( 'e' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:24:9: 'e'
            {
            match('e'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__26"

    // $ANTLR start "T__27"
    public final void mT__27() throws RecognitionException {
        try {
            int _type = T__27;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:25:7: ( 'e/x' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:25:9: 'e/x'
            {
            match("e/x"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__27"

    // $ANTLR start "T__28"
    public final void mT__28() throws RecognitionException {
        try {
            int _type = T__28;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:26:7: ( 'examine' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:26:9: 'examine'
            {
            match("examine"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__28"

    // $ANTLR start "T__29"
    public final void mT__29() throws RecognitionException {
        try {
            int _type = T__29;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:27:7: ( 'examine/x' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:27:9: 'examine/x'
            {
            match("examine/x"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__29"

    // $ANTLR start "T__30"
    public final void mT__30() throws RecognitionException {
        try {
            int _type = T__30;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:28:7: ( 'g' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:28:9: 'g'
            {
            match('g'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__30"

    // $ANTLR start "T__31"
    public final void mT__31() throws RecognitionException {
        try {
            int _type = T__31;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:29:7: ( 'goto' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:29:9: 'goto'
            {
            match("goto"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__31"

    // $ANTLR start "T__32"
    public final void mT__32() throws RecognitionException {
        try {
            int _type = T__32;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:30:7: ( 'help' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:30:9: 'help'
            {
            match("help"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__32"

    // $ANTLR start "T__33"
    public final void mT__33() throws RecognitionException {
        try {
            int _type = T__33;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:31:7: ( 'i' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:31:9: 'i'
            {
            match('i'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__33"

    // $ANTLR start "T__34"
    public final void mT__34() throws RecognitionException {
        try {
            int _type = T__34;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:32:7: ( 'i/x' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:32:9: 'i/x'
            {
            match("i/x"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__34"

    // $ANTLR start "T__35"
    public final void mT__35() throws RecognitionException {
        try {
            int _type = T__35;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:33:7: ( 'info' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:33:9: 'info'
            {
            match("info"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__35"

    // $ANTLR start "T__36"
    public final void mT__36() throws RecognitionException {
        try {
            int _type = T__36;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:34:7: ( 'info/x' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:34:9: 'info/x'
            {
            match("info/x"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__36"

    // $ANTLR start "T__37"
    public final void mT__37() throws RecognitionException {
        try {
            int _type = T__37;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:35:7: ( 'ins' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:35:9: 'ins'
            {
            match("ins"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__37"

    // $ANTLR start "T__38"
    public final void mT__38() throws RecognitionException {
        try {
            int _type = T__38;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:36:7: ( 'l' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:36:9: 'l'
            {
            match('l'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__38"

    // $ANTLR start "T__39"
    public final void mT__39() throws RecognitionException {
        try {
            int _type = T__39;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:37:7: ( 'load' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:37:9: 'load'
            {
            match("load"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__39"

    // $ANTLR start "T__40"
    public final void mT__40() throws RecognitionException {
        try {
            int _type = T__40;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:38:7: ( 'm' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:38:9: 'm'
            {
            match('m'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__40"

    // $ANTLR start "T__41"
    public final void mT__41() throws RecognitionException {
        try {
            int _type = T__41;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:39:7: ( 'mem' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:39:9: 'mem'
            {
            match("mem"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__41"

    // $ANTLR start "T__42"
    public final void mT__42() throws RecognitionException {
        try {
            int _type = T__42;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:40:7: ( 'nob' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:40:9: 'nob'
            {
            match("nob"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__42"

    // $ANTLR start "T__43"
    public final void mT__43() throws RecognitionException {
        try {
            int _type = T__43;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:41:7: ( 'nobreak' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:41:9: 'nobreak'
            {
            match("nobreak"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__43"

    // $ANTLR start "T__44"
    public final void mT__44() throws RecognitionException {
        try {
            int _type = T__44;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:42:7: ( 'not' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:42:9: 'not'
            {
            match("not"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__44"

    // $ANTLR start "T__45"
    public final void mT__45() throws RecognitionException {
        try {
            int _type = T__45;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:43:7: ( 'notrace' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:43:9: 'notrace'
            {
            match("notrace"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__45"

    // $ANTLR start "T__46"
    public final void mT__46() throws RecognitionException {
        try {
            int _type = T__46;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:44:7: ( 'prog' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:44:9: 'prog'
            {
            match("prog"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__46"

    // $ANTLR start "T__47"
    public final void mT__47() throws RecognitionException {
        try {
            int _type = T__47;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:45:7: ( 'quit' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:45:9: 'quit'
            {
            match("quit"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__47"

    // $ANTLR start "T__48"
    public final void mT__48() throws RecognitionException {
        try {
            int _type = T__48;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:46:7: ( 'r' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:46:9: 'r'
            {
            match('r'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__48"

    // $ANTLR start "T__49"
    public final void mT__49() throws RecognitionException {
        try {
            int _type = T__49;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:47:7: ( 'reg' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:47:9: 'reg'
            {
            match("reg"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__49"

    // $ANTLR start "T__50"
    public final void mT__50() throws RecognitionException {
        try {
            int _type = T__50;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:48:7: ( 'run' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:48:9: 'run'
            {
            match("run"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__50"

    // $ANTLR start "T__51"
    public final void mT__51() throws RecognitionException {
        try {
            int _type = T__51;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:49:7: ( 's' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:49:9: 's'
            {
            match('s'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__51"

    // $ANTLR start "T__52"
    public final void mT__52() throws RecognitionException {
        try {
            int _type = T__52;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:50:7: ( 'step' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:50:9: 'step'
            {
            match("step"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__52"

    // $ANTLR start "T__53"
    public final void mT__53() throws RecognitionException {
        try {
            int _type = T__53;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:51:7: ( 't' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:51:9: 't'
            {
            match('t'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__53"

    // $ANTLR start "T__54"
    public final void mT__54() throws RecognitionException {
        try {
            int _type = T__54;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:52:7: ( 'test' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:52:9: 'test'
            {
            match("test"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__54"

    // $ANTLR start "T__55"
    public final void mT__55() throws RecognitionException {
        try {
            int _type = T__55;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:53:7: ( 'trace' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:53:9: 'trace'
            {
            match("trace"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__55"

    // $ANTLR start "T__56"
    public final void mT__56() throws RecognitionException {
        try {
            int _type = T__56;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:54:7: ( 'w' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:54:9: 'w'
            {
            match('w'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__56"

    // $ANTLR start "T__57"
    public final void mT__57() throws RecognitionException {
        try {
            int _type = T__57;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:55:7: ( 'where' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:55:9: 'where'
            {
            match("where"); 



            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__57"

    // $ANTLR start "T__58"
    public final void mT__58() throws RecognitionException {
        try {
            int _type = T__58;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:56:7: ( 'x' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:56:9: 'x'
            {
            match('x'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "T__58"

    // $ANTLR start "String"
    public final void mString() throws RecognitionException {
        try {
            int _type = String;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            int a;

            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:173:9: ( '\"' a= ( (~ '\"' )+ ) '\"' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:173:11: '\"' a= ( (~ '\"' )+ ) '\"'
            {
            match('\"'); 

            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:173:17: ( (~ '\"' )+ )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:173:18: (~ '\"' )+
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:173:18: (~ '\"' )+
            int cnt1=0;
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= '\u0000' && LA1_0 <= '!')||(LA1_0 >= '#' && LA1_0 <= '\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt1 >= 1 ) break loop1;
                        EarlyExitException eee =
                            new EarlyExitException(1, input);
                        throw eee;
                }
                cnt1++;
            } while (true);


            }


            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "String"

    // $ANTLR start "Register"
    public final void mRegister() throws RecognitionException {
        try {
            int _type = Register;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:174:9: ( '%' Identifier )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:174:11: '%' Identifier
            {
            match('%'); 

            mIdentifier(); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Register"

    // $ANTLR start "Identifier"
    public final void mIdentifier() throws RecognitionException {
        try {
            int _type = Identifier;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:176:2: ( Character ( Character | Digit )* )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:176:4: Character ( Character | Digit )*
            {
            mCharacter(); 


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:176:14: ( Character | Digit )*
            loop2:
            do {
                int alt2=3;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '#' && LA2_0 <= '$')||(LA2_0 >= '-' && LA2_0 <= '/')||(LA2_0 >= 'A' && LA2_0 <= 'Z')||LA2_0=='\\'||LA2_0=='_'||(LA2_0 >= 'a' && LA2_0 <= 'z')) ) {
                    alt2=1;
                }
                else if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
                    alt2=2;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:176:15: Character
            	    {
            	    mCharacter(); 


            	    }
            	    break;
            	case 2 :
            	    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:176:25: Digit
            	    {
            	    mDigit(); 


            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Identifier"

    // $ANTLR start "Decimal"
    public final void mDecimal() throws RecognitionException {
        try {
            int _type = Decimal;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:177:9: ( ( Digit )+ )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:177:11: ( Digit )+
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:177:11: ( Digit )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Decimal"

    // $ANTLR start "Hex"
    public final void mHex() throws RecognitionException {
        try {
            int _type = Hex;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:178:5: ( '0' ( 'x' | 'X' ) ( HexDigit )+ )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:178:7: '0' ( 'x' | 'X' ) ( HexDigit )+
            {
            match('0'); 

            if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:178:21: ( HexDigit )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0 >= '0' && LA4_0 <= '9')||(LA4_0 >= 'A' && LA4_0 <= 'F')||(LA4_0 >= 'a' && LA4_0 <= 'f')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:
            	    {
            	    if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Hex"

    // $ANTLR start "HexDigit"
    public final void mHexDigit() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:181:9: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "HexDigit"

    // $ANTLR start "Digit"
    public final void mDigit() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:183:7: ( ( '0' .. '9' ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:
            {
            if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Digit"

    // $ANTLR start "Character"
    public final void mCharacter() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:186:2: ( 'A' .. 'Z' | 'a' .. 'z' | '_' | '/' | '#' | '$' | '-' | '.' | EscapeSpace )
            int alt5=9;
            switch ( input.LA(1) ) {
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'G':
            case 'H':
            case 'I':
            case 'J':
            case 'K':
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
                {
                alt5=1;
                }
                break;
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'g':
            case 'h':
            case 'i':
            case 'j':
            case 'k':
            case 'l':
            case 'm':
            case 'n':
            case 'o':
            case 'p':
            case 'q':
            case 'r':
            case 's':
            case 't':
            case 'u':
            case 'v':
            case 'w':
            case 'x':
            case 'y':
            case 'z':
                {
                alt5=2;
                }
                break;
            case '_':
                {
                alt5=3;
                }
                break;
            case '/':
                {
                alt5=4;
                }
                break;
            case '#':
                {
                alt5=5;
                }
                break;
            case '$':
                {
                alt5=6;
                }
                break;
            case '-':
                {
                alt5=7;
                }
                break;
            case '.':
                {
                alt5=8;
                }
                break;
            case '\\':
                {
                alt5=9;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }

            switch (alt5) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:186:4: 'A' .. 'Z'
                    {
                    matchRange('A','Z'); 

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:186:15: 'a' .. 'z'
                    {
                    matchRange('a','z'); 

                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:186:26: '_'
                    {
                    match('_'); 

                    }
                    break;
                case 4 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:186:32: '/'
                    {
                    match('/'); 

                    }
                    break;
                case 5 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:186:38: '#'
                    {
                    match('#'); 

                    }
                    break;
                case 6 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:186:44: '$'
                    {
                    match('$'); 

                    }
                    break;
                case 7 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:186:50: '-'
                    {
                    match('-'); 

                    }
                    break;
                case 8 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:186:56: '.'
                    {
                    match('.'); 

                    }
                    break;
                case 9 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:186:62: EscapeSpace
                    {
                    mEscapeSpace(); 


                    }
                    break;

            }

        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Character"

    // $ANTLR start "EscapeSpace"
    public final void mEscapeSpace() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:189:9: ( '\\\\ ' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:189:17: '\\\\ '
            {
            match("\\ "); 



            }


        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "EscapeSpace"

    // $ANTLR start "Comment"
    public final void mComment() throws RecognitionException {
        try {
            int _type = Comment;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:189:9: ( '#' ( (~ ( '\\n' | '\\r' ) )* NewLine ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:189:11: '#' ( (~ ( '\\n' | '\\r' ) )* NewLine )
            {
            match('#'); 

            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:189:15: ( (~ ( '\\n' | '\\r' ) )* NewLine )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:189:17: (~ ( '\\n' | '\\r' ) )* NewLine
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:189:17: (~ ( '\\n' | '\\r' ) )*
            loop6:
            do {
                int alt6=2;
                int LA6_0 = input.LA(1);

                if ( ((LA6_0 >= '\u0000' && LA6_0 <= '\t')||(LA6_0 >= '\u000B' && LA6_0 <= '\f')||(LA6_0 >= '\u000E' && LA6_0 <= '\uFFFF')) ) {
                    alt6=1;
                }


                switch (alt6) {
            	case 1 :
            	    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:
            	    {
            	    if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop6;
                }
            } while (true);


            mNewLine(); 


            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "Comment"

    // $ANTLR start "NewLine"
    public final void mNewLine() throws RecognitionException {
        try {
            int _type = NewLine;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:190:9: ( ( '\\r' )? '\\n' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:190:11: ( '\\r' )? '\\n'
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:190:11: ( '\\r' )?
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0=='\r') ) {
                alt7=1;
            }
            switch (alt7) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:190:11: '\\r'
                    {
                    match('\r'); 

                    }
                    break;

            }


            match('\n'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "NewLine"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:191:6: ( ( ' ' | '\\t' )+ )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:191:11: ( ' ' | '\\t' )+
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:191:11: ( ' ' | '\\t' )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( (LA8_0=='\t'||LA8_0==' ') ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:
            	    {
            	    if ( input.LA(1)=='\t'||input.LA(1)==' ' ) {
            	        input.consume();
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        	// do for sure before leaving
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:8: ( T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | String | Register | Identifier | Decimal | Hex | Comment | NewLine | WS )
        int alt9=51;
        alt9 = dfa9.predict(input);
        switch (alt9) {
            case 1 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:10: T__16
                {
                mT__16(); 


                }
                break;
            case 2 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:16: T__17
                {
                mT__17(); 


                }
                break;
            case 3 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:22: T__18
                {
                mT__18(); 


                }
                break;
            case 4 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:28: T__19
                {
                mT__19(); 


                }
                break;
            case 5 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:34: T__20
                {
                mT__20(); 


                }
                break;
            case 6 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:40: T__21
                {
                mT__21(); 


                }
                break;
            case 7 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:46: T__22
                {
                mT__22(); 


                }
                break;
            case 8 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:52: T__23
                {
                mT__23(); 


                }
                break;
            case 9 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:58: T__24
                {
                mT__24(); 


                }
                break;
            case 10 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:64: T__25
                {
                mT__25(); 


                }
                break;
            case 11 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:70: T__26
                {
                mT__26(); 


                }
                break;
            case 12 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:76: T__27
                {
                mT__27(); 


                }
                break;
            case 13 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:82: T__28
                {
                mT__28(); 


                }
                break;
            case 14 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:88: T__29
                {
                mT__29(); 


                }
                break;
            case 15 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:94: T__30
                {
                mT__30(); 


                }
                break;
            case 16 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:100: T__31
                {
                mT__31(); 


                }
                break;
            case 17 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:106: T__32
                {
                mT__32(); 


                }
                break;
            case 18 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:112: T__33
                {
                mT__33(); 


                }
                break;
            case 19 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:118: T__34
                {
                mT__34(); 


                }
                break;
            case 20 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:124: T__35
                {
                mT__35(); 


                }
                break;
            case 21 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:130: T__36
                {
                mT__36(); 


                }
                break;
            case 22 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:136: T__37
                {
                mT__37(); 


                }
                break;
            case 23 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:142: T__38
                {
                mT__38(); 


                }
                break;
            case 24 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:148: T__39
                {
                mT__39(); 


                }
                break;
            case 25 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:154: T__40
                {
                mT__40(); 


                }
                break;
            case 26 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:160: T__41
                {
                mT__41(); 


                }
                break;
            case 27 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:166: T__42
                {
                mT__42(); 


                }
                break;
            case 28 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:172: T__43
                {
                mT__43(); 


                }
                break;
            case 29 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:178: T__44
                {
                mT__44(); 


                }
                break;
            case 30 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:184: T__45
                {
                mT__45(); 


                }
                break;
            case 31 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:190: T__46
                {
                mT__46(); 


                }
                break;
            case 32 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:196: T__47
                {
                mT__47(); 


                }
                break;
            case 33 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:202: T__48
                {
                mT__48(); 


                }
                break;
            case 34 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:208: T__49
                {
                mT__49(); 


                }
                break;
            case 35 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:214: T__50
                {
                mT__50(); 


                }
                break;
            case 36 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:220: T__51
                {
                mT__51(); 


                }
                break;
            case 37 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:226: T__52
                {
                mT__52(); 


                }
                break;
            case 38 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:232: T__53
                {
                mT__53(); 


                }
                break;
            case 39 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:238: T__54
                {
                mT__54(); 


                }
                break;
            case 40 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:244: T__55
                {
                mT__55(); 


                }
                break;
            case 41 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:250: T__56
                {
                mT__56(); 


                }
                break;
            case 42 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:256: T__57
                {
                mT__57(); 


                }
                break;
            case 43 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:262: T__58
                {
                mT__58(); 


                }
                break;
            case 44 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:268: String
                {
                mString(); 


                }
                break;
            case 45 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:275: Register
                {
                mRegister(); 


                }
                break;
            case 46 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:284: Identifier
                {
                mIdentifier(); 


                }
                break;
            case 47 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:295: Decimal
                {
                mDecimal(); 


                }
                break;
            case 48 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:303: Hex
                {
                mHex(); 


                }
                break;
            case 49 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:307: Comment
                {
                mComment(); 


                }
                break;
            case 50 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:315: NewLine
                {
                mNewLine(); 


                }
                break;
            case 51 :
                // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:1:323: WS
                {
                mWS(); 


                }
                break;

        }

    }


    protected DFA9 dfa9 = new DFA9(this);
    static final String DFA9_eotS =
        "\2\uffff\1\37\2\uffff\1\40\1\42\2\31\1\50\1\52\1\31\1\56\1\60\1"+
        "\62\3\31\1\70\1\72\1\75\1\77\1\100\3\uffff\1\31\1\34\5\uffff\1\31"+
        "\1\uffff\5\31\1\uffff\1\31\1\uffff\3\31\1\uffff\1\31\1\uffff\1\31"+
        "\1\uffff\5\31\1\uffff\1\31\1\uffff\2\31\1\uffff\1\31\2\uffff\10"+
        "\31\1\uffff\1\31\2\uffff\2\31\1\147\1\150\1\151\3\31\1\155\1\31"+
        "\1\157\1\31\1\161\1\163\1\165\2\31\1\170\1\171\7\31\3\uffff\1\31"+
        "\1\u0081\1\u0082\1\uffff\1\u0084\1\uffff\1\u0085\1\uffff\1\31\1"+
        "\uffff\1\31\1\uffff\1\u0088\1\u0089\2\uffff\1\u008a\1\u008b\2\31"+
        "\1\u008e\1\u008f\1\31\2\uffff\1\31\2\uffff\2\31\4\uffff\1\u0094"+
        "\1\u0095\2\uffff\1\31\1\u0097\2\31\2\uffff\1\u009b\1\uffff\1\u009c"+
        "\1\u009d\1\31\3\uffff\1\u009f\1\uffff";
    static final String DFA9_eofS =
        "\u00a0\uffff";
    static final String DFA9_minS =
        "\1\11\1\uffff\1\43\2\uffff\2\43\1\154\1\141\2\43\1\145\3\43\1\157"+
        "\1\162\1\165\5\43\3\uffff\1\0\1\130\5\uffff\1\145\1\uffff\1\145"+
        "\1\165\1\164\1\170\1\141\1\uffff\1\164\1\uffff\1\154\1\170\1\146"+
        "\1\uffff\1\141\1\uffff\1\155\1\uffff\1\142\1\157\1\151\1\147\1\156"+
        "\1\uffff\1\145\1\uffff\1\163\1\141\1\uffff\1\145\2\uffff\12\0\2"+
        "\uffff\2\141\3\43\1\155\1\157\1\160\1\43\1\157\1\43\1\144\3\43\1"+
        "\147\1\164\2\43\1\160\1\164\1\143\1\162\1\0\1\153\1\162\3\uffff"+
        "\1\151\2\43\1\uffff\1\43\1\uffff\1\43\1\uffff\1\145\1\uffff\1\141"+
        "\1\uffff\2\43\2\uffff\2\43\2\145\2\43\1\156\2\uffff\1\170\2\uffff"+
        "\1\141\1\143\4\uffff\2\43\2\uffff\1\145\1\43\1\153\1\145\2\uffff"+
        "\1\43\1\uffff\2\43\1\170\3\uffff\1\43\1\uffff";
    static final String DFA9_maxS =
        "\1\172\1\uffff\1\172\2\uffff\2\172\1\160\1\141\2\172\1\145\3\172"+
        "\1\157\1\162\1\165\5\172\3\uffff\1\uffff\1\170\5\uffff\1\145\1\uffff"+
        "\1\145\1\165\1\164\1\170\1\141\1\uffff\1\164\1\uffff\1\154\1\170"+
        "\1\163\1\uffff\1\141\1\uffff\1\155\1\uffff\1\164\1\157\1\151\1\147"+
        "\1\156\1\uffff\1\145\1\uffff\1\163\1\141\1\uffff\1\145\2\uffff\12"+
        "\uffff\2\uffff\2\141\3\172\1\155\1\157\1\160\1\172\1\157\1\172\1"+
        "\144\3\172\1\147\1\164\2\172\1\160\1\164\1\143\1\162\1\uffff\1\153"+
        "\1\162\3\uffff\1\151\2\172\1\uffff\1\172\1\uffff\1\172\1\uffff\1"+
        "\145\1\uffff\1\141\1\uffff\2\172\2\uffff\2\172\2\145\2\172\1\156"+
        "\2\uffff\1\170\2\uffff\1\141\1\143\4\uffff\2\172\2\uffff\1\145\1"+
        "\172\1\153\1\145\2\uffff\1\172\1\uffff\2\172\1\170\3\uffff\1\172"+
        "\1\uffff";
    static final String DFA9_acceptS =
        "\1\uffff\1\1\1\uffff\1\3\1\4\22\uffff\1\54\1\55\1\56\2\uffff\1\57"+
        "\1\62\1\63\1\2\1\5\1\uffff\1\6\5\uffff\1\13\1\uffff\1\17\3\uffff"+
        "\1\22\1\uffff\1\27\1\uffff\1\31\5\uffff\1\41\1\uffff\1\44\2\uffff"+
        "\1\46\1\uffff\1\51\1\53\12\uffff\1\61\1\60\32\uffff\1\11\1\12\1"+
        "\14\3\uffff\1\23\1\uffff\1\26\1\uffff\1\32\1\uffff\1\33\1\uffff"+
        "\1\35\2\uffff\1\42\1\43\7\uffff\1\20\1\21\1\uffff\1\24\1\30\2\uffff"+
        "\1\37\1\40\1\45\1\47\2\uffff\1\7\1\10\4\uffff\1\50\1\52\1\uffff"+
        "\1\25\3\uffff\1\15\1\34\1\36\1\uffff\1\16";
    static final String DFA9_specialS =
        "\32\uffff\1\12\46\uffff\1\11\1\5\1\4\1\7\1\6\1\1\1\0\1\3\1\2\1\10"+
        "\31\uffff\1\13\73\uffff}>";
    static final String[] DFA9_transitionS = {
            "\1\36\1\35\2\uffff\1\35\22\uffff\1\36\1\uffff\1\27\1\32\1\31"+
            "\1\30\5\uffff\1\1\1\uffff\1\2\2\31\1\33\11\34\1\3\2\uffff\1"+
            "\4\3\uffff\32\31\1\uffff\1\31\2\uffff\1\31\1\uffff\1\5\1\6\1"+
            "\7\1\10\1\11\1\31\1\12\1\13\1\14\2\31\1\15\1\16\1\17\1\31\1"+
            "\20\1\21\1\22\1\23\1\24\2\31\1\25\1\26\2\31",
            "",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "",
            "",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\21\31\1\41\10\31",
            "\1\43\3\uffff\1\44",
            "\1\45",
            "\2\31\10\uffff\2\31\1\46\12\31\7\uffff\32\31\1\uffff\1\31\2"+
            "\uffff\1\31\1\uffff\27\31\1\47\2\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\16\31\1\51\13\31",
            "\1\53",
            "\2\31\10\uffff\2\31\1\54\12\31\7\uffff\32\31\1\uffff\1\31\2"+
            "\uffff\1\31\1\uffff\15\31\1\55\14\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\16\31\1\57\13\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\4\31\1\61\25\31",
            "\1\63",
            "\1\64",
            "\1\65",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\4\31\1\66\17\31\1\67\5\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\23\31\1\71\6\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\4\31\1\73\14\31\1\74\10\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\7\31\1\76\22\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "",
            "",
            "",
            "\43\113\1\105\1\106\10\113\1\107\1\110\1\104\12\112\7\113\32"+
            "\101\1\113\1\111\2\113\1\103\1\113\32\102\uff85\113",
            "\1\114\37\uffff\1\114",
            "",
            "",
            "",
            "",
            "",
            "\1\115",
            "",
            "\1\116",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "",
            "\1\123",
            "",
            "\1\124",
            "\1\125",
            "\1\126\14\uffff\1\127",
            "",
            "\1\130",
            "",
            "\1\131",
            "",
            "\1\132\21\uffff\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "",
            "\1\140",
            "",
            "\1\141",
            "\1\142",
            "",
            "\1\143",
            "",
            "",
            "\43\113\1\105\1\106\10\113\1\107\1\110\1\104\12\112\7\113\32"+
            "\101\1\113\1\111\2\113\1\103\1\113\32\102\uff85\113",
            "\43\113\1\105\1\106\10\113\1\107\1\110\1\104\12\112\7\113\32"+
            "\101\1\113\1\111\2\113\1\103\1\113\32\102\uff85\113",
            "\43\113\1\105\1\106\10\113\1\107\1\110\1\104\12\112\7\113\32"+
            "\101\1\113\1\111\2\113\1\103\1\113\32\102\uff85\113",
            "\43\113\1\105\1\106\10\113\1\107\1\110\1\104\12\112\7\113\32"+
            "\101\1\113\1\111\2\113\1\103\1\113\32\102\uff85\113",
            "\43\113\1\105\1\106\10\113\1\107\1\110\1\104\12\112\7\113\32"+
            "\101\1\113\1\111\2\113\1\103\1\113\32\102\uff85\113",
            "\43\113\1\105\1\106\10\113\1\107\1\110\1\104\12\112\7\113\32"+
            "\101\1\113\1\111\2\113\1\103\1\113\32\102\uff85\113",
            "\43\113\1\105\1\106\10\113\1\107\1\110\1\104\12\112\7\113\32"+
            "\101\1\113\1\111\2\113\1\103\1\113\32\102\uff85\113",
            "\43\113\1\105\1\106\10\113\1\107\1\110\1\104\12\112\7\113\32"+
            "\101\1\113\1\111\2\113\1\103\1\113\32\102\uff85\113",
            "\40\113\1\144\uffdf\113",
            "\43\113\1\105\1\106\10\113\1\107\1\110\1\104\12\112\7\113\32"+
            "\101\1\113\1\111\2\113\1\103\1\113\32\102\uff85\113",
            "",
            "",
            "\1\145",
            "\1\146",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\1\152",
            "\1\153",
            "\1\154",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\1\156",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\1\160",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\21\31\1\162\10\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\21\31\1\164\10\31",
            "\1\166",
            "\1\167",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\1\172",
            "\1\173",
            "\1\174",
            "\1\175",
            "\43\113\1\105\1\106\10\113\1\107\1\110\1\104\12\112\7\113\32"+
            "\101\1\113\1\111\2\113\1\103\1\113\32\102\uff85\113",
            "\1\176",
            "\1\177",
            "",
            "",
            "",
            "\1\u0080",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "",
            "\2\31\10\uffff\2\31\1\u0083\12\31\7\uffff\32\31\1\uffff\1\31"+
            "\2\uffff\1\31\1\uffff\32\31",
            "",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "",
            "\1\u0086",
            "",
            "\1\u0087",
            "",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "",
            "",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\1\u008c",
            "\1\u008d",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\1\u0090",
            "",
            "",
            "\1\u0091",
            "",
            "",
            "\1\u0092",
            "\1\u0093",
            "",
            "",
            "",
            "",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "",
            "",
            "\1\u0096",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\1\u0098",
            "\1\u0099",
            "",
            "",
            "\2\31\10\uffff\2\31\1\u009a\12\31\7\uffff\32\31\1\uffff\1\31"+
            "\2\uffff\1\31\1\uffff\32\31",
            "",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            "\1\u009e",
            "",
            "",
            "",
            "\2\31\10\uffff\15\31\7\uffff\32\31\1\uffff\1\31\2\uffff\1\31"+
            "\1\uffff\32\31",
            ""
    };

    static final short[] DFA9_eot = DFA.unpackEncodedString(DFA9_eotS);
    static final short[] DFA9_eof = DFA.unpackEncodedString(DFA9_eofS);
    static final char[] DFA9_min = DFA.unpackEncodedStringToUnsignedChars(DFA9_minS);
    static final char[] DFA9_max = DFA.unpackEncodedStringToUnsignedChars(DFA9_maxS);
    static final short[] DFA9_accept = DFA.unpackEncodedString(DFA9_acceptS);
    static final short[] DFA9_special = DFA.unpackEncodedString(DFA9_specialS);
    static final short[][] DFA9_transition;

    static {
        int numStates = DFA9_transitionS.length;
        DFA9_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA9_transition[i] = DFA.unpackEncodedString(DFA9_transitionS[i]);
        }
    }

    class DFA9 extends DFA {

        public DFA9(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 9;
            this.eot = DFA9_eot;
            this.eof = DFA9_eof;
            this.min = DFA9_min;
            this.max = DFA9_max;
            this.accept = DFA9_accept;
            this.special = DFA9_special;
            this.transition = DFA9_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__16 | T__17 | T__18 | T__19 | T__20 | T__21 | T__22 | T__23 | T__24 | T__25 | T__26 | T__27 | T__28 | T__29 | T__30 | T__31 | T__32 | T__33 | T__34 | T__35 | T__36 | T__37 | T__38 | T__39 | T__40 | T__41 | T__42 | T__43 | T__44 | T__45 | T__46 | T__47 | T__48 | T__49 | T__50 | T__51 | T__52 | T__53 | T__54 | T__55 | T__56 | T__57 | T__58 | String | Register | Identifier | Decimal | Hex | Comment | NewLine | WS );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA9_71 = input.LA(1);

                        s = -1;
                        if ( ((LA9_71 >= 'A' && LA9_71 <= 'Z')) ) {s = 65;}

                        else if ( ((LA9_71 >= 'a' && LA9_71 <= 'z')) ) {s = 66;}

                        else if ( (LA9_71=='_') ) {s = 67;}

                        else if ( (LA9_71=='/') ) {s = 68;}

                        else if ( (LA9_71=='#') ) {s = 69;}

                        else if ( (LA9_71=='$') ) {s = 70;}

                        else if ( (LA9_71=='-') ) {s = 71;}

                        else if ( (LA9_71=='.') ) {s = 72;}

                        else if ( (LA9_71=='\\') ) {s = 73;}

                        else if ( ((LA9_71 >= '0' && LA9_71 <= '9')) ) {s = 74;}

                        else if ( ((LA9_71 >= '\u0000' && LA9_71 <= '\"')||(LA9_71 >= '%' && LA9_71 <= ',')||(LA9_71 >= ':' && LA9_71 <= '@')||LA9_71=='['||(LA9_71 >= ']' && LA9_71 <= '^')||LA9_71=='`'||(LA9_71 >= '{' && LA9_71 <= '\uFFFF')) ) {s = 75;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 1 : 
                        int LA9_70 = input.LA(1);

                        s = -1;
                        if ( ((LA9_70 >= 'A' && LA9_70 <= 'Z')) ) {s = 65;}

                        else if ( ((LA9_70 >= 'a' && LA9_70 <= 'z')) ) {s = 66;}

                        else if ( (LA9_70=='_') ) {s = 67;}

                        else if ( (LA9_70=='/') ) {s = 68;}

                        else if ( (LA9_70=='#') ) {s = 69;}

                        else if ( (LA9_70=='$') ) {s = 70;}

                        else if ( (LA9_70=='-') ) {s = 71;}

                        else if ( (LA9_70=='.') ) {s = 72;}

                        else if ( (LA9_70=='\\') ) {s = 73;}

                        else if ( ((LA9_70 >= '0' && LA9_70 <= '9')) ) {s = 74;}

                        else if ( ((LA9_70 >= '\u0000' && LA9_70 <= '\"')||(LA9_70 >= '%' && LA9_70 <= ',')||(LA9_70 >= ':' && LA9_70 <= '@')||LA9_70=='['||(LA9_70 >= ']' && LA9_70 <= '^')||LA9_70=='`'||(LA9_70 >= '{' && LA9_70 <= '\uFFFF')) ) {s = 75;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 2 : 
                        int LA9_73 = input.LA(1);

                        s = -1;
                        if ( (LA9_73==' ') ) {s = 100;}

                        else if ( ((LA9_73 >= '\u0000' && LA9_73 <= '\u001F')||(LA9_73 >= '!' && LA9_73 <= '\uFFFF')) ) {s = 75;}

                        if ( s>=0 ) return s;
                        break;

                    case 3 : 
                        int LA9_72 = input.LA(1);

                        s = -1;
                        if ( ((LA9_72 >= 'A' && LA9_72 <= 'Z')) ) {s = 65;}

                        else if ( ((LA9_72 >= 'a' && LA9_72 <= 'z')) ) {s = 66;}

                        else if ( (LA9_72=='_') ) {s = 67;}

                        else if ( (LA9_72=='/') ) {s = 68;}

                        else if ( (LA9_72=='#') ) {s = 69;}

                        else if ( (LA9_72=='$') ) {s = 70;}

                        else if ( (LA9_72=='-') ) {s = 71;}

                        else if ( (LA9_72=='.') ) {s = 72;}

                        else if ( (LA9_72=='\\') ) {s = 73;}

                        else if ( ((LA9_72 >= '0' && LA9_72 <= '9')) ) {s = 74;}

                        else if ( ((LA9_72 >= '\u0000' && LA9_72 <= '\"')||(LA9_72 >= '%' && LA9_72 <= ',')||(LA9_72 >= ':' && LA9_72 <= '@')||LA9_72=='['||(LA9_72 >= ']' && LA9_72 <= '^')||LA9_72=='`'||(LA9_72 >= '{' && LA9_72 <= '\uFFFF')) ) {s = 75;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 4 : 
                        int LA9_67 = input.LA(1);

                        s = -1;
                        if ( ((LA9_67 >= 'A' && LA9_67 <= 'Z')) ) {s = 65;}

                        else if ( ((LA9_67 >= 'a' && LA9_67 <= 'z')) ) {s = 66;}

                        else if ( (LA9_67=='_') ) {s = 67;}

                        else if ( (LA9_67=='/') ) {s = 68;}

                        else if ( (LA9_67=='#') ) {s = 69;}

                        else if ( (LA9_67=='$') ) {s = 70;}

                        else if ( (LA9_67=='-') ) {s = 71;}

                        else if ( (LA9_67=='.') ) {s = 72;}

                        else if ( (LA9_67=='\\') ) {s = 73;}

                        else if ( ((LA9_67 >= '0' && LA9_67 <= '9')) ) {s = 74;}

                        else if ( ((LA9_67 >= '\u0000' && LA9_67 <= '\"')||(LA9_67 >= '%' && LA9_67 <= ',')||(LA9_67 >= ':' && LA9_67 <= '@')||LA9_67=='['||(LA9_67 >= ']' && LA9_67 <= '^')||LA9_67=='`'||(LA9_67 >= '{' && LA9_67 <= '\uFFFF')) ) {s = 75;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 5 : 
                        int LA9_66 = input.LA(1);

                        s = -1;
                        if ( ((LA9_66 >= 'A' && LA9_66 <= 'Z')) ) {s = 65;}

                        else if ( ((LA9_66 >= 'a' && LA9_66 <= 'z')) ) {s = 66;}

                        else if ( (LA9_66=='_') ) {s = 67;}

                        else if ( (LA9_66=='/') ) {s = 68;}

                        else if ( (LA9_66=='#') ) {s = 69;}

                        else if ( (LA9_66=='$') ) {s = 70;}

                        else if ( (LA9_66=='-') ) {s = 71;}

                        else if ( (LA9_66=='.') ) {s = 72;}

                        else if ( (LA9_66=='\\') ) {s = 73;}

                        else if ( ((LA9_66 >= '0' && LA9_66 <= '9')) ) {s = 74;}

                        else if ( ((LA9_66 >= '\u0000' && LA9_66 <= '\"')||(LA9_66 >= '%' && LA9_66 <= ',')||(LA9_66 >= ':' && LA9_66 <= '@')||LA9_66=='['||(LA9_66 >= ']' && LA9_66 <= '^')||LA9_66=='`'||(LA9_66 >= '{' && LA9_66 <= '\uFFFF')) ) {s = 75;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 6 : 
                        int LA9_69 = input.LA(1);

                        s = -1;
                        if ( ((LA9_69 >= 'A' && LA9_69 <= 'Z')) ) {s = 65;}

                        else if ( ((LA9_69 >= 'a' && LA9_69 <= 'z')) ) {s = 66;}

                        else if ( (LA9_69=='_') ) {s = 67;}

                        else if ( (LA9_69=='/') ) {s = 68;}

                        else if ( (LA9_69=='#') ) {s = 69;}

                        else if ( (LA9_69=='$') ) {s = 70;}

                        else if ( (LA9_69=='-') ) {s = 71;}

                        else if ( (LA9_69=='.') ) {s = 72;}

                        else if ( (LA9_69=='\\') ) {s = 73;}

                        else if ( ((LA9_69 >= '0' && LA9_69 <= '9')) ) {s = 74;}

                        else if ( ((LA9_69 >= '\u0000' && LA9_69 <= '\"')||(LA9_69 >= '%' && LA9_69 <= ',')||(LA9_69 >= ':' && LA9_69 <= '@')||LA9_69=='['||(LA9_69 >= ']' && LA9_69 <= '^')||LA9_69=='`'||(LA9_69 >= '{' && LA9_69 <= '\uFFFF')) ) {s = 75;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 7 : 
                        int LA9_68 = input.LA(1);

                        s = -1;
                        if ( ((LA9_68 >= 'A' && LA9_68 <= 'Z')) ) {s = 65;}

                        else if ( ((LA9_68 >= 'a' && LA9_68 <= 'z')) ) {s = 66;}

                        else if ( (LA9_68=='_') ) {s = 67;}

                        else if ( (LA9_68=='/') ) {s = 68;}

                        else if ( (LA9_68=='#') ) {s = 69;}

                        else if ( (LA9_68=='$') ) {s = 70;}

                        else if ( (LA9_68=='-') ) {s = 71;}

                        else if ( (LA9_68=='.') ) {s = 72;}

                        else if ( (LA9_68=='\\') ) {s = 73;}

                        else if ( ((LA9_68 >= '0' && LA9_68 <= '9')) ) {s = 74;}

                        else if ( ((LA9_68 >= '\u0000' && LA9_68 <= '\"')||(LA9_68 >= '%' && LA9_68 <= ',')||(LA9_68 >= ':' && LA9_68 <= '@')||LA9_68=='['||(LA9_68 >= ']' && LA9_68 <= '^')||LA9_68=='`'||(LA9_68 >= '{' && LA9_68 <= '\uFFFF')) ) {s = 75;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 8 : 
                        int LA9_74 = input.LA(1);

                        s = -1;
                        if ( ((LA9_74 >= 'A' && LA9_74 <= 'Z')) ) {s = 65;}

                        else if ( ((LA9_74 >= 'a' && LA9_74 <= 'z')) ) {s = 66;}

                        else if ( (LA9_74=='_') ) {s = 67;}

                        else if ( (LA9_74=='/') ) {s = 68;}

                        else if ( (LA9_74=='#') ) {s = 69;}

                        else if ( (LA9_74=='$') ) {s = 70;}

                        else if ( (LA9_74=='-') ) {s = 71;}

                        else if ( (LA9_74=='.') ) {s = 72;}

                        else if ( (LA9_74=='\\') ) {s = 73;}

                        else if ( ((LA9_74 >= '0' && LA9_74 <= '9')) ) {s = 74;}

                        else if ( ((LA9_74 >= '\u0000' && LA9_74 <= '\"')||(LA9_74 >= '%' && LA9_74 <= ',')||(LA9_74 >= ':' && LA9_74 <= '@')||LA9_74=='['||(LA9_74 >= ']' && LA9_74 <= '^')||LA9_74=='`'||(LA9_74 >= '{' && LA9_74 <= '\uFFFF')) ) {s = 75;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 9 : 
                        int LA9_65 = input.LA(1);

                        s = -1;
                        if ( ((LA9_65 >= 'A' && LA9_65 <= 'Z')) ) {s = 65;}

                        else if ( ((LA9_65 >= 'a' && LA9_65 <= 'z')) ) {s = 66;}

                        else if ( (LA9_65=='_') ) {s = 67;}

                        else if ( (LA9_65=='/') ) {s = 68;}

                        else if ( (LA9_65=='#') ) {s = 69;}

                        else if ( (LA9_65=='$') ) {s = 70;}

                        else if ( (LA9_65=='-') ) {s = 71;}

                        else if ( (LA9_65=='.') ) {s = 72;}

                        else if ( (LA9_65=='\\') ) {s = 73;}

                        else if ( ((LA9_65 >= '0' && LA9_65 <= '9')) ) {s = 74;}

                        else if ( ((LA9_65 >= '\u0000' && LA9_65 <= '\"')||(LA9_65 >= '%' && LA9_65 <= ',')||(LA9_65 >= ':' && LA9_65 <= '@')||LA9_65=='['||(LA9_65 >= ']' && LA9_65 <= '^')||LA9_65=='`'||(LA9_65 >= '{' && LA9_65 <= '\uFFFF')) ) {s = 75;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 10 : 
                        int LA9_26 = input.LA(1);

                        s = -1;
                        if ( ((LA9_26 >= 'A' && LA9_26 <= 'Z')) ) {s = 65;}

                        else if ( ((LA9_26 >= 'a' && LA9_26 <= 'z')) ) {s = 66;}

                        else if ( (LA9_26=='_') ) {s = 67;}

                        else if ( (LA9_26=='/') ) {s = 68;}

                        else if ( (LA9_26=='#') ) {s = 69;}

                        else if ( (LA9_26=='$') ) {s = 70;}

                        else if ( (LA9_26=='-') ) {s = 71;}

                        else if ( (LA9_26=='.') ) {s = 72;}

                        else if ( (LA9_26=='\\') ) {s = 73;}

                        else if ( ((LA9_26 >= '0' && LA9_26 <= '9')) ) {s = 74;}

                        else if ( ((LA9_26 >= '\u0000' && LA9_26 <= '\"')||(LA9_26 >= '%' && LA9_26 <= ',')||(LA9_26 >= ':' && LA9_26 <= '@')||LA9_26=='['||(LA9_26 >= ']' && LA9_26 <= '^')||LA9_26=='`'||(LA9_26 >= '{' && LA9_26 <= '\uFFFF')) ) {s = 75;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;

                    case 11 : 
                        int LA9_100 = input.LA(1);

                        s = -1;
                        if ( ((LA9_100 >= 'A' && LA9_100 <= 'Z')) ) {s = 65;}

                        else if ( ((LA9_100 >= 'a' && LA9_100 <= 'z')) ) {s = 66;}

                        else if ( (LA9_100=='_') ) {s = 67;}

                        else if ( (LA9_100=='/') ) {s = 68;}

                        else if ( (LA9_100=='#') ) {s = 69;}

                        else if ( (LA9_100=='$') ) {s = 70;}

                        else if ( (LA9_100=='-') ) {s = 71;}

                        else if ( (LA9_100=='.') ) {s = 72;}

                        else if ( (LA9_100=='\\') ) {s = 73;}

                        else if ( ((LA9_100 >= '0' && LA9_100 <= '9')) ) {s = 74;}

                        else if ( ((LA9_100 >= '\u0000' && LA9_100 <= '\"')||(LA9_100 >= '%' && LA9_100 <= ',')||(LA9_100 >= ':' && LA9_100 <= '@')||LA9_100=='['||(LA9_100 >= ']' && LA9_100 <= '^')||LA9_100=='`'||(LA9_100 >= '{' && LA9_100 <= '\uFFFF')) ) {s = 75;}

                        else s = 25;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 9, _s, input);
            error(nvae);
            throw nvae;
        }

    }
 

}