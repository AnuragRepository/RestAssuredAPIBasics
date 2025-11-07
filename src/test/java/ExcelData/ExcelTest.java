package ExcelData;

import UtilityPackage.Utility;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import org.testng.annotations.Test;


import java.io.IOException;
import java.util.ArrayList;

import static UtilityPackage.Utility.excelReadUtilityFillo;


public class ExcelTest {

    @Test
    public void getTestCaseDataFromExcel() throws IOException {

        ArrayList<String> data = Utility.getExcelDataConventionalWay("PageA", "TestCases", "Purchase");

        for (int i = 0; i < data.size(); i++) {
            System.out.println(data.get(i));
        }

    }


    @Test
    public void excelTest() throws FilloException {

        Recordset recordsetObj = excelReadUtilityFillo("select * from PageA");

        //Fetch result/output using recordsetObject

        //1. Print total no. of rows in excelSheet
        int totalNumberOfRowsInExcelSheet = recordsetObj.getCount();
        System.out.println("Total no of rows in excel sheet = " + totalNumberOfRowsInExcelSheet);

        //2. Print total no. of columns in excelSheet
        ArrayList<String> columnsNameList =recordsetObj.getFieldNames();
        int totalNumberOfColumnsInExcelSheet = columnsNameList.size();
        System.out.println("Total no of columns in excel sheet = " + totalNumberOfColumnsInExcelSheet);

        //3. Print headers/column title in excelSheet
        System.out.print("Print headers/column title in excelSheet = ");
        for (int i = 0; i < columnsNameList.size(); i++) {
            System.out.print(columnsNameList.get(i) + " ");
        }

        //4. Print 1st cell data of excelSheet
        recordsetObj.moveFirst();
        System.out.println("\n" + "Print 1st cell data of excelSheet = " +recordsetObj.getField(0).value());

        //5. Print Second cell data of 1st column in excelSheet
        recordsetObj.moveFirst();
        recordsetObj.moveNext();
        System.out.println("Print 2nd cell data of 1st column in excelSheet = "+recordsetObj.getField(0).value());

        //6. Print Last cell data of 1st column in excelSheet
        recordsetObj.moveLast();
        System.out.println("Print Last cell data of 1st column in excelSheet = "+recordsetObj.getField(0).value());

        //7. Print Second Last cell data of 1st column in excelSheet
        recordsetObj.moveLast();
        recordsetObj.movePrevious();
        System.out.println("Print 2nd Last cell data of 1st column in excelSheet = "+recordsetObj.getField(0).value());

        //8. Print Second Last cell data of 2nd column in excelSheet
        recordsetObj.moveLast();
        recordsetObj.movePrevious();
        System.out.println("Print 2nd Last cell data of 2nd column in excelSheet = "+recordsetObj.getField(1).value());

        //9. Print 1st row of excelSheet
        System.out.print("Print 1st rowData of excelSheet = ");
        recordsetObj.moveFirst();
        for(int i=0;i<totalNumberOfColumnsInExcelSheet;i++)
        {
            System.out.print(recordsetObj.getField(i).value()+" ");
        }

        //10. Print 2nd row of excelSheet
        System.out.print("\nPrint 2nd rowData of excelSheet = ");
        recordsetObj.moveFirst();
        recordsetObj.moveNext();
        for(int i=0;i<totalNumberOfColumnsInExcelSheet;i++)
        {
            System.out.print(recordsetObj.getField(i).value()+" ");
        }

        //11. Print 2nd last row of excelSheet
        System.out.print("\nPrint 2nd last rowData of excelSheet = ");
        recordsetObj.moveLast();
        recordsetObj.movePrevious();
        for(int i=0;i<totalNumberOfColumnsInExcelSheet;i++)
        {
            System.out.print(recordsetObj.getField(i).value()+" ");
        }

        //12. Print last row of excelSheet
        System.out.print("\nPrint last rowData of excelSheet = ");
        recordsetObj.moveLast();
        for(int i=0;i<totalNumberOfColumnsInExcelSheet;i++)
        {
            System.out.print(recordsetObj.getField(i).value()+" ");
        }
        //13. Print 1st column of excelSheet
        System.out.print("\nPrint 1st column of excelSheet = ");
        recordsetObj.moveFirst();
        do
        {
            System.out.print(recordsetObj.getField(columnsNameList.get(0))+" ");
        }
        while(recordsetObj.next());

        //14. Print 2nd column of excelSheet
        System.out.print("\nPrint 2nd column of excelSheet = ");
        recordsetObj.moveFirst();
        do
        {
            System.out.print(recordsetObj.getField(columnsNameList.get(1))+" ");
        }
        while(recordsetObj.next());

        //15. Print 2nd last column of excelSheet
        System.out.print("\nPrint 2nd last column of excelSheet = ");
        recordsetObj.moveFirst();
        do
        {
            System.out.print(recordsetObj.getField(columnsNameList.get(columnsNameList.size()-2))+" ");
        }
        while(recordsetObj.next());
        //16. Print last column of excelSheet
        System.out.print("\nPrint last column of excelSheet = ");
        recordsetObj.moveFirst();
        do
        {
            System.out.print(recordsetObj.getField(columnsNameList.get(columnsNameList.size()-1))+" ");
        }
        while(recordsetObj.next());

    }

}



