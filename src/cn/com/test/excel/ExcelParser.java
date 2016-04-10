package cn.com.test.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
public class ExcelParser {
 public static void main(String[] args) {
  Workbook wb = null;
  try {
   //获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
   InputStream is = new FileInputStream("E:/c.xls");
   wb = Workbook.getWorkbook(is);
   // 获得第一个工作表对象
   Sheet sheet = wb.getSheet(0);
   //获得工作表的行数和列数
   int rows = sheet.getRows();
   int cols = sheet.getColumns();
   System.out.println("一共 " + rows + " 行");
   System.out.println("一共 " + cols + " 列");
   int counter = 0; //工作表行游标
   int fileCounts = 1; //用来标识创建的excel文档数目
   while(counter<rows-1) {
    //得到counter行的所有单元格
    Cell[] rowCells = sheet.getRow(counter);
    Cell cell0 = rowCells[0];
    //判断单元格内容的类型
    if(cell0.getType() == CellType.LABEL) {
     System.out.println("正在解析第 " + fileCounts + " 个文件....");
     //新建一个excel文档
     File file = new File("E:/excel" + fileCounts + ".xls");
     WritableWorkbook wwb = Workbook.createWorkbook(file);
     //设置excel文档的工作表
     WritableSheet ws = wwb.createSheet("sheet1", 0);
     //第一行合并第0到第8列
     ws.mergeCells(0, 0, 8, 0);
     //设置第7,8,9列的列宽
     ws.setColumnView(6, 10);
     ws.setColumnView(7, 45);
     ws.setColumnView(8, 27);
     //向新建的表中写入数据，首先第一行先写入标题
     for(int k=0; k<rowCells.length; k++) {
      //创建WritableFont对象用来格式化字体，这里是20号宋体，加粗
      WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 20, WritableFont.BOLD, false);
      //使用WritableFont创建单元格格式化对象
      WritableCellFormat wcf = new WritableCellFormat(wf);
      //设置水平对齐方式
      wcf.setAlignment(Alignment.CENTRE);
      //设置垂直对齐方式
      wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
      //设置边框和颜色
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
      Cell cell = rowCells[k];
      Label label = new Label(k,0,cell.getContents(),wcf);
      //添加单元格到表中
      ws.addCell(label);
      //设置第一行的行高
      ws.setRowView(0, 30*20, false);
     }
     //向新建的表中写入数据，第二行写入表头
     for(int c=0; c<cols; c++) {
      String colCon = sheet.getCell(c, 1).getContents();
      WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.BOLD, false);
      WritableCellFormat wcf = new WritableCellFormat(wf);
      wcf.setAlignment(Alignment.CENTRE);
      wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
      wcf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
      ws.addCell(new Label(c,1,colCon,wcf));
      ws.setRowView(1, 18*20, false);
     }
     int rowCounts = 1; //用来遍历50
     counter++; //将游标移动到下一行
     if(counter == 1) //如果游标到了第二行 ，就自动把游标移动到第三行，第二行不需要处理
      counter = 2;
     int rowIndex = 2; //每篇excel文档的游标
     rowCells = sheet.getRow(counter);
     cell0 = rowCells[0];
     while(cell0.getType() == CellType.NUMBER && counter<rows-1) {
      rowCells = sheet.getRow(counter);
      for(int k=0; k<rowCells.length; k++) {
       WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD, false);
       WritableCellFormat wcf = new WritableCellFormat(wf);
       wcf.setAlignment(Alignment.CENTRE);
       wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
       wcf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
       Label label = new Label(k,rowIndex,rowCells[k].getContents(),wcf);
       ws.addCell(label);
      }
      //用来处理备注列的边框
      {
       WritableFont wf = new WritableFont(WritableFont.createFont("宋体"), 12, WritableFont.NO_BOLD, false);
       WritableCellFormat wcf = new WritableCellFormat(wf);
       wcf.setAlignment(Alignment.CENTRE);
       wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
       wcf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
       Label label = new Label(8,rowIndex,"",wcf);
       ws.addCell(label);
      }
      ws.setRowView(rowIndex, 18*20, false);
      rowIndex++;
      counter++;
      cell0 = sheet.getRow(counter)[0];
     }
     wwb.write();
     wwb.close();
     fileCounts++;
    }
   }
   System.out.println("程序执行结束....");
  } catch (FileNotFoundException e) {
   e.printStackTrace();
  } catch (BiffException e) {
   e.printStackTrace();
  } catch (IOException e) {
   e.printStackTrace();
  } catch (RowsExceededException e) {
   e.printStackTrace();
  } catch (WriteException e) {
   e.printStackTrace();
  } finally {
   wb.close(); //关闭Workbook对象
  }
 }
}
