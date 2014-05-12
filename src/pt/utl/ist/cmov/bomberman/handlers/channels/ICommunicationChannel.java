package pt.utl.ist.cmov.bomberman.handlers.channels;


public interface ICommunicationChannel {

	public String getChannelEndpoint();

	public void send(Object object);

	public void close();

}
