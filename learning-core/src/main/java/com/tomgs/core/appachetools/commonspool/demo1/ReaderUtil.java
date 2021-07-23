package com.tomgs.core.appachetools.commonspool.demo1;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * 将RawReaderUtil中的StringBuffer改成池的方式获取
 *
 * @author tomgs
 */
public class ReaderUtil {

    private final ObjectPool<StringBuffer> pool;

    public ReaderUtil(final ObjectPool<StringBuffer> pool) {
        this.pool = pool;
    }

    public String readToString(Reader in) throws IOException {
        StringBuffer stringBuffer = null;
        try {
            stringBuffer = pool.borrowObject();
            for (int c = in.read(); c != -1; c = in.read()) {
                stringBuffer.append((char) c);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Unable to borrow buffer from pool" + e.toString());
        } finally {
            in.close();
            try {
                pool.returnObject(stringBuffer);
            } catch (Exception e) {
                // ignored
            }
        }

    }

    public static void main(String[] args) throws IOException {
        ReaderUtil readerUtil = new ReaderUtil(new GenericObjectPool<>(new StringBufferFactory()));
        StringReader stringReader = new StringReader("test string reader");
        String string = readerUtil.readToString(stringReader);
        System.out.println(string);
    }

}
