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

	public ArgumentType getType()
	{
		return this._type;
	}

	public String getValue()
	{
		return this._value;
	}
}
