package sparkish.getsengo.com.gettender.system;

import android.content.Context;

import sparkish.getsengo.com.gettender.R;

/**
 * Created by cyclone on 06/07/15.
 */
public class config {
    public static final String   db_name    = "GetTender";
    static final        String   db_version = "1";
    static              String[] db_default = new String[3];
    static              String[][] db_upgrade = new String[0][0];
    static final        String   db_reset   = "delete from wellcomeInstruction; delete from login;  delete from konfigurasi";
    public static final String   url_faq    = "http://gettender.co.id/?i=faq.index";
    public String url_api = "";
    //public static final int      timer_cron     = 10000;// 1detik
    Context ctx;

    public config(Context ctxne) {
        this.ctx = ctxne;

        url_api = ctx.getResources().getString(R.string.api);

        db_default[0] = "CREATE TABLE wellcomeInstruction (versi TEXT NULL, status TEXT NULL);";
        db_default[1] = "CREATE TABLE login (" +
                "email VARCHAR (100) PRIMARY KEY, " +
                "password VARCHAR (50), " +
                "nama VARCHAR (100));";
        db_default[2] = "CREATE TABLE konfigurasi (param VARCHAR (100), nilai VARCHAR (100));";
    }
}
