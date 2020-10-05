import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class BMPFormatter extends Formatter {

	@Override
	public void Read(String s) {
		File bmpFile=new File(s);
		try {
			filePath=s;
			int find=filePath.lastIndexOf(92);
			name=filePath.substring(find+1);
			System.out.println("IME BMP FORMATERA JE "+name);
			BufferedImage bf=ImageIO.read(bmpFile);
			int width=bf.getWidth();
			int height=bf.getHeight();
			Layer l=new Layer(width, height,100, true,true);		
			l.format="bmp";
			l.name=name;
			l.filePath=s;
			l.vector_=new ArrayList<Pixel>();
			for (int i=0;i<bf.getHeight();i++)
				for(int j=0;j<bf.getWidth();j++)
				{
					int col=bf.getRGB(j,i);
					short red=(short)((col&0xff0000)>>16);
					short blue=(short)(col&0xff);
					short green=(short)((col&0xff00)>>8);
					short alpha=(short)(256+((col&0xff000000)>>24));
					Pixel p1=new Pixel(red, green, blue, alpha);
					l.vector_.add(p1);
				}
			layers.add(l);
		} catch (IOException e) {
			System.out.println("Fajl sa zadatim imenom ne postoji!");
			
		}
					
		
		
	}

	@Override
	public void Export(String path) {
		Layer l=layers.get(0);
		
		int width=l.width;
		int height=l.height;
		int size=width*height;
		try {
			OutputStream d1 = new FileOutputStream(path);
			DataOutputStream outputStream=new DataOutputStream(d1);
		
			int data=0x4d42;
			int data_second;
			data=((data<<8)&(0x0000FF00))|(data>>8);
			outputStream.writeShort(data);
			data_second=122+size*4;
			//size
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);
			data_second=0;
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);
			
			
			data_second = 0x7A;
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);

			data_second = 0x6C;		
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);
			
			data_second = width;
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);

			data_second = height;
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);

			data = 1;
			data=((data<<8)&(0x0000FF00))|(data>>8);	
			outputStream.writeShort(data);

			data = 32;
			data=((data<<8)&(0x0000FF00))|(data>>8);
			outputStream.writeShort(data);

			data_second = 3;
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);	
			outputStream.writeInt(data_second);

			data_second = 122 + size * 4;
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);

			
			
			data_second = 2835;
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);
			outputStream.writeInt(data_second);
	
			data_second = 0;
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);
			outputStream.writeInt(data_second);
			
			data_second = 0x0000FF00;
			//data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);

			data_second = 0x00FF0000;
			//data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);

			data_second = 0xFF000000;
			//data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);

			data_second = 0x000000FF;
			//data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);

			data_second = 0x57696E20;
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			outputStream.writeInt(data_second);

			data_second = 0;
			data_second=(data_second<<24)|((data_second>>8)&(0x0000FF00))|((data_second<<8)&(0x00FF0000))|(data_second>>24);
			for (int i = 0; i < 9; i++)
			outputStream.writeInt(data_second);
			outputStream.writeInt(data_second);
			outputStream.writeInt(data_second);
			outputStream.writeInt(data_second);

			
			byte data2;
			int j=0;
			for (int y = 0; y < height; y++) {
				for (int i = size - width - j; i < size - j; i++)
				{
					data2=(byte) l.vector_.get(i).getBlue();
					outputStream.write(data2);
					data2=(byte) l.vector_.get(i).getGreen();
					outputStream.write(data2);
					data2=(byte) l.vector_.get(i).getRed();
					outputStream.write(data2);
					data2=(byte) l.vector_.get(i).getAlpha();
					outputStream.write(data2);
					
				}
				j+=width;
			}
			outputStream.close();
			System.out.println("Zavrseno eksportovanje");
			
			
			
			
			
			
			
			
			
			
			
		} catch (FileNotFoundException e) {
			System.out.println("Neuspesno cuvanje");
		//	e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Neuspesno cuvanje");
			e.printStackTrace();
		}
	}

}
