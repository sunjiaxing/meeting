package com.zhengshang.meeting.dao.entity;
/**
 * 新闻栏目 数据库实体
 */
public class NewsChannel {
	public static final String KEY_COLUMN_ID = "_id";
	public static final String KEY_COLUMN_TYPE_ID = "type_id";
	public static final String KEY_COLUMN_NAME = "name";
	public static final String KEY_COLUMN_IS_LOCK = "is_lock";
	public static final String KEY_COLUMN_POSITION = "position";
	public static final String KEY_COLUMN_IS_MINE = "is_mine";
	public static final String KEY_COLUMN_MASTER_ID = "master_id";

	public static final String KEY_TABLE_NAME = "news_types";

	public static final String CREAT_TABLE = "CREATE TABLE " + KEY_TABLE_NAME
			+ " (" + KEY_COLUMN_ID + " INTEGER PRIMARY KEY,"
			+ KEY_COLUMN_TYPE_ID + " TEXT," + KEY_COLUMN_NAME + " TEXT,"
			+ KEY_COLUMN_IS_LOCK + " INTEGER," + KEY_COLUMN_POSITION
			+ " INTEGER," + KEY_COLUMN_IS_MINE + " INTEGER DEFAULT 0, "
			+ KEY_COLUMN_MASTER_ID + " TEXT " + ")";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ KEY_TABLE_NAME;
	public static final String DELETE_TABLE_DATA = "DELETE FROM "
			+ KEY_TABLE_NAME;
	/** 类别id */
	private String typeId;
	/** 类别名称 */
	private String name;
	/** 是否锁定 */
	private int isLock;
	/** 栏目所在位置 */
	private int position;
	/** 表示是否选中 */
	private int isMine;
	/** 登录账户的Id */
	private String masterId;

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIsLock() {
		return isLock;
	}

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getIsMine() {
		return isMine;
	}

	public void setIsMine(int isMine) {
		this.isMine = isMine;
	}

	public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

}
