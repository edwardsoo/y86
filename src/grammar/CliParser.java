// $ANTLR 3.4 /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g 2012-02-21 14:12:42

package grammar;

import ui.cli.UI;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class CliParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Character", "Comment", "Decimal", "Digit", "EscapeSpace", "Hex", "HexDigit", "Identifier", "NewLine", "Register", "String", "WS", "'+'", "'-'", "':'", "'='", "'a'", "'b'", "'break'", "'clear'", "'cpu'", "'dat'", "'e'", "'e/x'", "'examine'", "'examine/x'", "'g'", "'goto'", "'help'", "'i'", "'i/x'", "'info'", "'info/x'", "'ins'", "'l'", "'load'", "'m'", "'mem'", "'nob'", "'nobreak'", "'not'", "'notrace'", "'prog'", "'quit'", "'r'", "'reg'", "'run'", "'s'", "'step'", "'t'", "'test'", "'trace'", "'w'", "'where'", "'x'"
    };

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

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public CliParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public CliParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return CliParser.tokenNames; }
    public String getGrammarFileName() { return "/Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g"; }



    public class QuitException extends RuntimeException {}
    public class SyntaxErrorException extends RuntimeException {}

    @Override
    public void reportError (RecognitionException re) {
      throw new SyntaxErrorException ();
    }

    public interface CommandHandler {
      public enum MemFormat  {ASM,HEX};
      public enum MemRegion  {INS, DAT, ALL};
      public enum InsOper    {REPLACE,INSERT,DELETE};
      public enum DebugType  {BREAK,TRACE};
      public enum DebugPoint {INSTRUCTION, MEMORY_READ, MEMORY_WRITE, MEMORY_ACCESS, REGISTER_READ, REGISTER_WRITE, REGISTER_ACCESS};
      void load              (String filename);
      void test              (String filename, String bnchArch, String bnchVariant);
      void run               ();
      void step              ();
      void showWhere         ();
      void gotoPC            (int pc);
      void examineMem        (int count, MemFormat format, int addr);
      void examineReg        (int count, int addr);
      void examineMemAll     (MemFormat format, MemRegion region);
      void examineRegAll     ();
      void examineProc       (String state);
      void setReg            (int regNum, int value);
      void setMem            (int addr, int value);
      void setIns            (int addr, InsOper oper, String value);
      void debugPoint        (DebugType type, DebugPoint point, boolean isEnabled, int value);
      void traceProg         (boolean isEnabled);
      void clearDebugPoints  (DebugType type);
      void showDebugPoints   (DebugType type);
      void help              ();
      int  getRegisterNumber (String registerName);
      int  getLabelValue     (String label);
    }

    CommandHandler cmd;

    public void setCommandHandler (CommandHandler aCmd) { 
      cmd = aCmd; 
    }
    boolean dpIsEnabled;



    // $ANTLR start "command"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:1: command : ( load | test | run | step | where | gotoPC | examine | info | set | debug | infoDebug | quit | help ) EOF ;
    public final void command() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:9: ( ( load | test | run | step | where | gotoPC | examine | info | set | debug | infoDebug | quit | help ) EOF )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:11: ( load | test | run | step | where | gotoPC | examine | info | set | debug | infoDebug | quit | help ) EOF
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:11: ( load | test | run | step | where | gotoPC | examine | info | set | debug | infoDebug | quit | help )
            int alt1=13;
            switch ( input.LA(1) ) {
            case 38:
            case 39:
                {
                alt1=1;
                }
                break;
            case 54:
                {
                alt1=2;
                }
                break;
            case 48:
            case 50:
                {
                alt1=3;
                }
                break;
            case 51:
            case 52:
                {
                alt1=4;
                }
                break;
            case 56:
            case 57:
                {
                alt1=5;
                }
                break;
            case 30:
            case 31:
                {
                alt1=6;
                }
                break;
            case 26:
            case 27:
            case 28:
            case 29:
                {
                alt1=7;
                }
                break;
            case 33:
                {
                switch ( input.LA(2) ) {
                case 41:
                    {
                    int LA1_15 = input.LA(3);

                    if ( (LA1_15==EOF) ) {
                        alt1=8;
                    }
                    else if ( ((LA1_15 >= 16 && LA1_15 <= 17)||LA1_15==19) ) {
                        alt1=9;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 15, input);

                        throw nvae;

                    }
                    }
                    break;
                case 37:
                    {
                    int LA1_16 = input.LA(3);

                    if ( (LA1_16==EOF) ) {
                        alt1=8;
                    }
                    else if ( ((LA1_16 >= 16 && LA1_16 <= 17)||LA1_16==19) ) {
                        alt1=9;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 16, input);

                        throw nvae;

                    }
                    }
                    break;
                case 25:
                    {
                    int LA1_17 = input.LA(3);

                    if ( (LA1_17==EOF) ) {
                        alt1=8;
                    }
                    else if ( ((LA1_17 >= 16 && LA1_17 <= 17)||LA1_17==19) ) {
                        alt1=9;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 17, input);

                        throw nvae;

                    }
                    }
                    break;
                case 49:
                    {
                    int LA1_18 = input.LA(3);

                    if ( (LA1_18==EOF) ) {
                        alt1=8;
                    }
                    else if ( ((LA1_18 >= 16 && LA1_18 <= 17)||LA1_18==19) ) {
                        alt1=9;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 18, input);

                        throw nvae;

                    }
                    }
                    break;
                case 24:
                    {
                    int LA1_19 = input.LA(3);

                    if ( (LA1_19==EOF||LA1_19==Identifier||(LA1_19 >= 20 && LA1_19 <= 26)||LA1_19==28||(LA1_19 >= 30 && LA1_19 <= 33)||LA1_19==35||(LA1_19 >= 37 && LA1_19 <= 41)||(LA1_19 >= 46 && LA1_19 <= 58)) ) {
                        alt1=8;
                    }
                    else if ( ((LA1_19 >= 16 && LA1_19 <= 17)||LA1_19==19) ) {
                        alt1=9;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 19, input);

                        throw nvae;

                    }
                    }
                    break;
                case Decimal:
                case Hex:
                case Identifier:
                case 20:
                case 23:
                case 26:
                case 28:
                case 30:
                case 31:
                case 32:
                case 33:
                case 35:
                case 38:
                case 39:
                case 40:
                case 46:
                case 47:
                case 48:
                case 50:
                case 51:
                case 52:
                case 54:
                case 56:
                case 57:
                case 58:
                    {
                    alt1=9;
                    }
                    break;
                case 21:
                case 22:
                    {
                    int LA1_20 = input.LA(3);

                    if ( ((LA1_20 >= 16 && LA1_20 <= 17)||LA1_20==19) ) {
                        alt1=9;
                    }
                    else if ( (LA1_20==EOF) ) {
                        alt1=11;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 20, input);

                        throw nvae;

                    }
                    }
                    break;
                case 53:
                case 55:
                    {
                    int LA1_21 = input.LA(3);

                    if ( ((LA1_21 >= 16 && LA1_21 <= 17)||LA1_21==19) ) {
                        alt1=9;
                    }
                    else if ( (LA1_21==EOF) ) {
                        alt1=11;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 1, 21, input);

                        throw nvae;

                    }
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 8, input);

                    throw nvae;

                }

                }
                break;
            case 35:
                {
                int LA1_9 = input.LA(2);

                if ( ((LA1_9 >= 24 && LA1_9 <= 25)||LA1_9==37||LA1_9==41||LA1_9==49) ) {
                    alt1=8;
                }
                else if ( ((LA1_9 >= 21 && LA1_9 <= 22)||LA1_9==53||LA1_9==55) ) {
                    alt1=11;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 1, 9, input);

                    throw nvae;

                }
                }
                break;
            case 34:
            case 36:
                {
                alt1=8;
                }
                break;
            case Register:
            case 40:
                {
                alt1=9;
                }
                break;
            case 21:
            case 22:
            case 23:
            case 42:
            case 43:
            case 44:
            case 45:
            case 53:
            case 55:
                {
                alt1=10;
                }
                break;
            case 47:
                {
                alt1=12;
                }
                break;
            case 32:
                {
                alt1=13;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;

            }

            switch (alt1) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:12: load
                    {
                    pushFollow(FOLLOW_load_in_command41);
                    load();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:19: test
                    {
                    pushFollow(FOLLOW_test_in_command45);
                    test();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:26: run
                    {
                    pushFollow(FOLLOW_run_in_command49);
                    run();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:32: step
                    {
                    pushFollow(FOLLOW_step_in_command53);
                    step();

                    state._fsp--;


                    }
                    break;
                case 5 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:39: where
                    {
                    pushFollow(FOLLOW_where_in_command57);
                    where();

                    state._fsp--;


                    }
                    break;
                case 6 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:47: gotoPC
                    {
                    pushFollow(FOLLOW_gotoPC_in_command61);
                    gotoPC();

                    state._fsp--;


                    }
                    break;
                case 7 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:56: examine
                    {
                    pushFollow(FOLLOW_examine_in_command65);
                    examine();

                    state._fsp--;


                    }
                    break;
                case 8 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:66: info
                    {
                    pushFollow(FOLLOW_info_in_command69);
                    info();

                    state._fsp--;


                    }
                    break;
                case 9 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:73: set
                    {
                    pushFollow(FOLLOW_set_in_command73);
                    set();

                    state._fsp--;


                    }
                    break;
                case 10 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:79: debug
                    {
                    pushFollow(FOLLOW_debug_in_command77);
                    debug();

                    state._fsp--;


                    }
                    break;
                case 11 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:87: infoDebug
                    {
                    pushFollow(FOLLOW_infoDebug_in_command81);
                    infoDebug();

                    state._fsp--;


                    }
                    break;
                case 12 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:99: quit
                    {
                    pushFollow(FOLLOW_quit_in_command85);
                    quit();

                    state._fsp--;


                    }
                    break;
                case 13 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:69:106: help
                    {
                    pushFollow(FOLLOW_help_in_command89);
                    help();

                    state._fsp--;


                    }
                    break;

            }


            match(input,EOF,FOLLOW_EOF_in_command92); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "command"



    // $ANTLR start "keyword"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:71:1: keyword : ( 'a' | 'm' | 'i' | 'l' | 'x' | 'load' | 'test' | 'e' | 'examine' | 'r' | 'run' | 's' | 'step' | 'w' | 'where' | 'g' | 'goto' | 't' | 'trace' | 'b' | 'break' | 'info' | 'help' | 'quit' | 'mem' | 'ins' | 'dat' | 'reg' | 'cpu' | 'clear' | 'prog' ) ;
    public final void keyword() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:71:9: ( ( 'a' | 'm' | 'i' | 'l' | 'x' | 'load' | 'test' | 'e' | 'examine' | 'r' | 'run' | 's' | 'step' | 'w' | 'where' | 'g' | 'goto' | 't' | 'trace' | 'b' | 'break' | 'info' | 'help' | 'quit' | 'mem' | 'ins' | 'dat' | 'reg' | 'cpu' | 'clear' | 'prog' ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:
            {
            if ( (input.LA(1) >= 20 && input.LA(1) <= 26)||input.LA(1)==28||(input.LA(1) >= 30 && input.LA(1) <= 33)||input.LA(1)==35||(input.LA(1) >= 37 && input.LA(1) <= 41)||(input.LA(1) >= 46 && input.LA(1) <= 58) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "keyword"



    // $ANTLR start "load"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:73:1: load : ( 'l' | 'load' ) f= filename ;
    public final void load() throws RecognitionException {
        CliParser.filename_return f =null;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:73:6: ( ( 'l' | 'load' ) f= filename )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:73:8: ( 'l' | 'load' ) f= filename
            {
            if ( (input.LA(1) >= 38 && input.LA(1) <= 39) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            pushFollow(FOLLOW_filename_in_load178);
            f=filename();

            state._fsp--;


            cmd.load ((f!=null?f.value:null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "load"



    // $ANTLR start "test"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:75:1: test : ( 'test' ) filename (arch= identifier (variant= identifier )? )? ;
    public final void test() throws RecognitionException {
        CliParser.identifier_return arch =null;

        CliParser.identifier_return variant =null;

        CliParser.filename_return filename1 =null;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:75:9: ( ( 'test' ) filename (arch= identifier (variant= identifier )? )? )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:75:17: ( 'test' ) filename (arch= identifier (variant= identifier )? )?
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:75:17: ( 'test' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:75:18: 'test'
            {
            match(input,54,FOLLOW_54_in_test201); 

            }


            pushFollow(FOLLOW_filename_in_test204);
            filename1=filename();

            state._fsp--;


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:75:35: (arch= identifier (variant= identifier )? )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0==Identifier||(LA3_0 >= 20 && LA3_0 <= 26)||LA3_0==28||(LA3_0 >= 30 && LA3_0 <= 33)||LA3_0==35||(LA3_0 >= 37 && LA3_0 <= 41)||(LA3_0 >= 46 && LA3_0 <= 58)) ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:75:36: arch= identifier (variant= identifier )?
                    {
                    pushFollow(FOLLOW_identifier_in_test209);
                    arch=identifier();

                    state._fsp--;


                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:75:59: (variant= identifier )?
                    int alt2=2;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0==Identifier||(LA2_0 >= 20 && LA2_0 <= 26)||LA2_0==28||(LA2_0 >= 30 && LA2_0 <= 33)||LA2_0==35||(LA2_0 >= 37 && LA2_0 <= 41)||(LA2_0 >= 46 && LA2_0 <= 58)) ) {
                        alt2=1;
                    }
                    switch (alt2) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:75:59: variant= identifier
                            {
                            pushFollow(FOLLOW_identifier_in_test213);
                            variant=identifier();

                            state._fsp--;


                            }
                            break;

                    }


                    }
                    break;

            }


            cmd.test ((filename1!=null?input.toString(filename1.start,filename1.stop):null), (arch!=null?input.toString(arch.start,arch.stop):null), (variant!=null?input.toString(variant.start,variant.stop):null));

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "test"



    // $ANTLR start "run"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:77:1: run : ( 'r' | 'run' ) ;
    public final void run() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:77:5: ( ( 'r' | 'run' ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:77:7: ( 'r' | 'run' )
            {
            if ( input.LA(1)==48||input.LA(1)==50 ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


             cmd.run (); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "run"



    // $ANTLR start "step"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:79:1: step : ( 's' | 'step' ) ;
    public final void step() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:79:6: ( ( 's' | 'step' ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:79:8: ( 's' | 'step' )
            {
            if ( (input.LA(1) >= 51 && input.LA(1) <= 52) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


             cmd.step (); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "step"



    // $ANTLR start "where"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:81:1: where : ( 'w' | 'where' ) ;
    public final void where() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:81:8: ( ( 'w' | 'where' ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:81:10: ( 'w' | 'where' )
            {
            if ( (input.LA(1) >= 56 && input.LA(1) <= 57) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


             cmd.showWhere (); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "where"



    // $ANTLR start "gotoPC"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:83:1: gotoPC : ( 'g' | 'goto' ) pc= address ;
    public final void gotoPC() throws RecognitionException {
        int pc =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:83:8: ( ( 'g' | 'goto' ) pc= address )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:83:10: ( 'g' | 'goto' ) pc= address
            {
            if ( (input.LA(1) >= 30 && input.LA(1) <= 31) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            pushFollow(FOLLOW_address_in_gotoPC312);
            pc=address();

            state._fsp--;


             cmd.gotoPC (pc); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "gotoPC"



    // $ANTLR start "examine"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:85:1: examine : ( ( ( 'e' | 'examine' | ( ( 'e/x' | 'examine/x' ) ) ) ( (addr= address ( ':' count= number )? ) ) ) | ( ( 'e' | 'examine' ) ( ( register ( ':' count= number )? ) ) ) );
    public final void examine() throws RecognitionException {
        int addr =0;

        int count =0;

        int register2 =0;


        CommandHandler.MemFormat format=CommandHandler.MemFormat.ASM;
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:86:9: ( ( ( 'e' | 'examine' | ( ( 'e/x' | 'examine/x' ) ) ) ( (addr= address ( ':' count= number )? ) ) ) | ( ( 'e' | 'examine' ) ( ( register ( ':' count= number )? ) ) ) )
            int alt7=2;
            switch ( input.LA(1) ) {
            case 26:
                {
                int LA7_1 = input.LA(2);

                if ( (LA7_1==Decimal||LA7_1==Hex||LA7_1==Identifier||(LA7_1 >= 20 && LA7_1 <= 26)||LA7_1==28||(LA7_1 >= 30 && LA7_1 <= 33)||LA7_1==35||(LA7_1 >= 37 && LA7_1 <= 41)||(LA7_1 >= 46 && LA7_1 <= 58)) ) {
                    alt7=1;
                }
                else if ( (LA7_1==Register) ) {
                    alt7=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 1, input);

                    throw nvae;

                }
                }
                break;
            case 28:
                {
                int LA7_2 = input.LA(2);

                if ( (LA7_2==Decimal||LA7_2==Hex||LA7_2==Identifier||(LA7_2 >= 20 && LA7_2 <= 26)||LA7_2==28||(LA7_2 >= 30 && LA7_2 <= 33)||LA7_2==35||(LA7_2 >= 37 && LA7_2 <= 41)||(LA7_2 >= 46 && LA7_2 <= 58)) ) {
                    alt7=1;
                }
                else if ( (LA7_2==Register) ) {
                    alt7=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 7, 2, input);

                    throw nvae;

                }
                }
                break;
            case 27:
            case 29:
                {
                alt7=1;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }

            switch (alt7) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:86:11: ( ( 'e' | 'examine' | ( ( 'e/x' | 'examine/x' ) ) ) ( (addr= address ( ':' count= number )? ) ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:86:11: ( ( 'e' | 'examine' | ( ( 'e/x' | 'examine/x' ) ) ) ( (addr= address ( ':' count= number )? ) ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:86:12: ( 'e' | 'examine' | ( ( 'e/x' | 'examine/x' ) ) ) ( (addr= address ( ':' count= number )? ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:86:12: ( 'e' | 'examine' | ( ( 'e/x' | 'examine/x' ) ) )
                    int alt4=3;
                    switch ( input.LA(1) ) {
                    case 26:
                        {
                        alt4=1;
                        }
                        break;
                    case 28:
                        {
                        alt4=2;
                        }
                        break;
                    case 27:
                    case 29:
                        {
                        alt4=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 0, input);

                        throw nvae;

                    }

                    switch (alt4) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:86:13: 'e'
                            {
                            match(input,26,FOLLOW_26_in_examine350); 

                            }
                            break;
                        case 2 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:86:17: 'examine'
                            {
                            match(input,28,FOLLOW_28_in_examine352); 

                            }
                            break;
                        case 3 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:86:27: ( ( 'e/x' | 'examine/x' ) )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:86:27: ( ( 'e/x' | 'examine/x' ) )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:86:28: ( 'e/x' | 'examine/x' )
                            {
                            if ( input.LA(1)==27||input.LA(1)==29 ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            format=CommandHandler.MemFormat.HEX;

                            }


                            }
                            break;

                    }


                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:87:19: ( (addr= address ( ':' count= number )? ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:87:20: (addr= address ( ':' count= number )? )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:87:20: (addr= address ( ':' count= number )? )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:87:21: addr= address ( ':' count= number )?
                    {
                    pushFollow(FOLLOW_address_in_examine388);
                    addr=address();

                    state._fsp--;


                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:87:34: ( ':' count= number )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==18) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:87:35: ':' count= number
                            {
                            match(input,18,FOLLOW_18_in_examine391); 

                            pushFollow(FOLLOW_number_in_examine395);
                            count=number();

                            state._fsp--;


                            }
                            break;

                    }


                    cmd.examineMem (count, format, addr);

                    }


                    }


                    }


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:89:17: ( ( 'e' | 'examine' ) ( ( register ( ':' count= number )? ) ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:89:17: ( ( 'e' | 'examine' ) ( ( register ( ':' count= number )? ) ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:89:18: ( 'e' | 'examine' ) ( ( register ( ':' count= number )? ) )
                    {
                    if ( input.LA(1)==26||input.LA(1)==28 ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:90:19: ( ( register ( ':' count= number )? ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:90:20: ( register ( ':' count= number )? )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:90:20: ( register ( ':' count= number )? )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:90:21: register ( ':' count= number )?
                    {
                    pushFollow(FOLLOW_register_in_examine452);
                    register2=register();

                    state._fsp--;


                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:90:30: ( ':' count= number )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==18) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:90:31: ':' count= number
                            {
                            match(input,18,FOLLOW_18_in_examine455); 

                            pushFollow(FOLLOW_number_in_examine459);
                            count=number();

                            state._fsp--;


                            }
                            break;

                    }


                    cmd.examineReg (count, register2);

                    }


                    }


                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "examine"



    // $ANTLR start "info"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:92:1: info : ( ( ( 'i' | 'info' | ( ( 'i/x' | 'info/x' ) ) ) ( ( 'mem' ) | ( 'ins' ) | ( 'dat' ) ) ) | ( ( 'i' | 'info' ) ( ( 'reg' ) | ( 'cpu' (ps= identifier )? ) ) ) );
    public final void info() throws RecognitionException {
        CliParser.identifier_return ps =null;


        CommandHandler.MemFormat format=CommandHandler.MemFormat.ASM;
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:93:9: ( ( ( 'i' | 'info' | ( ( 'i/x' | 'info/x' ) ) ) ( ( 'mem' ) | ( 'ins' ) | ( 'dat' ) ) ) | ( ( 'i' | 'info' ) ( ( 'reg' ) | ( 'cpu' (ps= identifier )? ) ) ) )
            int alt12=2;
            switch ( input.LA(1) ) {
            case 33:
                {
                int LA12_1 = input.LA(2);

                if ( (LA12_1==25||LA12_1==37||LA12_1==41) ) {
                    alt12=1;
                }
                else if ( (LA12_1==24||LA12_1==49) ) {
                    alt12=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 1, input);

                    throw nvae;

                }
                }
                break;
            case 35:
                {
                int LA12_2 = input.LA(2);

                if ( (LA12_2==25||LA12_2==37||LA12_2==41) ) {
                    alt12=1;
                }
                else if ( (LA12_2==24||LA12_2==49) ) {
                    alt12=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 12, 2, input);

                    throw nvae;

                }
                }
                break;
            case 34:
            case 36:
                {
                alt12=1;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }

            switch (alt12) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:93:11: ( ( 'i' | 'info' | ( ( 'i/x' | 'info/x' ) ) ) ( ( 'mem' ) | ( 'ins' ) | ( 'dat' ) ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:93:11: ( ( 'i' | 'info' | ( ( 'i/x' | 'info/x' ) ) ) ( ( 'mem' ) | ( 'ins' ) | ( 'dat' ) ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:93:12: ( 'i' | 'info' | ( ( 'i/x' | 'info/x' ) ) ) ( ( 'mem' ) | ( 'ins' ) | ( 'dat' ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:93:12: ( 'i' | 'info' | ( ( 'i/x' | 'info/x' ) ) )
                    int alt8=3;
                    switch ( input.LA(1) ) {
                    case 33:
                        {
                        alt8=1;
                        }
                        break;
                    case 35:
                        {
                        alt8=2;
                        }
                        break;
                    case 34:
                    case 36:
                        {
                        alt8=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 0, input);

                        throw nvae;

                    }

                    switch (alt8) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:93:13: 'i'
                            {
                            match(input,33,FOLLOW_33_in_info501); 

                            }
                            break;
                        case 2 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:93:17: 'info'
                            {
                            match(input,35,FOLLOW_35_in_info503); 

                            }
                            break;
                        case 3 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:93:24: ( ( 'i/x' | 'info/x' ) )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:93:24: ( ( 'i/x' | 'info/x' ) )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:93:25: ( 'i/x' | 'info/x' )
                            {
                            if ( input.LA(1)==34||input.LA(1)==36 ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            format=CommandHandler.MemFormat.HEX;

                            }


                            }
                            break;

                    }


                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:94:19: ( ( 'mem' ) | ( 'ins' ) | ( 'dat' ) )
                    int alt9=3;
                    switch ( input.LA(1) ) {
                    case 41:
                        {
                        alt9=1;
                        }
                        break;
                    case 37:
                        {
                        alt9=2;
                        }
                        break;
                    case 25:
                        {
                        alt9=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 9, 0, input);

                        throw nvae;

                    }

                    switch (alt9) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:94:20: ( 'mem' )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:94:20: ( 'mem' )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:94:21: 'mem'
                            {
                            match(input,41,FOLLOW_41_in_info537); 

                            cmd.examineMemAll (format, CommandHandler.MemRegion.ALL);

                            }


                            }
                            break;
                        case 2 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:96:20: ( 'ins' )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:96:20: ( 'ins' )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:96:21: 'ins'
                            {
                            match(input,37,FOLLOW_37_in_info589); 

                            cmd.examineMemAll (format, CommandHandler.MemRegion.INS);

                            }


                            }
                            break;
                        case 3 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:98:20: ( 'dat' )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:98:20: ( 'dat' )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:98:21: 'dat'
                            {
                            match(input,25,FOLLOW_25_in_info640); 

                            cmd.examineMemAll (format, CommandHandler.MemRegion.DAT);

                            }


                            }
                            break;

                    }


                    }


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:100:17: ( ( 'i' | 'info' ) ( ( 'reg' ) | ( 'cpu' (ps= identifier )? ) ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:100:17: ( ( 'i' | 'info' ) ( ( 'reg' ) | ( 'cpu' (ps= identifier )? ) ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:100:18: ( 'i' | 'info' ) ( ( 'reg' ) | ( 'cpu' (ps= identifier )? ) )
                    {
                    if ( input.LA(1)==33||input.LA(1)==35 ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:101:19: ( ( 'reg' ) | ( 'cpu' (ps= identifier )? ) )
                    int alt11=2;
                    int LA11_0 = input.LA(1);

                    if ( (LA11_0==49) ) {
                        alt11=1;
                    }
                    else if ( (LA11_0==24) ) {
                        alt11=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 11, 0, input);

                        throw nvae;

                    }
                    switch (alt11) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:101:20: ( 'reg' )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:101:20: ( 'reg' )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:101:21: 'reg'
                            {
                            match(input,49,FOLLOW_49_in_info716); 

                            cmd.examineRegAll ();

                            }


                            }
                            break;
                        case 2 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:102:20: ( 'cpu' (ps= identifier )? )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:102:20: ( 'cpu' (ps= identifier )? )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:102:21: 'cpu' (ps= identifier )?
                            {
                            match(input,24,FOLLOW_24_in_info743); 

                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:102:29: (ps= identifier )?
                            int alt10=2;
                            int LA10_0 = input.LA(1);

                            if ( (LA10_0==Identifier||(LA10_0 >= 20 && LA10_0 <= 26)||LA10_0==28||(LA10_0 >= 30 && LA10_0 <= 33)||LA10_0==35||(LA10_0 >= 37 && LA10_0 <= 41)||(LA10_0 >= 46 && LA10_0 <= 58)) ) {
                                alt10=1;
                            }
                            switch (alt10) {
                                case 1 :
                                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:102:29: ps= identifier
                                    {
                                    pushFollow(FOLLOW_identifier_in_info747);
                                    ps=identifier();

                                    state._fsp--;


                                    }
                                    break;

                            }


                            cmd.examineProc ((ps!=null?input.toString(ps.start,ps.stop):null));

                            }


                            }
                            break;

                    }


                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "info"



    // $ANTLR start "set"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:103:1: set : ( ( register '=' number ) | ( 'm' addr= address '=' val= number ) | ( ( 'i' addr= address ) ( ( '=' instr0= instr ) | ( '+' instr1= instr ) | ( '-' ) ) ) );
    public final void set() throws RecognitionException {
        int addr =0;

        int val =0;

        String instr0 =null;

        String instr1 =null;

        int register3 =0;

        int number4 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:103:5: ( ( register '=' number ) | ( 'm' addr= address '=' val= number ) | ( ( 'i' addr= address ) ( ( '=' instr0= instr ) | ( '+' instr1= instr ) | ( '-' ) ) ) )
            int alt14=3;
            switch ( input.LA(1) ) {
            case Register:
                {
                alt14=1;
                }
                break;
            case 40:
                {
                alt14=2;
                }
                break;
            case 33:
                {
                alt14=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }

            switch (alt14) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:103:7: ( register '=' number )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:103:7: ( register '=' number )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:103:8: register '=' number
                    {
                    pushFollow(FOLLOW_register_in_set761);
                    register3=register();

                    state._fsp--;


                    match(input,19,FOLLOW_19_in_set763); 

                    pushFollow(FOLLOW_number_in_set765);
                    number4=number();

                    state._fsp--;


                    }


                     cmd.setReg (register3, number4); 

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:105:3: ( 'm' addr= address '=' val= number )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:105:3: ( 'm' addr= address '=' val= number )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:105:4: 'm' addr= address '=' val= number
                    {
                    match(input,40,FOLLOW_40_in_set779); 

                    pushFollow(FOLLOW_address_in_set783);
                    addr=address();

                    state._fsp--;


                    match(input,19,FOLLOW_19_in_set785); 

                    pushFollow(FOLLOW_number_in_set789);
                    val=number();

                    state._fsp--;


                    }


                     cmd.setMem (addr, val); 

                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:107:3: ( ( 'i' addr= address ) ( ( '=' instr0= instr ) | ( '+' instr1= instr ) | ( '-' ) ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:107:3: ( ( 'i' addr= address ) ( ( '=' instr0= instr ) | ( '+' instr1= instr ) | ( '-' ) ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:107:4: ( 'i' addr= address ) ( ( '=' instr0= instr ) | ( '+' instr1= instr ) | ( '-' ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:107:4: ( 'i' addr= address )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:107:5: 'i' addr= address
                    {
                    match(input,33,FOLLOW_33_in_set804); 

                    pushFollow(FOLLOW_address_in_set808);
                    addr=address();

                    state._fsp--;


                    }


                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:108:19: ( ( '=' instr0= instr ) | ( '+' instr1= instr ) | ( '-' ) )
                    int alt13=3;
                    switch ( input.LA(1) ) {
                    case 19:
                        {
                        alt13=1;
                        }
                        break;
                    case 16:
                        {
                        alt13=2;
                        }
                        break;
                    case 17:
                        {
                        alt13=3;
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 13, 0, input);

                        throw nvae;

                    }

                    switch (alt13) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:108:20: ( '=' instr0= instr )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:108:20: ( '=' instr0= instr )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:108:21: '=' instr0= instr
                            {
                            match(input,19,FOLLOW_19_in_set832); 

                            pushFollow(FOLLOW_instr_in_set836);
                            instr0=instr();

                            state._fsp--;


                            cmd.setIns (addr, CommandHandler.InsOper.REPLACE, instr0);

                            }


                            }
                            break;
                        case 2 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:110:20: ( '+' instr1= instr )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:110:20: ( '+' instr1= instr )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:110:21: '+' instr1= instr
                            {
                            match(input,16,FOLLOW_16_in_set887); 

                            pushFollow(FOLLOW_instr_in_set891);
                            instr1=instr();

                            state._fsp--;


                            cmd.setIns (addr, CommandHandler.InsOper.INSERT, instr1);

                            }


                            }
                            break;
                        case 3 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:112:20: ( '-' )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:112:20: ( '-' )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:112:21: '-'
                            {
                            match(input,17,FOLLOW_17_in_set942); 

                            cmd.setIns (addr, CommandHandler.InsOper.DELETE, "");

                            }


                            }
                            break;

                    }


                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "set"



    // $ANTLR start "debug"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:114:1: debug : ( debugPoint | traceProg | clear );
    public final void debug() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:114:9: ( debugPoint | traceProg | clear )
            int alt15=3;
            switch ( input.LA(1) ) {
            case 21:
            case 22:
            case 42:
            case 43:
                {
                alt15=1;
                }
                break;
            case 53:
            case 55:
                {
                int LA15_2 = input.LA(2);

                if ( (LA15_2==20||LA15_2==48||LA15_2==56||LA15_2==58) ) {
                    alt15=1;
                }
                else if ( (LA15_2==46) ) {
                    alt15=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 2, input);

                    throw nvae;

                }
                }
                break;
            case 44:
                {
                int LA15_3 = input.LA(2);

                if ( (LA15_3==20||LA15_3==48||LA15_3==56||LA15_3==58) ) {
                    alt15=1;
                }
                else if ( (LA15_3==46) ) {
                    alt15=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 3, input);

                    throw nvae;

                }
                }
                break;
            case 45:
                {
                int LA15_4 = input.LA(2);

                if ( (LA15_4==20||LA15_4==48||LA15_4==56||LA15_4==58) ) {
                    alt15=1;
                }
                else if ( (LA15_4==46) ) {
                    alt15=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 15, 4, input);

                    throw nvae;

                }
                }
                break;
            case 23:
                {
                alt15=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;

            }

            switch (alt15) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:114:17: debugPoint
                    {
                    pushFollow(FOLLOW_debugPoint_in_debug987);
                    debugPoint();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:114:30: traceProg
                    {
                    pushFollow(FOLLOW_traceProg_in_debug991);
                    traceProg();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:114:42: clear
                    {
                    pushFollow(FOLLOW_clear_in_debug995);
                    clear();

                    state._fsp--;


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "debug"



    // $ANTLR start "debugPoint"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:115:1: debugPoint : ( ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) ) | ( ( ( 'nob' | 'nobreak' ) ) | ( ( 'not' | 'notrace' ) ) ) ) ( 'x' (a0= address ) | ( ( ( ( 'r' | 'w' | 'a' ) (a1= address ) ) | ( ( 'r' | 'w' | 'a' ) ( register ) ) ) ) ) ;
    public final void debugPoint() throws RecognitionException {
        int a0 =0;

        int a1 =0;

        int register5 =0;


        boolean                   isEnabled=false; 
                               CommandHandler.DebugType  type=null; 
                               CommandHandler.DebugPoint point=null; 
                               int                       value=0;
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:119:9: ( ( ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) ) | ( ( ( 'nob' | 'nobreak' ) ) | ( ( 'not' | 'notrace' ) ) ) ) ( 'x' (a0= address ) | ( ( ( ( 'r' | 'w' | 'a' ) (a1= address ) ) | ( ( 'r' | 'w' | 'a' ) ( register ) ) ) ) ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:119:17: ( ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) ) | ( ( ( 'nob' | 'nobreak' ) ) | ( ( 'not' | 'notrace' ) ) ) ) ( 'x' (a0= address ) | ( ( ( ( 'r' | 'w' | 'a' ) (a1= address ) ) | ( ( 'r' | 'w' | 'a' ) ( register ) ) ) ) )
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:119:17: ( ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) ) | ( ( ( 'nob' | 'nobreak' ) ) | ( ( 'not' | 'notrace' ) ) ) )
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( ((LA18_0 >= 21 && LA18_0 <= 22)||LA18_0==53||LA18_0==55) ) {
                alt18=1;
            }
            else if ( ((LA18_0 >= 42 && LA18_0 <= 45)) ) {
                alt18=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;

            }
            switch (alt18) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:119:18: ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:119:18: ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) )
                    int alt16=2;
                    int LA16_0 = input.LA(1);

                    if ( ((LA16_0 >= 21 && LA16_0 <= 22)) ) {
                        alt16=1;
                    }
                    else if ( (LA16_0==53||LA16_0==55) ) {
                        alt16=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 0, input);

                        throw nvae;

                    }
                    switch (alt16) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:119:19: ( ( 'b' | 'break' ) )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:119:19: ( ( 'b' | 'break' ) )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:119:20: ( 'b' | 'break' )
                            {
                            if ( (input.LA(1) >= 21 && input.LA(1) <= 22) ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            type=CommandHandler.DebugType.BREAK;

                            }


                            }
                            break;
                        case 2 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:120:18: ( ( 't' | 'trace' ) )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:120:18: ( ( 't' | 'trace' ) )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:120:19: ( 't' | 'trace' )
                            {
                            if ( input.LA(1)==53||input.LA(1)==55 ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            type=CommandHandler.DebugType.TRACE;

                            }


                            }
                            break;

                    }


                    isEnabled=true;

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:121:17: ( ( ( 'nob' | 'nobreak' ) ) | ( ( 'not' | 'notrace' ) ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:121:17: ( ( ( 'nob' | 'nobreak' ) ) | ( ( 'not' | 'notrace' ) ) )
                    int alt17=2;
                    int LA17_0 = input.LA(1);

                    if ( ((LA17_0 >= 42 && LA17_0 <= 43)) ) {
                        alt17=1;
                    }
                    else if ( ((LA17_0 >= 44 && LA17_0 <= 45)) ) {
                        alt17=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 0, input);

                        throw nvae;

                    }
                    switch (alt17) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:121:18: ( ( 'nob' | 'nobreak' ) )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:121:18: ( ( 'nob' | 'nobreak' ) )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:121:19: ( 'nob' | 'nobreak' )
                            {
                            if ( (input.LA(1) >= 42 && input.LA(1) <= 43) ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            type=CommandHandler.DebugType.BREAK;

                            }


                            }
                            break;
                        case 2 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:122:18: ( ( 'not' | 'notrace' ) )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:122:18: ( ( 'not' | 'notrace' ) )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:122:19: ( 'not' | 'notrace' )
                            {
                            if ( (input.LA(1) >= 44 && input.LA(1) <= 45) ) {
                                input.consume();
                                state.errorRecovery=false;
                            }
                            else {
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            type=CommandHandler.DebugType.TRACE;

                            }


                            }
                            break;

                    }


                    isEnabled=false;

                    }
                    break;

            }


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:123:17: ( 'x' (a0= address ) | ( ( ( ( 'r' | 'w' | 'a' ) (a1= address ) ) | ( ( 'r' | 'w' | 'a' ) ( register ) ) ) ) )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==58) ) {
                alt22=1;
            }
            else if ( (LA22_0==20||LA22_0==48||LA22_0==56) ) {
                alt22=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;

            }
            switch (alt22) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:123:18: 'x' (a0= address )
                    {
                    match(input,58,FOLLOW_58_in_debugPoint1156); 

                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:123:22: (a0= address )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:123:23: a0= address
                    {
                    pushFollow(FOLLOW_address_in_debugPoint1161);
                    a0=address();

                    state._fsp--;


                    cmd.debugPoint (type, CommandHandler.DebugPoint.INSTRUCTION, isEnabled, a0);

                    }


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:125:17: ( ( ( ( 'r' | 'w' | 'a' ) (a1= address ) ) | ( ( 'r' | 'w' | 'a' ) ( register ) ) ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:125:17: ( ( ( ( 'r' | 'w' | 'a' ) (a1= address ) ) | ( ( 'r' | 'w' | 'a' ) ( register ) ) ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:125:18: ( ( ( 'r' | 'w' | 'a' ) (a1= address ) ) | ( ( 'r' | 'w' | 'a' ) ( register ) ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:125:18: ( ( ( 'r' | 'w' | 'a' ) (a1= address ) ) | ( ( 'r' | 'w' | 'a' ) ( register ) ) )
                    int alt21=2;
                    switch ( input.LA(1) ) {
                    case 48:
                        {
                        int LA21_1 = input.LA(2);

                        if ( (LA21_1==Decimal||LA21_1==Hex||LA21_1==Identifier||(LA21_1 >= 20 && LA21_1 <= 26)||LA21_1==28||(LA21_1 >= 30 && LA21_1 <= 33)||LA21_1==35||(LA21_1 >= 37 && LA21_1 <= 41)||(LA21_1 >= 46 && LA21_1 <= 58)) ) {
                            alt21=1;
                        }
                        else if ( (LA21_1==Register) ) {
                            alt21=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 21, 1, input);

                            throw nvae;

                        }
                        }
                        break;
                    case 56:
                        {
                        int LA21_2 = input.LA(2);

                        if ( (LA21_2==Decimal||LA21_2==Hex||LA21_2==Identifier||(LA21_2 >= 20 && LA21_2 <= 26)||LA21_2==28||(LA21_2 >= 30 && LA21_2 <= 33)||LA21_2==35||(LA21_2 >= 37 && LA21_2 <= 41)||(LA21_2 >= 46 && LA21_2 <= 58)) ) {
                            alt21=1;
                        }
                        else if ( (LA21_2==Register) ) {
                            alt21=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 21, 2, input);

                            throw nvae;

                        }
                        }
                        break;
                    case 20:
                        {
                        int LA21_3 = input.LA(2);

                        if ( (LA21_3==Decimal||LA21_3==Hex||LA21_3==Identifier||(LA21_3 >= 20 && LA21_3 <= 26)||LA21_3==28||(LA21_3 >= 30 && LA21_3 <= 33)||LA21_3==35||(LA21_3 >= 37 && LA21_3 <= 41)||(LA21_3 >= 46 && LA21_3 <= 58)) ) {
                            alt21=1;
                        }
                        else if ( (LA21_3==Register) ) {
                            alt21=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 21, 3, input);

                            throw nvae;

                        }
                        }
                        break;
                    default:
                        NoViableAltException nvae =
                            new NoViableAltException("", 21, 0, input);

                        throw nvae;

                    }

                    switch (alt21) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:125:19: ( ( 'r' | 'w' | 'a' ) (a1= address ) )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:125:19: ( ( 'r' | 'w' | 'a' ) (a1= address ) )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:125:20: ( 'r' | 'w' | 'a' ) (a1= address )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:125:20: ( 'r' | 'w' | 'a' )
                            int alt19=3;
                            switch ( input.LA(1) ) {
                            case 48:
                                {
                                alt19=1;
                                }
                                break;
                            case 56:
                                {
                                alt19=2;
                                }
                                break;
                            case 20:
                                {
                                alt19=3;
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("", 19, 0, input);

                                throw nvae;

                            }

                            switch (alt19) {
                                case 1 :
                                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:125:21: 'r'
                                    {
                                    match(input,48,FOLLOW_48_in_debugPoint1207); 

                                    point=CommandHandler.DebugPoint.MEMORY_READ;

                                    }
                                    break;
                                case 2 :
                                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:126:21: 'w'
                                    {
                                    match(input,56,FOLLOW_56_in_debugPoint1234); 

                                    point=CommandHandler.DebugPoint.MEMORY_WRITE;

                                    }
                                    break;
                                case 3 :
                                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:127:21: 'a'
                                    {
                                    match(input,20,FOLLOW_20_in_debugPoint1261); 

                                    point=CommandHandler.DebugPoint.MEMORY_ACCESS;

                                    }
                                    break;

                            }


                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:127:76: (a1= address )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:127:77: a1= address
                            {
                            pushFollow(FOLLOW_address_in_debugPoint1270);
                            a1=address();

                            state._fsp--;


                            value=a1;

                            }


                            }


                            }
                            break;
                        case 2 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:128:19: ( ( 'r' | 'w' | 'a' ) ( register ) )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:128:19: ( ( 'r' | 'w' | 'a' ) ( register ) )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:128:20: ( 'r' | 'w' | 'a' ) ( register )
                            {
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:128:20: ( 'r' | 'w' | 'a' )
                            int alt20=3;
                            switch ( input.LA(1) ) {
                            case 48:
                                {
                                alt20=1;
                                }
                                break;
                            case 56:
                                {
                                alt20=2;
                                }
                                break;
                            case 20:
                                {
                                alt20=3;
                                }
                                break;
                            default:
                                NoViableAltException nvae =
                                    new NoViableAltException("", 20, 0, input);

                                throw nvae;

                            }

                            switch (alt20) {
                                case 1 :
                                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:128:21: 'r'
                                    {
                                    match(input,48,FOLLOW_48_in_debugPoint1300); 

                                    point=CommandHandler.DebugPoint.REGISTER_READ;

                                    }
                                    break;
                                case 2 :
                                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:129:21: 'w'
                                    {
                                    match(input,56,FOLLOW_56_in_debugPoint1327); 

                                    point=CommandHandler.DebugPoint.REGISTER_WRITE;

                                    }
                                    break;
                                case 3 :
                                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:130:21: 'a'
                                    {
                                    match(input,20,FOLLOW_20_in_debugPoint1354); 

                                    point=CommandHandler.DebugPoint.REGISTER_ACCESS;

                                    }
                                    break;

                            }


                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:130:77: ( register )
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:130:78: register
                            {
                            pushFollow(FOLLOW_register_in_debugPoint1360);
                            register5=register();

                            state._fsp--;


                            value=register5;

                            }


                            }


                            }
                            break;

                    }


                    cmd.debugPoint (type, point, isEnabled, value);

                    }


                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "debugPoint"



    // $ANTLR start "traceProg"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:132:1: traceProg : ( ( ( 't' | 'trace' ) ) | ( 'not' | 'notrace' ) ) 'prog' ;
    public final void traceProg() throws RecognitionException {
        boolean isEnabled=false;
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:133:9: ( ( ( ( 't' | 'trace' ) ) | ( 'not' | 'notrace' ) ) 'prog' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:133:17: ( ( ( 't' | 'trace' ) ) | ( 'not' | 'notrace' ) ) 'prog'
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:133:17: ( ( ( 't' | 'trace' ) ) | ( 'not' | 'notrace' ) )
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==53||LA24_0==55) ) {
                alt24=1;
            }
            else if ( ((LA24_0 >= 44 && LA24_0 <= 45)) ) {
                alt24=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 24, 0, input);

                throw nvae;

            }
            switch (alt24) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:133:18: ( ( 't' | 'trace' ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:133:18: ( ( 't' | 'trace' ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:133:19: ( 't' | 'trace' )
                    {
                    if ( input.LA(1)==53||input.LA(1)==55 ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    isEnabled=true;

                    }


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:133:54: ( 'not' | 'notrace' )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:133:54: ( 'not' | 'notrace' )
                    int alt23=2;
                    int LA23_0 = input.LA(1);

                    if ( (LA23_0==44) ) {
                        alt23=1;
                    }
                    else if ( (LA23_0==45) ) {
                        alt23=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 23, 0, input);

                        throw nvae;

                    }
                    switch (alt23) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:133:55: 'not'
                            {
                            match(input,44,FOLLOW_44_in_traceProg1432); 

                            }
                            break;
                        case 2 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:133:61: 'notrace'
                            {
                            match(input,45,FOLLOW_45_in_traceProg1434); 

                            isEnabled=false;

                            }
                            break;

                    }


                    }
                    break;

            }


            match(input,46,FOLLOW_46_in_traceProg1440); 

            cmd.traceProg (isEnabled);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "traceProg"



    // $ANTLR start "clear"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:135:1: clear : 'clear' ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) ) ;
    public final void clear() throws RecognitionException {
        CommandHandler.DebugType type=null;
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:136:9: ( 'clear' ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:136:17: 'clear' ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) )
            {
            match(input,23,FOLLOW_23_in_clear1495); 

            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:137:17: ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) )
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( ((LA25_0 >= 21 && LA25_0 <= 22)) ) {
                alt25=1;
            }
            else if ( (LA25_0==53||LA25_0==55) ) {
                alt25=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;

            }
            switch (alt25) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:137:18: ( ( 'b' | 'break' ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:137:18: ( ( 'b' | 'break' ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:137:19: ( 'b' | 'break' )
                    {
                    if ( (input.LA(1) >= 21 && input.LA(1) <= 22) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    type=CommandHandler.DebugType.BREAK;

                    }


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:138:18: ( ( 't' | 'trace' ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:138:18: ( ( 't' | 'trace' ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:138:19: ( 't' | 'trace' )
                    {
                    if ( input.LA(1)==53||input.LA(1)==55 ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    type=CommandHandler.DebugType.TRACE;

                    }


                    }
                    break;

            }


            cmd.clearDebugPoints (type);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "clear"



    // $ANTLR start "infoDebug"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:140:1: infoDebug : ( 'i' | 'info' ) ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) ) ;
    public final void infoDebug() throws RecognitionException {
        CommandHandler.DebugType type=null;
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:141:9: ( ( 'i' | 'info' ) ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:141:17: ( 'i' | 'info' ) ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) )
            {
            if ( input.LA(1)==33||input.LA(1)==35 ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:142:10: ( ( ( 'b' | 'break' ) ) | ( ( 't' | 'trace' ) ) )
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( ((LA26_0 >= 21 && LA26_0 <= 22)) ) {
                alt26=1;
            }
            else if ( (LA26_0==53||LA26_0==55) ) {
                alt26=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 26, 0, input);

                throw nvae;

            }
            switch (alt26) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:142:11: ( ( 'b' | 'break' ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:142:11: ( ( 'b' | 'break' ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:142:12: ( 'b' | 'break' )
                    {
                    if ( (input.LA(1) >= 21 && input.LA(1) <= 22) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    type=CommandHandler.DebugType.BREAK;

                    }


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:143:18: ( ( 't' | 'trace' ) )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:143:18: ( ( 't' | 'trace' ) )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:143:19: ( 't' | 'trace' )
                    {
                    if ( input.LA(1)==53||input.LA(1)==55 ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    type=CommandHandler.DebugType.TRACE;

                    }


                    }
                    break;

            }


            cmd.showDebugPoints (type);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "infoDebug"



    // $ANTLR start "help"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:145:1: help : 'help' ;
    public final void help() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:145:7: ( 'help' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:145:9: 'help'
            {
            match(input,32,FOLLOW_32_in_help1677); 

            cmd.help ();

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "help"



    // $ANTLR start "quit"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:146:1: quit : 'quit' ;
    public final void quit() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:146:6: ( 'quit' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:146:8: 'quit'
            {
            match(input,47,FOLLOW_47_in_quit1687); 

            throw new QuitException ();

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return ;
    }
    // $ANTLR end "quit"



    // $ANTLR start "address"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:147:1: address returns [int value] : (a= adrAtm |b= adrAtm '+' c= address );
    public final int address() throws RecognitionException {
        int value = 0;


        int a =0;

        int b =0;

        int c =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:148:9: (a= adrAtm |b= adrAtm '+' c= address )
            int alt27=2;
            switch ( input.LA(1) ) {
            case Hex:
                {
                int LA27_1 = input.LA(2);

                if ( (LA27_1==EOF||(LA27_1 >= 17 && LA27_1 <= 19)) ) {
                    alt27=1;
                }
                else if ( (LA27_1==16) ) {
                    int LA27_6 = input.LA(3);

                    if ( (LA27_6==String) ) {
                        alt27=1;
                    }
                    else if ( (LA27_6==Decimal||LA27_6==Hex||LA27_6==Identifier||(LA27_6 >= 20 && LA27_6 <= 26)||LA27_6==28||(LA27_6 >= 30 && LA27_6 <= 33)||LA27_6==35||(LA27_6 >= 37 && LA27_6 <= 41)||(LA27_6 >= 46 && LA27_6 <= 58)) ) {
                        alt27=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 27, 6, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 27, 1, input);

                    throw nvae;

                }
                }
                break;
            case Decimal:
                {
                int LA27_2 = input.LA(2);

                if ( (LA27_2==EOF||(LA27_2 >= 17 && LA27_2 <= 19)) ) {
                    alt27=1;
                }
                else if ( (LA27_2==16) ) {
                    int LA27_6 = input.LA(3);

                    if ( (LA27_6==String) ) {
                        alt27=1;
                    }
                    else if ( (LA27_6==Decimal||LA27_6==Hex||LA27_6==Identifier||(LA27_6 >= 20 && LA27_6 <= 26)||LA27_6==28||(LA27_6 >= 30 && LA27_6 <= 33)||LA27_6==35||(LA27_6 >= 37 && LA27_6 <= 41)||(LA27_6 >= 46 && LA27_6 <= 58)) ) {
                        alt27=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 27, 6, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 27, 2, input);

                    throw nvae;

                }
                }
                break;
            case Identifier:
                {
                int LA27_3 = input.LA(2);

                if ( (LA27_3==EOF||(LA27_3 >= 17 && LA27_3 <= 19)) ) {
                    alt27=1;
                }
                else if ( (LA27_3==16) ) {
                    int LA27_6 = input.LA(3);

                    if ( (LA27_6==String) ) {
                        alt27=1;
                    }
                    else if ( (LA27_6==Decimal||LA27_6==Hex||LA27_6==Identifier||(LA27_6 >= 20 && LA27_6 <= 26)||LA27_6==28||(LA27_6 >= 30 && LA27_6 <= 33)||LA27_6==35||(LA27_6 >= 37 && LA27_6 <= 41)||(LA27_6 >= 46 && LA27_6 <= 58)) ) {
                        alt27=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 27, 6, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 27, 3, input);

                    throw nvae;

                }
                }
                break;
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 28:
            case 30:
            case 31:
            case 32:
            case 33:
            case 35:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
                {
                int LA27_4 = input.LA(2);

                if ( (LA27_4==EOF||(LA27_4 >= 17 && LA27_4 <= 19)) ) {
                    alt27=1;
                }
                else if ( (LA27_4==16) ) {
                    int LA27_6 = input.LA(3);

                    if ( (LA27_6==String) ) {
                        alt27=1;
                    }
                    else if ( (LA27_6==Decimal||LA27_6==Hex||LA27_6==Identifier||(LA27_6 >= 20 && LA27_6 <= 26)||LA27_6==28||(LA27_6 >= 30 && LA27_6 <= 33)||LA27_6==35||(LA27_6 >= 37 && LA27_6 <= 41)||(LA27_6 >= 46 && LA27_6 <= 58)) ) {
                        alt27=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 27, 6, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 27, 4, input);

                    throw nvae;

                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;

            }

            switch (alt27) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:148:15: a= adrAtm
                    {
                    pushFollow(FOLLOW_adrAtm_in_address1715);
                    a=adrAtm();

                    state._fsp--;


                    value =a;

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:150:15: b= adrAtm '+' c= address
                    {
                    pushFollow(FOLLOW_adrAtm_in_address1763);
                    b=adrAtm();

                    state._fsp--;


                    match(input,16,FOLLOW_16_in_address1765); 

                    pushFollow(FOLLOW_address_in_address1769);
                    c=address();

                    state._fsp--;


                    value =b+c;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return value;
    }
    // $ANTLR end "address"



    // $ANTLR start "adrAtm"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:152:1: adrAtm returns [int value] : ( number | identifier );
    public final int adrAtm() throws RecognitionException {
        int value = 0;


        int number6 =0;

        CliParser.identifier_return identifier7 =null;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:153:9: ( number | identifier )
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==Decimal||LA28_0==Hex) ) {
                alt28=1;
            }
            else if ( (LA28_0==Identifier||(LA28_0 >= 20 && LA28_0 <= 26)||LA28_0==28||(LA28_0 >= 30 && LA28_0 <= 33)||LA28_0==35||(LA28_0 >= 37 && LA28_0 <= 41)||(LA28_0 >= 46 && LA28_0 <= 58)) ) {
                alt28=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;

            }
            switch (alt28) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:153:17: number
                    {
                    pushFollow(FOLLOW_number_in_adrAtm1821);
                    number6=number();

                    state._fsp--;


                    value =number6;

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:153:50: identifier
                    {
                    pushFollow(FOLLOW_identifier_in_adrAtm1827);
                    identifier7=identifier();

                    state._fsp--;


                    value =cmd.getLabelValue ((identifier7!=null?input.toString(identifier7.start,identifier7.stop):null));

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return value;
    }
    // $ANTLR end "adrAtm"



    // $ANTLR start "number"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:154:1: number returns [int value] : ( Hex | Decimal );
    public final int number() throws RecognitionException {
        int value = 0;


        Token Hex8=null;
        Token Decimal9=null;

        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:155:2: ( Hex | Decimal )
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==Hex) ) {
                alt29=1;
            }
            else if ( (LA29_0==Decimal) ) {
                alt29=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;

            }
            switch (alt29) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:155:4: Hex
                    {
                    Hex8=(Token)match(input,Hex,FOLLOW_Hex_in_number1841); 

                     value =(int)(Long.parseLong((Hex8!=null?Hex8.getText():null).substring(2),16)); 

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:157:3: Decimal
                    {
                    Decimal9=(Token)match(input,Decimal,FOLLOW_Decimal_in_number1854); 

                     value =Integer.parseInt((Decimal9!=null?Decimal9.getText():null)); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return value;
    }
    // $ANTLR end "number"



    // $ANTLR start "instr"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:159:1: instr returns [String value] : string ;
    public final String instr() throws RecognitionException {
        String value = null;


        String string10 =null;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:160:9: ( string )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:160:11: string
            {
            pushFollow(FOLLOW_string_in_instr1879);
            string10=string();

            state._fsp--;


            value =string10;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return value;
    }
    // $ANTLR end "instr"



    // $ANTLR start "register"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:162:1: register returns [int value] : r= Register ;
    public final int register() throws RecognitionException {
        int value = 0;


        Token r=null;

        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:163:2: (r= Register )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:163:4: r= Register
            {
            r=(Token)match(input,Register,FOLLOW_Register_in_register1919); 

            value = cmd.getRegisterNumber ((r!=null?r.getText():null)!=null?(r!=null?r.getText():null).substring(1,(r!=null?r.getText():null).length()):null);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return value;
    }
    // $ANTLR end "register"


    public static class filename_return extends ParserRuleReturnScope {
        public String value;
    };


    // $ANTLR start "filename"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:164:1: filename returns [String value] : (a= string |b= identifier );
    public final CliParser.filename_return filename() throws RecognitionException {
        CliParser.filename_return retval = new CliParser.filename_return();
        retval.start = input.LT(1);


        String a =null;

        CliParser.identifier_return b =null;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:165:2: (a= string |b= identifier )
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==String) ) {
                alt30=1;
            }
            else if ( (LA30_0==Identifier||(LA30_0 >= 20 && LA30_0 <= 26)||LA30_0==28||(LA30_0 >= 30 && LA30_0 <= 33)||LA30_0==35||(LA30_0 >= 37 && LA30_0 <= 41)||(LA30_0 >= 46 && LA30_0 <= 58)) ) {
                alt30=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 30, 0, input);

                throw nvae;

            }
            switch (alt30) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:165:4: a= string
                    {
                    pushFollow(FOLLOW_string_in_filename1935);
                    a=string();

                    state._fsp--;


                    retval.value =a;

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:165:34: b= identifier
                    {
                    pushFollow(FOLLOW_identifier_in_filename1943);
                    b=identifier();

                    state._fsp--;


                    retval.value =(b!=null?input.toString(b.start,b.stop):null);

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "filename"


    public static class identifier_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "identifier"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:166:1: identifier : ( Identifier | keyword );
    public final CliParser.identifier_return identifier() throws RecognitionException {
        CliParser.identifier_return retval = new CliParser.identifier_return();
        retval.start = input.LT(1);


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:167:2: ( Identifier | keyword )
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==Identifier) ) {
                alt31=1;
            }
            else if ( ((LA31_0 >= 20 && LA31_0 <= 26)||LA31_0==28||(LA31_0 >= 30 && LA31_0 <= 33)||LA31_0==35||(LA31_0 >= 37 && LA31_0 <= 41)||(LA31_0 >= 46 && LA31_0 <= 58)) ) {
                alt31=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 31, 0, input);

                throw nvae;

            }
            switch (alt31) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:167:4: Identifier
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_identifier1953); 

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:167:17: keyword
                    {
                    pushFollow(FOLLOW_keyword_in_identifier1957);
                    keyword();

                    state._fsp--;


                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "identifier"



    // $ANTLR start "string"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:168:1: string returns [String value] : String ;
    public final String string() throws RecognitionException {
        String value = null;


        Token String11=null;

        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:169:9: ( String )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/Cli.g:169:17: String
            {
            String11=(Token)match(input,String,FOLLOW_String_in_string1982); 

            value =(String11!=null?String11.getText():null).substring (1, (String11!=null?String11.getText():null).length()-1);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }

        finally {
        	// do for sure before leaving
        }
        return value;
    }
    // $ANTLR end "string"

    // Delegated rules


 

    public static final BitSet FOLLOW_load_in_command41 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_test_in_command45 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_run_in_command49 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_step_in_command53 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_where_in_command57 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_gotoPC_in_command61 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_examine_in_command65 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_info_in_command69 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_set_in_command73 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_debug_in_command77 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_infoDebug_in_command81 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_quit_in_command85 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_help_in_command89 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_command92 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_load170 = new BitSet(new long[]{0x07FFC3EBD7F04800L});
    public static final BitSet FOLLOW_filename_in_load178 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_54_in_test201 = new BitSet(new long[]{0x07FFC3EBD7F04800L});
    public static final BitSet FOLLOW_filename_in_test204 = new BitSet(new long[]{0x07FFC3EBD7F00802L});
    public static final BitSet FOLLOW_identifier_in_test209 = new BitSet(new long[]{0x07FFC3EBD7F00802L});
    public static final BitSet FOLLOW_identifier_in_test213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_run249 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_step266 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_where286 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_gotoPC304 = new BitSet(new long[]{0x07FFC3EBD7F00A40L});
    public static final BitSet FOLLOW_address_in_gotoPC312 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_26_in_examine350 = new BitSet(new long[]{0x07FFC3EBD7F00A40L});
    public static final BitSet FOLLOW_28_in_examine352 = new BitSet(new long[]{0x07FFC3EBD7F00A40L});
    public static final BitSet FOLLOW_set_in_examine355 = new BitSet(new long[]{0x07FFC3EBD7F00A40L});
    public static final BitSet FOLLOW_address_in_examine388 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_18_in_examine391 = new BitSet(new long[]{0x0000000000000240L});
    public static final BitSet FOLLOW_number_in_examine395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_examine426 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_register_in_examine452 = new BitSet(new long[]{0x0000000000040002L});
    public static final BitSet FOLLOW_18_in_examine455 = new BitSet(new long[]{0x0000000000000240L});
    public static final BitSet FOLLOW_number_in_examine459 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_info501 = new BitSet(new long[]{0x0000022002000000L});
    public static final BitSet FOLLOW_35_in_info503 = new BitSet(new long[]{0x0000022002000000L});
    public static final BitSet FOLLOW_set_in_info506 = new BitSet(new long[]{0x0000022002000000L});
    public static final BitSet FOLLOW_41_in_info537 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_37_in_info589 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_info640 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_info690 = new BitSet(new long[]{0x0002000001000000L});
    public static final BitSet FOLLOW_49_in_info716 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_24_in_info743 = new BitSet(new long[]{0x07FFC3EBD7F00802L});
    public static final BitSet FOLLOW_identifier_in_info747 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_register_in_set761 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_set763 = new BitSet(new long[]{0x0000000000000240L});
    public static final BitSet FOLLOW_number_in_set765 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_40_in_set779 = new BitSet(new long[]{0x07FFC3EBD7F00A40L});
    public static final BitSet FOLLOW_address_in_set783 = new BitSet(new long[]{0x0000000000080000L});
    public static final BitSet FOLLOW_19_in_set785 = new BitSet(new long[]{0x0000000000000240L});
    public static final BitSet FOLLOW_number_in_set789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_33_in_set804 = new BitSet(new long[]{0x07FFC3EBD7F00A40L});
    public static final BitSet FOLLOW_address_in_set808 = new BitSet(new long[]{0x00000000000B0000L});
    public static final BitSet FOLLOW_19_in_set832 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_instr_in_set836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_set887 = new BitSet(new long[]{0x0000000000004000L});
    public static final BitSet FOLLOW_instr_in_set891 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_set942 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_debugPoint_in_debug987 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_traceProg_in_debug991 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_clear_in_debug995 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_debugPoint1029 = new BitSet(new long[]{0x0501000000100000L});
    public static final BitSet FOLLOW_set_in_debugPoint1061 = new BitSet(new long[]{0x0501000000100000L});
    public static final BitSet FOLLOW_set_in_debugPoint1097 = new BitSet(new long[]{0x0501000000100000L});
    public static final BitSet FOLLOW_set_in_debugPoint1126 = new BitSet(new long[]{0x0501000000100000L});
    public static final BitSet FOLLOW_58_in_debugPoint1156 = new BitSet(new long[]{0x07FFC3EBD7F00A40L});
    public static final BitSet FOLLOW_address_in_debugPoint1161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_debugPoint1207 = new BitSet(new long[]{0x07FFC3EBD7F00A40L});
    public static final BitSet FOLLOW_56_in_debugPoint1234 = new BitSet(new long[]{0x07FFC3EBD7F00A40L});
    public static final BitSet FOLLOW_20_in_debugPoint1261 = new BitSet(new long[]{0x07FFC3EBD7F00A40L});
    public static final BitSet FOLLOW_address_in_debugPoint1270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_48_in_debugPoint1300 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_56_in_debugPoint1327 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_20_in_debugPoint1354 = new BitSet(new long[]{0x0000000000002000L});
    public static final BitSet FOLLOW_register_in_debugPoint1360 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_traceProg1420 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_44_in_traceProg1432 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_45_in_traceProg1434 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_traceProg1440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_clear1495 = new BitSet(new long[]{0x00A0000000600000L});
    public static final BitSet FOLLOW_set_in_clear1516 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_clear1544 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_infoDebug1597 = new BitSet(new long[]{0x00A0000000600000L});
    public static final BitSet FOLLOW_set_in_infoDebug1614 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_infoDebug1643 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_32_in_help1677 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_quit1687 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_adrAtm_in_address1715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_adrAtm_in_address1763 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_address1765 = new BitSet(new long[]{0x07FFC3EBD7F00A40L});
    public static final BitSet FOLLOW_address_in_address1769 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_adrAtm1821 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_adrAtm1827 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Hex_in_number1841 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Decimal_in_number1854 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_in_instr1879 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Register_in_register1919 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_string_in_filename1935 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_filename1943 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_identifier1953 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_keyword_in_identifier1957 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_String_in_string1982 = new BitSet(new long[]{0x0000000000000002L});

}