package com.mazzone.isere.transport.util;

import com.mazzone.isere.transport.R;

public class MapItemUtil {

    public static int getIconResourceFromOperatorId(int operatorId) {
        switch (operatorId) {
            case 12: // Transisère
                return R.drawable.pin_operator_transisere;
            case 15: // TAG
                return R.drawable.pin_operator_tag;
            case 16: // Airport Grenoble Isère
                return R.drawable.pin_operator_age;
            case 17: // CAPI
                return R.drawable.pin_operator_capi;
            case 18: // Pays Viennois
                return R.drawable.pin_operator_pv;
            case 19: // Voironnais
                return R.drawable.pin_operator_voironnais;
            case 20: // SNCF
                return R.drawable.pin_operator_sncf;
            case 21: // Navette Airport Grenoble Isère
            case 22: // Navette Airport Saint Exupery
                return R.drawable.pin_operator_shuttle_airport;
            case 23: // VFD Aerocar
                return R.drawable.pin_operator_vfd_aerocar;
            case 24: // Transport du Grésivaudan
                return R.drawable.pin_operator_gresivaudan;
            case 25: // Transaltitude
                return R.drawable.pin_operator_transaltitude;
            default:
                return R.drawable.pin;
        }
    }

    public static int getColorResourceFromOperatorId(int operatorId) {
        switch (operatorId) {
            case 12: // Transisère
                return R.color.transisere;
            case 15: // TAG
                return R.color.tag;
            case 16: // Airport Grenoble Isère
                return R.color.age;
            case 17: // CAPI
                return R.color.capi;
            case 18: // Pays Viennois
                return R.color.pv;
            case 19: // Voironnais
                return R.color.voironnais;
            case 20: // SNCF
                return R.color.sncf;
            case 21: // Navette Airport Grenoble Isère
            case 22: // Navette Airport Saint Exupery
                return R.color.shuttle_airport;
            case 23: // VFD Aerocar
                return R.color.vfd_aerocar;
            case 24: // Transport du Grésivaudan
                return R.color.gresivaudan;
            case 25: // Transaltitude
                return R.color.transaltitude;
            default:
                return R.color.defaultOperator;
        }
    }
}
