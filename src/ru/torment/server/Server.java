package ru.torment.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

// TODO сделать сервисом (демоном)
public class Server
{
//	private static UsersList   list        = new UsersList();
//	private static ChatHistory chatHistory = new ChatHistory();

	static ServerSocket serverSocket = null;

	//======================================================================================
	public static void main( String[] args )
	{
		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook( new ShutdownThread() );
		runtime = null;

		try
		{
			try
			{
				System.out.println("Server start...");
				// Создаем слушатель
				serverSocket = new ServerSocket( Config.PORT );
				while (true)
				{
					Socket socket_Сlient = null;
					while ( socket_Сlient == null )
					{
						System.out.println("Ожидаю подключения клинта...");
						socket_Сlient = serverSocket.accept();
					}
					System.out.println("Клиент подключен");
					new ClientThread( socket_Сlient ); // Создаем новый поток, которому передаем сокет
				}
			}
			catch ( SocketException e )
			{
				System.err.println("Server::main() --- SocketException");
				if ( serverSocket.isClosed() )
				{
					System.out.println("Server::main() --- serverSocket closed");
					return;
				}
				e.printStackTrace();
			}
			catch ( IOException e )
			{
				System.err.println("Server::main() --- IOException");  
				e.printStackTrace();
			}
		}
		finally
		{
			try
			{
				if ( serverSocket != null && !serverSocket.isClosed() )
				{
					System.out.println("Server::main() --- finally --- serverSocket.close()");
					serverSocket.close();
				}
			}
			catch ( IOException e ) { e.printStackTrace(); }
		}
		System.exit(0);
	}

//	public synchronized static UsersList   getUserList()    { return list;        }
//	public synchronized static ChatHistory getChatHistory() { return chatHistory; }
}


//======================================================================================
// Поток, вызываемый при закрытии приложения (вызывается в любом случае)
//======================================================================================
class ShutdownThread extends Thread
{
	@Override
	public void run()
	{
		System.out.println("ShutdownThread::run()");
		try
		{
			if ( Server.serverSocket != null && !Server.serverSocket.isClosed() )
			{
				System.out.println("ShutdownThread::run() --- serverSocket.close()");
				Server.serverSocket.close();
			}
		}
		catch ( IOException e ) { e.printStackTrace(); }
	}
}
