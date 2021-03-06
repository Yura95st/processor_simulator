package processor_simulator.Utils;

import org.junit.Test;

public class GuardTests
{
	@Test
	public void moreThanZero_IntegerNumberisMoreThanZero_DoesNotThrowAnyException()
	{
		int number = 1;

		Guard.moreThanZero(number, "number");
	}

	@Test(expected = IllegalArgumentException.class)
	public void moreThanZero_NumberIsEqualToZero_ThrowsIllegalArgumentException()
	{
		int number = 0;
		Guard.moreThanZero(number, "number");
	}

	@Test(expected = IllegalArgumentException.class)
	public void moreThanZero_NumberIsLessThanZero_ThrowsIllegalArgumentException()
	{
		int number = -1;
		Guard.moreThanZero(number, "number");
	}

	@Test
	public void notNull_ObjectIsNotNull_DoesNotThrowAnyException()
	{
		Object tempObject = new Object();

		Guard.notNull(tempObject, "tempObject");
	}

	@Test(expected = IllegalArgumentException.class)
	public void notNull_ObjectIsNull_ThrowsIllegalArgumentException()
	{
		Object tempObject = null;

		Guard.notNull(tempObject, "tempObject");
	}

	@Test(expected = IllegalArgumentException.class)
	public void notNullOrEmpty_StringIsEmpty_DoesNotThrowAnyException()
	{
		String tempString = "";

		Guard.notNullOrEmpty(tempString, "tempString");
	}

	@Test(expected = IllegalArgumentException.class)
	public void notNullOrEmpty_StringIsNull_ThrowsIllegalArgumentException()
	{
		String tempString = null;

		Guard.notNullOrEmpty(tempString, "tempString");
	}

	@Test
	public void notNullOrEmpty_StringIsValid_DoesNotThrowAnyException()
	{
		String tempString = "tempString";

		Guard.notNullOrEmpty(tempString, "tempString");
	}
}
