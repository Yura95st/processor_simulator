package processor_simulator.Parsers.Concrete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import processor_simulator.Enums.ArgumentType;
import processor_simulator.Enums.CommandType;
import processor_simulator.Enums.SpecialTokenKind;
import processor_simulator.Enums.TokenKind;
import processor_simulator.Exceptions.CommandIsInvalidException;
import processor_simulator.Exceptions.SpecialTokenIsNotDefinedException;
import processor_simulator.Models.Argument;
import processor_simulator.Models.Command;
import processor_simulator.Models.Token;
import processor_simulator.Parsers.Abstract.ICommandsParser;

public class CommandsParserTests
{
	private ICommandsParser _commandsParser;

	private Map<SpecialTokenKind, Token> _specialTokensDictionary;

	@Test(expected = CommandIsInvalidException.class)
	public void parse_CommandDoesNotStartFromCommandToken_ThrowsCommandIsInvalidException()
		throws Exception
	{
		// Arrange - create tokens
		Token tokenA = new Token(TokenKind.Register, "R1");
		Token tokenB = new Token(TokenKind.Number, "1234");

		Token[] tokens =
			{
				tokenA, tokenB,
				this._specialTokensDictionary.get(SpecialTokenKind.Termination)
			}; // R1 1234 ;

		this._commandsParser.setTokens(Arrays.asList(tokens));

		// Act & Assert
		this._commandsParser.parse();
	}

	@Test
	public void parse_CommandNameIsUnknown_ReturnsValidCommandsList()
		throws Exception
	{
		// Arrange - create tokens
		Token tokenA = new Token(TokenKind.Command, "CommandA");
		Token tokenB = new Token(TokenKind.Register, "R1");

		Token[] tokens =
			{
				tokenA, tokenB,
				this._specialTokensDictionary.get(SpecialTokenKind.Termination)
			}; // CommandA R1 ;

		this._commandsParser.setTokens(Arrays.asList(tokens));

		// Arrange - create arguments
		Argument argumentA =
			new Argument(ArgumentType.Register, tokenB.getValue());

		// Arrange - create testCommands
		Command commandOne = new Command(CommandType.Unknown);

		commandOne.setArguments(Arrays.asList(argumentA));

		List<Command> testCommands = Arrays.asList(commandOne);

		// Arrange - set commandTypesDictionary
		this._commandsParser
				.setCommandTypesDictionary(new HashMap<String, CommandType>());

		// Act
		List<Command> commands = this._commandsParser.parse();

		// Assert
		Assert.assertEquals(testCommands, commands);
	}

	@Test(expected = CommandIsInvalidException.class)
	public void parse_CommandWithInvalidConcatenationSpecialToken_ThrowsCommandIsInvalidException()
		throws Exception
	{
		// Arrange - create tokens
		Token tokenA = new Token(TokenKind.Command, "CommandA");
		Token tokenB = new Token(TokenKind.Register, "R1");

		Token[] tokens =
			{
				tokenA,
				tokenB,
				this._specialTokensDictionary
						.get(SpecialTokenKind.Concatenation),
				this._specialTokensDictionary.get(SpecialTokenKind.Termination)

			}; // CommandA R1 , ;

		this._commandsParser.setTokens(Arrays.asList(tokens));

		// Act & Assert
		this._commandsParser.parse();
	}

	@Test(expected = CommandIsInvalidException.class)
	public void parse_CommandWithMissingArgumentRule_ThrowsCommandIsInvalidException()
		throws Exception
	{
		// Arrange - create tokens
		Token tokenA = new Token(TokenKind.Command, "CommandA");

		Token[] tokens =
			{
				tokenA,
				this._specialTokensDictionary.get(SpecialTokenKind.Termination),
			}; // CommandA ;

		this._commandsParser.setTokens(Arrays.asList(tokens));

		// Act & Assert
		this._commandsParser.parse();
	}

	@Test(expected = CommandIsInvalidException.class)
	public void parse_CommandWithMissingConcatenationSpecialToken_ThrowsCommandIsInvalidException()
		throws Exception
	{
		// Arrange - create tokens
		Token tokenA = new Token(TokenKind.Command, "CommandA");
		Token tokenB = new Token(TokenKind.Register, "R1");
		Token tokenC = new Token(TokenKind.Number, "1234");

		Token[] tokens =
			{
				tokenA, tokenB, tokenC,
				this._specialTokensDictionary.get(SpecialTokenKind.Termination)

			}; // CommandA = R1 1234 ;

		this._commandsParser.setTokens(Arrays.asList(tokens));

		// Act & Assert
		this._commandsParser.parse();
	}

