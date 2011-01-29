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
        return new And(this, e);
    }

    @Override
    public final IExpression or(IExpression e) {
        if (e instanceof Const) {
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
    			((Expression) not).not = not; 
    		}
    	}
    	return not;
    }
    
    @Override
    public final IExpression sub(int v, Const c, Map<IExpression, IExpression> ctx) {
        if (!vars.testBit(v)) {
            return this;
        }
        if (this instanceof Var) {
        	Var var = (Var)this;
			if (v == var.name) {
				return var.sign ? c.not() : c;
			} else {
				return this;
			}
        }
        IExpression sub = ctx.get(this);
        if (sub == null) {
        	if (this instanceof And) {
        		And and = (And)this;
				sub = and.a.sub(v, c, ctx).and(and.b.sub(v, c, ctx));        	
        	} 
        	if (this instanceof Or) {
        		Or and = (Or)this;
				sub = and.a.sub(v, c, ctx).or(and.b.sub(v, c, ctx));        	
        	} 
        	if (sub == null)
        		throw new IllegalArgumentException();
            ctx.put(this, sub);
        }
        return sub;
    }

    @Override
    public final IExpression sub(int v, Const c) {
        return sub(v, c, new HashMap<IExpression, IExpression>());
    }

    protected abstract IExpression notImpl();
    //protected abstract IExpression subImpl(int v, Const c, Map<IExpression, IExpression> ctx);
}
