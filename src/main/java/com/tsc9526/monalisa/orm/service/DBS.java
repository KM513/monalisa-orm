/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.orm.service;

import java.util.ArrayList;
import java.util.List;

import com.tsc9526.monalisa.orm.datasource.DBConfig;
import com.tsc9526.monalisa.orm.datatable.DataMap;
import com.tsc9526.monalisa.orm.dialect.Dialect;
import com.tsc9526.monalisa.orm.service.actions.ActionLocator;
import com.tsc9526.monalisa.orm.tools.logger.Logger;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBS {
	static Logger logger = Logger.getLogger(DBS.class);
 	
	public static DataMap dbs = new DataMap();
  	 
	public static void add(String dbName, DBConfig db) {
		add(dbName, db, new ActionLocator());
	}

	public synchronized static void add(String dbName, DBConfig db, ActionLocator locator) {
		dbName = Dialect.getRealname(dbName);

		if (!dbs.containsKey(dbName)) {
			DBS s = new DBS(dbName, db, locator);

			logger.info("Add DB service" + locator.getMethods() + ": /" + dbName + ", dbkey: " + db.getKey());
			dbs.put(dbName, s);
		} else {
			logger.info("Database service existed: /" + dbName + ", dbkey: " + db.getKey());
		}
	}

	public static DBS getDB(String dbName) {
		dbName = Dialect.getRealname(dbName);

		return (DBS) dbs.get(dbName);
	}
	
	public static List<String> getNames(){
		List<String> names=new ArrayList<String>();
		for(Object db:dbs.values()){
			names.add( ((DBS)db).getDbName());
		}
		
		return names;
	}
	
	public static void remove(String dbName) {
		dbName = Dialect.getRealname(dbName);

		DBS s = (DBS) dbs.remove(dbName);
		if (s != null) {
			logger.info("Removed DB service" + s.locator.getMethods()+ ": /" + dbName);
		}
	}

	public static void clear() {
		dbs.clear();
	}
 

	private String dbName;

	private DBConfig db;

	private ActionLocator locator;
 
	private DBS(String dbName, DBConfig db, ActionLocator locator) {
		this.dbName = dbName;

		this.db = db;

		this.locator = locator;
	}

	public DBConfig getDB() {
		return this.db;
	}
	 
	public ActionLocator getLocator() {
		return locator;
	}

	public String getDbName() {
		return dbName;
	}

	public void setLocator(ActionLocator locator) {
		this.locator = locator;
	}
}
