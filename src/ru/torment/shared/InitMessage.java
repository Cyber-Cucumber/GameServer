package ru.torment.shared;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InitMessage extends Message implements Serializable
{
	private static final long serialVersionUID = 1L;

	private List<User> list_User = new ArrayList<User>();

	// Конструктор, которым будет пользоваться клиент
	public InitMessage( User user, String message )
	{
		super( user, message );
	}

	// Конструктор, которым будет пользоваться сервер
	public InitMessage( User user, String message, List<User> list_User )
	{
		super( user, message );
		this.list_User = list_User;
	}

	// Getters
	public List<User> getUsers() { return this.list_User; }

	// Setters
	public void setUsers( List<User> list_User ) { this.list_User = list_User; }
}
