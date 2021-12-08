import java.util.Map;

public class StorageThread implements Runnable {

  Map<String, String> mapOfThisServer;

    public StorageThread(Map<String, String> mapOfThisServer) {
    this.mapOfThisServer = mapOfThisServer;
  }

  @Override
  public void run() {

  }
}
