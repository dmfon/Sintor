
package sintor;

import java.io.File;


class FileInformator
{
	public String pathOfProject;
	public String nameFile;
	public String nameFileWithoutPath;
	
	public FileInformator (String fileName)
	{
		/*String path = FileInformator.class.getResource("").toString();
		//System.out.println(path);
		char c1[];
		if (path.charAt(0) == 'j')
		{
			c1 = new char[path.length() - 11];
			for (int i = 0; i < path.length() - 11; i++)
			{
				c1[i] = path.charAt(i + 10);
				if (c1[i] == '/') c1[i] = '\\';
			}
		}
		else
		{
			c1 = new char[path.length() - 7];
			for (int i = 0; i < path.length() - 7; i++)
			{
				c1[i] = path.charAt(i + 6);
				if (c1[i] == '/') c1[i] = '\\';
			}
		}*/
		pathOfProject = new File("").getAbsolutePath();
		//pathOfProject = new String (c1);
		System.out.println(pathOfProject);
		nameFile = new String(pathOfProject + "\\audiofiles\\" + fileName);
		
		char c2[] = new char[fileName.length() - 4];
		for (int i = 0; i < fileName.length() - 4; i++)
		{
			c2[i] = fileName.charAt(i);
		}
		nameFileWithoutPath = new String(c2);
		
		//System.out.println(pathOfProject);
		//System.out.println(nameFileWithoutPath);
		//System.out.println(nameFile);
	}
	
	public String getPathFileForFolder (String nameFolder, String format)
	{
		//System.out.println(nameFileWithoutPath);
		return new String(pathOfProject + "\\" + nameFolder + "\\" + nameFileWithoutPath + "." + format);
	}
	
	public String getAPathFileForFolder (String nameFolder, String format)
	{
		//System.out.println(nameFileWithoutPath);
		String pathClass = new File("").getAbsolutePath();
		return new String(pathClass + "\\" + nameFolder + "\\" + nameFileWithoutPath + "." + format);
	}
	
	public String getAPathFileForFolder (String nameFolder, String name, String format)
	{
		//System.out.println(nameFileWithoutPath);
		String pathClass = new File("").getAbsolutePath();
		return new String(pathClass + "\\" + nameFolder + "\\" + name + "." + format);
	}
		
}
