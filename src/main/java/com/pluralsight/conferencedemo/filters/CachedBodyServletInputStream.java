package com.pluralsight.conferencedemo.filters;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CachedBodyServletInputStream extends ServletInputStream {

    private InputStream cachedStream;

    public CachedBodyServletInputStream(byte []cachedBody){
        cachedStream = new ByteArrayInputStream(cachedBody);
    }

    @Override
    public int read() throws IOException {
        return cachedStream.read();
    }

    @Override
    public boolean isFinished() {
        try{
            return cachedStream.available() == 0;
        }
        catch (IOException ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isReady(){
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener){

    }
}
