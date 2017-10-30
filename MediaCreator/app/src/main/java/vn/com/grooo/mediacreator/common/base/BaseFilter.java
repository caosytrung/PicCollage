package vn.com.grooo.mediacreator.common.base;

/**
 * Created by trungcs on 9/7/17.
 */

public class BaseFilter {

    private Object filterObject;
    private int type ;

    public BaseFilter(Object filterObject, int type) {
        this.filterObject = filterObject;
        this.type = type;
    }

    public Object getFilterObject() {

        return filterObject;
    }

    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
