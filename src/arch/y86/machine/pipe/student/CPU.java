package arch.y86.machine.pipe.student;

import machine.AbstractMainMemory;
import machine.Register;
import machine.RegisterSet;
import ui.Machine;
import arch.y86.machine.AbstractY86CPU;

/**
 * The Simple Machine CPU.
 * 
 * Simulate execution of a single cycle of the Simple Machine Y86-Pipe CPU.
 */

public class CPU extends AbstractY86CPU.Pipelined
{

    /*
     * Jump cache structure: has a fixed size table to count number of taken
     * jumps at some address has another table of the same size to validate
     * address
     */
    private class JumpCache
    {
	public final static int CACHE_SIZE = 256;
	public final static int CACHE_MASK = 0xFF;
	private int[] addressTable = new int[CACHE_SIZE];
	private byte[] countTable = new byte[CACHE_SIZE];
	public boolean predictedJump;

	void set(int address, byte count)
	{
	    addressTable[address & CACHE_MASK] = address;
	    countTable[address & CACHE_MASK] = count;

	    /*
	     * System.out.printf("Set jump cache addr 0x%x, count 0x%x\n",
	     * address & CACHE_MASK, (int) count);
	     */
	}

	byte get(int address)
	{
	    return countTable[address & CACHE_MASK];
	}

	boolean predictJump(int address, int valC)
	{
	    // If cache miss, predict backward jumps will be taken
	    if (addressTable[address & CACHE_MASK] != address)
		return valC < address;

	    // If cache hit, count 1 bits
	    byte bits = jumpCache.get(address);
	    int sumBits = 0;
	    while (bits > 0)
	    {
		sumBits += bits & 1;
		bits = (byte) (bits >> 1);
	    }
	    /*
	     * System.out.printf("Jump cache addr 0x%x, sumBits %d\n", address,
	     * sumBits);
	     */
	    return (sumBits > 4);
	}

    }

    private JumpCache jumpCache;
    static boolean USE_JUMP_CACHE;

    public CPU(String name, AbstractMainMemory memory)
    {
	super(name, memory);
	/*
	 * Toggle USE_JUMP_CACHE to use jump cache or always predict jumps taken
	 */
	jumpCache = new JumpCache();
	USE_JUMP_CACHE = true;
    }

    /**
     * Execute one clock cycle with all stages executing in parallel.
     * 
     * @throws InvalidInstructionException
     *             if instruction is invalid (including invalid register
     *             number).
     * @throws AbstractMainMemory.InvalidAddressException
     *             if instruction attemps an invalid memory access (either
     *             instruction or data).
     * @throws MachineHaltException
     *             if instruction halts the CPU.
     * @throws Register.TimingException
     */
    @Override
    protected void cycle() throws InvalidInstructionException,
	    AbstractMainMemory.InvalidAddressException, MachineHaltException,
	    Register.TimingException, ImplementationException
    {
	cyclePipe();
    }

    /**
     * Pipeline Hazard Control
     * 
     * IMPLEMENTED BY STUDENT
     */

