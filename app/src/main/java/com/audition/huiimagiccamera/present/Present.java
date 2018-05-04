package com.audition.huiimagiccamera.present;

import com.seu.magicfilter.MagicEngine;

/**
 * @author tanzhuohui
 * @date 2018/3/12
 */

public class Present {
    private MagicEngine magicEngine;

    public Present(MagicEngine magicEngine) {
        this.magicEngine = magicEngine;
    }

    public void cameraSwitch(){
        magicEngine.switchCamera();
    }
}
