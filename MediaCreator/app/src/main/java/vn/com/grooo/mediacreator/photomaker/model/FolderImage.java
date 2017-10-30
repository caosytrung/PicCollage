package vn.com.grooo.mediacreator.photomaker.model;

import java.util.ArrayList;

/**
 * Created by trungcs on 8/14/17.
 */

public class FolderImage {
    public String folderName;
    public ArrayList<String>  listUri;

    public FolderImage(String folderName, ArrayList<String> listUri) {
        this.folderName = folderName;
        this.listUri = listUri;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<String> getListUri() {
        return listUri;
    }

    public void setListUri(ArrayList<String> listUri) {
        this.listUri = listUri;
    }
}
