public class MyData {
    Action current;
    String Input;
    String Output;
    int Buffer;
    int readBuffer;
    static final String delimiter_size = "%";

    MyData(String in, String out, Action a, int buffer, int readbuffer) {
        Input = in;
        Output = out;
        current = a;
        Buffer = buffer;
        readBuffer = readbuffer;
    }
}