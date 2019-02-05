import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;
import org.testng.annotations.Test;

public class JBossSerializationTest {

    @Test( invocationCount = 5 )
    public void testSerializationDeserializationPerformance_serial_unshared() throws IOException {
        MarshallerFactory serialMarshallingFactory = Marshalling.getProvidedMarshallerFactory( "serial" );

        testSerializationDeserializationHelperUnshared(serialMarshallingFactory);
    }

    @Test( invocationCount = 5 )
    public void testSerializationDeserializationPerformance_river_unshared() throws IOException {
        MarshallerFactory serialMarshallingFactory = Marshalling.getProvidedMarshallerFactory( "river" );

        testSerializationDeserializationHelperUnshared(serialMarshallingFactory);
    }

    @Test( invocationCount = 5 )
    public void testSerializationDeserializationPerformance_serial_shared() throws IOException {
        MarshallerFactory serialMarshallingFactory = Marshalling.getProvidedMarshallerFactory( "serial" );

        testSerializationDeserializationHelperShared(serialMarshallingFactory);
    }

    @Test( invocationCount = 5 )
    public void testSerializationDeserializationPerformance_river_shared() throws IOException {
        MarshallerFactory serialMarshallingFactory = Marshalling.getProvidedMarshallerFactory( "river" );

        testSerializationDeserializationHelperShared(serialMarshallingFactory);
    }

    private void testSerializationDeserializationHelperUnshared( MarshallerFactory serialMarshallingFactory ) throws IOException {

        final Serializable testObject = new TestObject();
        try ( Marshaller serialMarshaller = serialMarshallingFactory.createMarshaller( new MarshallingConfiguration() );
              Unmarshaller serialUnmarshaller = serialMarshallingFactory.createUnmarshaller( new MarshallingConfiguration() ) ) {

            for ( int i = 0; i < 1_000_000; i++ ) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                try {
                    writeObjectUnshared( testObject, stream, serialMarshaller );
                    readObjectUnshared( new ByteArrayInputStream( stream.toByteArray() ), serialUnmarshaller );
                }
                catch ( Exception e ) {
                    System.err.println( "ERROR" + e );
                    throw new RuntimeException( e );
                }
            }
        }
    }

    private void writeObjectUnshared( final Object obj, final ByteArrayOutputStream outputStream, Marshaller serialMarshaller ) throws IOException {
        serialMarshaller.start( new ByteArrayByteOutput( outputStream ) );
        serialMarshaller.writeObjectUnshared( obj );
        serialMarshaller.finish();
    }

    private Object readObjectUnshared( final ByteArrayInputStream inputStream, Unmarshaller serialUnmarshaller ) throws IOException, ClassNotFoundException {
        serialUnmarshaller.start( new ByteArrayByteInput( inputStream ) );
        Object result = serialUnmarshaller.readObjectUnshared();
        serialUnmarshaller.finish();

        return result;
    }

    private void testSerializationDeserializationHelperShared( MarshallerFactory serialMarshallingFactory ) throws IOException {

        final Serializable testObject = new TestObject();
        try ( Marshaller serialMarshaller = serialMarshallingFactory.createMarshaller( new MarshallingConfiguration() );
              Unmarshaller serialUnmarshaller = serialMarshallingFactory.createUnmarshaller( new MarshallingConfiguration() ) ) {

            for ( int i = 0; i < 1_000_000; i++ ) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                try {
                    writeObjectShared( testObject, stream, serialMarshaller );
                    readObjectShared( new ByteArrayInputStream( stream.toByteArray() ), serialUnmarshaller );
                }
                catch ( Exception e ) {
                    System.err.println( "ERROR" + e );
                    throw new RuntimeException( e );
                }
            }
        }
    }

    private void writeObjectShared( final Object obj, final ByteArrayOutputStream outputStream, Marshaller serialMarshaller ) throws IOException {
        serialMarshaller.start( new ByteArrayByteOutput( outputStream ) );
        serialMarshaller.writeObjectUnshared( obj );
        serialMarshaller.finish();
    }

    private Object readObjectShared( final ByteArrayInputStream inputStream, Unmarshaller serialUnmarshaller ) throws IOException, ClassNotFoundException {
        serialUnmarshaller.start( new ByteArrayByteInput( inputStream ) );
        Object result = serialUnmarshaller.readObjectUnshared();
        serialUnmarshaller.finish();

        return result;
    }
}
