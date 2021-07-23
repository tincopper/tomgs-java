package com.tomgs.core.appachetools.commonspool.demo1;

import java.io.IOException;
import java.io.Reader;

public class RawReaderUtil {
    public RawReaderUtil() {
    }

    /**
     * Dumps the contents of the {@link Reader} to a
     * String, closing the {@link Reader} when done.
     */
    public String readToString(Reader in) throws IOException {
        StringBuffer buf = new StringBuffer();
        try {
            for (int c = in.read(); c != -1; c = in.read()) {
                buf.append((char) c);
            }
            return buf.toString();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                // ignored
            }
        }
    }
}
