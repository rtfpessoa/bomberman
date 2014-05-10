package pt.utl.ist.cmov.bomberman.game;

import java.util.HashMap;
import java.util.List;

import pt.utl.ist.cmov.bomberman.game.elements.Element;

public interface IGameClient {

	public void init(List<List<Element>> elements);

	public void updateScreen(List<Element> drawings);

	public void updatePlayers(HashMap<String, BombermanPlayer> players);

}
