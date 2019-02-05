import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.testng.annotations.Test;

import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

public class NettySerializationTest {

    @Test( invocationCount = 5 )
    public void testSerializationDeserializationPerformance() {
        final Serializable testObject = new TestObject();

        for ( int i = 0; i < 1_000_000; i++ ) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            try {
                writeObject( testObject, stream );
                readObject( new ByteArrayInputStream( stream.toByteArray() ) );
            }
            catch ( Exception e ) {
                System.err.println( "ERROR" + e );
                throw new RuntimeException( e );
            }
        }
    }

    private Object readObject( final InputStream inputStream ) throws IOException {
        try ( ObjectDecoderInputStream in = new ObjectDecoderInputStream( inputStream ) ) {
            return in.readObject();
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

    private void writeObject( final Object obj, final OutputStream outputStream ) throws IOException {
        try ( ObjectEncoderOutputStream out = new ObjectEncoderOutputStream( outputStream ) ) {
            out.writeObject( obj );
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }
}
