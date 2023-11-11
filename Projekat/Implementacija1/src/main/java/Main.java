import model.Schedule;

public class Main {
    public static void main(String[] args) {
        FileExporter fileExporter = new FileExporter(Schedule.getInstance());
        fileExporter.exportFile("nista");

    }
}