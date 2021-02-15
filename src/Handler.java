import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

// Work with file handle
public class Handler {
    private String FileName;
    private int buffSize;
    FileInputStream inputStream;
    int curSize;
    int fileSize;

    // Handle constructor
    //**
    //** In: Filename: String S
    //** Out: none
    Handler(String S, int buffSize) {
        this.curSize = 0;
        this.FileName = S;
        this.fileSize = 0;
        this.buffSize = buffSize;

    }

    public boolean OpenFile() {
        if (fileSize != 0)
            return false;
        try {
            File file = new File(FileName);
            if (!file.exists()) {
                throw new FileNotFoundException("incorrect path:" + FileName);
            }
            fileSize = (int) file.length();
            inputStream = new FileInputStream(file);
            return true;
        } catch (FileNotFoundException ex) {
            System.err.println("No file" + ex.getMessage());
        }
        return false;
    }

    // Get buffer(size = buffSize) from file
    //**
    //** In: none
    //** Out: String from file
    byte[] GetBuffer() {
        if (inputStream == null)
            return null;
        try {
            byte[] data;
            if (buffSize < fileSize - curSize) {
                data = new byte[buffSize];
                inputStream.read(data);
                curSize += buffSize;
            } else {
                data = new byte[fileSize - curSize];
                inputStream.read(data);
                inputStream.close();
                inputStream = null;
                fileSize = 0;
            }
            return data;
        } catch (IOException ex) {
            System.err.println("error while file reading" + ex.getMessage());


        }
        return null;
    }
    // Get string from file
    //**
    //** In: none
    //** Out: String from file
    String GetString() {
        String Out = "";
        while (fileSize != 0)
        {
            byte[] data = GetBuffer();
            Out += new String(data, StandardCharsets.UTF_8);
        }
        /**/
        return Out;
    }

    // Print string to file
    //**
    //** In: String s
    //** Out: none
    void SetString(String s) {
        try {
            File file = new File(this.FileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(FileName));

            writer.write(s);
            writer.close();

        } catch (Exception e) {
            System.err.println("File Not Found");
        }

    }
}
