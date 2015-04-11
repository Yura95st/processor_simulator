package processor_simulator.Lexers.Abstract;

import java.util.ArrayList;
import java.util.List;

import processor_simulator.Utils.Guard;

public abstract class Lexer<T>
{
	protected int _offset;

	protected String _source;

	protected List<Character> _spaceCharacters;

	public Lexer()
	{
		this._source = "";

		this._spaceCharacters = new ArrayList<Character>() {
			{
				this.add(' ');
				this.add('\n');
				this.add('\r');
				this.add('\t');
			}
		};
	}

	public String getSource()
	{
		return this._source;
	}

	public Iterable<Character> getSpaceCharacters()
	{
		return this._spaceCharacters;
	}

	public void setSource(String source)
	{
		Guard.notNull(source, "source");

		this._source = source;

		this._offset = 0;
	}

	public void setSpaceCharacters(Iterable<Character> spaceCharacters)
	{
		Guard.notNull(spaceCharacters, "spaceCharacters");

		this._spaceCharacters = new ArrayList<Character>();

		for (Character spaceCharacter : spaceCharacters)
		{
			this._spaceCharacters.add(spaceCharacter);
		}
	}

	protected boolean isInBounds()
	{
		return this._offset < this._source.length();
	}

	protected void skipSpaces()
	{
		while (this.isInBounds()
			&& this._spaceCharacters
					.contains(this._source.charAt(this._offset)))
		{
			this._offset++;
		}
	}
}