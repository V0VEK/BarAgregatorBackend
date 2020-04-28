package app;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="user_info")
public class UserInfoDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userID;

    @Column(name="password_hash")
    private String passwordHash;

    @Column(name="user_token")
    private String userToken;

    @Column(name="role_id")
    private String roleID;

    @Column(name="e_mail")
    private String email;

    private String otp;

    @Column(name="otp_creation_time")
    private long otpCreationTime;

    @Column(name="session_token")
    private String sessionToken;

    @Column(name="session_creation_time")
    private long sessionCreationTime;

    @Column(name="otp_used")
    private String otpUsed;


    public long getOtpCreationTime() {
        return otpCreationTime;
    }

    public void setOtpCreationTime(long otpCreationTime) {
        this.otpCreationTime = otpCreationTime;
    }

    public long getSessionCreationTime() {
        return sessionCreationTime;
    }

    public void setSessionCreationTime(long sessionCreationTime) {
        this.sessionCreationTime = sessionCreationTime;
    }

    public String getOtpUsed() {
        return otpUsed;
    }

    public void setOtpUsed(String otpUsed) {
        this.otpUsed = otpUsed;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
