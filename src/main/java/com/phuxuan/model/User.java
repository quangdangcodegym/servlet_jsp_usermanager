package com.phuxuan.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;



public class User implements java.io.Serializable{
	
	
    protected int id;
    
    protected String name;
    protected String email;
    protected int idCountry;
    
    protected String password;

    public int getIdCountry() {
		return idCountry;
	}

	public void setIdCountry(int idCountry) {
		this.idCountry = idCountry;
	}
	
	
	@NotEmpty
	@Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})", message = "Format email not right")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User() {}

    public User(String name, String email, int country) {
        super();
        this.name = name;
        this.email = email;
        this.idCountry = country;
    }

    public User(int id, String name, String email, int country) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.idCountry = country;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @NotEmpty (message = "Name not empty")
	@Length(min = 3, max = 10 , message = "Lenght of Name form 3 - 10 character ")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @NotEmpty
	@Email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public int getCountry() {
        return idCountry;
    }
    public void setCountry(int country) {
        this.idCountry = country;
    }

	@Override
	public String toString() {
		return String.format("User info: id-%d, name-%s, email-%s, pass-%s, idcountry-%d", this.id, this.name, this.email, this.password, this.idCountry);
	}

	
    
    
}