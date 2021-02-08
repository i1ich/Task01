import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            if (args.length != 1) {
                System.err.println("Bad args");
                return;
            }
            Config conf = new Config(args[0]);
            conf.RefreshData();//..

            // Init file handlers
            Handler h1 = new Handler(conf.data.Input, conf.data.readBuffer);
            h1.OpenFile();
            Handler h2 = new Handler(conf.data.Output, conf.data.readBuffer);
            h2.OpenFile();
            Huffman hf = new Huffman();
            lz77 LZ;
            if (conf.data.current == Action.COMPRESS) {
                LZ = new lz77(h1.GetString());
                //Run lz77
                LZ.Compress();
                // Split rez of lz77 to buffers
                String text = /*h1.GetString();*/LZ.Out;//"Huffman coding is a data compression algorithm.";
                int i = 0;
                String Out = "";
                // Converter
                byte[] array = text.getBytes();
                // May be useless
                byte[] array1 = new byte[array.length];

                int j = 0;
                for (byte b : array)
                    array1[j++] = b;
                byte[] Buf;

                // body
                while (array1.length - i >= conf.data.Buffer) {

                    Buf = Arrays.copyOfRange(array1, i, Math.min( array1.length, conf.data.Buffer));
                    /*for (int k = i; k < Math.min( array1.length, conf.data.Buffer); k++) {
                        Buf[k] = array1[i + k];
                    }*/
                    CData cd = hf.buildHuffmanTree(Buf);
                    cd.FillData();
                    int size = cd.Treedata.length() + cd.Textdata.length();
                    Out += String.valueOf(size) + MyData.delimiter_size + cd.Treedata + cd.Textdata;
                    i += conf.data.Buffer;
                }
                // tail
                if (array1.length - i < conf.data.Buffer)
                {
                    Buf = Arrays.copyOfRange(array1, i, array1.length);
                    /*for (int k = i; k < array1.length; k++) {
                        Buf[k] = array1[i + k];
                    }*/
                    // Run Huffman
                    CData cd = hf.buildHuffmanTree(Buf);
                    cd.FillData();
                    int size = cd.Treedata.length() + cd.Textdata.length();
                    Out += String.valueOf(size) + MyData.delimiter_size + cd.Treedata + cd.Textdata;
                }


                    //old CData cd = hf.buildHuffmanTree(text);
                    CData cd = hf.buildHuffmanTree(array);
                cd.FillData();
                // Print to file
                //old Out = cd.Treedata + cd.Textdata;
                h2.SetString(Out);
            } else { // Decompress
                // Load data
                String Input = h1.GetString();
                String in = "";
                while (Input.length() != 0) {
                    String tmp = Input.substring(0, Input.indexOf(MyData.delimiter_size.charAt(0)));
                    int size = Integer.parseInt(tmp);
                    String Buf = Input.substring(tmp.length() + 1, size + tmp.length() + 1);
                    Input = Input.substring(Buf.length() + tmp.length() + 1);
                    CData cd2 = new CData(Buf);
                    byte[] s11 = hf.Decode(cd2);

                    for (byte b : s11)
                        in += (char) b;
                    LZ = new lz77(in);//old s11.toString();
                    LZ.Decompress();
                    h2.SetString(/*s11);*/LZ.Out);
                }
                //old CData cd2 = new CData(h1.GetString());
                // Decode huffman
                //old String s11 = hf.Decode(cd2);
                //old byte[] s11 = hf.Decode(cd2);
                // Converter
                /*//old in = "";
                int j = 0;
                for (byte b : s11)
                    in += (char) b;*/
                //decode lz77
                /*LZ.In = in;//old s11.toString();
                LZ.Decompress();
                h2.SetString(/*s11);* /LZ.Out);*/

            }


        } catch (Exception e) {
            System.err.println("File Not Found");

        }
    }
}