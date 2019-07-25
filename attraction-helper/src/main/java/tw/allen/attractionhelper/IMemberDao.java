package tw.allen.attractionhelper;

import java.sql.SQLException;

public interface IMemberDao {
    public void add(Member m) throws SQLException;
    public void update(Member m) throws SQLException;
    public void delete(int user_id) throws SQLException;
    public Member queryId(int user_id) throws SQLException;
    public Member existed(String username,String password) throws SQLException;
    public void createConn() throws SQLException;
    public void closeConn() throws SQLException;
}
