import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO{
    public static void main(String[] args){

        String[] names = {"Alice", "Bob", "Charlie"};

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.vm"));
            writer.write("Hello, World!");
            writer.newLine();
            writer.write("Here is another line");
            for (String name: names){
                writer.write(name);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
     
        try {
            BufferedReader reader = new BufferedReader(new FileReader("output.vm"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}