package processor_simulator.Utils;

public class Guard
{
	/**
	 * Checks if the specified argument is not null.
	 *
	 * @param argument
	 *            the argument to test
	 * @param argumentName
	 *            the argument's name
	 */
	public static void notNull(Object argument, String argumentName)
	{
		if (argument == null)
		{
			throw new IllegalArgumentException(String.format(
				"Argument '%1$s' can't be null.", argumentName));
		}
	}

	/**
	 * Checks if the specified argument is not null or empty.
	 *
	 * @param argument
	 *            the argument to test
	 * @param argumentName
	 *            the argument's name
	 */
	public static void notNullOrEmpty(String argument, String argumentName)
	{
		Guard.notNull(argument, argumentName);

		if (argument.length() == 0)
		{
			throw new IllegalArgumentException(String.format(
				"Argument '%1$s' can't be empty.", argumentName));
		}
	}
}
