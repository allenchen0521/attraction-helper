package tw.allen.attractionhelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IAttractionDao {
    // 手動新增景點資料
    public void addAttraction() throws SQLException;
    
    // 手動更新景點資料
    public void updateAttraction() throws SQLException;
    
    // 手動刪除某一個時間區段景點資料
    public void deleteAttraction() throws SQLException;
    
    // 查詢目前資料庫台南景點所有資料
    public List<Attraction> queryAll() throws SQLException;
    
    // 查詢指定 id 景點資料
    public Attraction queryById(int id) throws SQLException;
    
    // ** 以下為進階功能 **
    // 讀取本機景點 json 檔案, 且回傳集合物件
    public List<Attraction> readJsonAttraction() throws SQLException;
    
    // 將 json 檔案讀取後, 新增至資料庫
    public void addJsonAttraction(List<Attraction> attractions) throws SQLException;
    
    // 撈出資料庫景點資料, 輸出 json 檔案
    public void writeJsonAttraction() throws SQLException, IOException;
    
    // 讀取本機景點 csv 檔案, 且回傳集合物件
    public List<Attraction> readCsvAttraction() throws SQLException;
    
    // 將 csv 讀取後, 新增至資料庫
    public void addCsvAttraction(List<Attraction> attractions) throws SQLException;
    
    // 撈出資料庫景點資料, 輸出 csv 檔案
    public void writeCsvAttraction() throws SQLException;
    
    // 輸出 Excel
    public void writeExcelAttraction() throws SQLException;
    
    // 建立連線
    public void createConn() throws SQLException;
    
    // 關閉連線
    public void closeConn() throws SQLException;
}
