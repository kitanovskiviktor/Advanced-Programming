import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;

class NoSuchRoomException extends Exception  {
    public NoSuchRoomException(String message) {
        super(message);
    }
}

class NoSuchUserException extends Exception  {
    public NoSuchUserException(String message) {
        super(message);
    }
}


class ChatRoom {
    String name;
    Set<String> users;

    public ChatRoom(String name) {
        this.name = name;
        users = new TreeSet<>();
    }

    public ChatRoom(){}

    public void addUser(String username){
        this.users.add(username);
    }

    public void removeUser(String username){
        this.users.remove(username);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        if(users.isEmpty()) {
            sb.append("EMPTY").append("\n");
        } else {
            users.forEach(user -> sb.append(user).append("\n"));
        }
        return sb.toString();
    }

    public boolean hasUser(String username){
        return this.users.contains(username);
    }

    public int numUsers(){
        return this.users.size();
    }
}

class ChatSystem {

    TreeMap<String, ChatRoom> rooms;
    List<String> allUsers;
    public ChatSystem() {
        this.rooms = new TreeMap<>();
        this.allUsers = new ArrayList<>();
    }

    public void addRoom(String roomName){
        this.rooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName){
        this.rooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if(this.rooms.containsKey(roomName)){
            return this.rooms.get(roomName);
        }
        return new ChatRoom();
    }

    public void register(String username) {
        this.allUsers.add(username);
        int minUsers = Integer.MAX_VALUE;
        ChatRoom minRoom = null;
        for (ChatRoom room : rooms.values()) {
            int numberUsers = room.numUsers();
            if (numberUsers < minUsers) {
                minUsers = numberUsers;
                minRoom = room;
            }
        }
        if (minRoom != null) {
            minRoom.addUser(username);
        }
    }

    public void registerAndJoin(String username, String roomname) throws NoSuchRoomException{
        this.allUsers.add(username);
        if (rooms.containsKey(roomname)) {
            this.rooms.get(roomname).addUser(username);
        }
    }

    public void joinRoom(String username, String roomname) throws NoSuchUserException, NoSuchRoomException{
        if(this.allUsers.contains(username) && this.rooms.containsKey(roomname)){
            this.rooms.get(roomname).addUser(username);
        }
    }

    public void leaveRoom(String username, String roomname) throws NoSuchRoomException{
        if(this.rooms.containsKey(roomname)){
            this.rooms.get(roomname).removeUser(username);
        }
    }

    public void followFriend(String username, String friendUsername) throws NoSuchUserException{
        if(!this.allUsers.contains(username)){
            return;
        }
        if (!this.allUsers.contains(friendUsername)) {
            return;
        }
        for(ChatRoom room : rooms.values()){
            if(room.hasUser(friendUsername)){
                room.addUser(username);
            }
        }
    }
}



public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            boolean flag = false;
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if(k!=0){
                    flag = true;
                }
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            if(!flag) {  System.out.println(""); }
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs, (Object[]) params);
                    }
                }
            }
        }
    }

}
