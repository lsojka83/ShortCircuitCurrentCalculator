package pl.ls4soft.sscc;

import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

/**
 * Created by Łukasz on 2017-01-22.
 */

public class Tile extends AppCompatActivity implements Serializable{


    //General variables
    private int id;
    private int type;
//    private int no;
    private double elementOperationalVoltage = 0d;
    private boolean allParametersEnetered = false;
    private boolean wasEdited = false;


    //General items variables
    private String name = null;
    private double power = 0d, efficiency = 0d, startUpFactor = 0d, cosFi = 0d, voltage = 0d, impedance = 0d, reactance = 0d, resistance = 0d;
    private double scImpedance = 0d, scReactance = 0d, scResistance = 0d, scCurrent = 0d, scImpCurrent;


    //Utility variables
    private String utilityName = null;
    private double utilityScPower = 0d, utilityNominalVoltage = 0d, utilityImpedance = 0d, utilityReactance = 0d, utilityResistance = 0d, utilityCurrent = 0d;

    //Trafo variables
    private  String trafoName = null;
    private double trafoPower = 0d, trafoPrimaryVoltage = 0d, trafoSeconaryVoltage = 0d, trafoScVoltage = 0d, trafoNominalPowerLoss = 0d,
            trafoImpedance = 0d,
            trafoReactance = 0d, trafoResistance = 0d, trafoCurrent = 0d;

    //Line variables
    private String lineName = null, lineMaterial =  null, linePlacement = null;
    private double lineLenght = 0d, lineCrossSection = 0d,  lineImpedance = 0d, lineReactance = 0d, lineResistance = 0d, lineCurrent = 0d;

    //Load variables
    private String loadName = null;

     private String loadPhaseNo = null;
    private double loadVoltage = 0d;

    //Motor variable
    private String motorName = null;
    private double motorScImpCurrent,motorScImpPrimCurrent,  motorScPrimCurrent;

     //Bus variables
    private String noOfPhases = null;



    //Motor variable
//    private String busName = null;




    //empty

//    //Utility variables
//    private String utilityName = null;
//    private double utilityScPower = 250d, utilityNominalVoltage = 15d, utilityImpedance = 0d, utilityReactance = 0d, utilityResistance = 0d, utilityCurrent = 0d;
//
//    //Trafo variables
//    private  String trafoName = null;
//    private double trafoPower = 630d, trafoPrimaryVoltage = 15d, trafoSeconaryVoltage = 0.42, trafoScVoltage = 4.5, trafoNominalPowerLoss = 9.45,
//            trafoImpedance = 0d,
//            trafoReactance = 0d, trafoResistance = 0d, trafoCurrent = 0d;
//
//    //Line variables
//    private String lineName = null, lineMaterial = "AL", linePlacement = "Air";
//    private double lineLenght = 400d, lineCrossSection = 70d,  lineImpedance = 0d, lineReactance = 0d, lineResistance = 0d, lineCurrent = 0d;
//
//    //Load variables
//    private String loadName = null;
//    private double loadVoltage = 0.4;


//        //Utility variables
//    private String utilityName = null, utilityScPower = "1500", utilityNominalVoltage = "110", utilityImpedance = null, utilityReactance = null, utilityResistance = null, utilityCurrent = null;
//
//    //Trafo variables
//    private  String trafoName = null, trafoPower = "1600", trafoPrimaryVoltage = "20", trafoSeconaryVoltage = "6.3", trafoScVoltage = "6", trafoNominalPowerLoss = "17",
//            trafoImpedance = null,
//            trafoReactance = null, trafoResistance = null, trafoCurrent = null;
//
//    //Line variables
//    private String lineName = null, lineLenght = "500", lineCrossSection = "95", lineMaterial = "AL", linePlacement = "Ground", lineImpedance = null, lineReactance = null, lineResistance = null, lineCurrent = null;
//
//    //Load variables
//    private String loadName = null, loadVoltage = "6.3";

