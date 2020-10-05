import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.PortableInterceptor.SUCCESSFUL;

public class MyFormatter extends Formatter {

	protected static boolean isExported=false;
	protected static String pathExport;
	protected String compositePathExport;
	 public MyFormatter() {
		
	}
	
	
	
	
	
	
	
	@Override
	public void Read(String path) {
		
			
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			filePath=path;
			int find=filePath.lastIndexOf(92);
			name=filePath.substring(find+1);
			String st;
			st=br.readLine();
			st=br.readLine();
			st=br.readLine();
			st=br.readLine();

			Pattern layerCheck=Pattern.compile("^<layer>([^ ]*) ([0-1]) ([0-1]).*\\<\\/layer>$");
			while (true) {
				
				Pattern myFormat=Pattern.compile("^.*xml$");
				
				Matcher mFormatMatch=myFormat.matcher(st);
				Matcher layerMatch=layerCheck.matcher(st);
				if (mFormatMatch.matches()) {
					MyFormatter m=new MyFormatter();
					m.Read(path);
					for (Layer l:m.layers) {
						this.layers.add(l);
					}
					
					for (Operation op:m.operations) {
						this.operations.add(op);
					}
					for (Selection s:m.selections) {
						selections.add(s);
					}
				}
				else {
					if (layerMatch.matches()) {
						String fileP=layerMatch.group(1);
						int act=Integer.parseInt(layerMatch.group(2));
						Boolean active=false;
						if (act==1)
							active=true;
						 act=Integer.parseInt(layerMatch.group(3));
						Boolean visible=false;
						if (act==1)
							visible=true;
						Formatter f=null;
						StringBuilder str=new StringBuilder();
						str.append(fileP.charAt(fileP.length()-3));
						str.append(fileP.charAt(fileP.length()-2));
						str.append(fileP.charAt(fileP.length()-1));
						String str2=new String(str);
						if (str2.equals("bmp")) {
							System.out.println("BMP");
							f=new BMPFormatter();
							
						}
						else if(str2.equals("pam")) {
							System.out.println("PAM");
							f=new PAMFormatter();
						}
						if (f!=null) {
							f.Read(fileP);		
							Layer l1=new Layer(f);
							l1.setActive(active);
							l1.setVisible(visible);
							layers.add(l1);
						}
					}
					else break;
				}
				st=br.readLine();				
			}
			st=br.readLine();	
			st=br.readLine();	
			while (!st.equals("</selections>")) {
				Boolean active=null;
				st=br.readLine();
				Pattern selName=Pattern.compile("^<name>([^<]*)</name>$");
				String nameS=null;
				Matcher mName=selName.matcher(st);
				if (mName.matches()) {
					nameS=mName.group(1);
				}
				st=br.readLine();
				Pattern activeCheck=Pattern.compile("^<active>.*([0-1]).*</active>$");
				mName=activeCheck.matcher(st);
				if (mName.matches()) {
					active=Boolean.parseBoolean(mName.group(1));
				}
				ArrayList<Rectangle>rectangles=new ArrayList<Rectangle>();
				while (!st.equals("</select>")) {
					st=br.readLine();
					int x,y,width,height;
					Pattern rectangleCheck=Pattern.compile("^<rectangle>([^ ]*) ([^ ]*) ([^ ]*) ([^ ]*).*</rectangle>$");
					Matcher rec=rectangleCheck.matcher(st);
					if (rec.matches()) {
						x=Integer.parseInt(rec.group(1));
						y=Integer.parseInt(rec.group(2));
						width=Integer.parseInt(rec.group(3));
						height=Integer.parseInt(rec.group(4));
						Rectangle r=new Rectangle(x, y, width, height);
						rectangles.add(r);
					}
				}
				Selection s=new Selection(active, nameS, rectangles, 0, 0);
				selections.add(s);
				st=br.readLine();
			}
			st=br.readLine();
			st=br.readLine();
			while (!st.equals("</composite>")) {
				Pattern compositeRead=Pattern.compile("^<comp>([^<]*)</comp>$");
				Matcher m=compositeRead.matcher(st);
				if (m.matches()) {
					String fpath=m.group(1);
					CompositeOperation c=new CompositeOperation();
					c.Read(fpath);
					operations.add(c);
				}
				st=br.readLine();
				}
				st=br.readLine();
				if (st.equals("</Image>")|| st.equals("</image>"))
					System.out.println("Uspesno procitan fajl!");
			br.close();
			
			
		} catch (FileNotFoundException e) {
				System.out.println("Fajl nije pronadjen");
				e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		
		
		
		
		
		
		
	}

	@Override
	public void Export(String path) {
	
		
		try {
			
			
			FileWriter fw = new FileWriter(path);
			fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			isExported=true;
			pathExport=path;
			
			
			
			String bmpPath;
			int index=path.lastIndexOf(92);
			bmpPath=path.substring(0, index+1);
			System.out.println(bmpPath);
			fw.write('\n');			
			fw.write("<Image>\n");
			fw.write("<layers>\n");
			for (Layer l:layers) {
				Formatter f=null;
				if (l.format.equals("pam")) {
					f=new PAMFormatter();
					f.layers.add(l);
					
					f.Export(bmpPath+l.name+".pam");
					
				}
				else {
					f=new BMPFormatter();
					f.layers.add(l);
					f.Export(bmpPath+l.name);
					
				}
				int isvis=l.isVisible()?1:0;
				int isact=l.isActive()?1:0;
				fw.write("<layer>"+bmpPath+l.name+" "+isvis+" "+isact+"</layer>\n");
			}
			fw.write("</layers>\n");
			fw.write("<selections>\n");
			if (!selections.isEmpty()) {
				for (Selection s:selections) {
					fw.write("<select>\n");
					fw.write("<name>"+s.getName()+"</name>\n");
					int v=s.isActive()?1:0;
					fw.write("<active>"+v+"</active>\n");
					for (Rectangle r:s.vector_) {
						fw.write("<rectangle>");
						fw.write(r.getX()+" "+r.getY()+" "+r.getWidth()+" "+r.getHeight());
						fw.write("</rectangle>\n");
					}
					fw.write("</select>\n");
				}
			}
			fw.write("</selections>\n");
			fw.write("<composite>\n");
			CompositeOperation c=new CompositeOperation();
			CompositeOperation.compositeOperations.remove(c);
			c.name="Kompozitna"+Main.CompCreated+".fun";
			compositePathExport=bmpPath+c.name;
			Main.CompCreated++;
			for (Operation op:operations) {
				c.Add_operation(op);
			}
			if (!operations.isEmpty()) {
				
				c.ExportFunction(compositePathExport);
				fw.write("<comp>"+compositePathExport+"</comp>\n");
			}
			fw.write("</composite>\n");
			fw.write("</Image>");
			fw.close();
				
		} catch (FileNotFoundException e) {
			System.out.println("Fajl nije pronadjen");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
}
