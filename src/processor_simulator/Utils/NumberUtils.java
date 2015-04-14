package processor_simulator.Utils;

public class NumberUtils
{
	public static int castValue(int value, int numberOfBits)
	{
		Guard.moreThanZero(numberOfBits, "numberOfBits");

		// Cut extra bits
		value &= (1 << numberOfBits) - 1;

		// Convert negative number to integer
		if (((1 << (numberOfBits - 1)) & value) != 0)
		{
			value |=
				((1 << (Integer.BYTES * 8 - numberOfBits)) - 1) << numberOfBits;
		}

		return value;
	}

	public static String toBinaryString(int value, int numberOfBits)
	{
		Guard.moreThanZero(numberOfBits, "numberOfBits");

		int castedValue = NumberUtils.castValue(value, numberOfBits);

		StringBuilder stringBuilder = new StringBuilder();

		String stringValue = Integer.toBinaryString(castedValue);

		int length = stringValue.length();

		if (length < numberOfBits)
		{
			for (int i = 0, count = numberOfBits - length; i < count; i++)
			{
				stringBuilder.append("0");
			}

			stringBuilder.append(stringValue);
		}
		else
		{
			stringBuilder.append(stringValue.substring(length - numberOfBits, length));
		}

		int spaceRatio = 4;

		// Add spaces
		for (int i = 1, count = (numberOfBits - 1) / spaceRatio; i <= count; i++)
		{
			stringBuilder.insert(numberOfBits - i * spaceRatio, " ");
		}

		String result = stringBuilder.toString();

		return result;
	}
}