	@Test(expected = CommandIsInvalidException.class)
	public void parse_CommandWithMissingTerminationSpecialToken_ThrowsCommandIsInvalidException()
		throws Exception
	{
		// Arrange - create tokens
		Token tokenA = new Token(TokenKind.Command, "CommandA");
		Token tokenB = new Token(TokenKind.Register, "R1");

		Token[] tokens = {
			tokenA, tokenB
		}; // CommandA R1

		this._commandsParser.setTokens(Arrays.asList(tokens));

		// Act & Assert
		this._commandsParser.parse();
	}

	@Test
	public void parse_MethodIsIdempotent() throws Exception
	{
		// Arrange
		Token[] tokens =
			{
				new Token(TokenKind.Command, "CommandA"),
				new Token(TokenKind.Register, "R1"),
				this._specialTokensDictionary
						.get(SpecialTokenKind.Concatenation),
				new Token(TokenKind.Number, "1234"),
				this._specialTokensDictionary.get(SpecialTokenKind.Termination),
			}; // CommandA R1, 1234 ;

		this._commandsParser.setTokens(Arrays.asList(tokens));

		// Act
		List<Command> commandsOne = this._commandsParser.parse();
		List<Command> commandsTwo = this._commandsParser.parse();

		// Assert
		Assert.assertEquals(commandsOne, commandsTwo);
	}

	@Test
	public void parse_ReturnsValidCommandsList() throws Exception
	{
		// Arrange - create tokens
		Token tokenA = new Token(TokenKind.Command, "CommandA");
		Token tokenB = new Token(TokenKind.Command, "CommandB");
		Token tokenC = new Token(TokenKind.Register, "R1");
		Token tokenD = new Token(TokenKind.Register, "R2");
		Token tokenE = new Token(TokenKind.Number, "1234");

		Token[] tokens =
			{
				tokenA,
				tokenC,
				this._specialTokensDictionary
						.get(SpecialTokenKind.Concatenation),
				tokenD,
				this._specialTokensDictionary
						.get(SpecialTokenKind.Concatenation),
				tokenE,
				this._specialTokensDictionary.get(SpecialTokenKind.Termination),
				tokenB, tokenC,
				this._specialTokensDictionary.get(SpecialTokenKind.Termination)
			}; // CommandA R1, R2, 1234 ; CommandB R1 ;

		this._commandsParser.setTokens(Arrays.asList(tokens));

		// Arrange - create arguments
		Argument argumentA =
			new Argument(ArgumentType.Register, tokenC.getValue());
		Argument argumentB =
			new Argument(ArgumentType.Register, tokenD.getValue());
		Argument argumentC =
			new Argument(ArgumentType.Number, tokenE.getValue());

		// Arrange - create testCommands
		Command commandOne = new Command(CommandType.Load);

		commandOne.setArguments(Arrays.asList(argumentA, argumentB, argumentC));

		Command commandTwo = new Command(CommandType.Move);

		commandTwo.setArguments(Arrays.asList(argumentA));

		List<Command> testCommands = Arrays.asList(commandOne, commandTwo);

		// Arrange - set commandTypesDictionary
		Map<String, CommandType> commandTypesDictionary =
			new HashMap<String, CommandType>() {
				{
					this.put(tokenA.getValue().toLowerCase(),
						commandOne.getType());
					this.put(tokenB.getValue().toLowerCase(),
						commandTwo.getType());
				}
			};

		this._commandsParser.setCommandTypesDictionary(commandTypesDictionary);

		// Act
		List<Command> commands = this._commandsParser.parse();

		// Assert
		Assert.assertEquals(testCommands, commands);
	}

	@Test
	public void parse_TokensListIsEmpty_ReturnsEmptyList() throws Exception
	{
		// Arrange
		this._commandsParser.setTokens(new ArrayList<Token>());

		// Act
		List<Command> commands = this._commandsParser.parse();

		// Assert
		Assert.assertEquals(0, commands.size());
	}

	@Test(expected = SpecialTokenIsNotDefinedException.class)
	public void setSpecialTokensDictionary_NotAllSpecialTokensAreDefined_ThrowsSpecialTokenIsNotDefinedException()
		throws Exception
	{
		// Arrange
		Map<SpecialTokenKind, Token> specialTokensDictionary =
			new HashMap<SpecialTokenKind, Token>() {
				{
					this.put(SpecialTokenKind.Concatenation, new Token(
						TokenKind.Delimiter, "token"));

					this.put(SpecialTokenKind.Termination, null);
				}
			};

		// Act & Assert
		this._commandsParser
				.setSpecialTokensDictionary(specialTokensDictionary);
	}

	@Before
	public void setUp() throws Exception
	{
		this._commandsParser = new CommandsParser();

		this._specialTokensDictionary =
			this._commandsParser.getSpecialTokensDictionary();
	}

}
