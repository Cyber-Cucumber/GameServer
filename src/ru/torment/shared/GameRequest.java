package ru.torment.shared;

public class GameRequest extends Message
{
	private static final long serialVersionUID = 1L;

	private User secondUser;

	public GameRequest( User user, User secondUser )
	{
		super( user, "GameRequest" );
		this.secondUser = secondUser;
	}

	public User getSecondUser() { return secondUser; }
}
