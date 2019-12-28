package common;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 5942011574971970871L;
    private String password;
    private String username;

    public User(String username, String password){
        this.password = password;
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return password;
    }


    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }


    public static ImageIcon getHeadIcon(){
        ImageIcon image = new ImageIcon("images/head.png");
        return image;
    }

//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + head;
//        result = prime * result + (int)(id ^ (id >> 32));
//        result = prime * result + ((username == null) ? 0 : username.hashCode());
//        result = prime * result + ((password == null) ? 0 : password.hashCode());
//        result = prime * result + sex;
//        return result;
//    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if(username == null){
            if(other.username != null)
                return false;
        }else if(!username.equals(other.username))
            return false;
        if(password == null){
            if(other.password != null)
                return false;
        }else if(!password.equals(other.password))
            return  false;
        return true;
    }

//    @Override
//    public String toString() {
//        return this.getClass().getName()
//                + "[id=" + this.id
//                + ",pwd=" + this.password
//                + ",username=" + this.username
//                + ",head=" + this.head
//                + ",sex=" + this.sex
//                + "]";
//    }
}
