package processor_simulator.Lexers.Concrete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import processor_simulator.Enums.TokenKind;
import processor_simulator.Lexers.Abstract.ICommandsLexer;
import processor_simulator.Models.Token;

public class CommandsLexerTests
{
	private ICommandsLexer _commandsLexer;

	@Test
	public void parse_MethodIsIdempotent()
	{
		// Arrange
		String source = "Load R1, 1;";

		this._commandsLexer.setSource(source);

		// Act
		List<Token> tokensOne = this._commandsLexer.parse();
		List<Token> tokensTwo = this._commandsLexer.parse();

		// Assert
		Assert.assertEquals(tokensOne, tokensTwo);
	}

	@Test
	public void parse_ReturnsValidTokens()
	{
		// Arrange - create tokens
		Token[] testTokens =
			{
				new Token(TokenKind.Command, "Command"),
				new Token(TokenKind.Register, "R1"),
				new Token(TokenKind.Delimiter, ","),
				new Token(TokenKind.Number, "1"),
				new Token(TokenKind.Delimiter, ";")
			};

		List<Character> spaceCharacters = new ArrayList<Character>();

		for (Character spaceCharacter : this._commandsLexer
				.getSpaceCharacters())
		{
			spaceCharacters.add(spaceCharacter);
		}

		// Arrange - create source string
		StringBuilder source = new StringBuilder();

		for (int i = 0, count = testTokens.length; i < count; i++)
		{
			Token testToken = testTokens[i];

			source.append(testToken.getValue());

			// Append all space characters
			for (int j = 0, countTwo = spaceCharacters.size(); j < countTwo; j++)
			{
				source.append(spaceCharacters.get(j));
			}
		}

		this._commandsLexer.setSource(source.toString());

		// Act
		List<Token> tokens = this._commandsLexer.parse();

		// Assert
		Assert.assertEquals(Arrays.asList(testTokens), tokens);
	}

	@Test
	public void parse_SourceContainsOnlyArguments_ReturnsArgumentTokens()
	{
		// Arrange
		String[] numbers = {
			"-0123", "0123", "1234"
		};

		for (String number : numbers)
		{
			Token token = new Token(TokenKind.Number, number);

			this._commandsLexer.setSource(number);

			// Act
			List<Token> tokens = this._commandsLexer.parse();

			// Assert
			Assert.assertEquals(1, tokens.size());

			Assert.assertEquals(token, tokens.get(0));
		}
	}

	@Test
	public void parse_SourceContainsOnlyCommands_ReturnsCommandTokens()
	{
		// Arrange
		String[] commands =
			{
				"command", "command-", "command_", "prefix-command",
				"command_suffix", "prefix-command_suffix"
			};

		for (String command : commands)
		{
			Token token = new Token(TokenKind.Command, command);

			this._commandsLexer.setSource(command);

			// Act
			List<Token> tokens = this._commandsLexer.parse();

			// Assert
			Assert.assertEquals(1, tokens.size());

			Assert.assertEquals(token, tokens.get(0));
		}
	}

	@Test
	public void parse_SourceContainsOnlyDelimiters_ReturnsDelimiterTokens()
	{
		// Arrange
		String[] delimiters = {
			",", ";"
		};

		for (String delimiter : delimiters)
		{
			Token token = new Token(TokenKind.Delimiter, delimiter);

			this._commandsLexer.setSource(delimiter);

			// Act
			List<Token> tokens = this._commandsLexer.parse();

			// Assert
			Assert.assertEquals(1, tokens.size());

			Assert.assertEquals(token, tokens.get(0));
		}
	}

	@Test
	public void parse_SourceContainsOnlyRegisters_ReturnsRegisterTokens()
	{
		// Arrange
		String[] registers = {
			"R1", "R234", "R1230"
		};

		for (String register : registers)
		{
			Token token = new Token(TokenKind.Register, register);

			this._commandsLexer.setSource(register);

			// Act
			List<Token> tokens = this._commandsLexer.parse();

			// Assert
			Assert.assertEquals(1, tokens.size());

			Assert.assertEquals(token, tokens.get(0));
		}
	}

	@Test
	public void parse_SourceContainsOnlySpaceCharacters_ReturnsEmptyTokensList()
	{
		// Arrange
		StringBuilder source = new StringBuilder();

		List<Character> spaceCharacters = new ArrayList<Character>();

		for (Character spaceCharacter : this._commandsLexer
				.getSpaceCharacters())
		{
			spaceCharacters.add(spaceCharacter);
		}

		for (int i = 0, count = spaceCharacters.size(); i < count; i++)
		{
			source.append(spaceCharacters.get(i));
		}

		this._commandsLexer.setSource(source.toString());

		// Act
		List<Token> tokens = this._commandsLexer.parse();

		// Assert
		Assert.assertEquals(0, tokens.size());
	}

	@Test
	public void parse_SourceContainsUnknownTokens_ReturnsValidTokens()
	{
		// Arrange - create tokens
		Token[] testTokens =
			{
				new Token(TokenKind.Unknown, "."),
				new Token(TokenKind.Unknown, "_"),
				new Token(TokenKind.Unknown, "-"),
				new Token(TokenKind.Unknown, "\"")
			};

		// Arrange - create source string
		StringBuilder source = new StringBuilder();

		for (int i = 0, count = testTokens.length; i < count; i++)
		{
			source.append(testTokens[i].getValue());
		}

		this._commandsLexer.setSource(source.toString());

		// Act
		List<Token> tokens = this._commandsLexer.parse();

		// Assert
		Assert.assertEquals(Arrays.asList(testTokens), tokens);
	}

	@Test
	public void parse_SourceIsEmpty_ReturnsEmptyTokensList()
	{
		// Arrange
		String source = "";
		this._commandsLexer.setSource(source);

		// Act
		List<Token> tokens = this._commandsLexer.parse();

		// Assert
		Assert.assertEquals(0, tokens.size());
	}

	@Before
	public void setUp()
	{
		this._commandsLexer = new CommandsLexer();
	}

}
