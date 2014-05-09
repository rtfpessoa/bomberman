package pt.utl.ist.cmov.bomberman.handlers.channels;

import pt.utl.ist.cmov.bomberman.handlers.CommunicationObject;
import pt.utl.ist.cmov.bomberman.handlers.managers.ICommunicationManager;

public class FakeCommunicationChannel implements ICommunicationChannel {

	private ICommunicationManager serverCommManager;
	private ICommunicationManager clientCommManager;

	public FakeCommunicationChannel(ICommunicationManager serverCommManager,
			ICommunicationManager clientCommManager) {
		this.serverCommManager = serverCommManager;
		this.clientCommManager = clientCommManager;
	}

	@Override
	public void send(CommunicationObject object) {
		serverCommManager.deliver(object);
		clientCommManager.deliver(object);
	}

}
