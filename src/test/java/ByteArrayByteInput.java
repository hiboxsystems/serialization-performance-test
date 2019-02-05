import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.jboss.marshalling.ByteInput;

/**
 * @author Sebastian Lövdahl
 */
public class ByteArrayByteInput implements ByteInput {

    private final ByteArrayInputStream inputStream;

    public ByteArrayByteInput( ByteArrayInputStream inputStream ) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() {
        return inputStream.read();
    }

    @Override
    public int read( byte[] b ) throws IOException {
        return inputStream.read( b );
    }

    @Override
    public int read( byte[] b, int off, int len ) {
        return inputStream.read( b, off, len );
    }

    @Override
    public int available() {
        return inputStream.available();
    }

    @Override
    public long skip( long n ) {
        return inputStream.available();
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}
