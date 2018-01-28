package ru.torment.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

import ru.torment.shared.GameData;
import ru.torment.shared.GameRequest;
import ru.torment.shared.InitMessage;
import ru.torment.shared.Message;
import ru.torment.shared.NewUser;
import ru.torment.shared.Ping;
import ru.torment.shared.StartGame;
import ru.torment.shared.StopGame;
import ru.torment.shared.User;
import ru.torment.shared.UserOut;

public class ClientThread extends Thread
{
	private final static int DELAY = 30000;

	private Socket socket;
	private User user;
	private int inPacks  = 0;
	private int outPacks = 0;
	private boolean flag = false;
	private Timer timer;

	private ObjectInputStream  objectInputStream  = null;
	private ObjectOutputStream objectOutputStream = null;


	//======================================================================================
	//======================================================================================
	public ClientThread( Socket socket )
	{
		this.socket = socket;
		start();
	}

	//======================================================================================
	//======================================================================================
	public void run()
	{
//		System.out.println(" + DesktopChatServer::ClientThread::run()");
		System.out.println(" + DesktopChatServer::ClientThread::run() --- Thread: " + getName() );

		try
		{
			objectInputStream  = new ObjectInputStream(  socket.getInputStream()  );
			objectOutputStream = new ObjectOutputStream( socket.getOutputStream() );

			//-----------------------------------------------------------------------------------------------
			// Сразу после установления соединения с клиентом, отсылаем ему приветственное сообщение
			InitMessage initMessage_Greeting = new InitMessage( null, Config.HELLO_MESSAGE, ClientList.getListUser() );
			objectOutputStream.writeObject( initMessage_Greeting );
			// Также отсылаем историю сообщений (несколько последних сообщений)
			for ( Message message_History : ChatHistory.getHistory() )
			{
				objectOutputStream.writeObject( message_History );
			}
			// Считываем ответ клиента
			Message message_Init = (Message) objectInputStream.readObject();
			// Инициализируем необходимые переменные
			user = message_Init.getUser();
			System.out.println(" + DesktopChatServer::ClientThread::run() --- login: " + user.getLogin() );
			System.out.println(" + DesktopChatServer::ClientThread::run() --- message: " + message_Init.getMessage() );
			// И рассылаем всем другим участникам комнаты чата сообщение о новом участнике
			broadcast( ClientList.getListClient(), new NewUser( user ) );
			broadcast( ClientList.getListClient(), new Message( new User("Server-Bot"), user.getLogin() + " вошёл в комнату чата") );
			// Затем, добавляем участника чата в список всех участников
			ClientList.addClient( user, socket, objectOutputStream, objectInputStream );
			//-----------------------------------------------------------------------------------------------

//			if ( !message_First.getMessage().equals( Config.HELLO_MESSAGE ) )
//			{
//				System.out.println("[" + login + "]: " + message_First.getMessage() );
//				ChatHistory.addMessage( message_First );
//			}
//			else
//			{
//				for ( Message message_History : ChatHistory.getHistory() )
//				{
//					objectOutputStream.writeObject( message_History );
//				}
//				broadcast( UsersList.getClientsList(), new Message("Server-Bot", login + " вошёл в комнату чата") );
//			}

//			message_First.setOnlineUsers( UsersList.getUsers() );
//			System.out.println(" + DesktopChatServer::ClientThread::run() --- User list SIZE: " + UsersList.getClientsList().size() );
//			for ( Client client : UsersList.getClientsList() )
//			{
//				System.out.println("client: " + client );
//			}
//			System.out.println(" + DesktopChatServer::ClientThread::run() --- message: " + message_First.getMessage() );
//			broadcast( UsersList.getClientsList(), message_First );

			//--------------------------------------------------------------------
			// Таймер для пинга клиента
			//--------------------------------------------------------------------
			timer = new Timer( DELAY,
					new ActionListener()
					{
						@Override
						public void actionPerformed( ActionEvent e )
						{
							try
							{
								if ( inPacks == outPacks )
								{
									objectOutputStream.writeObject( new Ping() );
									outPacks++;
									System.out.println( outPacks + " out");
								}
								else
								{
									System.out.println(" + DesktopChatServer::ClientThread::run() --- [ inPacks != outPacks ]");
									flag = true;
									disconnect();
//									throw new SocketException();
								}
							}
							catch ( SocketException se )
							{
								System.out.println(" + DesktopChatServer::ClientThread::run() --- SocketException");
//								System.out.println( user.getLogin() + " disconnected!");
//								ClientList.deleteClient( user );
//								broadcast( ClientList.getListClient(), new UserOut( user ) );
//								broadcast( ClientList.getListClient(), new Message( new User("Server-Bot"), user.getLogin() + " покинул комнату чата") );
//								timer.stop();
								flag = true;
								disconnect();
							}
							catch ( IOException ioe )
							{
								System.out.println(" + DesktopChatServer::ClientThread::run() --- IOException");
								System.out.println( ioe.getMessage() );
//								ioe.printStackTrace();
							}
						}
					});
			timer.start();
			//--------------------------------------------------------------------

			objectOutputStream.writeObject( new Ping() );
			outPacks++;
			System.out.println( outPacks + " out");

			System.out.println("Thread [" + getName() + "] --- Жду сообщения от клиента...");

			//--------------------------------------------------------------------
			// Основной цикл ожидания сообщений от клиента
			//--------------------------------------------------------------------
			while (true)
			{
				if ( flag )
				{
					flag = false;
					break;
				}

				Message message = (Message) objectInputStream.readObject();
				if ( message instanceof Ping )
				{
					inPacks++;
					System.out.println( inPacks + " in");
				}
				// Если пришёл запрос на игру
				else if ( message instanceof GameRequest )
				{
					GameRequest gameRequest = (GameRequest) message;
					System.out.println(" + DesktopChatServer::ClientThread::run() --- GameRequest from: " + gameRequest.getUser().getLogin() + " --- to: " + gameRequest.getSecondUser().getLogin() );
					objectOutputStream.writeObject( new Message( gameRequest.getUser(), "Пригласили пользователя [" + gameRequest.getSecondUser().getLogin() + "] сыграть...") );
					Client client_Target = ClientList.getClient( gameRequest.getSecondUser() );
					if ( client_Target != null )
					{
						client_Target.getObjectOutputStream().writeObject( gameRequest );
					}
				}
				// Если пользователь согласился сыграть
				else if ( message instanceof StartGame )
				{
					StartGame startGame = (StartGame) message;
					System.out.println(" + DesktopChatServer::ClientThread::run() --- StartGame from: " + startGame.getUser().getLogin() );
					GameList.addGame( startGame.getUser(), startGame.getSecondUser() );
					Client client_Target = ClientList.getClient( startGame.getSecondUser() );
					if ( client_Target != null )
					{
						client_Target.getObjectOutputStream().writeObject( startGame );
					}
				}
				// Если пришли игровые данные
				else if ( message instanceof GameData )
				{
					GameData gameData = (GameData) message;
					System.out.println(" + DesktopChatServer::ClientThread::run() --- GameData from: " + gameData.getUser().getLogin() + " --- DataType: " + gameData.getGameDataType() + " --- Unit ID: " + gameData.getUnit().getId() + " --- Name: " + gameData.getUnit().getName() );
					Game game = GameList.getGame( gameData.getUser() );
					if ( game != null )
					{
						game.newData( gameData );
//						game.addUnit( gameData.getUser(), gameData.getUnit() );
					}
					else
					{
						// TODO
					}
				}
				// Если пользователь закрыл игру
				else if ( message instanceof StopGame )
				{
					StopGame stopGame = (StopGame) message;
					System.out.println(" + DesktopChatServer::ClientThread::run() --- StopGame from: " + stopGame.getUser().getLogin() );
					Game game = GameList.getGame( stopGame.getUser() );
					if ( game != null )
					{
						GameList.deleteGame( game );
					}
					Client client_Target = ClientList.getClient( game.getUser1().equals( stopGame.getUser() ) ? game.getUser2() : game.getUser1() );
					if ( client_Target != null )
					{
						client_Target.getObjectOutputStream().writeObject( stopGame );
					}
				}
				// Если пришло первое (приветственное) сообщение
				else if ( message.getMessage().equals( Config.HELLO_MESSAGE ) )
				{
					for ( Message message_History : ChatHistory.getHistory() )
					{
						objectOutputStream.writeObject( message_History );
					}
					broadcast( ClientList.getListClient(), new NewUser( user ) );
					broadcast( ClientList.getListClient(), new Message( new User("Server-Bot"), user.getLogin() + " вошёл в комнату чата") );
				}
				else
				{
					System.out.println("[" + user.getLogin() + "]: " + message.getMessage() );
					ChatHistory.addMessage( message );
					System.out.println("Send broadcast Message: \"" + message.getMessage() + "\"");
					broadcast( ClientList.getListClient(), message );
				}

//				if ( !(message instanceof Ping) && !message.getMessage().equals( Config.HELLO_MESSAGE ) )
//				{
//					System.out.println("Send broadcast Message: \"" + message.getMessage() + "\"");
//					broadcast( ClientList.getListClient(), message );
//				}
			}
			//--------------------------------------------------------------------
		}
		catch ( SocketException e )
		{
			System.out.println(" + DesktopChatServer::ClientThread::run() --- SocketException");
//			System.out.println(" + ------------------------- \n + " + user.getLogin() + " disconnected! \n + -------------------------");
//			ClientList.deleteClient( user );
//			broadcast( ClientList.getListClient(), new UserOut( user ) );
//			broadcast( ClientList.getListClient(), new Message( new User("Server-Bot"), user.getLogin() + " покинул комнату чата") );
//			if ( timer != null && timer.isRunning() ) { timer.stop(); }
			disconnect();

//			// Закрываем сокет
//			try
//			{
//				// Нет смысла отдельно закрывать потоки ввода-вывода, так как при закрытии сокета автоматически закрываются и все его потоки
////				objectInputStream.close();
////				objectOutputStream.close();
//				socket.close();
//			}
//			catch ( IOException ioException )
//			{
//				System.out.println(" + DesktopChatServer::ClientThread::run() --- IOException");
//				System.out.println( ioException.getMessage() );
////				ioException.printStackTrace();
//			}
		}
		catch ( IOException e )
		{
			System.out.println(" + DesktopChatServer::ClientThread::run() --- IOException");
			System.out.println( e.getMessage() );
//			e.printStackTrace();
			disconnect();
		}
		catch ( ClassNotFoundException e )
		{
			System.out.println(" + DesktopChatServer::ClientThread::run() --- ClassNotFoundException");
			System.out.println( e.getMessage() );
//			e.printStackTrace();
			disconnect();
		}
		catch ( Exception e )
		{
			System.out.println(" + DesktopChatServer::ClientThread::run() --- Exception");
			System.out.println( e.getMessage() );
			disconnect();
		}

		System.out.println(" + DesktopChatServer::ClientThread::run() --- Thread: " + getName() + " is CLOSED" );
	}

