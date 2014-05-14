package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;

import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;
import android.net.wifi.p2p.WifiP2pDevice;

public interface IGameClient {

	void init(Integer lines, Integer cols, ArrayList<ModelDTO> models);

	void updateScreen(ArrayList<ModelDTO> models);

	void updatePlayers(HashMap<String, BombermanPlayer> players);

	void startServer(String username, String levelName, Integer width,
			Integer height, ArrayList<ModelDTO> models,
			ArrayList<WifiP2pDevice> players);

	void confirmQuit(String oldServer, String newServer);

}
