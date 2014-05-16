package pt.utl.ist.cmov.bomberman.game;

import pt.utl.ist.cmov.bomberman.util.Direction;

public interface IGameServer {

	void putBomberman(String username);

	void putBomb(String username);

	void pause(String username);

	void quit(String username);

	void split(String username);

	void move(String username, Direction direction);

}
