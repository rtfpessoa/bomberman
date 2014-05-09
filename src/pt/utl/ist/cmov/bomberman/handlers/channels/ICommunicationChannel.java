package pt.utl.ist.cmov.bomberman.handlers.channels;

import pt.utl.ist.cmov.bomberman.handlers.CommunicationObject;

public interface ICommunicationChannel {

	public void send(CommunicationObject object);

}
