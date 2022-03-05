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

public class TrafoActivity extends AppCompatActivity {



    //Trafo variables
    private EditText trafoNameET, TrafoPowerET, TrafoPrimaryVoltageET, TrafoSeconaryVoltageET, ukET, NominalLossET, ztET, xtET, rtET, TikBisET;
    private Button okBtn, cancelBtn, delBtn;


//    //Trafo variables
//    private  String trafoName = null, trafoPower = null, trafoPrimaryVoltage = null, trafoSeconaryVoltage = null, trafoScVoltage = null, trafoNominalPowerLoss = null, trafoImpedance = null,
//            trafoReactance = null, trafoResistance = null, trafoCurrent = null;

    //Trafo variables
    private  String trafoName = null;
    private double trafoPower =0d, trafoPrimaryVoltage = 0d, trafoSeconaryVoltage = 0d, trafoScVoltage = 0d, trafoNominalPowerLoss = 0d,
            trafoImpedance = 0d,
            trafoReactance = 0d, trafoResistance = 0d, trafoCurrent = 0d;

    private boolean allParametersEnetered = false;
    private boolean wasAChange = false;
    private boolean wasEdited = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trafo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        trafoNameET = (EditText) findViewById(R.id.trafoNameET);
        TrafoPowerET = (EditText) findViewById(R.id.TrafoPowerET);
        TrafoPrimaryVoltageET = (EditText) findViewById(R.id.TrafoPrimaryVoltageET);
        TrafoSeconaryVoltageET = (EditText) findViewById(R.id.TrafoSeconaryVoltageET);
        ukET = (EditText) findViewById(R.id.ukET);
        NominalLossET = (EditText) findViewById(R.id.NominalLossET);
        ztET = (EditText) findViewById(R.id.ztET);
        xtET = (EditText) findViewById(R.id.xtET);
        rtET = (EditText) findViewById(R.id.rtET);
//        TikBisET = (EditText) findViewById(R.id.TikBisET);

        okBtn = (Button) findViewById(R.id.TOkBtn);
        cancelBtn = (Button) findViewById(R.id.TCancelBtn);
        delBtn = (Button) findViewById(R.id.TDeleteBtn);

        Intent intentIn = getIntent();
        Bundle bundleIn = intentIn.getExtras();
        String[] dataArrInS;
        double[] dataArrInD;
        if(bundleIn != null) {
            dataArrInS = bundleIn.getStringArray("data1");
            trafoName = dataArrInS[0];

            dataArrInD = bundleIn.getDoubleArray("data2");

            trafoPower = dataArrInD[0];
            trafoPrimaryVoltage = dataArrInD[1];
            trafoSeconaryVoltage = dataArrInD[2];
            trafoScVoltage = dataArrInD[3];
            trafoNominalPowerLoss = dataArrInD[4];
            trafoImpedance = dataArrInD[5];
            trafoReactance = dataArrInD[6];
            trafoResistance = dataArrInD[7];
            trafoCurrent = dataArrInD[8];

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

        if(trafoName == null) {
            trafoNameET.setText("T1");
        }
        else
            trafoNameET.setText(trafoName);

        if(trafoPower == 0d) {
//            TrafoPowerET.setText("630");
        }
        else
            TrafoPowerET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(trafoPower)));

        if(trafoPrimaryVoltage == 0d) {
//            TrafoPrimaryVoltageET.setText("15");
        }
        else
            TrafoPrimaryVoltageET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(trafoPrimaryVoltage)));

        if(trafoSeconaryVoltage == 0d) {
//            TrafoSeconaryVoltageET.setText("0.42");
        }
        else
            TrafoSeconaryVoltageET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(trafoSeconaryVoltage)));

        if(trafoScVoltage == 0d) {
//            ukET.setText("6");
        }
        else
            ukET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(trafoScVoltage)));

        if(trafoNominalPowerLoss == 0d) {
//            NominalLossET.setText("9.45");
        }
        else
            NominalLossET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(trafoNominalPowerLoss)));

        if(trafoImpedance != 0d)
//            ztET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(trafoImpedance)));
            ztET.setText(CalcActivity.noExponentsString(trafoImpedance));

        if(trafoReactance != 0d)
//            xtET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(trafoReactance)));
            xtET.setText(CalcActivity.noExponentsString(trafoReactance));

        if(trafoResistance != 0d)
//            rtET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(trafoResistance)));
            rtET.setText(CalcActivity.noExponentsString(trafoResistance));

        if(trafoCurrent != 0d)
//            TikBisET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(trafoCurrent)));
            TikBisET.setText(CalcActivity.noExponentsString(trafoCurrent));


        trafoNameET.addTextChangedListener(textWatcher);
        TrafoPowerET.addTextChangedListener(textWatcher);
        TrafoPrimaryVoltageET.addTextChangedListener(textWatcher);
        TrafoSeconaryVoltageET.addTextChangedListener(textWatcher);
        ukET.addTextChangedListener(textWatcher);
        NominalLossET.addTextChangedListener(textWatcher);


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    trafoName = trafoNameET.getText().toString();
                trafoPower = Double.valueOf(TrafoPowerET.getText().toString());
                trafoPrimaryVoltage = Double.valueOf(TrafoPrimaryVoltageET.getText().toString());
                trafoSeconaryVoltage = Double.valueOf(TrafoSeconaryVoltageET.getText().toString());
                trafoScVoltage = Double.valueOf(ukET.getText().toString());
                trafoNominalPowerLoss = Double.valueOf(NominalLossET.getText().toString());

                String[] dataArrOutS = new String[1];
                dataArrOutS[0] = trafoName;

                double[] dataArrOutD = new double[5];
                dataArrOutD[0] = trafoPower;
                dataArrOutD[1] = trafoPrimaryVoltage;
                dataArrOutD[2] = trafoSeconaryVoltage;
                dataArrOutD[3] = trafoScVoltage;
                dataArrOutD[4] = trafoNominalPowerLoss;

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(TrafoActivity.this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(TrafoActivity.this);
        builder.setMessage(getString(R.string.ignore_changes_q)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();
    }
        else {
            finish();

        }
    }

}
