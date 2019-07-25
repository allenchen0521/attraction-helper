package tw.allen.attractionhelper;

public class Member {
    private int user_id;
    private String username;
    private String password;
    
    public Member() {
        
    }
    
    public Member(String username,String password) {
        this.username = username;
        this.password = password;
    }

    public int getUser_Id() {
        return user_id;
    }
    public void setUser_Id(int user_id) {
        this.user_id = user_id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
