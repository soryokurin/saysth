package com.saysth.core.mongodb;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSFile;

public class MongoDBManager{

	private String dbName;
	private String collName;
	private DB db;

	public MongoDBManager(String dbName, String collName) {
		this.dbName = dbName;
		this.collName = collName;
		try {
			db = MongoDBConfig.getDBByName(this.dbName);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public MongoDBManager() {
		getDb();
	}

	public DBCollection getCollection() {
		return db.getCollection(this.collName);
	}

	public DBObject map2Obj(Map<String, Object> map) {
		DBObject obj = new BasicDBObject();
		if (map.containsKey("class") && map.get("class") instanceof Class)
			map.remove("class");
		obj.putAll(map);
		return obj;
	}

	public DBObject insert(DBObject obj) {
		getCollection().insert(obj);
		return obj;
	}

	public void insertBatch(List<DBObject> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		List<DBObject> listDB = new ArrayList<DBObject>();
		for (int i = 0; i < list.size(); i++) {
			listDB.add(list.get(i));
		}
		getCollection().insert(listDB);
	}

	public void delete(DBObject obj) {
		getCollection().remove(obj);
	}

	public void deleteBatch(List<DBObject> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			getCollection().remove(list.get(i));
		}
	}

	public long getCollectionCount() {
		return getCollection().getCount();
	}

	public long getCount(DBObject obj) {
		if (obj != null)
			return getCollection().getCount(obj);
		return getCollectionCount();
	}

	public List<DBObject> find(DBObject obj) {
		DBCursor cur = getCollection().find(obj);
		return DBCursor2list(cur);
	}

	public List<DBObject> find(DBObject query, DBObject sort) {
		DBCursor cur;
		if (query != null) {
			cur = getCollection().find(query);
		} else {
			cur = getCollection().find();
		}
		if (sort != null) {
			cur.sort(sort);
		}
		return DBCursor2list(cur);
	}

	public List<DBObject> find(DBObject query, DBObject sort, int start,
			int limit) {
		DBCursor cur;
		if (query != null) {
			cur = getCollection().find(query);
		} else {
			cur = getCollection().find();
		}
		if (sort != null) {
			cur.sort(sort);
		}
		if (start == 0) {
			cur.batchSize(limit);
		} else {
			cur.skip(start).limit(limit);
		}

		return DBCursor2list(cur);
	}

	private List<DBObject> DBCursor2list(DBCursor cur) {
		List<DBObject> list = new ArrayList<DBObject>();
		if (cur != null) {
			list = cur.toArray();
		}
		return list;
	}

	public void update(DBObject setFields, DBObject whereFields) {
		getCollection().updateMulti(setFields, whereFields);
	}

	public List<DBObject> findAll() {
		DBCursor cur = getCollection().find();
		List<DBObject> list = new ArrayList<DBObject>();
		if (cur != null) {
			list = cur.toArray();
		}

		return list;
	}

	public DBObject getById(String id) {
		DBObject obj = new BasicDBObject();
		obj.put("_id", new ObjectId(id));
		DBObject result = getCollection().findOne(obj);
		return result;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
		this.db = MongoDBConfig.getDBByName(this.dbName);
	}

	public String getCollName() {
		return collName;
	}

	public void setCollName(String collName) {
		this.collName = collName;
	}

	public DB getDb() {
		if (this.db == null) {
			if (this.dbName == null) {
				this.db = MongoDBConfig.getDB();
			} else {
				this.db = MongoDBConfig.getDBByName(this.dbName);
			}
		}
		return this.db;
	}

	public List<String> getAllDBNames() {
		return MongoDBConfig.getDBNames();
	}

	public boolean saveFile(Object obj, HashMap<String, Object> paramsMap)
			throws IOException {
		boolean flag = false;

		GridFS gridFS = new GridFS(db);

		GridFSFile gridFSFile = null;
		if (obj instanceof InputStream) {
			gridFSFile = gridFS.createFile((InputStream) obj);
		} else if (obj instanceof byte[]) {
			gridFSFile = gridFS.createFile((byte[]) obj);
		} else if (obj instanceof File) {
			gridFSFile = gridFS.createFile((File) obj);
		}
		if (gridFSFile != null && paramsMap != null) {
			Iterator iter = paramsMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, Object> entry = (Entry<String, Object>) iter
						.next();
				gridFSFile.put(entry.getKey(), entry.getValue());
			}
			gridFSFile.save();
			flag = true;
		}
		return flag;
	}

	/**
	 * 通过gridFS删除
	 * 
	 * @param paramsMap
	 *            参数map
	 * @return
	 */
	public boolean deleteFile(HashMap<String, Object> paramsMap) {
		boolean flag = false;
		GridFS gridFS = new GridFS(db);
		DBObject query = map2Obj(paramsMap);
		
		DBObject obj = gridFS.findOne(query);
		if (obj != null) {
			gridFS.remove(obj);
			flag = true;
		}
		return flag;
	}

}
