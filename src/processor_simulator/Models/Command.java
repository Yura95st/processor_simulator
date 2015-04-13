package processor_simulator.Models;

import java.util.ArrayList;
import java.util.List;

import processor_simulator.Enums.CommandType;
import processor_simulator.Utils.Guard;

public class Command
{
	private List<Argument> _arguments;

	private final CommandType _type;

	public Command(CommandType type)
	{
		Guard.notNull(type, "type");

		this._type = type;

		this._arguments = new ArrayList<Argument>();
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
		Command other = (Command) obj;
		if (this._arguments == null)
		{
			if (other._arguments != null)
			{
				return false;
			}
		}
		else if (!this._arguments.equals(other._arguments))
		{
			return false;
		}
		if (this._type != other._type)
		{
			return false;
		}
		return true;
	}

	public List<Argument> getArguments()
	{
		return this._arguments;
	}

	public CommandType getType()
	{
		return this._type;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result =
			prime * result
				+ ((this._arguments == null) ? 0 : this._arguments.hashCode());
		result =
			prime * result + ((this._type == null) ? 0 : this._type.hashCode());
		return result;
	}

	public void setArguments(List<Argument> arguments)
	{
		Guard.notNull(arguments, "arguments");

		this._arguments = new ArrayList<Argument>(arguments);
	}

	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(String.format("%1$s ", this._type));

		boolean isFirstArgument = true;

		for (Argument argument : this._arguments)
		{
			if (!isFirstArgument)
			{
				stringBuilder.append(", ");
			}
			else
			{
				isFirstArgument = false;
			}

			stringBuilder.append(String.format("{%1$s; %2$s}",
				argument.getType(), argument.getValue()));
		}

		String value = stringBuilder.toString();

		return value;
	}
}
