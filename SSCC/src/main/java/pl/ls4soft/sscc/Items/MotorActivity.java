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

public class MotorActivity extends AppCompatActivity {



    //Motor variables
    private EditText MotorNameET, MotorPowerET, MotorVoltageET, MotorCosFiET, StartUpFactorET, EfficiencyET, xmET, rmET, zmET, MikBisET, MikPrimET, MipET,MipPrimET;
    private Button okBtn, cancelBtn, delBtn;


    //Motor variables
    private String motorName = null;
    private double power = 0d, efficiency = 0d, startUpFactor = 0d, cosFi = 0d, voltage = 0d,
            impedance = 0d, reactance = 0d, resistance = 0d, scCurrent = 0d, motorScPrimCurrent = 0d, motorScImpCurrent = 0d, motorSCImpPrimCurrent = 0d;

    private boolean allParametersEnetered = false;
    private boolean wasAChange = false;
    private boolean wasEdited = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MotorNameET = (EditText) findViewById(R.id.MotorNameET);
        MotorPowerET = (EditText) findViewById(R.id.MotorPowerET);
        MotorVoltageET = (EditText) findViewById(R.id.MotorVoltageET);
        MotorCosFiET = (EditText) findViewById(R.id.MotorCosFiET);
        StartUpFactorET = (EditText) findViewById(R.id.StartUpFactorET);
        EfficiencyET = (EditText) findViewById(R.id.EfficiencyET);
        zmET = (EditText) findViewById(R.id.zmET);
        xmET = (EditText) findViewById(R.id.xmET);
        rmET = (EditText) findViewById(R.id.rmET);
        MikBisET = (EditText) findViewById(R.id.MikBisET);
        MikPrimET = (EditText) findViewById(R.id.MikPrimET);
        MipET = (EditText) findViewById(R.id.MipET);
        MipPrimET = (EditText) findViewById(R.id.MipPrimET);

        okBtn = (Button) findViewById(R.id.TOkBtn);
        cancelBtn = (Button) findViewById(R.id.TCancelBtn);
        delBtn = (Button) findViewById(R.id.TDeleteBtn);

        Intent intentIn = getIntent();
        Bundle bundleIn = intentIn.getExtras();
        String[] dataArrInS;
        double[] dataArrInD;
        if(bundleIn != null) {
            dataArrInS = bundleIn.getStringArray("data1");
            motorName = dataArrInS[0];

            dataArrInD = bundleIn.getDoubleArray("data2");

            power = dataArrInD[0];
            voltage = dataArrInD[1];
            efficiency = dataArrInD[2];
            startUpFactor = dataArrInD[3];
            cosFi = dataArrInD[4];
            impedance = dataArrInD[5];
            reactance = dataArrInD[6];
            resistance = dataArrInD[7];
            scCurrent = dataArrInD[8];
            motorScPrimCurrent = dataArrInD[9];
            motorScImpCurrent = dataArrInD[10];
            motorSCImpPrimCurrent = dataArrInD[11];

            System.out.println("********"+motorScPrimCurrent);


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

//        System.out.println(motorName);
        try {
            if (motorName == null) {
                MotorNameET.setText("M1");
            } else
                MotorNameET.setText(motorName);
        }catch (Exception e){}

        if(power == 0d) {
//            TrafoPowerET.setText("630");
        }
        else
            MotorPowerET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(power)));

        if(voltage == 0d) {
//            TrafoPrimaryVoltageET.setText("15");
        }
        else
            MotorVoltageET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(voltage)));

        if(efficiency == 0d) {
//            TrafoSeconaryVoltageET.setText("0.42");
        }
        else
            EfficiencyET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(efficiency)));

        if(startUpFactor == 0d) {
//            ukET.setText("6");
        }
        else
            StartUpFactorET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(startUpFactor)));

        if(cosFi == 0d) {
//            NominalLossET.setText("9.45");
        }
        else
            MotorCosFiET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(cosFi)));

        if(impedance != 0d)
//            zmET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(impedance)));
            zmET.setText(CalcActivity.noExponentsString(impedance));

        if(reactance != 0d)
//            xmET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(reactance)));
            xmET.setText(CalcActivity.noExponentsString(reactance));

        if(resistance != 0d)
//            rmET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(resistance)));
            rmET.setText(CalcActivity.noExponentsString(resistance));

        if(scCurrent != 0d)
//            MikBisET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(scCurrent)));
            MikBisET.setText(CalcActivity.noExponentsString(scCurrent));

        if(motorScPrimCurrent != 0d)
//            MikBisET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(scCurrent)));
            MikPrimET.setText(CalcActivity.noExponentsString(motorScPrimCurrent));

        if(motorScImpCurrent != 0d)
//            MikBisET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(scCurrent)));
            MipET.setText(CalcActivity.noExponentsString(motorScImpCurrent));

        if(motorSCImpPrimCurrent != 0d)
//            MikBisET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(scCurrent)));
            MipPrimET.setText(CalcActivity.noExponentsString(motorSCImpPrimCurrent));


        MotorNameET.addTextChangedListener(textWatcher);
        MotorPowerET.addTextChangedListener(textWatcher);
        MotorVoltageET.addTextChangedListener(textWatcher);
        EfficiencyET.addTextChangedListener(textWatcher);
        StartUpFactorET.addTextChangedListener(textWatcher);
        MotorCosFiET.addTextChangedListener(textWatcher);


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    motorName = MotorNameET.getText().toString();
                    power = Double.valueOf(MotorPowerET.getText().toString());
                    voltage = Double.valueOf(MotorVoltageET.getText().toString());
                    efficiency = Double.valueOf(EfficiencyET.getText().toString());
                    startUpFactor = Double.valueOf(StartUpFactorET.getText().toString());
                    cosFi = Double.valueOf(MotorCosFiET.getText().toString());

                    String[] dataArrOutS = new String[1];
                    dataArrOutS[0] = motorName;

                    double[] dataArrOutD = new double[5];
                    dataArrOutD[0] = power;
                    dataArrOutD[1] = voltage;
                    dataArrOutD[2] = efficiency;
                    dataArrOutD[3] = startUpFactor;
                    dataArrOutD[4] = cosFi;

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

                AlertDialog.Builder builder = new AlertDialog.Builder(MotorActivity.this);
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

            AlertDialog.Builder builder = new AlertDialog.Builder(MotorActivity.this);
            builder.setMessage(getString(R.string.ignore_changes_q)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                    .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();
        }
        else {
            finish();

        }
    }

}
