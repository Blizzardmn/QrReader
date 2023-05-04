package com.reader.multiple.mvp.service;

public class BoosterStub extends IStub {
    public final BoosterService boosterService;

    public BoosterStub(BoosterService boosterService) {
        this.boosterService = boosterService;
    }

    @Override
    public String getName() {
        return null;
    }
}