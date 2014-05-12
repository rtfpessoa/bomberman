package pt.utl.ist.cmov.bomberman.game.dto;

import pt.utl.ist.cmov.bomberman.game.Level;
import pt.utl.ist.cmov.bomberman.game.model.BombermanModel;
import pt.utl.ist.cmov.bomberman.game.model.Model;

public class ModelDTOFactory {

	public static ModelDTO create(Model model) {
		if (model.getType() == Level.BOMBERMAN) {
			BombermanModel bomberman = (BombermanModel) model;

			return new ModelDTO(model.getId(), model.getType(), model.getPos(),
					bomberman.getBombermanId());
		} else {
			return new ModelDTO(model.getId(), model.getType(), model.getPos());

		}
	}

}
