package com.pierocheng.gugu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Created by PieroCheng on 2016/5/18.
 */
public class RegName extends Activity {
    public final static String SER_KEY = "com.PieroCheng.ser";
    public final static String SER_REG = "com.PieroCheng.reg";
    public final static String SER_NAME = "com.PieroCheng.name";
    public final static String SER_ADDRESS = "com.PieroCheng.address";
    private EditText name, address;
    private Button regbtn;
    private Boolean ISREG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regname);

        name = (EditText) findViewById(R.id.et_username);
        address = (EditText) findViewById(R.id.et_address);
        regbtn = (Button) findViewById(R.id.btn_register);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegName.this,Touch.class);
                Bundle mBundle = new Bundle();
                Bundle mBunde1 = new Bundle();
                Bundle mBunde2 = new Bundle();
                Bundle mBunde3 = new Bundle();
                mBundle.putSerializable(SER_KEY, getIntent().getSerializableExtra(HomePage.SER_KEY));
                mBunde1.putSerializable(SER_REG, ISREG);
                mBunde2.putSerializable(SER_NAME, name.getText().toString());
                mBunde3.putSerializable(SER_ADDRESS, address.getText().toString());
                intent.putExtras(mBundle);
                intent.putExtras(mBunde1);
                intent.putExtras(mBunde2);
                intent.putExtras(mBunde3);
                startActivity(intent);
            }
        });
    }
}
