package net.swined.prime.binary;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public abstract class Expression implements IExpression {

	private static final BigInteger SUB_THRESHOLD = BigInteger.valueOf(10);
	private static final BigInteger VAR_SUB_THRESHOLD = new BigInteger("10000000000000");
	protected final BigInteger complexity;
    protected final BigInteger vars;
    private IExpression not = null;

    public Expression(IExpression a, IExpression b) {
    	this.complexity = a.complexity().add(b.complexity());
        this.vars = a.getVars().or(b.getVars());
        if (a instanceof Const || b instanceof Const) {
            throw new IllegalArgumentException();
        }
    }
    
    public Expression(BigInteger complexity, BigInteger vars) {
    	this.complexity = complexity;
        this.vars = vars;
    }

    @Override
    public final IExpression and(IExpression e) {
        if (e instanceof Const) {
            return e.and(this);
        }
        if (this instanceof Var) {
        	Var var = (Var)this;
        	if (e.getVars().testBit(var.name))
        		return and(e.sub(var.name, var.sign ? Const.ZERO : Const.ONE));
        } else {
        	if (e instanceof Var && complexity().compareTo(VAR_SUB_THRESHOLD) < 0)
        		return e.and(this);
        }
        return new And(this, e);
    }

    @Override
    public final IExpression or(IExpression e) {
        if (e instanceof Const) {
            return e.or(this);
        }
        if (this instanceof Var) {
        	Var var = (Var)this;
        	if (e.getVars().testBit(var.name))
        		return or(e.sub(var.name, var.sign ? Const.ONE : Const.ZERO));
        } else {
        	if (e instanceof Var && complexity().compareTo(VAR_SUB_THRESHOLD) < 0)
        		return e.or(this);
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
        if (complexity().compareTo(SUB_THRESHOLD) < 0)
        	return subImpl(v, c, ctx);
        IExpression sub = ctx.get(this);
        if (sub == null) {
            ctx.put(this, sub = subImpl(v, c, ctx));
        }
        return sub;
    }

    @Override
    public final IExpression sub(int v, Const c) {
        return sub(v, c, new HashMap<IExpression, IExpression>());
    }

    @Override
    public final BigInteger complexity() {
    	return complexity;
    }
    
    protected abstract IExpression notImpl();
    protected abstract IExpression subImpl(int v, Const c, Map<IExpression, IExpression> ctx);
}
