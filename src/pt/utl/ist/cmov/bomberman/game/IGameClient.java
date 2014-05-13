package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;

import pt.utl.ist.cmov.bomberman.game.dto.ModelDTO;

public interface IGameClient {

	public void init(Integer lines, Integer cols, ArrayList<ModelDTO> models);

	public void updateScreen(ArrayList<ModelDTO> models);

	public void updatePlayers(HashMap<String, BombermanPlayer> players);

	public void startServer(ArrayList<ModelDTO> models);

}
