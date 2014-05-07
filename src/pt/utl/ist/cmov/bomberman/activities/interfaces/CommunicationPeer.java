package pt.utl.ist.cmov.bomberman.activities.interfaces;

import java.util.List;

import pt.utl.ist.cmov.bomberman.handlers.CommunicationManager;
import android.os.Handler;

public interface CommunicationPeer {
	public void addCommunicationManager(CommunicationManager cManager);

	public List<CommunicationManager> getCommunicationManagers();

	public Handler getHandler();
}
