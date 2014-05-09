package pt.utl.ist.cmov.bomberman.handlers;

public class CommunicationObject {

	public static final String PUT_BOMBERMAN = "pt.utl.ist.cmov.bomberman.PUT_BOMBERMAN";

	public static final String UPDATE_SCREEN = "pt.utl.ist.cmov.bomberman.UPDATE_SCREEN";

	public static final String DEBUG = "pt.utl.ist.cmov.bomberman.DEBUG";

	public static final String INIT = "pt.utl.ist.cmov.bomberman.INIT";

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
