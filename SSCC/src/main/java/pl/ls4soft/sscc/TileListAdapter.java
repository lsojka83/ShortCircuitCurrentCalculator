package pl.ls4soft.sscc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Łukasz on 2017-01-22.
 */

public class TileListAdapter extends BaseAdapter{

    public static int TYPE_UTILITY = 0;
    public static int TYPE_TRAFO = 1;
    public static int TYPE_CABLE = 2;
    public static int TYPE_LOAD = 3;
    public static int TYPE_MOTOR = 4;
    public static int TYPE_BUS = 5;
//    public static int TYPE_BUS1PH = 51;
//    public static int TYPE_BUS3PH = 53;

    private LayoutInflater inflater;
    private Context context;
    private List<Tile> tileList;

    public TileListAdapter(Context context, List<Tile> tileList) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.tileList = tileList;
//        this.listener = listener;
    }


    public void addItem(final Tile listItem) {
        tileList.add(listItem);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return tileList.size();
    }

    @Override
    public Object getItem(int position) {
        return tileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Tile tile = tileList.get(position);
        final ViewHolder holder;
        final int type = getItemViewType(position);
//        System.out.println("getView " + position + " " + convertView + " type = " + type);

        if(convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case 0:
                    convertView = inflater.inflate(R.layout.utility_tile_layout, null);

                    holder.tileUtilityNameTV = (TextView) convertView.findViewById(R.id.tileUtilityNameTV);
                    holder.tileUtilityIV = (ImageView) convertView.findViewById(R.id.tileUtilityIV);
                    holder.tileUtilityIV.setImageResource(R.drawable.se_s);
                    holder.utilityTileSCPowerTV = (TextView) convertView.findViewById(R.id.utilityTileSCPowerTV);
                    holder.utilityTileNominalVoltageTV = (TextView) convertView.findViewById(R.id.utilityTileNominalVoltageTV);
                    holder.utilityTileImpedanceTV = (TextView) convertView.findViewById(R.id.utilityTileImpedanceTV);
                    holder.utilityTileSCCurrentTV= (TextView) convertView.findViewById(R.id.utilityTileSCCurrentTV);

                    break;

                case  1:
                    convertView = inflater.inflate(R.layout.trafo_tile_layout, null);

                    holder.tileTrafoNameTV = (TextView) convertView.findViewById(R.id.tileTrafoNameTV);
                    holder.tileTrafoIV = (ImageView) convertView.findViewById(R.id.tileTrafoIV);
                    holder.tileTrafoIV.setImageResource(R.drawable.tr_s);
                    holder.trafoTilePowerTV = (TextView) convertView.findViewById(R.id.trafoTilePowerTV);
                    holder.trafoTilePrimaryVoltageTV = (TextView) convertView.findViewById(R.id.trafoTilePrimaryVoltageTV);
                    holder.trafoTileSeconaryVoltageTV = (TextView) convertView.findViewById(R.id.trafoTileSeconaryVoltageTV);
                    holder.trafoTileukTV = (TextView) convertView.findViewById(R.id.trafoTileukTV);
                    holder.trafoTileNominalLossTV = (TextView) convertView.findViewById(R.id.trafoTileNominalLossTV);
                    holder.trafoTileImpedanceTV = (TextView) convertView.findViewById(R.id.trafoTileImpedanceTV);
                    holder.trafoTileSCCurrentTV = (TextView) convertView.findViewById(R.id.trafoTileSCCurrentTV);
                    break;

                case 2:
                    convertView = inflater.inflate(R.layout.line_tile_layout, null);

                    holder.tileLineNameTV = (TextView) convertView.findViewById(R.id.tileLineNameTV);
                    holder.tileLineIV = (ImageView) convertView.findViewById(R.id.tileLineIV);
                    holder.tileLineIV.setImageResource(R.drawable.ln_s);
                    holder.lineTileLenghtTV = (TextView) convertView.findViewById(R.id.lineTileLenghtTV);
                    holder.lineTileSectionTV = (TextView) convertView.findViewById(R.id.lineTileSectionTV);
                    holder.lineTileMaterialTV = (TextView) convertView.findViewById(R.id.lineTileMaterialTV);
                    holder.lineTilePlacementTV = (TextView) convertView.findViewById(R.id.lineTilePlacementTV);
                    holder.lineTileImpedanceTV = (TextView) convertView.findViewById(R.id.lineTileImpedanceTV);
                    holder.lineTileSCCurrentTV = (TextView) convertView.findViewById(R.id.lineTileSCCurrentTV);

                    break;

                case 3:
                    convertView = inflater.inflate(R.layout.load_tile_layout, null);

                    holder.tileLoadNameTV = (TextView) convertView.findViewById(R.id.tileLoadNameTV);
                    holder.tileLoadIV = (ImageView) convertView.findViewById(R.id.tileLoadIV);
                    holder.tileLoadIV.setImageResource(R.drawable.ldg_s);
                    holder.loadTileVoltageTV = (TextView) convertView.findViewById(R.id.loadTileVoltageTV);
                    holder.loadTilePhaseNoTV = (TextView) convertView.findViewById(R.id.loadPhaseNoTV);
                    break;

                case 4:
                    convertView = inflater.inflate(R.layout.motor_tile_layout, null);

                    holder.tileMotorNameTV = (TextView) convertView.findViewById(R.id.tileMotorNameTV);
                    holder.tileMotorIV = (ImageView) convertView.findViewById(R.id.tileMotorIV);
                    holder.tileMotorIV.setImageResource(R.drawable.mt_3f_s);
                    holder.MotorTilePowerTV = (TextView) convertView.findViewById(R.id.MotorTilePowerTV);
                    holder.MotorTileVoltageTV = (TextView) convertView.findViewById(R.id.MotorTileVoltageTV);
                    holder.MotorTileCosfiTV = (TextView) convertView.findViewById(R.id.MotorTileCosfiTV);
                    holder.MotorTileStartupTV = (TextView) convertView.findViewById(R.id.MotorTileStartupTV);
                    holder.MotorTileEfficiencyTV = (TextView) convertView.findViewById(R.id.MotorTileEfficiencyTV);
                    holder.MotorTileImpedanceTV = (TextView) convertView.findViewById(R.id.MotorTileImpedanceTV);
                    holder.MotorTileSCCurrentTV = (TextView) convertView.findViewById(R.id.MotorTileSCCurrentTV);
                    break;

                case 5:
                    convertView = inflater.inflate(R.layout.bus_tile_layout, null);

                    holder.tileBusNameTV = (TextView) convertView.findViewById(R.id.tileBusNameTV);
                    holder.tileBusIV = (ImageView) convertView.findViewById(R.id.tileBusIV);
                    holder.tileBusIV.setImageResource(R.drawable.ps_s);
                    holder.BusTileImpedanceTV = (TextView) convertView.findViewById(R.id.BusTileImpedanceTV);
                    holder.BusTileSCCurrentTV = (TextView) convertView.findViewById(R.id.BusTileSCCurrentTV);
                    holder.BusTileSCImpCurrentTV = (TextView) convertView.findViewById(R.id.BusTileSCImpCurrentTV);
//                    holder.BusTileIkBisDescTV = convertView.findViewById(R.id.IkBisDesc);
//                    holder.BusTileIpDescTV = convertView.findViewById(R.id.IpDesc);
//                    holder.BusTileZkDescTV = convertView.findViewById(R.id.ZkTotalDesc);
                    holder.BusTileIkType = (TextView) convertView.findViewById(R.id.BusTileIkBisTypeTV);

            }
            convertView.setTag(holder);
        }

        else {
            holder = (ViewHolder) convertView.getTag();
        }

        switch (type)
        {
                case 0:
                    holder.tileUtilityNameTV.setText(tile.getUtilityName());
                    holder.utilityTileSCPowerTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getUtilityScPower()))));
                    holder.utilityTileNominalVoltageTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getUtilityNominalVoltage()))));
