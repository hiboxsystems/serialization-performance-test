import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jboss.marshalling.ByteOutput;

/**
 * @author Sebastian Lövdahl
 */
public class ByteArrayByteOutput implements ByteOutput {

    private final ByteArrayOutputStream outputStream;

    public ByteArrayByteOutput( ByteArrayOutputStream outputStream ) {
        this.outputStream = outputStream;
    }

    @Override
    public void write( int b ) {
        outputStream.write( b );
    }

    @Override
    public void write( byte[] b ) throws IOException {
        outputStream.write( b );
    }

    @Override
    public void write( byte[] b, int off, int len ) {
        outputStream.write( b, off, len );
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }
}
