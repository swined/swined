package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public abstract class Expression implements IExpression {

    protected final BigInteger vars;
    private IExpression not = null;

    public Expression(IExpression a, IExpression b) {
    	//this.complexity = a.complexity().add(b.complexity());
        this.vars = a.getVars().or(b.getVars());
        if (a instanceof Const || b instanceof Const) {
            throw new IllegalArgumentException();
        }
    }
    
    public Expression(BigInteger complexity, BigInteger vars) {
    	//this.complexity = complexity;
        this.vars = vars;
    }

    @Override
    public final IExpression and(IExpression e) {
        if (e instanceof Const) {
            return e.and(this);
        }
        if (this instanceof Var && e instanceof Var) {
        	Var a = (Var)this;
        	Var b = (Var)e;
        	if (a.name == b.name)
        		if (a.sign == b.sign)
        			return this;
        		else
        			return Const.ZERO;
        }
        return new And(this, e);
    }

    @Override
    public final IExpression or(IExpression e) {
        if (e instanceof Const) {
            return e.or(this);
        }
        if (this instanceof Var && e instanceof Var) {
        	Var a = (Var)this;
        	Var b = (Var)e;
        	if (a.name == b.name)
        		if (a.sign == b.sign)
        			return this;
        		else
        			return Const.ONE;
        }
        return new Or(this, e);
    }

    @Override
    public final BigInteger getVars() {
        return vars;
    }

    @Override 
    public final IExpression not() {
    	if (not == null) {
    		not = notImpl();
    		if (not instanceof Expression) {
    			((Expression) not).not = this; 
    		}
    	}
    	return not;
    }
    
    @Override
    public final IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
        if (!vars.testBit(v)) {
            return this;
        }
        IExpression sub = ctx.get(this);
        if (sub == null)
            ctx.put(this, sub = subImpl(v, c, ctx));
        return sub;
    }

    @Override
    public final IExpression sub(int v, Const c) {
        return sub(v, c, new HashMap<IExpression, IExpression>());
    }

    protected abstract IExpression notImpl();
    protected abstract IExpression subImpl(int v, Const c, Map<IExpression, IExpression> ctx);
}
