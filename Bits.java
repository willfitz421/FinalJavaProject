package Tickets;

/**
 * Enumeration for the number of bits of a system
 * @author ryank
 *
 */
public enum Bits {

	THIRTY_TWO(32), SIXTY_FOUR(64);
	
	private int bits; //associates the bit number with the enum
					   //for easier conversion when needed:
						//ex: SIXTY_FOUR.getBits() returns 64.
						
	
	/**
	 * Constructor provided so that each enum has 
	 * an underlying integer with the bits
	 * @param bits The integer version of the bits enum value
	 */
	Bits(int bits)
	{
		this.bits = bits;
	}

	/**
	 * Gets the associated integer version of the bits
	 * @return The integer bit value (so 32 instead of THIRTY_TWO)
	 */
	int getBits()
	{
		return bits;
	}
	
	/**
	 * Returns a version of the enum that can be used
	 * for nicer printing.
	 * @return a A string with a version of the enum that can be used
	 * for nicer printing
	 */
	@Override
	public String toString() {
		return ("" + bits);
	}
	
}
