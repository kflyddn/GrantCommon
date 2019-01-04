package cn.pcshao.grant.common.util;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pcshao.cn
 * @date 2018/12/27
 */
public class ExcelUtil {
    /**
     * 读取excel文件
     * 	输出Vector
     */
    public static List<List> TransExcelToList(InputStream excel){
        List<List> ret = new ArrayList<>();
        List v = null;
        Workbook wb_in = null;
        Sheet[] sheets;
        Cell[] cells;
        try {
            wb_in = Workbook.getWorkbook(excel);
        } catch (Exception e) {

        }
        if(wb_in==null)
            return null;
        //获得了Workbook对象之后，就可以通过它得到Sheet（工作表）对象了
        sheets = wb_in.getSheets();
        cells = sheets[0].getRow(0);
        if(sheets!=null&&sheets.length>0){
            //对每个工作表进行循环 这里只读第一张sheet
            for(int i=0;i<1;i++){
                //得到当前工作表的行数
                for(int j=0;j<sheets[i].getRows();j++){
                    //得到当前行的所有单元格
                    cells = sheets[i].getRow(j);
                    if(cells!=null&&cells.length>0){
                        //对每个单元格进行循环
                        v = new ArrayList();
                        for(int k=0;k<cells.length;k++){
                            //读取当前单元格的值
                            v.add(cells[k].getContents());
                        }
                        ret.add(v);
                    }
                }
            }
        }
        //最后关闭资源，释放内存
        wb_in.close();
        return ret;
    }
}
