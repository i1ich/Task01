import java.math.BigInteger;
import java.util.*;

// A Tree node
class Node {
    char ch;
    int freq;
    Node left = null, right = null;

    Node(char ch, int freq) {
        this.ch = ch;
        this.freq = freq;
    }

    public Node(char ch, int freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}

// Compressed Data class
class CData {
    // not binary
    public String NB;
    // map of codes
    public Map<Character, String> codemap;
    // binary data of codemap
    public String Treedata;
    // binary data of coded text
    public String Textdata;
    public static final String delimiter_size = "@";
    // CData constructor(not binary data)
    //** Treedata and Textdata are empty
    //** In: Encoded text, map of pairs (char, binary code): String nb, Map<Character, String> n
    //** Out: none
    CData(String nb, Map<Character, String> n) {
        NB = nb;
        codemap = n;
        Treedata = "";
        Textdata = "";
    }

    // CData constructor(binary data from file)
    //** Treedata and Textdata fills with parts of d string
    //** In: Encoded text from file: String d
    //** Out: none
    CData(String d) {
        NB = "";
        codemap = null;
        Scanner input = new Scanner(d);
        // Format: head@taaaaaaaaaaaaaaaaaaaaaaail
        //                  ....,bytecoded text..^end
        input.useDelimiter(delimiter_size);
        String head = input.next();
        input.useDelimiter("\\Z");
        String tail = input.next();
        int l = Integer.parseInt(head);
        String text = tail.substring(l + 1);
        tail = tail.substring(1, l + 1);
        Treedata = tail;
        Textdata = text;

    }


    // Fill Data fields
    //** Treedata and Textdata fills with map of codes and encoded text
    //** In: none
    //** Out: none
    void FillData() {
        // Print codemap to treedata
        ToBin(codemap);
        // write size at head of data
        Treedata = Treedata.length() + delimiter_size + Treedata;
        // bin to byte[] to String
        byte[] bval = new BigInteger(NB, 2).toByteArray();
        Textdata = Base64.getEncoder().encodeToString(bval);
    }


    // Fill TreeData field
    //** from map data(format: key1,val1,key2...)
    //** In: Map to convert to data: Map<Character, String> hc
    //** Out: none
    private void ToBin(Map<Character, String> hc) {

        for (Map.Entry<Character, String> entry : hc.entrySet()) {
            Treedata += entry.getKey() + "," + entry.getValue() + ",";
        }
        /*if (root == null) {
            return;
        }
        if (root.left == null && root.right == null)
            Treedata += root.ch + "," + root.freq + ", ";
        ToBin(root.left);
        ToBin(root.right);*/
    }
// useless
    /*void FromBin(Node root, String S) {
        int fr;
        char c;

        Scanner input = new Scanner(S);
        input.useDelimiter(",| ");

        if (S.length() == 0) {
            return;
        }
        String s1 = input.next();
        S = S.substring(s1.length());
        if (s1.charAt(0) == '-' && s1.charAt(1) == '1')
            return;
        String s2 = input.next();
        S = S.substring(s2.length());
        root = new Node(s2.charAt(0), Integer.getInteger(s1));

        //FromBin(root.left, S);
        FromBin(root.right, S);
    }
}*/
}

class Huffman {

    Huffman() {

    }

    // traverse the Huffman Tree and store Huffman Codes in a map.
    public void encode(Node root, String str, Map<Character, String> huffmanCode) {
        if (root == null)
            return;

        // found a leaf node
        if (root.left == null && root.right == null) {
            huffmanCode.put(root.ch, str);
        }

        encode(root.left, str + '0', huffmanCode);
        encode(root.right, str + '1', huffmanCode);
    }

    // traverse the Huffman Tree and decode the encoded string
    public int decode(Node root, int index, StringBuilder sb) {
        if (root == null)
            return index;

        // found a leaf node
        if (root.left == null && root.right == null) {
            System.out.print(root.ch);
            return index;
        }

        index++;

        if (sb.charAt(index) == '0')
            index = decode(root.left, index, sb);
        else
            index = decode(root.right, index, sb);

        return index;
    }

    // Builds Huffman Tree and huffmanCode and decode given input text
    //..
    //old public CData buildHuffmanTree(String text) {
    public CData buildHuffmanTree(byte[] array) {
        // count frequency of appearance of each character
        // and store it in a map
        /*//old Map<Character, Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }*/
        int[] freq = new int[256];
        for (byte c : array) {
            freq[c]++;
        }
        // Create a priority queue to store live nodes of Huffman codemap
        // Notice that highest priority item has lowest frequency
        PriorityQueue<Node> pq;
        pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));

