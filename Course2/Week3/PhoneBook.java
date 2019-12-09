import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class PhoneBook {
    public static void main(String[] args) {
        runSolution();
        // testSolution();
    }

    static void runSolution() {
        FastScanner in = new FastScanner();
        PhoneTable table = new DirectAddressTable();
        int queryCount = in.nextInt();
        for (int i = 0; i < queryCount; ++i) {
            String result = processQuery(readQuery(in), table);
            if (result != null)
                writeResponse(result);
        }
    }

    static void testSolution() {
        runTest(new Query[] { new Query("add", "police", 911), new Query("add", "Mom", 76213),
                new Query("add", "Bob", 17239), new Query("find", 76213), new Query("find", 910),
                new Query("find", 911), new Query("del", 910), new Query("del", 911), new Query("find", 911),
                new Query("find", 76213), new Query("add", "daddy", 76213), new Query("find", 76213) },
                new String[] { "Mom", "not found", "police", "not found", "Mom", "daddy" });

        runTest(new Query[] { new Query("find", 3839442), new Query("add", "me", 123456), new Query("add", "granny", 0),
                new Query("find", 0), new Query("find", 123456), new Query("del", 0), new Query("del", 0),
                new Query("find", 0) }, new String[] { "not found", "granny", "me", "not found" });
    }

    static void runTest(Query[] input, String[] expected) {
        ArrayList<String> actual = new ArrayList<String>();
        PhoneTable table = new DirectAddressTable();
        for (Query q : input) {
            String result = processQuery(q, table);
            if (result != null)
                actual.add(result);
        }
        String actualString = Arrays.toString(actual.toArray());
        String expectedString = Arrays.toString(expected);

        if (!actualString.equals(expectedString))
            System.out.println("Unexpected result - expected: " + expectedString + ", but got: " + actualString);
    }

    static Query readQuery(FastScanner in) {
        String type = in.next();
        int number = in.nextInt();
        if (type.equals("add")) {
            String name = in.next();
            return new Query(type, name, number);
        } else {
            return new Query(type, number);
        }
    }

    static void writeResponse(String response) {
        System.out.println(response);
    }

    static String processQuery(Query query, PhoneTable table) {
        if (query.type.equals("add")) {
            table.add(query.number, query.name);
        } else if (query.type.equals("del")) {
            table.del(query.number);
        } else {
            return table.find(query.number);
        }
        return null;
    }

    static class NaiveListTable implements PhoneTable {
        List<Contact> contacts;

        NaiveListTable() {
            contacts = new ArrayList<Contact>();
        }

        @Override
        public void add(int number, String name) {
            boolean wasFound = false;
            // if we already have contact with such number,
            // we should rewrite contact's name
            for (Contact contact : contacts)
                if (contact.number == number) {
                    contact.name = name;
                    wasFound = true;
                    break;
                }
            // otherwise, just add it
            if (!wasFound)
                contacts.add(new Contact(name, number));
        }

        @Override
        public void del(int number) {
            for (Iterator<Contact> it = contacts.iterator(); it.hasNext();)
                if (it.next().number == number) {
                    it.remove();
                    break;
                }
        }

        @Override
        public String find(int number) {
            String response = "not found";
            for (Contact contact : contacts)
                if (contact.number == number) {
                    response = contact.name;
                    break;
                }
            return response;
        }
    }

    static class DirectAddressTable implements PhoneTable {
        String[] contacts;

        DirectAddressTable(){
            contacts = new String[10000000];
        }

        @Override
        public void add(int number, String name) {
            contacts[number] = name;
        }

        @Override
        public void del(int number) {
            contacts[number] = null;
        }

        @Override
        public String find(int number) {
            String name = contacts[number];
            return name == null ? "not found" : name;
        }

    }

    interface PhoneTable {
        void add(int number, String name);

        void del(int number);

        String find(int number);
    }

    static class Contact {
        String name;
        int number;

        public Contact(String name, int number) {
            this.name = name;
            this.number = number;
        }
    }

    static class Query {
        String type;
        String name;
        int number;

        public Query(String type, String name, int number) {
            this.type = type;
            this.name = name;
            this.number = number;
        }

        public Query(String type, int number) {
            this.type = type;
            this.number = number;
        }
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}
