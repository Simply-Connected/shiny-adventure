package org.simply.connected.application.optimization.methods.front.newton;

import org.simply.connected.application.optimization.methods.multivariate.math.Vector;
import org.simply.connected.application.optimization.methods.newton.expression.*;
import org.simply.connected.application.optimization.methods.newton.function.FunctionalExpression;

import java.util.List;

class Functions {
    private Functions() {}
    static final X x1 = new X(0);
    static final X x2 = new X(1);
    static final X x3 = new X(2);
    static final X x4 = new X(3);

    static final FunctionalExpression f1 =  new FunctionalExpression(
            new Sub(
                    new Add(
                            new Square(x1),
                            new Square(x2)
                    ),
                    new Mul(new Const(1.2d), new Mul(x1, x2))
            ),
            2
    );

    static final FunctionalExpression f2 =  new FunctionalExpression(
            new Add(
            new Mul(
            new Const(100),
            new Square(new Sub(x2, new Square(x1)))),
            new Square(new Sub(Const.ONE, x1))
    ),
            2
    );
    static final FunctionalExpression f3 = new FunctionalExpression(
            new Add(
                    new Square(
                            new Sum(
                                    new Square(x1),
                                    x2,
                                    new Const(-11)
                            )),
                    new Square(
                            new Sum(
                                    x1,
                                    new Square(x2),
                                    new Const(-7)
                            ))
            ),
            2
    );
    static final FunctionalExpression f4 = new FunctionalExpression(
            new Sum(
                    new Square(new Add(x1, new Mul(new Const(10), x2))),
                    new Mul(
                            new Const(5),
                            new Square(new Sub(x3, x4))
                    ),
                    new Power(new Sub(x2, new Mul(new Const(2), x3)), 4),
                    new Mul(
                            new Const(10),
                            new Power(new Sub(x1, x4), 5)
                    )
            ),
            4
    );
    static final FunctionalExpression f5 =  new FunctionalExpression(
            new Sub(
                    new Sub(
                            new Const(100d),
                            new Div(
                                    Const.TWO,
                                    new Add(
                                            new Add(
                                                    Const.ONE,
                                                    new Square(
                                                            new Div(
                                                                    new Sub(x1, Const.ONE),
                                                                    Const.TWO
                                                            )
                                                    )
                                            ),
                                            new Square(
                                                    new Div(
                                                            new Sub(x2, Const.ONE),
                                                            Const.THREE
                                                    )
                                            )
                                    )
                            )),
                    new Div(
                            Const.ONE,
                            new Add(
                                    new Add(
                                            Const.ONE,
                                            new Square(new Div(new Sub(x1, Const.TWO), Const.TWO))
                                    ),
                                    new Square(new Div(new Sub(x2, Const.ONE), Const.THREE))
                            )
                    )

            ),
            2
    );

    static final FunctionalExpression mf1 = new FunctionalExpression(
            new Sum(
                    new Mul(new Const(0.5), new Square(x1)),
                    new Mul(new Const(5), new Square(x2)),
                    new Mul(new Const(5), x1),
                    new Mul(new Const(15), x2),
                    new Const(2)
            ),
            2
    );
    static final FunctionalExpression mf2 = new FunctionalExpression(
            new Cos(
                    new Sum(
                            new Const(Math.PI),
                            new Mul(new Const(0.01), new Square(x1)),
                            new Mul(new Const(0.01), new Power(x2, 4))
                    )
            )
            ,2
    );
    static final FunctionalExpression mf3 = new FunctionalExpression(
            new Sum(
                    new Cos(new Add(new Const(Math.PI), new Mul(new Const(0.01), new Square(x1)))),
                    new Sin(new Add(new Const(-Math.PI / 2), new Mul(new Const(0.01), new Square(x2))))
            ),
            2
    );


}
