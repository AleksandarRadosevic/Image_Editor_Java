import java.util.ArrayList;

public class Selection {

	private boolean active;
	private String name;
	protected ArrayList<Rectangle>vector_;
	private int picture_width;
	private int picture_height;
	public Selection(boolean active, String name, ArrayList<Rectangle> vector_, int picture_width, int picture_height) {
		super();
		this.active = active;
		this.name = name;
		this.vector_ = vector_;
		this.picture_width = picture_width;
		this.picture_height = picture_height;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPicture_width() {
		return picture_width;
	}
	public void setPicture_width(int picture_width) {
		this.picture_width = picture_width;
	}
	public int getPicture_height() {
		return picture_height;
	}
	public void setPicture_height(int picture_height) {
		this.picture_height = picture_height;
	}
	public void Delete() {
		active=false;
		vector_.clear();
	}
	
	
	
}
