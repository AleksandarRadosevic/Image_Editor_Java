import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Image {

	public JFrame frame;
	protected ArrayList<Layer>layers=new ArrayList<Layer>();
	protected ArrayList<Selection>selections=new ArrayList<Selection>();
	protected ArrayList<Operation>operations=new ArrayList<Operation>();
	private int width;
	private int height;
	protected BufferedImage img;
	public Image(int w,int h) {
		width=w;
		height=h;
		
	}
	public Image(Formatter f) {
		
			for (Layer l:f.layers) {
				layers.add(l);
			
			}
			if (f.operations.size()>0) {
				for (Operation op:f.operations)
					operations.add(op);
			}
			
			
			
			if (f.selections.size()>0) {
				for (Selection s:f.selections) {
					selections.add(s);
				}
			}
			
			
			width=layers.get(0).getWidth();
			height=layers.get(0).getHeight();
			//selekcije operacije
		
		
	}

	
	public Image(Layer l) {
		layers.add(l);
		width=l.getWidth();
		height=l.getHeight();
	}
	public void Add_layer(Layer layer_temp) {
		if (!layers.isEmpty()) {
			Layer first=layers.get(0);
			if (first.width>layer_temp.width) {
				int increment=first.width-layer_temp.width;
				int pos=layer_temp.width;
				for (int i=0;i<layer_temp.height;i++) {
					
					for (int j=increment;j>0;j--) {
						Pixel p1=new Pixel((short)0,(short) 0,(short) 0,(short) 0);
						layer_temp.vector_.add(pos,p1);
					}
				pos+=layer_temp.width+increment;
				}
				layer_temp.setWidth(first.width);
			}
			else if (first.width<layer_temp.width) {
				int increment=layer_temp.width-first.width;
				int pos=first.width;
				for (int i=0;i<first.height;i++)
				{
					for (int j=increment;j>0;j--) {
						Pixel p1=new Pixel((short)0,(short) 0,(short) 0,(short) 0);
						for (Layer l:layers) {
							l.vector_.add(pos,p1);
						}
					}
					pos+=first.width+increment;
				}
				for (Layer l:layers) {
					l.setWidth(layer_temp.width);
				}
			}
			if (first.height>layer_temp.height) {
				int difference=first.height-layer_temp.height;
				for (int i=0;i<difference;i++)
				{
					for (int j=0;j<layer_temp.width;j++)
						layer_temp.vector_.add(new Pixel((short)0, (short)0, (short)0,(short) 0));
				}
				layer_temp.setHeight(first.height);			
			}
			else if (first.height<layer_temp.height) {
				int difference=layer_temp.height-first.height;
				for (int i=0;i<difference;i++) {
					for (int j=0;j<layer_temp.width;j++) {
						for (Layer l:layers) {
							l.vector_.add(new Pixel((short)0, (short)0, (short)0,(short) 0));
						}
					}
				}
				for (Layer l:layers) {
					l.setHeight(layer_temp.height);
				}
			}		
		}
		width=layer_temp.width;
		height=layer_temp.height;
		layers.add(layer_temp);
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
	public void Paint(Main frame) {	
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Layer l=Image.CompressInOneLayer(layers);
		if (l!=null) {
		int i=0;
		int j=0;
		for (Pixel p:l.vector_) {
			int val=p.getAlpha();
			val<<=8;
			val|=p.getRed();
			val<<=8;
			val|=p.getGreen();
			val<<=8;
			val|=p.getBlue();	
			img.setRGB(j, i, val);
			j++;
			if (j==this.width) {
				j=0;
				i++;
			}
		}
		ImageIcon imgIcon=new ImageIcon(img);
		JLabel label=new JLabel();
		label.setIcon(imgIcon);
		if (frame.getContentPane().countComponents()>0)
		frame.getContentPane().remove(0);
		frame.getContentPane().add(label,BorderLayout.NORTH);
	    frame.setLocationRelativeTo(null);
	  
		}
		else {
			if (frame!=null && frame.getContentPane().countComponents()>0)
				frame.getContentPane().remove(0);
			frame.repaint();
			frame.setLocationRelativeTo(null);
		}
	}
	static Layer CompressInOneLayer(ArrayList<Layer>layers) {
		if (layers.size()>0) {
			Layer comp=null;
			int i = layers.size()-1;
			if(layers.get(i).isVisible()) {
				comp=new Layer(layers.get(i));
			}
		if (i>=1) {
		for (Layer temp=layers.get(i-1);i>0;i--)
		{
			
				
				int j = 0;
				if (temp.isVisible()&& comp!=null)
				for (Pixel iter : temp.vector_)
				{
					if (comp.vector_.get(j).getRed()==0) {
						comp.vector_.get(j).setRed(iter.getRed());
					}
					else 
					comp.vector_.get(j).setRed((short)((float)temp.getOpacity()/100*iter.getRed()*iter.getAlpha()/255+
							(1-((float)temp.getOpacity()/100)*((float)iter.getAlpha())/255)*comp.vector_.get(j).getRed()));
					
					if (comp.vector_.get(j).getGreen()==0) {
						comp.vector_.get(j).setGreen(iter.getGreen());
					}
					else 
					comp.vector_.get(j).setGreen((short)((float)temp.getOpacity()/100*iter.getGreen()*iter.getAlpha()/255+
							(1-((float)temp.getOpacity()/100)*((float)iter.getAlpha()/255))*comp.vector_.get(j).getGreen()));

					if (comp.vector_.get(j).getBlue()==0) {
						comp.vector_.get(j).setBlue(iter.getBlue());
					}
					else 
					comp.vector_.get(j).setBlue((short)((float)temp.getOpacity()/100*iter.getBlue()*iter.getAlpha()/255+
							(1-((float)temp.getOpacity()/100)*((float)iter.getAlpha()/255))*comp.vector_.get(j).getBlue()));
					if (comp.vector_.get(j).getAlpha()==0)
					{
						if (iter.getAlpha()!=0)
							comp.vector_.get(j).setAlpha(iter.getAlpha());
					}
					j++;
					
				}
				if (comp==null && temp.isVisible()) {
					comp=new Layer(temp);

				}
		}
		}
		return comp;
		
		}
		
		return null;
		
	}
	public void SaveImage(int val) {
		//0-BMP
		//1-PAM
		//2-MyFormatter
		switch (val) {
		case 0:{
			BMPFormatter bmpF=new BMPFormatter();
			CompressInOneLayer(layers);
		//	bmpF.Export(path);
			}
			break;
		case 1:System.out.println("PAM");
			break;
		case 2: System.out.println("XML");
			break;
		default:
			break;
		}
	}
	
	
	
	public void DrawLayer(Main frame,int k) {
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Layer l=layers.get(k);
		int i=0;
		int j=0;
		for (Pixel p:l.vector_) {
			int val=p.getAlpha();
			val<<=8;
			val|=p.getRed();
			val<<=8;
			val|=p.getGreen();
			val<<=8;
			val|=p.getBlue();	
			img.setRGB(j, i, val);
			j++;
			if (j==this.width) {
				j=0;
				i++;
			}
		}
		ImageIcon imgIcon=new ImageIcon(img);
		JLabel label=new JLabel();
		label.setIcon(imgIcon);
		if (frame!=null && frame.getContentPane().countComponents()>0)
		{
			while (frame.getContentPane().countComponents()>0) {
				frame.getContentPane().remove(0);
			}
			
		}
		frame.getContentPane().add(label,BorderLayout.NORTH);
	    frame.setLocationRelativeTo(null);
	    try {
	    	File outputFile=new File("aca.bmp");
			if (ImageIO.write(img, "bmp", outputFile)==true)
				System.out.println("SACUVANO");
			else System.out.println("NESACUVANO");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void AddAndExecute(Operation op) {
		operations.add(op);
		MyFormatter f=new MyFormatter();
		for (Layer l:layers)
			f.layers.add(l);
		for (Operation op2:operations) {
			f.operations.add(op2);
		}
		for (Selection s:selections)
			f.selections.add(s);
		
		if (MyFormatter.isExported)
		f.Export(MyFormatter.pathExport);
		
		else {
			JFileChooser chooser = new JFileChooser(  );
		    chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		    chooser.showSaveDialog( null );
			f.Export(chooser.getSelectedFile().getAbsolutePath());
		}
			ExecuteOperation("",MyFormatter.pathExport,f.compositePathExport);
			f.layers.clear();
			f.operations.clear();
			f.selections.clear();
			f.Read(MyFormatter.pathExport);
			int i=0;
			layers.clear();
			for (Layer l:f.layers) {
				layers.add(l);
			}
		
		}
					
		private void ExecuteOperation(String str,String fileP,String compPath) {
			String strX="D:\\Desktop\\PRVI_PROJEKAT_IZ_POOP-a.exe "+fileP+" "+compPath;
					Runtime runtime=Runtime.getRuntime();
				
						Process p;
						try {
							p = runtime.exec(strX);
							System.out.println("U toku je izvrsavanje operacije...");
							p.waitFor();
							System.out.println("Operacija je uspesno izvrsena!");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						}
		
		
	}
	
}
