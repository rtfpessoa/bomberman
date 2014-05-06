package pt.utl.ist.cmov.bomberman.game;

import java.util.List;

import pt.utl.ist.cmov.bomberman.util.Position;

public class GameMap {

	public static final Character WALL = 'W';
	public static final Character OBSTACLE = 'O';
	public static final Character ROBOT = 'R';
	public static final Character BOMB = 'B';
	public static final Character EMPTY = '-';
	public static final Character EXPLODING = 'E';

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

	public Character getContent(Position pos) {
		return this.mapInfo.get(pos.y).get(pos.x);
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

	public boolean hasBomberman(Position pos) {
		int res = Character.getNumericValue(this.getContent(pos));
		return res > 0;
	}

	public boolean hasBomberman(Integer x, Integer y) {
		int res = Character.getNumericValue(this.getContent(x, y));
		return res > 0;
	}

	public void move(Position orig, Position dest) {
		Character content = this.mapInfo.get(dest.y).get(dest.x);
		this.mapInfo.get(dest.y).set(dest.x,
				this.mapInfo.get(orig.y).get(orig.x));
		this.mapInfo.get(orig.y).set(orig.x, content);
	}

	public void putEmpty(Position pos) {
		this.mapInfo.get(pos.y).set(pos.x, EMPTY);
	}
	
	public void putEmpty(Integer x, Integer y) {
		this.mapInfo.get(y).set(x, EMPTY);
	}
	
	public void putExploding(Position pos) {
		this.mapInfo.get(pos.y).set(pos.x, EXPLODING);
	}
	
	public void putExploding(Integer x, Integer y) {
		this.mapInfo.get(y).set(x, EXPLODING);
	}

	public void putBomb(Position pos) {
		this.mapInfo.get(pos.y).set(pos.x, BOMB);
	}
	
	public void putBomb(Integer x, Integer y) {
		this.mapInfo.get(y).set(x, BOMB);
	}

}
