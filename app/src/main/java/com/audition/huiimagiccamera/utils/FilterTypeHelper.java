package com.audition.huiimagiccamera.utils;

import com.audition.huiimagiccamera.R;
import com.seu.magicfilter.filter.helper.MagicFilterType;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.utils
 * 创建日期： 2018/5/8
 * 描  述：
 */

public class FilterTypeHelper {
    public static final MagicFilterType[] types = new MagicFilterType[]{
            MagicFilterType.NONE,
            MagicFilterType.CRAYON,
            MagicFilterType.SKETCH,
            MagicFilterType.FAIRYTALE,
            MagicFilterType.SUNRISE,
            MagicFilterType.SUNSET,
            MagicFilterType.WHITECAT,
            MagicFilterType.BLACKCAT,
            MagicFilterType.SKINWHITEN,
            MagicFilterType.HEALTHY,
            MagicFilterType.SWEETS,
            MagicFilterType.ROMANCE,
            MagicFilterType.SAKURA,
            MagicFilterType.WARM,
            MagicFilterType.ANTIQUE,
            MagicFilterType.NOSTALGIA,
            MagicFilterType.CALM,
            MagicFilterType.LATTE,
            MagicFilterType.TENDER,
            MagicFilterType.COOL,
            MagicFilterType.EMERALD,
            MagicFilterType.EVERGREEN,
            MagicFilterType.AMARO,
            MagicFilterType.BRANNAN,
            MagicFilterType.BROOKLYN,
            MagicFilterType.EARLYBIRD,
            MagicFilterType.FREUD,
            MagicFilterType.HEFE,
            MagicFilterType.HUDSON,
            MagicFilterType.INKWELL,
            MagicFilterType.KEVIN,
            MagicFilterType.LOMO,
            MagicFilterType.N1977,
            MagicFilterType.NASHVILLE,
            MagicFilterType.PIXAR,
            MagicFilterType.RISE,
            MagicFilterType.SIERRA,
            MagicFilterType.SUTRO,
            MagicFilterType.TOASTER2,
            MagicFilterType.VALENCIA,
            MagicFilterType.WALDEN,
            MagicFilterType.XPROII
    };

    public static int FilterType2Color(MagicFilterType filterType){
        switch (filterType){
            case NONE:
                return R.color.filter_color_grey_light;
            case WHITECAT:
            case BLACKCAT:
            case SUNRISE:
            case SUNSET:
                return R.color.filter_color_brown_light;
            case COOL:
                return R.color.filter_color_blue_dark;
            case EMERALD:
            case EVERGREEN:
                return R.color.filter_color_blue_dark_dark;
            case FAIRYTALE:
                return R.color.filter_color_blue;
            case ROMANCE:
            case SAKURA:
            case WARM:
                return R.color.filter_color_pink;
            case AMARO:
            case BRANNAN:
            case BROOKLYN:
            case EARLYBIRD:
            case FREUD:
            case HEFE:
            case HUDSON:
            case INKWELL:
            case KEVIN:
            case LOMO:
            case N1977:
            case NASHVILLE:
            case PIXAR:
            case RISE:
            case SIERRA:
            case SUTRO:
            case TOASTER2:
            case VALENCIA:
            case WALDEN:
            case XPROII:
                return R.color.filter_color_brown_dark;
            case ANTIQUE:
            case NOSTALGIA:
                return R.color.filter_color_green_dark;
            case SKINWHITEN:
            case HEALTHY:
                return R.color.filter_color_red;
            case SWEETS:
                return R.color.filter_color_red_dark;
            case CALM:
            case LATTE:
            case TENDER:
                return R.color.filter_color_brown;
            default:
                return R.color.filter_color_grey_light;
        }
    }

