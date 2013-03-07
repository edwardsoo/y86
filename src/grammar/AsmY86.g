grammar AsmY86;
//options {k=2; backtrack=true; memoize=true;}

@header {
package grammar;

import isa.Memory;
import isa.MemoryCell;
import isa.Instruction;
import isa.Datum;
import arch.y86.isa.Assembler;
import arch.y86.machine.AbstractY86CPU;
}

@lexer::header {
package grammar;

import arch.y86.isa.Assembler;
}

@lexer::members{
@Override
public void emitErrorMessage(String msg) {
  throw new Assembler.AssemblyException (msg);
}
}

@members {
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
      emitErrorMessage (String.format ("Label not found: \%s at address \%d", label, pc));
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
}

@rulecatch {}

program	:	line* lineZ?;

line	:	(labelDeclaration)? ( instruction | directive )? ( NewLine | (Comment { comment = $Comment.text.substring(1).trim(); })) 
		{writeLine ();};
                
lineZ : (labelDeclaration)? ( instruction | directive ) ( EOF | (CommentZ { comment = $CommentZ.text.substring(1).trim(); })) 
    {writeLine ();};

labelDeclaration	
	:	(Identifier | operand) ':' {label = $labelDeclaration.text.substring (0, $labelDeclaration.text.length ()-1);};
label returns [int value]
	:	(Identifier | operand) {$value = getLabelValue ($label.text);};

// Instructions
instruction
	:	(nop | halt | rrmovxx | irmovl | rmmovl | mrmovl | opl | jxx | call | ret | pushl | popl | iopl | leave | jmp) 
		{lineType = LineType.INSTRUCTION;};

operand	:	('halt' | 'nop' | 'rrmovl' | 'cmovle' | 'cmovl' | 'cmove' | 'cmovne' | 'cmovge' | 'cmovg' | 'irmovl' | 'rmmovl' | 'mrmovl' | 
		 'addl' | 'subl' | 'andl' | 'xorl' | 'mull' | 'divl' | 'modl' |
         'iaddl' | 'isubl' | 'iandl' | 'ixorl' | 'imull' | 'idivl' | 'imodl' |
		 'jmp' | 'jle' | 'jl' | 'je' | 'jne' | 'jge' | 'jg' | 'call' | 'pushl' | 'popl' | 'leave');

halt	:	'halt' {opCode = AbstractY86CPU.I_HALT << 4;};

nop	:	'nop' {opCode = AbstractY86CPU.I_NOP << 4;};

