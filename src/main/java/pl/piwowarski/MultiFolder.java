package pl.piwowarski;

import java.util.List;

interface MultiFolder extends Folder {
    List<Folder> getFolders();
}
