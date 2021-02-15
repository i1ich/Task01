import javax.swing.text.Style;
import javax.xml.crypto.Data;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

enum Action {
    DEFAULT,
    COMPRESS,
    DECOMPRESS
}

// Work with config file class
public class Config {
    private

    // Config filename
            String FileName;
    // Data from config
    public MyData data;

    // special words in config file
    // action: "Compress", "Decompress"
    public enum Grammar {
        Compress("Compress"),
        Decompress("Decompress"),
        action("action"),
        from("from"),
        to("to"),
        buffer("buffer"),
        read_buffer("read_buffer");
        final public String word;

        Grammar(String word) {
            this.word = word;
        }
    }

    public static final String[] actions = {"Compress", "Decompress"};
    public static final String[] style = {"action:", "from:", "to:", "buffer:"};

    // enum to unify config read
    enum Style {
        ACTION,
        FROM,
        TO,
        BUFFER,
        ERROR
    }

    // Config class constructor
    //** Get ready to load(refresh) data
    //** In:
    //**** Config filename: String fileName
    //** Out: none
    public Config(String fileName) {
        this.FileName = fileName;
        this.data = new MyData("", "", Action.DEFAULT, 1000, 50);
    }


    public Integer getInteger(HashMap<String, String> parameters, String token)
    {
        if (parameters == null || token == null){
            System.err.println("Invalid arguments");
            return null;
        }
        try{
            String stringValue = parameters.get(token);
            if (stringValue == null){
                System.err.println("Invalid data");
            }
            Integer value = Integer.parseInt(stringValue);
            return value;
        }
        catch (NumberFormatException ex){
            System.err.println("Invalid cfg data");
        }
        return null;
    }
    public String getString(HashMap<String, String> parameters, String token) {
        if (parameters == null || token == null){
            System.err.println("Invalid arguments");
            return null;
        }
        try{
            String value = parameters.get(token);
            if (value == null){
                System.err.println("Invalid data");
            }
            return value;
        }
        catch (NumberFormatException ex){
            System.err.println("Invalid cfg data");
        }
        return null;

    }

    public HashMap<String, String> GetParams(String cfgPath, String separator/*, String endLine*/) {

        if (cfgPath == null || separator == null /*|| endLine == null*/) {
            System.err.println("Invalid arguments");
            return null;
        }
        HashMap<String, String> validParams = new HashMap<>();
        try {
            File cfg = new File(cfgPath);
            FileReader configReader = new FileReader(cfg);
            BufferedReader bufferedReader = new BufferedReader(configReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.matches(
                        "[ ]*[a-zA-Z_][a-zA-Z_0-9]*[ ]*" +
                                separator +
                                "[ ]*[\\\\a-zA-Z_0-9.-]+[ ]*" /*+
                                endLine*/)) {
                    String cleanLine = line.replace(" ", "")/*.replace(endLine, "")*/;
                    int eqIndex = cleanLine.indexOf(separator);
                    validParams.put(
                            cleanLine.substring(0, eqIndex),
                            cleanLine.substring(eqIndex + 1, cleanLine.length()));
                }
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            configReader.close();
        } catch (FileNotFoundException ex) {
            System.err.println("No config file");
        } catch (IOException ex) {
            System.err.println("Invalid data");
        }
        return validParams;
    }

    // Refresh data function
    //** Use this.FileName to load config data and set this.data field
    //** In: none
    //** Out: none
    public void RefreshData() throws Exception {
        // Open file from this.FileName


            HashMap<String, String> params = GetParams(this.FileName, ":");
            String action = getString(params, Grammar.action.word);
            if(action.equals(Grammar.Compress.word))
                this.data.current = Action.COMPRESS;
            else if(action.equals(Grammar.Decompress.word))
                this.data.current = Action.DECOMPRESS;
            this.data.Input = getString(params, Grammar.from.word);
            this.data.Output = getString(params, Grammar.to.word);
            this.data.Buffer = getInteger(params, Grammar.buffer.word);
            this.data.readBuffer = getInteger(params, Grammar.read_buffer.word);


    }

    //public void LoadData();

}
