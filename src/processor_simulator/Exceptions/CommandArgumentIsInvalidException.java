package processor_simulator.Exceptions;

public class CommandArgumentIsInvalidException extends Exception
{
	public CommandArgumentIsInvalidException()
	{

	}

	public CommandArgumentIsInvalidException(String message)
	{
		super(message);
	}

	public CommandArgumentIsInvalidException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public CommandArgumentIsInvalidException(Throwable cause)
	{
		super(cause);
	}
}
