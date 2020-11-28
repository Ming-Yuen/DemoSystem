package com.database.hibernate;

import java.io.File;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.common.util.ClassUtil;

public class HibernateHelper {
	private static SessionFactory factory;

	static {
		File f = new File("C:\\Users\\Admin\\eclipse-workspace\\DemoSystemBackEnd\\source.Database.Hibernate\\com\\database\\hibernate\\hibernate.cfg.xml");
		factory = new Configuration().configure(f).buildSessionFactory();
	}
	
	

	public static String addObject(Object record) {
		String value = null;
		try (Session session = factory.openSession()) {
			Transaction tx = session.beginTransaction();
			value = String.valueOf(session.save(record));
			tx.commit();
		}
		return value;
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
			if (!isProcess && tx != null) {
				tx.rollback();
			}
			session.close();
		}
	}

	public void updateEmployee(Object updateRecord, Integer id, String fieldName, Object salary) throws Exception {
		Session session = factory.openSession();
		Transaction tx = null;
		boolean isProcess = false;

		try {
			tx = session.beginTransaction();
			Object dbRecord = session.get(updateRecord.getClass(), ClassUtil.<Integer>getFieldValue(updateRecord, "id"));
			ClassUtil.referenceFields(updateRecord, dbRecord);
			session.update(dbRecord);
			tx.commit();
			isProcess = true;
		} finally {
			if (!isProcess && tx != null) {
				tx.rollback();
			}
			session.close();
		}
	}

	/* Method to DELETE an employee from the records */
	public void deleteEmployee(Class<?> clazz, Integer id) {
		Session session = factory.openSession();
		Transaction tx = null;
		boolean isProcess = false;
		try {
			tx = session.beginTransaction();
			Object employee = session.get(clazz, id);
			session.delete(employee);
			tx.commit();
			isProcess = true;
		} finally {
			if (!isProcess && tx != null) {
				tx.rollback();
			}
			session.close();
		}
	}
}
