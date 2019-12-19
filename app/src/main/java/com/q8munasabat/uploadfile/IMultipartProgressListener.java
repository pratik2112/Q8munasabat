package com.q8munasabat.uploadfile;

public interface IMultipartProgressListener {
    void transferred(long transferred, int progress);

    void setmax(long size);
}