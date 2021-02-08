import java.util.Scanner;

public class lz77 {

    private

            // lz77 triple class
    class Triple {
        int offset;
        int length;
        char C;

        // Handle constructor
        //**
        //** In: offset, length, symbol: int o, int l, char c
        //** Out: none
        public Triple(int o, int l, char c) {
            offset = o;
            length = l;
            C = c;
        }

        // Handle constructor(by string)
        //** format is (ooo,lll,C)
        //** In: Formatted string: String S
        //** Out: none
        public Triple(String S) {
            // check (
            if (S.charAt(0) != '(')
                S = "(" + S;
            /*Scanner input = new Scanner(S);
            input.useDelimiter(",| |(|)");*/
            String tmp = S.substring(1, S.indexOf(','));
            S = S.substring(S.indexOf(',') + 1);
            //String tmp = input.next();
            //tmp = input.next();
            offset = Integer.parseInt(tmp);
            tmp = S.substring(0, S.indexOf(','));
            S = S.substring(S.indexOf(',') + 1);
            length = Integer.parseInt(tmp);
            tmp = S.substring(0, 1);
            S = S.substring(2);
            C = tmp.charAt(0);
        }

        // Format triple
        //** format is (ooo,lll,C)
        //** In: none
        //** Out: Formatted string
        String Format() {
            String S = "(" + offset + "," + length + "," + C + ")";
            return S;
        }

    }

    String In;
    public String Out;

    lz77(String in) {
        In = in;
        Out = "";
    }

    //Get offset of substring in string
    //** offset = len - index - 1
    //** In: input string, scanned string, len of substring: String scanned, String s, int l
    //** Out: offset if found? -1 otherwise
    int GetOffset(String scanned, String s, int l) {
        int o = -1;

        //for (int i = 0; i < scanned.length() - l + 1; i++) {
        if (scanned.contains(s.substring(0, 0 + l))) {
            o = scanned.length() - scanned.lastIndexOf(s.substring(0, 0 + l));
            //    break;
        }
        //}
        return o;
    }


    // Get next triplet(substring)
    //** find max substring sub: s = sub + C + tail, sub is in scanned. Convert it to triplet.
    //** In: input string, scanned string: String scanned, String s
    //** Out: none
    Triple GetNext(String scanned, String s) {
        int o = -1, l = 1;
        Triple out = new Triple(0, 0, s.charAt(0));
        if (GetOffset(scanned, s, l) == -1)
            return out;
        while (true) {
            o = GetOffset(scanned, s, l);
            if (GetOffset(scanned, s, l + 1) == -1)
                break;
            l++;
        }
        out.length = l;
        out.offset = o;
        out.C = s.charAt(l);
        return out;
    }

    // lz77 compress function
    //** find max substring sub: s = sub + C + tail, sub is in scanned. Convert it to triplet. reformat strings
    //** In: none
    //** Out: none
    void Compress() {
        try {
            String s = In;
            String scanned = "";
            Triple t;

            // while S != 0
            while (s.length() != 0) {
                // find max substring sub: s = sub + C + tail, sub is in scanned
                t = GetNext(scanned, s);
                // associate sub to Triple (o, l, C)
                if (t.length == 0) {
                    scanned += t.C;
                    s = s.substring(1);
                } else {
                    scanned += s.substring(0, t.length) + t.C;
                    s = s.substring(t.length + 1);
                }
                // continue
                Out += t.Format();
            }


        } catch (Exception e) {
            System.err.println("bug in z77compress");

        }
    }

    // lz77 decompress function
    //** fill result with triple`s, offsets points on positions in result
    //** In: none
    //** Out: none
    void Decompress() {
        try {
            String s = In;
            if(s.charAt(0) != '(')
                s = "(" +s;
            String scanned = "";
            Triple t;
            while (s.length() != 0) {

                t = new Triple(s);
                s = s.substring(t.Format().length());
                while (t.length == 0 && s.length() != 0) {
                    scanned += t.C;
                    t = new Triple(s);
                    s = s.substring(t.Format().length());
                }
                scanned += scanned.substring(scanned.length() - t.offset, scanned.length() - t.offset + t.length);
                scanned += t.C;
                Out = scanned;
            }
            Out = scanned;
        } catch (Exception e) {
            System.err.println("bug in z77decompress");

        }
    }
}
