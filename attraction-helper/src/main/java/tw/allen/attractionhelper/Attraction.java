package tw.allen.attractionhelper;

import java.sql.Timestamp;

public class Attraction {
    private int attraction_id;
    private String name;
    private String open_time;
    private String district;
    private String address;
    private String tel;
    private String createdtime;
    
    public Attraction() {
        
    }
    
    public Attraction(String name,String open_time,String district,String address,String tel) {
        this.name = name;
        this.open_time = open_time;
        this.district = district;
        this.address = address;
        this.tel = tel;
    } 

    public int getAttraction_id() {
        return attraction_id;
    }

    public void setAttraction_id(int attraction_id) {
        this.attraction_id = attraction_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }
    
    public int countField() {
        return getClass().getDeclaredFields().length;
    }

    @Override
    public String toString() {
        return "id:"+attraction_id+" name:"+name+" open_time:"+open_time+" district:"+district+" address:"+address+" tel:"+tel;
    }
    
}
