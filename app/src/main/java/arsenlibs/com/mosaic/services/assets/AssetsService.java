package arsenlibs.com.mosaic.services.assets;


public interface AssetsService {

    String[] getList(String folder);

    String readText(String path);

}
