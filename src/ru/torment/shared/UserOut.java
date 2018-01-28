package ru.torment.shared;

import java.io.Serializable;

public class UserOut extends Message implements Serializable
{
	private static final long serialVersionUID = 1L;

	public UserOut( User user )
	{
		super( user, "UserOut");
	}
}
