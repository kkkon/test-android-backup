/*
 * The MIT License
 *
 * Copyright 2014 Kiyofumi Kondoh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jp.ne.sakura.kkkon.test.android.backup.lib;

import android.app.backup.BackupManager;
import android.app.backup.RestoreObserver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.Log;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kiyofumi Kondoh
 */
public class KKBackupHelper
{
    private static final String TAG = "kk-Backup-Helper";
    private static final String ANDROID_BACKUP_APIKEY = "com.google.android.backup.api_key";

    private static boolean isAndroid = false;
    private static Context mContext = null;
    private static BackupManager mBM = null;

    private static ApplicationInfo getApplicationInfoFromPackageManager( final int flags )
    {
        assert null != mContext;

        if ( null == mContext )
        {
            return null;
        }

        final PackageManager pm = mContext.getPackageManager();
        if ( null == pm )
        {
            return null;
        }

        ApplicationInfo appInfo = null;
        try
        {
            appInfo = pm.getApplicationInfo( mContext.getPackageName(), flags );
        }
        catch ( PackageManager.NameNotFoundException e )
        {
            
        }

        return appInfo;
    }

    public static synchronized void initialize( final Context context )
    {
        {
            Class<?>    clazz = null;
            try
            {
                clazz = Class.forName( "android.os.Build" );
                isAndroid = true;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(KKBackupHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        mContext = context;
        mBM = new BackupManager( context );

        {
            final ApplicationInfo appInfo = mContext.getApplicationInfo();
            final String backupAgentName = appInfo.backupAgentName; /* API-8 */
            Log.d( TAG, "backupAgentName=" + backupAgentName );
            boolean haveBackupApiKey = false;
            if ( null != appInfo.metaData )
            {
                if ( appInfo.metaData.containsKey( ANDROID_BACKUP_APIKEY ) )
                {
                    final String backupApiKey = appInfo.metaData.getString(ANDROID_BACKUP_APIKEY);
                    Log.d( TAG, "backupApiKey from Context=" + backupApiKey );
                    haveBackupApiKey = true;
                }
                else
                {
                    Log.d( TAG, "metaData from Context not contain key=" + ANDROID_BACKUP_APIKEY );
                }
            }

            if ( false == haveBackupApiKey )
            {
                final ApplicationInfo appInfoFromPM = getApplicationInfoFromPackageManager( PackageManager.GET_META_DATA );
                if ( null != appInfoFromPM )
                {
                    Log.d( TAG, "backupAgentName from PackageManager=" + appInfoFromPM.backupAgentName );
                    Log.d( TAG, "metaData from PakcageManager=" + appInfoFromPM.metaData );
                    if ( null != appInfoFromPM.metaData )
                    {
                        if ( appInfoFromPM.metaData.containsKey( ANDROID_BACKUP_APIKEY ) )
                        {
                            final String backupApiKey = appInfoFromPM.metaData.getString(ANDROID_BACKUP_APIKEY);
                            Log.d( TAG, "backupApiKey from PackageManager=" + backupApiKey );
                            haveBackupApiKey = true;
                        }
                        else
                        {
                            Log.d( TAG, "metaData from PackageManager not contain key=" + ANDROID_BACKUP_APIKEY );
                        }
                    }
                }

            }

            if ( false == haveBackupApiKey )
            {
                Log.e( TAG, "AndroidManifest.xml doesn't contain\n" + "<application><meta-data android:name=\"" + ANDROID_BACKUP_APIKEY + "\" android:value=\"xxxxxx\" /></application>" );
                Log.e( TAG, "register url http://code.google.com/android/backup/signup.html" );
            }

        }
    }

    public static void requestBackup()
    {
        mBM.dataChanged();
    }

    public static void requestRestore()
    {
        mBM.requestRestore( new RestoreObserver() {
            @Override
            public void restoreFinished(int error) {
                super.restoreFinished(error);
                if ( 0 == error )
                {
                    System.exit( 0 );
                }
            }
        });
    }

    public static SharedPref getSharedPreferences( final String prefName, final int prefMode )
    {
        final SharedPref sharedPref = new SharedPref();
        sharedPref.setSharedPreferencesName( prefName );
        sharedPref.setSharedPreferencesMode( prefMode );
        SharedPreferences pref = mContext.getSharedPreferences( prefName, prefMode );
        sharedPref.setSharedPreferences( pref );

        return sharedPref;
    }

}
