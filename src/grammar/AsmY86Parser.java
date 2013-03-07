// $ANTLR 3.4 /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g 2012-02-21 14:12:43

package grammar;

import isa.Memory;
import isa.MemoryCell;
import isa.Instruction;
import isa.Datum;
import arch.y86.isa.Assembler;
import arch.y86.machine.AbstractY86CPU;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class AsmY86Parser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "Character", "Comment", "CommentZ", "Decimal", "Digit", "Hex", "HexDigit", "Identifier", "NewLine", "WS", "'$'", "'%eax'", "'%ebp'", "'%ebx'", "'%ecx'", "'%edi'", "'%edx'", "'%esi'", "'%esp'", "'('", "')'", "'*'", "','", "'-'", "'.align'", "'.byte'", "'.long'", "'.pos'", "'.word'", "':'", "'addl'", "'andl'", "'call'", "'cmove'", "'cmovg'", "'cmovge'", "'cmovl'", "'cmovle'", "'cmovne'", "'divl'", "'halt'", "'iaddl'", "'iandl'", "'idivl'", "'imodl'", "'imull'", "'irmovl'", "'isubl'", "'ixorl'", "'je'", "'jg'", "'jge'", "'jl'", "'jle'", "'jmp'", "'jne'", "'leave'", "'modl'", "'mrmovl'", "'mull'", "'nop'", "'popl'", "'pushl'", "'ret'", "'rmmovl'", "'rrmovl'", "'subl'", "'xorl'"
    };

    public static final int EOF=-1;
    public static final int T__14=14;
    public static final int T__15=15;
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
    public static final int T__59=59;
    public static final int T__60=60;
    public static final int T__61=61;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__70=70;
    public static final int T__71=71;
    public static final int Character=4;
    public static final int Comment=5;
    public static final int CommentZ=6;
    public static final int Decimal=7;
    public static final int Digit=8;
    public static final int Hex=9;
    public static final int HexDigit=10;
    public static final int Identifier=11;
    public static final int NewLine=12;
    public static final int WS=13;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public AsmY86Parser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public AsmY86Parser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return AsmY86Parser.tokenNames; }
    public String getGrammarFileName() { return "/Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g"; }


    public enum LineType {INSTRUCTION, DATA, NULL};
    Memory memory;
    LineType lineType;
    int pc;
    int opCode;
    int[] op = new int[4];
    int opLength;
    String label;
    String comment;
    int dataSize;
    int dataValue;
    int dataCount;
    int pass;

    void init (Memory aMemory, int startingAddress) {
      memory      = aMemory;
      pc          = startingAddress;
      lineType    = LineType.NULL;
      comment     = "";
      label       = "";
    }

    public void checkSyntax (Memory aMemory, int startingAddress) throws Assembler.AssemblyException {
      init (aMemory, startingAddress);
      pass = 0;
      try {
        program ();
      } catch (RecognitionException e) {
        throw new Assembler.AssemblyException ("");
      }
    }

    public void passOne (Memory aMemory, int startingAddress) throws Assembler.AssemblyException {
      init (aMemory, startingAddress);
      pass = 1;
      try {
        program ();
      } catch (RecognitionException e) {
        throw new Assembler.AssemblyException ("");
      }
    }

    public void passTwo (Memory aMemory, int startingAddress) throws Assembler.AssemblyException {
      init (aMemory, startingAddress);
      pass = 2;
      try {
        program ();
      } catch (RecognitionException e) {
        throw new Assembler.AssemblyException ("");
      }
    }

    @Override
    public void emitErrorMessage(String msg) {
      throw new Assembler.AssemblyException (msg);
    }

    int getLabelValue (String label) {
      Integer value = memory.getLabelMap ().getAddress (label);
      if (value==null) {
        if (pass==1)
          value = pc;
        else
          emitErrorMessage (String.format ("Label not found: %s at address %d", label, pc));
      }
      return value.intValue ();
    }

    void writeLine () throws RecognitionException {
      MemoryCell cell = null;
      switch (lineType) {
        case INSTRUCTION:
          try {
            cell = Instruction.valueOf (memory, pc, opCode, op, label, comment);
            if (cell==null)
              throw new RecognitionException ();
            if (pass==1 && !label.trim ().equals ("")) 
              memory.addLabelOnly (cell);
            else if (pass==2)
              memory.add (cell);
            label = "";
            comment = "";
            pc += cell.length ();
          } catch (IndexOutOfBoundsException e) {
            throw new RecognitionException ();
          }
          break;
        case DATA:
          for (int i=0; i<dataCount; i++) {
            cell = Datum.valueOf (memory, pc, dataValue, dataSize, label, comment);
            if (cell==null)
              throw new RecognitionException ();
            if (pass==1 && !label.trim ().equals (""))
              memory.addLabelOnly (cell);
            else if (pass==2)
              memory.add (cell);
            label = "";
            comment = "";
            pc += dataSize;
          }
          label = "";
          comment = "";
          break;
        default:
      }
      lineType = LineType.NULL;
      op[0]=0;
      op[1]=0;
      op[2]=0;
      op[3]=0;
    }



    // $ANTLR start "program"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:144:1: program : ( line )* ( lineZ )? ;
    public final void program() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:144:9: ( ( line )* ( lineZ )? )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:144:11: ( line )* ( lineZ )?
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:144:11: ( line )*
            loop1:
            do {
                int alt1=2;
                alt1 = dfa1.predict(input);
                switch (alt1) {
            	case 1 :
            	    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:144:11: line
            	    {
            	    pushFollow(FOLLOW_line_in_program46);
            	    line();

            	    state._fsp--;


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:144:17: ( lineZ )?
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==Identifier||(LA2_0 >= 28 && LA2_0 <= 32)||(LA2_0 >= 34 && LA2_0 <= 71)) ) {
                alt2=1;
            }
            switch (alt2) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:144:17: lineZ
                    {
                    pushFollow(FOLLOW_lineZ_in_program49);
                    lineZ();

                    state._fsp--;


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
    // $ANTLR end "program"



    // $ANTLR start "line"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:1: line : ( labelDeclaration )? ( instruction | directive )? ( NewLine | ( Comment ) ) ;
    public final void line() throws RecognitionException {
        Token Comment1=null;

        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:6: ( ( labelDeclaration )? ( instruction | directive )? ( NewLine | ( Comment ) ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:8: ( labelDeclaration )? ( instruction | directive )? ( NewLine | ( Comment ) )
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:8: ( labelDeclaration )?
            int alt3=2;
            switch ( input.LA(1) ) {
                case Identifier:
                    {
                    alt3=1;
                    }
                    break;
                case 64:
                    {
                    int LA3_2 = input.LA(2);

                    if ( (LA3_2==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 44:
                    {
                    int LA3_3 = input.LA(2);

                    if ( (LA3_3==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 69:
                    {
                    int LA3_4 = input.LA(2);

                    if ( (LA3_4==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 41:
                    {
                    int LA3_5 = input.LA(2);

                    if ( (LA3_5==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 40:
                    {
                    int LA3_6 = input.LA(2);

                    if ( (LA3_6==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 37:
                    {
                    int LA3_7 = input.LA(2);

                    if ( (LA3_7==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 42:
                    {
                    int LA3_8 = input.LA(2);

                    if ( (LA3_8==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 39:
                    {
                    int LA3_9 = input.LA(2);

                    if ( (LA3_9==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 38:
                    {
                    int LA3_10 = input.LA(2);

                    if ( (LA3_10==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 50:
                    {
                    int LA3_11 = input.LA(2);

                    if ( (LA3_11==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 68:
                    {
                    int LA3_12 = input.LA(2);

                    if ( (LA3_12==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 62:
                    {
                    int LA3_13 = input.LA(2);

                    if ( (LA3_13==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 34:
                    {
                    int LA3_14 = input.LA(2);

                    if ( (LA3_14==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 70:
                    {
                    int LA3_15 = input.LA(2);

                    if ( (LA3_15==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 35:
                    {
                    int LA3_16 = input.LA(2);

                    if ( (LA3_16==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 71:
                    {
                    int LA3_17 = input.LA(2);

                    if ( (LA3_17==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 63:
                    {
                    int LA3_18 = input.LA(2);

                    if ( (LA3_18==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 43:
                    {
                    int LA3_19 = input.LA(2);

                    if ( (LA3_19==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 61:
                    {
                    int LA3_20 = input.LA(2);

                    if ( (LA3_20==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 57:
                    {
                    int LA3_21 = input.LA(2);

                    if ( (LA3_21==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 56:
                    {
                    int LA3_22 = input.LA(2);

                    if ( (LA3_22==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 53:
                    {
                    int LA3_23 = input.LA(2);

                    if ( (LA3_23==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 59:
                    {
                    int LA3_24 = input.LA(2);

                    if ( (LA3_24==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 55:
                    {
                    int LA3_25 = input.LA(2);

                    if ( (LA3_25==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 54:
                    {
                    int LA3_26 = input.LA(2);

                    if ( (LA3_26==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 36:
                    {
                    int LA3_27 = input.LA(2);

                    if ( (LA3_27==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 66:
                    {
                    int LA3_28 = input.LA(2);

                    if ( (LA3_28==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 65:
                    {
                    int LA3_30 = input.LA(2);

                    if ( (LA3_30==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 45:
                    {
                    int LA3_31 = input.LA(2);

                    if ( (LA3_31==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 51:
                    {
                    int LA3_32 = input.LA(2);

                    if ( (LA3_32==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 46:
                    {
                    int LA3_33 = input.LA(2);

                    if ( (LA3_33==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 52:
                    {
                    int LA3_34 = input.LA(2);

                    if ( (LA3_34==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 49:
                    {
                    int LA3_35 = input.LA(2);

                    if ( (LA3_35==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 47:
                    {
                    int LA3_36 = input.LA(2);

                    if ( (LA3_36==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 48:
                    {
                    int LA3_37 = input.LA(2);

                    if ( (LA3_37==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 60:
                    {
                    int LA3_38 = input.LA(2);

                    if ( (LA3_38==33) ) {
                        alt3=1;
                    }
                    }
                    break;
                case 58:
                    {
                    int LA3_39 = input.LA(2);

                    if ( (LA3_39==33) ) {
                        alt3=1;
                    }
                    }
                    break;
            }

            switch (alt3) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:9: labelDeclaration
                    {
                    pushFollow(FOLLOW_labelDeclaration_in_line59);
                    labelDeclaration();

                    state._fsp--;


                    }
                    break;

            }


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:28: ( instruction | directive )?
            int alt4=3;
            int LA4_0 = input.LA(1);

            if ( ((LA4_0 >= 34 && LA4_0 <= 71)) ) {
                alt4=1;
            }
            else if ( ((LA4_0 >= 28 && LA4_0 <= 32)) ) {
                alt4=2;
            }
            switch (alt4) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:30: instruction
                    {
                    pushFollow(FOLLOW_instruction_in_line65);
                    instruction();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:44: directive
                    {
                    pushFollow(FOLLOW_directive_in_line69);
                    directive();

                    state._fsp--;


                    }
                    break;

            }


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:57: ( NewLine | ( Comment ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==NewLine) ) {
                alt5=1;
            }
            else if ( (LA5_0==Comment) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:59: NewLine
                    {
                    match(input,NewLine,FOLLOW_NewLine_in_line76); 

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:69: ( Comment )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:69: ( Comment )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:146:70: Comment
                    {
                    Comment1=(Token)match(input,Comment,FOLLOW_Comment_in_line81); 

                     comment = (Comment1!=null?Comment1.getText():null).substring(1).trim(); 

                    }


                    }
                    break;

            }


            writeLine ();

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
    // $ANTLR end "line"



    // $ANTLR start "lineZ"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:1: lineZ : ( labelDeclaration )? ( instruction | directive ) ( EOF | ( CommentZ ) ) ;
    public final void lineZ() throws RecognitionException {
        Token CommentZ2=null;

        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:7: ( ( labelDeclaration )? ( instruction | directive ) ( EOF | ( CommentZ ) ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:9: ( labelDeclaration )? ( instruction | directive ) ( EOF | ( CommentZ ) )
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:9: ( labelDeclaration )?
            int alt6=2;
            switch ( input.LA(1) ) {
                case Identifier:
                    {
                    alt6=1;
                    }
                    break;
                case 64:
                    {
                    int LA6_2 = input.LA(2);

                    if ( (LA6_2==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 44:
                    {
                    int LA6_3 = input.LA(2);

                    if ( (LA6_3==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 69:
                    {
                    int LA6_4 = input.LA(2);

                    if ( (LA6_4==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 41:
                    {
                    int LA6_5 = input.LA(2);

                    if ( (LA6_5==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 40:
                    {
                    int LA6_6 = input.LA(2);

                    if ( (LA6_6==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 37:
                    {
                    int LA6_7 = input.LA(2);

                    if ( (LA6_7==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 42:
                    {
                    int LA6_8 = input.LA(2);

                    if ( (LA6_8==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 39:
                    {
                    int LA6_9 = input.LA(2);

                    if ( (LA6_9==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 38:
                    {
                    int LA6_10 = input.LA(2);

                    if ( (LA6_10==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 50:
                    {
                    int LA6_11 = input.LA(2);

                    if ( (LA6_11==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 68:
                    {
                    int LA6_12 = input.LA(2);

                    if ( (LA6_12==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 62:
                    {
                    int LA6_13 = input.LA(2);

                    if ( (LA6_13==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 34:
                    {
                    int LA6_14 = input.LA(2);

                    if ( (LA6_14==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 70:
                    {
                    int LA6_15 = input.LA(2);

                    if ( (LA6_15==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 35:
                    {
                    int LA6_16 = input.LA(2);

                    if ( (LA6_16==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 71:
                    {
                    int LA6_17 = input.LA(2);

                    if ( (LA6_17==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 63:
                    {
                    int LA6_18 = input.LA(2);

                    if ( (LA6_18==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 43:
                    {
                    int LA6_19 = input.LA(2);

                    if ( (LA6_19==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 61:
                    {
                    int LA6_20 = input.LA(2);

                    if ( (LA6_20==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 57:
                    {
                    int LA6_21 = input.LA(2);

                    if ( (LA6_21==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 56:
                    {
                    int LA6_22 = input.LA(2);

                    if ( (LA6_22==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 53:
                    {
                    int LA6_23 = input.LA(2);

                    if ( (LA6_23==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 59:
                    {
                    int LA6_24 = input.LA(2);

                    if ( (LA6_24==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 55:
                    {
                    int LA6_25 = input.LA(2);

                    if ( (LA6_25==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 54:
                    {
                    int LA6_26 = input.LA(2);

                    if ( (LA6_26==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 36:
                    {
                    int LA6_27 = input.LA(2);

                    if ( (LA6_27==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 66:
                    {
                    int LA6_28 = input.LA(2);

                    if ( (LA6_28==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 65:
                    {
                    int LA6_30 = input.LA(2);

                    if ( (LA6_30==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 45:
                    {
                    int LA6_31 = input.LA(2);

                    if ( (LA6_31==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 51:
                    {
                    int LA6_32 = input.LA(2);

                    if ( (LA6_32==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 46:
                    {
                    int LA6_33 = input.LA(2);

                    if ( (LA6_33==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 52:
                    {
                    int LA6_34 = input.LA(2);

                    if ( (LA6_34==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 49:
                    {
                    int LA6_35 = input.LA(2);

                    if ( (LA6_35==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 47:
                    {
                    int LA6_36 = input.LA(2);

                    if ( (LA6_36==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 48:
                    {
                    int LA6_37 = input.LA(2);

                    if ( (LA6_37==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 60:
                    {
                    int LA6_38 = input.LA(2);

                    if ( (LA6_38==33) ) {
                        alt6=1;
                    }
                    }
                    break;
                case 58:
                    {
                    int LA6_39 = input.LA(2);

                    if ( (LA6_39==33) ) {
                        alt6=1;
                    }
                    }
                    break;
            }

            switch (alt6) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:10: labelDeclaration
                    {
                    pushFollow(FOLLOW_labelDeclaration_in_lineZ101);
                    labelDeclaration();

                    state._fsp--;


                    }
                    break;

            }


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:29: ( instruction | directive )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( ((LA7_0 >= 34 && LA7_0 <= 71)) ) {
                alt7=1;
            }
            else if ( ((LA7_0 >= 28 && LA7_0 <= 32)) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;

            }
            switch (alt7) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:31: instruction
                    {
                    pushFollow(FOLLOW_instruction_in_lineZ107);
                    instruction();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:45: directive
                    {
                    pushFollow(FOLLOW_directive_in_lineZ111);
                    directive();

                    state._fsp--;


                    }
                    break;

            }


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:57: ( EOF | ( CommentZ ) )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==EOF) ) {
                alt8=1;
            }
            else if ( (LA8_0==CommentZ) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;

            }
            switch (alt8) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:59: EOF
                    {
                    match(input,EOF,FOLLOW_EOF_in_lineZ117); 

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:65: ( CommentZ )
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:65: ( CommentZ )
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:149:66: CommentZ
                    {
                    CommentZ2=(Token)match(input,CommentZ,FOLLOW_CommentZ_in_lineZ122); 

                     comment = (CommentZ2!=null?CommentZ2.getText():null).substring(1).trim(); 

                    }


                    }
                    break;

            }


            writeLine ();

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
    // $ANTLR end "lineZ"


    public static class labelDeclaration_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "labelDeclaration"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:152:1: labelDeclaration : ( Identifier | operand ) ':' ;
    public final AsmY86Parser.labelDeclaration_return labelDeclaration() throws RecognitionException {
        AsmY86Parser.labelDeclaration_return retval = new AsmY86Parser.labelDeclaration_return();
        retval.start = input.LT(1);


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:153:2: ( ( Identifier | operand ) ':' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:153:4: ( Identifier | operand ) ':'
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:153:4: ( Identifier | operand )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==Identifier) ) {
                alt9=1;
            }
            else if ( ((LA9_0 >= 34 && LA9_0 <= 66)||(LA9_0 >= 68 && LA9_0 <= 71)) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;

            }
            switch (alt9) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:153:5: Identifier
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_labelDeclaration142); 

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:153:18: operand
                    {
                    pushFollow(FOLLOW_operand_in_labelDeclaration146);
                    operand();

                    state._fsp--;


                    }
                    break;

            }


            match(input,33,FOLLOW_33_in_labelDeclaration149); 

            label = input.toString(retval.start,input.LT(-1)).substring (0, input.toString(retval.start,input.LT(-1)).length ()-1);

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
    // $ANTLR end "labelDeclaration"


    public static class label_return extends ParserRuleReturnScope {
        public int value;
    };


    // $ANTLR start "label"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:154:1: label returns [int value] : ( Identifier | operand ) ;
    public final AsmY86Parser.label_return label() throws RecognitionException {
        AsmY86Parser.label_return retval = new AsmY86Parser.label_return();
        retval.start = input.LT(1);


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:155:2: ( ( Identifier | operand ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:155:4: ( Identifier | operand )
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:155:4: ( Identifier | operand )
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0==Identifier) ) {
                alt10=1;
            }
            else if ( ((LA10_0 >= 34 && LA10_0 <= 66)||(LA10_0 >= 68 && LA10_0 <= 71)) ) {
                alt10=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 10, 0, input);

                throw nvae;

            }
            switch (alt10) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:155:5: Identifier
                    {
                    match(input,Identifier,FOLLOW_Identifier_in_label164); 

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:155:18: operand
                    {
                    pushFollow(FOLLOW_operand_in_label168);
                    operand();

                    state._fsp--;


                    }
                    break;

            }


            retval.value = getLabelValue (input.toString(retval.start,input.LT(-1)));

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
    // $ANTLR end "label"



    // $ANTLR start "instruction"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:158:1: instruction : ( nop | halt | rrmovxx | irmovl | rmmovl | mrmovl | opl | jxx | call | ret | pushl | popl | iopl | leave | jmp ) ;
    public final void instruction() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:2: ( ( nop | halt | rrmovxx | irmovl | rmmovl | mrmovl | opl | jxx | call | ret | pushl | popl | iopl | leave | jmp ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:4: ( nop | halt | rrmovxx | irmovl | rmmovl | mrmovl | opl | jxx | call | ret | pushl | popl | iopl | leave | jmp )
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:4: ( nop | halt | rrmovxx | irmovl | rmmovl | mrmovl | opl | jxx | call | ret | pushl | popl | iopl | leave | jmp )
            int alt11=15;
            switch ( input.LA(1) ) {
            case 64:
                {
                alt11=1;
                }
                break;
            case 44:
                {
                alt11=2;
                }
                break;
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 69:
                {
                alt11=3;
                }
                break;
            case 50:
                {
                alt11=4;
                }
                break;
            case 68:
                {
                alt11=5;
                }
                break;
            case 62:
                {
                alt11=6;
                }
                break;
            case 34:
            case 35:
            case 43:
            case 61:
            case 63:
            case 70:
            case 71:
                {
                alt11=7;
                }
                break;
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 59:
                {
                alt11=8;
                }
                break;
            case 36:
                {
                alt11=9;
                }
                break;
            case 67:
                {
                alt11=10;
                }
                break;
            case 66:
                {
                alt11=11;
                }
                break;
            case 65:
                {
                alt11=12;
                }
                break;
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 51:
            case 52:
                {
                alt11=13;
                }
                break;
            case 60:
                {
                alt11=14;
                }
                break;
            case 58:
                {
                alt11=15;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;

            }

            switch (alt11) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:5: nop
                    {
                    pushFollow(FOLLOW_nop_in_instruction182);
                    nop();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:11: halt
                    {
                    pushFollow(FOLLOW_halt_in_instruction186);
                    halt();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:18: rrmovxx
                    {
                    pushFollow(FOLLOW_rrmovxx_in_instruction190);
                    rrmovxx();

                    state._fsp--;


                    }
                    break;
                case 4 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:28: irmovl
                    {
                    pushFollow(FOLLOW_irmovl_in_instruction194);
                    irmovl();

                    state._fsp--;


                    }
                    break;
                case 5 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:37: rmmovl
                    {
                    pushFollow(FOLLOW_rmmovl_in_instruction198);
                    rmmovl();

                    state._fsp--;


                    }
                    break;
                case 6 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:46: mrmovl
                    {
                    pushFollow(FOLLOW_mrmovl_in_instruction202);
                    mrmovl();

                    state._fsp--;


                    }
                    break;
                case 7 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:55: opl
                    {
                    pushFollow(FOLLOW_opl_in_instruction206);
                    opl();

                    state._fsp--;


                    }
                    break;
                case 8 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:61: jxx
                    {
                    pushFollow(FOLLOW_jxx_in_instruction210);
                    jxx();

                    state._fsp--;


                    }
                    break;
                case 9 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:67: call
                    {
                    pushFollow(FOLLOW_call_in_instruction214);
                    call();

                    state._fsp--;


                    }
                    break;
                case 10 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:74: ret
                    {
                    pushFollow(FOLLOW_ret_in_instruction218);
                    ret();

                    state._fsp--;


                    }
                    break;
                case 11 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:80: pushl
                    {
                    pushFollow(FOLLOW_pushl_in_instruction222);
                    pushl();

                    state._fsp--;


                    }
                    break;
                case 12 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:88: popl
                    {
                    pushFollow(FOLLOW_popl_in_instruction226);
                    popl();

                    state._fsp--;


                    }
                    break;
                case 13 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:95: iopl
                    {
                    pushFollow(FOLLOW_iopl_in_instruction230);
                    iopl();

                    state._fsp--;


                    }
                    break;
                case 14 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:102: leave
                    {
                    pushFollow(FOLLOW_leave_in_instruction234);
                    leave();

                    state._fsp--;


                    }
                    break;
                case 15 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:159:110: jmp
                    {
                    pushFollow(FOLLOW_jmp_in_instruction238);
                    jmp();

                    state._fsp--;


                    }
                    break;

            }


            lineType = LineType.INSTRUCTION;

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
    // $ANTLR end "instruction"



    // $ANTLR start "operand"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:162:1: operand : ( 'halt' | 'nop' | 'rrmovl' | 'cmovle' | 'cmovl' | 'cmove' | 'cmovne' | 'cmovge' | 'cmovg' | 'irmovl' | 'rmmovl' | 'mrmovl' | 'addl' | 'subl' | 'andl' | 'xorl' | 'mull' | 'divl' | 'modl' | 'iaddl' | 'isubl' | 'iandl' | 'ixorl' | 'imull' | 'idivl' | 'imodl' | 'jmp' | 'jle' | 'jl' | 'je' | 'jne' | 'jge' | 'jg' | 'call' | 'pushl' | 'popl' | 'leave' ) ;
    public final void operand() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:162:9: ( ( 'halt' | 'nop' | 'rrmovl' | 'cmovle' | 'cmovl' | 'cmove' | 'cmovne' | 'cmovge' | 'cmovg' | 'irmovl' | 'rmmovl' | 'mrmovl' | 'addl' | 'subl' | 'andl' | 'xorl' | 'mull' | 'divl' | 'modl' | 'iaddl' | 'isubl' | 'iandl' | 'ixorl' | 'imull' | 'idivl' | 'imodl' | 'jmp' | 'jle' | 'jl' | 'je' | 'jne' | 'jge' | 'jg' | 'call' | 'pushl' | 'popl' | 'leave' ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:
            {
            if ( (input.LA(1) >= 34 && input.LA(1) <= 66)||(input.LA(1) >= 68 && input.LA(1) <= 71) ) {
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
    // $ANTLR end "operand"



    // $ANTLR start "halt"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:167:1: halt : 'halt' ;
    public final void halt() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:167:6: ( 'halt' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:167:8: 'halt'
            {
            match(input,44,FOLLOW_44_in_halt422); 

            opCode = AbstractY86CPU.I_HALT << 4;

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
    // $ANTLR end "halt"



    // $ANTLR start "nop"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:169:1: nop : 'nop' ;
    public final void nop() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:169:5: ( 'nop' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:169:7: 'nop'
            {
            match(input,64,FOLLOW_64_in_nop432); 

            opCode = AbstractY86CPU.I_NOP << 4;

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
    // $ANTLR end "nop"



    // $ANTLR start "rrmovxx"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:171:1: rrmovxx : ( 'rrmovl' | 'cmovle' | 'cmovl' | 'cmove' | 'cmovne' | 'cmovge' | 'cmovg' ) src= register ',' dst= register ;
    public final void rrmovxx() throws RecognitionException {
        int src =0;

        int dst =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:171:9: ( ( 'rrmovl' | 'cmovle' | 'cmovl' | 'cmove' | 'cmovne' | 'cmovge' | 'cmovg' ) src= register ',' dst= register )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:171:11: ( 'rrmovl' | 'cmovle' | 'cmovl' | 'cmove' | 'cmovne' | 'cmovge' | 'cmovg' ) src= register ',' dst= register
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:171:11: ( 'rrmovl' | 'cmovle' | 'cmovl' | 'cmove' | 'cmovne' | 'cmovge' | 'cmovg' )
            int alt12=7;
            switch ( input.LA(1) ) {
            case 69:
                {
                alt12=1;
                }
                break;
            case 41:
                {
                alt12=2;
                }
                break;
            case 40:
                {
                alt12=3;
                }
                break;
            case 37:
                {
                alt12=4;
                }
                break;
            case 42:
                {
                alt12=5;
                }
                break;
            case 39:
                {
                alt12=6;
                }
                break;
            case 38:
                {
                alt12=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 12, 0, input);

                throw nvae;

            }

            switch (alt12) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:171:12: 'rrmovl'
                    {
                    match(input,69,FOLLOW_69_in_rrmovxx443); 

                    opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_NC;

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:172:12: 'cmovle'
                    {
                    match(input,41,FOLLOW_41_in_rrmovxx460); 

                    opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_LE;

                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:173:12: 'cmovl'
                    {
                    match(input,40,FOLLOW_40_in_rrmovxx478); 

                    opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_L;

                    }
                    break;
                case 4 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:174:12: 'cmove'
                    {
                    match(input,37,FOLLOW_37_in_rrmovxx498); 

                    opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_E;

                    }
                    break;
                case 5 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:175:12: 'cmovne'
                    {
                    match(input,42,FOLLOW_42_in_rrmovxx517); 

                    opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_NE;

                    }
                    break;
                case 6 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:176:10: 'cmovge'
                    {
                    match(input,39,FOLLOW_39_in_rrmovxx532); 

                    opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_GE;

                    }
                    break;
                case 7 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:177:10: 'cmovg'
                    {
                    match(input,38,FOLLOW_38_in_rrmovxx547); 

                    opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_G;

                    }
                    break;

            }


            pushFollow(FOLLOW_register_in_rrmovxx564);
            src=register();

            state._fsp--;


            match(input,26,FOLLOW_26_in_rrmovxx566); 

            pushFollow(FOLLOW_register_in_rrmovxx570);
            dst=register();

            state._fsp--;


            op[0]=src; op[1]=dst;

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
    // $ANTLR end "rrmovxx"



    // $ANTLR start "irmovl"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:180:1: irmovl : 'irmovl' immediate ',' register ;
    public final void irmovl() throws RecognitionException {
        int register3 =0;

        int immediate4 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:180:8: ( 'irmovl' immediate ',' register )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:180:10: 'irmovl' immediate ',' register
            {
            match(input,50,FOLLOW_50_in_irmovl580); 

            pushFollow(FOLLOW_immediate_in_irmovl582);
            immediate4=immediate();

            state._fsp--;


            match(input,26,FOLLOW_26_in_irmovl584); 

            pushFollow(FOLLOW_register_in_irmovl586);
            register3=register();

            state._fsp--;


             opCode = AbstractY86CPU.I_IRMOVL << 4; op[0]= AbstractY86CPU.R_NONE; op[1]=register3; op[2]=immediate4; 

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
    // $ANTLR end "irmovl"



    // $ANTLR start "rmmovl"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:182:1: rmmovl : 'rmmovl' register ',' baseOffset ;
    public final void rmmovl() throws RecognitionException {
        AsmY86Parser.baseOffset_return baseOffset5 =null;

        int register6 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:182:9: ( 'rmmovl' register ',' baseOffset )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:182:11: 'rmmovl' register ',' baseOffset
            {
            match(input,68,FOLLOW_68_in_rmmovl597); 

            pushFollow(FOLLOW_register_in_rmmovl599);
            register6=register();

            state._fsp--;


            match(input,26,FOLLOW_26_in_rmmovl601); 

            pushFollow(FOLLOW_baseOffset_in_rmmovl602);
            baseOffset5=baseOffset();

            state._fsp--;


             opCode = (AbstractY86CPU.I_RMMOVL << 4) + (baseOffset5!=null?baseOffset5.scale:0); op[0]=register6; op[1]=(baseOffset5!=null?baseOffset5.base:0); op[2]=(baseOffset5!=null?baseOffset5.offset:0); 

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
    // $ANTLR end "rmmovl"



    // $ANTLR start "mrmovl"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:184:1: mrmovl : 'mrmovl' baseOffset ',' register ;
    public final void mrmovl() throws RecognitionException {
        AsmY86Parser.baseOffset_return baseOffset7 =null;

        int register8 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:184:8: ( 'mrmovl' baseOffset ',' register )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:184:10: 'mrmovl' baseOffset ',' register
            {
            match(input,62,FOLLOW_62_in_mrmovl612); 

            pushFollow(FOLLOW_baseOffset_in_mrmovl614);
            baseOffset7=baseOffset();

            state._fsp--;


            match(input,26,FOLLOW_26_in_mrmovl616); 

            pushFollow(FOLLOW_register_in_mrmovl618);
            register8=register();

            state._fsp--;


             opCode = (AbstractY86CPU.I_MRMOVL << 4) + (baseOffset7!=null?baseOffset7.scale:0); op[0]=register8; op[1]=(baseOffset7!=null?baseOffset7.base:0); op[2]=(baseOffset7!=null?baseOffset7.offset:0); 

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
    // $ANTLR end "mrmovl"



    // $ANTLR start "opl"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:186:1: opl : ( 'addl' | 'subl' | 'andl' | 'xorl' | 'mull' | 'divl' | 'modl' ) a= register ',' b= register ;
    public final void opl() throws RecognitionException {
        int a =0;

        int b =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:186:5: ( ( 'addl' | 'subl' | 'andl' | 'xorl' | 'mull' | 'divl' | 'modl' ) a= register ',' b= register )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:186:7: ( 'addl' | 'subl' | 'andl' | 'xorl' | 'mull' | 'divl' | 'modl' ) a= register ',' b= register
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:186:7: ( 'addl' | 'subl' | 'andl' | 'xorl' | 'mull' | 'divl' | 'modl' )
            int alt13=7;
            switch ( input.LA(1) ) {
            case 34:
                {
                alt13=1;
                }
                break;
            case 70:
                {
                alt13=2;
                }
                break;
            case 35:
                {
                alt13=3;
                }
                break;
            case 71:
                {
                alt13=4;
                }
                break;
            case 63:
                {
                alt13=5;
                }
                break;
            case 43:
                {
                alt13=6;
                }
                break;
            case 61:
                {
                alt13=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;

            }

            switch (alt13) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:186:8: 'addl'
                    {
                    match(input,34,FOLLOW_34_in_opl629); 

                    opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_ADDL;

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:187:8: 'subl'
                    {
                    match(input,70,FOLLOW_70_in_opl642); 

                    opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_SUBL;

                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:188:8: 'andl'
                    {
                    match(input,35,FOLLOW_35_in_opl655); 

                    opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_ANDL;

                    }
                    break;
                case 4 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:189:8: 'xorl'
                    {
                    match(input,71,FOLLOW_71_in_opl668); 

                    opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_XORL;

                    }
                    break;
                case 5 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:190:8: 'mull'
                    {
                    match(input,63,FOLLOW_63_in_opl681); 

                    opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_MULL;

                    }
                    break;
                case 6 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:191:8: 'divl'
                    {
                    match(input,43,FOLLOW_43_in_opl694); 

                    opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_DIVL;

                    }
                    break;
                case 7 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:192:8: 'modl'
                    {
                    match(input,61,FOLLOW_61_in_opl707); 

                    opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_MODL;

                    }
                    break;

            }


            pushFollow(FOLLOW_register_in_opl717);
            a=register();

            state._fsp--;


            match(input,26,FOLLOW_26_in_opl719); 

            pushFollow(FOLLOW_register_in_opl723);
            b=register();

            state._fsp--;


            op[0]=a; op[1]=b;

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
    // $ANTLR end "opl"



    // $ANTLR start "iopl"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:195:1: iopl : ( 'iaddl' | 'isubl' | 'iandl' | 'ixorl' | 'imull' | 'idivl' | 'imodl' ) immediate ',' register ;
    public final void iopl() throws RecognitionException {
        int register9 =0;

        int immediate10 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:195:6: ( ( 'iaddl' | 'isubl' | 'iandl' | 'ixorl' | 'imull' | 'idivl' | 'imodl' ) immediate ',' register )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:195:8: ( 'iaddl' | 'isubl' | 'iandl' | 'ixorl' | 'imull' | 'idivl' | 'imodl' ) immediate ',' register
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:195:8: ( 'iaddl' | 'isubl' | 'iandl' | 'ixorl' | 'imull' | 'idivl' | 'imodl' )
            int alt14=7;
            switch ( input.LA(1) ) {
            case 45:
                {
                alt14=1;
                }
                break;
            case 51:
                {
                alt14=2;
                }
                break;
            case 46:
                {
                alt14=3;
                }
                break;
            case 52:
                {
                alt14=4;
                }
                break;
            case 49:
                {
                alt14=5;
                }
                break;
            case 47:
                {
                alt14=6;
                }
                break;
            case 48:
                {
                alt14=7;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;

            }

            switch (alt14) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:195:9: 'iaddl'
                    {
                    match(input,45,FOLLOW_45_in_iopl734); 

                    opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_ADDL;

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:196:10: 'isubl'
                    {
                    match(input,51,FOLLOW_51_in_iopl749); 

                    opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_SUBL;

                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:197:10: 'iandl'
                    {
                    match(input,46,FOLLOW_46_in_iopl764); 

                    opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_ANDL;

                    }
                    break;
                case 4 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:198:10: 'ixorl'
                    {
                    match(input,52,FOLLOW_52_in_iopl779); 

                    opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_XORL;

                    }
                    break;
                case 5 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:199:10: 'imull'
                    {
                    match(input,49,FOLLOW_49_in_iopl794); 

                    opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_MULL;

                    }
                    break;
                case 6 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:200:10: 'idivl'
                    {
                    match(input,47,FOLLOW_47_in_iopl809); 

                    opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_DIVL;

                    }
                    break;
                case 7 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:201:10: 'imodl'
                    {
                    match(input,48,FOLLOW_48_in_iopl824); 

                    opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_MODL;

                    }
                    break;

            }


            pushFollow(FOLLOW_immediate_in_iopl832);
            immediate10=immediate();

            state._fsp--;


            match(input,26,FOLLOW_26_in_iopl834); 

            pushFollow(FOLLOW_register_in_iopl836);
            register9=register();

            state._fsp--;


            op[0]=AbstractY86CPU.R_NONE; op[1]=register9; op[2]=immediate10;

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
    // $ANTLR end "iopl"



    // $ANTLR start "jxx"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:204:1: jxx : ( 'jle' | 'jl' | 'je' | 'jne' | 'jge' | 'jg' ) literal ;
    public final void jxx() throws RecognitionException {
        int literal11 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:204:5: ( ( 'jle' | 'jl' | 'je' | 'jne' | 'jge' | 'jg' ) literal )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:204:7: ( 'jle' | 'jl' | 'je' | 'jne' | 'jge' | 'jg' ) literal
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:204:7: ( 'jle' | 'jl' | 'je' | 'jne' | 'jge' | 'jg' )
            int alt15=6;
            switch ( input.LA(1) ) {
            case 57:
                {
                alt15=1;
                }
                break;
            case 56:
                {
                alt15=2;
                }
                break;
            case 53:
                {
                alt15=3;
                }
                break;
            case 59:
                {
                alt15=4;
                }
                break;
            case 55:
                {
                alt15=5;
                }
                break;
            case 54:
                {
                alt15=6;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 15, 0, input);

                throw nvae;

            }

            switch (alt15) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:204:8: 'jle'
                    {
                    match(input,57,FOLLOW_57_in_jxx847); 

                    opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_LE;

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:205:8: 'jl'
                    {
                    match(input,56,FOLLOW_56_in_jxx861); 

                    opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_L;

                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:206:8: 'je'
                    {
                    match(input,53,FOLLOW_53_in_jxx876); 

                    opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_E;

                    }
                    break;
                case 4 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:207:6: 'jne'
                    {
                    match(input,59,FOLLOW_59_in_jxx890); 

                    opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_NE;

                    }
                    break;
                case 5 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:208:6: 'jge'
                    {
                    match(input,55,FOLLOW_55_in_jxx901); 

                    opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_GE;

                    }
                    break;
                case 6 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:209:6: 'jg'
                    {
                    match(input,54,FOLLOW_54_in_jxx912); 

                    opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_G;

                    }
                    break;

            }


            pushFollow(FOLLOW_literal_in_jxx924);
            literal11=literal();

            state._fsp--;


             op[0] = literal11;

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
    // $ANTLR end "jxx"



    // $ANTLR start "call"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:212:1: call : 'call' ( literal |a= regIndirect |b= baseOffset ',' a= register | '*' b= baseOffset ',' a= register ) ;
    public final void call() throws RecognitionException {
        int a =0;

        AsmY86Parser.baseOffset_return b =null;

        int literal12 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:212:6: ( 'call' ( literal |a= regIndirect |b= baseOffset ',' a= register | '*' b= baseOffset ',' a= register ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:212:8: 'call' ( literal |a= regIndirect |b= baseOffset ',' a= register | '*' b= baseOffset ',' a= register )
            {
            match(input,36,FOLLOW_36_in_call937); 

            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:212:15: ( literal |a= regIndirect |b= baseOffset ',' a= register | '*' b= baseOffset ',' a= register )
            int alt16=4;
            switch ( input.LA(1) ) {
            case Identifier:
                {
                int LA16_1 = input.LA(2);

                if ( (LA16_1==EOF||(LA16_1 >= Comment && LA16_1 <= CommentZ)||LA16_1==NewLine) ) {
                    alt16=1;
                }
                else if ( (LA16_1==23) ) {
                    alt16=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;

                }
                }
                break;
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
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
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 68:
            case 69:
            case 70:
            case 71:
                {
                int LA16_2 = input.LA(2);

                if ( (LA16_2==EOF||(LA16_2 >= Comment && LA16_2 <= CommentZ)||LA16_2==NewLine) ) {
                    alt16=1;
                }
                else if ( (LA16_2==23) ) {
                    alt16=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 2, input);

                    throw nvae;

                }
                }
                break;
            case 27:
                {
                int LA16_3 = input.LA(2);

                if ( (LA16_3==Decimal) ) {
                    int LA16_4 = input.LA(3);

                    if ( (LA16_4==EOF||(LA16_4 >= Comment && LA16_4 <= CommentZ)||LA16_4==NewLine) ) {
                        alt16=1;
                    }
                    else if ( (LA16_4==23) ) {
                        alt16=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 4, input);

                        throw nvae;

                    }
                }
                else if ( (LA16_3==Hex) ) {
                    int LA16_5 = input.LA(3);

                    if ( (LA16_5==EOF||(LA16_5 >= Comment && LA16_5 <= CommentZ)||LA16_5==NewLine) ) {
                        alt16=1;
                    }
                    else if ( (LA16_5==23) ) {
                        alt16=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 5, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 3, input);

                    throw nvae;

                }
                }
                break;
            case Decimal:
                {
                int LA16_4 = input.LA(2);

                if ( (LA16_4==EOF||(LA16_4 >= Comment && LA16_4 <= CommentZ)||LA16_4==NewLine) ) {
                    alt16=1;
                }
                else if ( (LA16_4==23) ) {
                    alt16=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 4, input);

                    throw nvae;

                }
                }
                break;
            case Hex:
                {
                int LA16_5 = input.LA(2);

                if ( (LA16_5==EOF||(LA16_5 >= Comment && LA16_5 <= CommentZ)||LA16_5==NewLine) ) {
                    alt16=1;
                }
                else if ( (LA16_5==23) ) {
                    alt16=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 5, input);

                    throw nvae;

                }
                }
                break;
            case 23:
                {
                switch ( input.LA(2) ) {
                case 15:
                    {
                    int LA16_10 = input.LA(3);

                    if ( (LA16_10==24) ) {
                        int LA16_18 = input.LA(4);

                        if ( (LA16_18==EOF||(LA16_18 >= Comment && LA16_18 <= CommentZ)||LA16_18==NewLine) ) {
                            alt16=2;
                        }
                        else if ( (LA16_18==26) ) {
                            alt16=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 16, 18, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA16_10==26) ) {
                        alt16=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 10, input);

                        throw nvae;

                    }
                    }
                    break;
                case 18:
                    {
                    int LA16_11 = input.LA(3);

                    if ( (LA16_11==24) ) {
                        int LA16_18 = input.LA(4);

                        if ( (LA16_18==EOF||(LA16_18 >= Comment && LA16_18 <= CommentZ)||LA16_18==NewLine) ) {
                            alt16=2;
                        }
                        else if ( (LA16_18==26) ) {
                            alt16=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 16, 18, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA16_11==26) ) {
                        alt16=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 11, input);

                        throw nvae;

                    }
                    }
                    break;
                case 20:
                    {
                    int LA16_12 = input.LA(3);

                    if ( (LA16_12==24) ) {
                        int LA16_18 = input.LA(4);

                        if ( (LA16_18==EOF||(LA16_18 >= Comment && LA16_18 <= CommentZ)||LA16_18==NewLine) ) {
                            alt16=2;
                        }
                        else if ( (LA16_18==26) ) {
                            alt16=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 16, 18, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA16_12==26) ) {
                        alt16=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 12, input);

                        throw nvae;

                    }
                    }
                    break;
                case 17:
                    {
                    int LA16_13 = input.LA(3);

                    if ( (LA16_13==24) ) {
                        int LA16_18 = input.LA(4);

                        if ( (LA16_18==EOF||(LA16_18 >= Comment && LA16_18 <= CommentZ)||LA16_18==NewLine) ) {
                            alt16=2;
                        }
                        else if ( (LA16_18==26) ) {
                            alt16=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 16, 18, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA16_13==26) ) {
                        alt16=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 13, input);

                        throw nvae;

                    }
                    }
                    break;
                case 22:
                    {
                    int LA16_14 = input.LA(3);

                    if ( (LA16_14==24) ) {
                        int LA16_18 = input.LA(4);

                        if ( (LA16_18==EOF||(LA16_18 >= Comment && LA16_18 <= CommentZ)||LA16_18==NewLine) ) {
                            alt16=2;
                        }
                        else if ( (LA16_18==26) ) {
                            alt16=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 16, 18, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA16_14==26) ) {
                        alt16=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 14, input);

                        throw nvae;

                    }
                    }
                    break;
                case 16:
                    {
                    int LA16_15 = input.LA(3);

                    if ( (LA16_15==24) ) {
                        int LA16_18 = input.LA(4);

                        if ( (LA16_18==EOF||(LA16_18 >= Comment && LA16_18 <= CommentZ)||LA16_18==NewLine) ) {
                            alt16=2;
                        }
                        else if ( (LA16_18==26) ) {
                            alt16=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 16, 18, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA16_15==26) ) {
                        alt16=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 15, input);

                        throw nvae;

                    }
                    }
                    break;
                case 21:
                    {
                    int LA16_16 = input.LA(3);

                    if ( (LA16_16==24) ) {
                        int LA16_18 = input.LA(4);

                        if ( (LA16_18==EOF||(LA16_18 >= Comment && LA16_18 <= CommentZ)||LA16_18==NewLine) ) {
                            alt16=2;
                        }
                        else if ( (LA16_18==26) ) {
                            alt16=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 16, 18, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA16_16==26) ) {
                        alt16=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 16, input);

                        throw nvae;

                    }
                    }
                    break;
                case 19:
                    {
                    int LA16_17 = input.LA(3);

                    if ( (LA16_17==24) ) {
                        int LA16_18 = input.LA(4);

                        if ( (LA16_18==EOF||(LA16_18 >= Comment && LA16_18 <= CommentZ)||LA16_18==NewLine) ) {
                            alt16=2;
                        }
                        else if ( (LA16_18==26) ) {
                            alt16=3;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 16, 18, input);

                            throw nvae;

                        }
                    }
                    else if ( (LA16_17==26) ) {
                        alt16=3;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 16, 17, input);

                        throw nvae;

                    }
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 6, input);

                    throw nvae;

                }

                }
                break;
            case 25:
                {
                alt16=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;

            }

            switch (alt16) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:212:16: literal
                    {
                    pushFollow(FOLLOW_literal_in_call940);
                    literal12=literal();

                    state._fsp--;


                    opCode = (AbstractY86CPU.I_CALL << 4) | AbstractY86CPU.X_DIRECT_CALL; op[0]=literal12; 

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:213:14: a= regIndirect
                    {
                    pushFollow(FOLLOW_regIndirect_in_call962);
                    a=regIndirect();

                    state._fsp--;


                    opCode = (AbstractY86CPU.I_CALL << 4) | AbstractY86CPU.X_INDIRECT_CALL; op[0] = a; op[1] = AbstractY86CPU.R_NONE;

                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:214:17: b= baseOffset ',' a= register
                    {
                    pushFollow(FOLLOW_baseOffset_in_call986);
                    b=baseOffset();

                    state._fsp--;


                    match(input,26,FOLLOW_26_in_call988); 

                    pushFollow(FOLLOW_register_in_call992);
                    a=register();

                    state._fsp--;


                    opCode = (AbstractY86CPU.I_CALL << 4) | AbstractY86CPU.X_INDIRECT_FLAG | ((b!=null?b.scale:0) == 0 ? 0x1 : (b!=null?b.scale:0)); op[0]=a; op[1]=(b!=null?b.base:0); op[2]=(b!=null?b.offset:0); 

                    }
                    break;
                case 4 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:215:17: '*' b= baseOffset ',' a= register
                    {
                    match(input,25,FOLLOW_25_in_call1014); 

                    pushFollow(FOLLOW_baseOffset_in_call1018);
                    b=baseOffset();

                    state._fsp--;


                    match(input,26,FOLLOW_26_in_call1020); 

                    pushFollow(FOLLOW_register_in_call1024);
                    a=register();

                    state._fsp--;


                    opCode = (AbstractY86CPU.I_CALL << 4) | AbstractY86CPU.X_DBL_INDIRECT_FLAG | ((b!=null?b.scale:0) == 0 ? 0x1 : (b!=null?b.scale:0)); op[0]=a; op[1]=(b!=null?b.base:0); op[2]=(b!=null?b.offset:0); 

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
    // $ANTLR end "call"



    // $ANTLR start "ret"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:218:1: ret : 'ret' ;
    public final void ret() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:218:5: ( 'ret' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:218:7: 'ret'
            {
            match(input,67,FOLLOW_67_in_ret1048); 

            opCode = AbstractY86CPU.I_RET << 4;

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
    // $ANTLR end "ret"



    // $ANTLR start "pushl"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:220:1: pushl : 'pushl' register ;
    public final void pushl() throws RecognitionException {
        int register13 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:220:7: ( 'pushl' register )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:220:9: 'pushl' register
            {
            match(input,66,FOLLOW_66_in_pushl1058); 

            pushFollow(FOLLOW_register_in_pushl1060);
            register13=register();

            state._fsp--;


            opCode = AbstractY86CPU.I_PUSHL << 4; op[0] = register13; op[1] = AbstractY86CPU.R_NONE;

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
    // $ANTLR end "pushl"



    // $ANTLR start "popl"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:222:1: popl : 'popl' register ;
    public final void popl() throws RecognitionException {
        int register14 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:222:6: ( 'popl' register )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:222:8: 'popl' register
            {
            match(input,65,FOLLOW_65_in_popl1070); 

            pushFollow(FOLLOW_register_in_popl1072);
            register14=register();

            state._fsp--;


            opCode = AbstractY86CPU.I_POPL << 4; op[0] = register14; op[1] = AbstractY86CPU.R_NONE;

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
    // $ANTLR end "popl"



    // $ANTLR start "leave"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:224:1: leave : 'leave' ;
    public final void leave() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:224:7: ( 'leave' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:224:9: 'leave'
            {
            match(input,60,FOLLOW_60_in_leave1082); 

            opCode = AbstractY86CPU.I_LEAVE << 4;

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
    // $ANTLR end "leave"



    // $ANTLR start "jmp"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:226:1: jmp : 'jmp' ( literal |b= baseOffset | '*' b= baseOffset ) ;
    public final void jmp() throws RecognitionException {
        AsmY86Parser.baseOffset_return b =null;

        int literal15 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:226:5: ( 'jmp' ( literal |b= baseOffset | '*' b= baseOffset ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:226:7: 'jmp' ( literal |b= baseOffset | '*' b= baseOffset )
            {
            match(input,58,FOLLOW_58_in_jmp1092); 

            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:226:13: ( literal |b= baseOffset | '*' b= baseOffset )
            int alt17=3;
            switch ( input.LA(1) ) {
            case Identifier:
                {
                int LA17_1 = input.LA(2);

                if ( (LA17_1==EOF||(LA17_1 >= Comment && LA17_1 <= CommentZ)||LA17_1==NewLine) ) {
                    alt17=1;
                }
                else if ( (LA17_1==23) ) {
                    alt17=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;

                }
                }
                break;
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
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
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 68:
            case 69:
            case 70:
            case 71:
                {
                int LA17_2 = input.LA(2);

                if ( (LA17_2==EOF||(LA17_2 >= Comment && LA17_2 <= CommentZ)||LA17_2==NewLine) ) {
                    alt17=1;
                }
                else if ( (LA17_2==23) ) {
                    alt17=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 2, input);

                    throw nvae;

                }
                }
                break;
            case 27:
                {
                int LA17_3 = input.LA(2);

                if ( (LA17_3==Decimal) ) {
                    int LA17_4 = input.LA(3);

                    if ( (LA17_4==EOF||(LA17_4 >= Comment && LA17_4 <= CommentZ)||LA17_4==NewLine) ) {
                        alt17=1;
                    }
                    else if ( (LA17_4==23) ) {
                        alt17=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 4, input);

                        throw nvae;

                    }
                }
                else if ( (LA17_3==Hex) ) {
                    int LA17_5 = input.LA(3);

                    if ( (LA17_5==EOF||(LA17_5 >= Comment && LA17_5 <= CommentZ)||LA17_5==NewLine) ) {
                        alt17=1;
                    }
                    else if ( (LA17_5==23) ) {
                        alt17=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 5, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 3, input);

                    throw nvae;

                }
                }
                break;
            case Decimal:
                {
                int LA17_4 = input.LA(2);

                if ( (LA17_4==EOF||(LA17_4 >= Comment && LA17_4 <= CommentZ)||LA17_4==NewLine) ) {
                    alt17=1;
                }
                else if ( (LA17_4==23) ) {
                    alt17=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 4, input);

                    throw nvae;

                }
                }
                break;
            case Hex:
                {
                int LA17_5 = input.LA(2);

                if ( (LA17_5==EOF||(LA17_5 >= Comment && LA17_5 <= CommentZ)||LA17_5==NewLine) ) {
                    alt17=1;
                }
                else if ( (LA17_5==23) ) {
                    alt17=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 5, input);

                    throw nvae;

                }
                }
                break;
            case 23:
                {
                alt17=2;
                }
                break;
            case 25:
                {
                alt17=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;

            }

            switch (alt17) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:226:14: literal
                    {
                    pushFollow(FOLLOW_literal_in_jmp1095);
                    literal15=literal();

                    state._fsp--;


                    opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_NC; op[0] = literal15;

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:227:18: b= baseOffset
                    {
                    pushFollow(FOLLOW_baseOffset_in_jmp1120);
                    b=baseOffset();

                    state._fsp--;


                    opCode = (AbstractY86CPU.I_JMPI << 4) | AbstractY86CPU.X_INDIRECT_FLAG | ((b!=null?b.scale:0) == 0 ? 0x1 : (b!=null?b.scale:0)); op[0] = AbstractY86CPU.R_NONE; op[1]=(b!=null?b.base:0); op[2]=(b!=null?b.offset:0); 

                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:228:12: '*' b= baseOffset
                    {
                    match(input,25,FOLLOW_25_in_jmp1137); 

                    pushFollow(FOLLOW_baseOffset_in_jmp1141);
                    b=baseOffset();

                    state._fsp--;


                    opCode = (AbstractY86CPU.I_JMPI << 4) | AbstractY86CPU.X_DBL_INDIRECT_FLAG | ((b!=null?b.scale:0) == 0 ? 0x1 : (b!=null?b.scale:0)); op[0] = AbstractY86CPU.R_NONE; op[1]=(b!=null?b.base:0); op[2]=(b!=null?b.offset:0);

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
    // $ANTLR end "jmp"



    // $ANTLR start "immediate"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:232:1: immediate returns [int value] : ( ( '$' )? label | ( '$' )? number );
    public final int immediate() throws RecognitionException {
        int value = 0;


        AsmY86Parser.label_return label16 =null;

        int number17 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:233:2: ( ( '$' )? label | ( '$' )? number )
            int alt20=2;
            switch ( input.LA(1) ) {
            case 14:
                {
                int LA20_1 = input.LA(2);

                if ( (LA20_1==Identifier||(LA20_1 >= 34 && LA20_1 <= 66)||(LA20_1 >= 68 && LA20_1 <= 71)) ) {
                    alt20=1;
                }
                else if ( (LA20_1==Decimal||LA20_1==Hex||LA20_1==27) ) {
                    alt20=2;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 20, 1, input);

                    throw nvae;

                }
                }
                break;
            case Identifier:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
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
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 68:
            case 69:
            case 70:
            case 71:
                {
                alt20=1;
                }
                break;
            case Decimal:
            case Hex:
            case 27:
                {
                alt20=2;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;

            }

            switch (alt20) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:233:4: ( '$' )? label
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:233:4: ( '$' )?
                    int alt18=2;
                    int LA18_0 = input.LA(1);

                    if ( (LA18_0==14) ) {
                        alt18=1;
                    }
                    switch (alt18) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:233:4: '$'
                            {
                            match(input,14,FOLLOW_14_in_immediate1169); 

                            }
                            break;

                    }


                    pushFollow(FOLLOW_label_in_immediate1172);
                    label16=label();

                    state._fsp--;


                    value = (label16!=null?label16.value:0);

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:233:42: ( '$' )? number
                    {
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:233:42: ( '$' )?
                    int alt19=2;
                    int LA19_0 = input.LA(1);

                    if ( (LA19_0==14) ) {
                        alt19=1;
                    }
                    switch (alt19) {
                        case 1 :
                            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:233:42: '$'
                            {
                            match(input,14,FOLLOW_14_in_immediate1178); 

                            }
                            break;

                    }


                    pushFollow(FOLLOW_number_in_immediate1181);
                    number17=number();

                    state._fsp--;


                    value = number17;

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
    // $ANTLR end "immediate"



    // $ANTLR start "literal"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:235:1: literal returns [int value] : ( label | number );
    public final int literal() throws RecognitionException {
        int value = 0;


        AsmY86Parser.label_return label18 =null;

        int number19 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:236:2: ( label | number )
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==Identifier||(LA21_0 >= 34 && LA21_0 <= 66)||(LA21_0 >= 68 && LA21_0 <= 71)) ) {
                alt21=1;
            }
            else if ( (LA21_0==Decimal||LA21_0==Hex||LA21_0==27) ) {
                alt21=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 21, 0, input);

                throw nvae;

            }
            switch (alt21) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:236:4: label
                    {
                    pushFollow(FOLLOW_label_in_literal1196);
                    label18=label();

                    state._fsp--;


                    value = (label18!=null?label18.value:0);

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:236:37: number
                    {
                    pushFollow(FOLLOW_number_in_literal1202);
                    number19=number();

                    state._fsp--;


                    value = number19;

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
    // $ANTLR end "literal"


    public static class baseOffset_return extends ParserRuleReturnScope {
        public int offset;
        public int base;
        public int scale;
    };


    // $ANTLR start "baseOffset"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:238:1: baseOffset returns [int offset, int base, int scale] : ( literal )? regIndirectScale ;
    public final AsmY86Parser.baseOffset_return baseOffset() throws RecognitionException {
        AsmY86Parser.baseOffset_return retval = new AsmY86Parser.baseOffset_return();
        retval.start = input.LT(1);


        int literal20 =0;

        AsmY86Parser.regIndirectScale_return regIndirectScale21 =null;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:239:2: ( ( literal )? regIndirectScale )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:239:4: ( literal )? regIndirectScale
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:239:4: ( literal )?
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( (LA22_0==Decimal||LA22_0==Hex||LA22_0==Identifier||LA22_0==27||(LA22_0 >= 34 && LA22_0 <= 66)||(LA22_0 >= 68 && LA22_0 <= 71)) ) {
                alt22=1;
            }
            switch (alt22) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:239:4: literal
                    {
                    pushFollow(FOLLOW_literal_in_baseOffset1217);
                    literal20=literal();

                    state._fsp--;


                    }
                    break;

            }


            pushFollow(FOLLOW_regIndirectScale_in_baseOffset1220);
            regIndirectScale21=regIndirectScale();

            state._fsp--;


            retval.offset =literal20; retval.base =(regIndirectScale21!=null?regIndirectScale21.value:0); retval.scale =(regIndirectScale21!=null?regIndirectScale21.scale:0);

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
    // $ANTLR end "baseOffset"


    public static class baseOffsetNoScale_return extends ParserRuleReturnScope {
        public int offset;
        public int base;
    };


    // $ANTLR start "baseOffsetNoScale"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:241:1: baseOffsetNoScale returns [int offset, int base] : ( literal )? regIndirect ;
    public final AsmY86Parser.baseOffsetNoScale_return baseOffsetNoScale() throws RecognitionException {
        AsmY86Parser.baseOffsetNoScale_return retval = new AsmY86Parser.baseOffsetNoScale_return();
        retval.start = input.LT(1);


        int literal22 =0;

        int regIndirect23 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:242:3: ( ( literal )? regIndirect )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:242:5: ( literal )? regIndirect
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:242:5: ( literal )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==Decimal||LA23_0==Hex||LA23_0==Identifier||LA23_0==27||(LA23_0 >= 34 && LA23_0 <= 66)||(LA23_0 >= 68 && LA23_0 <= 71)) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:242:5: literal
                    {
                    pushFollow(FOLLOW_literal_in_baseOffsetNoScale1236);
                    literal22=literal();

                    state._fsp--;


                    }
                    break;

            }


            pushFollow(FOLLOW_regIndirect_in_baseOffsetNoScale1239);
            regIndirect23=regIndirect();

            state._fsp--;


            retval.offset =literal22; retval.base =regIndirect23;

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
    // $ANTLR end "baseOffsetNoScale"



    // $ANTLR start "regIndirect"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:244:1: regIndirect returns [int value] : '(' register ')' ;
    public final int regIndirect() throws RecognitionException {
        int value = 0;


        int register24 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:245:2: ( '(' register ')' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:245:4: '(' register ')'
            {
            match(input,23,FOLLOW_23_in_regIndirect1256); 

            pushFollow(FOLLOW_register_in_regIndirect1258);
            register24=register();

            state._fsp--;


            match(input,24,FOLLOW_24_in_regIndirect1260); 

            value =register24;

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
    // $ANTLR end "regIndirect"


    public static class regIndirectScale_return extends ParserRuleReturnScope {
        public int value;
        public int scale;
    };


    // $ANTLR start "regIndirectScale"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:247:1: regIndirectScale returns [int value, int scale] : '(' register ( ',' scaleLit )? ')' ;
    public final AsmY86Parser.regIndirectScale_return regIndirectScale() throws RecognitionException {
        AsmY86Parser.regIndirectScale_return retval = new AsmY86Parser.regIndirectScale_return();
        retval.start = input.LT(1);


        int register25 =0;

        Integer scaleLit26 =null;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:248:9: ( '(' register ( ',' scaleLit )? ')' )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:248:11: '(' register ( ',' scaleLit )? ')'
            {
            match(input,23,FOLLOW_23_in_regIndirectScale1282); 

            pushFollow(FOLLOW_register_in_regIndirectScale1284);
            register25=register();

            state._fsp--;


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:248:24: ( ',' scaleLit )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==26) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:248:25: ',' scaleLit
                    {
                    match(input,26,FOLLOW_26_in_regIndirectScale1287); 

                    pushFollow(FOLLOW_scaleLit_in_regIndirectScale1289);
                    scaleLit26=scaleLit();

                    state._fsp--;


                    }
                    break;

            }


            match(input,24,FOLLOW_24_in_regIndirectScale1294); 

            retval.value =register25; retval.scale =scaleLit26!=null? scaleLit26 : 0x0;

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
    // $ANTLR end "regIndirectScale"



    // $ANTLR start "scaleLit"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:250:1: scaleLit returns [Integer value] : decimal ;
    public final Integer scaleLit() throws RecognitionException {
        Integer value = null;


        int decimal27 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:251:9: ( decimal )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:251:11: decimal
            {
            pushFollow(FOLLOW_decimal_in_scaleLit1316);
            decimal27=decimal();

            state._fsp--;


             value = decimal27; if (value != 1 && value != 2 && value != 4) { throw new Assembler.AssemblyException("Illegal scale: must be 1, 2 or 4"); } 

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
    // $ANTLR end "scaleLit"



    // $ANTLR start "register"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:253:1: register returns [int value] : ( '%eax' | '%ecx' | '%edx' | '%ebx' | '%esp' | '%ebp' | '%esi' | '%edi' );
    public final int register() throws RecognitionException {
        int value = 0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:254:2: ( '%eax' | '%ecx' | '%edx' | '%ebx' | '%esp' | '%ebp' | '%esi' | '%edi' )
            int alt25=8;
            switch ( input.LA(1) ) {
            case 15:
                {
                alt25=1;
                }
                break;
            case 18:
                {
                alt25=2;
                }
                break;
            case 20:
                {
                alt25=3;
                }
                break;
            case 17:
                {
                alt25=4;
                }
                break;
            case 22:
                {
                alt25=5;
                }
                break;
            case 16:
                {
                alt25=6;
                }
                break;
            case 21:
                {
                alt25=7;
                }
                break;
            case 19:
                {
                alt25=8;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 25, 0, input);

                throw nvae;

            }

            switch (alt25) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:254:4: '%eax'
                    {
                    match(input,15,FOLLOW_15_in_register1332); 

                    value = 0;

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:254:27: '%ecx'
                    {
                    match(input,18,FOLLOW_18_in_register1338); 

                    value = 1;

                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:254:50: '%edx'
                    {
                    match(input,20,FOLLOW_20_in_register1344); 

                    value = 2;

                    }
                    break;
                case 4 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:254:73: '%ebx'
                    {
                    match(input,17,FOLLOW_17_in_register1350); 

                    value = 3;

                    }
                    break;
                case 5 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:254:96: '%esp'
                    {
                    match(input,22,FOLLOW_22_in_register1356); 

                    value = 4;

                    }
                    break;
                case 6 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:255:3: '%ebp'
                    {
                    match(input,16,FOLLOW_16_in_register1365); 

                    value = 5;

                    }
                    break;
                case 7 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:255:26: '%esi'
                    {
                    match(input,21,FOLLOW_21_in_register1371); 

                    value = 6;

                    }
                    break;
                case 8 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:255:49: '%edi'
                    {
                    match(input,19,FOLLOW_19_in_register1377); 

                    value = 7;

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
    // $ANTLR end "register"



    // $ANTLR start "number"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:257:1: number returns [int value] : ( '-' )? ( decimal | hex ) ;
    public final int number() throws RecognitionException {
        int value = 0;


        int decimal28 =0;

        int hex29 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:258:3: ( ( '-' )? ( decimal | hex ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:258:5: ( '-' )? ( decimal | hex )
            {
            value = 1;

            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:258:19: ( '-' )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==27) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:258:20: '-'
                    {
                    match(input,27,FOLLOW_27_in_number1396); 

                    value = -1;

                    }
                    break;

            }


            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:258:41: ( decimal | hex )
            int alt27=2;
            int LA27_0 = input.LA(1);

            if ( (LA27_0==Decimal) ) {
                alt27=1;
            }
            else if ( (LA27_0==Hex) ) {
                alt27=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;

            }
            switch (alt27) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:258:43: decimal
                    {
                    pushFollow(FOLLOW_decimal_in_number1404);
                    decimal28=decimal();

                    state._fsp--;


                    value*=decimal28; 

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:258:80: hex
                    {
                    pushFollow(FOLLOW_hex_in_number1410);
                    hex29=hex();

                    state._fsp--;


                    value*=hex29;

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
        return value;
    }
    // $ANTLR end "number"



    // $ANTLR start "hex"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:260:1: hex returns [int value] : Hex ;
    public final int hex() throws RecognitionException {
        int value = 0;


        Token Hex30=null;

        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:261:2: ( Hex )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:261:4: Hex
            {
            Hex30=(Token)match(input,Hex,FOLLOW_Hex_in_hex1427); 

            value =(int)(Long.parseLong((Hex30!=null?Hex30.getText():null).substring(2),16));

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
    // $ANTLR end "hex"



    // $ANTLR start "decimal"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:264:1: decimal returns [int value] : Decimal ;
    public final int decimal() throws RecognitionException {
        int value = 0;


        Token Decimal31=null;

        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:265:3: ( Decimal )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:265:5: Decimal
            {
            Decimal31=(Token)match(input,Decimal,FOLLOW_Decimal_in_decimal1446); 

            value =(int)(Long.parseLong((Decimal31!=null?Decimal31.getText():null)));

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
    // $ANTLR end "decimal"



    // $ANTLR start "directive"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:268:1: directive : ( pos | data | align );
    public final void directive() throws RecognitionException {
        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:269:2: ( pos | data | align )
            int alt28=3;
            switch ( input.LA(1) ) {
            case 31:
                {
                alt28=1;
                }
                break;
            case 29:
            case 30:
            case 32:
                {
                alt28=2;
                }
                break;
            case 28:
                {
                alt28=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;

            }

            switch (alt28) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:269:4: pos
                    {
                    pushFollow(FOLLOW_pos_in_directive1464);
                    pos();

                    state._fsp--;


                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:269:10: data
                    {
                    pushFollow(FOLLOW_data_in_directive1468);
                    data();

                    state._fsp--;


                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:269:17: align
                    {
                    pushFollow(FOLLOW_align_in_directive1472);
                    align();

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
    // $ANTLR end "directive"



    // $ANTLR start "pos"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:271:1: pos : ( '.pos' number ) ;
    public final void pos() throws RecognitionException {
        int number32 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:271:5: ( ( '.pos' number ) )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:271:7: ( '.pos' number )
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:271:7: ( '.pos' number )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:271:8: '.pos' number
            {
            match(input,31,FOLLOW_31_in_pos1481); 

            pushFollow(FOLLOW_number_in_pos1483);
            number32=number();

            state._fsp--;


            pc = number32;

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
    // $ANTLR end "pos"



    // $ANTLR start "data"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:273:1: data : ( '.long' | '.word' | '.byte' ) literal ( ',' count= number )? ;
    public final void data() throws RecognitionException {
        int count =0;

        int literal33 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:273:6: ( ( '.long' | '.word' | '.byte' ) literal ( ',' count= number )? )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:273:8: ( '.long' | '.word' | '.byte' ) literal ( ',' count= number )?
            {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:273:8: ( '.long' | '.word' | '.byte' )
            int alt29=3;
            switch ( input.LA(1) ) {
            case 30:
                {
                alt29=1;
                }
                break;
            case 32:
                {
                alt29=2;
                }
                break;
            case 29:
                {
                alt29=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 29, 0, input);

                throw nvae;

            }

            switch (alt29) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:273:9: '.long'
                    {
                    match(input,30,FOLLOW_30_in_data1495); 

                    dataSize=4;

                    }
                    break;
                case 2 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:273:33: '.word'
                    {
                    match(input,32,FOLLOW_32_in_data1501); 

                    dataSize=2;

                    }
                    break;
                case 3 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:273:57: '.byte'
                    {
                    match(input,29,FOLLOW_29_in_data1507); 

                    dataSize=1;

                    }
                    break;

            }


            pushFollow(FOLLOW_literal_in_data1512);
            literal33=literal();

            state._fsp--;


            dataValue=literal33;

            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:273:116: ( ',' count= number )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==26) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:273:117: ',' count= number
                    {
                    match(input,26,FOLLOW_26_in_data1517); 

                    pushFollow(FOLLOW_number_in_data1521);
                    count=number();

                    state._fsp--;


                    }
                    break;

            }


            lineType = LineType.DATA; dataCount=count>0? count : 1;

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
    // $ANTLR end "data"



    // $ANTLR start "align"
    // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:276:1: align : '.align' number ;
    public final void align() throws RecognitionException {
        int number34 =0;


        try {
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:276:7: ( '.align' number )
            // /Users/feeley/Documents/Work/Courses/SimpleMachine/Grammar/Source/AsmY86.g:276:9: '.align' number
            {
            match(input,28,FOLLOW_28_in_align1535); 

            pushFollow(FOLLOW_number_in_align1537);
            number34=number();

            state._fsp--;


            pc = (pc + number34 - 1) / number34 * number34;

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
    // $ANTLR end "align"

    // Delegated rules


    protected DFA1 dfa1 = new DFA1(this);
    static final String DFA1_eotS =
        "\u014f\uffff";
    static final String DFA1_eofS =
        "\1\55\1\uffff\2\55\31\uffff\1\55\10\uffff\1\55\55\uffff\2\55\1\uffff"+
        "\4\55\1\uffff\2\55\2\uffff\20\55\6\uffff\2\55\1\uffff\2\55\3\uffff"+
        "\4\55\1\uffff\2\55\1\uffff\4\55\41\uffff\1\55\54\uffff\20\55\10"+
        "\uffff\10\55\10\uffff\1\55\11\uffff\10\55\1\uffff\1\55\11\uffff"+
        "\2\55\21\uffff\1\55\1\uffff\21\55\4\uffff\10\55";
    static final String DFA1_minS =
        "\1\5\1\41\2\5\7\17\1\7\1\17\1\7\7\17\7\7\1\17\1\5\1\17\7\7\1\5\6"+
        "\7\2\uffff\1\5\10\32\1\7\2\32\1\7\12\32\2\27\1\7\2\27\1\17\10\32"+
        "\2\5\1\7\4\5\1\7\2\5\1\17\1\7\20\5\1\7\2\32\1\7\2\32\2\5\1\7\2\5"+
        "\1\17\2\7\4\5\1\7\2\5\1\7\4\5\7\17\1\7\1\17\1\7\7\17\7\7\2\17\7"+
        "\7\1\5\1\7\2\17\1\7\10\30\2\17\10\30\2\27\1\7\2\27\2\17\10\30\2"+
        "\27\1\7\2\27\1\17\1\7\20\5\2\27\1\7\2\27\1\17\1\7\1\32\10\5\10\30"+
        "\1\5\1\7\10\30\10\5\1\7\1\5\10\30\1\7\2\5\11\30\1\17\1\32\1\17\1"+
        "\30\1\7\1\32\1\30\1\7\1\5\1\7\21\5\1\30\1\17\2\30\10\5";
    static final String DFA1_maxS =
        "\1\107\12\41\1\107\1\41\1\107\7\41\7\107\1\41\1\14\1\41\7\107\1"+
        "\41\1\107\1\33\3\107\1\33\2\uffff\1\107\10\32\1\107\2\32\1\11\12"+
        "\32\2\27\1\11\2\27\1\26\10\32\2\14\1\11\2\14\2\27\1\11\2\27\1\26"+
        "\1\107\20\14\1\107\2\32\1\11\2\32\2\27\1\11\2\27\1\26\1\107\1\11"+
        "\2\14\2\32\1\11\2\32\1\11\4\14\7\26\1\107\1\26\1\107\7\26\7\107"+
        "\2\26\7\107\1\14\1\107\2\26\1\107\10\32\2\26\10\32\2\27\1\11\2\27"+
        "\2\26\10\32\2\27\1\11\2\27\1\26\1\33\20\14\2\27\1\11\2\27\1\26\1"+
        "\7\1\32\10\14\11\32\1\7\10\32\10\14\1\7\1\14\10\32\1\11\2\14\10"+
        "\32\1\30\1\26\1\32\1\26\1\30\1\7\1\32\1\30\1\7\1\14\1\7\21\14\1"+
        "\30\1\26\2\30\10\14";
    static final String DFA1_acceptS =
        "\55\uffff\1\2\1\1\u0120\uffff";
    static final String DFA1_specialS =
        "\u014f\uffff}>";
    static final String[] DFA1_transitionS = {
            "\1\56\5\uffff\1\1\1\56\17\uffff\1\54\1\53\1\51\1\50\1\52\1\uffff"+
            "\1\16\1\20\1\33\1\7\1\12\1\11\1\6\1\5\1\10\1\23\1\3\1\37\1\41"+
            "\1\44\1\45\1\43\1\13\1\40\1\42\1\27\1\32\1\31\1\26\1\25\1\47"+
            "\1\30\1\46\1\24\1\15\1\22\1\2\1\36\1\34\1\35\1\14\1\4\1\17\1"+
            "\21",
            "\1\57",
            "\1\56\1\55\5\uffff\1\56\24\uffff\1\57",
            "\1\56\1\55\5\uffff\1\56\24\uffff\1\57",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64\12\uffff\1\57",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64\12\uffff\1\57",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64\12\uffff\1\57",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64\12\uffff\1\57",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64\12\uffff\1\57",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64\12\uffff\1\57",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64\12\uffff\1\57",
            "\1\74\1\uffff\1\75\1\uffff\1\71\2\uffff\1\70\14\uffff\1\73"+
            "\5\uffff\1\57\41\72\1\uffff\4\72",
            "\1\76\1\103\1\101\1\77\1\105\1\100\1\104\1\102\12\uffff\1\57",
            "\1\111\1\uffff\1\112\1\uffff\1\106\13\uffff\1\113\3\uffff\1"+
            "\110\5\uffff\1\57\41\107\1\uffff\4\107",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120\12\uffff\1"+
            "\57",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120\12\uffff\1"+
            "\57",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120\12\uffff\1"+
            "\57",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120\12\uffff\1"+
            "\57",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120\12\uffff\1"+
            "\57",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120\12\uffff\1"+
            "\57",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120\12\uffff\1"+
            "\57",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\5\uffff\1"+
            "\57\41\125\1\uffff\4\125",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\5\uffff\1"+
            "\57\41\125\1\uffff\4\125",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\5\uffff\1"+
            "\57\41\125\1\uffff\4\125",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\5\uffff\1"+
            "\57\41\125\1\uffff\4\125",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\5\uffff\1"+
            "\57\41\125\1\uffff\4\125",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\5\uffff\1"+
            "\57\41\125\1\uffff\4\125",
            "\1\134\1\uffff\1\135\1\uffff\1\131\13\uffff\1\136\1\uffff\1"+
            "\137\1\uffff\1\133\5\uffff\1\57\41\132\1\uffff\4\132",
            "\1\140\1\145\1\143\1\141\1\147\1\142\1\146\1\144\12\uffff\1"+
            "\57",
            "\1\56\1\55\5\uffff\1\56",
            "\1\150\1\155\1\153\1\151\1\157\1\152\1\156\1\154\12\uffff\1"+
            "\57",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\5\uffff\1\57\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\5\uffff\1\57\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\5\uffff\1\57\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\5\uffff\1\57\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\5\uffff\1\57\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\5\uffff\1\57\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\5\uffff\1\57\41\162\1\uffff\4\162",
            "\1\56\1\55\5\uffff\1\56\24\uffff\1\57",
            "\1\171\1\uffff\1\172\1\uffff\1\166\13\uffff\1\173\1\uffff\1"+
            "\174\1\uffff\1\170\5\uffff\1\57\41\167\1\uffff\4\167",
            "\1\176\1\uffff\1\177\21\uffff\1\175",
            "\1\u0083\1\uffff\1\u0084\1\uffff\1\u0080\17\uffff\1\u0082\6"+
            "\uffff\41\u0081\1\uffff\4\u0081",
            "\1\u0083\1\uffff\1\u0084\1\uffff\1\u0080\17\uffff\1\u0082\6"+
            "\uffff\41\u0081\1\uffff\4\u0081",
            "\1\u0083\1\uffff\1\u0084\1\uffff\1\u0080\17\uffff\1\u0082\6"+
            "\uffff\41\u0081\1\uffff\4\u0081",
            "\1\u0086\1\uffff\1\u0087\21\uffff\1\u0085",
            "",
            "",
            "\1\56\6\uffff\1\56\17\uffff\1\54\1\53\1\51\1\50\1\52\1\uffff"+
            "\1\u0094\1\u0096\1\u00a1\1\u008d\1\u0090\1\u008f\1\u008c\1\u008b"+
            "\1\u008e\1\u0099\1\u0089\1\u00a4\1\u00a6\1\u00a9\1\u00aa\1\u00a8"+
            "\1\u0091\1\u00a5\1\u00a7\1\u009d\1\u00a0\1\u009f\1\u009c\1\u009b"+
            "\1\u00ac\1\u009e\1\u00ab\1\u009a\1\u0093\1\u0098\1\u0088\1\u00a3"+
            "\1\u00a2\1\35\1\u0092\1\u008a\1\u0095\1\u0097",
            "\1\u00ad",
            "\1\u00ad",
            "\1\u00ad",
            "\1\u00ad",
            "\1\u00ad",
            "\1\u00ad",
            "\1\u00ad",
            "\1\u00ad",
            "\1\74\1\uffff\1\75\1\uffff\1\71\17\uffff\1\73\6\uffff\41\72"+
            "\1\uffff\4\72",
            "\1\u00ae",
            "\1\u00ae",
            "\1\74\1\uffff\1\75",
            "\1\u00ae",
            "\1\u00ae",
            "\1\u00af",
            "\1\u00af",
            "\1\u00af",
            "\1\u00af",
            "\1\u00af",
            "\1\u00af",
            "\1\u00af",
            "\1\u00af",
            "\1\113",
            "\1\113",
            "\1\111\1\uffff\1\112",
            "\1\113",
            "\1\113",
            "\1\u00b0\1\u00b5\1\u00b3\1\u00b1\1\u00b7\1\u00b2\1\u00b6\1"+
            "\u00b4",
            "\1\u00b8",
            "\1\u00b8",
            "\1\u00b8",
            "\1\u00b8",
            "\1\u00b8",
            "\1\u00b8",
            "\1\u00b8",
            "\1\u00b8",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\127\1\uffff\1\130",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56\12\uffff\1\u00b9",
            "\1\56\1\55\5\uffff\1\56\12\uffff\1\u00b9",
            "\1\134\1\uffff\1\135",
            "\1\56\1\55\5\uffff\1\56\12\uffff\1\u00b9",
            "\1\56\1\55\5\uffff\1\56\12\uffff\1\u00b9",
            "\1\u00ba\1\u00bf\1\u00bd\1\u00bb\1\u00c1\1\u00bc\1\u00c0\1"+
            "\u00be",
            "\1\u00c5\1\uffff\1\u00c6\1\uffff\1\u00c2\13\uffff\1\u00c7\3"+
            "\uffff\1\u00c4\6\uffff\41\u00c3\1\uffff\4\u00c3",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\164\1\uffff\1\165\1\uffff\1\161\17\uffff\1\163\6\uffff\41"+
            "\162\1\uffff\4\162",
            "\1\u00c8",
            "\1\u00c8",
            "\1\164\1\uffff\1\165",
            "\1\u00c8",
            "\1\u00c8",
            "\1\56\1\55\5\uffff\1\56\12\uffff\1\173",
            "\1\56\1\55\5\uffff\1\56\12\uffff\1\173",
            "\1\171\1\uffff\1\172",
            "\1\56\1\55\5\uffff\1\56\12\uffff\1\173",
            "\1\56\1\55\5\uffff\1\56\12\uffff\1\173",
            "\1\u00c9\1\u00ce\1\u00cc\1\u00ca\1\u00d0\1\u00cb\1\u00cf\1"+
            "\u00cd",
            "\1\u00d4\1\uffff\1\u00d5\1\uffff\1\u00d1\13\uffff\1\u00d6\3"+
            "\uffff\1\u00d3\6\uffff\41\u00d2\1\uffff\4\u00d2",
            "\1\176\1\uffff\1\177",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56\15\uffff\1\u00d7",
            "\1\56\1\55\5\uffff\1\56\15\uffff\1\u00d7",
            "\1\u0083\1\uffff\1\u0084",
            "\1\56\1\55\5\uffff\1\56\15\uffff\1\u00d7",
            "\1\56\1\55\5\uffff\1\56\15\uffff\1\u00d7",
            "\1\u0086\1\uffff\1\u0087",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64",
            "\1\60\1\65\1\63\1\61\1\67\1\62\1\66\1\64",
            "\1\74\1\uffff\1\75\1\uffff\1\71\2\uffff\1\70\14\uffff\1\73"+
            "\6\uffff\41\72\1\uffff\4\72",
            "\1\76\1\103\1\101\1\77\1\105\1\100\1\104\1\102",
            "\1\111\1\uffff\1\112\1\uffff\1\106\13\uffff\1\113\3\uffff\1"+
            "\110\6\uffff\41\107\1\uffff\4\107",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120",
            "\1\114\1\121\1\117\1\115\1\123\1\116\1\122\1\120",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\6\uffff\41"+
            "\125\1\uffff\4\125",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\6\uffff\41"+
            "\125\1\uffff\4\125",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\6\uffff\41"+
            "\125\1\uffff\4\125",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\6\uffff\41"+
            "\125\1\uffff\4\125",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\6\uffff\41"+
            "\125\1\uffff\4\125",
            "\1\127\1\uffff\1\130\1\uffff\1\124\17\uffff\1\126\6\uffff\41"+
            "\125\1\uffff\4\125",
            "\1\134\1\uffff\1\135\1\uffff\1\131\13\uffff\1\136\1\uffff\1"+
            "\137\1\uffff\1\133\6\uffff\41\132\1\uffff\4\132",
            "\1\140\1\145\1\143\1\141\1\147\1\142\1\146\1\144",
            "\1\150\1\155\1\153\1\151\1\157\1\152\1\156\1\154",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\6\uffff\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\6\uffff\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\6\uffff\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\6\uffff\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\6\uffff\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\6\uffff\41\162\1\uffff\4\162",
            "\1\164\1\uffff\1\165\1\uffff\1\161\2\uffff\1\160\14\uffff\1"+
            "\163\6\uffff\41\162\1\uffff\4\162",
            "\1\56\1\55\5\uffff\1\56",
            "\1\171\1\uffff\1\172\1\uffff\1\166\13\uffff\1\173\1\uffff\1"+
            "\174\1\uffff\1\170\6\uffff\41\167\1\uffff\4\167",
            "\1\u00d8\1\u00dd\1\u00db\1\u00d9\1\u00df\1\u00da\1\u00de\1"+
            "\u00dc",
            "\1\u00e0\1\u00e5\1\u00e3\1\u00e1\1\u00e7\1\u00e2\1\u00e6\1"+
            "\u00e4",
            "\1\u00eb\1\uffff\1\u00ec\1\uffff\1\u00e8\13\uffff\1\u00ed\3"+
            "\uffff\1\u00ea\6\uffff\41\u00e9\1\uffff\4\u00e9",
            "\1\u00ef\1\uffff\1\u00ee",
            "\1\u00ef\1\uffff\1\u00ee",
            "\1\u00ef\1\uffff\1\u00ee",
            "\1\u00ef\1\uffff\1\u00ee",
            "\1\u00ef\1\uffff\1\u00ee",
            "\1\u00ef\1\uffff\1\u00ee",
            "\1\u00ef\1\uffff\1\u00ee",
            "\1\u00ef\1\uffff\1\u00ee",
            "\1\u00f0\1\u00f5\1\u00f3\1\u00f1\1\u00f7\1\u00f2\1\u00f6\1"+
            "\u00f4",
            "\1\u00f8\1\u00fd\1\u00fb\1\u00f9\1\u00ff\1\u00fa\1\u00fe\1"+
            "\u00fc",
            "\1\u0100\1\uffff\1\u0101",
            "\1\u0100\1\uffff\1\u0101",
            "\1\u0100\1\uffff\1\u0101",
            "\1\u0100\1\uffff\1\u0101",
            "\1\u0100\1\uffff\1\u0101",
            "\1\u0100\1\uffff\1\u0101",
            "\1\u0100\1\uffff\1\u0101",
            "\1\u0100\1\uffff\1\u0101",
            "\1\u00c7",
            "\1\u00c7",
            "\1\u00c5\1\uffff\1\u00c6",
            "\1\u00c7",
            "\1\u00c7",
            "\1\u0102\1\u0107\1\u0105\1\u0103\1\u0109\1\u0104\1\u0108\1"+
            "\u0106",
            "\1\u010a\1\u010f\1\u010d\1\u010b\1\u0111\1\u010c\1\u0110\1"+
            "\u010e",
            "\1\u0113\1\uffff\1\u0112",
            "\1\u0113\1\uffff\1\u0112",
            "\1\u0113\1\uffff\1\u0112",
            "\1\u0113\1\uffff\1\u0112",
            "\1\u0113\1\uffff\1\u0112",
            "\1\u0113\1\uffff\1\u0112",
            "\1\u0113\1\uffff\1\u0112",
            "\1\u0113\1\uffff\1\u0112",
            "\1\u00d6",
            "\1\u00d6",
            "\1\u00d4\1\uffff\1\u00d5",
            "\1\u00d6",
            "\1\u00d6",
            "\1\u0114\1\u0119\1\u0117\1\u0115\1\u011b\1\u0116\1\u011a\1"+
            "\u0118",
            "\1\u011d\1\uffff\1\u011e\21\uffff\1\u011c",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\u00ed",
            "\1\u00ed",
            "\1\u00eb\1\uffff\1\u00ec",
            "\1\u00ed",
            "\1\u00ed",
            "\1\u011f\1\u0124\1\u0122\1\u0120\1\u0126\1\u0121\1\u0125\1"+
            "\u0123",
            "\1\u0127",
            "\1\u0128",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\u0129\1\uffff\1\u0101",
            "\1\u0129\1\uffff\1\u0101",
            "\1\u0129\1\uffff\1\u0101",
            "\1\u0129\1\uffff\1\u0101",
            "\1\u0129\1\uffff\1\u0101",
            "\1\u0129\1\uffff\1\u0101",
            "\1\u0129\1\uffff\1\u0101",
            "\1\u0129\1\uffff\1\u0101",
            "\1\56\1\55\5\uffff\1\56\15\uffff\1\u012a",
            "\1\u012b",
            "\1\u012d\1\uffff\1\u012c",
            "\1\u012d\1\uffff\1\u012c",
            "\1\u012d\1\uffff\1\u012c",
            "\1\u012d\1\uffff\1\u012c",
            "\1\u012d\1\uffff\1\u012c",
            "\1\u012d\1\uffff\1\u012c",
            "\1\u012d\1\uffff\1\u012c",
            "\1\u012d\1\uffff\1\u012c",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\u012e",
            "\1\56\1\55\5\uffff\1\56",
            "\1\u0130\1\uffff\1\u012f",
            "\1\u0130\1\uffff\1\u012f",
            "\1\u0130\1\uffff\1\u012f",
            "\1\u0130\1\uffff\1\u012f",
            "\1\u0130\1\uffff\1\u012f",
            "\1\u0130\1\uffff\1\u012f",
            "\1\u0130\1\uffff\1\u012f",
            "\1\u0130\1\uffff\1\u012f",
            "\1\u011d\1\uffff\1\u011e",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\u0132\1\uffff\1\u0131",
            "\1\u0132\1\uffff\1\u0131",
            "\1\u0132\1\uffff\1\u0131",
            "\1\u0132\1\uffff\1\u0131",
            "\1\u0132\1\uffff\1\u0131",
            "\1\u0132\1\uffff\1\u0131",
            "\1\u0132\1\uffff\1\u0131",
            "\1\u0132\1\uffff\1\u0131",
            "\1\u00ef",
            "\1\u0133\1\u0138\1\u0136\1\u0134\1\u013a\1\u0135\1\u0139\1"+
            "\u0137",
            "\1\u012a",
            "\1\u013b\1\u0140\1\u013e\1\u013c\1\u0142\1\u013d\1\u0141\1"+
            "\u013f",
            "\1\u0129",
            "\1\u0143",
            "\1\u0144",
            "\1\u0113",
            "\1\u0145",
            "\1\56\1\55\5\uffff\1\56",
            "\1\u0146",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\u012d",
            "\1\u0147\1\u014c\1\u014a\1\u0148\1\u014e\1\u0149\1\u014d\1"+
            "\u014b",
            "\1\u0130",
            "\1\u0132",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56",
            "\1\56\1\55\5\uffff\1\56"
    };

    static final short[] DFA1_eot = DFA.unpackEncodedString(DFA1_eotS);
    static final short[] DFA1_eof = DFA.unpackEncodedString(DFA1_eofS);
    static final char[] DFA1_min = DFA.unpackEncodedStringToUnsignedChars(DFA1_minS);
    static final char[] DFA1_max = DFA.unpackEncodedStringToUnsignedChars(DFA1_maxS);
    static final short[] DFA1_accept = DFA.unpackEncodedString(DFA1_acceptS);
    static final short[] DFA1_special = DFA.unpackEncodedString(DFA1_specialS);
    static final short[][] DFA1_transition;

    static {
        int numStates = DFA1_transitionS.length;
        DFA1_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA1_transition[i] = DFA.unpackEncodedString(DFA1_transitionS[i]);
        }
    }

    class DFA1 extends DFA {

        public DFA1(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 1;
            this.eot = DFA1_eot;
            this.eof = DFA1_eof;
            this.min = DFA1_min;
            this.max = DFA1_max;
            this.accept = DFA1_accept;
            this.special = DFA1_special;
            this.transition = DFA1_transition;
        }
        public String getDescription() {
            return "()* loopback of 144:11: ( line )*";
        }
    }
 

    public static final BitSet FOLLOW_line_in_program46 = new BitSet(new long[]{0xFFFFFFFDF0001822L,0x00000000000000FFL});
    public static final BitSet FOLLOW_lineZ_in_program49 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labelDeclaration_in_line59 = new BitSet(new long[]{0xFFFFFFFDF0001020L,0x00000000000000FFL});
    public static final BitSet FOLLOW_instruction_in_line65 = new BitSet(new long[]{0x0000000000001020L});
    public static final BitSet FOLLOW_directive_in_line69 = new BitSet(new long[]{0x0000000000001020L});
    public static final BitSet FOLLOW_NewLine_in_line76 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Comment_in_line81 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_labelDeclaration_in_lineZ101 = new BitSet(new long[]{0xFFFFFFFDF0000000L,0x00000000000000FFL});
    public static final BitSet FOLLOW_instruction_in_lineZ107 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_directive_in_lineZ111 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_EOF_in_lineZ117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CommentZ_in_lineZ122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_labelDeclaration142 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_operand_in_labelDeclaration146 = new BitSet(new long[]{0x0000000200000000L});
    public static final BitSet FOLLOW_33_in_labelDeclaration149 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Identifier_in_label164 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operand_in_label168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nop_in_instruction182 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_halt_in_instruction186 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rrmovxx_in_instruction190 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_irmovl_in_instruction194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_rmmovl_in_instruction198 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_mrmovl_in_instruction202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_opl_in_instruction206 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_jxx_in_instruction210 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_call_in_instruction214 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ret_in_instruction218 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pushl_in_instruction222 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_popl_in_instruction226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_iopl_in_instruction230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_leave_in_instruction234 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_jmp_in_instruction238 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_44_in_halt422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_64_in_nop432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_69_in_rrmovxx443 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_41_in_rrmovxx460 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_40_in_rrmovxx478 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_37_in_rrmovxx498 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_42_in_rrmovxx517 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_39_in_rrmovxx532 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_38_in_rrmovxx547 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_rrmovxx564 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_rrmovxx566 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_rrmovxx570 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_irmovl580 = new BitSet(new long[]{0xFFFFFFFC08004A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_immediate_in_irmovl582 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_irmovl584 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_irmovl586 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_rmmovl597 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_rmmovl599 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_rmmovl601 = new BitSet(new long[]{0xFFFFFFFC08800A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_baseOffset_in_rmmovl602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_62_in_mrmovl612 = new BitSet(new long[]{0xFFFFFFFC08800A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_baseOffset_in_mrmovl614 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_mrmovl616 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_mrmovl618 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_34_in_opl629 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_70_in_opl642 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_35_in_opl655 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_71_in_opl668 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_63_in_opl681 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_43_in_opl694 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_61_in_opl707 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_opl717 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_opl719 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_opl723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_iopl734 = new BitSet(new long[]{0xFFFFFFFC08004A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_51_in_iopl749 = new BitSet(new long[]{0xFFFFFFFC08004A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_46_in_iopl764 = new BitSet(new long[]{0xFFFFFFFC08004A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_52_in_iopl779 = new BitSet(new long[]{0xFFFFFFFC08004A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_49_in_iopl794 = new BitSet(new long[]{0xFFFFFFFC08004A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_47_in_iopl809 = new BitSet(new long[]{0xFFFFFFFC08004A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_48_in_iopl824 = new BitSet(new long[]{0xFFFFFFFC08004A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_immediate_in_iopl832 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_iopl834 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_iopl836 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_57_in_jxx847 = new BitSet(new long[]{0xFFFFFFFC08000A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_56_in_jxx861 = new BitSet(new long[]{0xFFFFFFFC08000A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_53_in_jxx876 = new BitSet(new long[]{0xFFFFFFFC08000A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_59_in_jxx890 = new BitSet(new long[]{0xFFFFFFFC08000A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_55_in_jxx901 = new BitSet(new long[]{0xFFFFFFFC08000A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_54_in_jxx912 = new BitSet(new long[]{0xFFFFFFFC08000A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_literal_in_jxx924 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_36_in_call937 = new BitSet(new long[]{0xFFFFFFFC0A800A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_literal_in_call940 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_regIndirect_in_call962 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_baseOffset_in_call986 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_call988 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_call992 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_call1014 = new BitSet(new long[]{0xFFFFFFFC08800A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_baseOffset_in_call1018 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_26_in_call1020 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_call1024 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_ret1048 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_66_in_pushl1058 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_pushl1060 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_popl1070 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_popl1072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_60_in_leave1082 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_58_in_jmp1092 = new BitSet(new long[]{0xFFFFFFFC0A800A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_literal_in_jmp1095 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_baseOffset_in_jmp1120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_25_in_jmp1137 = new BitSet(new long[]{0xFFFFFFFC08800A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_baseOffset_in_jmp1141 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_immediate1169 = new BitSet(new long[]{0xFFFFFFFC00000800L,0x00000000000000F7L});
    public static final BitSet FOLLOW_label_in_immediate1172 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_14_in_immediate1178 = new BitSet(new long[]{0x0000000008000280L});
    public static final BitSet FOLLOW_number_in_immediate1181 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_label_in_literal1196 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_number_in_literal1202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_baseOffset1217 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_regIndirectScale_in_baseOffset1220 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_baseOffsetNoScale1236 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_regIndirect_in_baseOffsetNoScale1239 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_regIndirect1256 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_regIndirect1258 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_regIndirect1260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_23_in_regIndirectScale1282 = new BitSet(new long[]{0x00000000007F8000L});
    public static final BitSet FOLLOW_register_in_regIndirectScale1284 = new BitSet(new long[]{0x0000000005000000L});
    public static final BitSet FOLLOW_26_in_regIndirectScale1287 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_scaleLit_in_regIndirectScale1289 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_24_in_regIndirectScale1294 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_decimal_in_scaleLit1316 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_15_in_register1332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_18_in_register1338 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_20_in_register1344 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_17_in_register1350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_22_in_register1356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_register1365 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_21_in_register1371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_19_in_register1377 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_27_in_number1396 = new BitSet(new long[]{0x0000000000000280L});
    public static final BitSet FOLLOW_decimal_in_number1404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_hex_in_number1410 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Hex_in_hex1427 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_Decimal_in_decimal1446 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_pos_in_directive1464 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_data_in_directive1468 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_align_in_directive1472 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_31_in_pos1481 = new BitSet(new long[]{0x0000000008000280L});
    public static final BitSet FOLLOW_number_in_pos1483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_30_in_data1495 = new BitSet(new long[]{0xFFFFFFFC08000A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_32_in_data1501 = new BitSet(new long[]{0xFFFFFFFC08000A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_29_in_data1507 = new BitSet(new long[]{0xFFFFFFFC08000A80L,0x00000000000000F7L});
    public static final BitSet FOLLOW_literal_in_data1512 = new BitSet(new long[]{0x0000000004000002L});
    public static final BitSet FOLLOW_26_in_data1517 = new BitSet(new long[]{0x0000000008000280L});
    public static final BitSet FOLLOW_number_in_data1521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_28_in_align1535 = new BitSet(new long[]{0x0000000008000280L});
    public static final BitSet FOLLOW_number_in_align1537 = new BitSet(new long[]{0x0000000000000002L});

}