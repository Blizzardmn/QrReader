package com.reader.multiple.mult.service;

public class CleanStub extends IStub {

    public final CleanerService cleanerService;

    public CleanStub(CleanerService cleanerService) {
        this.cleanerService = cleanerService;
    }

    @Override
    public String getName() {
        return null;
    }
}
