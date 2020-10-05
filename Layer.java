import java.util.ArrayList;

public class Layer {

	protected ArrayList<Pixel>vector_=new ArrayList<Pixel>();
	private boolean active;
	private boolean visible;
	protected String format;
	protected String filePath;
	protected String name;
	int width;
	int height;
	int opacity;
	private boolean created=false;
	Layer(int w,int h,int o,Boolean act,Boolean vis){
		width=w;
		height=h;
		opacity=o;
		active=act;
		visible=vis;
		
		
	}
	Layer (Layer a){
		for (Pixel p:a.vector_) {
			Pixel z=new Pixel(p.getRed(),p.getGreen(),p.getBlue(),p.getAlpha());
			vector_.add(z);
		}
		width=a.width;
		height=a.height;
		opacity=a.opacity;
		active=a.active;
		format=a.format;
		filePath=a.filePath;
		name=a.name;
	}
	
	Layer(Formatter f){
		Layer l=Image.CompressInOneLayer(f.layers);
		filePath=f.filePath;
		name=f.name;
		this.width=l.getWidth();
		this.height=l.getHeight();
		vector_=l.vector_;
		if (f instanceof BMPFormatter) {
			format="bmp";
		}
		else if (f instanceof PAMFormatter)
			format="pam";
		else format="xml";
	}
		
	
	
	public boolean isActive() {
		return active;
	}
	
	
	public boolean isCreated() {
		return created;
	}
	
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getOpacity() {
		return opacity;
	}
	
	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}
	
	
	
}
