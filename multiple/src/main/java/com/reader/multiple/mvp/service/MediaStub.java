package com.reader.multiple.mvp.service;

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
