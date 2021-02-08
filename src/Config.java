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

    // Get Style from string
    //** 4 example "action:" --> ACTION; "abracadabra"--> ERROR
    //** In: none
    //** Out: none
    public Style GetStyle(String S) {
        switch (S) {
            case ("action:"):
                return Style.ACTION;
            case ("from:"):
                return Style.FROM;
            case ("to:"):
                return Style.TO;
            case ("buffer:"):
                return Style.BUFFER;
            default:
                return Style.ERROR;
        }

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
            //int i = 0;
            /*File file = new File(this.FileName);
            Scanner input = new Scanner(file);
            input.useDelimiter(" |\\n");
            String s = input.next();
            //!!
            while (true) { // we have break when we read all file, exception otherwise

                while (s.equals("\n"))
                    s = input.next();

                Style style = GetStyle(s);
                // only 3 steps
                // const exp required in switch
                if (style == Style.ACTION) {
                    s = input.next();
                    if (s.equals(actions[1]))
                        this.data.current = Action.DECOMPRESS;
                    else
                        this.data.current = Action.COMPRESS;
                } else if (style == Style.FROM) {
                    s = input.next();
                    this.data.Input = s;
                } else if (style == Style.TO) {
                    s = input.next();
                    this.data.Output = s;
                } else if (style == Style.BUFFER) {
                    s = input.next();
                    this.data.Buffer = Integer.valueOf(s);
                } else {
                    System.err.println("Wrong algorithm");
                }
                /*switch (style) {
                    case (Style.ACTION): // type of action decompress/compress
                        s = input.next();
                        if (s.equals(actions[0]))
                            this.data.current = Action.DECOMPRESS;
                        else
                            this.data.current = Action.COMPRESS;
                        break;
                    case (1): //input file
                        s = input.next();
                        this.data.Input = s;
                        break;
                    case (2): //output file
                        s = input.next();
                        this.data.Output = s;
                        break;
                    default: // someone changed i
                        System.err.println("Wrong algorithm");
                        break;
                }
                if (input.hasNext())
                    s = input.next();
                else
                    break;
            }*/

    }

    //public void LoadData();

}
