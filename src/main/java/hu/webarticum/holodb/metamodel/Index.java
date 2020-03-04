package hu.webarticum.holodb.metamodel;

import java.util.Arrays;
import java.util.List;

import hu.webarticum.holodb.util.Selection;

public interface Index {

    public Table getTable();

    public List<Column> getColumns();
    
    public default Selection select(Object... values) {
        return select(Arrays.asList(values));
    }
    
    // TODO: map parameter?
    public Selection select(List<Object> values);
    
}
