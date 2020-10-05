import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Main extends JFrame{
	
	private CheckboxGroup states;
	private static Image image;
	private static String dialogStr;
	private JCheckBox opacity1;
	private JCheckBox opacity2;
	private Checkbox empty;
	private Checkbox nempty;
	protected boolean isSelection=false;
	private int xStart,yStart;
	private int xEnd,yEnd;
	private String nameSelection="name";
	private static int ValueSelection=0;
	private ArrayList<Rectangle>rectangles=new ArrayList<Rectangle>();
	public static int CompCreated=0;
	
	
	Main(){

		setSize(1200, 800);
		 addWindowListener(new WindowAdapter() {
			 @Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		 InicijalizujMeni();
		 	this.setVisible(true);
		 	this.setLocationRelativeTo(null);
		 	
		 	
		 	
		 	
		 	addMouseListener(new MouseAdapter() {
		 		@Override
		 		public void mousePressed(MouseEvent e) {
		 			int x=e.getX();
		 			int y=e.getY();
		 				 			
		 			SetStartCoordinates(x,y);
		 		}
		 		@Override
		 		public void mouseReleased(MouseEvent e) {
		 			int x=e.getX();
		 			int y=e.getY();
		 			SetEndCoordinates(x, y);
		 			if (isSelection)
		 				CreateRectangle();
		 		}
			});
		 	
	}
	
	private void SetStartCoordinates(int x,int y) {
		xStart=x;
		yStart=y;
	}
	
	private void SetEndCoordinates(int x,int y) {
		xEnd=x;
		yEnd=y;
	}
	
	public static void main(String[]args) {
		Main c=new Main();
	
		
	}
	
	private void InicijalizujMeni() {
		states=new CheckboxGroup();
		MenuBar DropDownMenu=new MenuBar();
		Menu menu=new Menu("Fajl");
		menu.add("Ucitaj sliku");
		menu.add("Sacuvaj sliku");
	
		
		Menu meni2=new Menu("Prikaz podataka");
		meni2.add("Prikazi sliku");
		meni2.add("Prikazi lejere");
		meni2.add("Prikazi selekcije");
		meni2.add("Prikazi operacije");
		
		
		Menu menu3=new Menu("Kreiraj");
		menu3.add("Sloj");
		menu3.add("Selekcija");
		menu3.add("Kompozitna operacija");
		
		
		Menu menu4=new Menu("Sacuvaj");
		menu4.add("BMP");
		menu4.add("PAM");
		menu4.add("XML");
		
		
		DropDownMenu.add(menu);
		DropDownMenu.add(meni2);
		DropDownMenu.add(menu3);
		DropDownMenu.add(menu4);
		
		
		this.setMenuBar(DropDownMenu);

	menu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand().equals("Ucitaj sliku")){
						ImportPicture();
						if (image!=null)
							PaintImage();
					}
		else if(e.getActionCommand().equals("Sacuvaj sliku")) {
			SavePicture();
		}
			}
			
		});
	
	meni2.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Prikazi sliku")){
					PaintImage();
				}
				else if(e.getActionCommand().equals("Prikazi lejere")) {
					DisplayLayers();
				}
				else if (e.getActionCommand().equals("Prikazi selekcije")) {
					DisplaySelections();
				}
				else if (e.getActionCommand().equals("Prikazi operacije"))  {
					DisplayOperations();
					
				}
				
		}
	});


	
	menu3.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Sloj")){
					CreateLayer();
				}
	else if(e.getActionCommand().equals("Selekcija")) {
		CreateSelecion();
	}
		else if (e.getActionCommand().equals("Kompozitna operacija")){
		CreateCompositeOperation();
	}
		}
	});
	
	
	menu4.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser(  );
		    chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		    chooser.showSaveDialog( null );
		    System.out.println( chooser.getSelectedFile() );
		    
		    
		    
				if (e.getActionCommand().equals("BMP")){
					
					Layer layer=Image.CompressInOneLayer(image.layers);
					BMPFormatter bmp=new BMPFormatter();
					bmp.layers.add(layer);
					
					bmp.Export(chooser.getSelectedFile().getAbsolutePath());
				  
				}
				else if(e.getActionCommand().equals("PAM")) {
					Layer layer=Image.CompressInOneLayer(image.layers);
					PAMFormatter pam=new PAMFormatter();
					pam.layers.add(layer);
					pam.Export(chooser.getSelectedFile().getAbsolutePath());
					}
				else if (e.getActionCommand().equals("XML")){
					
				}
			
				
				
		}
	});
	
	}
	
	
	private void ImportPicture() {
		JFrame fr=new JFrame();
		fr.setSize(265,75);
		JButton btn1=new JButton("Otvori");
		JButton btn2=new JButton("Sacuvaj");
		fr.setLayout(new GridLayout(1,2,0,0));

		fr.add(btn1);
		fr.add(btn2);
		fr.setVisible(true);
		fr.setLocationRelativeTo(null);
		
		btn1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String str=Create_Dialog();
				SetDialogString(str);		
							
			}
		});
		
		
		btn2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 String s=GetDialogString();
					Formatter f=null;
					StringBuilder str=new StringBuilder();
					str.append(s.charAt(s.length()-3));
					str.append(s.charAt(s.length()-2));
					str.append(s.charAt(s.length()-1));
					String str2=new String(str);
					if (str2.equals("bmp")) {
						f=new BMPFormatter();
						
					}
					else if(str2.equals("pam")) {
						f=new PAMFormatter();
					}
					else if(str2.equals("xml")) {
						f=new MyFormatter();
					}
					if (f!=null) {
						f.Read(s);		
						Layer l1=new Layer(f);
					
						l1.setOpacity(100);
						l1.setActive(true);
						l1.setVisible(true);
						if (image==null) {
							image=new Image(f);
						}
						else image.Add_layer(l1);
						fr.dispose();
					
					
					}	 	
					}
		});
	}

	
	
	
	
	
	
	
	
	private void SavePicture() {
		 JFrame fr=new JFrame("Sacuvaj sliku");
		 fr.setSize(300,200);
		 fr.setBackground(Color.BLUE);
		 CheckboxGroup rezimi=new CheckboxGroup();
		 Checkbox bmp=new Checkbox("bmp",rezimi,true);
		 Checkbox pam=new Checkbox("pam",rezimi,false);
		 Checkbox xml=new Checkbox("xml",rezimi,false);
		 Button btn1=new Button("Izaberi");
		 JPanel p1=new JPanel();
		 
		 p1.add(bmp); 
		 p1.add(pam);
		 p1.add(xml);
		 p1.add(btn1);
		
		 fr.add(p1,BorderLayout.CENTER);
		 btn1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (bmp.getState()==true) {
					image.SaveImage(0);
				}
				else if (pam.getState()==true) {
					image.SaveImage(1);
				}
				else image.SaveImage(2);
				
			}
		});
		fr.setVisible(true);
	}
	
	//CREATE SECTION
	private void SetDialogString(String s) {
		dialogStr=s;
	}
	private String GetDialogString() {
		return dialogStr;
	}
	
	private void CreateLayer() {
		 JFrame frame=new JFrame("Kreiraj sloj");
		 
		 JLabel label1=new JLabel("Vidljivost (0-100)");
		 TextField txtOpacity=new TextField();
		 txtOpacity.setSize(100, 30);
		 
		 opacity1=new JCheckBox("Vidljivost u izvrsavanju operacija");
		 opacity2=new JCheckBox("Vidljivost u prikazivanju slike");
		 

		 CheckboxGroup rezimi=new CheckboxGroup();
		 empty=new Checkbox("Prazan",rezimi,true);
		 nempty=new Checkbox("Neprazan",rezimi,false);
		 Button btn1=new Button("Otvori");
		 Button btn2=new Button("Sacuvaj");
		 
		 
		 btn1.setSize(100,50);
		 btn2.setSize(100,50);
		 JPanel p1=new JPanel();
		 JPanel p2=new JPanel();
		 p2.add(label1);
		 p2.add(txtOpacity);
		 p2.add(opacity1);
		 p2.add(opacity2);
		 
		 p1.add(empty);
		 p1.add(nempty);
		 p1.add(btn1);
		 p2.add(btn2);
		 frame.add(p1,BorderLayout.NORTH);
		 frame.add(p2,BorderLayout.CENTER);
		 btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (empty.getState()==true) {
					
				}
				else
				{
				String str=Create_Dialog();
				SetDialogString(str);				
				}
				
			}
		});
		 btn2.addActionListener(new ActionListener() {
			 @Override
				public void actionPerformed(ActionEvent e) {
				 if (empty.getState()==false) {
				 String s=GetDialogString();
					Formatter f=null;
					StringBuilder str=new StringBuilder();
					str.append(s.charAt(s.length()-3));
					str.append(s.charAt(s.length()-2));
					str.append(s.charAt(s.length()-1));
					String str2=new String(str);
					if (str2.equals("bmp")) {
						System.out.println("BMP");
						f=new BMPFormatter();
						
					}
					else if(str2.equals("pam")) {
						System.out.println("PAM");
						f=new PAMFormatter();
					}
					else if(str2.equals("xml")) {
						f=new MyFormatter();
					}
					if (f!=null) {
						f.Read(s);		
						Layer l1=new Layer(f);
					int val=Integer.parseInt(txtOpacity.getText());
					if (val<0 ||val>100)
						System.out.println("Prozirnost mora biti u opsegu 0-100");
					else
					{
						l1.setOpacity(val);
						l1.setActive(opacity1.isSelected());
						l1.setVisible(opacity2.isSelected());
						if (image==null) {
							image=new Image(l1);
						}
						else image.Add_layer(l1);
						frame.dispose();
					}
					
			 }
					}
				 else {
					 CreateLayerEmpty();
					 }
		 
			 }
		 });
		 
		 
		 frame.setSize(300,200);
		 frame.setLocationRelativeTo(null);
		 frame.setVisible(true);
		 
	}
	
	private void CreateLayerEmpty() {
		Frame fr=new Frame();
		Button btn1=new Button("Potvrdi");
		Button btn2=new Button("");
		Label l1=new Label("Visina: ");
		Label l2=new Label("Sirina: ");
		TextField t1=new TextField();
		TextField t2=new TextField();
		Panel p1=new Panel();
		p1.setLayout(new GridLayout(1,2,0,0));
		p1.add(l1);
		p1.add(t1);
		
		Panel p2=new Panel();
		p2.setLayout(new GridLayout(1,2,0,0));
		p2.add(l2);
		p2.add(t2);
		fr.setLayout(new GridLayout(3,1,0,0));
		fr.add(p1);
		fr.add(p2);
		fr.add(btn1);
		fr.setSize(300,200);
		fr.setLocationRelativeTo(null);
		fr.setVisible(true);
		
		btn1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String st=t1.getText();
				int width=Integer.parseInt(st);
				int height=Integer.parseInt(t2.getText());
				if (width>0 && width<1920 && height>0 && height<1080) {
					Layer layer=new Layer(width, height, 0, true, true);
					JFileChooser chooser = new JFileChooser(  );
				    chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
				    chooser.showSaveDialog(null);
				    
				    String s=chooser.getSelectedFile().getAbsolutePath();
				    
				    StringBuilder str=new StringBuilder();
					str.append(s.charAt(s.length()-3));
					str.append(s.charAt(s.length()-2));
					str.append(s.charAt(s.length()-1));
					String str2=new String(str);
					if (str2.equals("bmp")) {
						layer.format="bmp";
					}
					else if(str2.equals("pam")) {
					layer.format="pam";
					}
					layer.filePath=s;
					int find=s.lastIndexOf(92);
					layer.name=s.substring(find+1);
					for (int i=0;i<width*height;i++) {
						layer.vector_.add(new Pixel((short)0,(short) 0, (short)0,(short)255));
					}
					if (image==null) {
						image=new Image(layer.width,layer.height);
					}
					image.Add_layer(layer);
				}
			}
			
		});
	}
	
	private String Create_Dialog() {
		JFileChooser j = new JFileChooser(); 
		j.showSaveDialog(null); 
		j.setVisible(true);
		String s=null;
		if (j.getSelectedFile()!=null)
		s=j.getSelectedFile().getAbsolutePath();
		return s;
		
	}
	
	private void PaintImage() {
		if (image!=null) {
		 image.Paint(this);
		 this.setLocationRelativeTo(null);
		 this.setVisible(true);
		}
		else System.out.println("Slika nije ucitana");
	}
	
	private void DisplayLayers() {
		Frame fr=new Frame("Prikaz lejera");
		fr.setSize(300,250);
		fr.setBackground(Color.GREEN);
		if (image.layers.size()>0) {
		Button btn1=new Button("Prikazi");
			
		TextField txtF=new TextField();

		Checkbox []arr=new Checkbox[image.layers.size()];
		CheckboxGroup listLayers=new CheckboxGroup();
		Panel p1=new Panel();
		p1.setLayout(new GridLayout(image.layers.size()+1,1,5,5));
		Panel p2=new Panel();
		txtF.setSize(50,30);
		
		Label lblTxtf=new Label("Prozirnost");
		p2.setLayout(new GridLayout(2,1,0,0));
		p2.add(txtF,BorderLayout.NORTH);
		p2.add(lblTxtf,BorderLayout.SOUTH);
		
		
		Label lbl1=new Label("Aktivnost");
		Label lbl2=new Label("Vidljivost");
		Checkbox isVisile=new Checkbox();
		Checkbox isActive=new Checkbox();
		Button ChangeLayer=new Button("Promeni");
		Button DeleteLayer=new Button("Izbrisi lejer");
		
		Panel p3=new Panel();
		p3.setLayout(new GridLayout(3,2,0,15));
		p3.add(isVisile,BorderLayout.CENTER);
		p3.add(lbl1,BorderLayout.CENTER);
		p3.add(isActive,BorderLayout.CENTER);
		p3.add(lbl2,BorderLayout.CENTER);
		p3.add(ChangeLayer,BorderLayout.CENTER);
		p3.add(DeleteLayer,BorderLayout.CENTER);
		
		Panel p4=new Panel();
		p4.setLayout(new GridLayout(2,1,0,15));
		
		p4.add(p2,BorderLayout.NORTH);
		p4.add(p3,BorderLayout.CENTER);
		
		
		
		
		for (int i=0;i<image.layers.size();i++) {
			String str="Layer "+i;
			if (i==0)
			arr[i]=new Checkbox(str,listLayers,true);
			else 
			arr[i]=new Checkbox(str,listLayers,false);
		
			p1.add(arr[i],BorderLayout.WEST);
		}
		p1.add(btn1,BorderLayout.WEST);
		fr.add(p1,BorderLayout.WEST);
		fr.add(p4,BorderLayout.EAST);
		fr.setVisible(true);
		fr.addWindowListener(new WindowAdapter() {
			 @Override
			public void windowClosing(WindowEvent e) {
				fr.dispose();
			}
		});
		
		btn1.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				DrawOneLayer(arr);
							
			}
		});
		
		ChangeLayer.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				String str=txtF.getText();
				int LayerChange=0;
				for (int i=0;i<arr.length;i++) {
					if (arr[i].getState()==true) {
						LayerChange=i;
						break;
					}
				}
				if (!str.equals("")) {
					int val=Integer.parseInt(str);
					if (val>=0 && val<=100) 
					{
						image.layers.get(LayerChange).setOpacity(val);
					}
					
				}
					image.layers.get(LayerChange).setActive(isActive.getState());	
					image.layers.get(LayerChange).setVisible(isVisile.getState());		
					isActive.setState(false);
					isActive.repaint();
					isVisile.setState(false);
					isVisile.repaint();
					txtF.setText("");
					txtF.repaint();
					fr.setVisible(true);
					fr.repaint();
					
			}
		});
		
		DeleteLayer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int LayerChange=0;
				for (int i=0;i<arr.length;i++) {
					if (arr[i].getState()==true) {
						LayerChange=i;
						break;
					}
				}
				image.layers.remove(LayerChange);
				
			}
		});
	}
	
	
	}
	
	private void DisplaySelections() {
		Frame fr=new Frame("Prikaz selekcija");
		fr.setSize(300,250);
		fr.setBackground(Color.CYAN);
		if (image.selections.size()>0) {
		Button btn11=new Button("Prikazi");
			

		Checkbox []arr=new Checkbox[image.selections.size()];
		CheckboxGroup listLayers=new CheckboxGroup();
		Panel p1=new Panel();
		p1.setLayout(new GridLayout(image.selections.size()+1,1,5,5));
		Panel p2=new Panel();
		p2.setLayout(new GridLayout(2,1,0,30));
		
		Label lbl2=new Label("Vidljivost");
		Checkbox isVisile=new Checkbox();
		isVisile.setState(true);
		Button ChangeLayer=new Button("Promeni");
		Button deleteSelection=new Button("Izbrisi");
		
		Panel p3=new Panel();
		p3.add(lbl2);
		p3.add(isVisile,BorderLayout.CENTER);
		
		Panel p4=new Panel();
		p4.add(ChangeLayer);
		p4.add(deleteSelection);
		p2.add(p3,BorderLayout.NORTH);
		p2.add(p4,BorderLayout.SOUTH);
		
		for (int i=0;i<image.selections.size();i++) {
			String str="Selection "+i;
			if (i==0)
			arr[i]=new Checkbox(str,listLayers,true);
			else 
			arr[i]=new Checkbox(str,listLayers,false);
		
			p1.add(arr[i],BorderLayout.WEST);
		}
		p1.add(btn11,BorderLayout.WEST);
		fr.add(p1,BorderLayout.WEST);
		fr.add(p2,BorderLayout.EAST);
		fr.setVisible(true);
		fr.addWindowListener(new WindowAdapter() {
			 @Override
			public void windowClosing(WindowEvent e) {
				fr.dispose();
			}
		});
				
		ChangeLayer.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				int LayerChange=0;
				for (int i=0;i<arr.length;i++) {
					if (arr[i].getState()==true) {
						LayerChange=i;
						break;
					}
				}
					image.selections.get(LayerChange).setActive(arr[LayerChange].getState());
			}
		});
		
		deleteSelection.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int LayerChange=0;
				for (int i=0;i<arr.length;i++) {
					if (arr[i].getState()==true) {
						LayerChange=i;
						break;
					}
				}
				image.selections.remove(LayerChange);
				
				
			}
		});
		
		fr.setVisible(true);
		btn11.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int LayerChange=0;
				for (int i=0;i<arr.length;i++) {
					if (arr[i].getState()==true) {
						LayerChange=i;
						break;
					}
				}
				ShowSelection(LayerChange);
				
			}
		});
			
		}
	
	}
	Graphics ContextGraphics()
	{
		return this.getGraphics();
	}
	
	
	
	
	
	
	
	
	private void DrawOneLayer(Checkbox[]arr) {
		int j=0;
		for (int i=0;i<arr.length;i++) {
			if (arr[i].getState()==true)
			{
				j=i;
				break;
			}
		}
		
		image.DrawLayer(this,j);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.repaint();
	}
	
	private void CreateSelecion() {
		isSelection=true;
		PaintImage();
		JFrame fr=new JFrame();
		JButton btn1=new JButton("Zavrsi kreiranje selekcija");
		fr.setSize(100,100);
		fr.add(btn1);
		btn1.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				fr.dispose();
				FinishSelection();
			}
		});
		fr.setVisible(true);
	}
	private void CreateRectangle() {
	
		if (xStart<0 || xEnd<0 || xStart>image.getWidth() || xEnd>image.getWidth()|| yEnd<50|| yStart<50
				|| yEnd>image.getHeight()+50 ||
				yStart>image.getHeight()+50)
			System.out.println("Pravougaonik izlazi iz opsega");
		else {
			drawDashedRectangle(this.getGraphics());
			Rectangle rec=new Rectangle(xStart, yStart-50,xEnd-xStart,yEnd-yStart);
			System.out.println(xStart);
			System.out.println(yStart-50);
			System.out.println(xEnd);
			System.out.println(yEnd-50);
			rectangles.add(rec);
		
		}
	}
	private void FinishSelection() {
		Selection sel=new Selection(true,nameSelection+""+ValueSelection, rectangles, image.getWidth(),image.getHeight());
		rectangles=new ArrayList<Rectangle>();
		this.PaintImage();
		this.repaint();
		this.setLocationRelativeTo(null);
		image.selections.add(sel);
	}
	
	private void drawDashedRectangle(Graphics g){

		
		Graphics2D g2d = (Graphics2D) g.create();
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
        g2d.setStroke(dashed);
        
        if (xEnd<xStart) {
        	int temp=xStart;
        	xStart=xEnd;
        	xEnd=temp;
        }
        
        
        if (yEnd<yStart) {
        	int temp=yStart;
        	yStart=yEnd;
        	yEnd=temp;
        }
        
        g2d.drawRect(xStart, yStart,xEnd-xStart,yEnd-yStart);

        g2d.dispose();
}
	private void ShowSelection(int index) {
		Selection s=image.selections.get(index);
		for (Rectangle r:s.vector_) {
		xStart=r.getX();
		yStart=r.getY();
		xEnd=r.getX()+r.getWidth();
		yEnd=r.getY()+r.getHeight();
		drawDashedRectangle(ContextGraphics());
		}
		
	}
	private void CreateCompositeOperation() {
		int numberOfOperations=16+CompositeOperation.compositeOperations.size();
		Frame frame=new Frame();
		TextField InputValue=new TextField();
		TextField CompName=new TextField();

		Label lbl1=new Label("Vrednost argumenta");
		Label lbl2=new Label("Ime operacije");
 
		Button btn1=new Button("Dodaj operaciju");
		Button btn2=new Button("Zavrsi kreiranje operacije");
		CheckboxGroup operations=new CheckboxGroup();
		
		Checkbox[]basicOperations=new Checkbox[numberOfOperations];
		AddBasicOperations(basicOperations, null, operations);
		Panel p1=new Panel();
		p1.setLayout(new GridLayout(numberOfOperations,1,25,5));
		
		for (int i=0;i<numberOfOperations;i++) {
			p1.add(basicOperations[i]);
		}
		frame.setSize(400,350);
		frame.setLayout(new GridLayout(1,2,50,50));
		//
		
		
		Panel p2=new Panel();
		p2.setLayout(new GridLayout(numberOfOperations/2,1,5,5));
		p2.add(lbl1);
		p2.add(InputValue);
		p2.add(btn1);
		p2.add(lbl2);
		p2.add(CompName);
		p2.add(btn2);
		frame.add(p2);
		frame.add(p1);
		frame.setBackground(Color.YELLOW);
		CompositeOperation  composite=new CompositeOperation();
		btn1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String str=InputValue.getText();
				Pattern OneArgument=Pattern.compile("^([0-9]*)$");
				Pattern ThreeArgument=Pattern.compile("^([0-9^ ]*) ([0-9^ ]*) ([0-9]*)$");
				for (int i=0;i<numberOfOperations;i++) {
					if (basicOperations[i].getState()==true) {
						//check if need one argument
						Operation op1=null;
						if (i<8 || (i>8 && i<11)) {
							Matcher m=OneArgument.matcher(str);
							if (m.matches()) {
								String name=basicOperations[i].getLabel();
								int arg=Integer.parseInt(m.group(1));
								 op1=new Operation();
								op1.name=name;
								op1.arguments.add(arg);
							}
						}
						else if(i==15) {
							Matcher m=ThreeArgument.matcher(str);
							if (m.matches()) {
								String name=basicOperations[i].getLabel();
								
								 op1=new Operation();
								op1.name=name;
								op1.arguments.add(Integer.parseInt(m.group(1)));
								op1.arguments.add(Integer.parseInt(m.group(2)));
								op1.arguments.add(Integer.parseInt(m.group(3)));

							}
						}
						else if (i<15){
							String name=basicOperations[i].getLabel();
							op1=new Operation();
							op1.name=name;
						}
						else {
							op1=CompositeOperation.compositeOperations.get(i-16);
						}
						if (op1!=null)
						{
							composite.operations.add(op1);
						}
						
					}
				}
				InputValue.setText("");
				InputValue.revalidate();
			}
		});
		btn2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				composite.name=CompName.getText();
				 CheckComposite(composite);
				frame.dispose();
			}
		});
		
		frame.addWindowListener(new WindowAdapter() {
			 @Override
			public void windowClosing(WindowEvent e) {
				 CheckComposite(composite);
				frame.dispose();
			}
		});
		frame.setVisible(true);
	}
	void CheckComposite(CompositeOperation c) {
		if (c.operations.size()==0)
			CompositeOperation.compositeOperations.remove(c);
	}
	
	void AddBasicOperations(Checkbox[]bOp,String []bOpNames,CheckboxGroup group) {
	bOp[0]=new Checkbox("add", true, group);
	bOp[1]=new Checkbox("sub", false, group);
	bOp[2]=new Checkbox("mul", false, group);
	
	bOp[3]=new Checkbox("div", false, group);

	bOp[4]=new Checkbox("invsub", false, group);
	bOp[5]=new Checkbox("invdiv", false, group);
	bOp[6]=new Checkbox("pow", false, group);
	bOp[7]=new Checkbox("log", false, group);

	bOp[8]=new Checkbox("abs", false, group);
	
	bOp[9]=new Checkbox("min", false, group);
	bOp[10]=new Checkbox("max", false, group);

	bOp[11]=new Checkbox("inverse", false, group);

	bOp[12]=new Checkbox("grayscale", false, group);
	bOp[13]=new Checkbox("blackandwhite", false, group);
	bOp[14]=new Checkbox("median", false, group);
	bOp[15]=new Checkbox("fill", false, group);
	
	
	for (int i=16;i<16+CompositeOperation.compositeOperations.size();i++) {
		bOp[i]=new Checkbox(CompositeOperation.compositeOperations.get(i-16).name,false,group);
	}
		
	}
	void CreateComp(CompositeOperation s) {
		s=new CompositeOperation();
	}
	void DisplayOperations() {
		int numberOfOperations=16+CompositeOperation.compositeOperations.size();
		Button btn1=new Button("Izvrsi operaciju");
		TextField txt1=new TextField();
		Frame frame=new Frame();
		CheckboxGroup operations=new CheckboxGroup();
		
		Checkbox[]basicOperations=new Checkbox[numberOfOperations];
		AddBasicOperations(basicOperations, null, operations);
		Panel p1=new Panel();
		p1.setLayout(new GridLayout(numberOfOperations,1,25,5));
		
		for (int i=0;i<numberOfOperations;i++) {
			p1.add(basicOperations[i]);
		}
		frame.setSize(400,350);
		frame.setLayout(new GridLayout(1,2,50,50));
		Panel p2=new Panel();
		p2.setLayout(new GridLayout(numberOfOperations/2,1,5,5));
		p2.add(txt1);
		p2.add(btn1);
		frame.add(p1);
		frame.add(p2);
		
		btn1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
								String str=txt1.getText();
								Pattern OneArgument=Pattern.compile("^([0-9]*)$");
								Pattern ThreeArgument=Pattern.compile("^([0-9^ ]*) ([0-9^ ]*) ([0-9]*)$");
								for (int i=0;i<numberOfOperations;i++) {
									if (basicOperations[i].getState()==true) {
										//check if need one argument
										Operation op1=null;
										if (i<8 || (i>8 && i<11)) {
											Matcher m=OneArgument.matcher(str);
											if (m.matches()) {
												String name=basicOperations[i].getLabel();
												int arg=Integer.parseInt(m.group(1));
												 op1=new Operation();
												op1.name=name;
												op1.arguments.add(arg);
											}
										}
										else if(i==15) {
											Matcher m=ThreeArgument.matcher(str);
											if (m.matches()) {
												String name=basicOperations[i].getLabel();
												
												 op1=new Operation();
												op1.name=name;
												op1.arguments.add(Integer.parseInt(m.group(1)));
												op1.arguments.add(Integer.parseInt(m.group(2)));
												op1.arguments.add(Integer.parseInt(m.group(3)));

											}
										}
										else if (i<15){
											String name=basicOperations[i].getLabel();
											op1=new Operation();
											op1.name=name;
										}
										else {
											op1=CompositeOperation.compositeOperations.get(i-16);
										}
										if (op1!=null)
										image.AddAndExecute(op1);
							}
						}
					}		
		});
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			 @Override
			public void windowClosing(WindowEvent e) {
				frame.dispose();
			}
		});
	}
	
	
	
}
