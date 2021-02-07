(window.webpackJsonp=window.webpackJsonp||[]).push([[6],{67:function(e,t,r){"use strict";r.r(t),r.d(t,"frontMatter",(function(){return i})),r.d(t,"metadata",(function(){return s})),r.d(t,"toc",(function(){return p})),r.d(t,"default",(function(){return c}));var n=r(3),a=r(7),o=(r(0),r(86)),i={id:"overview",title:"Overview",slug:"./"},s={unversionedId:"overview",id:"overview",isDocsHomePage:!1,title:"Overview",description:"jom is a parser combinator library inspired by the great Rust library nom.",source:"@site/docs/overview.mdx",slug:"/",permalink:"/jom/docs/",editUrl:"https://github.com/Yegair/jom/edit/main/pages/docs/overview.mdx",version:"current",sidebar:"docs",next:{title:"Installation",permalink:"/jom/docs/getting-started/"}},p=[{value:"Motivation",id:"motivation",children:[]},{value:"When to use jom",id:"when-to-use-jom",children:[]},{value:"What does a jom parser look like?",id:"what-does-a-jom-parser-look-like",children:[]}],l={toc:p};function c(e){var t=e.components,r=Object(a.a)(e,["components"]);return Object(o.b)("wrapper",Object(n.a)({},l,r,{components:t,mdxType:"MDXLayout"}),Object(o.b)("p",null,Object(o.b)("em",{parentName:"p"},"jom")," is a parser combinator library inspired by the great Rust library ",Object(o.b)("a",Object(n.a)({parentName:"p"},{href:"https://github.com/Geal/nom"}),"nom"),"."),Object(o.b)("h2",{id:"motivation"},"Motivation"),Object(o.b)("p",null,"When I learn new programming languages I always need to do so in the scope of some little project,\nso I can get a glimpse of what it would be like to use the language for building software at work.\nA year ago I was trying out ",Object(o.b)("a",Object(n.a)({parentName:"p"},{href:"https://www.rust-lang.org/"}),"Rust")," and I decided to write a parser for a custom language,\nthat I had wanted to implement for a while but never got to it."),Object(o.b)("p",null,"Coming from the Java/JVM world I started to look for parser generators like ",Object(o.b)("a",Object(n.a)({parentName:"p"},{href:"https://www.antlr.org/"}),"antlr"),",\nbecause I liked the idea of just putting a grammar in and getting a fully working parser out.\nAfter trying out several parser generators for Rust, including antlr but also others,\nI was really disappointed by the developer experience.\nI already admired Rust (and Cargo) for its exceptionally pleasing developer experience,\nbut those parser generator frameworks introduced a complexity that just felt wrong.\nSo I thought there must be a way of writing a parser without sacrificing the great developer experience,\nthat is provided by Rust, and I was right.\nAs soon as I started to no longer look explicitly for parser generators,\nI immediately found ",Object(o.b)("a",Object(n.a)({parentName:"p"},{href:"https://github.com/Geal/nom"}),"nom"),",\na parser combinator framework for Rust.\nThis was a game changer."),Object(o.b)("p",null,"nom provides you with a set of low level parsers, and some higher order combinators,\nall of which are just simple Rust functions.\nYou can build almost any complex parser that you need by just combining the existing parsers."),Object(o.b)("p",null,"When I came back from Rust to my good ol\u2019 JVM (Java, Kotlin, \u2026),\nI really missed such a simple yet powerful library for writing parsers."),Object(o.b)("p",null,"For example, I write regular expressions for validating/recognizing some string input on a regular basis,\nand I always have to write 10-20 lines of comment just to explain to my coworkers how the regex is intended to work."),Object(o.b)("blockquote",null,Object(o.b)("p",{parentName:"blockquote"},"At least I think one should always extensively document regular expressions,\nbecause communicating intent to other developers through regex implementations is not that easy.")),Object(o.b)("p",null,"With a parser combinator like nom I could do the same thing,\nbut the code which does the parsing/recognition would be maintainable and comprehensible."),Object(o.b)("p",null,"I had a look at two seemingly promising parser combinator libraries,\nnamely ",Object(o.b)("a",Object(n.a)({parentName:"p"},{href:"https://github.com/jparsec/jparsec"}),"jparsec"),"\nand ",Object(o.b)("a",Object(n.a)({parentName:"p"},{href:"https://github.com/typemeta/funcj/tree/master/parser"}),"funcj.parser"),",\nbut at least in my opinion they both have way to bloated and unintuitive APIs."),Object(o.b)("p",null,"So I decided to just port nom to the JVM and call the result jom."),Object(o.b)("h2",{id:"when-to-use-jom"},"When to use jom"),Object(o.b)("p",null,Object(o.b)("em",{parentName:"p"},"jom")," was implemented in order to quickly prototype parsers for custom languages.\nIt is not (yet) recommended to use ",Object(o.b)("em",{parentName:"p"},"jom")," in production, because of its young age.\nThis recommendation will change in the future, when ",Object(o.b)("em",{parentName:"p"},"jom")," has matured.\nHowever, for very small use cases such as replacing a regular expression with a ",Object(o.b)("em",{parentName:"p"},"jom")," parser,\nit can be used without problems."),Object(o.b)("h2",{id:"what-does-a-jom-parser-look-like"},"What does a jom parser look like?"),Object(o.b)("p",null,"As an example for a simple parser, lets suppose we have some ",Object(o.b)("em",{parentName:"p"},"camelCase")," formatted text,\nfor example ",Object(o.b)("inlineCode",{parentName:"p"},'"fooBarBaz"'),"\nWe want to parse this text and extract the segments ",Object(o.b)("inlineCode",{parentName:"p"},'["foo", "Bar", "Baz"]')," as a list."),Object(o.b)("pre",null,Object(o.b)("code",Object(n.a)({parentName:"pre"},{className:"language-kotlin"}),'import io.yegair.jom.Combinators.many0\nimport io.yegair.jom.Combinators.map\nimport io.yegair.jom.Combinators.pair\nimport io.yegair.jom.Input\nimport io.yegair.jom.Parsers.satisfy1\nimport io.yegair.jom.Utf8CodePoints.isLowerCase\nimport io.yegair.jom.Utf8CodePoints.isUpperCase\nimport kotlin.system.exitProcess\n\nval camelCase =\n  // map applies a single parser and then maps its output\n  // by applying a custom function\n  map(\n    // pair applies two parsers in sequence and returns\n    // their result as a Kotlin Pair\n    pair(\n      // satisfy1 reads one or more UTF-8 code points that match\n      // a given predicate and returns a string containing all\n      // matched code points\n      satisfy1 { cp, _ -> cp.isLowerCase() },\n      // many0 applies a parser zero or more times until it no longer matches\n      // it then returns a list containing all results in order\n      many0(\n        // the second parameter given to satisfy1 is the index\n        // of the codepoint within the current parser invocation\n        satisfy1 { cp, index ->\n          when (index) {\n            0 -> cp.isUpperCase()\n            else -> cp.isLowerCase()\n          }\n        }\n      )\n    )\n  ) { (first, rest) ->\n    // first is the result of the first satisfy1 parser\n    // rest is the result of the many0 parser\n    listOf(first) + rest\n  }\n\nfun main() {\n  // Input can be created from various sources,\n  // for example Input.of(System.`in`) or Input.of(byteArrayOf(...))\n  val input = Input.of("fooBarBaz")\n  val result = camelCaseParser.parse(input)\n\n  when {\n    // result.ok indicates whether the parser was able to recognize the given input.\n    // If result.ok is true, result.output holds the produced value.\n    // Given the input "fooBarBaz", this will print ["foo", "Bar", "Baz"]\n    result.ok -> print(result.output)\n    else -> exitProcess(1)\n  }\n}\n')),Object(o.b)("p",null,"The ",Object(o.b)("inlineCode",{parentName:"p"},"camelCase")," parser already uses all features that ",Object(o.b)("em",{parentName:"p"},"jom")," provides.\nFrom here on the only thing that is to be explored are the numerous existing parsers and combinators,\nthat are contained in the ",Object(o.b)("em",{parentName:"p"},"jom")," library."))}c.isMDXComponent=!0},86:function(e,t,r){"use strict";r.d(t,"a",(function(){return u})),r.d(t,"b",(function(){return d}));var n=r(0),a=r.n(n);function o(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function i(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function s(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?i(Object(r),!0).forEach((function(t){o(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):i(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function p(e,t){if(null==e)return{};var r,n,a=function(e,t){if(null==e)return{};var r,n,a={},o=Object.keys(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||(a[r]=e[r]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var l=a.a.createContext({}),c=function(e){var t=a.a.useContext(l),r=t;return e&&(r="function"==typeof e?e(t):s(s({},t),e)),r},u=function(e){var t=c(e.components);return a.a.createElement(l.Provider,{value:t},e.children)},m={inlineCode:"code",wrapper:function(e){var t=e.children;return a.a.createElement(a.a.Fragment,{},t)}},b=a.a.forwardRef((function(e,t){var r=e.components,n=e.mdxType,o=e.originalType,i=e.parentName,l=p(e,["components","mdxType","originalType","parentName"]),u=c(r),b=n,d=u["".concat(i,".").concat(b)]||u[b]||m[b]||o;return r?a.a.createElement(d,s(s({ref:t},l),{},{components:r})):a.a.createElement(d,s({ref:t},l))}));function d(e,t){var r=arguments,n=t&&t.mdxType;if("string"==typeof e||n){var o=r.length,i=new Array(o);i[0]=b;var s={};for(var p in t)hasOwnProperty.call(t,p)&&(s[p]=t[p]);s.originalType=e,s.mdxType="string"==typeof e?e:n,i[1]=s;for(var l=2;l<o;l++)i[l]=r[l];return a.a.createElement.apply(null,i)}return a.a.createElement.apply(null,r)}b.displayName="MDXCreateElement"}}]);