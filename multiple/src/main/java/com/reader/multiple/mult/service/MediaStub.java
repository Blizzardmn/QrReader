package com.reader.multiple.mult.service;

public class MediaStub extends IStub {

    public final MediaService mediaService;

    public MediaStub(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @Override
    public String getName() {
        return null;
    }
}
