package math.function;

import java.util.Collection;
import java.util.Objects;

import math.MathEval;


public class ContainsFunction implements MathEval.FunctionHandler {

    public Object evaluateFunction(String fncnam, MathEval.ArgParser fncargs) throws ArithmeticException {
        Object source = fncargs.next();
        Object search = fncargs.next();
        if (source instanceof String) {
            return source.toString().indexOf(search.toString());
        } else if (source instanceof Collection) {
            int pos = -1;
            for (Object sourceVal : (Collection) source) {
                pos++;
                if (Objects.equals(sourceVal, search)) {
                    return pos;
                }
            }
        }
        return -1;
    }
}
