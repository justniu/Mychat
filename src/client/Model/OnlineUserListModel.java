package client.Model;

import javax.swing.*;
import java.util.List;

public class OnlineUserListModel extends AbstractListModel {
    private static final long serialVersionUID = -3903760573171074301L;
    private List<String> onlineUsers;

    public OnlineUserListModel(List<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public void addElement(String user) {
        if (onlineUsers.contains(user)) {
            return;
        }
        int index = onlineUsers.size();
        onlineUsers.add(user);
        fireIntervalAdded(this, index, index);
    }

    public boolean removeElement(Object object) {
        int index = onlineUsers.indexOf(object);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return onlineUsers.remove(object);
    }

    public int getSize() {
        return onlineUsers.size();
    }

    public Object getElementAt(int i) {
        return onlineUsers.get(i);
    }

    public List<String> getOnlineUsers() {
        return onlineUsers;
    }

}