    @Override
    protected void pipelineHazardControl() throws Register.TimingException
    {
	// Data Hazards
	if ((d.srcA.getValueProduced() != R_NONE && E.dstM.get() == d.srcA
		.getValueProduced())
		|| (d.srcB.getValueProduced() != R_NONE && E.dstM.get() == d.srcB
			.getValueProduced()))
	{
	    // 1 stall if srcA or srcB in Decode depends on dstM in Execute
	    F.stall = true;
	    D.stall = true;
	    E.bubble = true;
	}

	// Branch is always predicted to be taken
	else if (!USE_JUMP_CACHE && E.iCd.get() == I_JXX && E.iFn.get() != C_NC
		&& e.cnd.getValueProduced() == 0)
	{
	    // Predicted jump taken and wrong, turn the 2 stages after fetch
	    // into bubble
	    D.bubble = true;
	    E.bubble = true;
	}

	// Branch is predicted using a jump cache
	else if (USE_JUMP_CACHE && E.iCd.get() == I_JXX && E.iFn.get() != C_NC)
	{
	    // First find out what was predicted using the instruction address
	    // in Decode
	    int branchAddr;
	    switch (D.iCd.get())
	    {
	    case I_NOP:
	    case I_HALT:
	    case I_RET:
		branchAddr = D.valP.get() - 1;
		break;
	    case I_RRMVXX:
	    case I_OPL:
	    case I_PUSHL:
	    case I_POPL:
		branchAddr = D.valP.get() - 2;
		break;
	    case I_JXX:
	    case I_CALL:
		branchAddr = D.valP.get() - 5;
		break;
	    case I_IRMOVL:
	    case I_RMMOVL:
	    case I_MRMOVL:
		branchAddr = D.valP.get() - 6;
		break;
	    default:
		throw new AssertionError();
	    }

	    // Update jump prediction cache for this jump instruction at its
	    // address
	    jumpCache.set(E.valP.get() - 5,
		    (byte) (jumpCache.get(E.valP.get() - 5) << 1 | e.cnd
			    .getValueProduced()));

	    // If address of the instruction in Decode is the jump address, then
	    // the prediction was a "taken", otherwise it was a "not taken"
	    jumpCache.predictedJump = branchAddr == E.valC.get();
	    if ((jumpCache.predictedJump && e.cnd.getValueProduced() == 0)
		    || (!jumpCache.predictedJump && e.cnd.getValueProduced() == 1))
	    {
		// Wrong prediction, turn the 2 stages after fetch into bubble
		D.bubble = true;
		E.bubble = true;
	    }
	}
	// Control Hazard: RET
	else if (D.iCd.get() == I_RET || E.iCd.get() == I_RET
		|| M.iCd.get() == I_RET)
	{
	    F.stall = true;
	    D.bubble = true;
	}
    }

    /**
     * The SelectPC part of the fetch stage
     * 
     * IMPLEMENTED BY STUDENT
     */

    @Override
    protected void fetch_SelectPC() throws Register.TimingException
    {

	// Conditional branch in M
	if (M.iCd.get() == I_JXX && M.iFn.get() != C_NC)
	{
	    // Jumps are predicted using a jump cache
	    if (USE_JUMP_CACHE)
	    {

		// Predicted "jump not taken" and wrong
		if (!jumpCache.predictedJump && M.cnd.get() == 1)
		    f.pc.set(M.valC.get());

		// Predicted "jump taken" and wrong
		else if (jumpCache.predictedJump && M.cnd.get() == 0)
		    f.pc.set(M.valP.get());

		// Prediction was correct, continue with predicted PC
		else
		    f.pc.set(F.prPC.get());

	    }
	    // Jumps always predicted to be taken
	    else
	    {
		// jump prediction wrong
		if (M.cnd.get() == 0)
		    f.pc.set(M.valP.get());
		else
		    f.pc.set(F.prPC.get());
	    }
	}

	// RET in W
	else if (W.iCd.get() == I_RET)
	    f.pc.set(W.valM.get());

	// Otherwise, predicted value is correct
	else
	    f.pc.set(F.prPC.get());
    }

    /**
     * PC prediction part of FETCH stage. Predicts PC to fetch in next cycle.
     * Writes the predicted PC into the f.prPC register.
     * 
     * IMPLEMENTED BY STUDENT
     */

    private void fetch_PredictPC() throws Register.TimingException
    {
	if (f.stat.getValueProduced() == S_AOK)
	    switch (f.iCd.getValueProduced())
	    {
	    case I_JXX:
		// always jump for non-conditional jump
		if (f.iFn.getValueProduced() == C_NC)
		{
		    f.prPC.set(f.valC.getValueProduced());
		    break;
		}
		// Jumps are predicted using a cache
		if (USE_JUMP_CACHE)
		{
		    boolean takeJump = jumpCache.predictJump(
			    f.valP.getValueProduced() - 5,
			    f.valC.getValueProduced());
		    /*
		     * System.out.printf("Predicts " + (takeJump ? "jump taken"
		     * : "jump not taken") + " at addr 0x%x\n", valP - 5);
		     */
		    f.prPC.set(takeJump ? f.valC.getValueProduced() : f.valP
			    .getValueProduced());

		    break;
		}
	    case I_CALL:
		f.prPC.set(f.valC.getValueProduced());
		break;
	    default:
		f.prPC.set(f.valP.getValueProduced());
	    }
	else
	    f.prPC.set(f.pc.getValueProduced());
    }