//                    holder.utilityTileImpedanceTV.setText(String.valueOf(CalcActivity.roundNPlaces(tile.getUtilityImpedance(),7)));
//                    holder.utilityTileImpedanceTV.setText(String.valueOf(CalcActivity.round(tile.getUtilityImpedance())));
//                    holder.utilityTileImpedanceTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.roundSixPaces(tile.getUtilityImpedance()))));
                    holder.utilityTileImpedanceTV.setText(CalcActivity.cutOutZerosAtEndFromString(CalcActivity.noExponentsString(CalcActivity.roundSixPaces(tile.getUtilityImpedance()))));
//                    holder.utilityTileSCCurrentTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getUtilityCurrent()))));

                break;

                case 1:
                    holder.tileTrafoNameTV.setText(tile.getTrafoName());
                    holder.trafoTilePowerTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getTrafoPower()))));
                    holder.trafoTilePrimaryVoltageTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getTrafoPrimaryVoltage()))));
                    holder.trafoTileSeconaryVoltageTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getTrafoSeconaryVoltage()))));
                    holder.trafoTileukTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getTrafoScVoltage()))));
                    holder.trafoTileNominalLossTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getTrafoNominalPowerLoss()))));
                    holder.trafoTileImpedanceTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getTrafoImpedance()))));
