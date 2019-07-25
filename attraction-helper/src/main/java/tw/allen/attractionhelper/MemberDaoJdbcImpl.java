package tw.allen.attractionhelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDaoJdbcImpl implements IMemberDao {

    private MyJdbcDataSource mydao;
    private Connection conn;

    public MemberDaoJdbcImpl() throws FileNotFoundException, IOException {
        mydao = new MyJdbcDataSource();
    }

    @Override
    public void add(Member m) throws SQLException {
        String sqlstr = "Insert into Member(username,password) Values(?,?)";
        PreparedStatement state = conn.prepareStatement(sqlstr);
        state.setString(1, m.getUsername());
        state.setString(2, m.getPassword());
        
        // 回傳新增成功筆數
        int status = state.executeUpdate();
        String info = "";
        info = (status>0)?"Insert Member:"+m.getUsername()+" Successful":"Insert Failed";
        System.out.println(info);
        state.close();
    }

    @Override
    public void update(Member m) throws SQLException {
        String sqlstr = "Update Member Set password = ? Where username =?";
        PreparedStatement state = conn.prepareStatement(sqlstr);
        state.setString(1, m.getPassword());
        state.setString(2, m.getUsername());
        
        // 回傳更新成功筆數
        int status = state.executeUpdate();
        String info = "";
        info = (status>0)?"Update Member:"+m.getUsername()+" Successful":"Update Failed";
        System.out.println(info);
        state.close();
        
    }

    @Override
    public void delete(int user_id) throws SQLException {
        String sqlstr = "Delete From Member Where user_id = ?";
        PreparedStatement state = conn.prepareStatement(sqlstr);
        state.setInt(1, user_id);

        // 回傳刪除成功筆數
        int status = state.executeUpdate();
        String info = "";
        info = (status>0)?"Delete Id="+user_id+"Successful":"Delete Failed";
        System.out.println(info);
        state.close();
        
    }

    @Override
    public Member queryId(int user_id) throws SQLException {
        String sqlstr = "Select * From Member Where user_id = ?";
        PreparedStatement state = conn.prepareStatement(sqlstr);
        state.setInt(1, user_id);
        
        Member mem = null;
        // 回傳資料集合
        ResultSet rs = state.executeQuery();
        if(rs.next()) {
            mem = new Member();
            mem.getUsername();
            mem.getPassword();
        } else {
            System.out.println("The System doesn't have this id! Please confirm your id");
        }
        
        state.close();
        rs.close();
        return mem;
    }

    @Override
    public Member existed(String username,String password) throws SQLException {
        String sqlstr = "Select * From Member Where username = ? and password = ?";
        PreparedStatement state = conn.prepareStatement(sqlstr);
        state.setString(1, username);
        state.setString(2, password);

        // 回傳資料集合
        Member mem = new Member();
        ResultSet rs = state.executeQuery();
        
        if(rs.next()) {
            mem.setUser_Id(rs.getInt("user_id"));
            mem.setUsername(rs.getString("username"));
            mem.setPassword(rs.getString("password"));
        }
        rs.close();
        state.close();
        return mem;
    }

    @Override
    public void createConn() throws SQLException {
        conn = mydao.getConnection();
        System.out.println("mydao status:"+!(conn.isClosed()));
    }

    @Override
    public void closeConn() throws SQLException {
        conn.close(); 
    }

}
