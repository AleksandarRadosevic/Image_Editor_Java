import java.util.ArrayList;

public abstract class Formatter {

	public abstract void Read(String a);
	public abstract void Export(String a);
	
	protected ArrayList<Layer>layers=new ArrayList<Layer>();
	protected ArrayList<Operation>operations=new ArrayList<Operation>();
	protected ArrayList<Selection>selections=new ArrayList<Selection>();
	
	
	protected String name;
	protected String filePath;
}
