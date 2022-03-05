package pl.ls4soft.sscc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pl.ls4soft.sscc.Items.BusActivity;
import pl.ls4soft.sscc.Items.LineActivity;
import pl.ls4soft.sscc.Items.MotorActivity;
import pl.ls4soft.sscc.Items.TrafoActivity;
import pl.ls4soft.sscc.Items.UtilityActivity;
import pl.ls4soft.sscc.Items.LoadActivity;

public class CalcActivity extends AppCompatActivity implements Serializable{

//list for items
    private ListView listViewTiles;
    private TileListAdapter adapter;
    private List<Tile> tileList;

    public static int TYPE_UTILITY = 0;
    public static int TYPE_TRAFO = 1;
    public static int TYPE_CABLE = 2;
    public static int TYPE_LOAD = 3;
    public static int TYPE_MOTOR = 4;
    public static int TYPE_BUS = 5;

//    public static int tileType = 1; // 0 - utility, 1 - trafo, 2 - cable, 3 - load, 4 -motor

    public int globalTileID = 0;
    public int utilityID = 1;
    public int trafoID = 1;
    public int lineID = 1;
    public int loadID = 1;
    public int motorID = 1;
    public int busID = 1;

//calc activity view elements
    public Button addUtilityB, addTrafoB, addCableB, addLoadB, addMotorB, addBusB;
    public static Button calcB;

//    buttons behavior variables
    boolean noUtility = true;
    boolean loadPresent = false;
    boolean busPresent = false;
    boolean allItemsParametersEntered = false;
    boolean allNecessaryItemsPresent = false;
    boolean lastTrafoVoltageEqualsLoadVoltage = false;
    boolean trafosVoltagesMatch = true;

//    boolean trafoIsFirst = false;

    //items parameters edit variables
    public static final int RESULT_DELETE = 2;

    //calculation variables
    int currentTileEditPosition;
    String lastLineVoltage, loadPhasesNo = null;
    String[] linePlacementA;
    Double zK = 0.0;
    //ArrayLists for calc 3ph SCC and partially 1pf SSCC (from utlity to first trafo)
    ArrayList<Double> reactancesList = new ArrayList<Double>();
    ArrayList<Double> resistancesList = new ArrayList<Double>();
    //ArrayList for calc 1ph from first element after trafo to load
    ArrayList<Double> reactanceshList1Ph = new ArrayList<Double>();
    ArrayList<Double> resistancesList1Ph = new ArrayList<Double>();

    ArrayList<Double> trafoRatiosList = new ArrayList<Double>();
    ArrayList<String> namesOfTilesWithParametersNotEntered = new ArrayList<String>();
    ArrayList<String> listOfLackingItmes = new ArrayList<String>();
//    ArrayList<Double> trafosVoltagesLisst = new ArrayList<>();

    boolean itemsTilesInRightOrder = false;
    boolean tileIncludedToCalculations = false;


    //trafo operations
    boolean wasTrafo = false, firstTrafo = true;
    Double uP1, uS1;
    Double firstTrafoVoltage = 0d, lastTrafoVoltage = 0d;

    //calcs aux viariables
    Double loadVoltageAux = 0d;
    Tile tempTile = null;
    private int pointOfSSCPosition = 0;

    //files operation variables
    String openedFilePath = null;
    int biggersNumerInFileNameNotTaken = 0;
    boolean workSaved = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        biggersNumerInFileNameNotTaken = MainActivity.checkLastFileNumberName()+1; // wrong name, this is biggest numeres from all numers in files names
        linePlacementA = getResources().getStringArray(R.array.linePlacement_array); //auxiliary variable with line placement array from strings


        addUtilityB = (Button) findViewById(R.id.addUtilityBtn);
        addTrafoB = (Button) findViewById(R.id.addTransformerBtn);
        addCableB = (Button) findViewById(R.id.addCableBtn);
        addLoadB = (Button) findViewById(R.id.addLoadBtn);
        addMotorB = (Button) findViewById(R.id.addMotorBtn);
        addBusB = (Button) findViewById(R.id.addBusBtn);
        calcB = (Button) findViewById(R.id.calcBtn);

        listViewTiles = (ListView) findViewById(R.id.listViewTiles);

        addUtilityB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (noUtility) {
                    adapter.addItem(new Tile(globalTileID, TYPE_UTILITY, utilityID));
                    scrollMyListViewToBottom();
                    globalTileID++;
                    utilityID++;
                    noUtility = false;
                    addTrafoB.setEnabled(true);
                    addCableB.setEnabled(true);

                    if(loadPresent == false) {
                        addLoadB.setEnabled(true);
                        addMotorB.setEnabled(true);
                    }
                    noUtility = false;
                    if(busPresent == false)
                    addBusB.setEnabled(true);

                    workSaved = false;

                }
                addUtilityB.setEnabled(noUtility);
            }
        });
//        addTrafoB.setEnabled(false);
        addTrafoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(new Tile(globalTileID, TYPE_TRAFO, trafoID));
                scrollMyListViewToBottom();
                trafoID++;
                globalTileID++;
//                if(tileList.get(0).getType() == TYPE_TRAFO)
//                {
//                    addUtilityB.setEnabled(false);
//                    trafoIsFirst = true;
//                }
//                addTrafoB.setEnabled(true); - changed 20190220
                addCableB.setEnabled(true);
//                if(!wasTrafo)
//                addBusB.setEnabled(true);

                if(busPresent == false)
                {
                    addBusB.setEnabled(true);
                }

                if(loadPresent == false) {
                    addLoadB.setEnabled(true);
                    addMotorB.setEnabled(true);
                }
                workSaved = false;
                wasTrafo = true;
            }
        });
        addCableB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(new Tile(globalTileID, TYPE_CABLE, lineID));
                scrollMyListViewToBottom();
                lineID++;
                globalTileID++;

                workSaved = false;
            }
        });

        addLoadB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(new Tile(globalTileID, TYPE_LOAD, loadID));
                scrollMyListViewToBottom();
                globalTileID++;
                loadID++;
//                addTrafoB.setEnabled(false);
//                addCableB.setEnabled(false);
                addLoadB.setEnabled(false);
                addMotorB.setEnabled(false);
                loadPresent = true;
                calcB.setEnabled(loadPresent);

                workSaved = false;
            }
        });

        addMotorB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(new Tile(globalTileID, TYPE_MOTOR, motorID));
                scrollMyListViewToBottom();
                globalTileID++;
                motorID++;
//                addTrafoB.setEnabled(false);
//                addCableB.setEnabled(false);
                addLoadB.setEnabled(false);
                addMotorB.setEnabled(false);
                loadPresent = true;
                calcB.setEnabled(loadPresent);

                workSaved = false;
            }
        });

        addBusB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(new Tile(globalTileID, TYPE_BUS, busID));
                scrollMyListViewToBottom();
                globalTileID++;
                busID++;
                addBusB.setEnabled(false);
                calcB.setEnabled(loadPresent);
                busPresent = true;
                workSaved = false;
            }
        });

        calcB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //set number of phases in load for this activity
