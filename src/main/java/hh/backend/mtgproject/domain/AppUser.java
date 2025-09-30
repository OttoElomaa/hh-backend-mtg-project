package hh.backend.mtgproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;


@Entity
public class AppUser {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(nullable=false, updatable=false)
    private Long appId;
    
    @Column(name = "app_user_name", nullable=false, unique=true)
    private String username;
       
    @Column(name = "app_password", nullable=false)
    private String password;
    
    @Column(name = "role", nullable=false)
    private String role;

    @OneToOne(mappedBy = "appUser")
    private MtgUser mtgUser;


    // Constructors, getters and setters  
    public AppUser() {
    }

    public AppUser(String username, String password, String role) {
        
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return appId;
    }

    public void setId(Long id) {
        this.appId = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public MtgUser getMtgUser() {
        return mtgUser;
    }

    public void setMtgUser(MtgUser mtgUser) {
        this.mtgUser = mtgUser;
    }

    @Override
    public String toString() {
        return "AppUser [id=" + appId + ", username=" + username + ", password=" + password + ", role=" + role + "]";
    }
    
 




}
