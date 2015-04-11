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

	public List<Argument> getArguments()
	{
		return this._arguments;
	}

	public CommandType getType()
	{
		return this._type;
	}

	public void setArguments(List<Argument> arguments)
	{
		Guard.notNull(arguments, "arguments");

		this._arguments = new ArrayList<Argument>(arguments);
	}
}
