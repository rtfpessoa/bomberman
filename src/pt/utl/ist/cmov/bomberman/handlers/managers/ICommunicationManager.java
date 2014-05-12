package pt.utl.ist.cmov.bomberman.handlers.managers;

import java.util.ArrayList;

import pt.utl.ist.cmov.bomberman.handlers.channels.ICommunicationChannel;

public interface ICommunicationManager {

	public ArrayList<String> getChannelEndpoints();

	public void addCommChannel(ICommunicationChannel commChannel);

	public void receive(Object object);

	public void close();

}
