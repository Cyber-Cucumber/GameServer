package ru.torment.shared;

public class StopGame extends Message
{
	private static final long serialVersionUID = 1L;

	public StopGame( User user )
	{
		super( user, "StopGame" );
	}
}
