package processor_simulator.Simulators.Concrete;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import processor_simulator.Enums.ArgumentType;
import processor_simulator.Enums.CommandType;
import processor_simulator.Exceptions.CommandArgumentIsInvalidException;
import processor_simulator.Exceptions.CommandIsInvalidException;
import processor_simulator.Infrastructure.Abstact.IObservable;
import processor_simulator.Infrastructure.Abstact.ITactsListener;
import processor_simulator.Models.Argument;
import processor_simulator.Models.Command;
import processor_simulator.Simulators.Abstract.IProcessorSimulator;

public class ProcessorSimulatorTests
{
	private IProcessorSimulator _processorSimulator;

	private ITactsListener _tactsListenerMock;

	@Test
	public void performCommand_AddCommandWithNegativeResult_PerformsCommandAndSetsSignFlagAsTrue()
		throws Exception
	{
		// Arrange - create command
		int minValue =
			(int) -Math.pow(2, this._processorSimulator.getNumberOfBits() - 1);

		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, minValue);

		Command command = new Command(CommandType.Add);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] += argumentTwo.getValue();

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(true, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_AddCommandWithOverflowResult_PerformsCommandAndSetsOverflowFlagAsTrue()
		throws Exception
	{
		// Arrange - create command
		int maxValue =
			(int) (Math.pow(2, this._processorSimulator.getNumberOfBits() - 1) - 1);
		int minValue =
			(int) -Math.pow(2, this._processorSimulator.getNumberOfBits() - 1);

		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, maxValue);

		Command command = new Command(CommandType.Add);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] =
			minValue - 1 + testRegisters[argumentOne.getValue() - 1];

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(true, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(true, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_AddCommandWithZeroNumberArgument_PerformsCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, 0);

		Command command = new Command(CommandType.Add);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test(expected = IllegalArgumentException.class)
	public void performCommand_CommandIsNull_ThrowsIllegalArgumentException()
		throws Exception
	{
		this._processorSimulator.performCommand(null);
	}

	@Test
	public void performCommand_CommandIsUnknown_ThrowsCommandIsInvalidException()
		throws Exception
	{
		// Arrange
		Command command = new Command(CommandType.Unknown);

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		// Arrange - create testTactsCounter
		int testTactsCounter = 1;

		boolean exceptionIsThrown = false;

		// Act
		try
		{
			this._processorSimulator.performCommand(command);
		}
		catch (CommandIsInvalidException e)
		{
			exceptionIsThrown = true;
		}

		// Assert
		Assert.assertEquals(true, exceptionIsThrown);

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LeftMoveCommandWithMaxBitsNumberArgument_PerformsCommandAndSetsOverflowFlagAsTrue()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo =
			new Argument(ArgumentType.Number,
				this._processorSimulator.getNumberOfBits());

		Command command = new Command(CommandType.LeftMove);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] = 0;

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(true, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LeftMoveCommandWithNegativeNumberArgument_PerformsRightMoveCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, -1);

		Command command = new Command(CommandType.LeftMove);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] /= 2;

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LeftMoveCommandWithNegativeOddValueInRegisterAndNegativeNumberArgument_PerformsRightMoveCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 2);
		Argument argumentTwo = new Argument(ArgumentType.Number, -1);

		Command command = new Command(CommandType.LeftMove);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] =
			testRegisters[argumentOne.getValue() - 1] / 2 - 1;

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(true, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LeftMoveCommandWithPositiveNumberArgument_PerformsCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, 1);

		Command command = new Command(CommandType.LeftMove);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] *= 2;

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LeftMoveCommandWithZeroNumberArgument_PerformsCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, 0);

		Command command = new Command(CommandType.LeftMove);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LoadCommandWithFirstArgumentIsNotRegisterArgument_ThrowsCommandArgumentIsInvalidException()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Number, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, 2);

		Command command = new Command(CommandType.Load);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		// Arrange - create testTactsCounter
		int testTactsCounter = 1;

		boolean exceptionIsThrown = false;

		// Act
		try
		{
			this._processorSimulator.performCommand(command);
		}
		catch (CommandArgumentIsInvalidException e)
		{
			exceptionIsThrown = true;
		}

		// Assert
		Assert.assertEquals(true, exceptionIsThrown);

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LoadCommandWithMissingSecondArgument_ThrowsCommandIsInvalidException()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);

		Command command = new Command(CommandType.Load);

		command.setArguments(Arrays.asList(argumentOne));

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		// Arrange - create testTactsCounter
		int testTactsCounter = 1;

		boolean exceptionIsThrown = false;

		// Act
		try
		{
			this._processorSimulator.performCommand(command);
		}
		catch (CommandIsInvalidException e)
		{
			exceptionIsThrown = true;
		}

		// Assert
		Assert.assertEquals(true, exceptionIsThrown);

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LoadCommandWithNegativeNumberArgument_PerformsCommandAndSetsSignFlagAsTrue()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, -1);

		Command command = new Command(CommandType.Load);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] = argumentTwo.getValue();

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(true, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LoadCommandWithNonexistentFirstRegisterArgument_ThrowsCommandArgumentIsInvalidException()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne =
			new Argument(ArgumentType.Register,
				this._processorSimulator.getRegisters().length + 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, 1);

		Command command = new Command(CommandType.Load);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		// Arrange - create testTactsCounter
		int testTactsCounter = 1;

		boolean exceptionIsThrown = false;

		// Act
		try
		{
			this._processorSimulator.performCommand(command);
		}
		catch (CommandArgumentIsInvalidException e)
		{
			exceptionIsThrown = true;
		}

		// Assert
		Assert.assertEquals(true, exceptionIsThrown);

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LoadCommandWithNonexistentSecondRegisterArgument_ThrowsCommandArgumentIsInvalidException()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);

		Argument argumentTwo =
			new Argument(ArgumentType.Register,
				this._processorSimulator.getRegisters().length + 1);

		Command command = new Command(CommandType.Load);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		// Arrange - create testTactsCounter
		int testTactsCounter = 1;

		boolean exceptionIsThrown = false;

		// Act
		try
		{
			this._processorSimulator.performCommand(command);
		}
		catch (CommandArgumentIsInvalidException e)
		{
			exceptionIsThrown = true;
		}

		// Assert
		Assert.assertEquals(true, exceptionIsThrown);

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LoadCommandWithOverflowNumberArgument_PerformsCommandAndSetsSignFlagAndOverflowFlagAsTrue()
		throws Exception
	{
		// Arrange - create command
		int maxValue =
			(int) (Math.pow(2, this._processorSimulator.getNumberOfBits() - 1) - 1);
		int minValue =
			(int) -Math.pow(2, this._processorSimulator.getNumberOfBits() - 1);

		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, maxValue + 1);

		Command command = new Command(CommandType.Load);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] = minValue;

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(true, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(true, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LoadCommandWithPositiveNumberArgument_PerformsCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, 1);

		Command command = new Command(CommandType.Load);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] = argumentTwo.getValue();

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LoadCommandWithTwoRegisterArguments_PerformsCommand()
		throws Exception
	{
		// Arrange - create commands
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Register, 2);

		Command command = new Command(CommandType.Load);

		command.setArguments(Arrays.asList(argumentTwo, argumentOne));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentTwo.getValue() - 1] =
			testRegisters[argumentOne.getValue() - 1];

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_LoadCommandWithZeroNumberArgument_PerformsCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, 0);

		Command command = new Command(CommandType.Load);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] = argumentTwo.getValue();

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_RightMoveCommandWithMaxBitsNumberArgument_PerformsCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo =
			new Argument(ArgumentType.Number,
				this._processorSimulator.getNumberOfBits());

		Command command = new Command(CommandType.RightMove);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] = 0;

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_RightMoveCommandWithNegativeNumberArgument_PerformsLeftMoveCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, -1);

		Command command = new Command(CommandType.RightMove);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] *= 2;

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_RightMoveCommandWithNegativeRegisterValueAndMaxBitsNumberArgument_PerformsCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 2);
		Argument argumentTwo =
			new Argument(ArgumentType.Number,
				this._processorSimulator.getNumberOfBits());

		Command command = new Command(CommandType.RightMove);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] = -1;

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(true, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_RightMoveCommandWithPositiveNumberArgument_PerformsCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, 1);

		Command command = new Command(CommandType.RightMove);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		testRegisters[argumentOne.getValue() - 1] /= 2;

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Test
	public void performCommand_RightMoveCommandWithZeroNumberArgument_PerformsCommand()
		throws Exception
	{
		// Arrange - create command
		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, 0);

		Command command = new Command(CommandType.RightMove);

		command.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Arrange - create testRegisters
		int[] testRegisters = this._processorSimulator.getRegisters();

		// Arrange - create testCommandsCounter
		int testCommandsCounter = this._processorSimulator.getCommandsCounter();

		// Arrange - create testTactsCounter
		int testTactsCounter = 2;

		// Act
		this._processorSimulator.performCommand(command);

		// Assert
		Assert.assertArrayEquals(testRegisters,
			this._processorSimulator.getRegisters());

		Assert.assertEquals(testCommandsCounter + 1,
			this._processorSimulator.getCommandsCounter());

		Assert.assertEquals(false, this._processorSimulator.getOverflowFlag());
		Assert.assertEquals(false, this._processorSimulator.getSignFlag());
		Assert.assertEquals(testTactsCounter,
			this._processorSimulator.getTactsCounter());
		Assert.assertEquals(command.toString(),
			this._processorSimulator.getCurrentCommandText());

		Mockito.verify(this._tactsListenerMock, Mockito.times(testTactsCounter))
				.tactPerformed();
	}

	@Before
	public void setUp() throws Exception
	{
		this._processorSimulator = new ProcessorSimulator(4, 4);

		Argument argumentOne = new Argument(ArgumentType.Register, 1);
		Argument argumentTwo = new Argument(ArgumentType.Number, 3);
		Argument argumentThree = new Argument(ArgumentType.Register, 2);
		Argument argumentFour = new Argument(ArgumentType.Number, -3);

		Command commandOne = new Command(CommandType.Load);

		commandOne.setArguments(Arrays.asList(argumentOne, argumentTwo));

		// Load R2, 3;
		Command commandTwo = new Command(CommandType.Load);

		// Load R1, -3;
		commandTwo.setArguments(Arrays.asList(argumentThree, argumentFour));

		this._processorSimulator.performCommand(commandOne);
		this._processorSimulator.performCommand(commandTwo);

		this.mockTactsListener();
	}

	private void mockTactsListener()
	{
		this._tactsListenerMock = Mockito.mock(ITactsListener.class);

		((IObservable<ITactsListener>) this._processorSimulator)
				.addListener(this._tactsListenerMock);
	}
}
