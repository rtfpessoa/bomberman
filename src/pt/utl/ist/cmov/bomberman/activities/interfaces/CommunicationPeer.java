package pt.utl.ist.cmov.bomberman.activities.interfaces;

import pt.utl.ist.cmov.bomberman.handlers.CommunicationManager;
import android.os.Handler;

public interface CommunicationPeer {
	public void setCommunicationManager(CommunicationManager cManager);

	public CommunicationManager getCommunicationManager();

	public Handler getHandler();
}