    /**
     * The FETCH stage of CPU
     * 
     * @throws Register.TimingException
     */

    @Override
    protected void fetch() throws Register.TimingException
    {
	try
	{

	    // determine correct PC for this stage
	    fetch_SelectPC();

	    // get iCd and iFn
	    f.iCd.set(mem.read(f.pc.getValueProduced(), 1)[0].value() >>> 4);
	    f.iFn.set(mem.read(f.pc.getValueProduced(), 1)[0].value() & 0xf);

	    // stat MUX
	    switch (f.iCd.getValueProduced())
	    {
	    case I_HALT:
	    case I_NOP:
	    case I_IRMOVL:
	    case I_RMMOVL:
	    case I_MRMOVL:
	    case I_RET:
	    case I_PUSHL:
	    case I_POPL:
	    case I_CALL:
		switch (f.iFn.getValueProduced())
		{
		case 0x0:
		    f.stat.set(S_AOK);
		    break;
		default:
		    f.stat.set(S_INS);
		    break;
		}
		break;
	    case I_RRMVXX:
	    case I_JXX:
		switch (f.iFn.getValueProduced())
		{
		case C_NC:
		case C_LE:
		case C_L:
		case C_E:
		case C_NE:
		case C_GE:
		case C_G:
		    f.stat.set(S_AOK);
		    break;
		default:
		    f.stat.set(S_INS);
		}
		break;
	    case I_OPL:
		switch (f.iFn.getValueProduced())
		{
		case A_ADDL:
		case A_SUBL:
		case A_ANDL:
		case A_XORL:
		case A_MULL:
		case A_DIVL:
		case A_MODL:
		    f.stat.set(S_AOK);
		    break;
		default:
		    f.stat.set(S_INS);
		    break;
		}
		break;
	    default:
		f.stat.set(S_INS);
		break;
	    }

	    if (f.stat.getValueProduced() == S_AOK)
	    {

		// rA MUX
		switch (f.iCd.getValueProduced())
		{
		case I_HALT:
		    f.rA.set(R_NONE);
		    f.stat.set(S_HLT);
		    break;
		case I_RRMVXX:
		case I_RMMOVL:
		case I_MRMOVL:
		case I_OPL:
		case I_PUSHL:
		case I_POPL:
		    f.rA.set(mem.read(f.pc.getValueProduced() + 1, 1)[0]
			    .value() >>> 4);
		    break;
		default:
		    f.rA.set(R_NONE);
		}

		// rB MUX
		switch (f.iCd.getValueProduced())
		{
		case I_RRMVXX:
		case I_IRMOVL:
		case I_RMMOVL:
		case I_MRMOVL:
		case I_OPL:
		    f.rB.set(mem.read(f.pc.getValueProduced() + 1, 1)[0]
			    .value() & 0xf);
		    break;
		default:
		    f.rB.set(R_NONE);
		}

		// valC MUX
		switch (f.iCd.getValueProduced())
		{
		case I_IRMOVL:
		case I_RMMOVL:
		case I_MRMOVL:
		    f.valC.set(mem.readIntegerUnaligned(f.pc.getValueProduced() + 2));
		    break;
		case I_JXX:
		case I_CALL:
		    f.valC.set(mem.readIntegerUnaligned(f.pc.getValueProduced() + 1));
		    break;
		default:
		    f.valC.set(0);
		}

		// valP MUX
		switch (f.iCd.getValueProduced())
		{
		case I_NOP:
		case I_HALT:
		case I_RET:
		    f.valP.set(f.pc.getValueProduced() + 1);
		    break;
		case I_RRMVXX:
		case I_OPL:
		case I_PUSHL:
		case I_POPL:
		    f.valP.set(f.pc.getValueProduced() + 2);
		    break;
		case I_JXX:
		case I_CALL:
		    f.valP.set(f.pc.getValueProduced() + 5);
		    break;
		case I_IRMOVL:
		case I_RMMOVL:
		case I_MRMOVL:
		    f.valP.set(f.pc.getValueProduced() + 6);
		    break;
		default:
		    throw new AssertionError();
		}
	    }
	} catch (AbstractMainMemory.InvalidAddressException iae)
	{
	    f.stat.set(S_ADR);
	}

	// predict PC for next cycle
	fetch_PredictPC();
    }

