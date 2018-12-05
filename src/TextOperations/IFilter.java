package TextOperations;

import java.util.List;

public interface IFilter {

    public List<Token> filter(List<Token> lst);
    public boolean contains(Token token);
    public DynamicFilter substract(IFilter filter);
    public DynamicFilter intersection(IFilter filter);
}
