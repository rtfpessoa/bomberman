package pt.utl.ist.cmov.bomberman.network.channel;


public interface ICommunicationChannel {

	public String getChannelEndpoint();

	public void send(Object object);

	public void close();

}