    public static int FilterType2Thumb(MagicFilterType filterType){
        switch (filterType) {
            case NONE:
                return R.mipmap.filter_thumb_original;
            case WHITECAT:
                return R.mipmap.filter_thumb_whitecat;
            case BLACKCAT:
                return R.mipmap.filter_thumb_blackcat;
            case ROMANCE:
                return R.mipmap.filter_thumb_romance;
            case SAKURA:
                return R.mipmap.filter_thumb_sakura;
            case AMARO:
                return R.mipmap.filter_thumb_amoro;
            case BRANNAN:
                return R.mipmap.filter_thumb_brannan;
            case BROOKLYN:
                return R.mipmap.filter_thumb_brooklyn;
            case EARLYBIRD:
                return R.mipmap.filter_thumb_earlybird;
            case FREUD:
                return R.mipmap.filter_thumb_freud;
            case HEFE:
                return R.mipmap.filter_thumb_hefe;
            case HUDSON:
                return R.mipmap.filter_thumb_hudson;
            case INKWELL:
                return R.mipmap.filter_thumb_inkwell;
            case KEVIN:
                return R.mipmap.filter_thumb_kevin;
            case LOMO:
                return R.mipmap.filter_thumb_lomo;
            case N1977:
                return R.mipmap.filter_thumb_1977;
            case NASHVILLE:
                return R.mipmap.filter_thumb_nashville;
            case PIXAR:
                return R.mipmap.filter_thumb_piaxr;
            case RISE:
                return R.mipmap.filter_thumb_rise;
            case SIERRA:
                return R.mipmap.filter_thumb_sierra;
            case SUTRO:
                return R.mipmap.filter_thumb_sutro;
            case TOASTER2:
                return R.mipmap.filter_thumb_toastero;
            case VALENCIA:
                return R.mipmap.filter_thumb_valencia;
            case WALDEN:
                return R.mipmap.filter_thumb_walden;
            case XPROII:
                return R.mipmap.filter_thumb_xpro;
            case ANTIQUE:
                return R.mipmap.filter_thumb_antique;
            case SKINWHITEN:
                return R.mipmap.filter_thumb_beauty;
            case CALM:
                return R.mipmap.filter_thumb_calm;
            case COOL:
                return R.mipmap.filter_thumb_cool;
            case EMERALD:
                return R.mipmap.filter_thumb_emerald;
            case EVERGREEN:
                return R.mipmap.filter_thumb_evergreen;
            case FAIRYTALE:
                return R.mipmap.filter_thumb_fairytale;
            case HEALTHY:
                return R.mipmap.filter_thumb_healthy;
            case NOSTALGIA:
                return R.mipmap.filter_thumb_nostalgia;
            case TENDER:
                return R.mipmap.filter_thumb_tender;
            case SWEETS:
                return R.mipmap.filter_thumb_sweets;
            case LATTE:
                return R.mipmap.filter_thumb_latte;
            case WARM:
                return R.mipmap.filter_thumb_warm;
            case SUNRISE:
                return R.mipmap.filter_thumb_sunrise;
            case SUNSET:
                return R.mipmap.filter_thumb_sunset;
            case CRAYON:
                return R.mipmap.filter_thumb_crayon;
            case SKETCH:
                return R.mipmap.filter_thumb_sketch;
            default:
                return R.mipmap.filter_thumb_original;
        }
    }

    public static int FilterType2Name(MagicFilterType filterType){
        switch (filterType) {
            case NONE:
                return R.string.filter_none;
            case WHITECAT:
                return R.string.filter_whitecat;
            case BLACKCAT:
                return R.string.filter_blackcat;
            case ROMANCE:
                return R.string.filter_romance;
            case SAKURA:
                return R.string.filter_sakura;
            case AMARO:
                return R.string.filter_amaro;
            case BRANNAN:
                return R.string.filter_brannan;
            case BROOKLYN:
                return R.string.filter_brooklyn;
            case EARLYBIRD:
                return R.string.filter_Earlybird;
            case FREUD:
                return R.string.filter_freud;
            case HEFE:
                return R.string.filter_hefe;
            case HUDSON:
                return R.string.filter_hudson;
            case INKWELL:
                return R.string.filter_inkwell;
            case KEVIN:
                return R.string.filter_kevin;
            case LOMO:
                return R.string.filter_lomo;
            case N1977:
                return R.string.filter_n1977;
            case NASHVILLE:
                return R.string.filter_nashville;
            case PIXAR:
                return R.string.filter_pixar;
            case RISE:
                return R.string.filter_rise;
            case SIERRA:
                return R.string.filter_sierra;
            case SUTRO:
                return R.string.filter_sutro;
            case TOASTER2:
                return R.string.filter_toastero;
            case VALENCIA:
                return R.string.filter_valencia;
            case WALDEN:
                return R.string.filter_walden;
            case XPROII:
                return R.string.filter_xproii;
            case ANTIQUE:
                return R.string.filter_antique;
            case CALM:
                return R.string.filter_calm;
            case COOL:
                return R.string.filter_cool;
            case EMERALD:
                return R.string.filter_emerald;
            case EVERGREEN:
                return R.string.filter_evergreen;
            case FAIRYTALE:
                return R.string.filter_fairytale;
            case HEALTHY:
                return R.string.filter_healthy;
            case NOSTALGIA:
                return R.string.filter_nostalgia;
            case TENDER:
                return R.string.filter_tender;
            case SWEETS:
                return R.string.filter_sweets;
            case LATTE:
                return R.string.filter_latte;
            case WARM:
                return R.string.filter_warm;
            case SUNRISE:
                return R.string.filter_sunrise;
            case SUNSET:
                return R.string.filter_sunset;
            case SKINWHITEN:
                return R.string.filter_skinwhiten;
            case CRAYON:
                return R.string.filter_crayon;
            case SKETCH:
                return R.string.filter_sketch;
            default:
                return R.string.filter_none;
        }
    }
    
    
}