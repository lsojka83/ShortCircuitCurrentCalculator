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

public class UtilityActivity extends AppCompatActivity{

    //Utility variables

    private EditText UtilityNameET, scPowerET, voltageET, zqET, xqET, rqET, UikBisET;
    private Button okBtn, cancelBtn, delBtn;

//    //Utility variables
//    private String utilityName = null, utilityScPower = null, utilityNominalVoltage = null, utilityImpedance = null, utilityReactance = null, utilityResistance = null, utilityCurrent = null;

    //Utility variables
    private String utilityName = null;
    private double utilityScPower = 0d, utilityNominalVoltage = 0d, utilityImpedance = 0d, utilityReactance  = 0d, utilityResistance = 0d, utilityCurrent = 0d;

    private boolean allParametersEnetered = false;
    private boolean wasAChange = false;
    private boolean wasEdited = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UtilityNameET = (EditText) findViewById(R.id.UtilityNameET);
        scPowerET = (EditText) findViewById(R.id.SCPowerET);
        voltageET = (EditText) findViewById(R.id.voltageET);
        zqET = (EditText) findViewById(R.id.zqET);
        xqET = (EditText) findViewById(R.id.xqET);
        rqET = (EditText) findViewById(R.id.rqET);
        UikBisET = (EditText) findViewById(R.id.UikBisET);

        okBtn = (Button) findViewById(R.id.OkBtn);
        cancelBtn = (Button) findViewById(R.id.CancelBtn);
        delBtn = (Button) findViewById(R.id.UDeleteBtn);

        Intent intentIn = getIntent();
        Bundle bundleIn = intentIn.getExtras();
        String[] dataArrInS;
        double[] dataArrInD;
        if(bundleIn != null) {
            dataArrInS = bundleIn.getStringArray("data1");
            utilityName = dataArrInS[0];

            dataArrInD = bundleIn.getDoubleArray("data2");

            utilityScPower = dataArrInD[0];
            utilityNominalVoltage = dataArrInD[1];
            utilityImpedance = dataArrInD[2];
            utilityReactance = dataArrInD[3];
            utilityResistance = dataArrInD[4];
            utilityCurrent = dataArrInD[5];

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

        if(utilityName == null)
        UtilityNameET.setText("U1");
        else
            UtilityNameET.setText(utilityName);

        if(utilityScPower == 0d) {
//            scPowerET.setText("250");
        }
        else
            scPowerET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(utilityScPower)));

        if(utilityNominalVoltage == 0d) {
//        voltageET.setText("15");
        }
        else
            voltageET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(utilityNominalVoltage)));

        if(utilityImpedance != 0d)
//            zqET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(utilityImpedance)));
            zqET.setText(CalcActivity.noExponentsString(utilityImpedance));

        if(utilityReactance != 0d)
            xqET.setText(CalcActivity.noExponentsString(utilityReactance));
//            xqET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(utilityReactance)));

        if(utilityResistance != 0d)
//            rqET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(utilityResistance)));
            rqET.setText(CalcActivity.noExponentsString(utilityResistance));

        if(utilityCurrent != 0d)
//            UikBisET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(utilityCurrent)));
            UikBisET.setText(CalcActivity.noExponentsString(utilityCurrent));

//        allParametersEnetered =


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        UtilityNameET.addTextChangedListener(textWatcher);
        scPowerET.addTextChangedListener(textWatcher);
        voltageET.addTextChangedListener(textWatcher);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    utilityName = UtilityNameET.getText().toString();
                    utilityScPower = Double.valueOf(scPowerET.getText().toString());
                    utilityNominalVoltage = Double.valueOf(voltageET.getText().toString());
                    String[] dataArrOutS = new String[1];
                    dataArrOutS[0] = utilityName;

                    double[] dataArrOutD = new double[3];

                    dataArrOutD[0] = utilityScPower;
                    dataArrOutD[1] = utilityNominalVoltage;

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
                    Toast.makeText(getApplicationContext(), getString(R.string.enter_all_values), Toast.LENGTH_SHORT).show();
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(UtilityActivity.this);
                    builder.setMessage(getString(R.string.are_u_sure)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                            .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();
                }
                else {
                    setResult(CalcActivity.RESULT_DELETE);
                    finish();
                }
            }
        });

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

    private void backButtonAction() {
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

            AlertDialog.Builder builder = new AlertDialog.Builder(UtilityActivity.this);
            builder.setMessage(getString(R.string.ignore_changes_q)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                    .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();
        }
        else {
            finish();

        }
    }
}
