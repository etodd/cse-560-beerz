package testing;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import state.MemoryBank;
import state.MachineState;
import instructions.TrapHandler;

/**
 * Tests the functionality of the Trap function.
 */
public class TrapTest {

	/**
	 * Contains the initial state of the memory used for testing.
	 */
	private MemoryBank bank;
	
	/**
	 * Contains the initial state of the machine used for testing.
	 */
	private MachineState state;
	
	/**
	 * Set up the initial state of the machine and memory for all the tests.
	 */
	@Before
	public void init() {
		bank = new MemoryBank();
		state = new MachineState();
	}
	
	/**
	 * Test the halt trap vector.
	 */
	@Test
	public void haltTest() {
		new TrapHandler().execute(0xf025, this.state, this.bank);
		assertEquals("Trap - halt", this.state.executing, false);
	}
	 /**
	  * Tests the out vector.
	  */
	@Test
	public void outTest() {
		//Should output the letter 'A'
		this.state.registers[0] = 0x41;
		new TrapHandler().execute(0xF021, this.state, this.bank);
		
		//Should output the letter 'z'
		this.state.registers[0] = 0x7A;
		new TrapHandler().execute(0xF021, this.state, this.bank);
		
		//Should output the character '!'
		this.state.registers[0] = 0x21;
		new TrapHandler().execute(0xF021, this.state, this.bank);
	}
	 /**
	  * Tests the puts vector.
	  */
	@Test
	public void putsTest() {
		//Should print out "\nTest Message 1?"
		this.state.registers[0] = 0x4000;
		this.bank.write(0x4000, (short) 0x0D); //(new line)
		this.bank.write(0x4001, (short) 0x54); //T
		this.bank.write(0x4002, (short) 0x65); //e
		this.bank.write(0x4003, (short) 0x73); //s
		this.bank.write(0x4004, (short) 0x74); //t
		this.bank.write(0x4005, (short) 0x20); //(space)
		this.bank.write(0x4006, (short) 0x4D); //M
		this.bank.write(0x4007, (short) 0x65); //e
		this.bank.write(0x4008, (short) 0x73); //s
		this.bank.write(0x4009, (short) 0x73); //s
		this.bank.write(0x400A, (short) 0x61); //a
		this.bank.write(0x400B, (short) 0x67); //g
		this.bank.write(0x400C, (short) 0x65); //e
		this.bank.write(0x400D, (short) 0x20); //(space)
		this.bank.write(0x400E, (short) 0x31); //1
		this.bank.write(0x400F, (short) 0x3F); //?
		this.bank.write(0x4010, (short) 0x00); //null
		this.bank.write(0x4011, (short) 0x23); //#
		this.bank.write(0x4012, (short) 0x26); //&
		new TrapHandler().execute(0xF022, this.state, this.bank);	
	}
	 /**
	  * Tests the outn vector.
	  */
	@Test
	public void outnTest() {
		//Print new line to separate output
		this.state.registers[0] = 0x0D;
		new TrapHandler().execute(0xF021, this.state, this.bank);
		
		//Should output -2
		this.state.registers[0] = (short) 0xFFFE;
		new TrapHandler().execute(0xF031, this.state, this.bank);
		
		//Print new line to separate output
		this.state.registers[0] = 0x0D;
		new TrapHandler().execute(0xF021, this.state, this.bank);
		
		//Should output 32767
		this.state.registers[0] = (short) 0x7FFF;
		new TrapHandler().execute(0xF031, this.state, this.bank);
		
		//Print new line to separate output
		this.state.registers[0] = 0x0D;
		new TrapHandler().execute(0xF021, this.state, this.bank);
		
		//Should output 0
		this.state.registers[0] = (short) 0x0;
		new TrapHandler().execute(0xF031, this.state, this.bank);
	}
	 /**
	  * Tests the rnd vector.
	  */
	@Test
	public void rndTest() {
		int cycles = 10;
		while (cycles > 0){
			new TrapHandler().execute(0xF043, this.state, this.bank);
			int reg0 = this.state.registers[0];
			assertEquals("The random number should be greater than 0x8000", true, -0x8000 <= reg0);
			assertEquals("The random number should be less than 0x7FFF", true, reg0 <= 0x7FFF);
			cycles--;
		}
	}
	
}