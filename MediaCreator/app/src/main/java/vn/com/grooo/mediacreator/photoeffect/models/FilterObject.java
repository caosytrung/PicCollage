package vn.com.grooo.mediacreator.photoeffect.models;

/**
 * Created by trungcs on 9/29/17.
 */

public class FilterObject {
    public String name;
    public int offset;

    public FilterObject(String name, int offset) {
        this.name = name;
        this.offset = offset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