//                for (int i = tileList.size() - 1; i >= 0; i--)
                for (int i = 0; i < tileList.size(); i++)
                {
                    if (tileList.get(i).getType() == TYPE_LOAD)
                    {
                        loadPhasesNo = tileList.get(i).getLoadPhaseNo();


                    }
                    if (tileList.get(i).getType() == TYPE_MOTOR)
                    {
                        loadPhasesNo = "~3";

                    }
                }

                for (int i = 0; i < tileList.size(); i++)
                {
                    if (tileList.get(i).getType() == TYPE_TRAFO)
                    {
                        lastTrafoVoltage = tileList.get(i).getTrafoSeconaryVoltage();
//                        System.out.println("LAST TRAFO U:"+String.valueOf(lastTrafoVoltage));
                    }

                    if(tileList.get(i).getType() == TYPE_BUS) {
                        pointOfSSCPosition = i;
                        System.out.println("BUS position:"+ i);


                    }

                }

                namesOfTilesWithParametersNotEntered.clear();
                listOfLackingItmes.clear();

                //check conditions if calculations possible

                checkIfAllNecessaryTilesPresent();
                checkIfTilesInRightOrder();
                checkIfAllItemsParametersEntered();


                //calculation if ~3 or ~1
                calculateSC();

            }
        });

        calcStartState();

        //file open code

        Intent intentIn = getIntent();
        Bundle bundleIn = intentIn.getExtras();

        try {
            if (bundleIn != null) {
                openedFilePath = bundleIn.getString("path");
//            openedFileName = bundleIn.getString("name");

                File file = new File(openedFilePath);
                FileInputStream fIn;
                try {
                    fIn = new FileInputStream(file);
                    ObjectInputStream myOS = new ObjectInputStream(fIn);
                    tileList = (List<Tile>) myOS.readObject();

                    addUtilityB.setEnabled(true);
                    addBusB.setEnabled(true);
                    addTrafoB.setEnabled(true);
                    addCableB.setEnabled(true);
                    addLoadB.setEnabled(true);
                    addMotorB.setEnabled(true);

                    if (tileList.size() == 0) {
                        calcStartState();
                    } else {
                        for (int i = 0; i < tileList.size(); i++) {
                            if (tileList.get(i).getType() == TYPE_UTILITY) {
                                addUtilityB.setEnabled(false);

                            }
                            if (tileList.get(i).getType() == TYPE_LOAD || tileList.get(i).getType() == TYPE_MOTOR) {
                                addLoadB.setEnabled(false);
                                addMotorB.setEnabled(false);

                                loadPresent = true;
                                calcB.setEnabled(true);
                            }

                            if (tileList.get(i).getType() == TYPE_BUS) {
                                addBusB.setEnabled(false);
                                calcB.setEnabled(true);
                                busPresent = true;

                            }
                        }
                    }
//                }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            //end of file open code

            //new diagram
            else {
                tileList = new ArrayList<Tile>();
                openedFilePath = null;
            }
            //listview creation
            adapter = new TileListAdapter(getApplicationContext(), tileList);
            listViewTiles.setAdapter(adapter);
            scrollMyListViewToBottom();
        }
        catch (Exception e)
        {
           finish();

            Toast.makeText(getApplicationContext(), R.string.old_file, Toast.LENGTH_SHORT).show();
        }

        //click on items and pass data to items activieties
        listViewTiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onListItemClick(view,position,id);
            }
        });

        listViewTiles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                return onLongListItemClick(view,position,id);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


        //handling clicking on items - short click
    protected void onListItemClick(View view, int position, long id) {

        currentTileEditPosition = position;

        if (tileList.get(position).getType() == TYPE_UTILITY) {

            //string data
            String[] dataArrS = new String[1];
            dataArrS[0] = tileList.get(position).getUtilityName();

            //double data
            double[] dataArrD = new double[6];// = new Double[6];

            dataArrD[0] = tileList.get(position).getUtilityScPower();
            dataArrD[1] = tileList.get(position).getUtilityNominalVoltage();
            dataArrD[2] = tileList.get(position).getUtilityImpedance();
            dataArrD[3] = tileList.get(position).getUtilityReactance();
            dataArrD[4] = tileList.get(position).getUtilityResistance();
            dataArrD[5] = tileList.get(position).getUtilityCurrent();

            boolean wasEdited = tileList.get(position).getWasEdited();

            Intent intentBundle = new Intent(getApplicationContext(), UtilityActivity.class);
            Bundle bundleOut = new Bundle();
            bundleOut.putStringArray("data1", dataArrS);
            bundleOut.putDoubleArray("data2", dataArrD);
            bundleOut.putBoolean("data4", wasEdited);

            intentBundle.putExtras(bundleOut);
            startActivityForResult(intentBundle, TYPE_UTILITY);

        }

        if (tileList.get(position).getType() == TYPE_TRAFO) {
            //string data
            String[] dataArrS = new String[1];

            dataArrS[0] = tileList.get(position).getTrafoName();
            //double data
            double[] dataArrD = new double[9];

            dataArrD[0] = tileList.get(position).getTrafoPower();
            dataArrD[1] = tileList.get(position).getTrafoPrimaryVoltage();
            dataArrD[2] = tileList.get(position).getTrafoSeconaryVoltage();
            dataArrD[3] = tileList.get(position).getTrafoScVoltage();
            dataArrD[4] = tileList.get(position).getTrafoNominalPowerLoss();
            dataArrD[5] = tileList.get(position).getTrafoImpedance();
            dataArrD[6] = tileList.get(position).getTrafoReactance();
            dataArrD[7] = tileList.get(position).getTrafoResistance();
            dataArrD[8] = tileList.get(position).getTrafoCurrent();

            boolean wasEdited = tileList.get(position).getWasEdited();

            Intent intentBundle = new Intent(getApplicationContext(), TrafoActivity.class);
            Bundle bundleOut = new Bundle();
            bundleOut.putStringArray("data1", dataArrS);
            bundleOut.putDoubleArray("data2", dataArrD);
            bundleOut.putBoolean("data4", wasEdited);
            intentBundle.putExtras(bundleOut);
            startActivityForResult(intentBundle, TYPE_TRAFO);

        }

        if (tileList.get(position).getType() == TYPE_CABLE) {
            //string data
            String[] dataArrS = new String[3];

            dataArrS[0] = tileList.get(position).getLineName();
            dataArrS[1] = tileList.get(position).getLineMaterial();
            dataArrS[2] = tileList.get(position).getLinePlacement();

            //double data
            double[] dataArrD = new double[6];

            boolean wasEdited = tileList.get(position).getWasEdited();

            dataArrD[0] = tileList.get(position).getLineLenght();
            dataArrD[1] = tileList.get(position).getLineCrossSection();
            dataArrD[2] = tileList.get(position).getLineImpedance();
            dataArrD[3] = tileList.get(position).getLineReactance();
            dataArrD[4] = tileList.get(position).getLineResistance();
            dataArrD[5] = tileList.get(position).getLineCurrent();


            Intent intentBundle = new Intent(getApplicationContext(), LineActivity.class);
            Bundle bundleOut = new Bundle();
            bundleOut.putStringArray("data1", dataArrS);
            bundleOut.putDoubleArray("data2", dataArrD);
            bundleOut.putBoolean("data4", wasEdited);
            intentBundle.putExtras(bundleOut);
            startActivityForResult(intentBundle, TYPE_CABLE);
        }

        if (tileList.get(position).getType() == TYPE_LOAD) {
            //string data
            String[] dataArrS = new String[2];
            dataArrS[0] = tileList.get(position).getLoadName();
            dataArrS[1] = tileList.get(position).getLoadPhaseNo();
//            System.out.println(tileList.get(position).getLoadPhaseNo());

            //double data
            double[] dataArrD = new double[1];
            dataArrD[0] = tileList.get(position).getLoadVoltage();

            boolean wasEdited = tileList.get(position).getWasEdited();

            Intent intentBundle = new Intent(getApplicationContext(), LoadActivity.class);
            Bundle bundleOut = new Bundle();
            bundleOut.putStringArray("data1", dataArrS);
            bundleOut.putDoubleArray("data2", dataArrD);
            bundleOut.putBoolean("data4", wasEdited);
            intentBundle.putExtras(bundleOut);
            startActivityForResult(intentBundle, TYPE_LOAD);
        }

        if (tileList.get(position).getType() == TYPE_MOTOR) {
            //string data
            String[] dataArrS = new String[1];

            dataArrS[0] = tileList.get(position).getMotorName();
            //double data
            double[] dataArrD = new double[12];

            dataArrD[0] = tileList.get(position).getPower();
            dataArrD[1] = tileList.get(position).getVoltage();
            dataArrD[2] = tileList.get(position).getEfficiency();
            dataArrD[3] = tileList.get(position).getStartUpFactor();
            dataArrD[4] = tileList.get(position).getCosFi();
            dataArrD[5] = tileList.get(position).getImpedance();
            dataArrD[6] = tileList.get(position).getReactance();
            dataArrD[7] = tileList.get(position).getResistance();
            dataArrD[8] = tileList.get(position).getScCurrent();
            dataArrD[9] = tileList.get(position).getMotorScPrimCurrent();
            dataArrD[10] = tileList.get(position).getMotorScImpCurrent();
            dataArrD[11] = tileList.get(position).getMotorScImpPrimCurrent();

//            System.out.println("********"+tileList.get(position).getMotorScPrimCurrent());
//            System.out.println("********"+tileList.get(position).getMotorScImpCurrent());
//            System.out.println("********"+tileList.get(position).getMotorScImpPrimCurrent());


            boolean wasEdited = tileList.get(position).getWasEdited();

            Intent intentBundle = new Intent(getApplicationContext(), MotorActivity.class);
            Bundle bundleOut = new Bundle();
            bundleOut.putStringArray("data1", dataArrS);
            bundleOut.putDoubleArray("data2", dataArrD);
            bundleOut.putBoolean("data4", wasEdited);
            intentBundle.putExtras(bundleOut);
            startActivityForResult(intentBundle, TYPE_MOTOR);

        }

        if (tileList.get(position).getType() == TYPE_BUS) {
            //string data
            String[] dataArrS = new String[1];

            dataArrS[0] = tileList.get(position).getName();
            //double data
            double[] dataArrD = new double[6];

            dataArrD[0] = tileList.get(position).getVoltage();
            dataArrD[1] = tileList.get(position).getScImpedance();
            dataArrD[2] = tileList.get(position).getScReactance();
            dataArrD[3] = tileList.get(position).getScResistance();
            dataArrD[4] = tileList.get(position).getScCurrent();
            dataArrD[5] = tileList.get(position).getScImpCurrent();

            boolean wasEdited = tileList.get(position).getWasEdited();

            Intent intentBundle = new Intent(getApplicationContext(), BusActivity.class);
            Bundle bundleOut = new Bundle();
            bundleOut.putStringArray("data1", dataArrS);
            bundleOut.putDoubleArray("data2", dataArrD);
            bundleOut.putBoolean("data4", wasEdited);
            intentBundle.putExtras(bundleOut);
            startActivityForResult(intentBundle, TYPE_BUS);

        }


    }

    //handling clicking on items - long click

    int auxPosition;
    protected boolean onLongListItemClick(View view, int position, long id) {

        auxPosition = position;
        final String[] teilOperationA = getResources().getStringArray(R.array.tileListOperation_array);
        final String[] teilOperation =teilOperationA;
        AlertDialog.Builder builder = new AlertDialog.Builder(CalcActivity.this);
        builder.setTitle(getString(R.string.calc_tile_Operation));
        builder.setItems(teilOperation, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(teilOperation[which]==teilOperation[0]) {
                    moveTileUp(auxPosition);
                }
                if(teilOperation[which]==teilOperation[1]) {
                    moveTileDown(auxPosition);
                }
                if(teilOperation[which]==teilOperation[2]) {
                    deleteTile(auxPosition);
                }
            }
        });
        builder.create();
        builder.show();

        return true;
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calc_clearall, menu);
        getMenuInflater().inflate(R.menu.menu_calc_save_new, menu);
        getMenuInflater().inflate(R.menu.menu_calc_save, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //file saving btn
        if (id == R.id.action_save)
        {
            if(!(openedFilePath==null)) {
                saveFile();
            }
            else {
                saveNewFile();
            }

            return true;
        }

        //file saving btn
        if (id == R.id.action_save_new)
        {
            saveNewFile();
            return true;
        }

        //clear all btn

        if(id == R.id.action_clearAll) {

            if (tileList.size() > 0)
            {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                removeAllTiles();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.clear_all_are_u_sure)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                        .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();
            }
        }

        if(id == android.R.id.home) {
            backButtonAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //return items' parameters from items activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TYPE_UTILITY && resultCode == Activity.RESULT_OK) {
            String[] dataArrS;
            double[] dataArrD;
            boolean tileAllParametersEntered;

            dataArrS = (String[]) data.getExtras().get("data1");
            dataArrD = (double[]) data.getExtras().get("data2");
            tileAllParametersEntered = (boolean) data.getExtras().get("data3");
            boolean wasEdited = (boolean) data.getExtras().get("data4");

            tileList.get(currentTileEditPosition).setUtilityName(dataArrS[0]);
            tileList.get(currentTileEditPosition).setUtilityScPower(dataArrD[0]);
            tileList.get(currentTileEditPosition).setUtilityNominalVoltage(dataArrD[1]);
            tileList.get(currentTileEditPosition).setAllParametersEnetered(tileAllParametersEntered);
            tileList.get(currentTileEditPosition).setWasEdited(wasEdited);
            workSaved = false;
        }

        if (requestCode == TYPE_TRAFO && resultCode == Activity.RESULT_OK) {
            String[] dataArrS;
            double[] dataArrD;
            boolean tileAllParametersEntered;

            dataArrS = (String[]) data.getExtras().get("data1");
            dataArrD = (double[]) data.getExtras().get("data2");
            tileAllParametersEntered = (boolean) data.getExtras().get("data3");
            boolean wasEdited = (boolean) data.getExtras().get("data4");

            tileList.get(currentTileEditPosition).setTrafoName(dataArrS[0]);
            tileList.get(currentTileEditPosition).setTrafoPower(dataArrD[0]);
            tileList.get(currentTileEditPosition).setTrafoPrimaryVoltage(dataArrD[1]);
            tileList.get(currentTileEditPosition).setTrafoSeconaryVoltage(dataArrD[2]);
            tileList.get(currentTileEditPosition).setTrafoScVoltage(dataArrD[3]);
            tileList.get(currentTileEditPosition).setTrafoNominalPowerLoss(dataArrD[4]);
            tileList.get(currentTileEditPosition).setAllParametersEnetered(tileAllParametersEntered);
            tileList.get(currentTileEditPosition).setWasEdited(wasEdited);
            workSaved = false;
        }

        if (requestCode == TYPE_CABLE && resultCode == Activity.RESULT_OK) {
            String[] dataArrS;
            double[] dataArrD;
            boolean tileAllParametersEntered;

            dataArrS = (String[]) data.getExtras().get("data1");
            dataArrD = (double[]) data.getExtras().get("data2");
            tileAllParametersEntered = (boolean) data.getExtras().get("data3");
            boolean wasEdited = (boolean) data.getExtras().get("data4");

            tileList.get(currentTileEditPosition).setLineName(dataArrS[0]);
            tileList.get(currentTileEditPosition).setLineMaterial(dataArrS[1]);
            tileList.get(currentTileEditPosition).setLinePlacement(dataArrS[2]);

            tileList.get(currentTileEditPosition).setLineLenght(dataArrD[0]);
            tileList.get(currentTileEditPosition).setLineCrossSection(dataArrD[1]);
            tileList.get(currentTileEditPosition).setAllParametersEnetered(tileAllParametersEntered);
            tileList.get(currentTileEditPosition).setWasEdited(wasEdited);

            workSaved = false;
        }


        if (requestCode == TYPE_LOAD && resultCode == Activity.RESULT_OK) {
            String[] dataArrS;
            double[] dataArrD;
            boolean tileAllParametersEntered;

            dataArrS = (String[]) data.getExtras().get("data1");
            dataArrD = (double[]) data.getExtras().get("data2");
            tileAllParametersEntered = (boolean) data.getExtras().get("data3");
            boolean wasEdited = (boolean) data.getExtras().get("data4");

            tileList.get(currentTileEditPosition).setLoadName(dataArrS[0]);
            tileList.get(currentTileEditPosition).setLoadPhaseNo(dataArrS[1]);
            tileList.get(currentTileEditPosition).setLoadVoltage(dataArrD[0]);
            tileList.get(currentTileEditPosition).setAllParametersEnetered(tileAllParametersEntered);
            tileList.get(currentTileEditPosition).setWasEdited(wasEdited);

            workSaved = false;

            if(tileList.get(currentTileEditPosition).getLoadPhaseNo().equals("~1"))
            {
                for (int i = 0; i<tileList.size();i++)
                {
                    if(tileList.get(i).getType()== TYPE_BUS)
                    {
                        //TODO



                    }
                }
            }
        }

        if (requestCode == TYPE_MOTOR && resultCode == Activity.RESULT_OK) {
            String[] dataArrS;
            double[] dataArrD;
            boolean tileAllParametersEntered;

            dataArrS = (String[]) data.getExtras().get("data1");
            dataArrD = (double[]) data.getExtras().get("data2");
            tileAllParametersEntered = (boolean) data.getExtras().get("data3");
            boolean wasEdited = (boolean) data.getExtras().get("data4");

            tileList.get(currentTileEditPosition).setMotorName(dataArrS[0]);
            tileList.get(currentTileEditPosition).setPower(dataArrD[0]);
            tileList.get(currentTileEditPosition).setVoltage(dataArrD[1]);
            tileList.get(currentTileEditPosition).setEfficiency(dataArrD[2]);
            tileList.get(currentTileEditPosition).setStartUpFactor(dataArrD[3]);
            tileList.get(currentTileEditPosition).setCosFi(dataArrD[4]);
            tileList.get(currentTileEditPosition).setAllParametersEnetered(tileAllParametersEntered);
            tileList.get(currentTileEditPosition).setWasEdited(wasEdited);
            workSaved = false;
        }

        if (requestCode == TYPE_BUS && resultCode == Activity.RESULT_OK) {
            String[] dataArrS;
            double[] dataArrD;
            boolean tileAllParametersEntered;

            dataArrS = (String[]) data.getExtras().get("data1");
            dataArrD = (double[]) data.getExtras().get("data2");
            tileAllParametersEntered = (boolean) data.getExtras().get("data3");
            boolean wasEdited = (boolean) data.getExtras().get("data4");

            tileList.get(currentTileEditPosition).setName(dataArrS[0]);
            tileList.get(currentTileEditPosition).setVoltage(dataArrD[0]);
            tileList.get(currentTileEditPosition).setScImpedance(dataArrD[1]);
            tileList.get(currentTileEditPosition).setScReactance(dataArrD[2]);
            tileList.get(currentTileEditPosition).setScResistance(dataArrD[3]);
            tileList.get(currentTileEditPosition).setScCurrent(dataArrD[4]);
            tileList.get(currentTileEditPosition).setScImpCurrent(dataArrD[5]);
            tileList.get(currentTileEditPosition).setAllParametersEnetered(tileAllParametersEntered);
            tileList.get(currentTileEditPosition).setWasEdited(wasEdited);
            workSaved = false;
        }

        if (requestCode == TYPE_UTILITY && resultCode == RESULT_DELETE) {
            tileList.remove(currentTileEditPosition);
            deleteUtilityParams();
        }

        if ((requestCode == TYPE_TRAFO || requestCode == TYPE_CABLE) && resultCode == RESULT_DELETE) {
            tileList.remove(currentTileEditPosition);
            deleteTrafoOrLineParams();
        }

        if (requestCode == TYPE_LOAD && resultCode == RESULT_DELETE || requestCode == TYPE_MOTOR && resultCode == RESULT_DELETE) {

            tileList.remove(currentTileEditPosition);
            deleteLoadOrMotorParams();
        }

        if (requestCode == TYPE_BUS && resultCode == RESULT_DELETE) {

            tileList.remove(currentTileEditPosition);
            deleteBusParams();


        }
        adapter.notifyDataSetChanged();
    }

    private void scrollMyListViewToBottom() {

        listViewTiles.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listViewTiles.setSelection(adapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backButtonAction();
    }

//string threatment
    public static String cutOutZerosAtEndFromString(String s) {
        boolean resultHasNoZeros;
        int dotIndex = 0;
        resultHasNoZeros = s.endsWith(".0");

        if (resultHasNoZeros) {
            dotIndex = s.indexOf('.');
            s = s.substring(0, dotIndex);
        }
        if(s.equals("NaN"));
//            s.equals(getString(R.string.res_out_of_range));


        return s;
    }

    public static double round(Double d) {
        if (d != 0d) {
            long l = Math.round(d * 1000);
            d = (double) l / 1000;
        }
        return d;

    }

    public static double roundSixPaces(Double d) {
        if (d != 0d) {
            long l = Math.round(d * 1000000);
            d = (double) l / 1000000;
        }
        return d;

    }

    public static double roundNPlaces(Double d, int n) {
        if (d != 0d) {
            double nD = (double) n;
            long l = Math.round(d * Math.pow(10d, nD));
            d = (double) l / (10 * n);
        }
        return d;
    }

    public static String noExponentsString(double d)
    {

        DecimalFormat decimalFormatter = new DecimalFormat("###.#####");
        String s = decimalFormatter.format(d);

        if(s.equals("NaN"))
            s = "-Out of range-";

        return s;

    }



    //tiles operations
    private void removeAllTiles()
    {
        if (adapter.getCount() > 0)
        {
            tileList.clear();
            reactancesList.clear();
            resistancesList.clear();
            reactanceshList1Ph.clear();
            resistancesList1Ph.clear();


            adapter.notifyDataSetChanged();
        }
//        if (adapter.getCount() == 0)
//        {
//            calcStartState();
//        }
        calcStartState();
        clearIDnumber();

    }

    private void moveTileUp(int position)
    {

        if(!(tileList.get(position).getType()== TYPE_UTILITY) || (tileList.get(position).getType()== TYPE_UTILITY && position != 0)) //not an utility
        {
            if (!(tileList.get(position).getType() == TYPE_LOAD  || tileList.get(position).getType() == TYPE_MOTOR)
                  ) //a bus or motor on position different than last
            {
                if (!(tileList.get(position - 1).getType() == TYPE_UTILITY)) //utility not above
                {
                    reactancesList.removeAll(reactancesList);
                    resistancesList.removeAll(resistancesList);
                    clearAllTilesParameters();
                    tileList.add(position - 1, tileList.get(position));
                    tileList.remove(position + 1);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void moveTileDown(int position)
    {
        if(!(tileList.get(position).getType()==TYPE_UTILITY)) //not an utility
        {
            if (!(tileList.get(position).getType() == TYPE_LOAD  || tileList.get(position).getType() == TYPE_MOTOR)
                 ||   ((tileList.get(position).getType() == TYPE_LOAD  || tileList.get(position).getType() == TYPE_MOTOR) && position != tileList.size()-1)) //a bus or motor on position different than last
            {
                if (!(position + 1 == tileList.size()))
                {
                    if (!(tileList.get(position + 1).getType() == TYPE_LOAD  || tileList.get(position+1).getType() == TYPE_MOTOR)) //a bus not below
                        {
                        reactancesList.removeAll(reactancesList);
                        resistancesList.removeAll(resistancesList);
                        clearAllTilesParameters();
                        tileList.add(position + 1, tileList.get(position));
                        tileList.add(position, tileList.get(position + 2));
                        tileList.remove(position + 2);
                        tileList.remove(position + 2);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void deleteTile(final int position) {
        final int deletedType = tileList.get(position).getType();

        if (tileList.get(position).getWasEdited()) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                        {
                            switch (deletedType) {
                                case 0: {
                                    deleteUtilityParams();
                                    break;
                                }
                                case 1: {
                                    deleteTrafoOrLineParams();
                                    break;
                                }
                                case 2: {
                                    deleteTrafoOrLineParams();
                                    break;
                                }
                                case 3: {
                                    deleteLoadOrMotorParams();
                                    break;
                                }
                                case 4: {
                                    deleteLoadOrMotorParams();
                                    break;
                                }
                                case 5: {
                                    deleteBusParams();
                                    break;
                                }
                            }
                            tileList.remove(position);
                            adapter.notifyDataSetChanged();

                        }

                        break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(CalcActivity.this);
            builder.setMessage(getString(R.string.are_u_sure)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                    .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();
        }
        else
        {

            switch (deletedType) {
                case 0: {
                    deleteUtilityParams();
                    break;
                }
                case 1: {
                    deleteTrafoOrLineParams();
                    break;
                }
                case 2: {
                    deleteTrafoOrLineParams();
                    break;
                }
                case 3: {
                    deleteLoadOrMotorParams();
                    break;
                }
                case 4: {
                    deleteLoadOrMotorParams();
                    break;
                }
                case 5: {
                    deleteBusParams();
                    break;
                }
            }
            tileList.remove(position);
            adapter.notifyDataSetChanged();
        }
    }

    private void deleteUtilityParams()
    {
        if (!loadPresent) {
            addLoadB.setEnabled(true);
            addMotorB.setEnabled(true);
        }
        noUtility = true;

        addUtilityB.setEnabled(true);
//        addTrafoB.setEnabled(true);
//        addCableB.setEnabled(true);
        utilityID = 1;
        workSaved = false;

    }

    private void deleteTrafoOrLineParams()
    {
        if (!loadPresent) {
            addTrafoB.setEnabled(true);
            addCableB.setEnabled(true);
            addLoadB.setEnabled(true);
            addMotorB.setEnabled(true);
        }
//        if(busPresent)
        workSaved = false;
    }

    private void deleteLoadOrMotorParams()
    {
        addTrafoB.setEnabled(true);
        addCableB.setEnabled(true);
        addLoadB.setEnabled(true);
        addMotorB.setEnabled(true);
        loadPresent = false;
        calcB.setEnabled(loadPresent);
        workSaved = false;
        motorID = 1;
        loadID = 1;
    }

    private void deleteBusParams()
    {
        calcB.setEnabled(loadPresent);
        addBusB.setEnabled(true);
        workSaved = false;
        busPresent = false;
        if(busID > 0)
            busID = busID-1 ;
        else
            busID = 1;
    }


    private void clearAllTilesParameters(){}// TODO: 2017-02-04

    //file saving code:
    private String path = Environment.getExternalStorageDirectory().toString() + "/SCCurrent";

    private void saveNewFile()
    {
//        workSaved = true;
        createDir();
        createFile();
    }

    private void saveFile()
    {
//        workSaved = true;
        saveToFile();
    }

    public void createDir() {
        File folder = new File(path);
        if (!folder.exists()) {
            try {
                folder.mkdirs();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void createFile()
    {

//
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.enter_new_file_name));
        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do something with value!
                // Button action overrode below
            }
        });

        alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        final AlertDialog dialog = alert.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Boolean closeDialog = false;
                String fileName = input.getText().toString();
                openedFilePath = path+"/"+fileName+".SSCC";
                System.out.println(openedFilePath);

                File file = new File(openedFilePath); //

                FileOutputStream fOut;
                try
                {
                    if(file.exists())
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.file_exists_info), Toast.LENGTH_SHORT).show();
                    }

                    else {
                        fOut = new FileOutputStream(file);
                        ObjectOutputStream myOutStream = new ObjectOutputStream(fOut);
                        myOutStream.writeObject(tileList);
                        myOutStream.close();
                        fOut.close();
                        Toast.makeText(getApplicationContext(), getString(R.string.file_saved_new), Toast.LENGTH_LONG).show();
                        workSaved = true;

                        closeDialog = true;
                    }


                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
                }

                if(closeDialog)
                {
                    dialog.dismiss();
                }
            }
        });
    }


    public void saveToFile()
    {
        File file = new File(openedFilePath); //
        FileOutputStream fOut;
        try
        {
            fOut = new FileOutputStream(file);
            ObjectOutputStream myOutStream = new ObjectOutputStream(fOut);
            myOutStream.writeObject(tileList);
            myOutStream.close();
            fOut.close();
            Toast.makeText(getApplicationContext(),getString(R.string.file_saved), Toast.LENGTH_LONG).show();
            workSaved = true;


        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void backButtonAction()
    {

        if(workSaved == false)
        {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked

                            if(openedFilePath!=null)
                            {
                                saveFile();
                            }
                            else
                            {
                                saveNewFile();
                            }
//                            startActivity(new Intent(CalcActivity.this, MainActivity.class));
//                            finish();
//                            removeAllTiles();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
//                            startActivity(new Intent(CalcActivity.this, MainActivity.class));
                            finish();
//                            removeAllTiles();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.save_file)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                    .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();
        }
        else {
//            startActivity(new Intent(CalcActivity.this, MainActivity.class));
            finish();
//            removeAllTiles();
        }
    }



    ///CALCULATION METHODS

    public Tile calculateUtility(Tile tile)
    {
        Double uN, cmax, skQ, zkQ, xkQ, rkQ, ikQbis, ikMbis = 0d, cumulatedTRatio = 1d;


        uN = tile.getUtilityNominalVoltage(); // in kV
        skQ = tile.getUtilityScPower(); // in MVA
        if (uN <= 0.4)
            cmax = 1d;
        else if (0.4 < uN && uN <= 1)
            cmax = 1.05d;
        else
            cmax = 1.1d;

        if (wasTrafo) {
            for(int i = 0; i < trafoRatiosList.size(); i++)
            {
                cumulatedTRatio = cumulatedTRatio * trafoRatiosList.get(i) * trafoRatiosList.get(i);
            }
        }

        zkQ = (cmax * 1000 * uN * 1000 * uN) * (cumulatedTRatio) / (1000000 * skQ); // in oms

        if (uN > 35) //in kV
        {
            rkQ = 0d;
            xkQ = zkQ;
        }
        else
            {
            xkQ = 0.995 * zkQ; // in oms
            rkQ = 0.1 * xkQ; // in oms
        }

        if(tileList.get(tileList.size()-1).getType() == TYPE_MOTOR && tileList.indexOf(tile) == tileList.size()-2)
        {
            ikMbis = tileList.get(tileList.size()-1).getScCurrent();
        }


        ikQbis = cmax * (1000000*uN) / (Math.sqrt(3) * 1000000 * zkQ) + ikMbis; // in kA
//        System.out.println("*&**&*&*&*"+ikQbis);


        tile.setUtilityImpedance(zkQ);
        tile.setUtilityReactance(xkQ);
        tile.setUtilityResistance(rkQ);
        tile.setUtilityCurrent(ikQbis);

        reactancesList.add(xkQ);
        resistancesList.add(rkQ);

//        cumulatedTRatio = 1d;
        return tile;
    }

    public Tile calculateTrafo(Tile tile, int elementPosition, String name) {
        Double sT, uP, uS, uk, nomLoss,  uT, zkT, rkT, xkT, uN;

        sT = tile.getTrafoPower(); // in kVA
        nomLoss = tile.getTrafoNominalPowerLoss(); //in kW
        uP = tile.getTrafoPrimaryVoltage(); // in kV
        uS = tile.getTrafoSeconaryVoltage(); //in kV
        uk = tile.getTrafoScVoltage(); // in %

//        System.out.println("In trafo calc:"+String.valueOf(lastTrafoVoltage));



        if (wasTrafo == false)
            lastTrafoVoltage = uS;

        wasTrafo = true;

        //ratio = Us / Up;

        trafoRatiosList.add(uS/uP);


        uN = Double.valueOf(lastLineVoltage);
        tile.setElementOperationalVoltage(uN);
        lastLineVoltage = String.valueOf(uP);

        uT = uS; //uS should be dependant on voltage by which trafo Sc impedance is calculated


        if (sT >= 2500 || uk > 10) //in kVA / in %
            rkT = 0d;
        else
            rkT = (nomLoss * 1000 * uS * 1000 * uS) / (sT * sT) / 1000;

        zkT = (uk * uT * uT) / (100 * sT / 1000);
        xkT = Math.sqrt((zkT * zkT) - (rkT * rkT));

        tile.setTrafoReactance(xkT); //!!!!!!!!!!!!!!!!!!!!
        tile.setTrafoResistance(rkT);
        tile.setTrafoImpedance(zkT);

        System.out.println("Trafo position:"+ elementPosition +" Name "+name);

        if(pointOfSSCPosition > elementPosition) {
            reactancesList.add(xkT);
            resistancesList.add(rkT);
        }
        else
        {
            reactancesList.add(0d);
            resistancesList.add(0d);
        }


        return tile;

    }


    public Tile calculateTrafoCurrent(Tile tile, Double xC, Double rC) {

        Double uN, cmax, ikTbis, zK, ikMbis = 0d;

        zK = Math.sqrt(xC * xC + rC * rC);

        uN = tile.getElementOperationalVoltage();
//        uN = Double.valueOf(lastLineVoltage);

        if (uN <= 0.4)
            cmax = 1d;
        else if (0.4 < uN && uN <= 1)
            cmax = 1.05d;
        else
            cmax = 1.1d;

//        if(tileList.get(tileList.size()-1).getType() == TYPE_MOTOR && tile.getId() == tileList.size()-2)
        if(tileList.get(tileList.size()-1).getType() == TYPE_MOTOR && tileList.indexOf(tile) == tileList.size()-2)
        {
            ikMbis = tileList.get(tileList.size()-1).getScCurrent();
        }

        ikTbis = cmax * (uN) / (Math.sqrt(3d) * zK) + ikMbis;

        tile.setTrafoImpedance(zK);
        tile.setTrafoCurrent(ikTbis);

        return tile;

    }

    public Tile calculateLine(Tile tile, int elementPosition, String name) {
        Double rL, xL, lL, sL, uN, gamma, zL = 0d, cumulatedTRatio = 1d;

        lL = Double.valueOf(tile.getLineLenght());
        sL = Double.valueOf(tile.getLineCrossSection());

        uN = Double.valueOf(lastLineVoltage);
        tile.setElementOperationalVoltage(uN);

//        System.out.println("!!!!" + uN);
//        System.out.println("!!!!" +tile.getElementOperationalVoltage());


        if (tile.getLineMaterial().equals("CU"))
            gamma = 56.0;

        else if (tile.getLineMaterial().equals("AL"))
            gamma = 35.0;

        else gamma = -1.0;

        if (wasTrafo) {
            for(int i = 0; i < trafoRatiosList.size(); i++)
            {
                cumulatedTRatio = cumulatedTRatio * trafoRatiosList.get(i) * trafoRatiosList.get(i);
            }
        }

//                System.out.println("!!!!" + cumulatedTRatio);


        rL = lL / (gamma * sL) * cumulatedTRatio;

        // Line reactance if cable line

        if (tile.getLinePlacement().equals(linePlacementA[0])) {
            if (uN < 1f)  //voltage dependence U < 1kC - 0.08 , U >= 1kV - 0.1
                xL = (0.08 * lL / 1000) * cumulatedTRatio;
            else
                xL = (0.1 * lL / 1000) * cumulatedTRatio;
        } else {
            if (uN < 1f) //voltage dependence U < 1kC - 0.3 , U >= 1kV - 0.4
                xL = (0.3 * lL / 1000) * cumulatedTRatio;
            else
                xL = (0.4 * lL / 1000) * cumulatedTRatio;
        }

        zL = Math.sqrt(xL * xL + rL * rL) * cumulatedTRatio;


        tile.setLineReactance(xL);
        tile.setLineResistance(rL);

        System.out.println("Line position:"+ elementPosition +" Name "+name);


        if(pointOfSSCPosition > elementPosition)
        {
            if (loadPhasesNo.equals("~3")) {
                reactancesList.add(xL);
                resistancesList.add(rL);
            } else
                {
                if (wasTrafo == true) {
                    reactancesList.add(xL);
                    resistancesList.add(rL);
                    reactanceshList1Ph.add(0d);
                    resistancesList1Ph.add(0d);
                } else {
                    reactancesList.add(0d);
                    resistancesList.add(0d);
                    reactanceshList1Ph.add(xL);
                    resistancesList1Ph.add(rL);
                }
            }
        }
        else
        {
            reactancesList.add(0d);
            resistancesList.add(0d);
            reactanceshList1Ph.add(0d);
            resistancesList1Ph.add(0d);
        }



        tile.setLineImpedance(zL);

        return tile;
    }

    public Tile calculateLineCurrent(Tile tile, Double xC, Double rC) {
        Double ikLbis, zK, uN, cmax, ikMbis = 1d;

        uN = tile.getElementOperationalVoltage();

        if (uN <= 0.4)
            cmax = 1d;
        else if (0.4 < uN && uN <= 1)
            cmax = 1.05d;
        else
            cmax = 1.1d;

        zK = Math.sqrt(xC * xC + rC * rC);

        if(tileList.get(tileList.size()-1).getType() == TYPE_MOTOR && tileList.indexOf(tile) == tileList.size()-2)
        {
            ikMbis = tileList.get(tileList.size()-1).getScCurrent();
        }

        ikLbis = cmax * uN / (1.73 * zK) + ikMbis;

        tile.setLineImpedance(zK);
        tile.setLineCurrent(ikLbis);
        return tile;
    }

    private void calculateLoad(Tile tile) {

        lastLineVoltage = String.valueOf(tile.getLoadVoltage());
        loadVoltageAux = tile.getLoadVoltage();
        loadPhasesNo = tile.getLoadPhaseNo();
        reactancesList.add(0.0);
        resistancesList.add(0.0);
     }

    public Tile calculateMotor(Tile tile) {
        Double pN, uN, eta, cosFi, kr,cMax, ikMbis, ipM, zkM, rkM, xkM;

        pN = tile.getPower(); // in kW
        uN = tile.getVoltage(); // in kV
        cosFi = tile.getCosFi(); //in -
        eta = tile.getEfficiency(); // in -
        kr = tile.getStartUpFactor(); //in -

        if (uN <= 0.4)
            cMax = 1d;
        else if (0.4 < uN && uN <= 1)
            cMax = 1.05d;
        else
            cMax = 1.1d;

        zkM = (uN*1000 * uN*1000 * eta * cosFi) / (kr * pN*1000);
        xkM = 0.922 * zkM;
        rkM = 0.42 * xkM;

        ikMbis = cMax * uN / (Math.sqrt(3d) * zkM);
        ipM = 1.3*Math.sqrt(2)*ikMbis;

        tile.setReactance(xkM); //!!!!!!!!!!!!!!!!!!!!
        tile.setResistance(rkM);
        tile.setImpedance(zkM);
        tile.setScCurrent(ikMbis);
        tile.setMotorScImpCurrent(ipM);

//        System.out.println("#$#$#$#$"+ikMbis);
//        System.out.println("#$#$#$#$"+ipM);


        loadPhasesNo = "~3";
        loadVoltageAux = tile.getVoltage();

        lastLineVoltage = String.valueOf(uN);
        reactancesList.add(0.0);
        resistancesList.add(0.0);

        return tile;
    }

    private void motorLowerScCurrentByBetweenItems()
    {
        double zkM = 0d, km = 1d, zCumBusToMotor = 0d, ikMbis = 0d, ikMbisPrim = 0d, ipM = 0d, ipMPrim = 0d;

        if(tileList.get(tileList.size()-1).getType() == TYPE_MOTOR)
        {
            zkM = tileList.get(tileList.size()-1).getImpedance();
            ikMbis = tileList.get(tileList.size()-1).getScCurrent();
            ikMbisPrim = tileList.get(tileList.size()-1).getMotorScPrimCurrent();
            ipM = tileList.get(tileList.size()-1).getMotorScImpCurrent();
            ipMPrim = tileList.get(tileList.size()-1).getMotorScImpPrimCurrent();

            System.out.println("TileList size:" +tileList.size());

        for(int i = 0; i < tileList.size(); i++)
        {
            System.out.println("TileList:" +tileList.get(i).getType());

            if(tileList.get(i).getType() == TYPE_BUS)
            {
                System.out.println("TYPE_BUS");

                for(int j = i+1; j < tileList.size(); j++)
                {
                    if(tileList.get(j).getType() == TYPE_CABLE)
                    {
                        System.out.println("TYPE_CABLE");

                        zCumBusToMotor = zCumBusToMotor + tileList.get(j).getLineImpedance();
                    }
                    if(tileList.get(j).getType() == TYPE_TRAFO)
                    {
                        System.out.println("TYPE_TRAFO");
                        zCumBusToMotor = zCumBusToMotor + tileList.get(j).getTrafoImpedance();
                    }
                }
                break;
            }
        }
            km = zkM / (zkM + zCumBusToMotor); //TODO check if formula is ok
            ikMbisPrim = ikMbis * km; //TODO check if formula is ok
            ipMPrim = ipM * km; //TODO check if formula is ok

//            tileList.get(tileList.size()-1).setScCurrent(ikMbis);
            tileList.get(tileList.size()-1).setMotorScPrimCurrent(ikMbisPrim);
            tileList.get(tileList.size()-1).setMotorScImpPrimCurrent(ipMPrim);

//             System.out.println("!@!@!@"+tileList.get(tileList.size()-1).getScCurrent());
//             System.out.println("!@!@!@"+tileList.get(tileList.size()-1).getMotorScPrimCurrent());
//             System.out.println("!@!@!@"+tileList.get(tileList.size()-1).getMotorScImpCurrent());
//             System.out.println("!@!@!@"+tileList.get(tileList.size()-1).getMotorScImpPrimCurrent());

//            System.out.println("!@!@!@"+zCumBusToMotor);
//            System.out.println("!@!@!@"+zkM);
//            System.out.println("!@!@!@"+ikMbis);
//            System.out.println("@!@!@!"+ikMbisPrim);
//            System.out.println("@!@!@!"+ipM);
//            System.out.println("@!@!@!"+ipMPrim);
        }
    }

    private void calculateBus(Tile tile) {

//        lastLineVoltage = String.valueOf(tile.getVoltage());
        tile.setVoltage(Double.valueOf(lastLineVoltage));
        reactancesList.add(0.0);
        resistancesList.add(0.0);

        Double uN;

        uN = Double.valueOf(lastLineVoltage);
        tile.setElementOperationalVoltage(uN);

        tile.setNoOfPhases(loadPhasesNo);
    }

    private Tile calculateScCurrent(Tile tile, Double xC, Double rC, Double xC1, Double rC1) {

        Double ik3Bis = 0d, zKC, uN, cmax, ikMbis = 0d, ikMbisPrim = 0d,ipM = 0d, ipMPrim = 0d, ip = 0d, km = 1d, zCumBusToMotor = 0d, zkM = 0d,
                ik1Bis = 0d;

        uN = tile.getElementOperationalVoltage();

        if (uN <= 0.4)
            cmax = 1d;
        else if (0.4 < uN && uN <= 1)
            cmax = 1.05d;
        else
            cmax = 1.1d;

        if(tileList.get(tileList.size()-1).getType() == TYPE_MOTOR) // && tileList.indexOf(tile) == tileList.size()-2
        {
            ikMbis = tileList.get(tileList.size()-1).getScCurrent();
            ikMbisPrim = tileList.get(tileList.size()-1).getMotorScPrimCurrent();
            ipM = tileList.get(tileList.size()-1).getScImpCurrent();
            ipMPrim = tileList.get(tileList.size()-1).getMotorScImpPrimCurrent();
        }


        rC = rC + 1.24*2*rC1;
        xC = xC + 2*xC1;

        zKC = Math.sqrt(xC * xC + rC * rC);

        if(loadPhasesNo.equals("~3"))
        {
            ik3Bis = cmax * (uN) / (Math.sqrt(3d) * zKC) + ikMbisPrim;

            ip = (1.02 + 0.98 * Math.exp(-3 * rC / xC)) * Math.sqrt(2) * (cmax * (uN) / (Math.sqrt(3d) * zKC)) + ipMPrim;
        }
        else
        {
//            System.out.println("Load voltage: "+uN);
            ik1Bis = (0.95*uN)/zKC;
            ip = (1.02 + 0.98 * Math.exp(-3 * rC / xC)) * Math.sqrt(2) * ik1Bis;

        }

        tile.setScReactance(xC);
        tile.setScResistance(rC);
        tile.setScImpedance(zKC);
        if(loadPhasesNo.equals("~3"))
            tile.setScCurrent(ik3Bis);
        else
            tile.setScCurrent(ik1Bis);
        tile.setScImpCurrent(ip);

//        System.out.println(String.valueOf(xC));
//        System.out.println(String.valueOf(rC));
//        System.out.println(String.valueOf(zK));
//        System.out.println(String.valueOf(ik3bis));

        return tile;

    }


    private void calcStartState()
    {


        trafoRatiosList.clear();

        namesOfTilesWithParametersNotEntered.clear();
        listOfLackingItmes.clear();

        zK = 0d;

        uP1 = 0d;
        uS1 = 0d;

        //    buttons behavior variables
         noUtility = true;
         loadPresent = false;
        busPresent = false;
        allItemsParametersEntered = false;
            allNecessaryItemsPresent = false;
//         trafoIsFirst = false;

        itemsTilesInRightOrder = false;

        //trafo opertaions
        wasTrafo = false;
        firstTrafo = true;

        firstTrafoVoltage = 0d;
        lastTrafoVoltage = 0d;
        loadVoltageAux = 0d;


        //files operation variables
        workSaved = true;

        //    buttons behavior start state
        addUtilityB.setEnabled(true);
        addTrafoB.setEnabled(true);
        addCableB.setEnabled(false);
        addLoadB.setEnabled(false);
        addMotorB.setEnabled(false);
        addBusB.setEnabled(false);
        calcB.setEnabled(loadPresent);

        tempTile=null;
        trafosVoltagesMatch = true;
    }

    private void clearIDnumber()
    {
        globalTileID = 0;
        utilityID = 1;
        trafoID = 1;
        lineID = 1;
        loadID = 1;
        busID = 1;
    }

//    private void checkIfLoadVoltageEqualsLastTrafoVoltage(Double uToBeChecked)
//    {
//
//            if (lastTrafoVoltage.equals(uToBeChecked))
//                lastTrafoVoltageEqualsLoadVoltage = true;
//            else lastTrafoVoltageEqualsLoadVoltage = false;
//
//    }


    private void calculateSC()
    {

        Double summedReacatances = 0d, summedResistances = 0d, summedReactances1ph = 0d, summedResistances1Pf = 0d;

        if(allNecessaryItemsPresent) {
            if (itemsTilesInRightOrder) {
                if (allItemsParametersEntered)
                {

                    for (int i = tileList.size() - 1; i >= 0; i--) //calculate reactances and resistances of all items
                    {
                        if (tileList.get(i).getType() == TYPE_UTILITY) {
                            tileList.get(i).equals(calculateUtility(tileList.get(i)));

                        }
                        if (tileList.get(i).getType() == TYPE_TRAFO)
                        {
                            tileList.get(i).equals(calculateTrafo(tileList.get(i), i,tileList.get(i).getTrafoName()));


                        }
                        if (tileList.get(i).getType() == TYPE_CABLE) {
                            tileList.get(i).equals(calculateLine(tileList.get(i), i, tileList.get(i).getLineName()));

                        }

                        if (tileList.get(i).getType() == TYPE_LOAD) {
                            calculateLoad(tileList.get(i));
                        }
                        if (tileList.get(i).getType() == TYPE_MOTOR) {
                            calculateMotor(tileList.get(i));
                        }
                        if (tileList.get(i).getType() == TYPE_BUS)
                        {
                            calculateBus(tileList.get(i));
                            System.out.println("Current U: "+lastLineVoltage);

                        }

                    }

                    System.out.println("reactancesList");
                    for (int i = 0; i < reactancesList.size(); i++)
                        System.out.println(String.valueOf(reactancesList.get(i)));

                    System.out.println("resistancesList");
                    for (int i = 0; i < resistancesList.size(); i++)
                        System.out.println(String.valueOf(resistancesList.get(i)));

                    System.out.println("reactancesList1ph");
                    for (int i = 0; i < reactanceshList1Ph.size(); i++)
                        System.out.println(String.valueOf(reactanceshList1Ph.get(i)));

                    System.out.println("resistancesList1ph");
                    for (int i = 0; i < resistancesList1Ph.size(); i++)
                        System.out.println(String.valueOf(resistancesList1Ph.get(i)));

//                            checkIfLoadVoltageEqualsLastTrafoVoltage(loadVoltageAux);
//                    System.out.println("loadVoltageAux = "+String.valueOf(loadVoltageAux));
//                    System.out.println("lastTrafoVoltage = "+String.valueOf(lastTrafoVoltage));
//                    System.out.println("lastLineVoltage = "+String.valueOf(lastLineVoltage));

                    checkIflastTrafoVoltageEqualsLoadVoltage();
                    checkIfTrafosVoltagesMatch();

//                        System.out.println("lastTrafoVoltageEqualsLoadVoltage = "+String.valueOf(lastTrafoVoltageEqualsLoadVoltage));


                        if(lastTrafoVoltageEqualsLoadVoltage) //check if load's nominal voltage is the saame as last trafo's Secondary voltage

                            if(trafosVoltagesMatch)
                            {
                                {
                                    adapter.notifyDataSetChanged();

                                    //add impedances of items between bus with SC and motor
                                    motorLowerScCurrentByBetweenItems();

                                    //reverse calculated lists
                                    Collections.reverse(reactancesList);
                                    Collections.reverse(resistancesList);
                                    Collections.reverse(reactanceshList1Ph);
                                    Collections.reverse(resistancesList1Ph);
                                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! NOT SUMMED !!!!!!!!!!!!!!!!!!!!!


                                    for (int i = 1; i < reactancesList.size(); i++) // sum reactances for sc current calculation
                                    {
                                        reactancesList.set(i, reactancesList.get(i) + reactancesList.get(i - 1));
                                        summedReacatances = reactancesList.get(i);
                                    }

                                    for (int i = 1; i < resistancesList.size(); i++) // sum resistances for sc current calculation
                                    {
                                        resistancesList.set(i, resistancesList.get(i) + resistancesList.get(i - 1));
                                        summedResistances = resistancesList.get(i);
                                    }
                                    for (int i = 1; i < reactanceshList1Ph.size(); i++) // sum reactances for sc current calculation
                                    {
                                        reactanceshList1Ph.set(i, reactanceshList1Ph.get(i) + reactanceshList1Ph.get(i - 1));
                                        summedReactances1ph = resistancesList1Ph.get(i);
                                    }

                                    for (int i = 1; i < resistancesList1Ph.size(); i++) // sum resistances for sc current calculation
                                    {
                                        resistancesList1Ph.set(i, resistancesList1Ph.get(i) + resistancesList1Ph.get(i - 1));
                                        summedResistances1Pf = resistancesList1Ph.get(i);
                                    }


//                            System.out.println("reactancesList");
//                            for (int i = 0; i < reactancesList.size(); i++)
//                                System.out.println(String.valueOf(String.valueOf(reactancesList.get(i))));
//                            System.out.println("resistancesList");
//
//                            for (int i = 0; i < resistancesList.size(); i++)
//                                System.out.println(String.valueOf(String.valueOf(resistancesList.get(i))));


                                    //calc SSCC just in point of scc item
                                    for (int i = 0; i < tileList.size(); i++) { //calculate sc currents of items
//                                if (tileList.get(i).getType() == TYPE_UTILITY) {
//                                }
//                                if (tileList.get(i).getType() == TYPE_TRAFO) {
////                                tileList.get(i).equals(calculateTrafoCurrent(tileList.get(i), reactancesList.get(i), resistancesList.get(i)));
//                                }
//                                if (tileList.get(i).getType() == TYPE_CABLE) {
////                                tileList.get(i).equals(calculateLineCurrent(tileList.get(i), reactancesList.get(i), resistancesList.get(i)));
//                                }
//                                if (tileList.get(i).getType() == TYPE_LOAD) {
//                                }
//                                if (tileList.get(i).getType() == TYPE_MOTOR) {
//                                }
                                        if (tileList.get(i).getType() == TYPE_BUS) {
                                            tileList.get(i).equals(calculateScCurrent(tileList.get(i), summedReacatances, summedResistances,
                                                    summedReactances1ph, summedResistances1Pf));
                                        }
                                    }

                                }
                                Toast.makeText(getApplicationContext(), getString(R.string.calc_success), Toast.LENGTH_SHORT).show();
                            }
                                // calc end here
                          else
                                {
                                    Toast.makeText(getApplicationContext(), getString(R.string.trafos_u_not_match), Toast.LENGTH_SHORT).show();
                                }
                                     else
                    {
                        Toast.makeText(getApplicationContext(),getString(R.string.last_trafo_and_loadu_u_not_match), Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    String allNames = " ";
                    for (int i = 0; i < namesOfTilesWithParametersNotEntered.size(); i++) {
                        allNames = allNames + namesOfTilesWithParametersNotEntered.get(i) + " ";
                    }
//                    Toast.makeText(getApplicationContext(), getString(R.string.enter_all_values), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), getString(R.string.enter_values_in_tiles) + allNames, Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getApplicationContext(), getString(R.string.tiles_wrong_order), Toast.LENGTH_SHORT).show();
        }
        else {
            String lackingItems = " ";
            for (int i = 0; i < listOfLackingItmes.size(); i++)
            {
                lackingItems = lackingItems + listOfLackingItmes.get(i) + " ";
            }
            Toast.makeText(getApplicationContext(), getString(R.string.tiles_necessary_not_present)+" "+getString(R.string.lackof)+" "+lackingItems, Toast.LENGTH_SHORT).show();
        }

        //variables reset
//                    reactancesList.removeAll(reactancesList);
//                    resistancesList.removeAll(resistancesList);

        reactancesList.clear();
        resistancesList.clear();
        reactanceshList1Ph.clear();
        resistancesList1Ph.clear();
        trafoRatiosList.clear();
        listOfLackingItmes.clear();

        firstTrafoVoltage = 0d;
        lastTrafoVoltage = 0d;
        loadVoltageAux = 0d;

        zK = 0.0;

        uP1 = 0d;
        uS1 = 0d;

        pointOfSSCPosition = 0;

        globalTileID = 0;

        noUtility = true;
        wasTrafo = false;
        firstTrafo = true;
        lastTrafoVoltageEqualsLoadVoltage = false;
        tempTile=null;
        trafosVoltagesMatch = true;

        //end of variables reset
    }

    private void checkIfAllItemsParametersEntered()
    {
        allItemsParametersEntered = true;
        for(int i = 0; i < tileList.size(); i++)
        {
            if(tileList.get(i).getAllParametersEnetered()==false)
            {
                allItemsParametersEntered = false;
                break;
            }
        }
        for(int i = 0; i < tileList.size(); i++)
        {
            String name = "";

            if(tileList.get(i).getAllParametersEnetered()==false)
            {
                if(tileList.get(i).getType()==TYPE_UTILITY)
                    name = tileList.get(i).getUtilityName();
                else if(tileList.get(i).getType()==TYPE_TRAFO)
                    name = tileList.get(i).getTrafoName();
                else if(tileList.get(i).getType()==TYPE_CABLE)
                    name = tileList.get(i).getLineName();
                else if(tileList.get(i).getType()==TYPE_LOAD)
                    name = tileList.get(i).getLoadName();
                else if(tileList.get(i).getType()==TYPE_MOTOR)
                    name = tileList.get(i).getMotorName();
//                else if(tileList.get(i).getType()==TYPE_BUS)
//                    name = tileList.get(i).getName();
//                else
//                    break;
            }
            namesOfTilesWithParametersNotEntered.add(name);
        }
    }

    private void checkIfTilesInRightOrder() {
        itemsTilesInRightOrder = true;
        if (tileList.get(0).getType() == TYPE_TRAFO) {
            for (int i = 0; i < tileList.size(); i++) {
                if (tileList.get(i).getType() == TYPE_UTILITY) {
                    if (tileList.indexOf(tileList.get(i)) != 0) {
//                    System.out.println("!!! "+tileList.indexOf(tileList.get(i))+" !!! "+String.valueOf(tileList.size()-1));
                        itemsTilesInRightOrder = false;
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < tileList.size(); i++) {
            if (tileList.get(i).getType() == TYPE_LOAD || tileList.get(i).getType() == TYPE_MOTOR) {
                if (tileList.indexOf(tileList.get(i)) != tileList.size() - 1) {
//                    System.out.println("!!! "+tileList.indexOf(tileList.get(i))+" !!! "+String.valueOf(tileList.size()-1));
                    itemsTilesInRightOrder = false;
                    break;
                }
            }
        }


//        // TODO - Trafo after Point of ssc
        int auxPSCCPosition = 0, auxTrafoPosition = 0;
        for (int i = 0; i < tileList.size(); i++) {
            if (tileList.get(i).getType() == TYPE_BUS)
                auxPSCCPosition = i;
            if (tileList.get(i).getType() == TYPE_TRAFO)
                auxTrafoPosition = i;
        }

        if(auxPSCCPosition < auxTrafoPosition) {
            itemsTilesInRightOrder = false;
        }



    }
    private void checkIfAllNecessaryTilesPresent()
    {
        boolean sourcePresent = false, busPresent = false, loadPresent = false;
        for(int i = 0; i < tileList.size(); i++)
        {
            if(tileList.get(i).getType() == TYPE_UTILITY || tileList.get(i).getType() == TYPE_TRAFO)
            {
                sourcePresent = true;
            }
            if(tileList.get(i).getType() == TYPE_BUS)
            {
                busPresent = true;
            }
            if(tileList.get(i).getType() == TYPE_LOAD || tileList.get(i).getType() == TYPE_MOTOR)
            {
                loadPresent = true;
            }
        }
        if (sourcePresent && busPresent  && loadPresent ) {
            allNecessaryItemsPresent = true;
        }

        if(!sourcePresent)
        {
            listOfLackingItmes.add(getString(R.string.source));
        }
        if(!busPresent)
        {
            listOfLackingItmes.add(getString(R.string.bus));
        }
        if(!loadPresent)
        {
            listOfLackingItmes.add(getString(R.string.load));
        }
    }


    private void checkIflastTrafoVoltageEqualsLoadVoltage()
    {

        //check if load nom. voltage equals last traf's sec. voltage

        if(loadPhasesNo.equals("~3"))
        {
            if (lastTrafoVoltage >= loadVoltageAux * 0.9 && lastTrafoVoltage <= loadVoltageAux * 1.1)
            lastTrafoVoltageEqualsLoadVoltage = true;


        }
        else if(loadPhasesNo.equals("~1"))
        {
            if (lastTrafoVoltage / Math.sqrt(3) >= loadVoltageAux * 0.9
                    && lastTrafoVoltage / Math.sqrt(3) <= loadVoltageAux*1.1)

                lastTrafoVoltageEqualsLoadVoltage = true;

//            System.out.println(lastTrafoVoltage / Math.sqrt(3) + ">=" + loadVoltageAux * 0.9);
//            System.out.println(lastTrafoVoltage / Math.sqrt(3) + "<=" + loadVoltageAux*1.1);
        }
    }


    private void checkIfTrafosVoltagesMatch()
    {

        //The check is turn off

        for(int i=tileList.size()-1; i >= 0 ; i--)
        {

            if(tileList.get(i).getType() == TYPE_TRAFO)
            {

                if(tempTile==null)
                {
                    tempTile = tileList.get(i);
                }
                else
                {
                    if(tileList.get(i).getTrafoSeconaryVoltage() >= 0.8 * tempTile.getTrafoPrimaryVoltage()
                            && tileList.get(i).getTrafoSeconaryVoltage() <= 1.2 * tempTile.getTrafoPrimaryVoltage())
                    {
                        tempTile = tileList.get(i);
                    }
                    else
                    {
                        trafosVoltagesMatch = false;
                        tempTile = null;
                        break;
                    }
                }
            }
        }
    }

    public String getLoadPhasesNo()
    {
        return loadPhasesNo;
    }


}
