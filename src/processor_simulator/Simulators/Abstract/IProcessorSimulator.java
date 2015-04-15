package processor_simulator.Simulators.Abstract;

import processor_simulator.Exceptions.CommandArgumentIsInvalidException;
import processor_simulator.Exceptions.CommandIsInvalidException;
import processor_simulator.Models.Command;

public interface IProcessorSimulator
{
	/**
	 * Gets the commands counter.
	 *
	 * @return the commands counter
	 */
	int getCommandsCounter();

	/**
	 * Gets the current command's string representation.
	 *
	 * @return the current command's string representation
	 */
	String getCurrentCommandText();

	/**
	 * Gets the number of bits.
	 *
	 * @return the number of bits
	 */
	int getNumberOfBits();

	/**
	 * Gets the overflow flag.
	 *
	 * @return the overflow flag
	 */
	boolean getOverflowFlag();

	/**
	 * Gets the registers.
	 *
	 * @return the registers
	 */
	int[] getRegisters();

	/**
	 * Gets the sign flag.
	 *
	 * @return the sign flag
	 */
	boolean getSignFlag();

	/**
	 * Gets the tacts counter.
	 *
	 * @return the tacts counter
	 */
	int getTactsCounter();

	/**
	 * Performs the specified command.
	 *
	 * @param command
	 *            the command
	 * @throws CommandIsInvalidException
	 *             the command is invalid exception
	 * @throws CommandArgumentIsInvalidException
	 *             the command argument is invalid exception
	 */
	void performCommand(Command command) throws CommandIsInvalidException,
		CommandArgumentIsInvalidException;
}