	//======================================================================================
	//======================================================================================
	private void broadcast( List<Client> list_Client, Message message )
	{
		System.out.println(" + DesktopChatServer::ClientThread::broadcast()");
		try
		{
			for ( Client client : list_Client )
			{
				client.getObjectOutputStream().writeObject( message );
			}
		}
		catch ( SocketException e )
		{
			System.out.println(" + DesktopChatServer::ClientThread::broadcast() --- SocketException");
//			System.out.println("in broadcast: " + user.getLogin() + " disconnected!");
//			ClientList.deleteClient( user );
//			broadcast( ClientList.getListClient(), new UserOut( user ) );
//			broadcast( ClientList.getListClient(), new Message(new User("Server-Bot"), user.getLogin() + " покинул комнату чата") );
//			if ( timer != null && timer.isRunning() ) { timer.stop(); }
			disconnect();
		}
		catch ( IOException e )
		{
			System.out.println(" + DesktopChatServer::ClientThread::broadcast() --- IOException");
			System.out.println( e.getMessage() );
//			e.printStackTrace();
		}
	}

	//======================================================================================
	//======================================================================================
	private void disconnect()
	{
		System.out.println(" + DesktopChatServer::ClientThread::disconnect()");
		System.out.println(" + ------------------------- \n + " + user.getLogin() + " disconnected! \n + -------------------------");
		ClientList.deleteClient( user );
		Game game = GameList.getGame( user );
		if ( game != null )
		{
			GameList.deleteGame( game );
		}
		broadcast( ClientList.getListClient(), new UserOut( user ) );
		broadcast( ClientList.getListClient(), new Message( new User("Server-Bot"), user.getLogin() + " покинул комнату чата") );
		if ( timer != null && timer.isRunning() ) { timer.stop(); }
		if ( !socket.isClosed() )
		{
			// Закрываем сокет
			try
			{
				// Нет смысла отдельно закрывать потоки ввода-вывода, так как при закрытии сокета автоматически закрываются и все его потоки
//				objectInputStream.close();
//				objectOutputStream.close();
				socket.close();
			}
			catch ( IOException ioException )
			{
				System.out.println(" + DesktopChatServer::ClientThread::run() --- IOException");
				System.out.println( ioException.getMessage() );
//				ioException.printStackTrace();
			}
		}
	}
}
