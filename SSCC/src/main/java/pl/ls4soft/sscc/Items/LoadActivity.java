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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import pl.ls4soft.sscc.CalcActivity;
import pl.ls4soft.sscc.R;

public class LoadActivity extends AppCompatActivity {

    //Load velements
    private EditText LoadNameET, LoadVoltageET;
    private Button okBtn, cancelBtn, delBtn;
    private Spinner loadPhasesNpSpinner;

//    //Load variables
//    private String loadName = null, loadVoltage = null;

    //Load variables
    private String loadName = null, loadPhaseNo = null;
    private double loadVoltage = 0d;

    private boolean allParametersEnetered = false;
    private boolean wasAChange = false;
    private boolean wasEdited = false;

    String[] loadPhasesNoA;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       LoadNameET = (EditText) findViewById(R.id.LoadNameET);
       LoadVoltageET = (EditText) findViewById(R.id.LoadVoltageET);

       loadPhasesNoA = getResources().getStringArray(R.array.phases_no_array);

       okBtn = (Button) findViewById(R.id.LdOkBtn);
        cancelBtn = (Button) findViewById(R.id.LdCancelBtn);
        delBtn = (Button) findViewById(R.id.LdDeleteBtn);
        loadPhasesNpSpinner = (Spinner) findViewById(R.id.LoadPhaseNoSpinner);

        Intent intentIn = getIntent();
        Bundle bundleIn = intentIn.getExtras();
        String[] dataArrInS;
        double[] dataArrInD;
        if(bundleIn != null) {
            dataArrInS = bundleIn.getStringArray("data1");
            loadName = dataArrInS[0];
            loadPhaseNo = dataArrInS[1];

            dataArrInD = bundleIn.getDoubleArray("data2");
            loadVoltage = dataArrInD[0];

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




        if(loadName == null) {
//            LoadNameET.setText("LD1");
        }
        else
            LoadNameET.setText(loadName);

        if(loadVoltage == 0d) {
//            LoadVoltageET.setText("0.4");
        }
        else
            LoadVoltageET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(loadVoltage)));

        ArrayAdapter<CharSequence> adapterPhaseNo = ArrayAdapter.createFromResource(this, R.array.phases_no_array, android.R.layout.simple_spinner_item);
        adapterPhaseNo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loadPhasesNpSpinner.setAdapter(adapterPhaseNo);

        if(loadPhaseNo == null)
        {
//            System.out.println("null");

        }
        else if (loadPhaseNo.equals(loadPhasesNoA[0]))
        {
            wasAChange = false;
            loadPhasesNpSpinner.setSelection(0);

        }
        else if (loadPhaseNo.equals(loadPhasesNoA[1]))
        {
            wasAChange = false;
            loadPhasesNpSpinner.setSelection(1);

        }

        LoadNameET.addTextChangedListener(textWatcher);
        LoadVoltageET.addTextChangedListener(textWatcher);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                loadName = LoadNameET.getText().toString();
                loadVoltage = Double.valueOf(LoadVoltageET.getText().toString());
                loadPhaseNo =loadPhasesNpSpinner.getSelectedItem().toString();

                String[] dataArrOutS = new String[2];
                dataArrOutS[0] = loadName;
                dataArrOutS[1] = loadPhaseNo;
                double[] dataArrOutD = new double[1];

                dataArrOutD[0] = loadVoltage;

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

                AlertDialog.Builder builder = new AlertDialog.Builder(LoadActivity.this);
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
        if(wasAChange)
        {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(LoadActivity.this);
        builder.setMessage(getString(R.string.ignore_changes_q)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();
    }
        else {
            finish();

        }
    }


}
