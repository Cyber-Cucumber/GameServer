package ru.torment.shared;

public class StartGame extends Message
{
	private static final long serialVersionUID = 1L;

	private User secondUser;

	public StartGame( User user, User secondUser )
	{
		super( user, "StartGame" );
		this.secondUser = secondUser;
	}

	public User getSecondUser() { return secondUser; }
}
