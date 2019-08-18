import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Stack;

class Bracket {
    Bracket(char type, int position) {
        this.type = type;
        this.position = position;
    }

    boolean Match(char c) {
        if (this.type == '[' && c == ']')
            return true;
        if (this.type == '{' && c == '}')
            return true;
        if (this.type == '(' && c == ')')
            return true;
        return false;
    }

    char type;
    int position;
}

class check_brackets {
    static int find_first_unmatched_bracket(String text) {
        Stack<Bracket> opening_brackets_stack = new Stack<Bracket>();
        for (int position = 0; position < text.length(); ++position) {
            char next = text.charAt(position);

            if (next == '(' || next == '[' || next == '{') {
                opening_brackets_stack.add(new Bracket(next, position + 1));
            }

            if (next == ')' || next == ']' || next == '}') {
                if (opening_brackets_stack.isEmpty())
                    return position + 1;
                Bracket last = opening_brackets_stack.pop();
                if (!last.Match(next))
                    return position + 1;
            }
        }
        int position = -1;
        while (!opening_brackets_stack.isEmpty())
            position = opening_brackets_stack.pop().position;
        return position;
    }

    public static void main(String[] args) throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        String text = reader.readLine();

        int position = find_first_unmatched_bracket(text);
        if (position == -1)
            System.out.println("Success");
        else
            System.out.println(position);
    }
}
