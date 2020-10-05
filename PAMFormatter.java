import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.print.DocFlavor.BYTE_ARRAY;

public class PAMFormatter extends Formatter {

	@Override
	public void Read(String a) {

		try {
			InputStream inputStream=new FileInputStream(a);
			filePath=a;
			int find=filePath.lastIndexOf(92);
			name=filePath.substring(find+1);
			
			int byteRead;
			//First space
			while ((byteRead=inputStream.read())!=' ') {		
				
			}
			int width=0;
			int height=0;
			byteRead=inputStream.read();
			
			//Set Width
			while (byteRead!='\n'){
				int val=byteRead-'0';
				width=width*10+val;
				byteRead=inputStream.read();
			}
			//Space after width
			while (byteRead!=' '){
			byteRead=inputStream.read();
			}
			
			byteRead=inputStream.read();
			
			//Set Height
			while (byteRead!='\n'){
				int val=byteRead-'0';
				height=height*10+val;
				byteRead=inputStream.read();
			}
			byteRead=inputStream.read();
			int cnt=0;
			while (cnt<4) {
				byteRead=inputStream.read();
				if (byteRead=='\n') {
					cnt++;
				}
			}
			Layer l=new Layer(width,height,100,true,true);
			l.format="pam";
			l.name=name;
			l.filePath=a;
			for (int i=0;i<width*height;i++) {
				byteRead=inputStream.read();
				short red=(short)byteRead;
				byteRead=inputStream.read();
				short green=(short)byteRead;
				byteRead=inputStream.read();
				short blue=(short)byteRead;
				byteRead=inputStream.read();
				short alpha=(short)byteRead;
				Pixel p1=new Pixel(red, green, blue, alpha);
				l.vector_.add(p1);
			}
			layers.add(l);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

	@Override
	public void Export(String path) {
		OutputStream d1;
		try {
			d1 = new FileOutputStream(path);
			DataOutputStream outputStream=new DataOutputStream(d1);
			byte[] b="P7\nWIDTH ".getBytes();
			outputStream.write(b);

			
			int width=layers.get(0).width;
			
			int i=0;
		   			
			while (width>0) {
				byte bb=(byte) (width%10+'0');
				width/=10;
				b[i]=bb;
				i++;
			}
			for (int j=0;j<i/2;j++) {
				byte bb=b[j];
				b[j]=b[i-j-1];
				b[i-j-1]=bb;
			}
			b[i]='\n';
			for (int j=0;j<=i;j++) {
				outputStream.write(b[j]);
			}
			
			
			b="HEIGHT ".getBytes();
			outputStream.write(b);
			int height=layers.get(0).height;
			i=0;
			while (height>0) {
				byte bb=(byte) (height%10+'0');
				height/=10;
				b[i]=bb;
				i++;
			}
			for (int j=0;j<i/2;j++) {
				byte bb=b[j];
				b[j]=b[i-j-1];
				b[i-j-1]=bb;
			}
			b[i]='\n';
			for (int j=0;j<=i;j++) {
				outputStream.write(b[j]);
			}
			

			
			b="DEPTH 4\nMAXVAL 255\nTUPLTYPE RGB_ALPHA\nENDHDR\n".getBytes();
			outputStream.write(b);

			
			Layer l=layers.get(0);
			byte bb;
			for (i=0;i<l.vector_.size();i++) {
				bb=(byte) l.vector_.get(i).getRed();
				outputStream.write(bb);
				bb=(byte) l.vector_.get(i).getGreen();
				outputStream.write(bb);
				bb=(byte) l.vector_.get(i).getBlue();
				outputStream.write(bb);
				bb=(byte) l.vector_.get(i).getAlpha();
				outputStream.write(bb);
			}
			outputStream.close();
			System.out.println("Zavrsen upis");
			
			
		} catch (FileNotFoundException e) {
			System.out.println("Fajl nije pronadjen!");
			
		} catch (IOException e) {
				System.out.println("Neuspesno cuvanje");
				//e.printStackTrace();
		}
		
		
		
		
		
	}

}
