package tw.allen.attractionhelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import tw.allen.attractionhelper.Attraction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AttractionDaoJdbcImpl implements IAttractionDao {

    private MyJdbcDataSource dao;
    private Connection conn;

    public AttractionDaoJdbcImpl() throws FileNotFoundException, IOException {
        dao = new MyJdbcDataSource();
    }
    
    @Override
    public Attraction queryById(int id) throws SQLException {
        String sqlstr = "Select * From Attraction Where attraction_id = ?";
        PreparedStatement state = conn.prepareStatement(sqlstr);
        state.setInt(1, id);
        ResultSet rs = state.executeQuery();
        
        Attraction attraction = null;
        if(rs.next()) {
            attraction = new Attraction();
            attraction.setAttraction_id(rs.getInt("attraction_id"));
            attraction.setName(rs.getString("name"));
            attraction.setOpen_time("open_time");
            attraction.setDistrict(rs.getString("district"));
            attraction.setAddress(rs.getString("address"));
            attraction.setTel(rs.getString("tel"));
            attraction.setCreatedtime(rs.getString("createdtime"));
        }
        return attraction;
    }

    @Override
    public void addAttraction() throws SQLException {
        String sqlstr = "Insert into Attraction(name,open_time,district,address,tel) Values(?,?,?,?,?)";
        PreparedStatement state = conn.prepareStatement(sqlstr);

        Scanner sc = new Scanner(System.in);
        System.out.println("請輸入景點名稱");
        state.setString(1, sc.nextLine());

        System.out.println("請輸入景點開放時間");
        state.setString(2, sc.nextLine());

        System.out.println("請輸入景點位於哪一區");
        state.setString(3, sc.nextLine());

        System.out.println("請輸入景點地址");
        state.setString(4, sc.nextLine());

        System.out.println("請輸入電話號碼");
        state.setString(5, sc.nextLine());

        int status = state.executeUpdate();
        String info = "";
        info = (status > 0) ? "Insert Successful" : "Insert Failed";
        System.out.println(info);
        state.close();
    }

    @Override
    public void updateAttraction() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("請輸入想要修改景點的 id");
        String idstr = sc.nextLine();
        int id = Integer.parseInt(idstr);
        
        Attraction attraction = queryById(id);
        String sqlstr = "Update Attraction Set name = ?,open_time= ?,district= ?,address = ?, tel = ? Where attraction_id = ?";
        PreparedStatement state = conn.prepareStatement(sqlstr);
        
        // 更新指定景點 id
        state.setInt(6, id);

        System.out.println("請輸入想要修改景點的名稱");
        String name = sc.nextLine();
        name = (name.isEmpty())?attraction.getName():name;
        //System.out.println(name);
        state.setString(1, name);

        System.out.println("請輸入想要修改景點的開放時間");
        String open_time = sc.nextLine();
        open_time = (open_time.isEmpty())?attraction.getOpen_time():open_time;
        //System.out.println(open_time);
        state.setString(2, open_time);

        System.out.println("請輸入想要修改景點的區域");
        String district = sc.nextLine();
        district = (district.isEmpty())?attraction.getDistrict():district;
        //System.out.println(district);
        state.setString(3, district);
        
        System.out.println("請輸入想要修改景點的地址");
        String address = sc.nextLine();
        address = (address.isEmpty())?attraction.getAddress():address;
        //System.out.println(address);
        state.setString(4, address);
        
        System.out.println("請輸入想要修改景點的電話號碼");
        String tel = sc.nextLine();
        tel = (tel.isEmpty())?attraction.getTel():tel;
        //System.out.println(tel);
        state.setString(5, tel);
        
        
        
        int status = state.executeUpdate();
        //System.out.println("更新:" + status + "筆資料");
        state.close();
    }

    @Override
    public void deleteAttraction() throws SQLException {
        String sqlstr = "Delete From Attraction Where createdtime Between ? and ?";
        PreparedStatement state = conn.prepareStatement(sqlstr);
        
        Scanner sc = new Scanner(System.in);
        System.out.println("請輸入刪除資料起始時間");
        state.setString(1, sc.nextLine());
        
        System.out.println("請輸入刪除資料結束時間");
        state.setString(2, sc.nextLine());
        int row = state.executeUpdate();
        System.out.println("刪除"+row+"筆");
    }

    @Override
    public List<Attraction> queryAll() throws SQLException {
        String sqlstr = "Select * From Attraction";
        PreparedStatement state = conn.prepareStatement(sqlstr);
        ResultSet rs = state.executeQuery();

        List<Attraction> attractions = new ArrayList<Attraction>();
        Attraction attraction = null;
        while (rs.next()) {
            attraction = new Attraction();
            attraction.setAttraction_id(rs.getInt("attraction_id"));
            attraction.setName(rs.getString("name"));
            attraction.setOpen_time(rs.getString("open_time"));
            attraction.setDistrict(rs.getString("district"));
            attraction.setAddress(rs.getString("address"));
            attraction.setTel(rs.getString("tel"));
            attraction.setCreatedtime(rs.getString("createdtime"));

            System.out.printf(
                    "attraction_id=%s\tname=%s\topen_time=%s\tdistrict=%s\taddress=%s\ttel=%s\tcreatedtime=%s\n",
                    rs.getString("attraction_id"), rs.getString("name"), rs.getString("open_time"),
                    rs.getString("district"), rs.getString("address"), rs.getString("tel"),
                    rs.getTimestamp("createdtime"));
            attractions.add(attraction);
        }
        
        return attractions;
    }

    @Override
    public List<Attraction> readJsonAttraction() throws SQLException {
        System.out.println("請輸入 json 檔案路徑");
        Scanner sc = new Scanner(System.in);
        String path = sc.nextLine();
        Gson gson = new Gson();

        // 序列化
        List<Attraction> attractions = null;
        try (FileReader fr1 = new FileReader(path)) {
            Type type = TypeToken.getParameterized(ArrayList.class, Attraction.class).getType();
            attractions = gson.fromJson(fr1, type);
        } catch (IOException e) {
            System.out.println("並無此檔案路徑, 請重新執行");
            attractions = new ArrayList<Attraction>();
        }

        System.out.println("共有" + attractions.size() + "筆資料");
        int i = 1;
        for (Attraction attraction : attractions) {
            System.out.println(attraction.toString());
        }
        System.out.println("讀取" + path + "檔案完畢");
        return attractions;
    }

    @Override
    public void addJsonAttraction(List<Attraction> attractions) throws SQLException {
        String sqlstr = "Insert into Attraction(name,open_time,district,address,tel,createdtime) Values(?,?,?,?,?,?)";
        PreparedStatement state = conn.prepareStatement(sqlstr);

        for (Attraction attraction : attractions) {
            state.setString(1, attraction.getName());
            state.setString(2, attraction.getOpen_time());
            state.setString(3, attraction.getDistrict());
            state.setString(4, attraction.getAddress());
            state.setString(5, attraction.getTel());

            Calendar c1 = Calendar.getInstance();
            int year = c1.get(Calendar.YEAR);
            int month = c1.get(Calendar.MONTH) + 1;
            int date = c1.get(Calendar.DATE);
            int hour = c1.get(Calendar.HOUR_OF_DAY);
            int minute = c1.get(Calendar.MINUTE);
            int second = c1.get(Calendar.SECOND);
            int milesecond = c1.get(Calendar.MILLISECOND);
            String monthformat = (month < 10) ? "0" + month : "" + month;
            String dateformat = (date < 10) ? "0" + date : "" + date;
            String hourformat = (hour < 10) ? "0" + hour : "" + hour;
            String minuteformat = (minute < 10) ? "0" + minute : "" + minute;
            String secondformat = (second < 10) ? "0" + second : "" + second;
            String milesecondformat = "";
            if (milesecond < 10) {
                milesecondformat = "00" + milesecond;
            } else if (milesecond < 100) {
                milesecondformat = "0" + milesecond;
            } else {
                milesecondformat = "" + milesecond;
            }

            String now = year + "-" + monthformat + "-" + dateformat + " " + hourformat + ":" + minuteformat + ":"
                    + secondformat + "." + milesecondformat;
            state.setTimestamp(6, Timestamp.valueOf(now));

            state.executeUpdate();
        }
        state.close();

    }

    @Override
    public void writeJsonAttraction() throws SQLException, IOException {
        System.out.println("請輸入 json 檔案輸出路徑:");
        Scanner sc = new Scanner(System.in);
        String targetPath = sc.nextLine();
        List<Attraction> attractions = new ArrayList<Attraction>();

        String sqlstr = "Select * From Attraction";
        PreparedStatement state = conn.prepareStatement(sqlstr);
        ResultSet rs = state.executeQuery();
        
        while (rs.next()) {
            Attraction attraction = new Attraction();
            attraction.setAttraction_id(rs.getInt("attraction_id"));
            attraction.setName(rs.getString("name"));
            attraction.setOpen_time(rs.getString("open_time"));
            attraction.setDistrict(rs.getString("district"));
            attraction.setAddress(rs.getString("address"));
            attraction.setTel(rs.getString("Tel"));
            attraction.setCreatedtime(rs.getString("createdTime"));

            attractions.add(attraction);
        }

        Gson gson = new Gson();
        try (FileWriter fw1 = new FileWriter(targetPath)) {
            gson.toJson(attractions, fw1);
            System.out.println(targetPath + "已經建立完成");
        }

        rs.close();
        state.close();
    }

    @Override
    public List<Attraction> readCsvAttraction() throws SQLException {
        System.out.println("請輸入 csv 檔案路徑");
        Scanner sc = new Scanner(System.in);
        String path = sc.nextLine();

        List<Attraction> attractions = new ArrayList<Attraction>();
        try (BufferedReader br1 = new BufferedReader(new FileReader(path))) {
            String data;
            int lineNo = 0;
            while ((data = br1.readLine()) != null) {
                lineNo++;
                System.out.println("第" + lineNo + "筆:" + data);

                if (lineNo == 1) {
                    continue;
                }
                String[] values = data.split(",");
                attractions.add(new Attraction(values[1],values[2],values[3],values[4],values[5]));
            }

        } catch (IOException e) {
            System.out.println("並無此檔案路徑, 請重新執行");
        }
        
        System.out.println(attractions.size());
        return attractions;
    }

    @Override
    public void writeCsvAttraction() throws SQLException {
        String sqlstr = "Select * From Attraction";
        PreparedStatement state = conn.prepareStatement(sqlstr);
        ResultSet rs = state.executeQuery();

        System.out.println("請輸入 csv 檔案輸出路徑");
        Scanner sc = new Scanner(System.in);
        String path = sc.nextLine();

        try (BufferedWriter bw1 = new BufferedWriter(new FileWriter(path))) {
            String field = "attraction_id,name,open_time,district,address,tel\n";
            bw1.write(field);
            while (rs.next()) {
                String id = rs.getString("attraction_id");
                String name = rs.getString("name").replaceAll("\n", "");
                String open_time = rs.getString("open_time").replaceAll("\n|\r", "");
                open_time = (!(open_time.equals("")))?open_time:" ";
                String district = rs.getString("district").replaceAll("\n", "");
                String address = rs.getString("address").replaceAll("\n", "");
                String tel = rs.getString("tel").replaceAll("\n", "");
                tel = (!(tel.equals("")))?tel:" ";
                String attractionStr = id + "," + name + "," + open_time + "," + district + "," + address + "," + tel + "\n";

                bw1.write(attractionStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addCsvAttraction(List<Attraction> attractions) throws SQLException {
        String sqlstr = "Insert into Attraction(name,open_time,district,address,tel) Values(?,?,?,?,?)";
        PreparedStatement state = conn.prepareStatement(sqlstr);

        for(Attraction attrac:attractions) {
            state.setString(1, attrac.getName());
            state.setString(2, attrac.getOpen_time());
            state.setString(3, attrac.getDistrict());
            state.setString(4, attrac.getAddress());
            state.setString(5, attrac.getTel());
            state.addBatch();
            
        }
        state.executeBatch();
        state.close();
    }
    
    @Override
    public void writeExcelAttraction() throws SQLException {
        List<Attraction> attractions = queryAll();
        XSSFWorkbook wb = new XSSFWorkbook();
        
        System.out.println("請輸入 Excel 檔案輸出路徑");
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();
        XSSFSheet sheet= wb.createSheet("工作清單");
        
        // Excel 欄位名稱
        XSSFRow titleRow = sheet.createRow(0);
        XSSFCell cell1 = titleRow.createCell(0);
        cell1.setCellValue("attraction_id");
        XSSFCell cell2 = titleRow.createCell(1);
        cell2.setCellValue("name");
        XSSFCell cell3 = titleRow.createCell(2);
        cell3.setCellValue("open_time");
        XSSFCell cell4 = titleRow.createCell(3);
        cell4.setCellValue("district");
        XSSFCell cell5 = titleRow.createCell(4);
        cell5.setCellValue("address");
        XSSFCell cell6 = titleRow.createCell(5);
        cell6.setCellValue("tel");
        XSSFCell cell7 = titleRow.createCell(6);
        cell7.setCellValue("createdtime");
        
        Attraction fieldLength = new Attraction();
        int fieldCount = fieldLength.countField();
        for(int i=0;i<attractions.size();i++) {
            Attraction attraction = attractions.get(i);
            
            XSSFRow row = sheet.createRow(i+1);
            String idstr = ""+attraction.getAttraction_id();
            String[] attracArray = {
                    idstr,
                    attraction.getName(),
                    attraction.getOpen_time(),
                    attraction.getDistrict(),
                    attraction.getAddress(),
                    attraction.getTel(),
                    attraction.getCreatedtime()};
            for(int j=0;j<fieldCount;j++) {
                XSSFCell cell = row.createCell(j);
                cell.setCellValue(attracArray[j]);
            }  
        }
        
        try(FileOutputStream fos1 = new FileOutputStream(fileName)){
            wb.write(fos1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createConn() throws SQLException {
        conn = dao.getConnection();
        System.out.println("mydao status:" + !(conn.isClosed()));
    }

    @Override
    public void closeConn() throws SQLException {
        dao.closeConn();
    }

}
