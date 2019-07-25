package tw.allen.attractionhelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.bouncycastle.jce.provider.CertStoreCollectionSpi;

public class MyJdbcDataSource implements DataSource {
    
    private Properties props;
    private String mySQLUrl;
    private String myUser;
    private String myPwd;
    private int maxConn;
    private List<Connection> connPools;
    private Connection conn;

    public MyJdbcDataSource() throws FileNotFoundException, IOException {
        this("mssqlserver.properties");
    }
    
    public MyJdbcDataSource(String config) throws FileNotFoundException, IOException {
        props = new Properties();
        props.load(new FileInputStream(config));
        mySQLUrl = props.getProperty("mySQLUrl");
        myUser = props.getProperty("myUser");
        myPwd = props.getProperty("myPwd");
        maxConn = Integer.parseInt(props.getProperty("maxConn"));
        
        connPools = Collections.synchronizedList(new ArrayList<Connection>());       
    }

    @Override
    public Connection getConnection() throws SQLException {
        if(connPools.isEmpty()) {
            conn = DriverManager.getConnection(mySQLUrl,myUser,myPwd);
            return conn;
        } else {
            return connPools.remove(connPools.size()-1);
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        if(connPools.isEmpty()) {
            conn = DriverManager.getConnection(mySQLUrl,username,password);
            return conn;
        } else {
            return connPools.remove(connPools.size()-1);
        }
    }

    public void closeConn() throws SQLException {
        if(connPools.size() == maxConn) {
            conn.close();
        } else {
            connPools.add(conn);
        }
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter arg0) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLoginTimeout(int arg0) throws SQLException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

}
