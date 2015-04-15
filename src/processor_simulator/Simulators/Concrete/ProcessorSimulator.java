package processor_simulator.Simulators.Concrete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import processor_simulator.Enums.ArgumentType;
import processor_simulator.Enums.CommandType;
import processor_simulator.Exceptions.CommandArgumentIsInvalidException;
import processor_simulator.Exceptions.CommandIsInvalidException;
import processor_simulator.Infrastructure.Abstact.IObservable;
import processor_simulator.Infrastructure.Abstact.ITactsListener;
import processor_simulator.Models.Argument;
import processor_simulator.Models.Command;
import processor_simulator.Simulators.Abstract.IProcessorSimulator;
import processor_simulator.Utils.Guard;
import processor_simulator.Utils.NumberUtils;

public class ProcessorSimulator implements IProcessorSimulator,
IObservable<ITactsListener>
{
	private int _commandsCounter;

	private String _currentCommandText;

	private final int _numberOfBits;

	private boolean _overflowFlag;

	private final int[] _registers;

	private boolean _signFlag;

	private int _tactsCounter;

	private final List<ITactsListener> _tactsListener;

	public ProcessorSimulator(int numberOfBits, int numberOfRegisters)
	{
		Guard.moreThanZero(numberOfBits, "numberOfBits");
		Guard.moreThanZero(numberOfRegisters, "numberOfRegisters");

		this._numberOfBits = numberOfBits;

		this._registers = new int[numberOfRegisters];

		this._tactsListener = new ArrayList<ITactsListener>();
	}

	@Override
	public void addListener(ITactsListener listener)
	{
		Guard.notNull(listener, "listener");

		this._tactsListener.add(listener);
	}

	@Override
	public int getCommandsCounter()
	{
		return this._commandsCounter;
	}

	@Override
	public String getCurrentCommandText()
	{
		return this._currentCommandText;
	}

	@Override
	public int getNumberOfBits()
	{
		return this._numberOfBits;
	}

	@Override
	public boolean getOverflowFlag()
	{
		return this._overflowFlag;
	}

	@Override
	public int[] getRegisters()
	{
		return Arrays.copyOf(this._registers, this._registers.length);
	}

	@Override
	public boolean getSignFlag()
	{
		return this._signFlag;
	}

	@Override
	public int getTactsCounter()
	{
		return this._tactsCounter;
	}

	@Override
	public void performCommand(Command command)
			throws CommandIsInvalidException, CommandArgumentIsInvalidException
	{
		Guard.notNull(command, "command");

		this.reset();

		this._commandsCounter++;

		this._currentCommandText = command.toString();

		// Tact before command performing
		this.performTact();

		CommandType commandType = command.getType();

		if (commandType == CommandType.Add || commandType == CommandType.Load
				|| commandType == CommandType.LeftMove
				|| commandType == CommandType.RightMove
				|| commandType == CommandType.Xor)
		{
			this.performBinaryCommand(command);
		}
		else
		{
			throw new CommandIsInvalidException(String.format(
				"Command type '%1$s' is unknown.", command.getType()));
		}

		// Tact after command performing
		this.performTact();
	}

	@Override
	public void removeListener(ITactsListener listener)
	{
		this._tactsListener.remove(listener);
	}

	private void checkRegisterNumber(int registerNumber)
			throws CommandArgumentIsInvalidException
	{
		if (!this.isRegisterNumberValid(registerNumber))
		{
			throw new CommandArgumentIsInvalidException(String.format(
				"Register with the number '%1$s' doesn't exist.",
				registerNumber));
		}
	}

	private int getValueFromArgument(Argument argument)
			throws CommandArgumentIsInvalidException
	{
		int value;

		if (argument.getType() == ArgumentType.Register)
		{
			value = this.getValueFromRegister(argument.getValue());
		}
		else
		{
			value = argument.getValue();
		}

		return value;
	}

	private int getValueFromRegister(int registerNumber)
			throws CommandArgumentIsInvalidException
	{
		this.checkRegisterNumber(registerNumber);

		int value = this._registers[registerNumber - 1];

		return value;
	}

	private boolean isRegisterNumberValid(int registerNumber)
	{
		boolean result = registerNumber <= this._registers.length;

		return result;
	}

	private int leftMove(int value, int moveArgument)
	{
		int result = value;

		if (moveArgument >= 0)
		{
			result = result << moveArgument;
		}
		else
		{
			result = this.rightMove(value, -moveArgument);
		}

		return result;
	}

	private void performBinaryCommand(Command binaryCommand)
			throws CommandArgumentIsInvalidException, CommandIsInvalidException
	{
		List<Argument> arguments = binaryCommand.getArguments();

		if (arguments.size() < 2)
		{
			throw new CommandIsInvalidException(
					"Command must have at least two arguments.");
		}

		Argument argumentOne = arguments.get(0);
		Argument argumentTwo = arguments.get(1);

		if (argumentOne.getType() != ArgumentType.Register)
		{
			throw new CommandArgumentIsInvalidException(
				String.format(
					"First argument with type '%1$s' is invalid. Expected register argument.",
					argumentOne.getType()));
		}

		int registerNumber = argumentOne.getValue();

		int value = 0;

		int castedArgument =
			NumberUtils.castValue(this.getValueFromArgument(argumentTwo),
				this._numberOfBits);

		switch (binaryCommand.getType())
		{
			case Add:
			{
				value =
						this.getValueFromRegister(registerNumber) + castedArgument;
				break;
			}

			case Load:
			{
				value = this.getValueFromArgument(argumentTwo);
				break;
			}

			case LeftMove:
			{
				value =
					this.leftMove(this.getValueFromRegister(registerNumber),
						castedArgument);

				break;
			}

			case RightMove:
			{
				value =
					this.rightMove(this.getValueFromRegister(registerNumber),
						castedArgument);

				break;
			}

			case Xor:
			{
				value =
						this.getValueFromRegister(registerNumber)
						^ NumberUtils.castValue(
							this.getValueFromArgument(argumentTwo),
							this._numberOfBits);
				break;
			}
		}

		this.putValueToTheRegister(registerNumber, value);
	}

	private void performTact()
	{
		this._tactsCounter++;

		for (ITactsListener tactListener : this._tactsListener)
		{
			tactListener.tactPerformed();
		}
	}

	private void putValueToTheRegister(int registerNumber, int value)
			throws CommandArgumentIsInvalidException
	{
		this.checkRegisterNumber(registerNumber);

		int castedValue = NumberUtils.castValue(value, this._numberOfBits);

		this._registers[registerNumber - 1] = castedValue;

		if (castedValue != value)
		{
			this._overflowFlag = true;
		}

		if (castedValue < 0)
		{
			this._signFlag = true;
		}
	}

	private void reset()
	{
		this._overflowFlag = false;
		this._signFlag = false;

		this._tactsCounter = 0;

		this._currentCommandText = "";
	}

	private int rightMove(int value, int moveArgument)
	{
		int result = value;

		if (moveArgument >= 0)
		{
			for (int i = 0; i < moveArgument; i++)
			{
				// Add "-1" if value is negative and odd
				if (result < 0 && result % 2 != 0)
				{
					result--;
				}

				result /= 2;
			}
		}
		else
		{
			result = this.leftMove(value, -moveArgument);
		}

		return result;
	}
}
