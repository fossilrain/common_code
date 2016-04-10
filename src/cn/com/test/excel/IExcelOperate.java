package cn.com.test.excel;

import java.util.List;

public interface IExcelOperate {

    /**
     * 加载excel文件
     * @param fileName
     */
    void load(String fileName) throws Exception;

    /**
     * 读取excel文件数据
     *
     * @throws Exception
     */
    public <T> List<T> readExcel() throws Exception;

    /**
     * 分割excel
     *
     * @throws Exception
     */
    public void splitExcel() throws Exception;

    /**
     * 多线程分割excel
     */
    public void splitExcelThread() throws Exception;
    /**
     * 关闭文件
     */
    void close();
}