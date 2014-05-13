package pt.utl.ist.cmov.bomberman.network.proxy;

import java.util.ArrayList;

import pt.utl.ist.cmov.bomberman.network.channel.ICommunicationChannel;

public interface ICommunicationProxy {

	public ArrayList<String> getChannelEndpoints();

	public void addCommChannel(ICommunicationChannel commChannel);

	public void receive(Object object);

	public void close();

}
