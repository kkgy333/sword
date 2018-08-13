package com.github.kkgy333.sword.fabric.sdk.Manager;


import com.github.kkgy333.sword.fabric.sdk.entity.SampleOrg;

public class FabricManager {

    private SampleOrg org;
    public SampleOrg getOrg(){
        return org;
    }

    FabricManager(SampleOrg org) {
        this.org = org;
    }




}
