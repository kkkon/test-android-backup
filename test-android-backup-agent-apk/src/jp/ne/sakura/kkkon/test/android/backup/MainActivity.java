package jp.ne.sakura.kkkon.test.android.backup;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.UUID;
import jp.ne.sakura.kkkon.test.android.backup.lib.KKBackupHelper;
import jp.ne.sakura.kkkon.test.android.backup.lib.SharedPref;

public class MainActivity extends Activity implements View.OnClickListener
{
    private static final String TAG = "kk-Backup-Apk";

    private LinearLayout        mLayout;

    private TextView            mTextViewUUID;
    private Button              mButtonBackupRequestBackup;
    private Button              mButtonBackupRequestRestore;
    private Button              mButtonCreateSharedPreferences;

    public static final String NAME_SHAREDPREF_PREF = "pref";
    public static final String BACKUP_SHAREDPREF = "backupSharedKeys";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d( TAG, "MainActivity#onCreate:");
        super.onCreate(savedInstanceState);

        {
            final String prefName = this.getClass().getName();
            final Context contextApp = this.getApplicationContext();
            KKBackupHelper.initialize( contextApp );
        }

        mLayout = new LinearLayout( this );
        mLayout.setOrientation( LinearLayout.VERTICAL );

        {
            mTextViewUUID = new TextView(this);
            mTextViewUUID.setText( "uuid=" );

            final SharedPref pref = KKBackupHelper.getSharedPreferences( NAME_SHAREDPREF_PREF, Activity.MODE_PRIVATE );
            if ( null != pref )
            {
                if ( pref.contains("uuid") )
                {
                    final String uuid = pref.getString( "uuid" );
                    mTextViewUUID.setText( "uuid=" + uuid );
                }
            }
            
            mLayout.addView( mTextViewUUID );
        }
        mButtonBackupRequestBackup = new Button( this );
        mButtonBackupRequestBackup.setText( "Backup: request backup" );
        mButtonBackupRequestBackup.setOnClickListener( this );
        mLayout.addView( mButtonBackupRequestBackup );

        mButtonBackupRequestRestore  = new Button( this );
        mButtonBackupRequestRestore.setText( "Backup: request restore" );
        mButtonBackupRequestRestore.setOnClickListener( this );
        mLayout.addView( mButtonBackupRequestRestore );

        mButtonCreateSharedPreferences = new Button( this );
        mButtonCreateSharedPreferences.setText( "Create SharedPreferences" );
        mButtonCreateSharedPreferences.setOnClickListener( this );
        mLayout.addView( mButtonCreateSharedPreferences );

        this.setContentView( mLayout );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void createSharedPreferences()
    {
        final SharedPref pref = KKBackupHelper.getSharedPreferences( NAME_SHAREDPREF_PREF, Activity.MODE_PRIVATE );
        if ( null != pref )
        {
            if ( pref.contains("uuid") )
            {
            }
            else
            {
                final SharedPreferences.Editor editor = pref.modifyStart();
                final UUID uuid = UUID.randomUUID();
                if ( null != uuid )
                {
                    final String strUUID = uuid.toString();
                    editor.putString( "uuid", strUUID );
                }
                final boolean writeOK = editor.commit(); // syncronous
                if ( false == writeOK )
                {
                    Log.e( TAG, "SharedPreferences.Editor#commit failed!!" );
                }
                pref.modifyFinish();
            }

            {
                final String uuid = pref.getString("uuid");
                mTextViewUUID.setText( "uuid=" + uuid );
            }

        }
    }

    public void onClick(View view)
    {
        if ( this.mButtonCreateSharedPreferences == view )
        {
            this.createSharedPreferences();
        }
        else if ( this.mButtonBackupRequestBackup == view )
        {
            KKBackupHelper.requestBackup();
        }
        else if ( this.mButtonBackupRequestRestore == view )
        {
            KKBackupHelper.requestRestore();
        }
    }


}
