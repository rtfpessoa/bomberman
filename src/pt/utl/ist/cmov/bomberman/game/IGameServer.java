package pt.utl.ist.cmov.bomberman.game;

import pt.utl.ist.cmov.bomberman.util.Direction;

public interface IGameServer {

	public void putBomberman(String username);

	public void putBomb(String username);

	public void pause(String username);

	public void quit(String username);

	public void move(String username, Direction direction);

}
