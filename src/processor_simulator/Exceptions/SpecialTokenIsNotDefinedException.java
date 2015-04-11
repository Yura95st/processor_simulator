package processor_simulator.Exceptions;

public class SpecialTokenIsNotDefinedException extends Exception
{
	public SpecialTokenIsNotDefinedException()
	{

	}

	public SpecialTokenIsNotDefinedException(String message)
	{
		super(message);
	}

	public SpecialTokenIsNotDefinedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SpecialTokenIsNotDefinedException(Throwable cause)
	{
		super(cause);
	}
}
