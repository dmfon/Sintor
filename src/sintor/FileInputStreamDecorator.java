/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sintor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class FileInputStreamDecorator extends FileInputStream
{
	private int countSymbolsIsRead;
		public FileInputStreamDecorator (String name) throws FileNotFoundException
		{
			super(name);
			countSymbolsIsRead = 0;
		}
		
		@Override
		 public int read() throws IOException
		{
			countSymbolsIsRead++;
			return super.read();
		}
		 
		 public int getCount()
		 {
			 return countSymbolsIsRead;
		 }
}
