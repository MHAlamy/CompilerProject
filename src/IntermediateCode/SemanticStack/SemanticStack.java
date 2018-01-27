package IntermediateCode.SemanticStack;

import IntermediateCode.SemanticStack.SSObject.SSObject;

import java.util.Stack;

/**
 * Created by mha on 1/27/18.
 */
public class SemanticStack extends Stack<SSObject> {

    public SemanticStack() {
        super();
    }

    @Override
    public SSObject push(SSObject ssObject) {
        return super.push(ssObject);
    }

    @Override
    public synchronized SSObject pop() {
        return super.pop();
    }

    @Override
    public synchronized SSObject peek() {
        return super.peek();
    }

    public SSObject getElementAt(int indexDownFromTop) {

        int size = this.size();

        if (indexDownFromTop > size)
            throw new IndexOutOfBoundsException();

        return elementAt(size - 1 - indexDownFromTop);

    }

}
