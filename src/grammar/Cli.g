grammar Cli;

@header {
package Cli;

import Simulator.Monitor;
}

@lexer::header {
package Cli;
}

@members {
CliUI ui;
public void setUI (CliUI aUI) { ui = aUI; }
char    dpType;
String  dpOp;
int     dpValue;
boolean dpIsEnabled;
}

cmdLine	:	cmd NewLine ;
cmd	:	loadC | runC | stepC | whereC | gotoC | examineC | setC | dpC | showC | quitC | helpC ;

loadC	:	('l'|'load') f=String 
			{ ui.cmdLoadFile ($f.text.substring (1,$f.text.length () -1)); };
runC	:	('r'|'run') 
			{ ui.cmdRun (); };
stepC	:	('s' | 'step') 
			{ ui.cmdStep (); };
whereC 	:	('where')
			{ ui.cmdShowWhere (); };
gotoC	:	('g'|'goto') pc=number 
			{ ui.cmdGotoPC ($pc.value); };
examineC:	('x'|'examine') (
		  (('/' fmt=memfmt)? addr=number (':' count=number)?
			{ ui.cmdExamineMem ($count.value, (fmt!=null? $fmt.value: "x"), $addr.value); } ) |
		  (('/' fmt=regfmt)? reg (':' count=number)?
			{ ui.cmdExamineReg ($count.value, (fmt!=null? $fmt.value: "x"), $reg.value); } ));
setC	:	(reg '=' number) 
			{ ui.cmdSetReg ($reg.value, $number.value); } |
		('m' addr=number '=' val=number) 
			{ ui.cmdSetMem ($addr.value, $val.value); } |
		('i' number '=' instr) 
			{ ui.cmdSetIns ($number.value, $instr.text); } ;
dpC 	:	((tp=('t'|'trace'|'not'|'notrace'|'b'|'break'|'nob'|'nobreak')
			{ dpIsEnabled = $tp.text.charAt(0)!='n';
			  dpType = dpIsEnabled? $tp.text.charAt(0): $tp.text.charAt(2); } ) (
		 ((ins=number
		  	{ dpOp="i"; dpValue = $ins.value; } ) |
		  (tp=('r'|'w'|'a') addr=number
		  	{ dpOp=$tp.text.concat("m"); dpValue=$addr.value; } ) |
		  (tp=('r'|'w'|'a') reg
		  	{ dpOp=$tp.text.concat("r"); dpValue=$reg.value; } )) 
		      { ui.cmdDebugPoint (dpType, dpOp, dpIsEnabled, dpValue); } ) |
		 (tp=('t'|'trace'|'not'|'notrace') 'prog'
		  	{ ui.cmdTraceProg ($tp.text.charAt(0)!='n'); }) |
		 (tp=('not'|'notrace'|'nob'|'nobreak') 'all')
		 	{ ui.cmdClearDebugPoints ($tp.text.charAt(2)); } );
showC	:	('show' tp=('t'|'trace'|'b'|'break'))
			{ ui.cmdShowDebugPoints ($tp.text.charAt(0)); };
helpC 	:	'help'  { ui.cmdHelp (); };
quitC	:	'quit' 
			{ ui.cmdQuit (); };
number returns [int value]
	:	Hex 
			{ $value=(int)(Long.parseLong($Hex.text.substring(2),16)); } | 
		Decimal 
			{ $value=Integer.parseInt($Decimal.text); };
instr	:	HexDigit*;
reg returns [int value]
	:	Register
			{ $value=Integer.parseInt($Register.text.substring(1)); };
regfmt returns [String value]
	:	'x' { value = "x"; } | 'i' { value = "i"; } |'d' { value = "d"; };
memfmt returns [String value]
	:	'x' { value = "x"; } | 'i' { value = "i"; } | 'd' { value = "d"; };

Register:	('r'|'R') RegisterNumber;
String
	:	'"' (~('"'))* '"';
Decimal
	:	Digit+;
Hex
	:	'0' ('x'|'X') HexDigit+;
fragment
RegisterNumber
	:	('0'..'7');
fragment
HexDigit:	('0'..'9'|'a'..'f'|'A'..'F');
fragment
Digit	:	('0'..'9');
NewLine :	'\r'?'\n';
WS  	:   	(' '|'\t')+ {skip();} ;
