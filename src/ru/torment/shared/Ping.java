package ru.torment.shared;

import java.io.Serializable;

public class Ping extends Message implements Serializable
{
	private static final long serialVersionUID = 1L;

	public Ping()
	{
		super( new User("ping"), "ping");
	}
}
