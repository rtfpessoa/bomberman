package pt.utl.ist.cmov.bomberman.handlers.managers;

import pt.utl.ist.cmov.bomberman.handlers.CommunicationObject;
import pt.utl.ist.cmov.bomberman.handlers.channels.ICommunicationChannel;

public interface ICommunicationManager {
	
	public void addCommChannel(ICommunicationChannel commChannel);

	public void receive(CommunicationObject object);

}
