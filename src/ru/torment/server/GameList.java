package ru.torment.server;

import java.util.ArrayList;
import java.util.List;

import ru.torment.shared.User;

public final class GameList
{
	private static List<Game> list_Game = new ArrayList<Game>();  // Список всех игр

	private GameList() {}

	//======================================================================================
	// Добавить игру в список
	//======================================================================================
	public synchronized static void addGame( Game game )
	{
		System.out.println(" + GameServer::GameList::addGame(1)");
		list_Game.add( game );
	}
	//======================================================================================
	public synchronized static void addGame( User user1, User user2 )
	{
		System.out.println(" + GameServer::GameList::addGame(2)");
		list_Game.add( new Game( user1, user2 ) );
	}

	//======================================================================================
	// Удалить игру из списка
	//======================================================================================
	public synchronized static void deleteGame( Game gameForDel )
	{
		System.out.println(" + GameServer::GameList::deleteGame(1)");
		for ( Game game : list_Game )
		{
			if ( game.equals( gameForDel ) )
			{
				list_Game.remove( gameForDel );
				break;
			}
		}
	}
	//======================================================================================
	public synchronized static void deleteGame( User user1, User user2 )
	{
		System.out.println(" + GameServer::GameList::deleteGame(2)");
		for ( Game game : list_Game )
		{
			if ( game.getUser1().equals( user1 ) && game.getUser2().equals( user2 ) )
			{
				list_Game.remove( game );
				break;
			}
		}
	}

	//======================================================================================
	// Получить список с играми
	//======================================================================================
	public synchronized static List<Game> getListGame()
	{
		System.out.println(" + GameServer::GameList::getListGame()");
		return list_Game;
	}

	//======================================================================================
	// Поиск игры по пользователю
	//======================================================================================
	public synchronized static Game getGame( User user )
	{
		System.out.println(" + GameServer::GameList::getGame()");
		Game game_Target = null;
		for ( Game game : list_Game )
		{
			if ( game.getUser1().equals( user ) || game.getUser2().equals( user ) )
			{
				game_Target = game;
				break;
			}
		}
		if ( game_Target == null ) { System.out.println(" + GameServer::GameList::getGame() --- Game not found!"); }
		return game_Target;
	}
}
