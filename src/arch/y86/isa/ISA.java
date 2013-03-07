package arch.y86.isa;

import isa.AbstractISA;

import java.util.HashMap;

import arch.y86.machine.AbstractY86CPU;

public class ISA extends AbstractISA {
  HashMap <Integer,String> registerNames = new HashMap <Integer,String> ();
  InsLayout opCode, register, regIndirect, immediate, literal, baseOffset, baseOffset1, baseOffset2, baseOffset4, indirect, f;

  public ISA () {
    super ("Y86", Endianness.LITTLE, new Assembler ());

    registerNames.put (0, "eax");
    registerNames.put (1, "ecx");
    registerNames.put (2, "edx");
    registerNames.put (3, "ebx");
    registerNames.put (4, "esp");
    registerNames.put (5, "ebp");
    registerNames.put (6, "esi");
    registerNames.put (7, "edi");

    opCode      = new OpCodeField    (8,  "%02x",  "%s",     "%s");
    register    = new DictonaryField (4,  "%x",    "%%%s",   "r[%s]",    registerNames);
    immediate   = new LabelableField (32, " %08x", "$0x%x",  "%s", "0x%x", "%s");
    literal     = new LabelableField (32, " %08x", "0x%x",   "%s", "0x%x", "%s");
    baseOffset  = new CompoundField  (new InsLayout[] {register,literal}, new int[] {1,0}, new String[] {"%s","(%s)"},   new int[] {1,0}, new String[] {"m[%s ","+ %s]"});
    baseOffset1 = new CompoundField  (new InsLayout[] {register,literal}, new int[] {1,0}, new String[] {"%s","(%s)"},   new int[] {1,0}, new String[] {"m[%s ","+ %s]"});
    baseOffset2 = new CompoundField  (new InsLayout[] {register,literal}, new int[] {1,0}, new String[] {"%s","(%s,2)"}, new int[] {1,0}, new String[] {"m[%s ","+ %s*2]"});
    baseOffset4 = new CompoundField  (new InsLayout[] {register,literal}, new int[] {1,0}, new String[] {"%s","(%s,4)"}, new int[] {1,0}, new String[] {"m[%s ","+ %s*4]"});
    indirect    = new CompoundField  (new InsLayout[] {register,literal}, new int[] {1,0}, new String[] {"%s","(%s)"},   new int[] {1,0}, new String[] {"%s ","+ %s"});
    f           = new ConstantField  (4,  "%x",     "%s", "%s", 0xf);

    define ((AbstractY86CPU.I_HALT   << 4),
	new CompoundField (new InsLayout[] {opCode},	                     new int[] {0},     new String[] {"halt"},                new int[] {0},     new String[] {"halt"}));

    define ((AbstractY86CPU.I_NOP    << 4),
	new CompoundField (new InsLayout[] {opCode},	                     new int[] {0},     new String[] {"nop"},                 new int[] {0},     new String[] {"nop"}));

    define ((AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_NC,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"rrmovl ","%s, ","%s"}, new int[] {2,1},   new String[] {"%s = ","%s if CC <= 0"}));

