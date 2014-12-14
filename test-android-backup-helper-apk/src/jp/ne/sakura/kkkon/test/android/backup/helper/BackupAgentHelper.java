package jp.ne.sakura.kkkon.test.android.backup.helper;

import jp.ne.sakura.kkkon.test.android.backup.lib.KKBackupAgentHelper;

public class BackupAgentHelper extends KKBackupAgentHelper
{

    @Override
    protected ParamSharedPreferences[] getParamSharedPreferences()
    {
        ParamSharedPreferences[] params = null;

        params = new ParamSharedPreferences[] {
            new ParamSharedPreferences(
                MainActivity.BACKUP_SHAREDPREF
                , new String[] {
                    MainActivity.NAME_SHAREDPREF_PREF
                } )
        };

        return params;
    }

    @Override
    protected ParamFile[] getParamFile()
    {
        ParamFile[] params = null;

        return params;
    }


}
