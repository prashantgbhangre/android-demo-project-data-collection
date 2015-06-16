package com.prashantb.demo.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ashraf
 * 
 */
public class CsvFileWriter {
	
	//Delimiter used in CSV file
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	
	//CSV file header
	private static final String FILE_HEADER = "Name,Number,Email";

	public static void writeCsvFile(String fileName , List<Data> datas) {
		
		//Create new datas objects
		/*Data data1 = new Data("Ahmed", "Mohamed", "M");
		Data data2 = new Data("Sara", "Said", "F");
		Data data3 = new Data("Ali", "Hassan", "M");
		Data data4 = new Data("Sama", "Karim", "F");
		Data data5 = new Data("Khaled", "Mohamed", "M");
		Data data6 = new Data("Ghada", "Sarhan", "F");
		
		//Create a new list of data objects
		List<Data> datas = new ArrayList<Data>();
		datas.add(data1);
		datas.add(data2);
		datas.add(data3);
		datas.add(data4);
		datas.add(data5);
		datas.add(data6);
		*/
		
		FileWriter fileWriter = null;
				
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(FILE_HEADER.toString());
			
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			//Write a new data object list to the CSV file
			for (Data data : datas) {
				fileWriter.append(data.getName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(data.getNumber());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(data.getEmail());
				fileWriter.append(NEW_LINE_SEPARATOR);
			}

			
			
			System.out.println("CSV file was created successfully !!!");
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {
			
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
			}
			
		}
	}
}
