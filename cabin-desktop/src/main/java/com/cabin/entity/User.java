package com.cabin.entity;

import java.io.Serializable;

public class User implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -2896207096244227836L;
	private String name;
    private String pass;
    private Long profileId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

}
