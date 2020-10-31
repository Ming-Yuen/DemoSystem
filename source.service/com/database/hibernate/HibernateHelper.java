package com.database.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateHelper {
	private static SessionFactory factory;

	static {
		factory = new Configuration().configure().buildSessionFactory();
	}

	public Integer addObject(Object record) {
		Integer id = 0;
		try (Session session = factory.openSession()) {
			Transaction tx = session.beginTransaction();
			id = (Integer) session.save(record);
			tx.commit();
		}
		return id;
	}

	public static List<?> query(String queryString) {
		Session session = factory.openSession();
		Transaction tx = null;
		boolean isProcess = false;
		try {
			tx = session.beginTransaction();
			List<?> resultSet = session.createQuery(queryString).list();
			tx.commit();
			isProcess = true;
			return resultSet;
		} finally {
			if(!isProcess && tx != null) {
				tx.rollback();
			}
			session.close();
		}
	}
}