    /**
     * Determine current value of specified register by employing data
     * fowarding, where necessary. STUDENT CHANGES THIS METHOD TO IMPLEMENT DATA
     * FORWARDING
     * 
     * @param regNum
     *            number of register being read
     * @return value of register
     * @throws Machine.RegisterSet.InvalidRegisterNumberException
     *             if register number is invalid
     */
    private int decode_ReadRegisterWithForwarding(int regNum)
	    throws RegisterSet.InvalidRegisterNumberException,
	    Register.TimingException
    {

	if (regNum == R_NONE)
	    return 0;
	// Stages closer to decode have higher priority of forwarding its data
	// A stage can only forward its data if cnd is 1

	// Execute stage can only forward valE
	else if (regNum == E.dstE.get() && e.cnd.getValueProduced() == 1)
	    return e.valE.getValueProduced();

	else if (regNum == M.dstM.get() && M.cnd.get() == 1)
	    return m.valM.getValueProduced();
	else if (regNum == M.dstE.get() && M.cnd.get() == 1)
	    return M.valE.get();

	else if (regNum == W.dstM.get() && W.cnd.get() == 1)
	    return W.valM.get();
	else if (regNum == W.dstE.get() && W.cnd.get() == 1)
	    return W.valE.get();

	// If no dependency on subsequent stages, just read register
	else
	    return reg.get(regNum);
    }

    /**
     * The DECODE stage of CPU
     * 
     * @throws Register.TimingException
     */

    @Override
    protected void decode() throws Register.TimingException
    {

	// pass-through signals
	d.stat.set(D.stat.get());
	d.iCd.set(D.iCd.get());
	d.iFn.set(D.iFn.get());
	d.valC.set(D.valC.get());
	d.valP.set(D.valP.get());

	if (D.stat.get() == S_AOK)
	{
	    try
	    {

		// srcA MUX
		switch (D.iCd.get())
		{
		case I_RRMVXX:
		case I_RMMOVL:
		case I_OPL:
		case I_PUSHL:
		    d.srcA.set(D.rA.get());
		    break;
		case I_RET:
		case I_POPL:
		    d.srcA.set(R_ESP);
		    break;
		default:
		    d.srcA.set(R_NONE);
		}

		// srcB MUX
		switch (D.iCd.get())
		{
		case I_RMMOVL:
		case I_MRMOVL:
		case I_OPL:
		    d.srcB.set(D.rB.get());
		    break;
		case I_CALL:
		case I_RET:
		case I_PUSHL:
		case I_POPL:
		    d.srcB.set(R_ESP);
		    break;
		default:
		    d.srcB.set(R_NONE);
		}

		// dstE MUX
		switch (D.iCd.get())
		{
		case I_RRMVXX:
		case I_IRMOVL:
		case I_OPL:
		    d.dstE.set(D.rB.get());
		    break;
		case I_CALL:
		case I_RET:
		case I_PUSHL:
		case I_POPL:
		    d.dstE.set(R_ESP);
		    break;
		default:
		    d.dstE.set(R_NONE);
		}

		// dstM MUX
		switch (D.iCd.get())
		{
		case I_MRMOVL:
		case I_POPL:
		    d.dstM.set(D.rA.get());
		    break;
		default:
		    d.dstM.set(R_NONE);
		}

		try
		{
		    d.valA.set(decode_ReadRegisterWithForwarding(d.srcA
			    .getValueProduced()));
		    d.valB.set(decode_ReadRegisterWithForwarding(d.srcB
			    .getValueProduced()));
		} catch (RegisterSet.InvalidRegisterNumberException irne)
		{
		    throw new InvalidInstructionException(irne);
		}
	    } catch (InvalidInstructionException iie)
	    {
		d.stat.set(S_INS);
	    }
	}

	if (d.stat.getValueProduced() != S_AOK)
	{
	    d.srcA.set(R_NONE);
	    d.srcB.set(R_NONE);
	    d.dstE.set(R_NONE);
	    d.dstM.set(R_NONE);
	}
    }

