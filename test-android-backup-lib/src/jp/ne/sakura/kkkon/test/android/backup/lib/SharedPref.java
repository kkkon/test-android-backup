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

import android.app.Activity;
import android.content.SharedPreferences;

/**
 *
 * @author Kiyofumi Kondoh
 */
public class SharedPref
{
    private String  prefName = "pref";
    private int     prefMode = Activity.MODE_PRIVATE;

    private SharedPreferences mSharedPref = null;

    public void setSharedPreferencesName( final String name )
    {
        assert null == mSharedPref;

        assert null != name;
        assert 0 < name.length();

        prefName = name;
    }

    public void setSharedPreferencesMode( final int mode )
    {
        assert null == mSharedPref;

        prefMode = mode;
    }

    protected void setSharedPreferences( final SharedPreferences pref )
    {
        this.mSharedPref = pref;
    }


    public boolean contains( final String key )
    {
        return this.mSharedPref.contains( key );
    }

    public String getString( final String key )
    {
        return this.mSharedPref.getString( key, null );
    }

    public SharedPreferences.Editor modifyStart()
    {
        if ( null == this.mSharedPref )
        {
            return null;
        }

        return this.mSharedPref.edit();
    }

    public void modifyFinish()
    {
        KKBackupHelper.requestBackup();
    }

}
