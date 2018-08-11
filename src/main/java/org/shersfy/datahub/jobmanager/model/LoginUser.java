package org.shersfy.datahub.jobmanager.model;

public class LoginUser extends BaseEntity{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    /** 用户名 **/
    private String userName;

    /** 登录密码 **/
    private String password;

    /** 真实用户名 **/
    private String realName;

    /** 用户角色 **/
    private String roles;

    /** 手机 **/
    private String phone;

    /** 邮箱 **/
    private String email;

    /** 删除标志 **/
    private Integer delFlg;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getDelFlg() {
        return delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }
}
