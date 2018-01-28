package ru.torment.shared;

import java.io.Serializable;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;

	private User   user;
	private String message;
	private Date   time;
	private String msgStatus;
	private User   recipientUser;  // Для приватных сообщений

	public Message( User user, String message )
	{
		this.user    = user;
		this.message = message;
		this.time    = Calendar.getInstance().getTime();
	}

	// Getters
	public User   getUser()          { return user;          }
	public String getMessage()       { return message;       }
	public String getDate()          { return new Time( time.getTime() ).toString(); }
	public String getMsgStatus()     { return msgStatus;     }
	public User   getRecipientUser() { return recipientUser; }

	// Setters
	public void setMsgStatus(     String msgStatus     ) { this.msgStatus     = msgStatus;     }
	public void setRecipientUser( User   recipientUser ) { this.recipientUser = recipientUser; }
}
