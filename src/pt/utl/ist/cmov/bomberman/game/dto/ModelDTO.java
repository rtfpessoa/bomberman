package pt.utl.ist.cmov.bomberman.game.dto;

import pt.utl.ist.cmov.bomberman.util.Position;
import android.os.Parcel;
import android.os.Parcelable;

public class ModelDTO implements Parcelable {

	private final Integer id;
	private final Character type;
	private final Position pos;
	private final Integer bombermanId;

	public ModelDTO(Parcel in) {
		id = in.readInt();
		type = in.readString().charAt(0);
		pos = (Position) in.readSerializable();
		bombermanId = in.readInt();
	}

	public ModelDTO(Integer id, Character type, Position pos) {
		this.id = id;
		this.type = type;
		this.pos = pos;
		this.bombermanId = 0;
	}

	public ModelDTO(Integer id, Character type, Position pos,
			Integer bombermanId) {
		this.id = id;
		this.type = type;
		this.pos = pos;
		this.bombermanId = bombermanId;
	}

	public Integer getId() {
		return id;
	}

	public Character getType() {
		return type;
	}

	public Position getPos() {
		return pos;
	}

	public Integer getBombermanId() {
		return bombermanId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(type.toString());
		dest.writeSerializable(pos);
		dest.writeInt(bombermanId);
	}

	public static final Parcelable.Creator<ModelDTO> CREATOR = new Parcelable.Creator<ModelDTO>() {
		public ModelDTO createFromParcel(Parcel in) {
			return new ModelDTO(in);
		}

		public ModelDTO[] newArray(int size) {
			return new ModelDTO[size];
		}
	};

}
