package pt.utl.ist.cmov.bomberman.game;

import android.os.Parcel;
import android.os.Parcelable;

public class BombermanPlayer implements Parcelable {

	private Integer id;
	private String username;
	private Integer score;
	private Integer time;
	private Integer players;

	public BombermanPlayer(Parcel in) {
		id = in.readInt();
		username = in.readString();
		score = in.readInt();
		time = in.readInt();
		players = in.readInt();
	}

	public BombermanPlayer(Integer id, String username) {
		this.id = id;
		this.username = username;
		this.score = 0;
		this.time = 0;
		this.players = 0;
	}

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	public void addToScore(Integer points) {
		this.score += points;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getPlayers() {
		return players;
	}

	public void setPlayers(Integer players) {
		this.players = players;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(username);
		dest.writeInt(score);
		dest.writeInt(time);
		dest.writeInt(players);
	}

	public static final Parcelable.Creator<BombermanPlayer> CREATOR = new Parcelable.Creator<BombermanPlayer>() {
		public BombermanPlayer createFromParcel(Parcel in) {
			return new BombermanPlayer(in);
		}

		public BombermanPlayer[] newArray(int size) {
			return new BombermanPlayer[size];
		}
	};

}
