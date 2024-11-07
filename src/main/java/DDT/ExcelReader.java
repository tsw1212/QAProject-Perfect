package DDT;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {
    private String filePath;

    public ExcelReader(String filePath) {
        this.filePath = filePath;
    }

    public List<String[]> readFormData() throws IOException {
        List<String[]> formDataList = new ArrayList<>();
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            String[] rowData = new String[row.getLastCellNum()]; // Array for each row's data
            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell cell = row.getCell(i);
                rowData[i] = cell != null ? cell.toString() : "";
            }
            formDataList.add(rowData);
        }

        workbook.close();
        fis.close();
        return formDataList;
    }
}