        // Create a leaf node for each character and add it
        // to the priority queue.
        /*//old for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }*/
        for (int i = 0; i < 256; i++) {
            if (freq[i] > 0)
                pq.add(new Node((char)i, freq[i]));
        }
        // do till there is more than one node in the queue
        while (pq.size() != 1) {
            // Remove the two nodes of highest priority
            // (lowest frequency) from the queue
            Node left = pq.poll();
            Node right = pq.poll();

            // Create a new internal node with these two nodes as children
            // and with frequency equal to the sum of the two nodes
            // frequencies. Add the new node to the priority queue.
            int sum = left.freq + right.freq;
            pq.add(new Node('\0', sum, left, right));
        }

        // root stores pointer to root of Huffman Tree
        Node root = pq.peek();

        // traverse the Huffman codemap and store the Huffman codes in a map
        Map<Character, String> huffmanCode = new HashMap<>();
        encode(root, "", huffmanCode);

        // print the Huffman codes
        System.out.println("Huffman Codes are : " + huffmanCode);
        //old System.out.println("Original string was : " + text);

        // print encoded string
        StringBuilder sb = new StringBuilder();
        /*//old for (char c : text.toCharArray()) {
            sb.append(huffmanCode.get(c));
        }*/
        for (byte c : array) {
            sb.append(huffmanCode.get((char)c));
        }
        System.out.println("Encoded string is : " + sb);
        // Fill Cdata

        CData cd = new CData(sb.toString(), huffmanCode);
        int index = -1;
        System.out.print("Decoded string is: ");
        while (index < sb.length() - 2) {
            index = decode(root, index, sb);
        }
        return cd;
        // Decoding
        // traverse the Huffman Tree again and this time
        // decode the encoded string
    }


    // Convert Byte to binary function
    //**
    //** In: array of bytes: byte[] bytes
    //** Out: String of bytes
    String toBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    // Convert binary data to Text function
    //** find Symbol code and print symbol
    //** In: Codes, binary decoded data: Map<String, Character> codes, String binary
    //** Out: Result String

    //old String PrintText(Map<String, Character> codes, String binary) {
    Byte[] PrintText(Map<String, Byte> codes, String binary) {
        List<Byte> Ans = new ArrayList<Byte>();
        int l = 0;
        //List<Byte> Ans = new ArrayList<Byte>();
        //oldString Ans = "";
        while (binary.length() != 0) {
            l = 1;
            // Find code
            while (!codes.containsKey(binary.substring(0, l))) {
                l++;
            }
            // Print Symbol
            Ans.add(codes.get(binary.substring(0, l)));
            binary = binary.substring(l);


        }
        Byte[] Ans2 = Ans.toArray(new Byte[Ans.size()]);
        return Ans2;
    }

    // Decode (decompress) Compressed Data
    //**
    //** In: Compressed  data: CData cd
    //** Out: Result(decompressed) String

    //old String Decode(CData cd) {
    byte[] Decode(CData cd) {
        try {
            // for codes of each character
            // store it in a map
            Map<String, Byte> codes = new HashMap<>();
            /*Scanner input = new Scanner(cd.Treedata);
            // Format: head@taaaaaaaaaaaaaaaaaaaaaaail
            //                  ....,bytecoded text..^end
            input.useDelimiter("@");
            String head = input.next();
            String tail = input.next();
            String text = tail.substring(Integer.getInteger(head));
            tail = tail.substring(0, Integer.getInteger(head));*/

            // now we splitted head and tail and bytecoded text
            Scanner input = new Scanner(cd.Treedata);
            //input.useDelimiter(",| ");

            // getting char and their codes
            while (cd.Treedata.length() != 0) {
                char c = cd.Treedata.charAt(0);
                // skip char and ','
                cd.Treedata = cd.Treedata.substring(2);
                // 100,... to 100(int)
                String tmp = cd.Treedata.substring(0, cd.Treedata.indexOf(','));
                cd.Treedata = cd.Treedata.substring(cd.Treedata.indexOf(',') + 1);
                codes.put(tmp, (byte)c);
            }

            // reformat textdata from byte to binary...
            byte[] bval = Base64.getDecoder().decode(cd.Textdata);
            String binary = toBinary(bval);
            String s1 = binary;

            //!!! skip 0 at start
            /*while (s1.charAt(0) == '0') {
                s1 = s1.substring(1);

            }*/
            //old String s2 = PrintText(codes, s1);
            Byte[] s2 = PrintText(codes, s1);
            byte[] ans = new byte[s2.length];
            // Converter
            int j = 0;
            for (Byte b : s2)
                ans[j++] = b.byteValue();
            return ans;

        } catch (Exception e) {
            System.err.println("File Not Found");
        }
        return null;
    }
}
