package ru.torment.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config
{
	private static final String PROPERTIES_FILE_NAME = "server.properties";

	public static int    PORT;
	public static int    HISTORY_LENGTH;
    public static String HELLO_MESSAGE = "Добро пожаловать в чат";

	static
	{
		System.out.println(" + DesktopChat::Config::static_init_block");

		Properties properties = new Properties();
		FileInputStream propertiesFile = null;
		try
		{
			String userHomeDir = System.getProperty("user.home");
			String filePath = userHomeDir + File.separatorChar + PROPERTIES_FILE_NAME;
			System.out.println(" + DesktopChat::Config::static_init_block --- Property file path: " + filePath );

			propertiesFile = new FileInputStream( filePath );
			properties.load( propertiesFile );

			PORT           = Integer.parseInt( properties.getProperty("PORT") );
			HISTORY_LENGTH = Integer.parseInt( properties.getProperty("HISTORY_LENGTH") );
//			HELLO_MESSAGE  = properties.getProperty("HELLO_MESSAGE");

			System.out.println("+ DesktopChat::Config::static_init_block --- PORT           = " + PORT           );
			System.out.println("+ DesktopChat::Config::static_init_block --- HISTORY_LENGTH = " + HISTORY_LENGTH );
			System.out.println("+ DesktopChat::Config::static_init_block --- HELLO_MESSAGE  = " + HELLO_MESSAGE  );
		}
		catch ( FileNotFoundException ex )
		{
			System.err.println("Properties config file not found");
		}
		catch ( IOException ex )
		{
			System.err.println("Error while reading file");
		}
		finally
		{
			try
			{
				propertiesFile.close();
			}
			catch ( IOException ex )
			{
				ex.printStackTrace();
			}
		}
	}
}
