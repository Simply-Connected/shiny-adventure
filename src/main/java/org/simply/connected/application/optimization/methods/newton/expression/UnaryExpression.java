package org.simply.connected.application.optimization.methods.newton.expression;

public abstract class UnaryExpression implements Expression {
    protected final Expression expression;


    public UnaryExpression(Expression expression) {
        this.expression = expression;
    }
}
