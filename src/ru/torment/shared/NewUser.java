package ru.torment.shared;

import java.io.Serializable;

public class NewUser extends Message implements Serializable
{
	private static final long serialVersionUID = 1L;

	public NewUser( User user )
	{
		super( user, "NewUser");
	}
}
