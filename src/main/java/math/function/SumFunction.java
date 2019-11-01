package math.function;


import math.MathEval;

public class SumFunction implements MathEval.FunctionHandler {

    public Object evaluateFunction(String fncnam, MathEval.ArgParser fncargs) throws ArithmeticException {
        StringBuilder result = new StringBuilder();
        while (fncargs.hasNext()) {
            result.append(MathEval.toString(fncargs.next()));
        }
        return result.toString();
    }
}
