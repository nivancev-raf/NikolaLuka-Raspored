package api;

public class Manager {

    private static SpecFileExport obj;

    public static SpecFileExport getSpecFileExport(){
        return obj;
    }

    public static void setObj(SpecFileExport obj) {
        Manager.obj = obj;
    }
}
