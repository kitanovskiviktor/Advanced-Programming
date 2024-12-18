import java.util.LinkedList;
import java.util.Scanner;


class SuperString {

    private LinkedList<String> strings;


    public SuperString(){
        this.strings = new LinkedList<>();
    }

    public void append(String s){
        strings.addLast(s);
    }

    public void insert(String s){
        strings.addFirst(s);
    }

    public boolean contains(String s) {
        StringBuilder fullString = new StringBuilder();
        for (String part : strings) {
            fullString.append(part);
        }
        return fullString.toString().contains(s);
    }

    @Override
    public String toString() {
        String sb = "";
        for(int i=0; i<strings.size(); i++){
            sb+=strings.get(i);
        }
        return sb;
    }

    public void removeLast(int k) {
        for (int i = 0; i < k && !strings.isEmpty(); i++) {
            strings.removeLast();
        }
    }

    public void reverse() {
        LinkedList<String> reversedList = new LinkedList<>();
        for (String part : strings) {
            reversedList.addFirst(new StringBuilder(part).reverse().toString());
        }
        strings = reversedList;
    }
}



public class SuperStringTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (  k == 0 ) {
            SuperString s = new SuperString();
            while ( true ) {
                int command = jin.nextInt();
                if ( command == 0 ) {//append(String s)
                    s.append(jin.next());
                }
                if ( command == 1 ) {//insert(String s)
                    s.insert(jin.next());
                }
                if ( command == 2 ) {//contains(String s)
                    System.out.println(s.contains(jin.next()));
                }
                if ( command == 3 ) {//reverse()
                    s.reverse();
                }
                if ( command == 4 ) {//toString()
                    System.out.println(s);
                }
                if ( command == 5 ) {//removeLast(int k)
                    s.removeLast(jin.nextInt());
                }
                if ( command == 6 ) {//end
                    break;
                }
            }
        }
    }

}
