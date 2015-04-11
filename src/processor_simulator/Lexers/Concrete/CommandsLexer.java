package processor_simulator.Lexers.Concrete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import processor_simulator.Enums.TokenKind;
import processor_simulator.Lexers.Abstract.ICommandsLexer;
import processor_simulator.Lexers.Abstract.Lexer;
import processor_simulator.Models.Token;
import processor_simulator.Models.TokenDefinition;
import processor_simulator.Utils.Guard;

public class CommandsLexer extends Lexer<Token> implements ICommandsLexer
{
	private List<TokenDefinition> _tokenDefinitions;

	public CommandsLexer()
	{
		super();

		this._tokenDefinitions = new ArrayList<TokenDefinition>() {
			{
				this.add(new TokenDefinition("[a-zA-Z][a-zA-Z_-]*",
					TokenKind.Command));
				this.add(new TokenDefinition("R[1-9][0-9]*",
					TokenKind.Register));
				this.add(new TokenDefinition("-?[0-9]+",
					TokenKind.Number));
				this.add(new TokenDefinition("[,;]", TokenKind.Delimiter));
			}
		};
	}

	@Override
	public Iterable<TokenDefinition> getTokenDefinitions()
	{
		return this._tokenDefinitions;
	}

	@Override
	public List<Token> parse()
	{
		List<Token> tokens = new ArrayList<Token>();

		this._offset = 0;

		while (this.isInBounds())
		{
			this.skipSpaces();

			if (!this.isInBounds())
			{
				break;
			}

			Token token = this.processToken();

			if (token == null)
			{
				String tokenValue =
					this._source.substring(this._offset, this._offset + 1);

				token = new Token(TokenKind.Unknown, tokenValue);

				this._offset += tokenValue.length();
			}

			tokens.add(token);
		}

		return tokens;
	}

	@Override
	public void setTokenDefinitions(Iterable<TokenDefinition> tokenDefinitions)
	{
		Guard.notNull(tokenDefinitions, "tokenDefinitions");

		this._tokenDefinitions = new ArrayList<TokenDefinition>();

		for (TokenDefinition tokenDefinition : tokenDefinitions)
		{
			this._tokenDefinitions.add(tokenDefinition);
		}
	}

	private Token processToken()
	{
		List<Token> foundTokens = new ArrayList<Token>();

		for (TokenDefinition definition : this._tokenDefinitions)
		{
			String matchString = this._source.substring(this._offset);

			Matcher matcher =
				definition.getRepresentation().matcher(matchString);

			if (!matcher.lookingAt())
			{
				continue;
			}

			TokenKind tokenKind = definition.getKind();

			String tokenValue = matchString.substring(0, matcher.end());

			Token token = new Token(tokenKind, tokenValue);

			foundTokens.add(token);
		}

		if (foundTokens.size() == 0)
		{
			return null;
		}

		Token longestToken =
			Collections.max(foundTokens, (n1, n2) -> Integer.compare(n1
					.getValue().length(), n2.getValue().length()));

		this._offset += longestToken.getValue().length();

		return longestToken;
	}
}