package processor_simulator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import processor_simulator.Infrastructure.Abstact.IObservable;
import processor_simulator.Infrastructure.Abstact.ITactsListener;
import processor_simulator.Lexers.Abstract.ICommandsLexer;
import processor_simulator.Lexers.Concrete.CommandsLexer;
import processor_simulator.Models.Command;
import processor_simulator.Models.Token;
import processor_simulator.Parsers.Abstract.ICommandsParser;
import processor_simulator.Parsers.Concrete.CommandsParser;
import processor_simulator.Simulators.Abstract.IProcessorSimulator;
import processor_simulator.Simulators.Concrete.ProcessorSimulator;
import processor_simulator.Utils.NumberUtils;

public class Main
{
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.out
					.println("Please, specify the path to the commands file.");

			return;
		}

		try
		{
			// Read all lines from commands file.
			String source = Main.readAllLinesFromFile(args[0]);

			ICommandsLexer commandsLexer = new CommandsLexer();

			commandsLexer.setSource(source);

			// Parse tokens from the source
			List<Token> tokens = commandsLexer.parse();

			ICommandsParser commandsParser = new CommandsParser();

			commandsParser.setTokens(tokens);

			// Parse commands from the tokens
			List<Command> commands = commandsParser.parse();

			// Create processorSimulator
			IProcessorSimulator processorSimulator =
				new ProcessorSimulator(14, 4);

			// Add tactsListener
			((IObservable<ITactsListener>) processorSimulator)
					.addListener(new ITactsListener() {

						@Override
						public void tactPerformed()
						{
							Main.printProcessorSimulatorInfo(processorSimulator);
						}
					});

			for (Command command : commands)
			{
				processorSimulator.performCommand(command);
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void printProcessorSimulatorInfo(
		IProcessorSimulator processorSimulator)
	{
		StringBuilder stringBuilder = new StringBuilder();

		int[] registers = processorSimulator.getRegisters();

		stringBuilder.append(String.format("CurrentCommandText = %1$s",
			processorSimulator.getCurrentCommandText()));
		stringBuilder.append(String.format("%1$s%1$s", System.lineSeparator()));

		for (int i = 0; i < registers.length; i++)
		{
			stringBuilder.append(String.format("R%1$d = %2$s (%3$d)", i + 1,
				NumberUtils.toBinaryString(registers[i], processorSimulator.getNumberOfBits()), registers[i]));
			stringBuilder.append(System.lineSeparator());
		}

		stringBuilder.append(System.lineSeparator());

		stringBuilder.append(String.format("CommandsCounter = %1$s",
			processorSimulator.getCommandsCounter()));
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append(String.format("TactsCounter = %1$s",
			processorSimulator.getTactsCounter()));
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append(String.format("OverflowFlag = %1$s",
			processorSimulator.getOverflowFlag()));
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append(String.format("SignFlag = %1$s",
			processorSimulator.getSignFlag()));
		stringBuilder.append(System.lineSeparator());

		stringBuilder.append(String.format("%1$s-------------------------%1$s",
			System.lineSeparator()));

		System.out.println(stringBuilder.toString());
	}

	private static String readAllLinesFromFile(String filePath)
		throws IOException
	{
		Path path = Paths.get(filePath);

		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

		StringBuilder stringBuilder = new StringBuilder();

		for (String line : lines)
		{
			stringBuilder.append(line);
			stringBuilder.append(System.getProperty("line.separator"));
		}

		String source = stringBuilder.toString().trim();

		return source;
	}
}