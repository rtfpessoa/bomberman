package pt.utl.ist.cmov.bomberman.network;

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
	public static final String START_SERVER = "pt.utl.ist.cmov.bomberman.START_SERVER";

	private final String type;
	private final String message;
	private final String extraMessage;
	private final String obj;

	public CommunicationObject(String type, String message) {
		this.type = type;
		this.message = message;
		this.extraMessage = null;
		this.obj = null;
	}

	public CommunicationObject(String type, String message, String extraMessage) {
		this.type = type;
		this.message = message;
		this.extraMessage = extraMessage;
		this.obj = null;
	}

	public CommunicationObject(String type, String message,
			String extraMessage, String obj) {
		this.type = type;
		this.message = message;
		this.extraMessage = extraMessage;
		this.obj = obj;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public String getExtraMessage() {
		return extraMessage;
	}

	public String getObject() {
		return obj;
	}

}
