package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;

import pt.utl.ist.cmov.bomberman.game.elements.Element;

public interface IGameClient {

	public void init(ArrayList<ArrayList<Element>> elements);

	public void updateScreen(ArrayList<Element> drawings);

	public void updatePlayers(HashMap<String, BombermanPlayer> players);

}
