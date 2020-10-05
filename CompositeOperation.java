import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompositeOperation extends Operation{

	protected ArrayList<Operation>operations=new ArrayList<Operation>();
	protected String filePath;
	public static ArrayList<CompositeOperation>compositeOperations=new ArrayList<CompositeOperation>();
	public CompositeOperation(String n) {
		super(n);
		compositeOperations.add(this);
		// TODO Auto-generated constructor stub
	}
	public CompositeOperation() {
		compositeOperations.add(this);
	}
	public void Add_operation(Operation o) {
		operations.add(o);
	}
	
	public void Read(String path) {
		
		boolean succReading=false;
		Pattern p = Pattern.compile("^.*fun$");
		Matcher m = p.matcher(path);
		if(m.matches()) {
			File file=new File(path);
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				filePath=path;
				String st;
				while (true) {
				st=br.readLine();
				st=br.readLine();
				st=br.readLine();
				p = Pattern.compile("^<name>([^<]*)</name>$");
				m=p.matcher(st);
				if(m.matches()) {
					name=m.group(1);
				}
				Pattern CheckBasic = Pattern.compile("^<operation>([^<]*)</operation>$");
				Pattern CheckComposite=Pattern.compile("^<comp operation>([^<]*)</comp operation>$");
				//check if basic or composite in composite
				
				
				while ((st=br.readLine())!=null) {
					m=CheckBasic.matcher(st);
					Matcher mComposite=CheckComposite.matcher(st);
					String nameStr;
					ArrayList<Integer>parameters=new ArrayList<Integer>();
					if (m.matches()) {
						String NameOp;
						nameStr=m.group(1);
						
						//check number of arguments
						Pattern OneArgument=Pattern.compile("^([^ ]*) ([0-9]*)$");
						Pattern ThreeArgument=Pattern.compile("^([^ ]*) ([^ ]*) ([^ ]*) ([0-9]*)$");
						Pattern TwoArgument=Pattern.compile("^([^ ]*) ([^ ]*) ([0-9]*)$");
						Matcher mThree=ThreeArgument.matcher(nameStr);
						Matcher mTwo=TwoArgument.matcher(nameStr);

						//check one
						m=OneArgument.matcher(nameStr);
						if (m.matches()) {
						//operation has one argument
						NameOp=m.group(1);
						int val=Integer.parseInt(m.group(2));
						parameters.add(val);
						}
						//check three
						else if (mThree.matches()) {
							NameOp=mThree.group(1);
							parameters.add(Integer.parseInt(mThree.group(2)));
							parameters.add(Integer.parseInt(mThree.group(3)));
							parameters.add(Integer.parseInt(mThree.group(4)));
						}
						//check if two 
						else if (mTwo.matches()) {
							NameOp=mTwo.group(1);
							parameters.add(Integer.parseInt(mTwo.group(2)));
							parameters.add(Integer.parseInt(mTwo.group(3)));		
						}
						else	//no argument operation 
						{
							NameOp=nameStr;
						}
						Operation op1=new Operation(NameOp);
						op1.arguments=parameters;
						operations.add(op1);
					}
					else if (mComposite.matches()) {
						String pathName=mComposite.group(1);
						CompositeOperation c2=new CompositeOperation();
						c2.Read(pathName);
						operations.add(c2);
					}
				}
				br.close();
				break;	
				}				
			} catch (FileNotFoundException e) {
				System.out.println("Zadati fajl nije pronadjen!");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
			
			
		}
		else System.out.println("Neispravan fajl");
		
		
	}
	public void ExportFunction(String filePath) {
		try {
			FileWriter fw = new FileWriter(filePath);
			fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			fw.write('\n');
			fw.write("<Composite>\n");
			fw.write("<name>"+this.name+"</name>\n");
			for (Operation op:operations) {
				if (op instanceof CompositeOperation) {
					for (Operation op2:((CompositeOperation) op).operations)
					WriteOneBasicOperation(fw, op2);
				}
				else {
					WriteOneBasicOperation(fw, op);
				}
			}
			fw.write("</Composite>");
			fw.close();
		} catch (IOException e) {
			System.out.println("Pogresna putanja zadata!");
			e.printStackTrace();
		}
	}
	public void WriteOneBasicOperation(FileWriter fw,Operation op) throws IOException {
		if (op.arguments.size()==0) {
			fw.write("<operation>"+op.name+"</operation>");
		}
		else if (op.arguments.size()==1) {
			fw.write("<operation>"+op.name+" "+op.arguments.get(0)+"</operation>");
		}
		else if (op.arguments.size()==3) {
			fw.write("<operation>"+op.name+" "+op.arguments.get(0)+" "+op.arguments.get(1)+" "+op.arguments.get(2)+"</operation>");
		}
		else if (op.arguments.size()==2) {
			fw.write("<operation>"+op.name+" "+op.arguments.get(0)+" "+op.arguments.get(1)+"</operation>");
		}
		fw.write('\n');
	}
}
