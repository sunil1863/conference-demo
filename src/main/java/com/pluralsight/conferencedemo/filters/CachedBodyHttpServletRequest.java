package com.pluralsight.conferencedemo.filters;

import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
    private byte[] cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) {
        super(request);
        try{
            InputStream requestStream = request.getInputStream();
            this.cachedBody = StreamUtils.copyToByteArray(requestStream);
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public ServletInputStream getInputStream(){
        return new CachedBodyServletInputStream(cachedBody);
    }

    @Override
    public BufferedReader getReader() throws IOException{
        ByteArrayInputStream byteArrInputStream = new ByteArrayInputStream(this.cachedBody);
        return new BufferedReader(new InputStreamReader(byteArrInputStream));
    }
}
