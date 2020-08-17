package com.mrfsong.common.model;

/**
 * <p>
 *
 * </p>
 *
 * @Author: Felix
 * @Created: 2020/06/30 20:53
 */
public class Group {

    private String groupName;
    private User user;

    public Group(String groupName, User user) {
        this.groupName = groupName;
        this.user = user;
    }


    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                ", user=" + user +
                '}';
    }
}
