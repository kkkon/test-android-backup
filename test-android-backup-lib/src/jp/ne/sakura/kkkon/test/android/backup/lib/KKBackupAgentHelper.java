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

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.IOException;

/**
 *
 * @author Kiyofumi Kondoh
 */
public abstract class KKBackupAgentHelper extends BackupAgentHelper
{
    private static final String TAG = "kk-Backup-Helper";

    public class ParamSharedPreferences
    {
        public String   keyPrefix;
        public String[] prefNames;

        public ParamSharedPreferences(String keyPrefix, String[] prefNames)
        {
            this.keyPrefix = keyPrefix;
            this.prefNames = prefNames;
        }

    }
    public class ParamFile
    {
        public String   keyPrefix;
        public String[] fileNames;

        public ParamFile(String keyPrefix, String[] fileNames)
        {
            this.keyPrefix = keyPrefix;
            this.fileNames = fileNames;
        }

    }

    protected abstract ParamSharedPreferences[] getParamSharedPreferences();

    protected abstract ParamFile[] getParamFile();

    @Override
    public void onCreate() {
        Log.d( TAG, "KKBackupAgentHelper#onCreate");
        super.onCreate();

        {
            final ParamSharedPreferences[] params = getParamSharedPreferences();
            if ( null != params )
            {
                for ( final ParamSharedPreferences param : params )
                {
                    final String keyPrefix = param.keyPrefix;
                    final String[] prefNames = param.prefNames;

                    SharedPreferencesBackupHelper   helper =
                            new SharedPreferencesBackupHelper( this, prefNames );

                    addHelper( keyPrefix, helper );
                }
            }
        }
        {
            final ParamFile[] params = getParamFile();
            if ( null != params )
            {
                for ( final ParamFile param : params )
                {
                    final String keyPrefix = param.keyPrefix;
                    final String[] fileNames = param.fileNames;

                    FileBackupHelper    helper =
                            new FileBackupHelper( this, fileNames );

                    addHelper( keyPrefix, helper );
                }
            }
        }
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        Log.d( TAG, "KKBackupAgentHelper#onBackup");
        super.onBackup(oldState, data, newState);
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        Log.d( TAG, "KKBackupAgentHelper#onRestore");
        Log.d( TAG, " appVersionCode=" + appVersionCode );
        super.onRestore(data, appVersionCode, newState);
    }

}
