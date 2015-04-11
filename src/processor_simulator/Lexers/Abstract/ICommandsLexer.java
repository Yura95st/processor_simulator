package processor_simulator.Lexers.Abstract;

import processor_simulator.Models.Token;
import processor_simulator.Models.TokenDefinition;

public interface ICommandsLexer extends ILexer<Token>
{
	/**
	 * Gets the token definitions.
	 *
	 * @return the token definitions
	 */
	Iterable<TokenDefinition> getTokenDefinitions();

	/**
	 * Sets the token definitions.
	 *
	 * @param tokenDefinitions
	 *            the new token definitions
	 */
	void setTokenDefinitions(Iterable<TokenDefinition> tokenDefinitions);
}
