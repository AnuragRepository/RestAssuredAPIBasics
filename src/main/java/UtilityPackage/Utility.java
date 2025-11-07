package UtilityPackage;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import io.restassured.path.json.JsonPath;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class Utility {

    public static JsonPath
    jsonPathUtility(String response)
    {
        JsonPath js = new JsonPath(response);
        return js;
    }

    public static ArrayList<String> getExcelDataConventionalWay(String spreadSheetName, String columnHeader, String testCaseName) throws IOException {

      /*  Flow for access- Excel->Sheet->row->column
        Step 1- Create object for class XSSFWorkbook
        Step 2 - Get access to specific sheet of Workbook
        Step 3- Get access to all rows of specific sheet of workbook
        Step 4- Get access to specific rows of specific sheet of workbook which is required
        Step 5- Get access to all cells/columns of that specific row of that specific sheet of workbook
        Step 6 - Access the data from excel*/

        ArrayList<String> data = new ArrayList<String>();
        //create fileinputstream obj and pass excel path to pass
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/Payloads/TestData.xlsx");

        //Step 1- Create object for class XSSFWorkbook

        //class accept fileinputstream only
        //xssfWorkbookObj has access to whole sheet
        XSSFWorkbook xssfWorkbookObj = new XSSFWorkbook(fis);

        //Step 2 - Get access to specific spreadsheet of Workbook
        //get count of all spreadsheet in workbook
        int numberOfSpreadSheet = xssfWorkbookObj.getNumberOfSheets();

        //now reach to required specific spreadsheet in workbook while iterating all speadsheet

        for(int i=0;i<numberOfSpreadSheet;i++)
        {
            if(xssfWorkbookObj.getSheetName(i).equalsIgnoreCase(spreadSheetName))
            {
                XSSFSheet spreadSheet = xssfWorkbookObj.getSheetAt(i);// we have the spreadSheet in this Obj spreadSheet

                //AGENDA -Now lets say we have to find TestCase with Name Purchase, for this
              /*Step A- scan first row header of excel spreadsheet to find column header " TestCase" containing names
                Step B- Once column header found scan full column for finding Purchase TestCase name
                Step C- Once Purchase TestCase name found in column , scan full row to get data*/


                //Step 3- Get access to all rows of specific sheet of workbook

                //Step A- scan first row header of excel spreadsheet to find column header " TestCase" containing names

                Iterator<Row> rowIterator = spreadSheet.iterator(); // sheet is collection of row

                Row firstRow = rowIterator.next();//

                Iterator<Cell> cellIterator = firstRow.cellIterator(); // row is collection of column

                int columnInitCount = 0;
                int actualColumnIndex = 0;

                while(cellIterator.hasNext())
                {
                    Cell cellValue = cellIterator.next();
                    if(cellValue.getStringCellValue().equalsIgnoreCase(columnHeader))
                    {
                        //we found the column but we need to store the column index for iterating this specific column
                        actualColumnIndex = columnInitCount;
                    }
                    columnInitCount++;
                }
                System.out.println("\n\nExcel Data fetch by conventional way -XSSFWorkbook class");
                System.out.println("ColumnIndex of testcase "+testCaseName+ " present in excel spreadsheet "+spreadSheetName+" is "+actualColumnIndex);

                //Step B- Once column header found scan full column for finding Purchase TestCase name

                //there is no method for column so use rows iterator for iterating through each row data in that column

                while(rowIterator.hasNext()) // already have row iterator object created at step1 in beginning
                {
                    Row rows = rowIterator.next(); // iterating through each row
                    //rows will search only in specified column by passing column index

                    //Step 4- Get access to specific rows of specific sheet of workbook which is required
                    if(rows.getCell(actualColumnIndex).getStringCellValue().equalsIgnoreCase(testCaseName))
                    {
                        Iterator<Cell> cellValues = rows.cellIterator();//create cell Iterator object

                        // Step 5- Get access to all cells/columns of that specific row of that specific sheet of workbook
                        //Step C- Once Purchase TestCase name found in column , scan full row to get data
                        while(cellValues.hasNext())
                        {
                            //System.out.println(cellValues.next().getStringCellValue());// iterating though each cell data of purchase row

                            // store it in variable and call it so that it will not increment, if directly used cellValues.next() value will increment in if condition as well
                            Cell cell = cellValues.next();
                            if(cell.getCellType().equals(CellType.STRING)) {
                                data.add(cell.getStringCellValue());
                            }
                            else
                            {
                                // if data is numeric , fetch numeric content and then convert to string and add in List
                                data.add(NumberToTextConverter.toText(cell.getNumericCellValue()));
                            }

                        }
                    }
                }

            }

        }
        return data;
    }
    public static Recordset excelReadUtilityFillo(String sqlQuery) throws FilloException {


        //Create Fillo Obj
        Fillo filloObj = new Fillo();

        //connect to excel using FilloObject and store in connectionObject

        //Fillo 1.24 version (dependancy) only support xls not xlsx format, 1.22 supports both
        Connection connectionObj= filloObj.getConnection(System.getProperty("user.dir")+"/src/main/java/Payloads/TestData.xlsx");

        //Execute query using connectionObject and store in recordsetObject
        Recordset recordsetObj = connectionObj.executeQuery(sqlQuery);

        return recordsetObj;


    }
    public static CSVParser csvReadUtility(String csvFilePath) throws IOException {
        //For format
        CSVFormat csvFormatObj = CSVFormat.DEFAULT.builder()
                .setHeader() // use the first row as header
                .setSkipHeaderRecord(true) // Skip the header record itself when iterating
                .setTrim(true) // Trim whitespace from values
                .build();

        //For CSV file path
        BufferedReader bufferedReaderObj = Files.newBufferedReader(Paths.get(csvFilePath));

        // Main CSV class
        CSVParser csvParserObj = new CSVParser(bufferedReaderObj, csvFormatObj);

        return csvParserObj;

    }


}