    //Tile constructor
    public Tile(int id, int type, int no) {
        this.id = id;
        this.type = type;

        if(type == 0)
        {
            utilityName = "U"+no;
        }
        if(type == 1)
        {
            trafoName = "T"+no;
        }
        if(type == 2)
        {
            lineName = "L"+no;
        }
        if(type == 3)
        {
            loadName = "LD"+no;
        }
        if(type == 4)
        {
            motorName = "M"+no;
        }
        if(type == 5)
        {
//            name = "B" +no;
            allParametersEnetered = true;
        }

    }


//Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public double getElementOperationalVoltage() {
        return elementOperationalVoltage;
    }

    public void setElementOperationalVoltage(double elementOperationalVoltage) {
        this.elementOperationalVoltage = elementOperationalVoltage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double powerkW) {
        this.power = powerkW;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public double getStartUpFactor() {
        return startUpFactor;
    }

    public void setStartUpFactor(double startUpFactor) {
        this.startUpFactor = startUpFactor;
    }

    public double getCosFi() {
        return cosFi;
    }

    public void setCosFi(double cosFi) {
        this.cosFi = cosFi;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double volage) {
        this.voltage = volage;
    }

    public double getImpedance() {
        return impedance;
    }

    public void setImpedance(double impedance) {
        this.impedance = impedance;
    }

    public double getReactance() {
        return reactance;
    }

    public void setReactance(double reactance) {
        this.reactance = reactance;
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }


    public double getScImpedance() {
        return scImpedance;
    }

    public void setScImpedance(double scImpedance) {
        this.scImpedance = scImpedance;
    }

    public double getScReactance() {
        return scReactance;
    }

    public void setScReactance(double scReactance) {
        this.scReactance = scReactance;
    }

    public double getScResistance() {
        return scResistance;
    }

    public void setScResistance(double scResistance) {
        this.scResistance = scResistance;
    }

    public double getScCurrent() {
        return scCurrent;
    }

    public void setScCurrent(double scCurrent) {
        this.scCurrent = scCurrent;
    }

    public double getScImpCurrent() {
        return scImpCurrent;
    }

    public void setScImpCurrent(double scImpCurrent) {
        this.scImpCurrent = scImpCurrent;
    }

    public String getUtilityName() {
        return utilityName;
    }

    public void setUtilityName(String utilityName) {
        this.utilityName = utilityName;
    }

    public double getUtilityScPower() {
        return utilityScPower;
    }

    public void setUtilityScPower(double utilityScPower) {
        this.utilityScPower = utilityScPower;
    }

    public double getUtilityNominalVoltage() {
        return utilityNominalVoltage;
    }

    public void setUtilityNominalVoltage(double utilityNominalVoltage) {
        this.utilityNominalVoltage = utilityNominalVoltage;
    }

    public double getUtilityImpedance() {
        return utilityImpedance;
    }

    public void setUtilityImpedance(double utilityImpedance) {
        this.utilityImpedance = utilityImpedance;
    }

    public double getUtilityReactance() {
        return utilityReactance;
    }

    public void setUtilityReactance(double utilityReactance) {
        this.utilityReactance = utilityReactance;
    }

    public double getUtilityResistance() {
        return utilityResistance;
    }

    public void setUtilityResistance(double utilityResistance) {
        this.utilityResistance = utilityResistance;
    }

    public double getUtilityCurrent() {
        return utilityCurrent;
    }

    public void setUtilityCurrent(double utilityCurrent) {
        this.utilityCurrent = utilityCurrent;
    }

    public String getTrafoName() {
        return trafoName;
    }

    public void setTrafoName(String trafoName) {
        this.trafoName = trafoName;
    }

    public double getTrafoPower() {
        return trafoPower;
    }

    public void setTrafoPower(double trafoPower) {
        this.trafoPower = trafoPower;
    }

    public double getTrafoPrimaryVoltage() {
        return trafoPrimaryVoltage;
    }

    public void setTrafoPrimaryVoltage(double trafoPrimaryVoltage) {
        this.trafoPrimaryVoltage = trafoPrimaryVoltage;
    }

    public double getTrafoSeconaryVoltage() {
        return trafoSeconaryVoltage;
    }

    public void setTrafoSeconaryVoltage(double trafoSeconaryVoltage) {
        this.trafoSeconaryVoltage = trafoSeconaryVoltage;
    }

    public double getTrafoScVoltage() {
        return trafoScVoltage;
    }

    public void setTrafoScVoltage(double trafoScVoltage) {
        this.trafoScVoltage = trafoScVoltage;
    }

    public double getTrafoNominalPowerLoss() {
        return trafoNominalPowerLoss;
    }

    public void setTrafoNominalPowerLoss(double trafoNominalPowerLoss) {
        this.trafoNominalPowerLoss = trafoNominalPowerLoss;
    }

    public double getTrafoImpedance() {
        return trafoImpedance;
    }

    public void setTrafoImpedance(double trafoImpedance) {
        this.trafoImpedance = trafoImpedance;
    }

    public double getTrafoReactance() {
        return trafoReactance;
    }

    public void setTrafoReactance(double trafoReactance) {
        this.trafoReactance = trafoReactance;
    }

    public double getTrafoResistance() {
        return trafoResistance;
    }

    public void setTrafoResistance(double trafoResistance) {
        this.trafoResistance = trafoResistance;
    }

    public double getTrafoCurrent() {
        return trafoCurrent;
    }

    public void setTrafoCurrent(double trafoCurrent) {
        this.trafoCurrent = trafoCurrent;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getLineMaterial() {
        return lineMaterial;
    }

    public void setLineMaterial(String lineMaterial) {
        this.lineMaterial = lineMaterial;
    }

    public String getLinePlacement() {
        return linePlacement;
    }

    public void setLinePlacement(String linePlacement) {
        this.linePlacement = linePlacement;
    }

    public double getLineLenght() {
        return lineLenght;
    }

    public void setLineLenght(double lineLenght) {
        this.lineLenght = lineLenght;
    }

    public double getLineCrossSection() {
        return lineCrossSection;
    }

    public void setLineCrossSection(double lineCrossSection) {
        this.lineCrossSection = lineCrossSection;
    }

    public double getLineImpedance() {
        return lineImpedance;
    }

    public void setLineImpedance(double lineImpedance) {
        this.lineImpedance = lineImpedance;
    }

    public double getLineReactance() {
        return lineReactance;
    }

    public void setLineReactance(double lineReactance) {
        this.lineReactance = lineReactance;
    }

    public double getLineResistance() {
        return lineResistance;
    }

    public void setLineResistance(double lineResistance) {
        this.lineResistance = lineResistance;
    }

    public double getLineCurrent() {
        return lineCurrent;
    }

    public void setLineCurrent(double lineCurrent) {
        this.lineCurrent = lineCurrent;
    }

    public String getLoadName() {
        return loadName;
    }

    public void setLoadName(String loadName) {
        this.loadName = loadName;
    }

    public double getLoadVoltage() {
        return loadVoltage;
    }

    public void setLoadVoltage(double loadVoltage) {
        this.loadVoltage = loadVoltage;
    }


    public String getLoadPhaseNo() {
        return loadPhaseNo;
    }

    public void setLoadPhaseNo(String loadPhaseNo) {
        this.loadPhaseNo = loadPhaseNo;
    }


    public String getMotorName() {
        return motorName;
    }

    public void setMotorName(String motorName) {
        this.motorName = motorName;
    }

    public double getMotorScImpCurrent() {
        return motorScImpCurrent;
    }

    public void setMotorScImpCurrent(double motorScImpCurrent) {
        this.motorScImpCurrent = motorScImpCurrent;
    }

    public double getMotorScPrimCurrent() {
        return motorScPrimCurrent;
    }

    public void setMotorScPrimCurrent(double motorScPrimCurrent) {
        this.motorScPrimCurrent = motorScPrimCurrent;
    }

    public double getMotorScImpPrimCurrent() {
        return motorScImpPrimCurrent;
    }

    public void setMotorScImpPrimCurrent(double motorScImpPrimCurrent) {
        this.motorScImpPrimCurrent = motorScImpPrimCurrent;
    }

    public boolean getAllParametersEnetered() {
        return allParametersEnetered;
    }

    public void setAllParametersEnetered(boolean allParametersEnetered) {
        this.allParametersEnetered = allParametersEnetered;
    }

    public boolean getWasEdited() {
        return wasEdited;
    }

    public void setWasEdited(boolean wasEdited) {
        this.wasEdited = wasEdited;
    }

    public String getNoOfPhases() {
        return noOfPhases;
    }
    public void setNoOfPhases (String noOfPhases) {
        this.noOfPhases = noOfPhases;
    }
}