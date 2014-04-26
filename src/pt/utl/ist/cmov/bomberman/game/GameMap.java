package pt.utl.ist.cmov.bomberman.game;

import java.util.List;

public class GameMap {

	public static final Character WALL = 'W';
	public static final Character OBSTACLE = 'O';
	public static final Character ROBOT = 'R';
	public static final Character EMPTY = '-';

	private List<List<Character>> mapInfo;
	private Integer height;
	private Integer width;

	public GameMap(List<List<Character>> mapInfo) {
		super();
		this.mapInfo = mapInfo;
		this.height = mapInfo.size();
		this.width = mapInfo.get(0).size();
	}

	public Character getContent(Integer x, Integer y) {
		return this.mapInfo.get(y).get(x);
	}

	public List<List<Character>> getMapInfo() {
		return mapInfo;
	}

	public void setMapInfo(List<List<Character>> mapInfo) {
		this.mapInfo = mapInfo;
	}

	public Integer getHeight() {
		return height;
	}

	public Integer getWidth() {
		return width;
	}

}