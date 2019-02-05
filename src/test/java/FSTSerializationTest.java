import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;
import org.testng.annotations.Test;

public class FSTSerializationTest {

    private final FSTConfiguration fstConf = FSTConfiguration.createDefaultConfiguration();

    public static void main( String[] args ) throws ClassNotFoundException {
        new FSTSerializationTest().testSerializationDeserializationPerformance();
    }

    @Test( invocationCount = 5 )
    public void testSerializationDeserializationPerformance() throws ClassNotFoundException {
        final Serializable testObject = new TestObject();

        // Pre-register classes to work around https://github.com/RuedigerMoeller/fast-serialization/issues/235
        fstConf.registerClass( DateTime.class );
        fstConf.registerClass( LocalDate.class );
        fstConf.registerClass( Class.forName( "org.joda.time.chrono.ISOChronology$Stub" ) );
        fstConf.registerClass( Class.forName( "org.joda.time.DateTimeZone$Stub" ) );
        fstConf.registerClass( Class.forName( "TestObject" ) );

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
        try ( FSTObjectInput in = new FSTObjectInput( inputStream, fstConf ) ) {
            return in.readObject();
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

    private void writeObject( final Object obj, final OutputStream outputStream ) throws IOException {
        try ( FSTObjectOutput out = new FSTObjectOutput( outputStream, fstConf ) ) {
            out.writeObject( obj );
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }
}