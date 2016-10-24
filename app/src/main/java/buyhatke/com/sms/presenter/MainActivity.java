package buyhatke.com.sms.presenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import buyhatke.com.sms.R;
import buyhatke.com.sms.adapter.SmsAdapter;
import buyhatke.com.sms.model.CustomSms;

public class MainActivity extends AppCompatActivity  {


    RecyclerView rv;
    SmsAdapter adapter;
    FloatingActionButton fab;
    Cursor c;
    ExportTask task;
    ArrayList<CustomSms> smslistSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        smslistSearch = new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.messages_view);

        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        rv.setLayoutManager(llm);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this,SendSms.class);
                startActivity(i);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();


        try {


            final ArrayList<CustomSms> smslist, smsgrouplist;

            smslist = new ArrayList<>();
            smsgrouplist = new ArrayList<>();

            c = getContentResolver().query(SmsApplication.INBOX_URI, null, null, null, null);


            while (c.moveToNext()) {
                String address = c.getString(c.getColumnIndexOrThrow("address"));
                String date = c.getString(c.getColumnIndexOrThrow("date"));
                String body = c.getString(c.getColumnIndexOrThrow("body"));

                smslist.add(new CustomSms(address, date, body));
            }

       /*for(int i=smslist.size()-1;i>=0;i--)
        {
            String s = smslist.get(i).address;

            for(int j=smslist.size()-1;j>=0;j--)
            {
                if(i!=j) {
                    if (smslist.get(j).address.equals(s)) {

                    }
                }
            }
        }*/

            smslistSearch = smslist;
            Map<String, CustomSms> map = new LinkedHashMap<>();

            for (CustomSms ays : smslist) {

                CustomSms existingValue = map.get(ays.address);
                if(existingValue == null){
                    map.put(ays.address, ays);
                }
            }

            smsgrouplist.clear();
            smsgrouplist.addAll(map.values());


            adapter = new SmsAdapter(MainActivity.this);
            adapter.updateList(smsgrouplist);
            rv.setAdapter(adapter);

            rv.addOnItemTouchListener(
                    new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            // TODO Handle item click
                            ArrayList<CustomSms> smsinsidegroup = new ArrayList<CustomSms>();

                            String n = smsgrouplist.get(position).address;

                            for (int i = 0; i < smslist.size(); i++) {
                                if(smslist.get(i).address.equals(n))
                                   smsinsidegroup.add(smslist.get(i));
                            }

                            Intent i = new Intent(MainActivity.this, ReadAllSms.class);
                            i.putParcelableArrayListExtra("messages", smsinsidegroup);
                            startActivity(i);
                        }
                    })
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    class ExportTask extends AsyncTask<Void, Integer, Uri> {

    ProgressDialog pDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Exporting to file ...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgress(0);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected Uri doInBackground(Void... params) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            FileOutputStream fos = null;
            try {
                File f = new File(Environment.getExternalStorageDirectory(), "QuickSmsBackUp.txt");
                fos = new FileOutputStream(f);
                int count = c.getCount(), i = 0;

                StringBuilder sb = new StringBuilder();
                if (c.moveToFirst()) {
                    do {
                        sb.append(c.getString(c.getColumnIndex("address")))
                                .append("\n");
                        sb.append(c.getString(c.getColumnIndex("body")))
                                .append("\n");
                        sb.append("\n");
                        publishProgress(++i*100/count);
                    } while (!isCancelled() && c.moveToNext());
                }
                fos.write(sb.toString().getBytes());
                return Uri.fromFile(f);

            } catch (Exception e) {
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {}
                }
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        pDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Uri result) {
        super.onPostExecute(result);
        pDialog.dismiss();

        if (result == null) {
            Toast.makeText(MainActivity.this, "Export task failed!",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent(MainActivity.this,UploadData.class);
        startActivity(i);
    }

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent i = new Intent(MainActivity.this,SearchActivity.class);
            i.putParcelableArrayListExtra("search", smslistSearch);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_share) {
            /*Intent i = new Intent(MainActivity.this,UploadData.class);
            startActivity(i);*/

            task = new ExportTask();
            task.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (task != null) {
            task.cancel(false);
            task.pDialog.dismiss();
        }
        super.onPause();
    }

}
