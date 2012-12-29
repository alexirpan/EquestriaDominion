package framework;

import java.util.ArrayList;

public class TextLogger 
{
	public static final ArrayList<String> TEXT_LOG = new ArrayList<String>();
	public static int indentAmount = 0;
	
	public static void record(String msg)
	{
		String indent = "";
		for (int i = 0; i < indentAmount; i++)
		{
			indent += "    ";
		}
		TEXT_LOG.add(indent + msg);
	}
	
	public static void emptyLog()
	{
		TEXT_LOG.clear();
	}
	
	public static void increaseIndent(int n)
	{
		indentAmount += n;
	}
	
	public static void setIndent(int n)
	{
		indentAmount = n;
	}
}