    define ((AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_LE,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"cmovle ","%s, ","%s"}, new int[] {2,1},   new String[] {"%s = ","%s if CC < 0"}));

    define ((AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_L,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"cmovl  ","%s, ","%s"}, new int[] {2,1},   new String[] {"%s = ","%s if CC == 0"}));

    define ((AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_E,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"cmove  ","%s, ","%s"}, new int[] {2,1},   new String[] {"%s = ","%s if CC != 0"}));

    define ((AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_NE,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"cmovne ","%s, ","%s"}, new int[] {2,1},   new String[] {"%s = ","%s if CC >= 0"}));

    define ((AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_GE,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"cmovge ","%s, ","%s"}, new int[] {2,1},   new String[] {"%s = ","%s if CC == 0"}));

    define ((AbstractY86CPU.I_RRMVXX << 4) | AbstractY86CPU.C_G,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"cmovg  ","%s, ","%s"}, new int[] {2,1},   new String[] {"%s = ","%s"}));

    define ((AbstractY86CPU.I_IRMOVL << 4),
	new CompoundField (new InsLayout[] {opCode, f, register, immediate}, new int[] {0,3,2}, new String[] {"irmovl ","%s, ","%s"}, new int[] {2,3},   new String[] {"%s = ","%s"}));

    define ((AbstractY86CPU.I_RMMOVL << 4) | 0x0,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset},   new int[] {0,1,2}, new String[] {"rmmovl ","%s, ","%s"}, new int[] {2,1},   new String[] {"%s = ","%s"}));

    define ((AbstractY86CPU.I_RMMOVL << 4) | 0x1,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset1},  new int[] {0,1,2}, new String[] {"rmmovl ","%s, ","%s"}, new int[] {2,1},   new String[] {"%s = ","%s"}));

    define ((AbstractY86CPU.I_RMMOVL << 4) | 0x2,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset2},  new int[] {0,1,2}, new String[] {"rmmovl ","%s, ","%s"}, new int[] {2,1},   new String[] {"%s = ","%s"}));

    define ((AbstractY86CPU.I_RMMOVL << 4) | 0x4,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset4},  new int[] {0,1,2}, new String[] {"rmmovl ","%s, ","%s"}, new int[] {2,1},   new String[] {"%s = ","%s"}));

    define ((AbstractY86CPU.I_MRMOVL << 4) | 0x0,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset},   new int[] {0,2,1}, new String[] {"mrmovl ","%s, ","%s"}, new int[] {1,2},   new String[] {"%s = ","%s"}));

    define ((AbstractY86CPU.I_MRMOVL << 4) | 0x1,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset1},  new int[] {0,2,1}, new String[] {"mrmovl ","%s, ","%s"}, new int[] {1,2},   new String[] {"%s = ","%s"}));

    define ((AbstractY86CPU.I_MRMOVL << 4) | 0x2,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset2},  new int[] {0,2,1}, new String[] {"mrmovl ","%s, ","%s"}, new int[] {1,2},   new String[] {"%s = ","%s"}));

    define ((AbstractY86CPU.I_MRMOVL << 4) | 0x4,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset4},  new int[] {0,2,1}, new String[] {"mrmovl ","%s, ","%s"}, new int[] {1,2},   new String[] {"%s = ","%s"}));

    define ((AbstractY86CPU.I_OPL    << 4) | AbstractY86CPU.A_ADDL,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"addl   ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s + ","%s"}));

    define ((AbstractY86CPU.I_OPL    << 4) | AbstractY86CPU.A_SUBL,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"subl   ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s - ","%s"}));

    define ((AbstractY86CPU.I_OPL    << 4) | AbstractY86CPU.A_ANDL,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"andl   ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s & ","%s"}));

    define ((AbstractY86CPU.I_OPL    << 4) | AbstractY86CPU.A_XORL,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"xorl   ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s xor ","%s"}));

    define ((AbstractY86CPU.I_OPL    << 4) | AbstractY86CPU.A_MULL,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"mull   ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s * ","%s"}));

    define ((AbstractY86CPU.I_OPL    << 4) | AbstractY86CPU.A_DIVL,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"divl   ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s / ","%s"}));

    define ((AbstractY86CPU.I_OPL    << 4) | AbstractY86CPU.A_MODL,
	new CompoundField (new InsLayout[] {opCode, register, register},     new int[] {0,1,2}, new String[] {"modl   ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s %% ","%s"}));

    define ((AbstractY86CPU.I_JXX    << 4) | AbstractY86CPU.C_NC,
	new CompoundField (new InsLayout[] {opCode, literal},                new int[] {0,1},   new String[] {"jmp    ","%s"},        new int[] {1},     new String[] {"pc = %s"}));

    define ((AbstractY86CPU.I_JXX    << 4) | AbstractY86CPU.C_LE,
	new CompoundField (new InsLayout[] {opCode, literal},                new int[] {0,1},   new String[] {"jle    ","%s"},        new int[] {1},     new String[] {"pc = %s if CC <= 0"}));

    define ((AbstractY86CPU.I_JXX    << 4) | AbstractY86CPU.C_L,
	new CompoundField (new InsLayout[] {opCode, literal},                new int[] {0,1},   new String[] {"jl     ","%s"},        new int[] {1},     new String[] {"pc = %s if CC < 0"}));

    define ((AbstractY86CPU.I_JXX    << 4) | AbstractY86CPU.C_E,
	new CompoundField (new InsLayout[] {opCode, literal},                new int[] {0,1},   new String[] {"je     ","%s"},        new int[] {1},     new String[] {"pc = %s if CC == 0"}));

    define ((AbstractY86CPU.I_JXX    << 4) | AbstractY86CPU.C_NE,
	new CompoundField (new InsLayout[] {opCode, literal},                new int[] {0,1},   new String[] {"jne    ","%s"},        new int[] {1},     new String[] {"pc = %s if CC != 0"}));

    define ((AbstractY86CPU.I_JXX    << 4) | AbstractY86CPU.C_GE,
	new CompoundField (new InsLayout[] {opCode, literal},                new int[] {0,1},   new String[] {"jge    ","%s"},        new int[] {1},     new String[] {"pc = %s if CC >= 0"}));

    define ((AbstractY86CPU.I_JXX    << 4) | AbstractY86CPU.C_G,
	new CompoundField (new InsLayout[] {opCode, literal},                new int[] {0,1},   new String[] {"jg     ","%s"},        new int[] {1},     new String[] {"pc = %s if CC > 0"}));

    define ((AbstractY86CPU.I_CALL   << 4) | AbstractY86CPU.X_DIRECT_CALL,
	new CompoundField (new InsLayout[] {opCode, literal},                new int[] {0,1},   new String[] {"call   ","%s"},        new int[] {1},     new String[] {"push pc; pc = %s"}));

    define ((AbstractY86CPU.I_CALL   << 4) | AbstractY86CPU.X_INDIRECT_CALL,
	new CompoundField (new InsLayout[] {opCode, register, f},            new int[] {0,1},   new String[] {"call   ","(%s)"},      new int[] {1},     new String[] {"push pc; pc = %s"}));

    define ((AbstractY86CPU.I_CALL   << 4) | AbstractY86CPU.X_INDIRECT_FLAG | 0x1,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset1},  new int[] {0,2,1}, new String[] {"call ","%s, ","%s"},   new int[] {1,2},   new String[] {"%s = pc;", "pc = %s"}));

    define ((AbstractY86CPU.I_CALL   << 4) | AbstractY86CPU.X_INDIRECT_FLAG | 0x2,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset2},  new int[] {0,2,1}, new String[] {"call ","%s, ","%s"},   new int[] {1,2},   new String[] {"%s = pc;", "pc = %s"}));

    define ((AbstractY86CPU.I_CALL   << 4) | AbstractY86CPU.X_INDIRECT_FLAG | 0x4,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset4},  new int[] {0,2,1}, new String[] {"call ","%s, ","%s"},   new int[] {1,2},   new String[] {"%s = pc;", "pc = %s"}));

    define ((AbstractY86CPU.I_CALL   << 4) | AbstractY86CPU.X_DBL_INDIRECT_FLAG | 0x1,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset1},  new int[] {0,2,1}, new String[] {"call ","*%s, ","%s"},   new int[] {1,2},   new String[] {"%s = pc;", "pc = %s"}));

    define ((AbstractY86CPU.I_CALL   << 4) | AbstractY86CPU.X_DBL_INDIRECT_FLAG | 0x2,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset2},  new int[] {0,2,1}, new String[] {"call ","*%s, ","%s"},   new int[] {1,2},   new String[] {"%s = pc;", "pc = %s"}));

    define ((AbstractY86CPU.I_CALL   << 4) | AbstractY86CPU.X_DBL_INDIRECT_FLAG | 0x4,
	new CompoundField (new InsLayout[] {opCode, register, baseOffset4},  new int[] {0,2,1}, new String[] {"call ","*%s, ","%s"},   new int[] {1,2},   new String[] {"%s = pc;", "pc = %s"}));

    define ((AbstractY86CPU.I_RET << 4),
	new CompoundField (new InsLayout[] {opCode},                          new int[] {0},     new String[] {"ret"},                 new int[] {0},     new String[] {"pop pc"}));

    define ((AbstractY86CPU.I_PUSHL << 4),
	new CompoundField (new InsLayout[] {opCode, register, f},             new int[] {0,1},   new String[] {"pushl  ","%s"},        new int[] {1},     new String[] {"m[r[esp]] <= %s; r[esp] += 4"}));

    define ((AbstractY86CPU.I_POPL << 4),
	new CompoundField (new InsLayout[] {opCode, register, f},             new int[] {0,1},   new String[] {"popl   ","%s"},        new int[] {1},     new String[] {"%s = m[r[esp]-4]; r[esp] -= 4"}));

    define ((AbstractY86CPU.I_IOPL << 4) | AbstractY86CPU.A_ADDL,
	new CompoundField (new InsLayout[] {opCode, f, register, immediate},  new int[] {0,3,2}, new String[] {"iaddl  ","%s, ","%s"}, new int[] {2,2,3}, new String[] {"%s = ","%s + ","%s"}));

    define ((AbstractY86CPU.I_IOPL << 4) | AbstractY86CPU.A_SUBL,
	new CompoundField (new InsLayout[] {opCode, f, register, immediate},  new int[] {0,3,2}, new String[] {"isubl  ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s - ","%s"}));

    define ((AbstractY86CPU.I_IOPL << 4) | AbstractY86CPU.A_ANDL,
	new CompoundField (new InsLayout[] {opCode, f, register, immediate},  new int[] {0,3,2}, new String[] {"iandl  ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s & ","%s"}));

    define ((AbstractY86CPU.I_IOPL << 4) | AbstractY86CPU.A_XORL,
	new CompoundField (new InsLayout[] {opCode, f, register, immediate},  new int[] {0,3,2}, new String[] {"ixorl  ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s xor ","%s"}));

    define ((AbstractY86CPU.I_IOPL << 4) | AbstractY86CPU.A_MULL,
	new CompoundField (new InsLayout[] {opCode, f, register, immediate},  new int[] {0,3,2}, new String[] {"imull  ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s * ","%s"}));

    define ((AbstractY86CPU.I_IOPL << 4) | AbstractY86CPU.A_DIVL,
	new CompoundField (new InsLayout[] {opCode, f, register, immediate},  new int[] {0,3,2}, new String[] {"idivl  ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s / ","%s"}));

    define ((AbstractY86CPU.I_IOPL << 4) | AbstractY86CPU.A_MODL,
	new CompoundField (new InsLayout[] {opCode, f, register, immediate},  new int[] {0,3,2}, new String[] {"imodl  ","%s, ","%s"}, new int[] {2,2,1}, new String[] {"%s = ","%s %% ","%s"}));

    define ((AbstractY86CPU.I_LEAVE << 4),
	new CompoundField (new InsLayout[] {opCode},                          new int[] {0},     new String[] {"leave"},               new int[] {0},     new String[] {"r[esp] = r[ebp] + 4; r[ebp] = m[r[ebp]]"}));

    define ((AbstractY86CPU.I_JMPI << 4) | AbstractY86CPU.X_INDIRECT_FLAG     | 0x1,
	new CompoundField (new InsLayout[] {opCode, f, baseOffset1},          new int[] {0,2},   new String[] {"jmp    ","%s"},       new int[] {2},     new String[] {"pc = m[%s]"}));

    define ((AbstractY86CPU.I_JMPI << 4) | AbstractY86CPU.X_INDIRECT_FLAG     | 0x2,
	new CompoundField (new InsLayout[] {opCode, f, baseOffset2},          new int[] {0,2},   new String[] {"jmp    ","%s"},       new int[] {2},     new String[] {"pc = m[%s]"}));

    define ((AbstractY86CPU.I_JMPI << 4) | AbstractY86CPU.X_INDIRECT_FLAG     | 0x4,
	new CompoundField (new InsLayout[] {opCode, f, baseOffset4},          new int[] {0,2},   new String[] {"jmp    ","%s"},       new int[] {2},     new String[] {"pc = m[%s]"}));

    define ((AbstractY86CPU.I_JMPI << 4) | AbstractY86CPU.X_DBL_INDIRECT_FLAG | 0x1,
	new CompoundField (new InsLayout[] {opCode, f, baseOffset1},          new int[] {0,2},   new String[] {"jmp    ","*%s"},       new int[] {2},     new String[] {"pc = m[%s]"}));

    define ((AbstractY86CPU.I_JMPI << 4) | AbstractY86CPU.X_DBL_INDIRECT_FLAG | 0x2,
	new CompoundField (new InsLayout[] {opCode, f, baseOffset2},          new int[] {0,2},   new String[] {"jmp    ","*%s"},       new int[] {2},     new String[] {"pc = m[%s]"}));

    define ((AbstractY86CPU.I_JMPI << 4) | AbstractY86CPU.X_DBL_INDIRECT_FLAG | 0x4,
	new CompoundField (new InsLayout[] {opCode, f, baseOffset4},          new int[] {0,2},   new String[] {"jmp    ","*%s"},       new int[] {2},     new String[] {"pc = m[%s]"}));

    setPlaceholderInstruction (0x10);
  }
}