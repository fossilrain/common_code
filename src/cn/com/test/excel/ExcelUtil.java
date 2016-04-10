package cn.com.test.excel;


import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtil<T> implements IExcelOperate {
//    protected final Log log = LogFactory.getLog(getClass());

    private ExcelModel excelModel;
    private Class tClass;
    private Cell[] titleCell;
    private jxl.Workbook workBook;
    private Sheet sheet;

    public int getRows() {
        if (this.sheet != null) {
            return this.sheet.getRows();
        }
        return 0;
    }

    public ExcelUtil() {
        super();
    }

    public ExcelUtil(ExcelModel excelModel, Class tClass) {
        this.tClass = tClass;
        this.excelModel = excelModel;
    }

    public ExcelModel getExcelModel() {
        return excelModel;
    }

    public void setExcelModel(ExcelModel excelModel) {
        this.excelModel = excelModel;
    }
    
    @Override
    public void load(String fileName) throws Exception {
        try {
            File file = new File(fileName);

            workBook = Workbook.getWorkbook(file);
            this.excelModel.setFiles(new ArrayList<FileInfoModel>());
            //默认读取文件的路径
            String[] ary = file.getName().split("\\.");
            String suffix = ary[1];
            this.excelModel.getFiles().add(new FileInfoModel(file.getName(), fileName, "." + suffix, file.getParent()));

            if (this.excelModel.isCleareRepeat()) {
                //this.clearRepeat(file);
                workBook = Workbook.getWorkbook(file);
            }

            if (workBook == null) {
                throw new Exception("读取excel文件出错!");
            }
            sheet = this.excelModel.getSheetName().trim() == "" ? workBook.getSheet(0) : workBook.getSheet(this.excelModel.getSheetName());
            if (sheet == null) {
                sheet = workBook.getSheet(0);
            }
            if (sheet == null) {
                throw new Exception("读取sheet出错!");
            }

            this.excelModel.setTotalRows(sheet.getRows());
            // 用于存储列标题
            titleCell = new Cell[sheet.getColumns()];

            // 将列标题存储存到一个一维数组中
            for (int i = 0; i < titleCell.length; i++) {
                titleCell[i] = sheet.getCell(i, 0);
            }

        } catch (IOException e) {
            workBook.close();
        }
    }
    
       @Override
    public void splitExcel() throws Exception {
        //根据文件数分割 excel，重置文件集合
        this.excelModel.setFiles(new ArrayList<FileInfoModel>());
        //生成Guid作为文件前缀
        UUID uuid = UUID.randomUUID();
        FileInfoModel fileInfoModel = new FileInfoModel();
        for (int i = 1; i <= this.excelModel.getFileCount(); i++) {
            fileInfoModel = getFileInfoModelBySplit(i, uuid);
            this.excelModel.getFiles().add(this.split(fileInfoModel));
        }
    }

    @Override
    public void splitExcelThread() throws Exception {
        //根据文件数分割 excel，重置文件集合
        this.excelModel.setFiles(new ArrayList<FileInfoModel>());
        //生成Guid作为文件前缀
        UUID uuid = UUID.randomUUID();
        //根据文件数分割 excel，重置文件集合
        this.excelModel.setFiles(new ArrayList<FileInfoModel>());
        //工作线程
        ExecutorService executorService = Executors.newFixedThreadPool(this.excelModel.getFileCount());
        FileInfoModel fileInfoModel = new FileInfoModel();
        for (int i = 1; i <= this.excelModel.getFileCount(); i++) {
            fileInfoModel = getFileInfoModelBySplit(i, uuid);
            ExcelThread thread = new ExcelThread(fileInfoModel);
            executorService.execute(thread);
            this.excelModel.getFiles().add(thread.getReturnFile());
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            //检查所有线程都执行完成
        }
    }

  /**
     * 分割excel时获取文件信息
     *
     * @param i
     * @return
     */
    private FileInfoModel getFileInfoModelBySplit(int i, UUID uuid) {

        /*结束行*/
        int endRow = i * this.excelModel.getFileSize();
                 /*起始行*/
        int beginRow = (endRow - this.excelModel.getFileSize()) + 1;
                /*如果结束行超出总记录数，结束行就等于总记录数*/
        if (endRow >= this.excelModel.getTotalRows()) {
            endRow = this.excelModel.getTotalRows();
        }
        //获取文件路径
        String filePath = MessageFormat.format("{0}/{1}_{2}.xls", this.excelModel.getExcelChildFileDir(), uuid, i);
        FileInfoModel fileInfoModel = new FileInfoModel();
        fileInfoModel.setFilePath(filePath);
        fileInfoModel.setBeginRow(beginRow);
        fileInfoModel.setEndRow(endRow);
        fileInfoModel.setForIndex(i - 1);
        return fileInfoModel;
    }

/**
     * 分割excel
     *
     * @param fileInfoModel
     * @return
     */
    private FileInfoModel split(FileInfoModel fileInfoModel) {
        File file = new File(fileInfoModel.getFilePath());
        try {
            jxl.write.WritableWorkbook ww = Workbook.createWorkbook(file);
            WritableSheet ws = ww.createSheet(this.excelModel.getSheetName(), 0);

            //添加表头
            for (int iColumn = 0; iColumn < this.titleCell.length; iColumn++) {
                ws.addCell(new Label(iColumn, 0, this.titleCell[iColumn].getContents()));
            }

            //添加数据到excel中
            int rowIndex = 1;
            for (int iRow = fileInfoModel.getBeginRow(); iRow <= fileInfoModel.getEndRow(); iRow++, rowIndex++) {
                Cell[] cells = this.sheet.getRow(iRow);
                for (int iCell = 0; iCell < cells.length; iCell++) {
                    Cell cell = cells[iCell];
                    //excel 行的索引需要计算
                    ws.addCell(new Label(iCell, rowIndex, cell.getContents()));
                }
            }

            ww.write();
            ww.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RowsExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String[] ary = file.getName().split("\\.");
        String fileName = ary[0];
        String suffix = ary[1];

        fileInfoModel.setFileFullName(file.getName());
        fileInfoModel.setFileName(fileName);
        fileInfoModel.setFileSuffix("." + suffix);
        return fileInfoModel;
    }

 class ExcelThread implements Runnable {// 任务接口
        FileInfoModel returnFile;

        public FileInfoModel getReturnFile() {
            return returnFile;
        }

        ExcelThread(FileInfoModel fileInfoModel) {
            this.returnFile = fileInfoModel;
        }

        public void run() {
            long beginTime = System.currentTimeMillis();
            this.returnFile = split(this.returnFile);
            System.out.println(MessageFormat.format("分割文件{0},执行耗时{1}秒", this.returnFile.getForIndex(), (System.currentTimeMillis() - beginTime) / 1000f));
        }
    }

@Override
public <T> List<T> readExcel() throws Exception {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void close() {
	// TODO Auto-generated method stub
	
}
}
