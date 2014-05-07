package pt.utl.ist.cmov.bomberman.game;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import pt.utl.ist.cmov.bomberman.game.models.BombModel;
import pt.utl.ist.cmov.bomberman.game.models.BombermanModel;
import pt.utl.ist.cmov.bomberman.game.models.EmptyModel;
import pt.utl.ist.cmov.bomberman.game.models.ExplosionModel;
import pt.utl.ist.cmov.bomberman.game.models.Model;
import pt.utl.ist.cmov.bomberman.game.models.ObstacleModel;
import pt.utl.ist.cmov.bomberman.game.models.RobotModel;
import pt.utl.ist.cmov.bomberman.game.models.WallModel;
import pt.utl.ist.cmov.bomberman.util.Position;

public class GameMap {

	public static final Character WALL = 'W';
	public static final Character OBSTACLE = 'O';
	public static final Character ROBOT = 'R';
	public static final Character BOMB = 'B';
	public static final Character EMPTY = '-';
	public static final Character EXPLODING = 'E';

	private List<List<Character>> mapInfo;
	private List<List<Model>> modelMap = new ArrayList<List<Model>>();
	private Integer height;
	private Integer width;
	private Context context;

	private Position bombermanToAdd = null;
	private Integer idBombermanToAdd = null;

	public GameMap(Context ctx, List<List<Character>> mapInfo) {
		super();
		this.context = ctx;
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
	
	public Model getModelContent(Position pos) {
		return this.modelMap.get(pos.y).get(pos.x);
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

	public void move(Position orig, Position dest) {
		Character content = this.mapInfo.get(dest.y).get(dest.x);
		this.mapInfo.get(dest.y).set(dest.x,
				this.mapInfo.get(orig.y).get(orig.x));
		this.mapInfo.get(orig.y).set(orig.x, content);

		Model model = this.modelMap.get(dest.y).get(dest.x);
		Model otherModel = this.modelMap.get(orig.y).get(orig.x);
		
		model.setPos(orig);
		otherModel.setPos(dest);
		
		this.modelMap.get(dest.y).set(dest.x, otherModel);
		this.modelMap.get(orig.y).set(orig.x, model);
	}

	public void putEmpty(Position pos) {
		this.mapInfo.get(pos.y).set(pos.x, EMPTY);
		this.modelMap.get(pos.y).set(pos.x, new EmptyModel());
	}

	public void putExploding(Position pos) {
		this.mapInfo.get(pos.y).set(pos.x, EXPLODING);
		this.modelMap.get(pos.y).set(pos.x,
				new ExplosionModel(context, pos.x, pos.y));
	}

	public void putBomb(Position pos) {
		this.mapInfo.get(pos.y).set(pos.x, BOMB);
		this.modelMap.get(pos.y).set(pos.x,
				new BombModel(context, pos.x, pos.y));
	}

	public void putBomberman(Position pos, int bombermanId) {
		if (this.modelMap.size() == 0) {
			this.bombermanToAdd = pos;
			this.idBombermanToAdd = bombermanId;
			return;
		}
		this.mapInfo.get(pos.y).set(pos.x, Character.forDigit(bombermanId, 10));
		this.modelMap.get(pos.y).set(pos.x,
				new BombermanModel(context, pos.x, pos.y, bombermanId));
	}

	public void parseMap() {
		for (int y = 0; y < height; y++) {
			List<Model> line = new ArrayList<Model>();

			for (int x = 0; x < width; x++) {
				Character c = mapInfo.get(y).get(x);

				if (c == WALL)
					line.add(new WallModel(this.context, x, y));
				else if (c == OBSTACLE)
					line.add(new ObstacleModel(this.context, x, y));
				else if (c == ROBOT)
					line.add(new RobotModel(this.context, x, y));
				else if (c == BOMB)
					line.add(new BombModel(this.context, x, y));
				else if (c == EMPTY)
					line.add(new EmptyModel());
				else if (c == EXPLODING)
					line.add(new ExplosionModel(this.context, x, y));
			}

			modelMap.add(line);
		}

		if (this.bombermanToAdd != null) {
			Position pos = this.bombermanToAdd;
			Integer id = this.idBombermanToAdd;
			this.bombermanToAdd = null;
			this.idBombermanToAdd = null;
			this.putBomberman(pos, id);
		}
	}

	public void draw(Canvas canvas) {
		for (List<Model> line : modelMap) {
			for (Model model : line) {
				model.draw(canvas);
			}
		}

	}

}
