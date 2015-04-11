package processor_simulator.Models;

import processor_simulator.Enums.ArgumentType;
import processor_simulator.Utils.Guard;

public class Argument
{
	private final ArgumentType _type;

	private final String _value;

	public Argument(ArgumentType type, String value)
	{
		Guard.notNull(type, "type");
		Guard.notNullOrEmpty(value, "value");

		this._type = type;
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
		Argument other = (Argument) obj;
		if (this._type != other._type)
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

	public ArgumentType getType()
	{
		return this._type;
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
			prime * result + ((this._type == null) ? 0 : this._type.hashCode());
		result =
			prime * result
				+ ((this._value == null) ? 0 : this._value.hashCode());
		return result;
	}
}
