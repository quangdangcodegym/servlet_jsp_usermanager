package com.phuxuan.utils;

import java.util.List;

import com.phuxuan.dao.CountryDAO;
import com.phuxuan.dao.ICountryDAO;
import com.phuxuan.model.Country;

public class AppUtils {
	private static List<Country> listCountry;
	
	
	public static List<Country> getAllCountry() {
		if(listCountry==null) {
			ICountryDAO iCountryDAO = new CountryDAO();
			
			listCountry = iCountryDAO.getAllCountry(); 
		}
		return listCountry;
	}
	
	

}
