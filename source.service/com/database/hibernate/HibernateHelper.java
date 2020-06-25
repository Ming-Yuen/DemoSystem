package com.database.hibernate;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.database.hibernate.model.ProductsModel;

public class HibernateHelper {
	private static SessionFactory factory;

	public HibernateHelper() {
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

	public <T> List<T> getObject(Class<T> clazz) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<T> employees = session.createQuery("FROM products where productCode = 'test'").list();
			tx.commit();
			return employees;
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}
}
