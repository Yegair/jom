var pages = [{"name":"object Combinators","description":"io.yegair.jom.Combinators","location":"jom/io.yegair.jom/-combinators/index.html","searchKeys":["Combinators","object Combinators"]},{"name":"fun <O> allConsuming(parser: Parser<O>): Parser<O>","description":"io.yegair.jom.Combinators.allConsuming","location":"jom/io.yegair.jom/-combinators/all-consuming.html","searchKeys":["allConsuming","fun <O> allConsuming(parser: Parser<O>): Parser<O>"]},{"name":"fun <O> alt(vararg parsers: Parser<O>): Parser<O>","description":"io.yegair.jom.Combinators.alt","location":"jom/io.yegair.jom/-combinators/alt.html","searchKeys":["alt","fun <O> alt(vararg parsers: Parser<O>): Parser<O>"]},{"name":"fun <O> cond(condition: Boolean, parser: Parser<O>): Parser<O?>","description":"io.yegair.jom.Combinators.cond","location":"jom/io.yegair.jom/-combinators/cond.html","searchKeys":["cond","fun <O> cond(condition: Boolean, parser: Parser<O>): Parser<O?>"]},{"name":"fun <O> count(parser: Parser<O>, times: Int): Parser<List<O>>","description":"io.yegair.jom.Combinators.count","location":"jom/io.yegair.jom/-combinators/count.html","searchKeys":["count","fun <O> count(parser: Parser<O>, times: Int): Parser<List<O>>"]},{"name":"fun <O> delimited(left: Parser<*>, middle: Parser<O>, right: Parser<*>): Parser<O>","description":"io.yegair.jom.Combinators.delimited","location":"jom/io.yegair.jom/-combinators/delimited.html","searchKeys":["delimited","fun <O> delimited(left: Parser<*>, middle: Parser<O>, right: Parser<*>): Parser<O>"]},{"name":"fun <O> many0(parser: Parser<O>): Parser<List<O>>","description":"io.yegair.jom.Combinators.many0","location":"jom/io.yegair.jom/-combinators/many0.html","searchKeys":["many0","fun <O> many0(parser: Parser<O>): Parser<List<O>>"]},{"name":"fun <O> many1(parser: Parser<O>): Parser<List<O>>","description":"io.yegair.jom.Combinators.many1","location":"jom/io.yegair.jom/-combinators/many1.html","searchKeys":["many1","fun <O> many1(parser: Parser<O>): Parser<List<O>>"]},{"name":"fun <O, R> map(parser: Parser<O>, mapping: (O) -> R): Parser<R>","description":"io.yegair.jom.Combinators.map","location":"jom/io.yegair.jom/-combinators/map.html","searchKeys":["map","fun <O, R> map(parser: Parser<O>, mapping: (O) -> R): Parser<R>"]},{"name":"fun <O, R> mapOpt(parser: Parser<O>, mapping: (O) -> R?): Parser<R>","description":"io.yegair.jom.Combinators.mapOpt","location":"jom/io.yegair.jom/-combinators/map-opt.html","searchKeys":["mapOpt","fun <O, R> mapOpt(parser: Parser<O>, mapping: (O) -> R?): Parser<R>"]},{"name":"fun not(parser: Parser<*>): Parser<Unit>","description":"io.yegair.jom.Combinators.not","location":"jom/io.yegair.jom/-combinators/not.html","searchKeys":["not","fun not(parser: Parser<*>): Parser<Unit>"]},{"name":"fun <O> opt(parser: Parser<O>): Parser<O?>","description":"io.yegair.jom.Combinators.opt","location":"jom/io.yegair.jom/-combinators/opt.html","searchKeys":["opt","fun <O> opt(parser: Parser<O>): Parser<O?>"]},{"name":"fun <O1, O2> pair(first: Parser<O1>, second: Parser<O2>): Parser<Pair<O1, O2>>","description":"io.yegair.jom.Combinators.pair","location":"jom/io.yegair.jom/-combinators/pair.html","searchKeys":["pair","fun <O1, O2> pair(first: Parser<O1>, second: Parser<O2>): Parser<Pair<O1, O2>>"]},{"name":"fun <O> peek(parser: Parser<O>): Parser<O>","description":"io.yegair.jom.Combinators.peek","location":"jom/io.yegair.jom/-combinators/peek.html","searchKeys":["peek","fun <O> peek(parser: Parser<O>): Parser<O>"]},{"name":"fun peek(): Input","description":"io.yegair.jom.Input.peek","location":"jom/io.yegair.jom/-input/peek.html","searchKeys":["peek","fun peek(): Input"]},{"name":"fun <O1, O2> permutation2(parser1: Parser<O1>, parser2: Parser<O2>): Parser<Pair<O1, O2>>","description":"io.yegair.jom.Combinators.permutation2","location":"jom/io.yegair.jom/-combinators/permutation2.html","searchKeys":["permutation2","fun <O1, O2> permutation2(parser1: Parser<O1>, parser2: Parser<O2>): Parser<Pair<O1, O2>>"]},{"name":"fun <O1, O2, O3> permutation3(parser1: Parser<O1>, parser2: Parser<O2>, parser3: Parser<O3>): Parser<Triple<O1, O2, O3>>","description":"io.yegair.jom.Combinators.permutation3","location":"jom/io.yegair.jom/-combinators/permutation3.html","searchKeys":["permutation3","fun <O1, O2, O3> permutation3(parser1: Parser<O1>, parser2: Parser<O2>, parser3: Parser<O3>): Parser<Triple<O1, O2, O3>>"]},{"name":"fun <O> preceded(first: Parser<*>, second: Parser<O>): Parser<O>","description":"io.yegair.jom.Combinators.preceded","location":"jom/io.yegair.jom/-combinators/preceded.html","searchKeys":["preceded","fun <O> preceded(first: Parser<*>, second: Parser<O>): Parser<O>"]},{"name":"fun <O> separatedList0(separator: Parser<*>, parser: Parser<O>): Parser<List<O>>","description":"io.yegair.jom.Combinators.separatedList0","location":"jom/io.yegair.jom/-combinators/separated-list0.html","searchKeys":["separatedList0","fun <O> separatedList0(separator: Parser<*>, parser: Parser<O>): Parser<List<O>>"]},{"name":"fun <O1, O2> separatedPair(first: Parser<O1>, separator: Parser<*>, second: Parser<O2>): Parser<Pair<O1, O2>>","description":"io.yegair.jom.Combinators.separatedPair","location":"jom/io.yegair.jom/-combinators/separated-pair.html","searchKeys":["separatedPair","fun <O1, O2> separatedPair(first: Parser<O1>, separator: Parser<*>, second: Parser<O2>): Parser<Pair<O1, O2>>"]},{"name":"fun <O> success(output: O): Parser<O>","description":"io.yegair.jom.Combinators.success","location":"jom/io.yegair.jom/-combinators/success.html","searchKeys":["success","fun <O> success(output: O): Parser<O>"]},{"name":"fun success(): Parser<Unit>","description":"io.yegair.jom.Parsers.success","location":"jom/io.yegair.jom/-parsers/success.html","searchKeys":["success","fun success(): Parser<Unit>"]},{"name":"fun <O> terminated(first: Parser<O>, second: Parser<*>): Parser<O>","description":"io.yegair.jom.Combinators.terminated","location":"jom/io.yegair.jom/-combinators/terminated.html","searchKeys":["terminated","fun <O> terminated(first: Parser<O>, second: Parser<*>): Parser<O>"]},{"name":"fun <O1, O2, O3> triple(first: Parser<O1>, second: Parser<O2>, third: Parser<O3>): Parser<Triple<O1, O2, O3>>","description":"io.yegair.jom.Combinators.triple","location":"jom/io.yegair.jom/-combinators/triple.html","searchKeys":["triple","fun <O1, O2, O3> triple(first: Parser<O1>, second: Parser<O2>, third: Parser<O3>): Parser<Triple<O1, O2, O3>>"]},{"name":"fun <O> value(output: O, parser: Parser<*>): Parser<O>","description":"io.yegair.jom.Combinators.value","location":"jom/io.yegair.jom/-combinators/value.html","searchKeys":["value","fun <O> value(output: O, parser: Parser<*>): Parser<O>"]},{"name":"fun <O> verify(parser: Parser<O>, predicate: (O) -> Boolean): Parser<O>","description":"io.yegair.jom.Combinators.verify","location":"jom/io.yegair.jom/-combinators/verify.html","searchKeys":["verify","fun <O> verify(parser: Parser<O>, predicate: (O) -> Boolean): Parser<O>"]},{"name":"class Input","description":"io.yegair.jom.Input","location":"jom/io.yegair.jom/-input/index.html","searchKeys":["Input","class Input"]},{"name":"object Companion","description":"io.yegair.jom.Input.Companion","location":"jom/io.yegair.jom/-input/-companion/index.html","searchKeys":["Companion","object Companion"]},{"name":"object Companion","description":"io.yegair.jom.ParseResult.Companion","location":"jom/io.yegair.jom/-parse-result/-companion/index.html","searchKeys":["Companion","object Companion"]},{"name":"object Companion","description":"io.yegair.jom.Parser.Companion","location":"jom/io.yegair.jom/-parser/-companion/index.html","searchKeys":["Companion","object Companion"]},{"name":"object Companion","description":"io.yegair.jom.test.ParseResultAssert.Companion","location":"jom/io.yegair.jom.test/-parse-result-assert/-companion/index.html","searchKeys":["Companion","object Companion"]},{"name":"const val CODE_POINT_EOF: Int","description":"io.yegair.jom.Input.Companion.CODE_POINT_EOF","location":"jom/io.yegair.jom/-input/-companion/-c-o-d-e_-p-o-i-n-t_-e-o-f.html","searchKeys":["CODE_POINT_EOF","const val CODE_POINT_EOF: Int"]},{"name":"fun isEof(cp: Int): Boolean","description":"io.yegair.jom.Input.Companion.isEof","location":"jom/io.yegair.jom/-input/-companion/is-eof.html","searchKeys":["isEof","fun isEof(cp: Int): Boolean"]},{"name":"val bytesProcessed: Long","description":"io.yegair.jom.Input.bytesProcessed","location":"jom/io.yegair.jom/-input/bytes-processed.html","searchKeys":["bytesProcessed","val bytesProcessed: Long"]},{"name":"fun exhausted(): Boolean","description":"io.yegair.jom.Input.exhausted","location":"jom/io.yegair.jom/-input/exhausted.html","searchKeys":["exhausted","fun exhausted(): Boolean"]},{"name":"fun readByteArray(): ByteArray","description":"io.yegair.jom.Input.readByteArray","location":"jom/io.yegair.jom/-input/read-byte-array.html","searchKeys":["readByteArray","fun readByteArray(): ByteArray"]},{"name":"fun readByteString(byteCount: Long): ByteString","description":"io.yegair.jom.Input.readByteString","location":"jom/io.yegair.jom/-input/read-byte-string.html","searchKeys":["readByteString","fun readByteString(byteCount: Long): ByteString"]},{"name":"fun readUtf8(): String","description":"io.yegair.jom.Input.readUtf8","location":"jom/io.yegair.jom/-input/read-utf8.html","searchKeys":["readUtf8","fun readUtf8(): String"]},{"name":"fun readUtf8CodePoint(): Utf8CodePoint","description":"io.yegair.jom.Input.readUtf8CodePoint","location":"jom/io.yegair.jom/-input/read-utf8-code-point.html","searchKeys":["readUtf8CodePoint","fun readUtf8CodePoint(): Utf8CodePoint"]},{"name":"fun skip(byteCount: Long)","description":"io.yegair.jom.Input.skip","location":"jom/io.yegair.jom/-input/skip.html","searchKeys":["skip","fun skip(byteCount: Long)"]},{"name":"open override fun toString(): String","description":"io.yegair.jom.Input.toString","location":"jom/io.yegair.jom/-input/to-string.html","searchKeys":["toString","open override fun toString(): String"]},{"name":"open override fun toString(): String","description":"io.yegair.jom.ParseResult.toString","location":"jom/io.yegair.jom/-parse-result/to-string.html","searchKeys":["toString","open override fun toString(): String"]},{"name":"enum ParseError : Enum<ParseError> ","description":"io.yegair.jom.ParseError","location":"jom/io.yegair.jom/-parse-error/index.html","searchKeys":["ParseError","enum ParseError : Enum<ParseError> "]},{"name":"Alpha()","description":"io.yegair.jom.ParseError.Alpha","location":"jom/io.yegair.jom/-parse-error/-alpha/index.html","searchKeys":["Alpha","Alpha()"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.Alpha.name","location":"jom/io.yegair.jom/-parse-error/-alpha/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.AlphaNumeric.name","location":"jom/io.yegair.jom/-parse-error/-alpha-numeric/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.Choice.name","location":"jom/io.yegair.jom/-parse-error/-choice/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.CodePoint.name","location":"jom/io.yegair.jom/-parse-error/-code-point/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.Count.name","location":"jom/io.yegair.jom/-parse-error/-count/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.CrLf.name","location":"jom/io.yegair.jom/-parse-error/-cr-lf/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.Digit.name","location":"jom/io.yegair.jom/-parse-error/-digit/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.Eof.name","location":"jom/io.yegair.jom/-parse-error/-eof/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.HexDigit.name","location":"jom/io.yegair.jom/-parse-error/-hex-digit/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.Many.name","location":"jom/io.yegair.jom/-parse-error/-many/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.MapOpt.name","location":"jom/io.yegair.jom/-parse-error/-map-opt/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.MultiSpace.name","location":"jom/io.yegair.jom/-parse-error/-multi-space/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.NoneOf.name","location":"jom/io.yegair.jom/-parse-error/-none-of/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.Not.name","location":"jom/io.yegair.jom/-parse-error/-not/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.OctDigit.name","location":"jom/io.yegair.jom/-parse-error/-oct-digit/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.OneOf.name","location":"jom/io.yegair.jom/-parse-error/-one-of/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.Satisfy.name","location":"jom/io.yegair.jom/-parse-error/-satisfy/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.Space.name","location":"jom/io.yegair.jom/-parse-error/-space/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.Tag.name","location":"jom/io.yegair.jom/-parse-error/-tag/name.html","searchKeys":["name","val name: String"]},{"name":"val name: String","description":"io.yegair.jom.ParseError.Verify.name","location":"jom/io.yegair.jom/-parse-error/-verify/name.html","searchKeys":["name","val name: String"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.Alpha.ordinal","location":"jom/io.yegair.jom/-parse-error/-alpha/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.AlphaNumeric.ordinal","location":"jom/io.yegair.jom/-parse-error/-alpha-numeric/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.Choice.ordinal","location":"jom/io.yegair.jom/-parse-error/-choice/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.CodePoint.ordinal","location":"jom/io.yegair.jom/-parse-error/-code-point/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.Count.ordinal","location":"jom/io.yegair.jom/-parse-error/-count/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.CrLf.ordinal","location":"jom/io.yegair.jom/-parse-error/-cr-lf/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.Digit.ordinal","location":"jom/io.yegair.jom/-parse-error/-digit/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.Eof.ordinal","location":"jom/io.yegair.jom/-parse-error/-eof/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.HexDigit.ordinal","location":"jom/io.yegair.jom/-parse-error/-hex-digit/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.Many.ordinal","location":"jom/io.yegair.jom/-parse-error/-many/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.MapOpt.ordinal","location":"jom/io.yegair.jom/-parse-error/-map-opt/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.MultiSpace.ordinal","location":"jom/io.yegair.jom/-parse-error/-multi-space/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.NoneOf.ordinal","location":"jom/io.yegair.jom/-parse-error/-none-of/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.Not.ordinal","location":"jom/io.yegair.jom/-parse-error/-not/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.OctDigit.ordinal","location":"jom/io.yegair.jom/-parse-error/-oct-digit/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.OneOf.ordinal","location":"jom/io.yegair.jom/-parse-error/-one-of/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.Satisfy.ordinal","location":"jom/io.yegair.jom/-parse-error/-satisfy/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.Space.ordinal","location":"jom/io.yegair.jom/-parse-error/-space/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.Tag.ordinal","location":"jom/io.yegair.jom/-parse-error/-tag/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"val ordinal: Int","description":"io.yegair.jom.ParseError.Verify.ordinal","location":"jom/io.yegair.jom/-parse-error/-verify/ordinal.html","searchKeys":["ordinal","val ordinal: Int"]},{"name":"AlphaNumeric()","description":"io.yegair.jom.ParseError.AlphaNumeric","location":"jom/io.yegair.jom/-parse-error/-alpha-numeric/index.html","searchKeys":["AlphaNumeric","AlphaNumeric()"]},{"name":"Choice()","description":"io.yegair.jom.ParseError.Choice","location":"jom/io.yegair.jom/-parse-error/-choice/index.html","searchKeys":["Choice","Choice()"]},{"name":"CodePoint()","description":"io.yegair.jom.ParseError.CodePoint","location":"jom/io.yegair.jom/-parse-error/-code-point/index.html","searchKeys":["CodePoint","CodePoint()"]},{"name":"Count()","description":"io.yegair.jom.ParseError.Count","location":"jom/io.yegair.jom/-parse-error/-count/index.html","searchKeys":["Count","Count()"]},{"name":"CrLf()","description":"io.yegair.jom.ParseError.CrLf","location":"jom/io.yegair.jom/-parse-error/-cr-lf/index.html","searchKeys":["CrLf","CrLf()"]},{"name":"Digit()","description":"io.yegair.jom.ParseError.Digit","location":"jom/io.yegair.jom/-parse-error/-digit/index.html","searchKeys":["Digit","Digit()"]},{"name":"Eof()","description":"io.yegair.jom.ParseError.Eof","location":"jom/io.yegair.jom/-parse-error/-eof/index.html","searchKeys":["Eof","Eof()"]},{"name":"HexDigit()","description":"io.yegair.jom.ParseError.HexDigit","location":"jom/io.yegair.jom/-parse-error/-hex-digit/index.html","searchKeys":["HexDigit","HexDigit()"]},{"name":"Many()","description":"io.yegair.jom.ParseError.Many","location":"jom/io.yegair.jom/-parse-error/-many/index.html","searchKeys":["Many","Many()"]},{"name":"MapOpt()","description":"io.yegair.jom.ParseError.MapOpt","location":"jom/io.yegair.jom/-parse-error/-map-opt/index.html","searchKeys":["MapOpt","MapOpt()"]},{"name":"MultiSpace()","description":"io.yegair.jom.ParseError.MultiSpace","location":"jom/io.yegair.jom/-parse-error/-multi-space/index.html","searchKeys":["MultiSpace","MultiSpace()"]},{"name":"NoneOf()","description":"io.yegair.jom.ParseError.NoneOf","location":"jom/io.yegair.jom/-parse-error/-none-of/index.html","searchKeys":["NoneOf","NoneOf()"]},{"name":"Not()","description":"io.yegair.jom.ParseError.Not","location":"jom/io.yegair.jom/-parse-error/-not/index.html","searchKeys":["Not","Not()"]},{"name":"OctDigit()","description":"io.yegair.jom.ParseError.OctDigit","location":"jom/io.yegair.jom/-parse-error/-oct-digit/index.html","searchKeys":["OctDigit","OctDigit()"]},{"name":"OneOf()","description":"io.yegair.jom.ParseError.OneOf","location":"jom/io.yegair.jom/-parse-error/-one-of/index.html","searchKeys":["OneOf","OneOf()"]},{"name":"Satisfy()","description":"io.yegair.jom.ParseError.Satisfy","location":"jom/io.yegair.jom/-parse-error/-satisfy/index.html","searchKeys":["Satisfy","Satisfy()"]},{"name":"Space()","description":"io.yegair.jom.ParseError.Space","location":"jom/io.yegair.jom/-parse-error/-space/index.html","searchKeys":["Space","Space()"]},{"name":"Tag()","description":"io.yegair.jom.ParseError.Tag","location":"jom/io.yegair.jom/-parse-error/-tag/index.html","searchKeys":["Tag","Tag()"]},{"name":"Verify()","description":"io.yegair.jom.ParseError.Verify","location":"jom/io.yegair.jom/-parse-error/-verify/index.html","searchKeys":["Verify","Verify()"]},{"name":"sealed class ParseResult<O>","description":"io.yegair.jom.ParseResult","location":"jom/io.yegair.jom/-parse-result/index.html","searchKeys":["ParseResult","sealed class ParseResult<O>"]},{"name":"fun <O> ok(remaining: Input, output: O): ParseResult<O>","description":"io.yegair.jom.ParseResult.Companion.ok","location":"jom/io.yegair.jom/-parse-result/-companion/ok.html","searchKeys":["ok","fun <O> ok(remaining: Input, output: O): ParseResult<O>"]},{"name":"val ok: Boolean","description":"io.yegair.jom.ParseResult.ok","location":"jom/io.yegair.jom/-parse-result/ok.html","searchKeys":["ok","val ok: Boolean"]},{"name":"fun isError(): Boolean","description":"io.yegair.jom.ParseResult.isError","location":"jom/io.yegair.jom/-parse-result/is-error.html","searchKeys":["isError","fun isError(): Boolean"]},{"name":"abstract fun mapError(mapping: (Input, ParseError) -> ParseResult<O>): ParseResult<O>","description":"io.yegair.jom.ParseResult.mapError","location":"jom/io.yegair.jom/-parse-result/map-error.html","searchKeys":["mapError","abstract fun mapError(mapping: (Input, ParseError) -> ParseResult<O>): ParseResult<O>"]},{"name":"val remaining: Input","description":"io.yegair.jom.ParseResult.remaining","location":"jom/io.yegair.jom/-parse-result/remaining.html","searchKeys":["remaining","val remaining: Input"]},{"name":"fun fun interface Parser<O>","description":"io.yegair.jom.Parser","location":"jom/io.yegair.jom/-parser/index.html","searchKeys":["Parser","fun fun interface Parser<O>"]},{"name":"fun <O> peeking(parser: Parser<O>): Parser<O>","description":"io.yegair.jom.Parser.Companion.peeking","location":"jom/io.yegair.jom/-parser/-companion/peeking.html","searchKeys":["peeking","fun <O> peeking(parser: Parser<O>): Parser<O>"]},{"name":"object Parsers","description":"io.yegair.jom.Parsers","location":"jom/io.yegair.jom/-parsers/index.html","searchKeys":["Parsers","object Parsers"]},{"name":"fun alpha0(): Parser<String>","description":"io.yegair.jom.Parsers.alpha0","location":"jom/io.yegair.jom/-parsers/alpha0.html","searchKeys":["alpha0","fun alpha0(): Parser<String>"]},{"name":"fun alpha1(): Parser<String>","description":"io.yegair.jom.Parsers.alpha1","location":"jom/io.yegair.jom/-parsers/alpha1.html","searchKeys":["alpha1","fun alpha1(): Parser<String>"]},{"name":"fun alphaNumeric0(): Parser<String>","description":"io.yegair.jom.Parsers.alphaNumeric0","location":"jom/io.yegair.jom/-parsers/alpha-numeric0.html","searchKeys":["alphaNumeric0","fun alphaNumeric0(): Parser<String>"]},{"name":"fun alphaNumeric1(): Parser<String>","description":"io.yegair.jom.Parsers.alphaNumeric1","location":"jom/io.yegair.jom/-parsers/alpha-numeric1.html","searchKeys":["alphaNumeric1","fun alphaNumeric1(): Parser<String>"]},{"name":"fun anyCodePoint(): Parser<Utf8CodePoint>","description":"io.yegair.jom.Parsers.anyCodePoint","location":"jom/io.yegair.jom/-parsers/any-code-point.html","searchKeys":["anyCodePoint","fun anyCodePoint(): Parser<Utf8CodePoint>"]},{"name":"fun crlf(): Parser<String>","description":"io.yegair.jom.Parsers.crlf","location":"jom/io.yegair.jom/-parsers/crlf.html","searchKeys":["crlf","fun crlf(): Parser<String>"]},{"name":"fun digit0(): Parser<String>","description":"io.yegair.jom.Parsers.digit0","location":"jom/io.yegair.jom/-parsers/digit0.html","searchKeys":["digit0","fun digit0(): Parser<String>"]},{"name":"fun digit1(): Parser<String>","description":"io.yegair.jom.Parsers.digit1","location":"jom/io.yegair.jom/-parsers/digit1.html","searchKeys":["digit1","fun digit1(): Parser<String>"]},{"name":"fun hexDigit0(): Parser<String>","description":"io.yegair.jom.Parsers.hexDigit0","location":"jom/io.yegair.jom/-parsers/hex-digit0.html","searchKeys":["hexDigit0","fun hexDigit0(): Parser<String>"]},{"name":"fun hexDigit1(): Parser<String>","description":"io.yegair.jom.Parsers.hexDigit1","location":"jom/io.yegair.jom/-parsers/hex-digit1.html","searchKeys":["hexDigit1","fun hexDigit1(): Parser<String>"]},{"name":"fun lineEnding(): Parser<String>","description":"io.yegair.jom.Parsers.lineEnding","location":"jom/io.yegair.jom/-parsers/line-ending.html","searchKeys":["lineEnding","fun lineEnding(): Parser<String>"]},{"name":"fun multiSpace0(): Parser<String>","description":"io.yegair.jom.Parsers.multiSpace0","location":"jom/io.yegair.jom/-parsers/multi-space0.html","searchKeys":["multiSpace0","fun multiSpace0(): Parser<String>"]},{"name":"fun multiSpace1(): Parser<String>","description":"io.yegair.jom.Parsers.multiSpace1","location":"jom/io.yegair.jom/-parsers/multi-space1.html","searchKeys":["multiSpace1","fun multiSpace1(): Parser<String>"]},{"name":"fun newline(): Parser<Utf8CodePoint>","description":"io.yegair.jom.Parsers.newline","location":"jom/io.yegair.jom/-parsers/newline.html","searchKeys":["newline","fun newline(): Parser<Utf8CodePoint>"]},{"name":"fun noneOf(codePoints: String): Parser<Utf8CodePoint>","description":"io.yegair.jom.Parsers.noneOf","location":"jom/io.yegair.jom/-parsers/none-of.html","searchKeys":["noneOf","fun noneOf(codePoints: String): Parser<Utf8CodePoint>"]},{"name":"fun notLineEnding(): Parser<String>","description":"io.yegair.jom.Parsers.notLineEnding","location":"jom/io.yegair.jom/-parsers/not-line-ending.html","searchKeys":["notLineEnding","fun notLineEnding(): Parser<String>"]},{"name":"fun octDigit0(): Parser<String>","description":"io.yegair.jom.Parsers.octDigit0","location":"jom/io.yegair.jom/-parsers/oct-digit0.html","searchKeys":["octDigit0","fun octDigit0(): Parser<String>"]},{"name":"fun octDigit1(): Parser<String>","description":"io.yegair.jom.Parsers.octDigit1","location":"jom/io.yegair.jom/-parsers/oct-digit1.html","searchKeys":["octDigit1","fun octDigit1(): Parser<String>"]},{"name":"fun oneOf(codePoints: String): Parser<Utf8CodePoint>","description":"io.yegair.jom.Parsers.oneOf","location":"jom/io.yegair.jom/-parsers/one-of.html","searchKeys":["oneOf","fun oneOf(codePoints: String): Parser<Utf8CodePoint>"]},{"name":"fun satisfy(predicate: (Utf8CodePoint) -> Boolean): Parser<Utf8CodePoint>","description":"io.yegair.jom.Parsers.satisfy","location":"jom/io.yegair.jom/-parsers/satisfy.html","searchKeys":["satisfy","fun satisfy(predicate: (Utf8CodePoint) -> Boolean): Parser<Utf8CodePoint>"]},{"name":"fun satisfy0(predicate: (Utf8CodePoint, Int) -> Boolean): Parser<String>","description":"io.yegair.jom.Parsers.satisfy0","location":"jom/io.yegair.jom/-parsers/satisfy0.html","searchKeys":["satisfy0","fun satisfy0(predicate: (Utf8CodePoint, Int) -> Boolean): Parser<String>"]},{"name":"fun satisfy1(predicate: (Utf8CodePoint, Int) -> Boolean): Parser<String>","description":"io.yegair.jom.Parsers.satisfy1","location":"jom/io.yegair.jom/-parsers/satisfy1.html","searchKeys":["satisfy1","fun satisfy1(predicate: (Utf8CodePoint, Int) -> Boolean): Parser<String>"]},{"name":"fun space0(): Parser<String>","description":"io.yegair.jom.Parsers.space0","location":"jom/io.yegair.jom/-parsers/space0.html","searchKeys":["space0","fun space0(): Parser<String>"]},{"name":"fun space1(): Parser<String>","description":"io.yegair.jom.Parsers.space1","location":"jom/io.yegair.jom/-parsers/space1.html","searchKeys":["space1","fun space1(): Parser<String>"]},{"name":"fun tab(): Parser<Utf8CodePoint>","description":"io.yegair.jom.Parsers.tab","location":"jom/io.yegair.jom/-parsers/tab.html","searchKeys":["tab","fun tab(): Parser<Utf8CodePoint>"]},{"name":"fun tag(tag: String): Parser<String>","description":"io.yegair.jom.Parsers.tag","location":"jom/io.yegair.jom/-parsers/tag.html","searchKeys":["tag","fun tag(tag: String): Parser<String>"]},{"name":"fun tagNoCase(tag: String): Parser<String>","description":"io.yegair.jom.Parsers.tagNoCase","location":"jom/io.yegair.jom/-parsers/tag-no-case.html","searchKeys":["tagNoCase","fun tagNoCase(tag: String): Parser<String>"]},{"name":"fun takeBytes(count: Long): Parser<ByteArray>","description":"io.yegair.jom.Parsers.takeBytes","location":"jom/io.yegair.jom/-parsers/take-bytes.html","searchKeys":["takeBytes","fun takeBytes(count: Long): Parser<ByteArray>"]},{"name":"object Utf8CodePoints","description":"io.yegair.jom.Utf8CodePoints","location":"jom/io.yegair.jom/-utf8-code-points/index.html","searchKeys":["Utf8CodePoints","object Utf8CodePoints"]},{"name":"fun Utf8CodePoint?.equalsIgnoreCase(other: Utf8CodePoint?): Boolean","description":"io.yegair.jom.Utf8CodePoints.equalsIgnoreCase","location":"jom/io.yegair.jom/-utf8-code-points/equals-ignore-case.html","searchKeys":["equalsIgnoreCase","fun Utf8CodePoint?.equalsIgnoreCase(other: Utf8CodePoint?): Boolean"]},{"name":"fun Utf8CodePoint.isAlpha(): Boolean","description":"io.yegair.jom.Utf8CodePoints.isAlpha","location":"jom/io.yegair.jom/-utf8-code-points/is-alpha.html","searchKeys":["isAlpha","fun Utf8CodePoint.isAlpha(): Boolean"]},{"name":"fun Utf8CodePoint.isAlphaNumeric(): Boolean","description":"io.yegair.jom.Utf8CodePoints.isAlphaNumeric","location":"jom/io.yegair.jom/-utf8-code-points/is-alpha-numeric.html","searchKeys":["isAlphaNumeric","fun Utf8CodePoint.isAlphaNumeric(): Boolean"]},{"name":"fun Utf8CodePoint.isChar(chr: Char): Boolean","description":"io.yegair.jom.Utf8CodePoints.isChar","location":"jom/io.yegair.jom/-utf8-code-points/is-char.html","searchKeys":["isChar","fun Utf8CodePoint.isChar(chr: Char): Boolean"]},{"name":"fun Utf8CodePoint.isDigit(): Boolean","description":"io.yegair.jom.Utf8CodePoints.isDigit","location":"jom/io.yegair.jom/-utf8-code-points/is-digit.html","searchKeys":["isDigit","fun Utf8CodePoint.isDigit(): Boolean"]},{"name":"fun Utf8CodePoint.isHexDigit(): Boolean","description":"io.yegair.jom.Utf8CodePoints.isHexDigit","location":"jom/io.yegair.jom/-utf8-code-points/is-hex-digit.html","searchKeys":["isHexDigit","fun Utf8CodePoint.isHexDigit(): Boolean"]},{"name":"fun Utf8CodePoint.isLowerCase(): Boolean","description":"io.yegair.jom.Utf8CodePoints.isLowerCase","location":"jom/io.yegair.jom/-utf8-code-points/is-lower-case.html","searchKeys":["isLowerCase","fun Utf8CodePoint.isLowerCase(): Boolean"]},{"name":"fun Utf8CodePoint.isMultiSpace(): Boolean","description":"io.yegair.jom.Utf8CodePoints.isMultiSpace","location":"jom/io.yegair.jom/-utf8-code-points/is-multi-space.html","searchKeys":["isMultiSpace","fun Utf8CodePoint.isMultiSpace(): Boolean"]},{"name":"fun Utf8CodePoint.isOctDigit(): Boolean","description":"io.yegair.jom.Utf8CodePoints.isOctDigit","location":"jom/io.yegair.jom/-utf8-code-points/is-oct-digit.html","searchKeys":["isOctDigit","fun Utf8CodePoint.isOctDigit(): Boolean"]},{"name":"fun Utf8CodePoint.isSpace(): Boolean","description":"io.yegair.jom.Utf8CodePoints.isSpace","location":"jom/io.yegair.jom/-utf8-code-points/is-space.html","searchKeys":["isSpace","fun Utf8CodePoint.isSpace(): Boolean"]},{"name":"fun Utf8CodePoint.isUpperCase(): Boolean","description":"io.yegair.jom.Utf8CodePoints.isUpperCase","location":"jom/io.yegair.jom/-utf8-code-points/is-upper-case.html","searchKeys":["isUpperCase","fun Utf8CodePoint.isUpperCase(): Boolean"]},{"name":"fun String.toUtf8CodePoints(): List<Utf8CodePoint>","description":"io.yegair.jom.Utf8CodePoints.toUtf8CodePoints","location":"jom/io.yegair.jom/-utf8-code-points/to-utf8-code-points.html","searchKeys":["toUtf8CodePoints","fun String.toUtf8CodePoints(): List<Utf8CodePoint>"]},{"name":"fun Utf8CodePoint.utf8(): String","description":"io.yegair.jom.Utf8CodePoints.utf8","location":"jom/io.yegair.jom/-utf8-code-points/utf8.html","searchKeys":["utf8","fun Utf8CodePoint.utf8(): String"]},{"name":"fun Utf8CodePoint.utf8Size(): Int","description":"io.yegair.jom.Utf8CodePoints.utf8Size","location":"jom/io.yegair.jom/-utf8-code-points/utf8-size.html","searchKeys":["utf8Size","fun Utf8CodePoint.utf8Size(): Int"]},{"name":"class ParseResultAssert<O>","description":"io.yegair.jom.test.ParseResultAssert","location":"jom/io.yegair.jom.test/-parse-result-assert/index.html","searchKeys":["ParseResultAssert","class ParseResultAssert<O>"]},{"name":"fun <O> assertThatParseResult(actual: ParseResult<O>?): ParseResultAssert<O>","description":"io.yegair.jom.test.ParseResultAssert.Companion.assertThatParseResult","location":"jom/io.yegair.jom.test/-parse-result-assert/-companion/assert-that-parse-result.html","searchKeys":["assertThatParseResult","fun <O> assertThatParseResult(actual: ParseResult<O>?): ParseResultAssert<O>"]},{"name":"fun hasError(expected: ParseError): ParseResultAssert<O>","description":"io.yegair.jom.test.ParseResultAssert.hasError","location":"jom/io.yegair.jom.test/-parse-result-assert/has-error.html","searchKeys":["hasError","fun hasError(expected: ParseError): ParseResultAssert<O>"]},{"name":"fun hasOutput(expected: O): ParseResultAssert<O>","description":"io.yegair.jom.test.ParseResultAssert.hasOutput","location":"jom/io.yegair.jom.test/-parse-result-assert/has-output.html","searchKeys":["hasOutput","fun hasOutput(expected: O): ParseResultAssert<O>"]},{"name":"val isNotNull: ParseResultAssert<O>","description":"io.yegair.jom.test.ParseResultAssert.isNotNull","location":"jom/io.yegair.jom.test/-parse-result-assert/is-not-null.html","searchKeys":["isNotNull","val isNotNull: ParseResultAssert<O>"]}]