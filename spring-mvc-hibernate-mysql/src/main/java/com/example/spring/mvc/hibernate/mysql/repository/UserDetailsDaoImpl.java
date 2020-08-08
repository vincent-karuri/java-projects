package com.example.spring.mvc.hibernate.mysql.repository;

import com.example.spring.mvc.hibernate.mysql.entity.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by Vincent Karuri on 08/08/2020
 */

@Repository
public class UserDetailsDaoImpl implements UserDetailsDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public User findUserByUserName(String userName) {
		return sessionFactory.getCurrentSession().get(User.class, userName);
	}
}
