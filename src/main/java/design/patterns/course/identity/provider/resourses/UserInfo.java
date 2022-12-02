package design.patterns.course.identity.provider.resourses;

import org.springframework.data.repository.CrudRepository;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserInfo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String password;

    private String email;

    public String info;

    protected UserInfo(){}

    public UserInfo(String name , String password , String email, String info ){
        this.name=name;
        this.email=email;
        this.password=password;
        this.info=info;
    }

    public Integer getID(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }

    public String getEmail(){
        return email;
    }

    public String getInfo(){ return info;}

    public void setID(Integer other){
        this.id=other;
    }

    public void setName(String newName){
        this.name=newName;
    }

    public void setPassword(String newPassword){
        this.password=newPassword;
    }

    public void setEmail(String newEmail){
        this.email=newEmail;
    }

    public void setInfo( String newInfo){this.info=newInfo;  }



}
