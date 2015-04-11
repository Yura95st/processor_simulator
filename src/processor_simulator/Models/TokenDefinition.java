package processor_simulator.Models;

import java.util.regex.Pattern;

import processor_simulator.Enums.TokenKind;

public class TokenDefinition
{
	private final TokenKind _kind;

	private final Pattern _representation;

	public TokenDefinition(String representation, TokenKind kind)
	{
		this._representation = Pattern.compile(representation);
		this._kind = kind;
	}

	public TokenKind getKind()
	{
		return this._kind;
	}

	public Pattern getRepresentation()
	{
		return this._representation;
	}

}
