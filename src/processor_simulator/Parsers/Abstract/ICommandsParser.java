package processor_simulator.Parsers.Abstract;

import java.util.List;
import java.util.Map;

import processor_simulator.Enums.CommandType;
import processor_simulator.Enums.SpecialTokenKind;
import processor_simulator.Exceptions.CommandIsInvalidException;
import processor_simulator.Exceptions.SpecialTokenIsNotDefinedException;
import processor_simulator.Models.Command;
import processor_simulator.Models.Token;

public interface ICommandsParser
{
	/**
	 * Gets the command types dictionary.
	 *
	 * @return the command types dictionary
	 */
	Map<String, CommandType> getCommandTypesDictionary();

	/**
	 * Gets the special tokens dictionary.
	 *
	 * @return the special tokens dictionary
	 */
	Map<SpecialTokenKind, Token> getSpecialTokensDictionary();

	/**
	 * Gets the tokens.
	 *
	 * @return the tokens
	 */
	List<Token> getTokens();

	/**
	 * Parses the tokens.
	 *
	 * @return the list of commands parsed from the tokens
	 * @throws CommandIsInvalidException
	 *             the command is invalid exception
	 */
	List<Command> parse() throws CommandIsInvalidException;

	void setCommandTypesDictionary(
		Map<String, CommandType> commandTypesDictionary);

	/**
	 * Sets the special tokens dictionary.
	 *
	 * @param specialTokensDictionary
	 *            the special tokens dictionary
	 * @throws SpecialTokenIsNotDefinedException
	 *             the special token is not defined exception
	 */
	void setSpecialTokensDictionary(
		Map<SpecialTokenKind, Token> specialTokensDictionary)
		throws SpecialTokenIsNotDefinedException;

	/**
	 * Sets the tokens.
	 *
	 * @param tokens
	 *            the new tokens
	 */
	void setTokens(List<Token> tokens);
}
