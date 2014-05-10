package pt.utl.ist.cmov.bomberman.handlers;

import java.io.Serializable;

public class CommunicationObject implements Serializable {

	private static final long serialVersionUID = -3749194449598125192L;

	public static final String DEBUG = "pt.utl.ist.cmov.bomberman.DEBUG";

	/* Server */
	public static final String INIT = "pt.utl.ist.cmov.bomberman.INIT";
	public static final String PUT_BOMBERMAN = "pt.utl.ist.cmov.bomberman.PUT_BOMBERMAN";
	public static final String PUT_BOMB = "pt.utl.ist.cmov.bomberman.PUT_BOMB";
	public static final String PAUSE = "pt.utl.ist.cmov.bomberman.PAUSE";
	public static final String QUIT = "pt.utl.ist.cmov.bomberman.QUIT";
	public static final String MOVE = "pt.utl.ist.cmov.bomberman.MOVE";

	/* Client */
	public static final String UPDATE_SCREEN = "pt.utl.ist.cmov.bomberman.UPDATE_SCREEN";
	public static final String UPDATE_PLAYERS = "pt.utl.ist.cmov.bomberman.UPDATE_PLAYERS";

	private String type;
	private Object message;

	public CommunicationObject(String type, Object message) {
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public Object getMessage() {
		return message;
	}

}