//                    holder.trafoTileSCCurrentTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getTrafoCurrent()))));

                break;

                case 2:
                    holder.tileLineNameTV.setText(tile.getLineName());
                    holder.lineTileLenghtTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getLineLenght()))));
                    holder.lineTileSectionTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getLineCrossSection()))));
                    if(tile.getLineMaterial()==null)
                    holder.lineTileMaterialTV.setText("-");
                    else
                    holder.lineTileMaterialTV.setText(String.valueOf(tile.getLineMaterial()));
                    if(tile.getLinePlacement()==null)
                    holder.lineTilePlacementTV.setText("-");
                    else
                    holder.lineTilePlacementTV.setText(String.valueOf(tile.getLinePlacement()));
                    holder.lineTileImpedanceTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getLineImpedance()))));
//                    holder.lineTileSCCurrentTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getLineCurrent()))));

                    break;

                    case 3:
                        holder.tileLoadNameTV.setText(tile.getLoadName());
                        holder.loadTileVoltageTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getLoadVoltage()))));
                        holder.loadTilePhaseNoTV.setText(tile.getLoadPhaseNo());
                        break;

                    case 4:
                        holder.tileMotorNameTV.setText(tile.getMotorName());
                        holder.MotorTilePowerTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getPower()))));
                        holder.MotorTileVoltageTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getVoltage()))));
                        holder.MotorTileCosfiTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getCosFi()))));
                        holder.MotorTileStartupTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getStartUpFactor()))));
                        holder.MotorTileEfficiencyTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getEfficiency()))));
                        holder.MotorTileImpedanceTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getImpedance()))));
                        holder.MotorTileSCCurrentTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getScCurrent()))));
                        break;

                    case 5:
                        holder.tileBusNameTV.setText(tile.getName());
                        holder.BusTileImpedanceTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getScImpedance()))));
//                        System.out.print("!!!!"+CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getScImpedance())))+"!!!");
                        holder.BusTileSCCurrentTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getScCurrent()))));
                        holder.BusTileSCImpCurrentTV.setText(CalcActivity.cutOutZerosAtEndFromString(String.valueOf(CalcActivity.round(tile.getScImpCurrent()))));
                        if(tile.getNoOfPhases() == null)
                        holder.BusTileIkType.setText("Ik?\"[kA]");
                        else if(tile.getNoOfPhases().equals("~3"))
                        holder.BusTileIkType.setText("Ik3\"[kA]");
                        else if(tile.getNoOfPhases().equals("~1"))
                            holder.BusTileIkType.setText("Ik1\"[kA]");
                        break;

        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        if(tileList.get(position).getType() == 0)
            return TYPE_UTILITY;
        if(tileList.get(position).getType() == 1)
            return TYPE_TRAFO;
        if(tileList.get(position).getType() == 2)
            return TYPE_CABLE;
        if (tileList.get(position).getType() == 3)
            return TYPE_LOAD;
        if (tileList.get(position).getType() == 4)
            return TYPE_MOTOR;
        if (tileList.get(position).getType() == 5)
            return TYPE_BUS;
        else
            return -1;
    }

    public class ViewHolder{

        //Utility variables
        TextView tileUtilityNameTV;
        ImageView tileUtilityIV;
        TextView utilityTileSCPowerTV, utilityTileNominalVoltageTV, utilityTileImpedanceTV, xqET, rqET, utilityTileSCCurrentTV;

        //Trafo variables
        TextView tileTrafoNameTV;
        ImageView tileTrafoIV;
        TextView trafoTilePowerTV, trafoTilePrimaryVoltageTV, trafoTileSeconaryVoltageTV, trafoTileukTV, trafoTileNominalLossTV, trafoTileImpedanceTV, trafoTileSCCurrentTV;

        //Line variables
        TextView tileLineNameTV;
        ImageView tileLineIV;
        TextView lineTileLenghtTV, lineTileSectionTV, lineTileMaterialTV, lineTilePlacementTV,lineTileImpedanceTV, lineTileSCCurrentTV;


        //Load variables
        TextView tileLoadNameTV;
        ImageView tileLoadIV;
        TextView loadTileVoltageTV, loadTilePhaseNoTV;

        //Motor variables
        TextView tileMotorNameTV, MotorTilePowerTV, MotorTileVoltageTV, MotorTileCosfiTV, MotorTileStartupTV, MotorTileEfficiencyTV, MotorTileImpedanceTV, MotorTileSCCurrentTV;
        ImageView tileMotorIV;

        //Bus variables
        TextView tileBusNameTV, BusTileImpedanceTV, BusTileSCCurrentTV, BusTileSCImpCurrentTV, BusTileIkBisDescTV, BusTileIpDescTV, BusTileZkDescTV, BusTileIkType;
        ImageView tileBusIV;
    }

}




