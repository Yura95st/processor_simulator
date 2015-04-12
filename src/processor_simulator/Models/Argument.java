package processor_simulator.Models;

import processor_simulator.Enums.ArgumentType;
import processor_simulator.Utils.Guard;

public class Argument
{
	private final ArgumentType _type;

	private final int _value;

	public Argument(ArgumentType type, int value)
	{
		Guard.notNull(type, "type");

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
		if (this._value != other._value)
		{
			return false;
		}
		return true;
	}

	public ArgumentType getType()
	{
		return this._type;
	}

	public int getValue()
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
		result = prime * result + this._value;
		return result;
	}
}
