package processor_simulator.Parsers.Concrete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

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
import processor_simulator.Utils.Guard;

public class CommandsParser implements ICommandsParser
{
	private Stack<Command> _commands;

	private Map<String, CommandType> _commandTypesDictionary;

	private int _currentTokenId;

	private Map<SpecialTokenKind, Token> _specialTokensDictionary;

	private int _state;

	private List<Token> _tokens;

	public CommandsParser() throws SpecialTokenIsNotDefinedException
	{
		this._tokens = new ArrayList<Token>();

		this._currentTokenId = 0;

		this.setCommandTypesDictionary(new HashMap<String, CommandType>() {
			{
				this.put("add", CommandType.Add);
				this.put("load", CommandType.Load);
				this.put("rightmove", CommandType.RightMove);
				this.put("leftmove", CommandType.LeftMove);
				this.put("xor", CommandType.Xor);
			}
		});

		this.setSpecialTokensDictionary(new HashMap<SpecialTokenKind, Token>() {
			{
				this.put(SpecialTokenKind.Concatenation, new Token(
					TokenKind.Delimiter, ","));

				this.put(SpecialTokenKind.Termination, new Token(
					TokenKind.Delimiter, ";"));
			}
		});
	}

	@Override
	public Map<String, CommandType> getCommandTypesDictionary()
	{
		return this._commandTypesDictionary;
	}

	@Override
	public Map<SpecialTokenKind, Token> getSpecialTokensDictionary()
	{
		return this._specialTokensDictionary;
	}

	@Override
	public List<Token> getTokens()
	{
		return this._tokens;
	}

	@Override
	public List<Command> parse() throws CommandIsInvalidException
	{
		this.initialize();

		this.processCommands();

		return this._commands;
	}

	@Override
	public void setCommandTypesDictionary(
		Map<String, CommandType> commandTypesDictionary)
	{
		Guard.notNull(commandTypesDictionary, "commandTypesDictionary");

		this._commandTypesDictionary =
			new HashMap<String, CommandType>(commandTypesDictionary);
	}

	@Override
	public void setSpecialTokensDictionary(
		Map<SpecialTokenKind, Token> specialTokensDictionary)
		throws SpecialTokenIsNotDefinedException
	{
		Guard.notNull(specialTokensDictionary, "specialTokensDictionary");

		this._specialTokensDictionary =
			new HashMap<SpecialTokenKind, Token>(specialTokensDictionary);

		for (SpecialTokenKind kind : SpecialTokenKind.values())
		{
			if (this._specialTokensDictionary.get(kind) == null)
			{
				throw new SpecialTokenIsNotDefinedException(String.format(
					"Special node with kind '%1$s' must be defined.", kind));
			}
		}
	}

	@Override
	public void setTokens(List<Token> tokens)
	{
		Guard.notNull(tokens, "tokens");

		this._tokens = new ArrayList<Token>(tokens);

		this._currentTokenId = 0;
	}

	private void createCommandStep() throws CommandIsInvalidException
	{
		Token currentToken = this.getCurrentToken();

		if (currentToken.getKind() != TokenKind.Command)
		{
			throw new CommandIsInvalidException(
				String.format(
					"Invalid command at token '%1$s'. Command must start only from commandName.",
					currentToken.getValue()));
		}

		this._commands.push(this.getCommandFromToken(currentToken));

		this._state = 1;
	}

	private Argument getArgumentFromToken(Token token)
	{
		ArgumentType argumentType =
			(token.getKind() == TokenKind.Register) ? ArgumentType.Register
				: ArgumentType.Number;

		String argumentValue = token.getValue();

		if (argumentType == ArgumentType.Register)
		{
			// Get the register's number.
			argumentValue = argumentValue.substring(1);
		}

		Argument argument =
			new Argument(argumentType, Integer.parseInt(argumentValue));

		return argument;
	}

	private Command getCommandFromToken(Token token)
	{
		CommandType commandType =
			this._commandTypesDictionary.get(token.getValue().toLowerCase());

		if (commandType == null)
		{
			commandType = CommandType.Unknown;
		}

		return new Command(commandType);
	}

	private Token getCurrentToken()
	{
		Token currentToken = this._tokens.get(this._currentTokenId);

		return currentToken;
	}

	private void initialize()
	{
		this._commands = new Stack<Command>();

		this._currentTokenId = 0;

		this._state = 0;
	}

	private boolean isInBounds()
	{
		return this._currentTokenId < this._tokens.size();
	}

	private void multipleArgumentsStep() throws CommandIsInvalidException
	{
		Token currentToken = this.getCurrentToken();

		if (currentToken.equals(this._specialTokensDictionary
				.get(SpecialTokenKind.Concatenation)))
		{
			this._state = 1;
		}
		else if (currentToken.equals(this._specialTokensDictionary
				.get(SpecialTokenKind.Termination)))
		{
			this._state = 0;
		}
		else
		{
			throw new CommandIsInvalidException(
				String.format(
					"Invalid command at token '%1$s'. Expected concatenation or termination token.",
					currentToken.getValue()));
		}
	}

	private void processCommands() throws CommandIsInvalidException
	{
		while (this.isInBounds())
		{
			switch (this._state)
			{
				case 0:
				{
					this.createCommandStep();
					break;
				}

				case 1:
				{
					this.setArgumentStep();
					break;
				}

				case 2:
				{
					this.multipleArgumentsStep();
					break;
				}
			}

			this._currentTokenId++;
		}

		if (this._state != 0)
		{
			Command lastCommand = this._commands.peek();

			throw new CommandIsInvalidException(String.format(
				"Invalid command '%1$s'.", lastCommand.toString()));
		}
	}

	private void setArgumentStep() throws CommandIsInvalidException
	{
		Token currentToken = this.getCurrentToken();

		TokenKind currentTokenKind = currentToken.getKind();

		if (currentTokenKind != TokenKind.Register
			&& currentTokenKind != TokenKind.Number)
		{
			throw new CommandIsInvalidException(
				String.format(
					"Invalid command at token '%1$s'. Expected register or number token.",
					currentToken.getValue()));
		}

		Command lastCommand = this._commands.peek();

		List<Argument> arguments =
			new ArrayList<Argument>(lastCommand.getArguments());

		arguments.add(this.getArgumentFromToken(currentToken));

		lastCommand.setArguments(arguments);

		this._state = 2;
	}
}