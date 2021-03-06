package com.zocki.db.library.impl;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.zocki.baselibrary.logger.LogUtils;
import com.zocki.db.library.PserInitialize;
import com.zocki.db.library.annotation.attr.ColumnAttr;
import com.zocki.db.library.security.DESCoderUtils;
import com.zocki.db.library.tablemanager.TableCreateManager;
import com.zocki.db.library.utils.DaoUtil;
import com.zocki.db.library.dao.IDBDao;
import com.zocki.db.library.curd.Query;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBDaoImpl<T> implements IDBDao<T> {

    private SQLiteDatabase mSqLiteDatabase;
    private Class<T> mClazz;
    private final Object[] mPutMethondAttrs = new Object[2];
    private final Map<String,Method> mPutMethods = new HashMap<>();
    private Query<T> mQuerySupport;

    public void init(SQLiteDatabase sqLiteDatabase, Class<T> clazz) {
        this.mSqLiteDatabase = sqLiteDatabase;
        this.mClazz = clazz;
        // 可能后期加入表
        TableCreateManager.createTable(sqLiteDatabase,clazz);
    }

    @Override
    public long insert(T obj) {
        ContentValues values = transfromContentValue( obj );
        return mSqLiteDatabase.insert( DaoUtil.getTableName(obj.getClass()),null, values );
    }

    @Override
    public void insert(List<T> datas) {
        if( datas == null ) return ;
        try {
            mSqLiteDatabase.beginTransaction();

            for (T data : datas) { insert( data ); }

            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mSqLiteDatabase.endTransaction();
        }
    }

    /**
     * 查询所有
     * @return All
     */
    @Override
    public Query<T> query() {
        if( mQuerySupport == null ) mQuerySupport = new Query<>(mSqLiteDatabase,mClazz);
        return mQuerySupport;
    }

    @Override
    public int delete(String whereClause,String[] whereArgs) {
        return mSqLiteDatabase.delete(DaoUtil.getTableName(mClazz),whereClause,whereArgs);
    }

    @Override
    public int update(T obj, String whereClause, String... whereArgs) {
        ContentValues values = transfromContentValue(obj);
        return mSqLiteDatabase.update(DaoUtil.getTableName(mClazz),values,whereClause,whereArgs);
    }

    /**
     * Obj 转成 contentValues
     * @param obj
     * @return
     */
    private ContentValues transfromContentValue(T obj) {

        ContentValues values = new ContentValues();
        for (Field field : obj.getClass().getDeclaredFields()) {
            try {

                ColumnAttr columnAttr = DaoUtil.getColumnAttr(field);
                if( columnAttr == null ) continue;

                Object value = field.get( obj );

                if( columnAttr.isSecurity() ) {
                    value = DESCoderUtils.encrypt(value.toString().getBytes(), PserInitialize.getSecurityKey());
                }

                mPutMethondAttrs[0] = columnAttr.getColumnName();
                mPutMethondAttrs[1] = value;

                if( value == null && !TextUtils.isEmpty(columnAttr.getDefaultValue()) ) {
                    mPutMethondAttrs[1] = columnAttr.getDefaultValue();
                } else if( value == null ){
                    continue;
                }

                String fieldNameType = field.getType().getName();
                Method putMethod = mPutMethods.get(fieldNameType);
                if( putMethod == null && value != null ) {
                    // 插入方法
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    mPutMethods.put(fieldNameType,putMethod);
                }

                if( putMethod == null ) {
                    continue;
                }

                // putMethod.invoke(values, key, value);
                // 优化
                putMethod.invoke(values,mPutMethondAttrs);

                // value 需要制定确定类型
                // values.put(field.getName(), value);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPutMethondAttrs[0] = mPutMethondAttrs[1] = null;
            }
        }
        return values;
    }
}
