package processor_simulator.Utils;

import org.junit.Assert;
import org.junit.Test;

public class NumberUtilsTests
{
	@Test(expected = IllegalArgumentException.class)
	public void castValue_NumberOfBitsIsEqualToZero_ThrowsIllegalArgumentException()
	{
		// Arrange
		int value = 1;
		int numberOfBits = 0;

		// Act & Assert
		NumberUtils.castValue(value, numberOfBits);
	}

	@Test(expected = IllegalArgumentException.class)
	public void castValue_NumberOfBitsIsLessThanZero_ThrowsIllegalArgumentException()
	{
		// Arrange
		int value = 1;
		int numberOfBits = -1;

		// Act & Assert
		NumberUtils.castValue(value, numberOfBits);
	}

	@Test
	public void castValue_ValueDoesNotFitInTheNumberOfBits_ReturnsValidValue()
	{
		// Arrange
		int value = 2;
		int numberOfBits = 2;

		int testCastedValue = -2;

		// Act
		int castedValue = NumberUtils.castValue(value, numberOfBits);

		// Assert
		Assert.assertEquals(testCastedValue, castedValue);
	}

	@Test
	public void castValue_ValueFitsInTheNumberOfBits_ReturnsSameValue()
	{
		// Arrange
		int value = 1;
		int numberOfBits = 2;

		// Act
		int castedValue = NumberUtils.castValue(value, numberOfBits);

		// Assert
		Assert.assertEquals(value, castedValue);
	}

	@Test
	public void castValue_ValueOverflowsTwice_ReturnsValidValue()
	{
		// Arrange
		int value = 6;
		int numberOfBits = 2;

		int testCastedValue = -2;

		// Act
		int castedValue = NumberUtils.castValue(value, numberOfBits);

		// Assert
		Assert.assertEquals(testCastedValue, castedValue);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toBinaryString_NumberOfBitsIsEqualToZero_ThrowsIllegalArgumentException()
	{
		// Arrange
		int value = 1;
		int numberOfBits = 0;

		// Act & Assert
		NumberUtils.toBinaryString(value, numberOfBits);
	}

	@Test
	public void toBinaryString_NumberOfBitsIsLessOrEqualToFour_DoesNotAddSpacesToTheBinaryString()
	{
		// Arrange
		int value = 1;
		int numberOfBits = 4;

		String testBinaryString = "0001";

		// Act
		String binaryString = NumberUtils.toBinaryString(value, numberOfBits);

		// Assert
		Assert.assertEquals(testBinaryString, binaryString);
	}

	@Test(expected = IllegalArgumentException.class)
	public void toBinaryString_NumberOfBitsIsLessThanZero_ThrowsIllegalArgumentException()
	{
		// Arrange
		int value = 1;
		int numberOfBits = -1;

		// Act & Assert
		NumberUtils.toBinaryString(value, numberOfBits);
	}

	@Test
	public void toBinaryString_ValueDoesNotFitInTheNumberOfBits_CastsTheValueAndReturnsValidBinaryString()
	{
		// Arrange
		int value = 16;
		int numberOfBits = 5;

		String testBinaryString = "1 0000";

		// Act
		String binaryString = NumberUtils.toBinaryString(value, numberOfBits);

		// Assert
		Assert.assertEquals(testBinaryString, binaryString);
	}

	@Test
	public void toBinaryString_ValueFitsInTheNumberOfBits_ReturnsValidBinaryString()
	{
		// Arrange
		int value = 1;
		int numberOfBits = 5;

		String testBinaryString = "0 0001";

		// Act
		String binaryString = NumberUtils.toBinaryString(value, numberOfBits);

		// Assert
		Assert.assertEquals(testBinaryString, binaryString);
	}
}
