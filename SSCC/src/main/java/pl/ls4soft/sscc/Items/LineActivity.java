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

public class LineActivity extends AppCompatActivity {

    //Line variables
        private Spinner LineMaterialSpinner, LinePlacementSpinner;
        private EditText lineNameET, LineLenghtET, LineSectionET,zlET, xlET, rlET, LikBisET;

    private Button okBtn, cancelBtn, delBtn;

//    //Line variables
//    private String lineName = null, lineLenght = null, lineCrossSection = null, lineMaterial = null, linePlacement = null, lineImpedance = null, lineReactance = null, lineResistance = null, lineCurrent = null;

    //Line variables
    private String lineName = null, lineMaterial = null, linePlacement = null;
    private double lineLenght = 0d, lineCrossSection = 0d,  lineImpedance = 0d, lineReactance = 0d, lineResistance = 0d, lineCurrent = 0d;

    private boolean allParametersEnetered = false;
    private boolean wasAChange = false;
    private boolean wasEdited = false;

    private String lineMaterialInitial = null;


    String[] linePlacementA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linePlacementA = getResources().getStringArray(R.array.linePlacement_array);

        LineMaterialSpinner = (Spinner) findViewById(R.id.LineMaterialSpinner);
        LinePlacementSpinner = (Spinner) findViewById(R.id.LinePlacementSpinner);
        lineNameET = (EditText) findViewById(R.id.lineNameET);
        LineLenghtET = (EditText) findViewById(R.id.LineLenghtET);
        LineSectionET = (EditText) findViewById(R.id.LineSectionET);
        zlET = (EditText) findViewById(R.id.zlET);
        xlET = (EditText) findViewById(R.id.xlET);
        rlET = (EditText) findViewById(R.id.rlET);
//        LikBisET = (EditText) findViewById(R.id.LikBisET);

        okBtn = (Button) findViewById(R.id.LOkBtn);
        cancelBtn = (Button) findViewById(R.id.LCancelBtn);
        delBtn = (Button) findViewById(R.id.LDeleteBtn);

        Intent intentIn = getIntent();
        Bundle bundleIn = intentIn.getExtras();
        String[] dataArrInS;
        double[] dataArrInD;
        if(bundleIn != null) {
            dataArrInS = bundleIn.getStringArray("data1");
            lineName = dataArrInS[0];
            lineMaterial = dataArrInS[1];
            linePlacement = dataArrInS[2];
            dataArrInD = bundleIn.getDoubleArray("data2");

            lineLenght = dataArrInD[0];
            lineCrossSection = dataArrInD[1];
            lineImpedance = dataArrInD[2];
            lineReactance = dataArrInD[3];
            lineResistance = dataArrInD[4];
            lineCurrent = dataArrInD[5];

            wasEdited = bundleIn.getBoolean("data4");

        }

        lineMaterialInitial = lineMaterial;

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

        if(lineName == null) {
//            lineNameET.setText("L1");
        }
        else
            lineNameET.setText(lineName);

        if(lineLenght == 0d)
        {
//            LineLenghtET.setText("400");
        }
        else
            LineLenghtET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(lineLenght)));

        if(lineCrossSection == 0d) {
//            LineSectionET.setText("70");
        }
        else
            LineSectionET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(lineCrossSection)));


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.lineMaterial_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        LineMaterialSpinner.setAdapter(adapter1);

//        LineMaterialSpinner.setSelected(true);

        if(lineMaterial == null)
        {}
        else if (lineMaterial.equals("CU")) {
            wasAChange = false;
            LineMaterialSpinner.setSelection(0);
        }
        else if (lineMaterial.equals("AL"))
        {
            wasAChange = false;
            LineMaterialSpinner.setSelection(1);

        }

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.linePlacement_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner

        LinePlacementSpinner.setAdapter(adapter2);

//        LinePlacementSpinner.setSelected(true);

        if(linePlacement==null)
        {}
        else if (linePlacement.equals(linePlacementA[0])) {
            wasAChange = false;
            LinePlacementSpinner.setSelection(0);
        }
        else if (linePlacement.equals(linePlacementA[1])) {
            wasAChange = false;
            LinePlacementSpinner.setSelection(1);
        }


            if(lineImpedance !=0d)
//            zlET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(lineImpedance)));
            zlET.setText(CalcActivity.noExponentsString(lineImpedance));
        if(lineImpedance !=0d)
//            xlET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(lineReactance)));
            xlET.setText(CalcActivity.noExponentsString(lineReactance));
        if(lineImpedance !=0d)
//            rlET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(lineResistance)));
            rlET.setText(CalcActivity.noExponentsString(lineResistance));
//        if(lineImpedance !=0d)
//            LikBisET.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(lineCurrent)));

//        LineMaterialSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                if(position == 0)
//                    lineMaterial = "CU";
//                if(position == 1)
//                    lineMaterial = "AL";
//                            }
//        });

//        LineMaterialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if(position == 0)
//                    lineMaterial = "CU";
//                if(position == 1)
//                    lineMaterial = "AL";
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        lineNameET.addTextChangedListener(textWatcher);
        LineLenghtET.addTextChangedListener(textWatcher);
        LineSectionET.addTextChangedListener(textWatcher);


//        LineMaterialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                spinnerChangeValue();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        LinePlacementSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                spinnerChangeValue();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                lineName = lineNameET.getText().toString();
                lineLenght = Double.valueOf(LineLenghtET.getText().toString());
                lineCrossSection = Double.valueOf(LineSectionET.getText().toString());
                lineMaterial = LineMaterialSpinner.getSelectedItem().toString();
                linePlacement = LinePlacementSpinner.getSelectedItem().toString();

                String[] dataArrOutS = new String[3];
                dataArrOutS[0] = lineName;
                dataArrOutS[1] = lineMaterial;
                dataArrOutS[2] = linePlacement;
                double[] dataArrOutD = new double[2];

                dataArrOutD[0] = lineLenght;
                dataArrOutD[1] = lineCrossSection;

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

                AlertDialog.Builder builder = new AlertDialog.Builder(LineActivity.this);
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

            AlertDialog.Builder builder = new AlertDialog.Builder(LineActivity.this);
            builder.setMessage(getString(R.string.ignore_changes_q)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                    .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();

        }
        else {
            finish();
        }
    }


    private void spinnerChangeValue()
    {

                        wasAChange = true;

    }



}