rrmovxx :	('rrmovl' {opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_NC;} |
           'cmovle' {opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_LE;} | 
           'cmovl'  {opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_L;}  | 
           'cmove'  {opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_E;}  |
           'cmovne' {opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_NE;} |
	      	 'cmovge' {opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_GE;} |
	      	 'cmovg'  {opCode = (AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_G;})
	      	 src=register ',' dst=register {op[0]=$src.value; op[1]=$dst.value;};

irmovl	:	'irmovl' immediate ',' register { opCode = AbstractY86CPU.I_IRMOVL << 4; op[0]= AbstractY86CPU.R_NONE; op[1]=$register.value; op[2]=$immediate.value; };

rmmovl 	:	'rmmovl' register ','baseOffset { opCode = (AbstractY86CPU.I_RMMOVL << 4) + $baseOffset.scale; op[0]=$register.value; op[1]=$baseOffset.base; op[2]=$baseOffset.offset; };

mrmovl	:	'mrmovl' baseOffset ',' register { opCode = (AbstractY86CPU.I_MRMOVL << 4) + $baseOffset.scale; op[0]=$register.value; op[1]=$baseOffset.base; op[2]=$baseOffset.offset; };

opl	:	('addl' {opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_ADDL;} |
       'subl' {opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_SUBL;} |
       'andl' {opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_ANDL;} |
       'xorl' {opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_XORL;} |
       'mull' {opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_MULL;} |
       'divl' {opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_DIVL;} |
       'modl' {opCode = (AbstractY86CPU.I_OPL << 4) + AbstractY86CPU.A_MODL;}) 
		a=register ',' b=register {op[0]=$a.value; op[1]=$b.value;};

iopl	:	('iaddl' {opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_ADDL;} |
         'isubl' {opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_SUBL;} |
         'iandl' {opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_ANDL;} |
         'ixorl' {opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_XORL;} |
         'imull' {opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_MULL;} |
         'idivl' {opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_DIVL;} |
         'imodl' {opCode = (AbstractY86CPU.I_IOPL << 4) + AbstractY86CPU.A_MODL;}) 
		immediate ',' register {op[0]=AbstractY86CPU.R_NONE; op[1]=$register.value; op[2]=$immediate.value;};

jxx	:	('jle' {opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_LE;} | 
       'jl'  {opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_L;}  |
       'je'  {opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_E;}  | 
		   'jne' {opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_NE;} |
		   'jge' {opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_GE;} |
		   'jg'  {opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_G;}  ) 
		 literal { op[0] = $literal.value;};
		 
call	:	'call' (literal {opCode = (AbstractY86CPU.I_CALL << 4) | AbstractY86CPU.X_DIRECT_CALL; op[0]=$literal.value; } | 
			          a=regIndirect {opCode = (AbstractY86CPU.I_CALL << 4) | AbstractY86CPU.X_INDIRECT_CALL; op[0] = $a.value; op[1] = AbstractY86CPU.R_NONE;} |
                b=baseOffset ',' a=register {opCode = (AbstractY86CPU.I_CALL << 4) | AbstractY86CPU.X_INDIRECT_FLAG | ($b.scale == 0 ? 0x1 : $b.scale); op[0]=$a.value; op[1]=$b.base; op[2]=$b.offset; } |
                '*' b=baseOffset ',' a=register {opCode = (AbstractY86CPU.I_CALL << 4) | AbstractY86CPU.X_DBL_INDIRECT_FLAG | ($b.scale == 0 ? 0x1 : $b.scale); op[0]=$a.value; op[1]=$b.base; op[2]=$b.offset; }
			         );

ret	:	'ret' {opCode = AbstractY86CPU.I_RET << 4;};

pushl	:	'pushl' register {opCode = AbstractY86CPU.I_PUSHL << 4; op[0] = $register.value; op[1] = AbstractY86CPU.R_NONE;};

popl	:	'popl' register {opCode = AbstractY86CPU.I_POPL << 4; op[0] = $register.value; op[1] = AbstractY86CPU.R_NONE;};

leave	:	'leave' {opCode = AbstractY86CPU.I_LEAVE << 4;};

jmp	:	'jmp' (literal {opCode = (AbstractY86CPU.I_JXX << 4) | AbstractY86CPU.C_NC; op[0] = $literal.value;} |
                 b=baseOffset {opCode = (AbstractY86CPU.I_JMPI << 4) | AbstractY86CPU.X_INDIRECT_FLAG | ($b.scale == 0 ? 0x1 : $b.scale); op[0] = AbstractY86CPU.R_NONE; op[1]=$b.base; op[2]=$b.offset; } |
		         '*' b=baseOffset {opCode = (AbstractY86CPU.I_JMPI << 4) | AbstractY86CPU.X_DBL_INDIRECT_FLAG | ($b.scale == 0 ? 0x1 : $b.scale); op[0] = AbstractY86CPU.R_NONE; op[1]=$b.base; op[2]=$b.offset;}
		        );

// operands
immediate	returns [int value]
	:	'$'? label {$value = $label.value;} | '$'? number {$value = $number.value;};

literal returns [int value]
	:	label {$value = $label.value;} | number {$value = $number.value;};

baseOffset returns [int offset, int base, int scale]
	:	literal? regIndirectScale {$offset=$literal.value; $base=$regIndirectScale.value; $scale=$regIndirectScale.scale;};

baseOffsetNoScale returns [int offset, int base]
  : literal? regIndirect {$offset=$literal.value; $base=$regIndirect.value;};
  
regIndirect returns [int value]
	: '(' register ')' {$value=$register.value;};

regIndirectScale returns [int value, int scale]
        : '(' register (',' scaleLit )? ')' {$value=$register.value; $scale=$scaleLit.value!=null? $scaleLit.value : 0x0;};

scaleLit returns [Integer value]
        : decimal { $value = $decimal.value; if ($value != 1 && value != 2 && value != 4) { throw new Assembler.AssemblyException("Illegal scale: must be 1, 2 or 4"); } }; 

register returns [int value]
	:	'%eax' {$value = 0;} | '%ecx' {$value = 1;} | '%edx' {$value = 2;} | '%ebx' {$value = 3;} | '%esp' {$value = 4;} | 
		'%ebp' {$value = 5;} | '%esi' {$value = 6;} | '%edi' {$value = 7;};

number returns [int value]
 	:	{$value = 1;} ('-' {$value = -1;})? ( decimal {$value*=$decimal.value; } | hex {$value*=$hex.value;});

hex returns [int value]	
	:	Hex
 		{$value=(int)(Long.parseLong($Hex.text.substring(2),16));};

decimal returns [int value]
 	:	Decimal 
 			{$value=(int)(Long.parseLong($Decimal.text));};		

directive
	:	pos | data | align;

pos	:	('.pos' number {pc = $number.value;});

data	:	('.long' {dataSize=4;} | '.word' {dataSize=2;} | '.byte' {dataSize=1;}) literal {dataValue=$literal.value;} (',' count=number)?
		{lineType = LineType.DATA; dataCount=$count.value>0? $count.value : 1;};

align	:	'.align' number {pc = (pc + $number.value - 1) / $number.value * $number.value;};

// Lexical Structure
         Identifier: Character (Character | Digit)*;
         Decimal	 : Digit+;
         Hex	     : '0' ('x'|'X') HexDigit+;
fragment HexDigit  : ('0'..'9'|'a'..'f'|'A'..'F');
fragment Digit	   : ('0'..'9');
fragment Character : 'A'..'Z' | 'a'..'z' | '_';
         Comment   : '#' ( ~('\n'|'\r')* NewLine );
         NewLine   : '\r'?'\n';
         WS        : (' '|'\t')+ {$channel=HIDDEN;};