    /**
     * The EXECUTE stage of CPU
     * 
     * @throws Register.TimingException
     */

    @Override
    protected void execute() throws Register.TimingException
    {

	// pass-through signals
	e.stat.set(E.stat.get());
	e.iCd.set(E.iCd.get());
	e.iFn.set(E.iFn.get());
	e.valC.set(E.valC.get());
	e.valA.set(E.valA.get());
	e.dstE.set(E.dstE.get());
	e.dstM.set(E.dstM.get());
	e.valP.set(E.valP.get());

	if (E.stat.get() == S_AOK)
	{

	    // aluA MUX
	    int aluA;
	    switch (E.iCd.get())
	    {
	    case I_RRMVXX:
	    case I_OPL:
		aluA = E.valA.get();
		break;
	    case I_IRMOVL:
	    case I_MRMOVL:
	    case I_RMMOVL:
		aluA = E.valC.get();
		break;
	    case I_RET:
	    case I_POPL:
		aluA = 4;
		break;
	    case I_CALL:
	    case I_PUSHL:
		aluA = -4;
		break;
	    default:
		aluA = 0;
	    }

	    // aluB MUX
	    int aluB;
	    switch (E.iCd.get())
	    {
	    case I_RRMVXX:
	    case I_IRMOVL:
		aluB = 0;
		break;
	    case I_RMMOVL:
	    case I_MRMOVL:
	    case I_OPL:
	    case I_CALL:
	    case I_RET:
	    case I_PUSHL:
	    case I_POPL:
		aluB = E.valB.get();
		break;
	    default:
		aluB = 0;
	    }

	    // aluFun and setCC muxes MUX
	    int aluFun;
	    boolean setCC;
	    switch (E.iCd.get())
	    {
	    case I_RRMVXX:
	    case I_IRMOVL:
	    case I_RMMOVL:
	    case I_MRMOVL:
	    case I_CALL:
	    case I_RET:
	    case I_PUSHL:
	    case I_POPL:
		aluFun = A_ADDL;
		setCC = false;
		break;
	    case I_OPL:
		aluFun = E.iFn.get();
		setCC = true;
		break;
	    default:
		aluFun = 0;
		setCC = false;
	    }

	    // the ALU
	    boolean overflow;
	    switch (aluFun)
	    {
	    case A_ADDL:
		e.valE.set(aluB + aluA);
		overflow = ((aluB < 0) == (aluA < 0))
			&& ((e.valE.getValueProduced() < 0) != (aluB < 0));
		break;
	    case A_SUBL:
		e.valE.set(aluB - aluA);
		overflow = ((aluB < 0) != (aluA < 0))
			&& ((e.valE.getValueProduced() < 0) != (aluB < 0));
		break;
	    case A_ANDL:
		e.valE.set(aluB & aluA);
		overflow = false;
		break;
	    case A_XORL:
		e.valE.set(aluB ^ aluA);
		overflow = false;
		break;
	    case A_MULL:
		int result = aluB * aluA;
		e.valE.set(result);
		overflow = aluB != 0 && result / aluB != aluA;
		break;
	    case A_DIVL:
		e.valE.set(aluA == 0 ? aluB : aluB / aluA);
		overflow = aluA == 0;
		break;
	    case A_MODL:
		e.valE.set(aluA == 0 ? aluB : aluB % aluA);
		overflow = aluA == 0;
		break;
	    default:
		overflow = false;
	    }

	    // CC MUX
	    if (setCC)
		p.cc.set(((e.valE.getValueProduced() == 0) ? 0x100 : 0)
			| ((e.valE.getValueProduced() < 0) ? 0x10 : 0)
			| (overflow ? 0x1 : 0));
	    else
		p.cc.set(P.cc.get());

	    // cnd MUX
	    boolean cnd;
	    switch (E.iCd.get())
	    {
	    case I_JXX:
	    case I_RRMVXX:
		boolean zf = (P.cc.get() & 0x100) != 0;
		boolean sf = (P.cc.get() & 0x010) != 0;
		boolean of = (P.cc.get() & 0x001) != 0;
		switch (E.iFn.get())
		{
		case C_NC:
		    cnd = true;
		    break;
		case C_LE:
		    cnd = (sf ^ of) | zf;
		    break;
		case C_L:
		    cnd = sf ^ of;
		    break;
		case C_E:
		    cnd = zf;
		    break;
		case C_NE:
		    cnd = !zf;
		    break;
		case C_GE:
		    cnd = !(sf ^ of);
		    break;
		case C_G:
		    cnd = !(sf ^ of) & !zf;
		    break;
		default:
		    throw new AssertionError();
		}
		break;
	    default:
		cnd = true;
	    }
	    e.cnd.set(cnd ? 1 : 0);

	} else
	    e.cnd.set(0);
    }

