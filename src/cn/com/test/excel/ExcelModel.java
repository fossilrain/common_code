package cn.com.test.excel;

import java.util.List;

public class ExcelModel {

    /*excel 文件路径*/
    private String excelFilePath;
    /*分割后的文件目录*/
    private String excelChildFileDir;
    /*excel 名称*/
    private String excelName;
    /*sheet 名称*/
    private String sheetName = "Sheet1";
    /*excel 总记录数*/
    private int totalRows;
    /*分割文件后每个文件的记录数*/
    private int fileSize = 3000;
    /*分割后的文件数*/
    private int fileCount;
    /*分割后的文件集合*/
    private List<FileInfoModel> files;
    /*计算总行数时是否去表头*/
    private boolean isRemoveHeader = true;
    /*是否去除重复数据*/
    private boolean isCleareRepeat = false;
    /*导入文件过程中是否出错*/
    private boolean isError;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean isError) {
        this.isError = isError;
    }

    public String getExcelFilePath() {
        return excelFilePath;
    }

    public void setExcelFilePath(String excelFilePath) {
        this.excelFilePath = excelFilePath;
    }

    public boolean isCleareRepeat() {
        return isCleareRepeat;
    }

    public void setCleareRepeat(boolean isCleareRepeat) {
        this.isCleareRepeat = isCleareRepeat;
    }

    public String getExcelChildFileDir() {
        return excelChildFileDir;
    }

    public void setExcelChildFileDir(String excelChildFileDir) {
        this.excelChildFileDir = excelChildFileDir;
    }

    public boolean isRemoveHeader() {
        return isRemoveHeader;
    }

    public void setRemoveHeader(boolean isRemoveHeader) {
        this.isRemoveHeader = isRemoveHeader;
    }

    public int getFileCount() {
        //根据总记录数及分割文件的行数计算文件数量
        fileCount = (int) Math.ceil(this.totalRows / this.fileSize) + 1;
        return fileCount;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getTotalRows() {
        if (this.isRemoveHeader) {
            return totalRows - 1;
        } else {
            return totalRows;
        }
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public List<FileInfoModel> getFiles() {
        return files;
    }

    public void setFiles(List<FileInfoModel> files) {
        this.files = files;
    }
}
