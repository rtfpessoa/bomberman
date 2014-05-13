package pt.utl.ist.cmov.bomberman.game.dto;

import java.io.Serializable;

import pt.utl.ist.cmov.bomberman.util.Position;

public class ModelDTO implements Serializable {

	private static final long serialVersionUID = -7638022407453988722L;

	private final Integer id;
	private final Character type;
	private final Position pos;
	private final Integer bombermanId;

	ModelDTO(Integer id, Character type, Position pos) {
		this.id = id;
		this.type = type;
		this.pos = pos;
		this.bombermanId = 0;
	}

	ModelDTO(Integer id, Character type, Position pos, Integer bombermanId) {
		this.id = id;
		this.type = type;
		this.pos = pos;
		this.bombermanId = bombermanId;
	}

	public Integer getId() {
		return id;
	}

	public Character getType() {
		return type;
	}

	public Position getPos() {
		return pos;
	}

	public Integer getBombermanId() {
		return bombermanId;
	}

}
