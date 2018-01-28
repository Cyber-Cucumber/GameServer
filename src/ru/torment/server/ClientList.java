package ru.torment.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.torment.shared.User;

public final class ClientList
{
	private static List<Client> list_Client = new ArrayList<Client>();  // Список всех клиентов

	private ClientList() {}

	//======================================================================================
	// Добавить клиент в список
	//======================================================================================
	public synchronized static void addClient( User user, Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream )
	{
		System.out.println(" + GameServer::ClientList::addClient()");
//		System.out.println( user.getLogin() + " connected" );
		list_Client.add( new Client( socket, objectOutputStream, objectInputStream, user ) );
	}

	//======================================================================================
	// Удалить клиент из списка
	//======================================================================================
	public synchronized static void deleteClient( User user )
	{
		System.out.println(" + GameServer::ClientList::deleteClient()");
		Client client_ForDelete = null;
		for ( Client client : list_Client )
		{
			if ( client.getUser().equals( user ) )
			{
				client_ForDelete = client;
				break;
			}
		}
		if ( client_ForDelete != null )
		{
			list_Client.remove( client_ForDelete );
		}
	}

	//======================================================================================
	// Получить список с клиентами
	//======================================================================================
	public synchronized static List<Client> getListClient()
	{
		System.out.println(" + GameServer::ClientList::getListClient()");
		return list_Client;
	}

	//======================================================================================
	// Получить список с пользователями
	//======================================================================================
	public synchronized static List<User> getListUser()
	{
		System.out.println(" + GameServer::ClientList::getListUser()");
		List<User> list_User = new ArrayList<User>();
		for ( Client client : list_Client )
		{
			list_User.add( client.getUser() );
		}
		return list_User;
	}

	//======================================================================================
	// Поиск клиента по пользователю
	//======================================================================================
	public synchronized static Client getClient( User user )
	{
		System.out.println(" + GameServer::ClientList::getClient()");
		Client client_Target = null;
		for ( Client client : list_Client )
		{
			if ( client.getUser().equals( user ) )
			{
				client_Target = client;
				break;
			}
		}
		return client_Target;
	}
}
