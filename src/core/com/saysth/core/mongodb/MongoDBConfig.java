package com.saysth.core.mongodb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.mongodb.DB;
import com.mongodb.Mongo;


public class MongoDBConfig {

	private static Mongo mongo;
	private static DB db;
	private static final String MONGO_DB_ADDRESS = "localhost";
	private static final int MONGO_DB_PORT = 27017;
	private static final String MONGO_DB_USERNAME = "";
	private static final String MONGO_DB_PASSWORD = "";
	private static final String MONGO_DB_DBNAME = "saysth";
	private static final String MONGO_DB_RESOURCE_FILE = "mongodb.cfg.properties";

	/**
	 * Mongo数据库参数
	 */
	private static Map<String, String> cfgMap = new HashMap<String, String>();

	private static Hashtable<String, DB> mongoDBs = new Hashtable<String, DB>();

	/**
	 * 初始化Mongo的数据库
	 */
	static {
		init();
	}

	public static File getConfigFile() {
		String path = MongoDBConfig.class.getResource("/").getPath();
		String fileName = path + MONGO_DB_RESOURCE_FILE;
		File file = new File(fileName);
		if (file.exists()) {
			return file;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static void initCfgMap() {
		File file = getConfigFile();
		if (file != null) {
			Properties p = new Properties();
			try {
				p.load(new FileInputStream(file));
				for (Enumeration enu = p.propertyNames(); enu.hasMoreElements();) {
					String key = (String) enu.nextElement();
					String value = (String) p.getProperty(key);
					cfgMap.put(key, value);
				}
			} catch (IOException e) {
				System.out.println("记载Mongo配置文件失败!");
				e.printStackTrace();
			}
		} else {
			cfgMap.put("mongo.db.address", MONGO_DB_ADDRESS);
			cfgMap.put("mongo.db.port", String.valueOf(MONGO_DB_PORT));
			cfgMap.put("mongo.db.username", MONGO_DB_USERNAME);
			cfgMap.put("mongo.db.password", MONGO_DB_PASSWORD);
			cfgMap.put("mongo.db.dbname", MONGO_DB_DBNAME);
		}
	}

	/**
	 * 初始化Mongo数据库
	 */
	private static void init() {
		initCfgMap();
		try {
			String address = cfgMap.get("mongo.db.address");
			int port = Integer.parseInt(cfgMap.get("mongo.db.port").toString());
			String dbName = cfgMap.get("mongo.db.dbname");
			String username = cfgMap.get("mongo.db.username");
			String password = cfgMap.get("mongo.db.password");
			mongo = new Mongo(address, port);
			if (dbName != null && !"".equals(dbName)) {
				db = mongo.getDB(dbName);
				if (username != null && !"".equals(username)) {
					db.addUser(username, password.toCharArray());
				}
				mongoDBs.put(dbName, db);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到Mongo的实例
	 * 
	 * @return
	 */
	public static Mongo getMongo() {
		return mongo;
	}

	/**
	 * 得到Mongo的图片数据库
	 * 
	 * @return
	 */
	public static DB getDB() {
		return db;
	}

	public static List<String> getDBNames() {
		return mongo.getDatabaseNames();
	}

	/**
	 * 根据数据库名称,得到数据库<br/>
	 * 如果不存在,则创建一个该名称的数据库,并设置用户名和密码为配置文件中的参数值</br>
	 * 
	 * @param dbName
	 * @return
	 */
	public static DB getDBByName(String dbName) {
		DB db = mongo.getDB(dbName);
		if (!mongoDBs.contains(db)) {
			db.addUser(cfgMap.get("mongo.db.username"), cfgMap.get(
					"mongo.db.password").toCharArray());
			mongoDBs.put(dbName, db);
		}
		return db;
	}

}