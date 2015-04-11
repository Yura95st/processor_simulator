package processor_simulator.Models;

import processor_simulator.Enums.TokenKind;
import processor_simulator.Utils.Guard;

public class Token
{
	private final TokenKind _kind;

	private final String _text;

	public Token(TokenKind nodeKind, String text)
	{
		Guard.notNull(nodeKind, "nodeKind");
		Guard.notNull(text, "text");

		this._kind = nodeKind;
		this._text = text;
	}

	public TokenKind getKind()
	{
		return this._kind;
	}

	public String getText()
	{
		return this._text;
	}
}
