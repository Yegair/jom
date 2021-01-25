package io.yegair.jom.examples

import io.yegair.jom.Combinators.alt
import io.yegair.jom.Combinators.delimited
import io.yegair.jom.Combinators.many0
import io.yegair.jom.Combinators.map
import io.yegair.jom.Combinators.preceded
import io.yegair.jom.Input
import io.yegair.jom.ParseResult
import io.yegair.jom.Parsers.digit1
import io.yegair.jom.Parsers.multiSpace0
import io.yegair.jom.Parsers.tag
import io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult
import org.junit.jupiter.api.Test

sealed class Expr()

data class ValueExpr(private val value: Long) : Expr() {
    override fun toString() = "$value"
}

data class BinExpr(private val left: Expr, private val op: Op, private val right: Expr) : Expr() {
    override fun toString() = "($left $op $right)"
}

data class ParenExpr(private val inner: Expr) : Expr() {
    override fun toString() = "[$inner]"
}

enum class Op {
    Add,
    Sub,
    Mul,
    Div;

    override fun toString() = when (this) {
        Add -> "+"
        Sub -> "-"
        Mul -> "*"
        else -> "/"
    }
}

private fun parens(input: Input) =
    delimited<Expr>(
        multiSpace0(),
        delimited(
            tag("("),
            map(::expr) { ParenExpr(it) },
            tag(")")
        ),
        multiSpace0()
    ).parse(input)

private fun factor(input: Input) =
    alt(
        map(delimited(multiSpace0(), digit1(), multiSpace0())) { ValueExpr(it.toLong()) },
        ::parens
    ).parse(input)

private fun foldExpr(initial: Expr, remainder: List<Pair<Op, Expr>>): Expr =
    remainder.fold(initial) { acc, (op, expr) ->
        BinExpr(acc, op, expr)
    }

private fun term(input: Input): ParseResult<Expr> {
    val initial = factor(input)

    if (!initial.ok) {
        return initial
    }

    val remainder = many0(
        alt(
            { i ->
                preceded(tag("*"), ::factor)
                    .parse(i)
                    .map { remaining, expr ->
                        ParseResult.ok(remaining, Pair(Op.Mul, expr))
                    }
            },
            { i ->
                preceded(tag("/"), ::factor)
                    .parse(i)
                    .map { remaining, expr ->
                        ParseResult.ok(remaining, Pair(Op.Div, expr))
                    }
            }
        )
    ).parse(initial.remaining)

    if (!remainder.ok) {
        return ParseResult.error(remainder)
    }

    return ParseResult.ok(remainder.remaining, foldExpr(initial.output, remainder.output))
}

private fun expr(input: Input): ParseResult<Expr> {
    val initial = term(input)

    if (!initial.ok) {
        return initial
    }

    val remainder = many0(
        alt(
            { i ->
                preceded(tag("+"), ::term)
                    .parse(i)
                    .map { remaining, expr ->
                        ParseResult.ok(remaining, Pair(Op.Add, expr))
                    }
            },
            { i ->
                preceded(tag("-"), ::term)
                    .parse(i)
                    .map { remaining, expr ->
                        ParseResult.ok(remaining, Pair(Op.Sub, expr))
                    }
            }
        )
    ).parse(initial.remaining)

    if (!remainder.ok) {
        return ParseResult.error(remainder)
    }

    return ParseResult.ok(remainder.remaining, foldExpr(initial.output, remainder.output))
}

class ArithmeticAst {

    @Test
    fun factorTest() {
        assertThatParseResult(factor(Input.of("  3  ")))
            .isOk(ValueExpr(3))
            .hasRemainingInput("")
    }

    @Test
    fun termTest() {
        assertThatParseResult(term(Input.of(" 3 *  5   ")))
            .isOk(BinExpr(ValueExpr(3), Op.Mul, ValueExpr(5)))
            .hasRemainingInput("")
    }

    @Test
    fun exprTest() {
        assertThatParseResult(expr(Input.of(" 1 + 2 *  3 ")))
            .isOk(
                BinExpr(
                    ValueExpr(1),
                    Op.Add,
                    BinExpr(
                        ValueExpr(2),
                        Op.Mul,
                        ValueExpr(3)
                    )
                )
            )
            .hasRemainingInput("")

        assertThatParseResult(expr(Input.of(" 1 + 2 *  3 / 4 - 5 ")))
            .isOk(
                BinExpr(
                    BinExpr(
                        ValueExpr(1),
                        Op.Add,
                        BinExpr(
                            BinExpr(
                                ValueExpr(2),
                                Op.Mul,
                                ValueExpr(3)
                            ),
                            Op.Div,
                            ValueExpr(4)
                        )
                    ),
                    Op.Sub,
                    ValueExpr(5)
                )
            )
            .hasRemainingInput("")

        assertThatParseResult(expr(Input.of(" 72 / 2 / 3 ")))
            .isOk(
                BinExpr(
                    BinExpr(
                        ValueExpr(72),
                        Op.Div,
                        ValueExpr(2)
                    ),
                    Op.Div,
                    ValueExpr(3)
                )
            )
            .hasRemainingInput("")
    }

    @Test
    fun parensTest() {
        assertThatParseResult(expr(Input.of(" ( 1 + 2 ) *  3 ")))
            .isOk(
                BinExpr(
                    ParenExpr(
                        BinExpr(
                            ValueExpr(1),
                            Op.Add,
                            ValueExpr(2)
                        )
                    ),
                    Op.Mul,
                    ValueExpr(3)
                )
            )
            .hasRemainingInput("")
    }
}
