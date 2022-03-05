package pl.ls4soft.sscc.Items;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pl.ls4soft.sscc.CalcActivity;
import pl.ls4soft.sscc.R;

public class BusActivity extends AppCompatActivity {

    //variables
    private EditText BusNameET, BusVoltageET, xkET, rkET, zkET, BikBisET, BipET;
    private Button okBtn, cancelBtn, delBtn;


    //variables
    private  String name = null;
    private double  voltage = 0d, scImpedance = 0d, scReactance = 0d, scResistance = 0d, scCurrent = 0d, scImpCurrent = 0d;

    private boolean allParametersEnetered = true;
    private boolean wasAChange = false;
    private boolean wasEdited = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BusNameET = (EditText) findViewById(R.id.BusNameET);
//        BusVoltageET = (EditText) findViewById(R.id.BusVoltageET);
        xkET = (EditText) findViewById(R.id.xkET);
        rkET = (EditText) findViewById(R.id.rkET);
        zkET = (EditText) findViewById(R.id.zkET);
        BikBisET = (EditText) findViewById(R.id.BikBisET);
        BipET = (EditText) findViewById(R.id.BipET);

        okBtn = (Button) findViewById(R.id.TOkBtn);
        cancelBtn = (Button) findViewById(R.id.TCancelBtn);
        delBtn = (Button) findViewById(R.id.TDeleteBtn);

        Intent intentIn = getIntent();
        Bundle bundleIn = intentIn.getExtras();
        String[] dataArrInS;
        double[] dataArrInD;
        if(bundleIn != null) {
            dataArrInS = bundleIn.getStringArray("data1");
            name = dataArrInS[0];

            dataArrInD = bundleIn.getDoubleArray("data2");

            voltage = dataArrInD[0];
            scImpedance = dataArrInD[1];
            scReactance = dataArrInD[2];
            scResistance = dataArrInD[3];
            scCurrent = dataArrInD[4];
            scImpCurrent = dataArrInD[5];

            wasEdited = bundleIn.getBoolean("data4");

        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                wasAChange = true;
                wasEdited = true;
            }
        };

        if(name == null) {
//            BusNameET.setText("B1");
        }
        else
            BusNameET.setText(name);

//        if(voltage == 0d) {
//        }
//        else
//            BusVoltageET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(voltage)));

        if(scImpedance == 0d) {
        }
        else
//            zkET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(scImpedance)));
            zkET.setText(CalcActivity.noExponentsString(scImpedance));

        if(scReactance == 0d) {
        }
        else
//            xkET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(scReactance)));
            xkET.setText(CalcActivity.noExponentsString(scReactance));

        if(scResistance == 0d) {
        }
        else
//            rkET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(scResistance)));
            rkET.setText(CalcActivity.noExponentsString(scResistance));

        if(scCurrent == 0d) {
        }
        else
//            BikBisET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(scCurrent)));
            BikBisET.setText(CalcActivity.noExponentsString(scCurrent));
        if(scImpCurrent == 0d) {
        }
        else
//            BipET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(scImpCurrent)));
            BipET.setText(CalcActivity.noExponentsString(scImpCurrent));

        BusNameET.addTextChangedListener(textWatcher);
//        BusVoltageET.addTextChangedListener(textWatcher);


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    name = BusNameET.getText().toString();
//                    voltage = Double.valueOf(BusVoltageET.getText().toString());

                    String[] dataArrOutS = new String[1];
                    dataArrOutS[0] = name;

                    double[] dataArrOutD = new double[6];
                    dataArrOutD[0] = voltage;
                    dataArrOutD[1] = scImpedance;
                    dataArrOutD[2] = scReactance;
                    dataArrOutD[3] = scResistance;
                    dataArrOutD[4] = scCurrent;
                    dataArrOutD[5] = scImpCurrent;

                    allParametersEnetered = true;


                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("data1", dataArrOutS);
                    returnIntent.putExtra("data2", dataArrOutD);
                    returnIntent.putExtra("data3", allParametersEnetered);
                    returnIntent.putExtra("data4", wasEdited);

                    setResult(RESULT_OK, returnIntent);

                    finish();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),  getString(R.string.enter_all_values), Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonAction();

            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wasAChange || wasEdited) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    setResult(CalcActivity.RESULT_DELETE);
                                    finish();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(BusActivity.this);
                    builder.setMessage(getString(R.string.are_u_sure)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                            .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();
                }
                else {
                    setResult(CalcActivity.RESULT_DELETE);
                    finish();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            backButtonAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backButtonAction();
    }

    private void backButtonAction()
    {
        if (wasAChange) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(BusActivity.this);
            builder.setMessage(getString(R.string.ignore_changes_q)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                    .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();
        }
        else {
            finish();

        }
    }
}
