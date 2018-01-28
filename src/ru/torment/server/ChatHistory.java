package ru.torment.server;

import java.util.List;
import java.util.ArrayList;

import ru.torment.shared.Message;

public class ChatHistory
{
	private static List<Message> history = new ArrayList<Message>( Config.HISTORY_LENGTH );

	public synchronized static void addMessage( Message message )
	{
		if ( history.size() > Config.HISTORY_LENGTH ) { history.remove(0); }

		history.add( message );
	}

	public synchronized static List<Message> getHistory()
	{
		return history;
	}
}
