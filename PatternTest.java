import java.util.ArrayList;
import java.util.List;

class Song{
    String title;
    String artist;
    public Song(String naslov, String artist) {
        this.title = naslov;
        this.artist = artist;
    }
    public Song(Song sledna) {
        this.artist=sledna.artist;
        this.title=sledna.title;
    }
    public String getNaslov() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    @Override
    public String toString() {

        return "Song{" +

                "title=" + title +
                ", artist=" + artist +
                '}';
    }
}
class MP3Player{
    List<Song> songs;
    Song momentalno;
    boolean play;
    boolean stop;
    boolean pause;
    public MP3Player() {

        songs=new ArrayList<>();

        momentalno=null;
    }
    public MP3Player(List<Song> songs) {
        this.songs = songs;
        momentalno=songs.get(0);
        play=false;
        stop=false;
        pause=false;
    }
    public void pressPlay(){
        if(play==false)
            System.out.println("Song "+songs.indexOf(momentalno)+" is playing");
        else
            System.out.println("Song is already playing");
        play=true;
        stop=false;
        pause=false;
    }
    public void pressStop(){
        if(stop){
            System.out.println("Songs are already stopped");
            return;
        }
        if(pause){

            momentalno=songs.get(0);

            System.out.println("Songs are stopped");
            stop=true;
        }else {
            System.out.println("Song " + songs.indexOf(momentalno) + " is paused");
            play=false;
            pause=true;
        }
    }
    public void pressFWD(){
        System.out.println("Forward...");
        stop=false;
        play=false;
        pause=true;
        if(songs.indexOf(momentalno)==songs.size()-1){
            momentalno=songs.get(0);
        }else {
            momentalno=songs.get(songs.indexOf(momentalno)+1);
        }
    }
    public void pressREW(){
        System.out.println("Reward...");
        stop=false;
        play=false;
        pause=true;
        if(songs.indexOf(momentalno)==0){

            momentalno=songs.get(songs.size()-1);

        }else {
            momentalno=songs.get(songs.indexOf(momentalno)-1);
        }
    }
    public void printCurrentSong(){
        System.out.println(momentalno);
    }
    @Override
    public String toString() {
        return String.format("MP3Player{currentSong = %d, songList = %s}",songs.indexOf(momentalno),songs);
    }
}


public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde