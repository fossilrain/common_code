package cn.com.test.excel;

public class FileInfoModel {

    /**
     * 文件在数据库中的Id
     */
    private Long fileId;
    /**
     * 是否上传
     */
    private boolean isUpload;
    /**
     * 存储循环过程中的索引值
     */
    private int forIndex;
    /**
     * 文件全名 xxx.xls
     */
    private String fileFullName;
    /**
     * 文件名 xxx
     */
    private String fileName;
    /**
     * 文件后缀 .xls
     */
    private String fileSuffix;
    /**
     * 文件保存路径 e:\\xx\xx
     */
    private String filePath;
    /**
     * 文件的起始行号
     */
    private int beginRow;
    /**
     * 文件的末尾行号
     */
    private int endRow;

    public FileInfoModel() {
        super();
    }

    public FileInfoModel(String fileFullName) {
        super();
        this.fileFullName = fileFullName;
    }

    public FileInfoModel(String fileName, String fileFullName) {
        super();
        this.fileName = fileName;
        this.fileFullName = fileFullName;
    }

    public FileInfoModel(String fileFullName, String fileName, String fileSuffix) {
        super();
        this.fileFullName = fileFullName;
        this.fileName = fileName;
        this.fileSuffix = fileSuffix;
    }

    public FileInfoModel(String fileFullName, String fileName, String fileSuffix, String filePath) {
        super();
        this.fileFullName = fileFullName;
        this.fileName = fileName;
        this.fileSuffix = fileSuffix;
        this.filePath = filePath;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }

    public int getForIndex() {
        return forIndex;
    }

    public void setForIndex(int forIndex) {
        this.forIndex = forIndex;
    }

    public String getFileFullName() {
        return fileFullName;
    }

    public void setFileFullName(String fileFullName) {
        this.fileFullName = fileFullName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getBeginRow() {
        return beginRow;
    }

    public void setBeginRow(int beginRow) {
        this.beginRow = beginRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }
}