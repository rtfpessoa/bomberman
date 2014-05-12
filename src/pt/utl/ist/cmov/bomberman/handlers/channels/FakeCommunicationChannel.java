package pt.utl.ist.cmov.bomberman.handlers.channels;

import pt.utl.ist.cmov.bomberman.handlers.managers.ICommunicationManager;

public class FakeCommunicationChannel implements ICommunicationChannel {

	private ICommunicationManager outCommManager;

	public FakeCommunicationChannel(ICommunicationManager outCommManager) {
		this.outCommManager = outCommManager;
	}

	@Override
	public void send(Object object) {
		outCommManager.receive(object);
	}

	@Override
	public void close() {
	}

}
