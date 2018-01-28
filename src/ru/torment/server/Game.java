package ru.torment.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.torment.shared.GameData;
import ru.torment.shared.GameDataType;
import ru.torment.shared.Unit;
import ru.torment.shared.User;

public class Game
{
	private User user1;
	private User user2;
	private List<Unit> list_Unit;

	//======================================================================================
	public Game( User user1, User user2 )
	{
		System.out.println(" + GameServer::Game::Game()");
		this.user1 = user1;
		this.user2 = user2;
		list_Unit = new ArrayList<Unit>();
	}

	//======================================================================================
	public User getUser1() { return user1; }
	public User getUser2() { return user2; }

	//======================================================================================
	public synchronized void addUnit( User user, Unit unit, GameData gameData )
	{
		System.out.println(" + GameServer::Game::addUnit()");

		list_Unit.add( unit );
		User user_ForNonify = user1;
		if ( user.equals( user1 ) ) { user_ForNonify = user2; }
		try
		{
			Client client = ClientList.getClient( user_ForNonify );
			if ( client != null ) { client.getObjectOutputStream().writeObject( gameData ); }
		}
		catch ( IOException e )
		{
			System.out.println(" + GameServer::Game::addUnit() --- IOException");
			System.out.println( e.getMessage() );
//			e.printStackTrace();
		}
	}

	//======================================================================================
	public synchronized void deleteUnit( User user, Unit unit )
	{
		System.out.println(" + GameServer::Game::deleteUnit()");
		list_Unit.remove( unit );
	}

	//======================================================================================
	public void newData( GameData gameData )
	{
		System.out.println(" + GameServer::Game::newData()");

		if ( gameData.getGameDataType().equals( GameDataType.NEW_UNIT ) )
		{
			addUnit( gameData.getUser(), gameData.getUnit(), gameData );
		}
		else if ( gameData.getGameDataType().equals( GameDataType.DELETE_UNIT ) )
		{
			deleteUnit( gameData.getUser(), gameData.getUnit() );
		}
		else if ( gameData.getGameDataType().equals( GameDataType.UNIT_NEW_POSITION ) )
		{
			// TODO
		}
	}

	//======================================================================================
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user1 == null) ? 0 : user1.hashCode());
		result = prime * result + ((user2 == null) ? 0 : user2.hashCode());
		return result;
	}

	//======================================================================================
	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj  ) return true;
		if ( obj  == null ) return false;
		if ( getClass() != obj.getClass() ) return false;
		Game other = (Game) obj;
		if ( user1 == null )
		{
			if (other.user1 != null) return false;
		}
		else if ( !user1.equals( other.user1 ) ) return false;
		if ( user2 == null )
		{
			if ( other.user2 != null ) return false;
		}
		else if ( !user2.equals( other.user2 ) ) return false;
		return true;
	}
}
