package pt.utl.ist.cmov.bomberman.network.channel;

import pt.utl.ist.cmov.bomberman.network.proxy.ICommunicationProxy;

public class FakeCommunicationChannel implements ICommunicationChannel {

	private ICommunicationProxy outCommManager;

	public FakeCommunicationChannel(ICommunicationProxy outCommManager) {
		this.outCommManager = outCommManager;
	}

	@Override
	public void send(Object object) {
		outCommManager.receive(object);
	}

	@Override
	public void close() {
	}

	@Override
	public String getChannelEndpoint() {
		return null;
	}

}
