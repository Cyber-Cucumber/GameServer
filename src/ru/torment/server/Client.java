package ru.torment.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ru.torment.shared.User;

public class Client
{
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream  objectInputStream;
	private User user;

	public Client( Socket socket )
	{
		this.socket = socket;
	}

	public Client( Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream, User user )
	{
		this.socket = socket;
		this.objectOutputStream = objectOutputStream;
		this.objectInputStream  = objectInputStream;
		this.user = user;
	}

	// Getters
	public Socket getSocket() { return socket; }
	public ObjectOutputStream getObjectOutputStream() { return objectOutputStream; }
	public ObjectInputStream  getObjectInputStream()  { return objectInputStream; }
	public User getUser() { return user; }

	// Setters
	public void setObjectOutputStream( ObjectOutputStream objectOutputStream ) { this.objectOutputStream = objectOutputStream; }
	public void setObjectInputStream(  ObjectInputStream  objectInputStream  ) { this.objectInputStream  = objectInputStream;  }
	public void setUser( User user ) { this.user = user; }
}
