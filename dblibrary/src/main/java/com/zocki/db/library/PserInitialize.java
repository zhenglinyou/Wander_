package com.zocki.db.library;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.zocki.baselibrary.logger.LogUtils;
import com.zocki.db.library.config.GlobalException;
import com.zocki.db.library.tablemanager.TabModel;
import com.zocki.db.library.tablemanager.TableCreateManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaisheng3 on 2017/8/21.
 * 数据库初始化类
 */
public class PserInitialize {

    public static String dbName;
    public static int dbVersion;

    /**
     * 初始化DB
     * @param dbPath
     * @param dbName
     * @param version
     */
    private static void initDbInfo(String dbPath, String dbName, int version) {

        if( !TextUtils.isEmpty(dbPath) ) {

            File dbRoot = new File(dbPath);
            if( !dbRoot.exists() ) {
                boolean b = dbRoot.mkdirs();
            }

            if( dbPath.endsWith(File.separator) ) {
                dbName = dbPath + dbName;
            } else {
                dbName = dbPath + File.separator + dbName;
            }
        } else {
            dbName = PserInitialize.getContext().getExternalFilesDir("") + "/databases/" + dbName;
        }

        PserInitialize.dbName = dbName;
        PserInitialize.dbVersion = version;
    }


    /****************** 数据库表的模型 ********************/

    private static List<TabModel> tabModels = new ArrayList<>();

    public static List<TabModel> getTabModels() {
        return tabModels;
    }

    public static void addTableModels(List<TabModel> tabModels) {
        PserInitialize.tabModels.addAll(tabModels);
    }

    /**
     * Global application context.
     * 上下文
     */
    private static Context sContext = null;

    public static void initialize(Context context,String dbPath, String dbName, int version) {
        sContext = context;
        initDbInfo(dbPath,dbName,version);
    }

    public static Context getContext() {
        if (sContext == null) {
            throw new ExceptionInInitializerError(GlobalException.APPLICATION_CONTEXT_IS_NULL);
        }
        return sContext;
    }

    /**************秘钥************/

    private static String securityKey = "66666666";

    public static void setSecurityKey(String securityKey) {

        if( TextUtils.isEmpty(securityKey) ) {
            Log.e("PserInitialize ---> ", " PserInitialize#securityKey is null" );
            return ;
        }

        if( securityKey.length() < 8 || securityKey.length() % 8 != 0 ) {
            Log.e("PserInitialize ---> ", " PserInitialize#securityKey lenght < 8 or lenght % 8 != 0" );
            return ;
        }

        PserInitialize.securityKey = securityKey;
    }

    public static String getSecurityKey() {
        return securityKey;
    }
}
