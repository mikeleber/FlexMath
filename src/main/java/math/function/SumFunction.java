package math.function;

import ch.sympany.commons.lang.math.MathEval;

public class SumFunction implements MathEval.FunctionHandler {

    @Override
    public Object evaluateFunction(String fncnam, MathEval.ArgParser fncargs) throws ArithmeticException {
        StringBuilder result = new StringBuilder();
        while (fncargs.hasNext()) {
            result.append(MathEval.toString(fncargs.next()));
        }
        return result.toString();
    }
}
