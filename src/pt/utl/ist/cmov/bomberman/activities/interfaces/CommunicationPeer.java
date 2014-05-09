package pt.utl.ist.cmov.bomberman.activities.interfaces;

import java.util.List;

import pt.utl.ist.cmov.bomberman.handlers.channels.SocketCommunicationChannel;
import android.os.Handler;

public interface CommunicationPeer {
	public void addCommunicationManager(SocketCommunicationChannel cManager);

	public List<SocketCommunicationChannel> getCommunicationManagers();

	public Handler getHandler();
}
