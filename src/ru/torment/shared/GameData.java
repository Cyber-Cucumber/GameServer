package ru.torment.shared;

public class GameData extends Message
{
	private static final long serialVersionUID = 1L;

	private GameDataType gameDataType;
	private Unit unit;

	public GameData( GameDataType gameDataType, User user, String message, Unit unit )
	{
		super( user, message );
		this.gameDataType = gameDataType;
		this.unit = unit;
	}

	public Unit getUnit() { return unit; }
	public GameDataType getGameDataType() { return gameDataType; }
}
