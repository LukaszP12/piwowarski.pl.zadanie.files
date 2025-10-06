package pl.piwowarski;

public interface Cabinet {
    Optional<Folder> findFolderByName(String name);

    List<Folder> findFoldersBySize(String size);

    int count();
}
