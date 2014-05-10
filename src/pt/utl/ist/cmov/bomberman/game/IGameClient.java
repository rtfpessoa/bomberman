package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;

import pt.utl.ist.cmov.bomberman.game.drawings.Drawing;

public interface IGameClient {

	public void init(Integer lines, Integer cols, ArrayList<Drawing> elements);

	public void updateScreen(ArrayList<Drawing> drawings);

	public void updatePlayers(HashMap<String, BombermanPlayer> players);

}
