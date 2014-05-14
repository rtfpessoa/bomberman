package pt.utl.ist.cmov.bomberman.network.channel;

import java.io.IOException;


public interface ICommunicationChannel {

	public String getChannelEndpoint();

	public void send(Object object) throws IOException;

	public void close();

}
