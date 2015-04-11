package processor_simulator.Models;

import processor_simulator.Enums.TokenKind;
import processor_simulator.Utils.Guard;

public class Token
{
	private final TokenKind _kind;

	private final String _value;

	public Token(TokenKind tokenKind, String value)
	{
		Guard.notNull(tokenKind, "tokenKind");
		Guard.notNull(value, "value");

		this._kind = tokenKind;
		this._value = value;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (this.getClass() != obj.getClass())
		{
			return false;
		}
		Token other = (Token) obj;
		if (this._kind != other._kind)
		{
			return false;
		}
		if (this._value == null)
		{
			if (other._value != null)
			{
				return false;
			}
		}
		else if (!this._value.equals(other._value))
		{
			return false;
		}
		return true;
	}

	public TokenKind getKind()
	{
		return this._kind;
	}

	public String getValue()
	{
		return this._value;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result =
				prime * result + ((this._kind == null) ? 0 : this._kind.hashCode());
		result =
				prime * result
				+ ((this._value == null) ? 0 : this._value.hashCode());
		return result;
	}
}
