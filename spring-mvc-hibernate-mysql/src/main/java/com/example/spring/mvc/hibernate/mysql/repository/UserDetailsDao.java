package com.example.spring.mvc.hibernate.mysql.repository;

import com.example.spring.mvc.hibernate.mysql.entity.User;

/**
 * Created by Vincent Karuri on 08/08/2020
 */
public interface UserDetailsDao {
	User findUserByUserName(String userName);
}
