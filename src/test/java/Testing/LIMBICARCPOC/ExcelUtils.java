package Testing.LIMBICARCPOC;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	TestController tc=new TestController();
	public ArrayList<Map<String, String>> expectedResultsArrayList ( String expectedResultsFilename, String worksheetName) throws Exception {
			int columnCount = 0;
			String sourceFilePath;
	        // Location of the source file
			
				sourceFilePath = "./src/test/resources/"+ expectedResultsFilename;
	        String sheetName = worksheetName;
	        FileInputStream fileInputStream = null;
	        // Array List to store the excel sheet data
	        ArrayList<Map<String, String>> excelData = new ArrayList<Map<String, String>>();
	        //HashMap for column name / value pair
	        Map<String, String> resultsMap = null;
	        String[] expectedResultsSplit = expectedResultsFilename.split("\\.");
	        String extension = expectedResultsSplit[expectedResultsSplit.length-1];
	        //HANDLES XLS AND XLSX
			if (extension.equals("xls")) {
				//List to store cell data for a row 
		        List<HSSFCell> cellData = new ArrayList<HSSFCell>();
		        //List to store column names 
		        List<HSSFCell> columnName = new ArrayList<HSSFCell>();
		        try {
		            // FileInputStream to read the excel file
		            fileInputStream = new FileInputStream(sourceFilePath);
		            // Create an excel workbook
		            HSSFWorkbook excelWorkBook = new HSSFWorkbook(fileInputStream);
		            // Retrieve the sheet from the workbook by name.
		            HSSFSheet excelSheet = excelWorkBook.getSheet(sheetName);
		            /*
		             * Iterate through the sheet rows and cells. First row will be seen as the column names.
		             * Stores key/value pairs for each cell after the first row and then stores in excelData ArrayList.  
		             */
		            // Store the retrieved data in an arrayList
		            Iterator<?> rows = excelSheet.iterator();
		            HSSFRow row = null;
		            Iterator<?> cells = null;
		            HSSFCell cell = null;
		            String columnKey = "";
		            String columnValue = "";
		            while (rows.hasNext()) {
		            	resultsMap = new HashMap<String, String>(); //new resultsMap for each row
		            	row = (HSSFRow) rows.next();
		                cells = row.cellIterator();
		                if (row.getRowNum() == 0) { //first row in excel is for column names
		                	while (cells.hasNext()) {
			                    cell = (HSSFCell) cells.next();
			                    columnName.add(cell);
			                    columnCount++;
			                }
		                } else {
		                	String emptyRowCheck = "";
		                	while (cells.hasNext()) { //Rows in excel with deleted values are still seen as rows
		                		cell = (HSSFCell) cells.next();
		                		emptyRowCheck = emptyRowCheck + cell;
		                	}
		                	if (!emptyRowCheck.equals("")) {
				                for (int i = 0; i < columnCount; i++) { //for all columns in the row based on columnCount
				                	columnKey = columnName.get(i).toString();
				                	columnValue = "";
				                	cell = row.getCell(i);
				                	if (cell != null) {
				                		cell.setCellType(Cell.CELL_TYPE_STRING); //converts cell type to string to prevent values from returning as other types (ie number 4 returns as 4.0)
				                		columnValue = cell.toString(); //gets cell value as a string if cell is not null; else it remains blank string
				                	}
				                    cellData.add(cell);
				                    resultsMap.put(columnKey, columnValue); //stores key/value in resultsMap
				                }
				                cellData.clear();
				                excelData.add(resultsMap);
		                	}

		                }
		            }
		            excelWorkBook.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } finally {
		            if (fileInputStream != null) {
		                fileInputStream.close();
		            }
		        }
			}
			else if (extension.equals("xlsx")) {
				//List to store cell data for a row 
		        List<XSSFCell> cellData = new ArrayList<XSSFCell>();
		        //List to store column names 
		        List<XSSFCell> columnName = new ArrayList<XSSFCell>();
		        try {
		            // FileInputStream to read the excel file
		            fileInputStream = new FileInputStream(sourceFilePath);
		            // Create an excel workbook
		            XSSFWorkbook excelWorkBook = new XSSFWorkbook(fileInputStream);
		            // Retrieve the sheet from the workbook by name.
		            XSSFSheet excelSheet = excelWorkBook.getSheet(sheetName);
		            /*
		             * Iterate through the sheet rows and cells. First row will be seen as the column names.
		             * Stores key/value pairs for each cell after the first row and then stores in excelData ArrayList.  
		             */
		            // Store the retrieved data in an arrayList
		            Iterator<?> rows = excelSheet.iterator();
		            XSSFRow row = null;
		            Iterator<?> cells = null;
		            XSSFCell cell = null;
		            String columnKey = "";
		            String columnValue = "";
		            while (rows.hasNext()) {
		            	resultsMap = new HashMap<String, String>(); //new resultsMap for each row
		                row = (XSSFRow) rows.next();
		                cells = row.cellIterator();
		                if (row.getRowNum() == 0) { //first row in excel is for column names
		                	while (cells.hasNext()) {
			                    cell = (XSSFCell) cells.next();
			                    columnName.add(cell);
			                    columnCount++;
			                }
		                } else {
		                	String emptyRowCheck = "";
		                	while (cells.hasNext()) { //Rows in excel with deleted values are still seen as rows
		                		cell = (XSSFCell) cells.next();
		                		emptyRowCheck = emptyRowCheck + cell;
		                	}
		                	if (!emptyRowCheck.equals("")) {
				                for (int i = 0; i < columnCount; i++) { //for all columns in the row based on columnCount
				                	columnKey = columnName.get(i).toString();
				                	columnValue = "";
				                	cell = row.getCell(i);
				                	cell.setCellType(Cell.CELL_TYPE_STRING); //converts cell type to string to prevent values from returning as other types (ie number 4 returns as 4.0)
				                	if (cell != null) {
				                		columnValue = cell.toString(); //gets cell value as a string if cell is not null; else it remains blank string
				                	}
				                    cellData.add(cell);
				                    resultsMap.put(columnKey, columnValue); //stores key/value in resultsMap
				                }
				                cellData.clear();
				                excelData.add(resultsMap);
		                	}
		                }
		            }
		            excelWorkBook.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } finally {
		            if (fileInputStream != null) {
		                fileInputStream.close();
		            }
		        }
			}
			else {
				throw new Exception("Expected results file must be xls or xlsx!");
			}
			return excelData;
	    }
	
	
	public  void writeExcel(String fileName,String sheetName,String[] dataToWrite) throws IOException{

        //Create an object of File class to open xlsx file
		String sourceFilePath = "./src/test/resources/";

        File file =    new File(sourceFilePath+"/"+fileName);

        //Create an object of FileInputStream class to read excel file

        FileInputStream inputStream = new FileInputStream(file);

        Workbook CASWorkbook = null;

        //Find the file extension by splitting  file name in substring and getting only extension name

        String fileExtensionName = fileName.substring(fileName.indexOf("."));

        //Check condition if the file is xlsx file

        if(fileExtensionName.equals(".xlsx")){

        //If it is xlsx file then create object of XSSFWorkbook class

        	CASWorkbook = new XSSFWorkbook(inputStream);

        }

        //Check condition if the file is xls file

        else if(fileExtensionName.equals(".xls")){

            //If it is xls file then create object of XSSFWorkbook class

        	CASWorkbook = new HSSFWorkbook(inputStream);

        }

        

    //Read excel sheet by sheet name    

    Sheet sheet = CASWorkbook.getSheet(sheetName);
    
    //Create new Row and Update the cell with new values
    Row row = sheet.createRow(1);
    Row newRow = sheet.getRow(1);
   
    for(int i = 0; i<2; i++){
    	newRow.createCell(i).setCellValue("default");
    	newRow.getCell(i).setCellValue(dataToWrite[i]);
    }

    //Close input stream

    inputStream.close();

    //Create an object of FileOutputStream class to create write data in excel file

    FileOutputStream outputStream = new FileOutputStream(file);

    //write data in the excel file

    CASWorkbook.write(outputStream);

    //close output stream

    outputStream.close();

    }
}