    /**
     * The MEMORY stage of CPU
     * 
     * @throws Register.TimingException
     */

    @Override
    protected void memory() throws Register.TimingException
    {

	// pass-through signals
	m.iCd.set(M.iCd.get());
	m.iFn.set(M.iFn.get());
	m.cnd.set(M.cnd.get());
	m.valE.set(M.valE.get());
	m.dstE.set(M.dstE.get());
	m.dstM.set(M.dstM.get());
	m.valP.set(M.valP.get());

	if (M.stat.get() == S_AOK)
	{
	    try
	    {

		// write Main Memory
		switch (M.iCd.get())
		{
		case I_RMMOVL:
		case I_PUSHL:
		    mem.writeInteger(M.valE.get(), M.valA.get());
		    break;
		case I_CALL:
		    mem.writeInteger(M.valE.get(), M.valP.get());
		    break;
		default:
		}

		// valM MUX (read main memory)
		switch (M.iCd.get())
		{
		case I_MRMOVL:
		    m.valM.set(mem.readInteger(M.valE.get()));
		    break;
		case I_RET:
		case I_POPL:
		    m.valM.set(mem.readInteger(M.valA.get()));
		    break;
		default:
		}
		m.stat.set(M.stat.get());

	    } catch (AbstractMainMemory.InvalidAddressException iae)
	    {
		m.stat.set(S_ADR);
	    }

	} else
	{
	    m.stat.set(M.stat.get());
	}
    }

    /**
     * The WRITE BACK stage of CPU
     * 
     * @throws MachineHaltException
     *             if instruction halts the CPU (e.g., halt instruction).
     * @throws InvalidInstructionException
     * @throws AbstractMainMemory.InvalidAddressException
     * @throws Register.TimingException
     */

    @Override
    protected void writeBack() throws MachineHaltException,
	    InvalidInstructionException,
	    AbstractMainMemory.InvalidAddressException,
	    Register.TimingException
    {
	if (W.stat.get() == S_AOK)
	    try
	    {
		try
		{

		    // write valE to register file
		    if (W.dstE.get() != R_NONE && W.cnd.get() == 1)
			reg.set(W.dstE.get(), W.valE.get());

		    // write valM to register file
		    if (W.dstM.get() != R_NONE)
			reg.set(W.dstM.get(), W.valM.get());

		    w.stat.set(W.stat.get());

		} catch (RegisterSet.InvalidRegisterNumberException irne)
		{
		    throw new InvalidInstructionException(irne);
		}

	    } catch (InvalidInstructionException iie)
	    {
		w.stat.set(S_INS);
	    }
	else
	    w.stat.set(W.stat.get());

	if (w.stat.getValueProduced() == S_ADR)
	    throw new AbstractMainMemory.InvalidAddressException();
	else if (w.stat.getValueProduced() == S_INS)
	    throw new InvalidInstructionException();
	else if (w.stat.getValueProduced() == S_HLT)
	    throw new MachineHaltException();
    }
}