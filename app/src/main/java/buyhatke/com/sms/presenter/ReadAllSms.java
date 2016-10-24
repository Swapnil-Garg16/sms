package buyhatke.com.sms.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;

import buyhatke.com.sms.R;
import buyhatke.com.sms.adapter.SmsAdapter;
import buyhatke.com.sms.model.CustomSms;

public class ReadAllSms extends AppCompatActivity {

    RecyclerView rv;
    SmsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_all_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = this.getIntent();
        ArrayList<CustomSms> messageList = i.getParcelableArrayListExtra("messages");

        getSupportActionBar().setTitle(messageList.get(0).address);

        rv = (RecyclerView) findViewById(R.id.single_address_view);

        LinearLayoutManager llm = new LinearLayoutManager(ReadAllSms.this);
        rv.setLayoutManager(llm);

        adapter = new SmsAdapter(ReadAllSms.this);
        adapter.updateList(messageList);
        rv.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {

        Toast.makeText(getApplicationContext(),"Use the up button to return",Toast.LENGTH_LONG).show();
    }
}
