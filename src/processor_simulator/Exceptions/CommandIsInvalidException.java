package processor_simulator.Exceptions;

public class CommandIsInvalidException extends Exception
{
	public CommandIsInvalidException()
	{

	}

	public CommandIsInvalidException(String message)
	{
		super(message);
	}

	public CommandIsInvalidException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public CommandIsInvalidException(Throwable cause)
	{
		super(cause);
	}
}
