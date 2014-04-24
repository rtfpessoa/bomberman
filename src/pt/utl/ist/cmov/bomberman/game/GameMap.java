package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;

public class GameMap {

	private ArrayList<ArrayList<Character>> mapInfo;
	private Integer height;
	private Integer width;

	public GameMap(ArrayList<ArrayList<Character>> mapInfo) {
		super();
		this.mapInfo = mapInfo;
		this.height = mapInfo.size();
		this.width = mapInfo.get(0).size();
	}

	public Character getContent(Integer x, Integer y) {
		return this.mapInfo.get(y).get(x);
	}

	public ArrayList<ArrayList<Character>> getMapInfo() {
		return mapInfo;
	}

	public void setMapInfo(ArrayList<ArrayList<Character>> mapInfo) {
		this.mapInfo = mapInfo;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

}
