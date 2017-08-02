package coursera.datastructure.week1;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Stack;

class Bracket {
    Bracket(char type, int position) {
        this.type = type;
        this.position = position;
    }

    boolean match(char c) {
        return (this.type == '[' && c == ']')
        || (this.type == '{' && c == '}')
        || (this.type == '(' && c == ')');
    }

    char type;
    int position;
}

class CheckBrackets {
    public static void main(String[] args) throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        String text = reader.readLine();

        Stack<Bracket> openingBracketsStack = new Stack<Bracket>();
        for (int position = 0; position < text.length(); ++position) {
            char next = text.charAt(position);

            if (next == '(' || next == '[' || next == '{') {
            	openingBracketsStack.add(new Bracket(next, position));
            }

            if (next == ')' || next == ']' || next == '}') {
            	if (openingBracketsStack.isEmpty()) {
            		System.out.println(position +1);
            		return;
            	}
            	Bracket bracket = openingBracketsStack.pop();
            	if (!bracket.match(next)) {
            		System.out.println(position +1);
            		return;
            	}
            }
        }
        
        if (openingBracketsStack.isEmpty()) {
        	System.out.println("Success");
        } else {
        	Bracket bracket = null;
        	while (!openingBracketsStack.isEmpty()) {
        		bracket = openingBracketsStack.pop();
        	}
        	System.out.println(bracket.position +1);
        }
    }
}
