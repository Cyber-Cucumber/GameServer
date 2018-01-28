package ru.torment.shared;

import java.awt.Color;
import java.io.Serializable;
import java.util.UUID;

public class Unit implements Serializable
{
	private static final long serialVersionUID = 1L;

	private UUID     id;
	private UnitType unitType;
	private String   name;
	private Color    color;
	private Integer  coordX;
	private Integer  coordY;

	public Unit( UnitType unitType, String name, Color color, Integer coordX, Integer coordY )
	{
		id = UUID.randomUUID();
		this.unitType = unitType;
		this.name     = name;
		this.color    = color;
		this.coordX   = coordX;
		this.coordY   = coordY;
	}

	public UUID     getId()       { return id;       }
	public UnitType getUnitType() { return unitType; }
	public String   getName()     { return name;     }
	public Color    getColor()    { return color;    }
	public Integer  getCoordX()   { return coordX;   }
	public Integer  getCoordY()   { return coordY;   }

	public void setUnitType( UnitType unitType ) { this.unitType = unitType; }
	public void setName(     String   name     ) { this.name     = name;     }
	public void setColor(    Color    color    ) { this.color    = color;    }
	public void setCoordX(   Integer  coordX   ) { this.coordX   = coordX;   }
	public void setCoordY(   Integer  coordY   ) { this.coordY   = coordY;   }

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj  ) return true;
		if ( obj  == null ) return false;
		if ( getClass() != obj.getClass() ) return false;
		Unit other = (Unit) obj;
		if ( id == null )
		{
			if ( other.id != null ) return false;
		}
		else if ( !id.equals( other.id ) ) return false;
		return true;
	}
}
