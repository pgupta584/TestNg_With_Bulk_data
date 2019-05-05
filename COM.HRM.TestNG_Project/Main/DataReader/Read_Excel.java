package DataReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Read_Excel 
{
	public FileOutputStream fileout=null;
	public String path;
	public FileInputStream fis;
	public XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public XSSFRow row;
	public XSSFCell cell;

	@SuppressWarnings({ "deprecation", "resource" })
	public Object[][] getExcelDataBasedOnScenario(String excelpath,String sheetname,String testName) 
	{
		try 
		{
			String datasets[][]=null;
			FileInputStream file=new FileInputStream(new File(excelpath));
			
			XSSFWorkbook workbook= new XSSFWorkbook(file);
			XSSFSheet sheet=workbook.getSheet(sheetname);
			
			int totalrow =sheet.getLastRowNum();
			int totalCoulmn=0;
			
			Iterator<Row> rowIterator=sheet.iterator();
			int i=0;
			int count=1;
			
			while(rowIterator.hasNext() && count==1 || count==2) 
			{
				Row row=rowIterator.next();
				Iterator<Cell> cellIterator=row.cellIterator();
				int j=0;
				while(cellIterator.hasNext()) 
				{
					Cell cell=cellIterator.next();					
					if(cell.getStringCellValue().contains(testName+"end")) 
					{
						count=0;
						break;
					}					
					if(cell.getStringCellValue().contains(testName+"start")) 
					{							
						totalCoulmn=row.getPhysicalNumberOfCells()-1;				  
						datasets=new String[totalrow][totalCoulmn];
					}
					if(cell.getStringCellValue().contains(testName+"start") || count==2) 
					{
						count=2;
						
						switch(cell.getCellType()) //converting cell type data into String type
						{
						case Cell.CELL_TYPE_NUMERIC:
							datasets[i-1][j++]=cell.getStringCellValue().trim();
							//System.out.println(cell.getNumericCellValue());
							break;
							
						case Cell.CELL_TYPE_STRING:
							if(!cell.getStringCellValue().contains(testName+"start")) 
							{
							datasets[i-1][j++]=cell.getStringCellValue().trim();
							//System.out.println(cell.getStringCellValue());
							}
							break;
							
						case Cell.CELL_TYPE_BOOLEAN:
							datasets[i-1][j++]=cell.getStringCellValue().trim();
							//System.out.println(cell.getStringCellValue());
							break;
							
						case Cell.CELL_TYPE_FORMULA:
							datasets[i-1][j++]=cell.getStringCellValue().trim();
							//System.out.println(cell.getStringCellValue());
							break;
						}	
					}
				}					
			    // System.out.println("");
			     i++;					
			}
			file.close();
			//return datasets;//--Parsing not required(If data is not in proper structure(like un-structured data) then we need to do parsing else not req.)
								//Here we are already set the data and CELL_TYPE-int,string so parsing not req.
			return parseData(datasets, totalCoulmn);//To ignore space between two scenario in excel
		} 
		catch (Exception e) 
		{			
			e.printStackTrace();
		} 
		return null;
	}
	//Because user input is nearly always in the form of strings. 
	//Before you can convert it to a Java data type that you can actually work with, you usually need to parse it.
	public Object[][] parseData(Object[][] data,int colsize)//Stroing all data in big 2d Arraylist by converting it into String
	{		
		ArrayList<ArrayList<String>> list =new ArrayList<ArrayList<String>>();
		
		ArrayList<String> list1;
		
		//System.out.println(data.length);
		
		for(int i=0;i<data.length;i++) 
		{				
			list1= new ArrayList<String>();
			
			for(int j=0;j<data[i].length;j++) {
				
				if(data[i][j] != null) 
				{
					list1.add((String) data[i][j]);
				}					
			}
			
			if(list1.size()>0) 
			{
				list.add(list1);
			}
			
		}
		Object[][] arr2d=new Object[list.size()][colsize];
		
		for(int i=0;i< list.size();i++) 
		{
			ArrayList<String> t= list.get(i);				
			for(int j=0;j<t.size();j++) 
			{
				arr2d[i][j]=t.get(j);
			}
		}
		return arr2d;
		}
}
